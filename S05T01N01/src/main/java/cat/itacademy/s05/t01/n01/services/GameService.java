package cat.itacademy.s05.t01.n01.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.itacademy.s05.t01.n01.model.Game;
import cat.itacademy.s05.t01.n01.model.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import reactor.core.publisher.Mono;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;

	public Mono<Game> createNewGame(Player player) {
		return gameRepository.save(new Game(player.getPlayerName(), generateGameId(player.getPlayerId())));

	}

	private String generateGameId(int playerId) {
		return playerId + "-" + System.currentTimeMillis();
	}

	public Mono<Game> getGameById(String gameId) {
		return gameRepository.findById(gameId)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<Game> nextPlayType(String gameId, String playType, int bid) {
		return gameRepository.findById(gameId)
				.flatMap(game -> {
					game.setBid(bid);
					game.setPlayType(playType);
					return gameRepository.save(game);
				})
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));
		
	

	}

	@Transactional
	public Mono<Game> deleteGameById(String gameId) {
		return gameRepository.findById(gameId)
				.flatMap(existingGame -> gameRepository.delete(existingGame).then(Mono.just(existingGame)))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

}
