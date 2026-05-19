package poker.ui;

import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import poker.Player;

/**
 * HeaderBar — top strip showing the game title, current chips, and high score.
 */
public class HeaderBar extends HBox {

    private final Label chipsLabel;
    private final Label highScoreLabel;

    public HeaderBar() {
        setAlignment(Pos.CENTER);
        setSpacing(16);
        setPadding(new Insets(14, 20, 10, 20));
        setStyle("-fx-background-color:rgba(0,0,0,0.35);" +
                 "-fx-border-color:rgba(245,215,110,0.3);" +
                 "-fx-border-width:0 0 1 0;");

        Label title    = UiFactory.headerLabel("♠  FIVE-CARD DRAW POKER  ♥");
        chipsLabel     = UiFactory.chipLabel("Chips: 1000", "#7effa0");
        highScoreLabel = UiFactory.chipLabel("Best: 1000",  "#f5d76e");

        getChildren().addAll(UiFactory.hSpacer(), title, UiFactory.hSpacer(), chipsLabel, highScoreLabel);
    }

    /** Refresh both labels from the current player state. */
    public void refresh(Player player) {
        chipsLabel    .setText("Chips: " + player.getChips());
        highScoreLabel.setText("Best: "  + player.getHighScore());
    }
}
