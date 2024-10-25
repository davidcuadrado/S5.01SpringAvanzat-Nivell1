package cat.itacademy.s05.t01.n01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import reactor.core.publisher.Mono;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;

	public Mono<Game> createNewGame(Mono<Player> savedPlayer) {
		return savedPlayer.flatMap(player -> gameRepository.save(new Game(player)))
				.doOnError(e -> System.out.println("Error while saving the game: " + e.getMessage()));
	}

	public Mono<Game> getGameById(Mono<String> gameId) {
		return gameId.flatMap(id -> gameRepository.findById(id))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<Game> nextPlayType(Mono<String> gameId, Mono<String> playType) {
		return gameRepository.findById(gameId).flatMap(game -> {
			return gameRepository.save(game);
		}).switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<Game> deleteGameById(Mono<String> gameId) {
		return gameRepository.findById(gameId)
				.flatMap(existingGame -> gameRepository.delete(existingGame).then(Mono.just(existingGame)))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}
	
	

}
