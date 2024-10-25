package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "game")
public class Game {

	@Id
	private String gameId;
	private Deck deck;
	private Player player;
	private Hand playerHand;
	private Player dealer;
	private Hand dealerHand;
	private int gameRounds;
	private boolean isRunning;
	
	
	

	public Game() {
		this.deck = new Deck();
		this.dealer = new Player("Dealer");
	}

	public Game(Player player) {
		this.deck = new Deck();
		this.player = player;
		this.playerHand = new Hand();
		this.dealer = new Player("Dealer");
		this.dealerHand = new Hand();
		this.isRunning = false;
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

	public int getGameRounds() {
		return gameRounds;
	}

	public void setGameRounds(int gameRounds) {
		this.gameRounds = gameRounds;
	}

	public Hand getPlayerHand() {
		return playerHand;
	}

	public void setPlayerHand(Hand playerHand) {
		this.playerHand = playerHand;
	}

	public Hand getDealerHand() {
		return dealerHand;
	}

	public void setDealerHand(Hand dealerHand) {
		this.dealerHand = dealerHand;
	}

	public boolean getIsRunning() {
		return isRunning;
	}

	public void setIsRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
	
	

}
