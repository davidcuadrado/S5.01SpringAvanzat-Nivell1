package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {

	@Id
	private final String gameId;
	private Deck deck;
	private Player player;
	private Player dealer;
	private int currentBid;
	private int initialBudget;
	private int highestBudget;

	public Game(String playerName) {
		this.deck = new Deck();
		this.player = new Player(playerName);
		this.gameId = setGameId(this.player.getPlayerId());
		this.dealer = new Player("Dealer");
		this.setInitialBudget(1000);
	}

	public Deck getDeck() {
		return deck;
	}

	public Player getPlayer() {
		return player;
	}

	public Player getDealer() {
		return dealer;
	}

	private String setGameId(int playerId) {
		return playerId + "-" + System.currentTimeMillis();
	}

	public int getCurrentBid() {
		return currentBid;
	}

	public void setCurrentBid(int currentBid) {
		this.currentBid = currentBid;
	}

	public int getInitialBudget() {
		return initialBudget;
	}

	public void setInitialBudget(int initialBudget) {
		this.initialBudget = initialBudget;
	}

	public int getHighestBudget() {
		return highestBudget;
	}

	public void setHighestBudget(int currentBudget) {
		if (currentBudget > highestBudget) {
			this.highestBudget = currentBudget;
		}
	}

}
