package cat.itacademy.s05.t01.n01.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import reactor.core.publisher.Mono;

@Data
@Table(name = "players")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Player {

	@Id
	private int playerId;
	@Column("playerName")
	private String playerName;
	@Column("playerMaxPoints")
	private int playerMaxPoints;
	@Transient
	private Hand hand;

	public Player() {
	}

	public Player(String playerName) {
		this.playerName = playerName;
		this.playerMaxPoints = 1000;
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
		return Mono.just(this.playerMaxPoints);
	}

	public Integer getPlayerMaxPointsSync() {
		return this.playerMaxPoints;
	}

	public Mono<Void> setPlayerMaxPoints(int playerMaxPoints) {
		return Mono.fromRunnable(() -> this.playerMaxPoints = playerMaxPoints);
	}

	public Mono<Void> receiveCard(Card card) {
		return this.hand.addCard(card);
	}

	public Mono<Integer> getScore() {
		return this.hand.getScore();
	}

	public Mono<Boolean> isBlackjack() {
		return this.hand.isBlackjack();
	}

	public Mono<Boolean> isBust() {
		return this.hand.isBust();
	}

	public Mono<Hand> getHand() {
		return Mono.just(this.hand);
	}

}
