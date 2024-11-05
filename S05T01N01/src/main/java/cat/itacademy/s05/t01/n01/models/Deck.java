package cat.itacademy.s05.t01.n01.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	private ArrayList<Card> cards;

	public Deck() {
		this.cards = new ArrayList<>();
		String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };
		String[] values = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

		for (String suit : suits) {
			for (String value : values) {
				cards.add(new Card(suit, value));
			}
		}
		shuffle();
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	public Card drawCard() {
		return cards.remove(0);
	}
	
	public String toString() {
		return cards.toString();
	}
	
	public List<Card> giveNewHand() {
	    if (cards.size() < 48) {
	        new Deck();
	    }
	    List<Card> newHand = new ArrayList<>();
	    newHand.add(drawCard());
	    newHand.add(drawCard());
	    return newHand;
	}
}
