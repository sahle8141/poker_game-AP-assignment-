package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A standard 52-card deck.
 * Supports shuffling and dealing single cards.
 */
public class Deck {

    private final ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>(52);
        reset();
    }

    /** Re-populate and shuffle the deck. */
    public void reset() {
        cards.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    /** Deal one card from the top of the deck. */
    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty — call reset() first.");
        }
        return cards.remove(cards.size() - 1);
    }

    public int remaining() { return cards.size(); }
}
