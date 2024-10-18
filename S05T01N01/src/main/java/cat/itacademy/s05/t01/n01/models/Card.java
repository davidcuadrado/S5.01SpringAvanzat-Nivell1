package cat.itacademy.s05.t01.n01.models;

public class Card {
	
	private final String suit;
	private final String value;
	
	public Card(String suit, String value) {
		this.suit = suit;
		this.value = value;
	}

	public String getSuit() {
		return suit;
	}

	public String getValue() {
		return value;
	}
	
	
	public int getNumericValue() {
        if ("A".equals(value)) return 11;
        if ("K".equals(value) || "Q".equals(value) || "J".equals(value) || "10".equals(value)) return 10;
        return Integer.parseInt(value);
    }
	
	

}
