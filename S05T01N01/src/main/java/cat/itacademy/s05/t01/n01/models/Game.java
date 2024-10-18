package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
	
	@Id
	private String gameId;
	private Deck deck;
    private Player player;
    private Player dealer;
	
	
	
    public Game(String playerName) {
        this.deck = new Deck();
        this.player = new Player(playerName);
        this.dealer = new Player("Dealer");
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
	
}
