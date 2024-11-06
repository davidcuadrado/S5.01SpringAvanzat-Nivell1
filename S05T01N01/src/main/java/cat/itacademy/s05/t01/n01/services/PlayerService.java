package cat.itacademy.s05.t01.n01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import cat.itacademy.s05.t01.n01.exceptions.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepository;

	public Mono<Player> savePlayer(Mono<Player> player) {
		return player.flatMap(playerUpdate -> playerRepository.save(playerUpdate))
				.onErrorMap(e -> new DatabaseException("An error happend while saving the new player. Please try again. "));
	}

	public Mono<Player> createNewPlayer(Mono<String> playerName) {
		return playerName.flatMap(player -> playerRepository.save(new Player(player)))
				.onErrorMap(e -> new DatabaseException("Error creating new player"));
	}

	public Flux<Player> getAllPlayersByRanking() {
		return playerRepository.findAll()
				.switchIfEmpty(Mono.error(new NotFoundException("No existing players to show. ")))
				.sort((player1, player2) -> player2.getPlayerMaxPointsSync()
						.compareTo(player1.getPlayerMaxPointsSync()))
				.onErrorMap(e -> new DatabaseException("Error retrieving ranked players. "));
	}

	public Mono<Player> changePlayerName(Mono<Integer> playerId, Mono<String> inputPlayer) {
		return playerId.flatMap(id -> playerRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Player with ID: " + playerId + " not found")))
				.flatMap(player -> inputPlayer.flatMap(name -> {
					player.setPlayerName(name);
					return playerRepository.save(player);
				})).onErrorMap(e -> new DatabaseException("Unable to modify name"));
	}

	public Mono<Player> updatePlayerMaxPoints(Mono<Integer> playerId, Mono<Integer> inputPlayerMaxPoints) {
		return playerId.flatMap(id -> playerRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Player with input ID not found")))
				.flatMap(player -> inputPlayerMaxPoints.flatMap(points -> {
					player.setPlayerMaxPoints(points);
					return playerRepository.save(player);
				})).onErrorMap(e -> new DatabaseException("Error updating player max points. "));
	}

}
