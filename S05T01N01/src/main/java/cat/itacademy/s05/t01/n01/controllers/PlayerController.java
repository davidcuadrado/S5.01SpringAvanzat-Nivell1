package cat.itacademy.s05.t01.n01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ranking")
public class PlayerController {

	@Autowired
	PlayerService playerService;

	@GetMapping()
	public Flux<ResponseEntity<Player>> getRanking() {
		return playerService.getAllPlayersByRanking().map(player -> ResponseEntity.status(HttpStatus.OK).body(player))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

}