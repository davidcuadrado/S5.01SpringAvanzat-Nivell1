package cat.itacademy.s05.t01.n01.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t01.n01.models.Card;
import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import cat.itacademy.s05.t01.n01.exceptions.*;
import reactor.core.publisher.Mono;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private PlayerService playerService;

	@Autowired
	private PlayerRepository playerRepository;

	public Mono<Game> createNewGame(Mono<Player> savedPlayer) {
		return savedPlayer.flatMap(player -> gameRepository.save(new Game(player)))
				.onErrorMap(e -> new DatabaseException("An error happend while creting new game"));
	}

	public Mono<Game> getGameById(Mono<String> gameId) {
		return gameId.flatMap(id -> gameRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Game ID: " + gameId + " not found.")));
	}

	public Mono<Game> nextPlayType(Mono<String> gameId, Mono<String> playType) {
		return gameId.flatMap(id -> gameRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Game not found")))
				.flatMap(game -> playType.flatMap(type -> {
					if (game.getIsRunning()) {
						return switch (type.toLowerCase()) {
						case "hit" -> playerHit(Mono.just(game));
						case "stand" -> playerStand(Mono.just(game));
						case "close" -> gameClose(Mono.just(game)).doOnNext(g -> g.setIsRunning(false));
						default -> Mono.error(new BadRequestException("Invalid play type"));
						};
					} else if ("start".equalsIgnoreCase(type)) {
						game.setIsRunning(true);
						return gameRepository.save(game);
					} else {
						return Mono.error(new IllegalArgumentException("Game is not running"));
					}
				}));
	}

	public Mono<Game> startGame(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> game.getPlayerHand().getNewPlayerHand(Mono.just(game)).doOnNext(cards -> {
			game.getPlayerHand().setCards(game.getPlayerHand().getCards());
			System.out.println("Cartas en Player Hand antes de guardar: " + cards);
		}).then(game.getDealerHand().getNewPlayerHand(Mono.just(game)).doOnNext(cards -> {
			game.getDealerHand().setCards(game.getDealerHand().getCards());
			System.out.println("Cartas en Dealer Hand antes de guardar: " + cards);
		})).then(Mono.defer(() -> checkForBlackjack(Mono.just(game)))).flatMap(updatedGame -> {
			System.out.println("Game antes de guardar: " + updatedGame);
			return gameRepository.save(updatedGame);
		}));
	}

	private Mono<Game> checkForBlackjack(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> Mono.defer(() -> {
			boolean playerHasBlackjack = game.getPlayerHand().getScore() == 21;
			boolean dealerHasBlackjack = game.getDealerHand().getScore() == 21;

			if (playerHasBlackjack && dealerHasBlackjack) {
				game.setLastResult("Draw: player and dealer both have a BLackjack! ");
				game.setIsRunning(false);
			} else if (playerHasBlackjack) {
				game.setLastResult("Player wins with Blackjack!");
				game.setCurrentPoints(game.getCurrentPoints() + 150);
				game.setIsRunning(false);
			} else if (dealerHasBlackjack) {
				game.setLastResult("Dealer wins with Blackjack.");
				game.setCurrentPoints(game.getCurrentPoints() - 100);
				game.setIsRunning(false);
			} else {
				game.setIsRunning(true);
				game.setLastResult("Select your next action.");
			}

			return Mono.just(game);
		}));
	}

	public Mono<Game> playerHit(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> game.getPlayerHand().addCard(game.getDeck().drawCard()).then(Mono.defer(() -> {
			if (game.getPlayerHand().getScore() > 21) {
				game.setLastResult("Player busts with " + game.getPlayerHand().getScore() + "! Dealer wins the round");
				game.setIsRunning(false);
			}
			return gameRepository.save(game);
		})));
	}

	public Mono<Game> playerStand(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> {
			while (game.getDealerHand().getScore() < 17) {
				game.getDealerHand().addCard(game.getDeck().drawCard()).block();
			}

			Mono<Void> dealerAction = Mono.defer(() -> {
				if (game.getDealerHand().getScore() > 21) {
					game.setLastResult(
							"Dealer busts with " + game.getDealerHand().getScore() + "! Player wins the round.");
					game.setCurrentPoints(game.getCurrentPoints() + 100);
					game.setIsRunning(false);
				} else if (game.getDealerHand().getScore() > game.getPlayerHand().getScore()) {
					game.setLastResult("Dealer wins with " + game.getDealerHand().getScore() + " against player's "
							+ game.getPlayerHand().getScore());
					game.setCurrentPoints(game.getCurrentPoints() - 100);
					game.setIsRunning(false);
				} else if (game.getDealerHand().getScore() == game.getPlayerHand().getScore()) {
					game.setLastResult("It's a tie! Both hands are " + game.getPlayerHand().getScore());
					game.setIsRunning(false);
				} else {
					game.setLastResult("Player wins with " + game.getPlayerHand().getScore() + " against dealer's "
							+ game.getDealerHand().getScore());
					game.setIsRunning(false);
				}
				return Mono.empty();
			});

			return dealerAction.then(gameRepository.save(game));
		});
	}

	public Mono<Game> gameClose(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> {
			if (game.getCurrentPoints() > game.getPlayer().getPlayerMaxPointsSync()) {
				return playerRepository.findById(game.getPlayer().getPlayerId()).flatMap(updatePlayer -> {
					playerService.updatePlayerMaxPoints(game.getPlayer().getPlayerId(),
							Mono.just(game.getCurrentPoints()));
					return gameRepository.save(game);
				});
			}
			game.setIsRunning(false);
			return gameRepository.save(game);

		});
	}

	public Mono<Game> deleteGameById(Mono<String> gameId) {
		return gameId.flatMap(id -> gameRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Game ID: " + gameId + " not found.")))
				.flatMap(existingGame -> gameRepository.delete(existingGame).then(Mono.just(existingGame)));
	}

}
