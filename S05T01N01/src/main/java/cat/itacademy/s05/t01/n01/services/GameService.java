package cat.itacademy.s05.t01.n01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import reactor.core.publisher.Mono;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;

	public Mono<Game> createNewGame(String player) {
		return gameRepository.save(new Game(player))
				.doOnError(e -> System.out.println("Error while saving the game: " + e.getMessage()));
	}

	public Mono<Game> getGameById(String gameId) {
		return gameRepository.findById(gameId)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<Game> nextPlayType(String gameId, String playType, int bid) {
		return gameRepository.findById(gameId).flatMap(game -> {
			return gameRepository.save(game);
		}).switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<Game> deleteGameById(String gameId) {
		return gameRepository.findById(gameId)
				.flatMap(existingGame -> gameRepository.delete(existingGame).then(Mono.just(existingGame)))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}
	
	
	/*
	public Mono<Void> dealInitialCards(Game game) {
		return Mono.fromRunnable(() -> {
			game.getPlayer().receiveCard(game.getDeck().drawCard());
			game.getPlayer().receiveCard(game.getDeck().drawCard());
			game.getDealer().receiveCard(game.getDeck().drawCard());
		});
	}

	public Mono<String> playerTurn(Game game) {
		return game.getPlayer().isBlackjack().flatMap(isBlackjack -> {
			if (isBlackjack) {
				return Mono.just("Blackjack!");
			}
			return game.getPlayer().isBust().flatMap(isBust -> {
				if (isBust) {
					return Mono.just("Bust!");
				}
				return Mono.just("Your turn");
			});
		});
	}

	public Mono<Void> dealerTurn(Game game) {
		return Mono.defer(() -> game.getDealer().getScore().flatMap(score -> {
			if (score < 17) {
				return game.getDealer().receiveCard(game.getDeck().drawCard()).then(Mono.just(score));
			}
			return Mono.just(score);
		})).repeatWhen(scoreMono -> scoreMono.filter(score -> score < 17)).then();
	}

	public Mono<String> checkWinner(Game game) {
		return game.getPlayer().isBust().flatMap(isPlayerBust -> {
			if (isPlayerBust) {
				return Mono.just("Dealer wins.");
			}
			return game.getDealer().isBust().flatMap(isDealerBust -> {
				if (isDealerBust) {
					return Mono.just("Player wins.");
				}
				return game.getPlayer().getScore().zipWith(game.getDealer().getScore()).flatMap(tuple -> {
					int playerScore = tuple.getT1();
					int dealerScore = tuple.getT2();
					if (playerScore > dealerScore) {
						return Mono.just("Player wins.");
					} else if (playerScore < dealerScore) {
						return Mono.just("Dealer wins.");
					} else {
						return Mono.just("Draw.");
					}
				});
			});
		});
	}
	*/

}
