package poker.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import poker.*;

import java.util.List;

/**
 * PokerApp — main JavaFX Application.
 *
 * Responsibilities (only):
 *   • Assemble panels into a BorderPane
 *   • Wire button callbacks to GameEngine calls
 *   • Refresh panels after each state change
 *
 * All layout, styling, and animation live in the dedicated panel classes.
 */
public class PokerApp extends Application implements ControlBar.ButtonActions {

    private static final int STARTING_CHIPS = 1000;

    // ── Game layer ────────────────────────────────────────────────────────────
    private GameEngine engine;
    private Player     player;

    // ── UI panels ─────────────────────────────────────────────────────────────
    private HeaderBar     headerBar;
    private GameArea      gameArea;
    private PaytablePanel paytablePanel;
    private StatsPanel    statsPanel;
    private ControlBar    controlBar;

    // ── Application lifecycle ─────────────────────────────────────────────────

    @Override
    public void start(Stage stage) {
        player = new Player("Player1", STARTING_CHIPS);
        engine = new GameEngine(player, new StatsManager());

        headerBar     = new HeaderBar();
        gameArea      = new GameArea();
        paytablePanel = new PaytablePanel();
        statsPanel    = new StatsPanel();
        controlBar    = new ControlBar(this);

        BorderPane root = new BorderPane();
        root.setBackground(UiFactory.feltBackground());
        root.setTop(headerBar);
        root.setCenter(gameArea);
        root.setLeft(paytablePanel);
        root.setRight(statsPanel);
        root.setBottom(controlBar);

        Scene scene = new Scene(root, 1100, 720);
        scene.getStylesheets().add(PokerStyleSheet.asDataUri());

        stage.setTitle("Five-Card Draw Poker");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> engine.saveStats());
        stage.show();

        refreshAll();
    }

    // ── ControlBar.ButtonActions ──────────────────────────────────────────────

    @Override
    public void onDeal() {
        int bet = controlBar.getCurrentBet();
        try {
            engine.deal(bet);
        } catch (IllegalArgumentException ex) {
            gameArea.showFlash(ex.getMessage(), false);
            return;
        }

        gameArea.clearResult();
        paytablePanel.clearHighlight();
        controlBar.onDealt();

        gameArea.animateDeal(engine.getCurrentHand(), null);
        gameArea.setInstruction(GameArea.INSTRUCTION_DEALT);
        refreshAll();
    }

    @Override
    public void onDraw() {
        List<Integer> toReplace = gameArea.getIndicesToReplace();
        engine.draw(toReplace);

        gameArea.animateDraw(engine.getCurrentHand(), toReplace, () -> {
            // Called after the last card animation lands
            gameArea.showResult(engine.getLastResult());
            paytablePanel.highlight(engine.getLastHandRank());
            controlBar.onResult();
            gameArea.setInstruction(GameArea.INSTRUCTION_DONE);
            refreshAll();
        });
    }

    @Override
    public void onReset() {
        engine.reset();
        gameArea.resetCards();
        gameArea.clearResult();
        paytablePanel.clearHighlight();
        controlBar.onReset();
        gameArea.setInstruction(GameArea.INSTRUCTION_IDLE);

        if (player.isBroke()) {
            player.setChips(STARTING_CHIPS);
            gameArea.showFlash("Out of chips! Restarting with " + STARTING_CHIPS + " chips.", true);
        }

        refreshAll();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Push current player state to every panel that displays it. */
    private void refreshAll() {
        headerBar .refresh(player);
        statsPanel.refresh(player);
        controlBar.setMaxBet(player.getChips());
    }

    // ── Entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        launch(args);
    }
}
