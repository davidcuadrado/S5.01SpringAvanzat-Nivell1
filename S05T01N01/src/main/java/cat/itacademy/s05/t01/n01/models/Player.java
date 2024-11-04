package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import reactor.core.publisher.Mono;

@Schema(description = "Player entity that represents every player that created a game. ")
@Table(name = "players")
public class Player {

	@Id
	@Column("playerId")
	private int playerId;
	@Column("playerName")
	private String playerName;
	@Column("maxPoints")
	private int maxPoints;

	public Player() {
	}

	public int getPlayerIdNum() {
		return this.playerId;
	}

	public Player(String playerName) {
		this.playerName = playerName;
		this.maxPoints = 1000;
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
		return this.playerId + " | " + this.playerName + " | " + this.maxPoints;
	}

}
