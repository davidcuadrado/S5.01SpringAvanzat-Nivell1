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

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.services.GameService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameService gameService;


	@PostMapping("/new")
	public Mono<ResponseEntity<Game>> createNewGame(@RequestBody Mono<Player> playerMono) {
	    return playerMono.flatMap(player -> gameService.createNewGame(player))
	            .map(newGame -> ResponseEntity.status(HttpStatus.CREATED).body(newGame));
	}
	
	
	

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable("id") String gameId) {
	    return gameService.getGameById(gameId)
	            .map(game -> ResponseEntity.status(HttpStatus.OK).body(game))
	            .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found"))); 
	}
	
	
	
	/*
	public Mono<ResponseEntity<Game>> getGameDetails(@PathVariable String gameId) {
		return gameService.getGameById(gameId).map(game -> ResponseEntity.status(HttpStatus.OK).body(game));
	}
	*/

	@PostMapping("/{id}/play")
	public Mono<ResponseEntity<Game>> makePlay(@PathVariable String gameId, String playType, int bid) {
		// PLACERHOLDER
		return gameService.nextPlayType(gameId, playType, bid)
				.map(game -> ResponseEntity.status(HttpStatus.OK).body(game));
		// desarrollar
	}

	@DeleteMapping
	public Mono<ResponseEntity<String>> deleteGame(@PathVariable String gameId) {
		return gameService.deleteGameById(gameId)
				.map(deleteGame -> ResponseEntity.status(HttpStatus.NO_CONTENT).body("Game " + gameId + " deleted succesfully"))
				.defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game " + gameId + " not found. "));

	}

}
