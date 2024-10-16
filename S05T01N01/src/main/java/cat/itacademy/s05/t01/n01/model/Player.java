package cat.itacademy.s05.t01.n01.model;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Player {
	
	@Id
	private String playerId;
	private String playerName;
	private int playerMaxPoints;
	

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getPlayerMaxPoints() {
		return playerMaxPoints;
	}

	public void setPlayerMaxPoints(int playerMaxPoints) {
		this.playerMaxPoints = playerMaxPoints;
	}
	
	

	
	
}
