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
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/player")
public class PlayerController {

	@Autowired
	PlayerService playerService;

	@PutMapping("/{playerId}")
	public Mono<ResponseEntity<Player>> setNewPlayerName(@PathVariable String playerId, @RequestBody Player player) {
	    try {
	        int playerIdInt = Integer.parseInt(playerId);
	        return playerService.changePlayerName(playerIdInt, player.getPlayerName())
	            .map(updatedPlayer -> ResponseEntity.ok(updatedPlayer))
	            .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
	    } catch (NumberFormatException e) {
	        return Mono.just(ResponseEntity.badRequest().build());
	    }
	}

}
