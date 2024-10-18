package cat.itacademy.s05.t01.n01.models;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getScore() {
        int total = 0;
        int aces = 0;

        for (Card card : cards) {
            total += card.getNumericValue();
            if ("A".equals(card.getValue())) {
                aces++;
            }
        }

        // Adjust the value of Aces if necessary
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isBlackjack() {
        return getScore() == 21 && cards.size() == 2;
    }

    public boolean isBust() {
        return getScore() > 21;
    }
}
