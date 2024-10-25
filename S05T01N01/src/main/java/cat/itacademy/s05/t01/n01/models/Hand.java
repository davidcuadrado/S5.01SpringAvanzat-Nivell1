package cat.itacademy.s05.t01.n01.models;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Mono;

public class Hand {
	private List<Card> cards;

	public Hand() {
		this.cards = new ArrayList<>();
	}

	public Mono<Void> addCard(Card card) {
		return Mono.fromRunnable(() -> cards.add(card));
	}

	public int getScore() {
        int score = 0;
        int aces = 0;

        for (Card card : cards) {
            score += card.getNumericValue();
            if (card.getValue().equals("A")) aces++;
        }

        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }
        return score;
    }
	
	public boolean isBust() {
        return getScore() > 21;
    }

	public Mono<List<Card>> getCards() {
		return Mono.just(cards);
	}
	// métodos que pertenecían a Player
	/*
	public Mono<Boolean> isBlackjack() {
		return getScore().map(score -> score == 21 && cards.size() == 2);
	}

	public Mono<Boolean> isBust() {
		return getScore().map(score -> score > 21);
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

	public Mono<Hand> getHandMono() {
		return Mono.just(this.hand);
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}
	
	*/
	
}
