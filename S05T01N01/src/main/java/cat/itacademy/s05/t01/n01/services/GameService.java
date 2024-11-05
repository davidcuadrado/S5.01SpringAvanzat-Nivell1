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
				.switchIfEmpty(Mono.error(new NotFoundException("Game ID: " + gameId + " not found.")))
				.flatMap(game -> playType.flatMap(type -> {

					if (game.getIsRunning() == true) {
						switch (type) {
						case "start" -> Mono.error(new IllegalArgumentException("Game is already running"));
						case "hit" -> playerHit(Mono.just(game));
						case "stand" -> playerStand(Mono.just(game));
						case "close" -> {
							game.setIsRunning(false);
							gameClose(Mono.just(game));
						}

						default -> Mono.error(new BadRequestException("Invalid play type input. "));
						}
					} else if (game.getIsRunning() == false && type.equalsIgnoreCase("start")) {
						game.setIsRunning(true);
						startGame(Mono.just(game));

					} else {
						Mono.error(new BadRequestException("Game has not been started. Start the game to play. "));
					}

					return gameRepository.save(game);
				})).onErrorMap(e -> new DatabaseException("Unable to save progress. "));
	}

	public Mono<Game> startGame(Mono<Game> gameMono) {
	    return gameMono.flatMap(game -> {
	        Mono<List<Card>> playerCards = game.getPlayerHand().addCard(game.getDeck().drawCard())
	                .then(game.getPlayerHand().addCard(game.getDeck().drawCard()));
	        Mono<List<Card>> dealerCards = game.getDealerHand().addCard(game.getDeck().drawCard())
	                .then(game.getDealerHand().addCard(game.getDeck().drawCard()));
	        
	        return Mono.when(playerCards, dealerCards)
	            .then(Mono.defer(() -> checkForBlackjack(Mono.just(game))))
	            .flatMap(gameRepository::save);
	    });
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
		return gameMono.flatMap(game -> {
			game.getPlayerHand().addCard(game.getDeck().drawCard());

			if (game.getPlayerHand().getScore() > 21) {
				game.setLastResult("Player busts with " + game.getPlayerHand().getScore() + "! Dealer wins the round");
				game.setIsRunning(false);
				return gameRepository.save(game);
			}

			return gameRepository.save(game);
		});
	}

	public Mono<Game> playerStand(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> {

			while (game.getDealerHand().getScore() < 17) {
				game.getDealerHand().addCard(game.getDeck().drawCard());
			}

			if (game.getDealerHand().getScore() > 21) {
				game.setLastResult("Dealer busts with " + game.getDealerHand().getScore() + "! Player wins the round.");
				game.setCurrentPoints(game.getCurrentPoints() + 100);
				game.setIsRunning(false);
			} else if (game.getDealerHand().getScore() > game.getPlayerHand().getScore()) {
				game.setLastResult("Dealer wins with " + +game.getDealerHand().getScore() + " against player's "
						+ game.getPlayerHand().getScore() + " hand. ");
				game.setCurrentPoints(game.getCurrentPoints() - 100);
				game.setIsRunning(false);
			} else if (game.getDealerHand().getScore() == game.getPlayerHand().getScore()) {
				game.setLastResult("It's a tie! " + "Both hands are " + game.getPlayerHand().getScore());
				game.setIsRunning(false);
			} else {
				game.setLastResult("Player wins with " + +game.getPlayerHand().getScore() + " against dealer's "
						+ game.getDealerHand().getScore() + " hand. ");
				game.setIsRunning(false);
			}

			return gameRepository.save(game);
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
