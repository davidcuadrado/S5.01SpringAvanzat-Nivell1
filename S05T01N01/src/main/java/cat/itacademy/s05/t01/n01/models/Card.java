package cat.itacademy.s05.t01.n01.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "Card entity that represents each card of the deck. ")
@Getter
@RequiredArgsConstructor
public class Card {

	private String suit;
	private String value;
	private int numericValue;

	public Card(String suit, String value) {
		this.suit = suit;
		this.value = value;
		this.numericValue = setNumericValue(value);
	}

	public String getSuit() {
		return suit;
	}

	public String getValue() {
		return value;
	}

	public int setNumericValue(String value) {
		if ("A".equals(value))
			return 11;
		if ("K".equals(value) || "Q".equals(value) || "J".equals(value) || "10".equals(value))
			return 10;
		return Integer.parseInt(value);
	}

	public int getNumericValue() {
		return numericValue;
	}

}
