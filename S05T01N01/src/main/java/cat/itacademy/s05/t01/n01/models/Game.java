package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "game")
public class Game {

	@Id
	private String gameId;
	private Deck deck;
	private Player player;
	private Player dealer;
	private int currentBid;
	private int initialBudget;
	private int highestBudget;
	private int gameRounds;

	public Game(String player) {
		this.deck = new Deck();
		this.player = new Player(player);
		this.dealer = new Player("Dealer");
		this.setInitialBudget(1000);
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
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

	public int getGameRounds() {
		return gameRounds;
	}

	public void setGameRounds(int gameRounds) {
		this.gameRounds = gameRounds;
	}

}
