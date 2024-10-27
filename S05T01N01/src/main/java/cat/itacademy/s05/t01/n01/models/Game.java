package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Game entity that represent every game created. ")
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
	private int currentPoints;
	private String lastResult;
	private boolean isRunning;
	
	
	

	public Game() {
		this.deck = new Deck();
		this.dealer = new Player("Dealer");
	}

	public Game(Player player) {
		this.player = player;
		this.deck = new Deck();
		this.playerHand = new Hand();
		this.dealer = new Player("Dealer");
		this.dealerHand = new Hand();
		this.isRunning = false;
		this.currentPoints = 1000;
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
	
	public void incrementGameRounds() {
		this.gameRounds += 1;
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

	public String getLastResult() {
		return lastResult;
	}

	public void setLastResult(String lastResult) {
		this.lastResult = lastResult;
	}

	public int getCurrentPoints() {
		return currentPoints;
	}

	public void setCurrentPoints(int currentPoints) {
		this.currentPoints = currentPoints;
	}
	
	

}
