package poker.ui;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * ControlBar — bottom strip with the bet slider and Deal / Draw / New Game buttons.
 * Exposes enable/disable helpers so PokerApp can drive the state machine
 * without reaching into individual controls.
 */
public class ControlBar extends HBox {

    public interface ButtonActions {
        void onDeal();
        void onDraw();
        void onReset();
    }

    private final Button dealButton;
    private final Button drawButton;
    private final Button resetButton;
    private final Slider betSlider;
    private final Label  betLabel;

    public ControlBar(ButtonActions actions) {
        setAlignment(Pos.CENTER);
        setSpacing(14);
        setPadding(new Insets(14, 20, 16, 20));
        setStyle("-fx-background-color:rgba(0,0,0,0.4);" +
                 "-fx-border-color:rgba(245,215,110,0.25);" +
                 "-fx-border-width:1 0 0 0;");

        // Bet slider
        betSlider = new Slider(10, 200, 10);
        betSlider.setMajorTickUnit(50);
        betSlider.setBlockIncrement(10);
        betSlider.setPrefWidth(180);
        betSlider.getStyleClass().add("poker-slider");

        betLabel = new Label("Bet: 10");
        betLabel.setStyle(
            "-fx-font-size:13px; -fx-font-weight:bold;" +
            "-fx-text-fill:#f5d76e; -fx-min-width:65px;");

        betSlider.valueProperty().addListener((obs, o, n) -> {
            int v = (int)(Math.round(n.doubleValue() / 10.0) * 10);
            betSlider.setValue(v);
            betLabel.setText("Bet: " + v);
        });

        dealButton  = UiFactory.actionButton("DEAL",     "#27ae60", "#1e8449");
        drawButton  = UiFactory.actionButton("DRAW",     "#2980b9", "#1a5276");
        resetButton = UiFactory.actionButton("NEW GAME", "#7f8c8d", "#616a6b");

        drawButton.setDisable(true);

        dealButton .setOnAction(e -> actions.onDeal());
        drawButton .setOnAction(e -> actions.onDraw());
        resetButton.setOnAction(e -> actions.onReset());

        Label betTitle = new Label("BET:");
        betTitle.setStyle(
            "-fx-font-size:12px; -fx-text-fill:#90c090; -fx-font-weight:bold;");

        getChildren().addAll(
            betTitle, betSlider, betLabel,
            UiFactory.hSpacer(),
            dealButton, drawButton, resetButton,
            UiFactory.hSpacer()
        );
    }

    // ── State helpers called by PokerApp ──────────────────────────────────────

    public void onDealt() {
        dealButton .setDisable(true);
        drawButton .setDisable(false);
        betSlider  .setDisable(true);
    }

    public void onResult() {
        dealButton .setDisable(false);
        drawButton .setDisable(true);
        betSlider  .setDisable(false);
    }

    public void onReset() {
        dealButton .setDisable(false);
        drawButton .setDisable(true);
        betSlider  .setDisable(false);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    public int getCurrentBet() {
        return (int)(Math.round(betSlider.getValue() / 10.0) * 10);
    }

    /** Cap the slider maximum to the player's available chips. */
    public void setMaxBet(int maxChips) {
        int cap = Math.max(10, Math.min(200, maxChips));
        betSlider.setMax(cap);
        if (betSlider.getValue() > cap) betSlider.setValue(cap);
    }
}
