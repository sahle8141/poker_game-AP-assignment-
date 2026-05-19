package poker.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import poker.Card;

/**
 * JavaFX component representing one playing card.
 * Supports: face-up/face-down, selected (hold) toggle, flip animation.
 */
public class CardView extends StackPane {

    private static final double CARD_W = 95;
    private static final double CARD_H = 135;
    private static final double RADIUS  = 10;

    private Card    card;
    private boolean selected  = false;   // "hold" selection
    private boolean faceUp    = false;

    // Layers
    private final StackPane frontPane = new StackPane();
    private final StackPane backPane  = new StackPane();
    private final Label     holdLabel = new Label("HOLD");

    public CardView() {
        setMinSize(CARD_W, CARD_H);
        setMaxSize(CARD_W, CARD_H);
        setPrefSize(CARD_W, CARD_H);
        buildBack();
        buildFront();
        buildHoldLabel();
        getChildren().addAll(backPane, frontPane, holdLabel);
        showBack();
        setCursor(Cursor.DEFAULT);
    }

    // ── Public API ────────────────────────────────────────────────────────────

    public void setCard(Card card, boolean animate) {
        this.card   = card;
        this.faceUp = false;
        refreshFront();
        if (animate) {
            flipIn();
        } else {
            showFront();
        }
    }

    public void setSelectable(boolean selectable) {
        if (selectable) {
            setCursor(Cursor.HAND);
            setOnMouseClicked(e -> toggleSelected());
        } else {
            setCursor(Cursor.DEFAULT);
            setOnMouseClicked(null);
        }
    }

    public void setSelectedState(boolean sel) {
        selected = sel;
        applySelectionStyle();
    }

    public boolean isSelected() { return selected; }

    public void toggleSelected() {
        selected = !selected;
        applySelectionStyle();
    }

    public void showBack() {
        frontPane.setVisible(false);
        backPane.setVisible(true);
        holdLabel.setVisible(false);
    }

    public void showFront() {
        frontPane.setVisible(true);
        backPane.setVisible(false);
    }

    public void reset() {
        card     = null;
        selected = false;
        showBack();
        holdLabel.setVisible(false);
        setEffect(null);
        setTranslateY(0);
    }

    // ── Build helpers ─────────────────────────────────────────────────────────

    private void buildBack() {
        Rectangle bg = roundRect(CARD_W, CARD_H, "#1a1a6e");
        // Simple pattern using inner rectangle
        Rectangle inner = roundRect(CARD_W - 10, CARD_H - 10, "transparent");
        inner.setStroke(Color.web("#3a3aae", 0.5));
        inner.setStrokeWidth(1);
        Label logo = new Label("♠");
        logo.setStyle("-fx-font-size:36px; -fx-text-fill: #3a3aae;");
        backPane.getChildren().addAll(bg, inner, logo);
        backPane.setAlignment(Pos.CENTER);
        applyCardShadow(backPane);
    }

    private void buildFront() {
        Rectangle bg = roundRect(CARD_W, CARD_H, "#f8f5ee");
        frontPane.getChildren().add(bg);
        frontPane.setAlignment(Pos.CENTER);
        applyCardShadow(frontPane);
    }

    private void buildHoldLabel() {
        holdLabel.setStyle(
            "-fx-font-size:10px; -fx-font-weight:bold;" +
            "-fx-text-fill:#ffffff; -fx-background-color:#c0392b;" +
            "-fx-padding:2 5 2 5; -fx-background-radius:3;"
        );
        holdLabel.setVisible(false);
        StackPane.setAlignment(holdLabel, Pos.TOP_CENTER);
        holdLabel.setTranslateY(-58);
    }

    private void refreshFront() {
        if (card == null) return;
        frontPane.getChildren().clear();

        Rectangle bg = roundRect(CARD_W, CARD_H, "#f8f5ee");
        applyCardShadow(frontPane);

        boolean red = card.getSuit().isRed();
        String  color = red ? "#c0392b" : "#1a1a2e";

        // Top-left rank + suit
        Label topLabel = cornerLabel(card.getRank().getSymbol() + "\n" + card.getSuit().getSymbol(), color, 13);
        StackPane.setAlignment(topLabel, Pos.TOP_LEFT);
        topLabel.setTranslateX(6);
        topLabel.setTranslateY(4);

        // Bottom-right (rotated 180°)
        Label botLabel = cornerLabel(card.getRank().getSymbol() + "\n" + card.getSuit().getSymbol(), color, 13);
        StackPane.setAlignment(botLabel, Pos.BOTTOM_RIGHT);
        botLabel.setTranslateX(-6);
        botLabel.setTranslateY(-4);
        botLabel.setRotate(180);

        // Center suit symbol (large)
        Label center = new Label(card.getSuit().getSymbol());
        center.setStyle("-fx-font-size:42px; -fx-text-fill:" + color + ";");
        StackPane.setAlignment(center, Pos.CENTER);

        // Center rank overlay (smaller, subtle)
        Label rankCenter = new Label(card.getRank().getSymbol());
        rankCenter.setStyle("-fx-font-size:18px; -fx-font-weight:bold; -fx-text-fill:" + color + "88;");
        StackPane.setAlignment(rankCenter, Pos.CENTER);
        rankCenter.setTranslateY(8);

        frontPane.getChildren().addAll(bg, topLabel, botLabel, center, rankCenter);
    }

    private void applySelectionStyle() {
        if (selected) {
            setTranslateY(-12);
            holdLabel.setVisible(true);
            DropShadow glow = new DropShadow(15, Color.web("#f1c40f"));
            setEffect(glow);
        } else {
            setTranslateY(0);
            holdLabel.setVisible(false);
            setEffect(null);
        }
    }

    // ── Flip animation ────────────────────────────────────────────────────────

    private void flipIn() {
        // Scale X from 0 → 1 (fold-in effect)
        ScaleTransition fold = new ScaleTransition(Duration.millis(120), this);
        fold.setFromX(1.0);
        fold.setToX(0.0);
        fold.setOnFinished(e -> {
            showFront();
            ScaleTransition unfold = new ScaleTransition(Duration.millis(120), this);
            unfold.setFromX(0.0);
            unfold.setToX(1.0);
            unfold.play();
        });
        fold.play();
    }

    // ── Util ──────────────────────────────────────────────────────────────────

    private Rectangle roundRect(double w, double h, String fill) {
        Rectangle r = new Rectangle(w, h);
        r.setArcWidth(RADIUS * 2);
        r.setArcHeight(RADIUS * 2);
        if (fill.equals("transparent")) {
            r.setFill(Color.TRANSPARENT);
        } else {
            r.setFill(Color.web(fill));
        }
        return r;
    }

    private Label cornerLabel(String text, String color, int size) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size:" + size + "px; -fx-font-weight:bold;" +
                   "-fx-text-fill:" + color + "; -fx-line-spacing:-4;");
        l.setAlignment(Pos.CENTER);
        return l;
    }

    private void applyCardShadow(Pane pane) {
        DropShadow shadow = new DropShadow(8, 2, 3, Color.web("#00000055"));
        pane.setEffect(shadow);
    }
}
