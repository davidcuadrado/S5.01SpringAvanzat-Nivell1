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

	public Mono<Integer> getPlayerMaxPoints() {
		return Mono.just(this.handId);
	}

	public Integer getPlayerMaxPointsSync() {
		return this.handId;
	}

	public Mono<Void> setPlayerMaxPoints(int playerMaxPoints) {
		return Mono.fromRunnable(() -> this.handId = playerMaxPoints);
	}

	public String toString() {
		return this.playerId + " | " + this.playerName + " | " + this.handId;

	}

}
