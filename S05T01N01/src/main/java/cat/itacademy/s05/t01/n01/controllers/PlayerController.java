package cat.itacademy.s05.t01.n01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
public class PlayerController {

	@Autowired
	PlayerService playerService;

	@PutMapping("/{playerId}")
	public Mono<ResponseEntity<Player>> setNewPlayerName(@PathVariable int playerId,
			@RequestBody String inputPlayerName) {
		if (inputPlayerName == null || inputPlayerName.trim().isEmpty()) {
			Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
		}

		return playerService.changePlayerName(playerId, inputPlayerName)
				.map(player -> ResponseEntity.status(HttpStatus.OK).body(player))
				.onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
	}

}
