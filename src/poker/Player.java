package poker;

/**
 * Represents a player: holds their name, chip balance, and high-score.
 * Pure data model — no UI or file I/O here.
 */
public class Player {

    private final String name;
    private int chips;
    private int highScore;
    private int totalHandsPlayed;
    private int totalHandsWon;

    public Player(String name, int startingChips) {
        this.name         = name;
        this.chips        = startingChips;
        this.highScore    = startingChips;
        this.totalHandsPlayed = 0;
        this.totalHandsWon    = 0;
    }

    // ── Chip management ───────────────────────────────────────────────────────

    public void placeBet(int amount) {
        if (amount > chips) throw new IllegalArgumentException("Insufficient chips.");
        chips -= amount;
    }

    public void addWinnings(int amount) {
        chips += amount;
        if (chips > highScore) highScore = chips;
    }

    public void recordHand(boolean won) {
        totalHandsPlayed++;
        if (won) totalHandsWon++;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public String getName()           { return name;             }
    public int    getChips()          { return chips;            }
    public int    getHighScore()      { return highScore;        }
    public int    getTotalHandsPlayed() { return totalHandsPlayed; }
    public int    getTotalHandsWon()    { return totalHandsWon;    }

    /** Called when loading from persistence to restore state. */
    public void setChips(int chips) {
        this.chips = chips;
        if (chips > highScore) highScore = chips;
    }

    public void setHighScore(int highScore) { this.highScore = highScore; }
    public void setTotalHandsPlayed(int v)  { this.totalHandsPlayed = v;  }
    public void setTotalHandsWon(int v)     { this.totalHandsWon    = v;  }

    public boolean isBroke() { return chips <= 0; }
}
