package cat.itacademy.s05.t01.n01.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cat.itacademy.s05.t01.n01.models.Game;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.GameRepository;
import reactor.core.publisher.Mono;

@Service
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	private final Game game;
	
	public GameService(Game game){
		this.game = game;
	}
	

	public Mono<Game> createNewGame(Player player) {
		return gameRepository.save(new Game(player.getPlayerName()));

	}

	private String generateGameId(int playerId) {
		return playerId + "-" + System.currentTimeMillis();
	}

	public Mono<Game> getGameById(String gameId) {
		return gameRepository.findById(gameId)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	public Mono<Game> nextPlayType(String gameId, String playType, int bid) {
		return gameRepository.findById(gameId).flatMap(game -> {
			game.setCurrentBid(bid);
			game.setPlayType(playType);
			return gameRepository.save(game);
		}).switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}

	@Transactional
	public Mono<Game> deleteGameById(String gameId) {
		return gameRepository.findById(gameId)
				.flatMap(existingGame -> gameRepository.delete(existingGame).then(Mono.just(existingGame)))
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Game ID: " + gameId + " not found.")));

	}
	
	public Mono<Void> dealInitialCards() {
        return Mono.fromRunnable(() -> {
            game.getPlayer().receiveCard(game.getDeck().drawCard());
            game.getPlayer().receiveCard(game.getDeck().drawCard());
            game.getDealer().receiveCard(game.getDeck().drawCard());
        });
    }

    public Mono<String> playerTurn() {
        return Mono.fromCallable(() -> {
            if (game.getPlayer().isBlackjack()) {
                return "Blackjack!";
            } else if (game.getPlayer().isBust()) {
                return "Bust!";
            }
            return "Your turn";
        });
    }

    public Mono<Void> dealerTurn() {
        return Mono.fromRunnable(() -> {
            while (game.getDealer().getScore() < 17) {
                game.getDealer().receiveCard(game.getDeck().drawCard());
            }
        });
    }

    public Mono<String> checkWinner() {
        return Mono.fromCallable(() -> {
            if (game.getPlayer().isBust()) {
                return "Dealer wins.";
            } else if (game.getDealer().isBust()) {
                return "Player wins.";
            } else if (game.getPlayer().getScore() > game.getDealer().getScore()) {
                return "Player wins.";
            } else if (game.getPlayer().getScore() < game.getDealer().getScore()) {
                return "Dealer wins.";
            } else {
                return "Draw.";
            }
        });
    }

}
