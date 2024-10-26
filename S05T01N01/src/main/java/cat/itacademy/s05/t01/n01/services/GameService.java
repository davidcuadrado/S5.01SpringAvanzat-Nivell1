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
	@Autowired
	private PlayerService playerService;

	public Mono<Game> createNewGame(Mono<Player> savedPlayer) {
		return savedPlayer.flatMap(player -> gameRepository.save(new Game(player)))
				.doOnError(e -> System.out.println("Error while saving the player: " + e.getMessage()));
	}

	public Mono<Game> getGameById(Mono<String> gameId) {
		return gameId.flatMap(id -> gameRepository.findById(id))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<Game> nextPlayType(Mono<String> gameId, Mono<String> playType) {
		return gameId.flatMap(id -> gameRepository.findById(id)).flatMap(game -> playType.flatMap(type -> {
			if (game.getIsRunning() == true) {
				switch (type) {
				case "start" -> startGame();
				case "hit" -> playerHit(Mono.just(game));
				case "stand" -> playerStand(Mono.just(game));
				case "close" -> gameClose(Mono.just(game));

				default -> Mono.error(new IllegalArgumentException("Invalid play type input. "));
				}
			} else if (game.getIsRunning() == false && type.equalsIgnoreCase("start")) {
				game.setIsRunning(true);
				startGame();

			} else {
				Mono.error(new IllegalArgumentException("Invalid play type input. "));
			}

			return gameRepository.save(game);
		})).switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<Game> deleteGameById(Mono<String> gameId) {
		return gameRepository.findById(gameId)
				.flatMap(existingGame -> gameRepository.delete(existingGame).then(Mono.just(existingGame)))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<String> startGame() {
		return Mono.just("Game is ready to start. ");

	}

	public Mono<Game> playerHit(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> {
			game.getPlayerHand().addCard(game.getDeck().drawCard());

			if (game.getPlayerHand().getScore() > 21) {
				game.setIsRunning(false);
				game.setLastResult("Player busts with " + game.getPlayerHand().getScore() + "! Dealer wins the round");
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
			} else if (game.getDealerHand().getScore() > game.getPlayerHand().getScore()) {
				game.setLastResult("Dealer wins with " + +game.getDealerHand().getScore() + " against player's "
						+ game.getPlayerHand().getScore() + " hand. ");
				game.setCurrentPoints(game.getCurrentPoints() - 100);
			} else if (game.getDealerHand().getScore() == game.getPlayerHand().getScore()) {
				game.setLastResult("It's a tie! " + "Both hands are " + game.getPlayerHand().getScore());
			} else {
				game.setLastResult("Player wins with " + +game.getPlayerHand().getScore() + " against dealer's "
						+ game.getDealerHand().getScore() + " hand. ");
			}

			return gameRepository.save(game);
		});
	}

	public Mono<Game> gameClose(Mono<Game> gameMono) {
		return gameMono.flatMap(game -> {
			if (game.getCurrentPoints() > game.getPlayer().getPlayerMaxPointsSync()) {
				return playerService
						.updatePlayerMaxPoints(game.getPlayer().getPlayerId(), Mono.just(game.getCurrentPoints()))
						.flatMap(updatedPlayer -> {
							game.getPlayer().setPlayerMaxPoints(game.getCurrentPoints());
							game.setIsRunning(false);
							return gameRepository.save(game);
						});
			} else {
				game.setIsRunning(false);
				return gameRepository.save(game);
			}
		});
	}

}
