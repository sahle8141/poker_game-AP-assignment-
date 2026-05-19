package poker.ui;

import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import poker.Hand;

/**
 * PaytablePanel — left sidebar listing all hand ranks and payouts.
 * Highlights the winning row after each hand.
 */
public class PaytablePanel extends VBox {

    private static final String[][] ROWS = {
        {"Royal Flush",      "250x"},
        {"Straight Flush",    "50x"},
        {"Four of a Kind",    "25x"},
        {"Full House",         "9x"},
        {"Flush",              "6x"},
        {"Straight",           "4x"},
        {"Three of a Kind",    "3x"},
        {"Two Pair",           "2x"},
        {"One Pair",           "1x"},
        {"High Card",          "—" },
    };

    // tier 0 = HIGH_CARD → index 9, tier 9 = ROYAL_FLUSH → index 0
    private static final int[] TIER_TO_ROW = {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

    private final Label[] nameLabels = new Label[ROWS.length];
    private final HBox[]  rowBoxes   = new HBox[ROWS.length];

    public PaytablePanel() {
        setSpacing(4);
        setPadding(new Insets(20, 12, 20, 16));
        setMinWidth(175);
        setStyle("-fx-background-color:rgba(0,0,0,0.3);" +
                 "-fx-border-color:rgba(245,215,110,0.25);" +
                 "-fx-border-width:0 1 0 0;");

        getChildren().add(UiFactory.panelTitle("PAYTABLE"));

        for (int i = 0; i < ROWS.length; i++) {
            Label name = new Label(ROWS[i][0]);
            name.setStyle("-fx-font-size:11.5px; -fx-text-fill:#d0e8d0;");
            name.setMinWidth(115);

            Label mult = new Label(ROWS[i][1]);
            mult.setStyle("-fx-font-size:11.5px; -fx-font-weight:bold; -fx-text-fill:#f5d76e;");

            HBox row = new HBox(name, mult);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(3, 6, 3, 6));
            row.setStyle("-fx-background-radius:4;");

            nameLabels[i] = name;
            rowBoxes[i]   = row;
            getChildren().add(row);
        }
    }

    public void highlight(Hand.HandRank rank) {
        clearHighlight();
        int tier = rank.getTier();
        if (tier >= 0 && tier < TIER_TO_ROW.length) {
            int idx = TIER_TO_ROW[tier];
            rowBoxes[idx].setStyle(
                "-fx-background-color:rgba(245,215,110,0.25); -fx-background-radius:4;");
            nameLabels[idx].setStyle(
                "-fx-font-size:11.5px; -fx-text-fill:#f5d76e; -fx-font-weight:bold;");
        }
    }

    public void clearHighlight() {
        for (int i = 0; i < ROWS.length; i++) {
            rowBoxes[i].setStyle("-fx-background-radius:4;");
            nameLabels[i].setStyle("-fx-font-size:11.5px; -fx-text-fill:#d0e8d0;");
        }
    }
}
