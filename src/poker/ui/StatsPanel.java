package poker.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import poker.Player;


public class StatsPanel extends VBox {

    private final Label handsLabel;
    private final Label winRateLabel;

    public StatsPanel() {
        setSpacing(10);
        setPadding(new Insets(20, 16, 20, 12));
        setMinWidth(155);
        setStyle("-fx-background-color:rgba(0,0,0,0.3);" +
                 "-fx-border-color:rgba(245,215,110,0.25);" +
                 "-fx-border-width:0 0 0 1;");

        handsLabel   = UiFactory.statLine("Hands: 0");
        winRateLabel = UiFactory.statLine("Win Rate: —");

        Label fileNote = new Label("Auto-saved to\nstats.txt");
        fileNote.setStyle(
            "-fx-font-size:10px; -fx-text-fill:#80a080;" +
            "-fx-opacity:0.7; -fx-padding:12 0 0 0;");

        getChildren().addAll(UiFactory.panelTitle("STATS"), handsLabel, winRateLabel, fileNote);
    }

    public void refresh(Player player) {
        handsLabel.setText("Hands: " + player.getTotalHandsPlayed());
        int played = player.getTotalHandsPlayed();
        if (played > 0) {
            int pct = (int)(player.getTotalHandsWon() * 100.0 / played);
            winRateLabel.setText("Win Rate: " + pct + "%");
        } else {
            winRateLabel.setText("Win Rate: —");
        }
    }
}
