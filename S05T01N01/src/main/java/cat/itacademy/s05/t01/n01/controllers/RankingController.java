package cat.itacademy.s05.t01.n01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ranking")
public class RankingController {

	@Autowired
	PlayerService playerService;

	@GetMapping()
	public Flux<Player> getRanking() {
		return playerService.getAllPlayersByRanking()
				.switchIfEmpty(Flux.error(new IllegalArgumentException("No players found in ranking.")));
	}

}
