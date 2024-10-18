package cat.itacademy.s05.t01.n01.services;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PlayerService {

	@Autowired
	PlayerRepository playerRepository;

	public Flux<Player> getAllPlayersByRanking() {
		return playerRepository.findAll()
				.sort(Comparator.comparing(Player::getPlayerMaxPoints).thenComparing(Player::getGamesPlayed));

	}

	public Mono<Player> changePlayerName(int playerId, String inputPlayerName) {
		return playerRepository.findById(playerId).flatMap(player -> {
			player.setPlayerName(inputPlayerName);
			return playerRepository.save(player);
		}).switchIfEmpty(Mono.error(new IllegalArgumentException("Player with ID: " + playerId + " not found")));
	}

}
