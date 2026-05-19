package poker;


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



    public String getName()           { return name;             }
    public int    getChips()          { return chips;            }
    public int    getHighScore()      { return highScore;        }
    public int    getTotalHandsPlayed() { return totalHandsPlayed; }
    public int    getTotalHandsWon()    { return totalHandsWon;    }


    public void setChips(int chips) {
        this.chips = chips;
        if (chips > highScore) highScore = chips;
    }

    public void setHighScore(int highScore) { this.highScore = highScore; }
    public void setTotalHandsPlayed(int v)  { this.totalHandsPlayed = v;  }
    public void setTotalHandsWon(int v)     { this.totalHandsWon    = v;  }

    public boolean isBroke() { return chips <= 0; }
}
