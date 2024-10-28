package cat.itacademy.s05.t01.n01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t01.n01.exceptions.NotFoundException;
import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.services.GameService;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import cat.itacademy.s05.t01.n01.exceptions.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Tag(name = "Game", description = "the Game API")
@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameService gameService;
	@Autowired
	private PlayerService playerService;

	@Operation(summary = "Create a new game", description = "Prepare a new game after introducing the player name. ")
	@PostMapping("/new")
	public Mono<ResponseEntity<Game>> createNewGame(@RequestBody String newPlayer) {
		return playerService.createNewPlayer(Mono.just(newPlayer))
				.flatMap(player -> gameService.createNewGame(Mono.just(player))
						.map(newGame -> ResponseEntity.status(HttpStatus.CREATED).body(newGame))
						.onErrorMap(e -> new IllegalArgumentException("Error saving new game. ")));
	}

	@Operation(summary = "Search for game details", description = "Retrieve an especific game details via ID from the database. ")
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable("id") String gameId) {
		return gameService.getGameById(Mono.just(gameId)).map(game -> ResponseEntity.status(HttpStatus.OK).body(game))
				.onErrorMap(e -> new NotFoundException("Game with ID: " + gameId + " not found"));
	}

	@Operation(summary = "Play the game", description = "Start playing the game, make your next decision or save your progress. ")
	@PostMapping("/{id}/play")
	public Mono<ResponseEntity<Game>> makePlay(@PathVariable("id") String gameId, @RequestBody String playType) {
		return gameService.nextPlayType(Mono.just(gameId), Mono.just(playType))
				.map(game -> ResponseEntity.status(HttpStatus.OK).body(game))
				.onErrorMap(e -> new BadRequestException("Invalid play type input. "));
	}

	@Operation(summary = "Delete a game", description = "Delete an existing game introducing its game ID. ")
	@DeleteMapping("/{id}/delete")
	public Mono<ResponseEntity<String>> deleteGame(@PathVariable("id") String gameId) {
		return gameService.deleteGameById(Mono.just(gameId))
				.map(deleteGame -> ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body("Game " + gameId + " deleted succesfully"))
				.switchIfEmpty(Mono.error(new NotFoundException("Game with ID: " + gameId + " not found")));

	}

}
