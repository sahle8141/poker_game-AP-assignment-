package poker;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles persistence of player statistics to a plain-text file.
 *
 * File format (key=value, one per line):
 * ─────────────────────────────────────
 *   username=Player1
 *   highscore=5000
 *   currentchips=1200
 *   handsplayed=42
 *   handswon=18
 *
 * Reading  : BufferedReader + line.split("=", 2)
 * Writing  : PrintWriter wrapped in try-with-resources
 *
 * IOException is caught and logged — the game never crashes
 * due to a missing or corrupt stats file.
 */
public class StatsManager {

    private static final String DEFAULT_STATS_FILE = "stats.txt";
    private static final int    DEFAULT_CHIPS       = 1000;

    private final String filePath;

    public StatsManager() {
        this(DEFAULT_STATS_FILE);
    }

    public StatsManager(String filePath) {
        this.filePath = filePath;
    }

    // ── Save ─────────────────────────────────────────────────────────────────

    /**
     * Writes the player's current stats to the text file.
     * Uses try-with-resources so the writer is always closed.
     */
    public void save(Player player) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("username="    + player.getName());
            writer.println("highscore="   + player.getHighScore());
            writer.println("currentchips="+ player.getChips());
            writer.println("handsplayed=" + player.getTotalHandsPlayed());
            writer.println("handswon="    + player.getTotalHandsWon());
        } catch (IOException e) {
            System.err.println("[StatsManager] Could not save stats: " + e.getMessage());
        }
    }

    // ── Load ─────────────────────────────────────────────────────────────────

    /**
     * Reads stats from the text file and applies them to the player.
     * If the file is missing or malformed, defaults are used silently.
     */
    public void load(Player player) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            // First run — no file yet, use defaults
            return;
        }

        Map<String, String> data = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || !line.contains("=")) continue;
                String[] parts = line.split("=", 2);   // limit 2 handles values containing "="
                data.put(parts[0].trim().toLowerCase(), parts[1].trim());
            }
        } catch (IOException e) {
            System.err.println("[StatsManager] Could not load stats: " + e.getMessage());
            return;
        }

        // Apply parsed values — parseInt failures are caught individually
        applyInt(data,  "currentchips", v -> player.setChips(v));
        applyInt(data,  "highscore",    v -> player.setHighScore(v));
        applyInt(data,  "handsplayed",  v -> player.setTotalHandsPlayed(v));
        applyInt(data,  "handswon",     v -> player.setTotalHandsWon(v));
    }

    /** Raw load for display only (returns the file's text content). */
    public String rawFileContents() {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            return "(stats file not found)";
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    @FunctionalInterface
    interface IntConsumer { void accept(int value); }

    private void applyInt(Map<String, String> data, String key, IntConsumer action) {
        if (!data.containsKey(key)) return;
        try {
            action.accept(Integer.parseInt(data.get(key)));
        } catch (NumberFormatException e) {
            System.err.println("[StatsManager] Bad value for key '" + key + "': " + data.get(key));
        }
    }
}
