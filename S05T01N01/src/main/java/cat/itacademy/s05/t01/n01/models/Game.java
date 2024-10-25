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
	private int gameRounds;
	
	

	public Game() {
		this.deck = new Deck();
		this.dealer = new Player("Dealer");
	}

	public Game(Player player) {
		this.deck = new Deck();
		this.player = player;
		this.dealer = new Player("Dealer");
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
	
	

}
