package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Getter
@Setter
@NoArgsConstructor
public class Player {

	@Id
	private int playerId;
	private String playerName;
	private int playerMaxPoints;
	private Hand hand;
	private int gamesPlayed;

	public Player(String playerName) {
		this.playerName = playerName;
		this.setGamesPlayed(0);
		this.hand = new Hand();
	}
	
	

	public Mono<String> getPlayerName() {
	    return Mono.just(playerName);
	}

	public Mono<Void> setPlayerName(String playerName) {
	    return Mono.fromRunnable(() -> this.playerName = playerName);
	}

	public Mono<Integer> getPlayerId() {
	    return Mono.just(playerId);
	}

	public Mono<Void> setPlayerId(int playerId) {
	    return Mono.fromRunnable(() -> this.playerId = playerId);
	}

	public Mono<Integer> getPlayerMaxPoints() {
	    return Mono.just(this.playerMaxPoints);
	}
	
	public Integer getPlayerMaxPointsSync() {
		return this.playerMaxPoints;
	}

	public Mono<Void> setPlayerMaxPoints(int playerMaxPoints) {
	    return Mono.fromRunnable(() -> this.playerMaxPoints = playerMaxPoints);
	}

	public Mono<Integer> getGamesPlayed() {
	    return Mono.just(gamesPlayed);
	}

	public Integer getGamesPlayedSync() {
		return this.gamesPlayed;
	}
	
	public Mono<Void> setGamesPlayed(int gamesPlayed) {
	    return Mono.fromRunnable(() -> this.gamesPlayed = gamesPlayed);
	}

    public Mono<Void> receiveCard(Card card) {
        return this.hand.addCard(card);
    }

    public Mono<Integer> getScore() {
        return this.hand.getScore();
    }

    public Mono<Boolean> isBlackjack() {
        return this.hand.isBlackjack();
    }

    public Mono<Boolean> isBust() {
        return this.hand.isBust();
    }

    public Mono<Hand> getHand() {
        return Mono.just(this.hand);
    }


}
