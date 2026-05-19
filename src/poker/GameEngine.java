package poker;

import java.util.ArrayList;
import java.util.List;

/**
 * GameEngine — all poker game logic, zero JavaFX references.
 *
 * State machine:
 *   IDLE → BETTING → DEALT → DRAWING → RESULT → IDLE
 *
 * The JavaFX UI calls these methods and reads the resulting state.
 */
public class GameEngine {

    public enum GameState { IDLE, BETTING, DEALT, DRAWING, RESULT }

    private static final int ANTE = 10;   // minimum bet per hand

    private final Deck         deck;
    private final Player       player;
    private final StatsManager statsManager;

    private Hand      currentHand;
    private GameState state;
    private int       currentBet;
    private String    lastResult;
    private Hand.HandRank lastHandRank;

    public GameEngine(Player player, StatsManager statsManager) {
        this.player       = player;
        this.statsManager = statsManager;
        this.deck         = new Deck();
        this.state        = GameState.IDLE;
        this.lastResult   = "";
        statsManager.load(player);
    }

    // ── Public API (called by UI) ─────────────────────────────────────────────

    /**
     * Begin a new hand: place the bet and deal 5 cards.
     * @param betAmount chips wagered (must be >= ANTE)
     */
    public void deal(int betAmount) {
        assertState(GameState.IDLE);
        if (betAmount < ANTE)         throw new IllegalArgumentException("Minimum bet is " + ANTE + " chips.");
        if (betAmount > player.getChips()) throw new IllegalArgumentException("Not enough chips.");

        deck.reset();
        currentBet  = betAmount;
        currentHand = new Hand();
        player.placeBet(betAmount);

        for (int i = 0; i < Hand.HAND_SIZE; i++) {
            currentHand.addCard(deck.deal());
        }

        state = GameState.DEALT;
    }

    /**
     * Draw phase: replace the cards at the given indices (0-4) with new cards.
     * Pass an empty list to "stand pat" (keep all cards).
     */
    public void draw(List<Integer> indicesToReplace) {
        assertState(GameState.DEALT);
        state = GameState.DRAWING;

        for (int idx : indicesToReplace) {
            currentHand.replaceCard(idx, deck.deal());
        }

        evaluateResult();
        state = GameState.RESULT;
    }

    /** Reset the game to IDLE, ready for a new hand. */
    public void reset() {
        state       = GameState.IDLE;
        currentHand = null;
        lastResult  = "";
        lastHandRank = null;
    }

    /** Save current player stats to the text file. */
    public void saveStats() {
        statsManager.save(player);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public GameState     getState()        { return state;        }
    public Hand          getCurrentHand()  { return currentHand;  }
    public Player        getPlayer()       { return player;       }
    public String        getLastResult()   { return lastResult;   }
    public Hand.HandRank getLastHandRank() { return lastHandRank; }
    public int           getCurrentBet()   { return currentBet;   }
    public int           getAnte()         { return ANTE;         }
    public StatsManager  getStatsManager() { return statsManager; }

    // ── Private helpers ───────────────────────────────────────────────────────

    private void evaluateResult() {
        lastHandRank = currentHand.evaluate();
        int multiplier = Hand.getPayoutMultiplier(lastHandRank);
        int winnings   = currentBet * multiplier;
        boolean won    = multiplier > 0;

        player.recordHand(won);

        if (won) {
            player.addWinnings(winnings + currentBet); // return bet + profit
            lastResult = "🏆 " + lastHandRank.getDisplayName()
                         + "!  +" + winnings + " chips";
        } else {
            lastResult = "💸 " + lastHandRank.getDisplayName()
                         + "  (No payout)";
        }

        statsManager.save(player); // auto-save after each hand
    }

    private void assertState(GameState expected) {
        if (state != expected) {
            throw new IllegalStateException(
                "Expected state " + expected + " but was " + state);
        }
    }
}
