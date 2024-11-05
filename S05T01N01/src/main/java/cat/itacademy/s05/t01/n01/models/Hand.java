package cat.itacademy.s05.t01.n01.models;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Mono;

public class Hand {
    private List<Card> cards;
    private int score;
    private int aces;

    public Hand() {
        this.cards = new ArrayList<>();
        this.score = 0;
        this.aces = 0;
    }

    public Mono<Void> addCard(Card card) {
        return Mono.fromRunnable(() -> {
            cards.add(card);
            int cardValue = card.getNumericValue();
            score += cardValue;
            if ("A".equals(card.getValue())) {
                aces++;
            }
            adjustScoreForAces();
        });
    }

    private void adjustScoreForAces() {
        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }
    }

    public int getScore() {
        return score;
    }

    public boolean isBust() {
        return score > 21;
    }

    public Mono<List<Card>> getCards() {
        return Mono.just(new ArrayList<>(cards));
    }
    
    public Mono<List<Card>> getNewPlayerHand(Mono<Game> monoGame) {
        return monoGame.flatMap(game -> {
            game.getPlayerHand().setNewCards(game);
            return game.getPlayerHand().getCards();
        });
    }
    
    public void setCards(List<Card> cards) {
    	this.cards = cards;
    }
    
    public List<Card> setNewCards(Game game) {
    	return game.getDeck().giveNewPlayerHand();
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}