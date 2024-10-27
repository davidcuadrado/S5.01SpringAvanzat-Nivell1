package cat.itacademy.s05.t01.n01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Tag(name = "Player", description = "the Player API")
@RestController
@RequestMapping("/player")
public class PlayerController {

	@Autowired
	PlayerService playerService;

	@Operation(summary = "Change player name", description = "Modify an existing player's name introducing its ID and new name. ")
	@PutMapping("/{playerId}")
	public Mono<ResponseEntity<Player>> setNewPlayerName(@PathVariable int playerId, @RequestBody String playerName) {
		return playerService.changePlayerName(Mono.just(playerId), Mono.just(playerName))
				.map(updatedPlayer -> ResponseEntity.ok(updatedPlayer))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));

	}

}
