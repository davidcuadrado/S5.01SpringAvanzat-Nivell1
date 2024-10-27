package cat.itacademy.s05.t01.n01.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;

@Tag(name = "Ranking", description = "the Ranking API")
@RestController
@RequestMapping("/ranking")
public class RankingController {

	@Autowired
	PlayerService playerService;

	@Operation(summary = "Get player ladder", description = "Retrieve all ranked players. ")
	@GetMapping()
	public Flux<Player> getRanking() {
		return playerService.getAllPlayersByRanking()
				.switchIfEmpty(Flux.error(new IllegalArgumentException("No players found in ranking.")));
	}

}
