package cat.itacademy.s05.t01.n01.services;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cat.itacademy.s05.t01.n01.models.Card;
import cat.itacademy.s05.t01.n01.models.Player;
import cat.itacademy.s05.t01.n01.repositories.PlayerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

	@Autowired
	private PlayerRepository playerRepository;
	
	public Mono<Player> createNewPlayer(String player) {
		return playerRepository.save(new Player(player))
				.doOnError(e -> System.out.println("Error while saving the game: " + e.getMessage()));
	}
	

	public Flux<Player> getAllPlayersByRanking() {
		return playerRepository.findAll()
				.sort(Comparator.comparing(Player::getPlayerMaxPointsSync));
	}

	public Mono<Player> changePlayerName(int playerId, String inputPlayerName) {
		return playerRepository.findById(playerId).flatMap(player -> {
			player.setPlayerNameMono(inputPlayerName);
			return playerRepository.save(player);
		}).switchIfEmpty(Mono.error(new IllegalArgumentException("Player with ID: " + playerId + " not found")));
	}
	

    public Mono<Void> addCardToPlayer(int playerId, Card card) {
        return playerRepository.findById(playerId)
            .flatMap(player -> player.receiveCard(card))
            .then();
    }

    public Mono<Integer> getPlayerScore(int playerId) {
        return playerRepository.findById(playerId)
            .flatMap(Player::getScore);
    }

    public Mono<Boolean> checkBlackjack(int playerId) {
        return playerRepository.findById(playerId)
            .flatMap(Player::isBlackjack);
    }

    public Mono<Boolean> checkIfBust(int playerId) {
        return playerRepository.findById(playerId)
            .flatMap(Player::isBust);
    }

    public Mono<Player> getPlayerHand(int playerId) {
        return playerRepository.findById(playerId)
            .flatMap(player -> player.getHand().thenReturn(player));
    }

    public Mono<Player> savePlayer(Player player) {
        return playerRepository.save(player);
    }

}
