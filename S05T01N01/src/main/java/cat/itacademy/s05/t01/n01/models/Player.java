package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	// revisar constructor con APIs reactivas

	public Player(String playerName) {
		this.playerName = playerName;
		this.setGamesPlayed(0);
		this.hand = new Hand();
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

	// revisar reactividad métodos -> llevar la funcionalidad a Services

	public void receiveCard(Card card) {
		hand.addCard(card);
	}

	public int getScore() {
		return hand.getScore();
	}

	public boolean isBlackjack() {
		return hand.isBlackjack();
	}

	public boolean isBust() {
		return hand.isBust();
	}

	public Hand getHand() {
		this.setGamesPlayed(this.getGamesPlayed() + 1);
		return hand;
	}

}
