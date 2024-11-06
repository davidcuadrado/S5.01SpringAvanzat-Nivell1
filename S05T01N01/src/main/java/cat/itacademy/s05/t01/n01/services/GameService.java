package cat.itacademy.s05.t01.n01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private PlayerRepository playerRepository;

	public Mono<Game> createNewGame(Mono<Player> savedPlayer) {
		return savedPlayer.flatMap(player -> gameRepository.save(new Game(player)))
				.onErrorMap(e -> new DatabaseException("An error happend while creating new game"));
	}

	public Mono<Game> getGameById(Mono<String> gameId) {
		return gameId.flatMap(id -> gameRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Game ID: " + gameId + " not found.")));
	}

	public Mono<Game> nextPlayType(Mono<String> gameId, Mono<String> playType) {
		return gameId.flatMap(id -> gameRepository.findById(id).flatMap(game -> playType.flatMap(type -> {
			switch (type.toLowerCase()) {
			case "start":
				if (game.getIsRunning()) {
					return Mono.error(new BadRequestException("Invalid play type. Game is already started. "));
				} else {
					game.setIsRunning(true);
					return startGame(Mono.just(game)).flatMap(gameRepository::save);
				}
			case "hit":
				if (game.getIsRunning()) {
					return playerHit(Mono.just(game)).flatMap(gameRepository::save);
				} else {
					return Mono.error(new BadRequestException(
							"Invalid play type. Game is closed, start the game before making a move. "));
				}
			case "stand":
				if (game.getIsRunning()) {
					return playerStand(Mono.just(game)).flatMap(gameRepository::save);
				} else {
					return Mono.error(new BadRequestException(
							"Invalid play type. Game is close, start the game before making a move.  "));
				}
			case "close":
				game.setIsRunning(false);
				return gameClose(Mono.just(game)).flatMap(gameRepository::save);

			default:
				return Mono
						.error(new IllegalArgumentException("Invalid play type. Try introducing a proper play type."));
			}
		})).switchIfEmpty(Mono.error(new NotFoundException("Game ID: " + id + " not found."))));
	}

	private Mono<Game> startGame(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> {
			if (game.getDeck().getCards().size() < 20) {
				game.setNewDeck();
			}
			Mono<Void> playersCards = startPlayerCards(game).then(startDealerCards(game));

			Mono<Void> dealerCards = startDealerCards(game);

			return Mono.zip(playersCards, dealerCards).then(checkForBlackjackAndSetResult(game))
					.flatMap(gameRepository::save);
		});
	}

	private Mono<Void> startPlayerCards(Game game) {
		return game.getPlayerHand().resetHand()
				.then(game.getPlayerHand().addCard(game.getDeck().drawCard()))
				.then(game.getPlayerHand().addCard(game.getDeck().drawCard()));
	}
	
	private Mono<Void> startDealerCards (Game game){
		return game.getDealerHand().resetHand()
				.then(game.getDealerHand().addCard(game.getDeck().drawCard()))
				.then(game.getDealerHand().addCard(game.getDeck().drawCard()));
	}

	private Mono<Game> checkForBlackjackAndSetResult(Game game) {
		return Mono.defer(() -> {
			boolean playerHasBlackjack = game.getPlayerHand().getScore() == 21;
			boolean dealerHasBlackjack = game.getDealerHand().getScore() == 21;

			if (playerHasBlackjack && dealerHasBlackjack) {
				game.setLastResult("Draw: both have Blackjack! ");
				game.setIsRunning(false);
			} else if (playerHasBlackjack) {
				game.setLastResult("Player wins with Blackjack! ");
				game.setCurrentPoints(game.getCurrentPoints() + 100);
				game.setIsRunning(false);
			} else if (dealerHasBlackjack) {
				game.setLastResult("Dealer wins with Blackjack. ");
				game.setCurrentPoints(game.getCurrentPoints() - 100);
				game.setIsRunning(false);
			} else {
				game.setIsRunning(true);
				game.setLastResult("Game is ready to continue. ");
			}
			

			return gameRepository.save(game);
		});
	}

	private Mono<Game> playerHit(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> game.getPlayerHand().addCard(game.getDeck().drawCard()).then(Mono.defer(() -> {
			if (game.getPlayerHand().getScore() > 21) {
				game.setLastResult("Player busts with " + game.getPlayerHand().getScore() + "! Dealer wins the round");
				game.setIsRunning(false);
				game.updateCurrentPoints(-100);
			}
			return gameRepository.save(game);
		})));
	}

	private Mono<Game> playerStand(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> {
			while (game.getDealerHand().getScore() < 17) {
				game.getDealerHand().addCard(game.getDeck().drawCard()).block();
			}

			Mono<Void> dealerAction = Mono.defer(() -> {
				if (game.getDealerHand().getScore() > 21) {
					game.setLastResult(
							"Dealer busts with " + game.getDealerHand().getScore() + "! Player wins the round.");
					game.updateCurrentPoints(100);
				} else if (game.getDealerHand().getScore() > game.getPlayerHand().getScore()) {
					game.setLastResult("Dealer wins with " + game.getDealerHand().getScore() + " against player's "
							+ game.getPlayerHand().getScore());
					game.updateCurrentPoints(-100);
				} else if (game.getDealerHand().getScore() == game.getPlayerHand().getScore()) {
					game.setLastResult("It's a tie! Both hands are " + game.getPlayerHand().getScore());
				} else {
					game.setLastResult("Player wins with " + game.getPlayerHand().getScore() + " against dealer's "
							+ game.getDealerHand().getScore());
					game.updateCurrentPoints(100);
				}
				game.setIsRunning(false);
				return Mono.empty();
			});

			return dealerAction.then(gameRepository.save(game));
		});
	}

	private Mono<Game> gameClose(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> {
			return playerRepository.findById(game.getPlayer().getPlayerId()).flatMap(player -> {
				if (game.getCurrentPoints() > player.getMaxPoints()) {
					player.setMaxPoints(game.getCurrentPoints());
					return playerRepository.save(player).thenReturn(game);
				}
				return Mono.just(game);
			}).flatMap(updatedGame -> {
				updatedGame.setIsRunning(false);
				return gameRepository.save(updatedGame);
			});
		});
	}

	public Mono<Game> deleteGameById(Mono<String> gameId) {
		return gameId.flatMap(id -> gameRepository.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundException("Game ID: " + gameId + " not found.")))
				.flatMap(existingGame -> gameRepository.delete(existingGame).then(Mono.just(existingGame)));
	}

}
