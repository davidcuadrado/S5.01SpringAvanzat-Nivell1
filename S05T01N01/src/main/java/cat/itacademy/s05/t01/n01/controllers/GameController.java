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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.services.GameService;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameService gameService;
	@Autowired
	private PlayerService playerService;

	@PostMapping("/new")
	public Mono<ResponseEntity<Game>> createNewGame(@RequestBody Player newPlayer) {
	    return playerService.createNewPlayer(newPlayer.getPlayerName())
	        .flatMap(player -> gameService.createNewGame(newPlayer.getPlayerName())
	        .map(newGame -> ResponseEntity.status(HttpStatus.CREATED).body(newGame))
	        .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())));
	}


	@GetMapping("/{id}")
	public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable("id") String gameId) {
		return gameService.getGameById(gameId).map(game -> ResponseEntity.status(HttpStatus.OK).body(game))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
	}

	@PostMapping("/{id}/play")
	public Mono<ResponseEntity<Game>> makePlay(@PathVariable("id") String gameId, @RequestParam String playType,
			@RequestParam int bid) {
		return gameService.nextPlayType(gameId, playType, bid)
				.map(game -> ResponseEntity.status(HttpStatus.OK).body(game))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
	}

	@DeleteMapping("/delete/{id}")
	public Mono<ResponseEntity<String>> deleteGame(@PathVariable("id") String gameId) {
		return gameService.deleteGameById(gameId)
				.map(deleteGame -> ResponseEntity.status(HttpStatus.NO_CONTENT)
						.body("Game " + gameId + " deleted succesfully"))
				.defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game " + gameId + " not found. "));

	}

}
