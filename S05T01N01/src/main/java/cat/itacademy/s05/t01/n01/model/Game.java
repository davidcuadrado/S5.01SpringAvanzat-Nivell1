package cat.itacademy.s05.t01.n01.model;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
	
	@Id
	private String gameId;
	private String playerName;
	
	
	
	public Game(String gameId, String playerName) {
        this.setPlayerName(playerName);
        this.gameId = gameId;
    }



	public String getPlayerName() {
		return playerName;
	}



	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
}
