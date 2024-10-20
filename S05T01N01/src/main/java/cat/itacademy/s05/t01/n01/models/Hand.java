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

	public Mono<Integer> getScore() {
		return Mono.fromSupplier(() -> {
			int total = 0;
			int aces = 0;

			for (Card card : cards) {
				total += card.getNumericValue();
				if ("A".equals(card.getValue())) {
					aces++;
				}
			}

			while (total > 21 && aces > 0) {
				total -= 10;
				aces--;
			}

			return total;
		});
	}

	public Mono<List<Card>> getCards() {
		return Mono.just(cards);
	}

	public Mono<Boolean> isBlackjack() {
		return getScore().map(score -> score == 21 && cards.size() == 2);
	}

	public Mono<Boolean> isBust() {
		return getScore().map(score -> score > 21);
	}
	
}
