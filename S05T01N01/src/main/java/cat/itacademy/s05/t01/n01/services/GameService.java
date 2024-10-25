package cat.itacademy.s05.t01.n01.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t01.n01.models.Deck;
import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.models.Hand;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import reactor.core.publisher.Mono;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;

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
				case "start" -> game.setIsRunning(true);
				case "hit" -> playerHit(Mono.just(game));
				case "stand" -> playerStand(Mono.just(game));
				case "close" -> game.setIsRunning(false);
				default -> Mono.error(new IllegalArgumentException("Invalid play type input. "));
				}
			} else if (game.getIsRunning() == false && type.equalsIgnoreCase("start")) {
				game.setIsRunning(true);

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

	public void startGame(Mono<Game> game) {

	}

	public void playerHit(Mono<Game> game) {
		Deck deck = new Deck();
		Hand playerHand = new Hand();
		Hand dealerHand = new Hand();

		playerHand.addCard(deck.drawCard());
		playerHand.addCard(deck.drawCard());
		dealerHand.addCard(deck.drawCard());
	}

	public void playerStand(Mono<Game> game) {
		Deck deck = new Deck();
		Hand playerHand = new Hand();
		Hand dealerHand = new Hand();

		playerHand.addCard(deck.drawCard());
		playerHand.addCard(deck.drawCard());
		dealerHand.addCard(deck.drawCard());
	}

	/*
	 * public Mono<String> playerDrawCard(Mono<Game> game) {
	 * playerHand.addCard(deck.drawCard()); if (playerHand.isBust()) { return
	 * Mono.just("Player busts! Dealer wins."); } return
	 * Mono.just("Player's turn continues."); }
	 * 
	 * public Mono<String> dealerTurn() { while (dealerHand.getScore() < 17) {
	 * dealerHand.addCard(deck.drawCard()); } return checkOutcome(); }
	 * 
	 * private Mono<String> checkOutcome() { if (dealerHand.isBust()) { return
	 * Mono.just("Dealer busts! Player wins."); } else if (playerHand.getScore() >
	 * dealerHand.getScore()) { return Mono.just("Player wins!"); } else if
	 * (playerHand.getScore() == dealerHand.getScore()) { return
	 * Mono.just("It's a tie!"); } else { return Mono.just("Dealer wins."); } }
	 */

}
