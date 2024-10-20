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
	private Mono<Hand> hand;
	private int gamesPlayed;

	public Player(String playerName) {
		this.playerName = playerName;
		this.setGamesPlayed(0);
		this.hand = Mono.just(new Hand());
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getPlayerMaxPoints() {
		return playerMaxPoints;
	}

	public void setPlayerMaxPoints(int playerMaxPoints) {
		this.playerMaxPoints = playerMaxPoints;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}

    public Mono<Void> receiveCard(Card card) {
        return this.hand.flatMap(hand -> {
            hand.addCard(card);
            return Mono.empty();
        });
    }

    public Mono<Integer> getScore() {
        return this.hand.map(Hand::getScore);
    }

    public Mono<Boolean> isBlackjack() {
        return this.hand.map(Hand::isBlackjack);
    }

    public Mono<Boolean> isBust() {
        return this.hand.map(Hand::isBust);
    }

    public Mono<Hand> getHand() {
        this.setGamesPlayed(this.getGamesPlayed() + 1);
        return this.hand;
    }

}
