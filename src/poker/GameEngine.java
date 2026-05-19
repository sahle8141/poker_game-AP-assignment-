package poker;

import java.util.ArrayList;
import java.util.List;


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

    public void draw(List<Integer> indicesToReplace) {
        assertState(GameState.DEALT);
        state = GameState.DRAWING;

        for (int idx : indicesToReplace) {
            currentHand.replaceCard(idx, deck.deal());
        }

        evaluateResult();
        state = GameState.RESULT;
    }

    public void reset() {
        state       = GameState.IDLE;
        currentHand = null;
        lastResult  = "";
        lastHandRank = null;
    }


    public void saveStats() {
        statsManager.save(player);
    }



    public GameState     getState()        { return state;        }
    public Hand          getCurrentHand()  { return currentHand;  }
    public Player        getPlayer()       { return player;       }
    public String        getLastResult()   { return lastResult;   }
    public Hand.HandRank getLastHandRank() { return lastHandRank; }
    public int           getCurrentBet()   { return currentBet;   }
    public int           getAnte()         { return ANTE;         }
    public StatsManager  getStatsManager() { return statsManager; }



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

        statsManager.save(player); 
    }

    private void assertState(GameState expected) {
        if (state != expected) {
            throw new IllegalStateException(
                "Expected state " + expected + " but was " + state);
        }
    }
}
