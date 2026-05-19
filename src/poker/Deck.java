package poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Deck {

    private final ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>(52);
        reset();
    }

    
    public void reset() {
        cards.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    
    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty — call reset() first.");
        }
        return cards.remove(cards.size() - 1);
    }

    public int remaining() { return cards.size(); }
}
