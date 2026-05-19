package poker;

import java.util.*;
import java.util.stream.Collectors;


public class Hand {

    public enum HandRank {
        HIGH_CARD      ("High Card",       0),
        ONE_PAIR       ("One Pair",        1),
        TWO_PAIR       ("Two Pair",        2),
        THREE_OF_A_KIND("Three of a Kind", 3),
        STRAIGHT       ("Straight",        4),
        FLUSH          ("Flush",           5),
        FULL_HOUSE     ("Full House",      6),
        FOUR_OF_A_KIND ("Four of a Kind",  7),
        STRAIGHT_FLUSH ("Straight Flush",  8),
        ROYAL_FLUSH    ("Royal Flush",     9);

        private final String displayName;
        private final int    tier;

        HandRank(String displayName, int tier) {
            this.displayName = displayName;
            this.tier        = tier;
        }
        public String getDisplayName() { return displayName; }
        public int    getTier()        { return tier;        }
    }

    public static final int HAND_SIZE = 5;

    private final ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>(HAND_SIZE);
    }

    
    public void addCard(Card card) {
        if (cards.size() >= HAND_SIZE) {
            throw new IllegalStateException("Hand is already full.");
        }
        cards.add(card);
    }

    
    public void replaceCard(int index, Card newCard) {
        if (index < 0 || index >= cards.size()) {
            throw new IndexOutOfBoundsException("Invalid card index: " + index);
        }
        cards.set(index, newCard);
    }

    public Card getCard(int index) { return cards.get(index); }
    public int  size()             { return cards.size();     }

    
    public List<Card> getCards() { return Collections.unmodifiableList(cards); }


    public HandRank evaluate() {
        boolean flush    = isFlush();
        boolean straight = isStraight();

        if (flush && straight) {

            List<Integer> values = getSortedValues();
            if (values.get(0) == 10) return HandRank.ROYAL_FLUSH;
            return HandRank.STRAIGHT_FLUSH;
        }
        if (hasFourOfAKind())  return HandRank.FOUR_OF_A_KIND;
        if (isFullHouse())     return HandRank.FULL_HOUSE;
        if (flush)             return HandRank.FLUSH;
        if (straight)         return HandRank.STRAIGHT;
        if (hasThreeOfAKind()) return HandRank.THREE_OF_A_KIND;
        if (isTwoPair())       return HandRank.TWO_PAIR;
        if (isOnePair())       return HandRank.ONE_PAIR;
        return HandRank.HIGH_CARD;
    }


    private List<Integer> getSortedValues() {
        return cards.stream()
                    .map(c -> c.getRank().getValue())
                    .sorted()
                    .collect(Collectors.toList());
    }


    private Map<Integer, Long> getRankCounts() {
        return cards.stream()
                    .collect(Collectors.groupingBy(
                        c -> c.getRank().getValue(),
                        Collectors.counting()));
    }

    private boolean isFlush() {
        Card.Suit first = cards.get(0).getSuit();
        return cards.stream().allMatch(c -> c.getSuit() == first);
    }

    private boolean isStraight() {
        List<Integer> values = getSortedValues();

        for (int i = 1; i < values.size(); i++) {
            if (values.get(i) - values.get(i - 1) != 1) {

                return values.equals(Arrays.asList(2, 3, 4, 5, 14));
            }
        }
        return true;
    }

    private boolean isOnePair() {
        return getRankCounts().values().stream().anyMatch(c -> c == 2) &&
               getRankCounts().size() == 4;
    }

    private boolean isTwoPair() {
        return getRankCounts().values().stream().filter(c -> c == 2).count() == 2;
    }

    private boolean hasThreeOfAKind() {
        return getRankCounts().values().stream().anyMatch(c -> c == 3) &&
               getRankCounts().size() == 3;
    }

    private boolean isFullHouse() {
        Map<Integer, Long> counts = getRankCounts();
        return counts.values().stream().anyMatch(c -> c == 3) &&
               counts.values().stream().anyMatch(c -> c == 2);
    }

    private boolean hasFourOfAKind() {
        return getRankCounts().values().stream().anyMatch(c -> c == 4);
    }


    public static int getPayoutMultiplier(HandRank rank) {
        return switch (rank) {
            case ROYAL_FLUSH    -> 250;
            case STRAIGHT_FLUSH -> 50;
            case FOUR_OF_A_KIND -> 25;
            case FULL_HOUSE     -> 9;
            case FLUSH          -> 6;
            case STRAIGHT       -> 4;
            case THREE_OF_A_KIND-> 3;
            case TWO_PAIR       -> 2;
            case ONE_PAIR       -> 1;
            default             -> 0;
        };
    }
}
