package poker;

/**
 * Immutable value object representing a single playing card.
 * Suit and Rank are enums for type-safety and easy comparison.
 */
public class Card {

    public enum Suit {
        CLUBS("♣", "clubs"),
        DIAMONDS("♦", "diamonds"),
        HEARTS("♥", "hearts"),
        SPADES("♠", "spades");

        private final String symbol;
        private final String name;

        Suit(String symbol, String name) {
            this.symbol = symbol;
            this.name   = name;
        }
        public String getSymbol() { return symbol; }
        public String getName()   { return name;   }
        public boolean isRed()    { return this == HEARTS || this == DIAMONDS; }
    }

    public enum Rank {
        TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"),
        SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"),
        TEN(10, "10"), JACK(11, "J"), QUEEN(12, "Q"), KING(13, "K"), ACE(14, "A");

        private final int value;
        private final String symbol;

        Rank(int value, String symbol) {
            this.value  = value;
            this.symbol = symbol;
        }
        public int    getValue()  { return value;  }
        public String getSymbol() { return symbol; }
    }

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() { return suit; }
    public Rank getRank() { return rank; }

    @Override
    public String toString() {
        return rank.getSymbol() + suit.getSymbol();
    }
}
