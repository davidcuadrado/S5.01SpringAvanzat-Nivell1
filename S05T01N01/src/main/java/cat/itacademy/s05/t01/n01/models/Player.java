package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import reactor.core.publisher.Mono;

@Data
@Table(name = "players")
public class Player {

	@Id
	@Column("playerId")
	private int playerId;
	@Column("playerName")
	private String playerName;
	@Column("handId")
	private int handId;
	@Column("currentPoints")
	private int maxPoints;

	public Player() {
	}

	public Player(String playerName) {
		this.playerName = playerName;
		this.handId = 0;
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Mono<String> getPlayerNameMono() {
		return Mono.just(playerName);
	}

	public Mono<Void> setPlayerNameMono(String playerName) {
		return Mono.fromRunnable(() -> this.playerName = playerName);
	}

	public Mono<Integer> getPlayerId() {
		return Mono.just(playerId);
	}

	public Mono<Void> setPlayerId(int playerId) {
		return Mono.fromRunnable(() -> this.playerId = playerId);
	}

	public Mono<Integer> getPlayerHandIdMono() {
		return Mono.just(this.handId);
	}

	public int getPlayerHandId() {
		return this.handId;
	}
	
	public void setPlayerHandId(int handId) {
		this.handId = handId;
	}


	public void setPlayerMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}
	
	public Mono<Integer> getPlayerMaxPoints() {
		return Mono.just(this.maxPoints);
	}

	public Integer getPlayerMaxPointsSync() {
		return this.maxPoints;
	}

	public String toString() {
		return this.playerId + " | " + this.playerName + " | " + this.handId;

	}

}
