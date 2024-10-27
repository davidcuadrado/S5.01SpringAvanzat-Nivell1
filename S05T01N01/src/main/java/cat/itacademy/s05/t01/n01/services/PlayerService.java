package cat.itacademy.s05.t01.n01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepository;

	public Mono<Player> savePlayer(Mono<Player> player) {
		return player.flatMap(playerUpdate -> playerRepository.save(playerUpdate));
	}

	public Mono<Player> createNewPlayer(Mono<String> playerName) {
		return playerName.flatMap(player -> playerRepository.save(new Player(player))).doOnError(e -> {
			System.err.println("Error while saving the player: " + e.getMessage());
		});
	}

	public Flux<Player> getAllPlayersByRanking() {
		return playerRepository.findAll().sort(
				(player1, player2) -> player2.getPlayerMaxPointsSync().compareTo(player1.getPlayerMaxPointsSync()));
	}

	public Mono<Player> changePlayerName(Mono<Integer> playerId, Mono<String> inputPlayer) {
		return playerId.flatMap(id -> playerRepository.findById(id)).flatMap(player -> inputPlayer.flatMap(name -> {
			player.setPlayerName(name);
			return playerRepository.save(player);
		}).switchIfEmpty(Mono.error(new IllegalArgumentException("Player with ID: " + playerId + " not found"))));
	}

	public Mono<Player> updatePlayerMaxPoints(Mono<Integer> playerId, Mono<Integer> inputPlayerMaxPoints) {
		return playerId.flatMap(id -> playerRepository.findById(id))
				.flatMap(player -> inputPlayerMaxPoints.flatMap(points -> {
					player.setPlayerMaxPoints(points);
					return playerRepository.save(player);
				}).switchIfEmpty(
						Mono.error(new IllegalArgumentException("Player with ID: " + playerId + " not found"))));
	}

}
