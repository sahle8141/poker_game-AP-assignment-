package poker.ui;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Duration;
import poker.Hand;

/**
 * GameArea — the centre panel.
 * Owns the five CardViews, the result label, and the instruction line.
 * All animation logic for dealing and drawing lives here.
 */
public class GameArea extends VBox {

    private final CardView[] cardViews = new CardView[5];
    private final Label      resultLabel;
    private final Label      instructionLabel;

    public static final String INSTRUCTION_IDLE  = "Place your bet and deal to start!";
    public static final String INSTRUCTION_DEALT = "Select cards to HOLD, then click DRAW  " +
                                                   "(hold all and draw to stand pat)";
    public static final String INSTRUCTION_DONE  = "Deal for another hand!";

    public GameArea() {
        setAlignment(Pos.CENTER);
        setSpacing(22);
        setPadding(new Insets(30, 20, 20, 20));

        HBox cardsRow = new HBox(16);
        cardsRow.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            cardViews[i] = new CardView();
            cardsRow.getChildren().add(cardViews[i]);
        }

        resultLabel      = UiFactory.resultLabel();
        instructionLabel = UiFactory.instructionLabel(INSTRUCTION_IDLE);

        getChildren().addAll(resultLabel, cardsRow, instructionLabel);
    }

    // ── Card animations ───────────────────────────────────────────────────────

    /** Animate dealing all 5 cards from the given hand. */
    public void animateDeal(poker.Hand hand, Runnable onComplete) {
        resetCards();
        for (int i = 0; i < 5; i++) {
            final int idx = i;
            PauseTransition delay = new PauseTransition(Duration.millis(i * 120));
            delay.setOnFinished(e -> {
                cardViews[idx].setCard(hand.getCard(idx), true);
                cardViews[idx].setSelectable(true);
                cardViews[idx].setSelectedState(false);
            });
            delay.play();
        }
        if (onComplete != null) {
            PauseTransition done = new PauseTransition(Duration.millis(5 * 120 + 100));
            done.setOnFinished(e -> onComplete.run());
            done.play();
        }
    }

    /** Animate only the replaced cards; call onComplete when last card lands. */
    public void animateDraw(poker.Hand hand, java.util.List<Integer> replaced, Runnable onComplete) {
        for (CardView cv : cardViews) cv.setSelectable(false);

        for (int idx : replaced) {
            final int i = idx;
            PauseTransition delay = new PauseTransition(Duration.millis(idx * 100));
            delay.setOnFinished(e -> cardViews[i].setCard(hand.getCard(i), true));
            delay.play();
        }

        int lastDelay = replaced.isEmpty() ? 0 : (replaced.get(replaced.size() - 1) * 100 + 200);
        PauseTransition done = new PauseTransition(Duration.millis(lastDelay + 300));
        done.setOnFinished(e -> { if (onComplete != null) onComplete.run(); });
        done.play();
    }

    // ── Result display ────────────────────────────────────────────────────────

    /** Show the hand result with a pulse animation. */
    public void showResult(String message) {
        resultLabel.setStyle("-fx-font-family:'Georgia'; -fx-font-size:22px;" +
                             "-fx-font-weight:bold; -fx-text-fill:#f5d76e;" +
                             "-fx-effect:dropshadow(gaussian,#000,8,0,0,2);");
        resultLabel.setText(message);

        ScaleTransition pulse = new ScaleTransition(Duration.millis(200), resultLabel);
        pulse.setFromX(0.6); pulse.setToX(1.0);
        pulse.setFromY(0.6); pulse.setToY(1.0);
        pulse.play();
    }

    /** Show a temporary flash message (positive = green, false = red). */
    public void showFlash(String message, boolean positive) {
        resultLabel.setStyle("-fx-font-family:'Georgia'; -fx-font-size:15px;" +
                             "-fx-font-weight:bold; -fx-text-fill:" +
                             (positive ? "#7effa0" : "#ff7e7e") + ";");
        resultLabel.setText(message);

        PauseTransition clear = new PauseTransition(Duration.seconds(3));
        clear.setOnFinished(e -> {
            resultLabel.setText("");
            resultLabel.setStyle("-fx-font-family:'Georgia'; -fx-font-size:22px;" +
                                 "-fx-font-weight:bold; -fx-text-fill:#f5d76e;" +
                                 "-fx-effect:dropshadow(gaussian,#000,8,0,0,2);");
        });
        clear.play();
    }

    public void clearResult()                     { resultLabel.setText(""); }
    public void setInstruction(String text)       { instructionLabel.setText(text); }

    // ── Card hold query ───────────────────────────────────────────────────────

    /** Returns the list of card indices that are NOT held (i.e. to be replaced). */
    public java.util.List<Integer> getIndicesToReplace() {
        java.util.List<Integer> list = new java.util.ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (!cardViews[i].isSelected()) list.add(i);
        }
        return list;
    }

    // ── Reset ─────────────────────────────────────────────────────────────────

    public void resetCards() {
        for (CardView cv : cardViews) {
            cv.reset();
            cv.setSelectable(false);
        }
    }
}
