package poker.ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;


public class UiFactory {


    public static Button actionButton(String text, String bgColor, String hoverColor) {
        Button btn = new Button(text);
        String base  = buttonStyle(bgColor);
        String hover = buttonStyle(hoverColor);
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited (e -> btn.setStyle(base));
        return btn;
    }

    private static String buttonStyle(String bg) {
        return "-fx-background-color:" + bg + ";" +
               "-fx-text-fill:white; -fx-font-weight:bold;" +
               "-fx-font-size:13px; -fx-padding:9 22 9 22;" +
               "-fx-background-radius:6; -fx-cursor:hand;" +
               "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.4),4,0,1,2);";
    }

    public static Label headerLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family:'Georgia'; -fx-font-size:26px;" +
                   "-fx-font-weight:bold; -fx-text-fill:#f5d76e;" +
                   "-fx-effect:dropshadow(gaussian,#000,6,0,1,2);");
        return l;
    }

    public static Label chipLabel(String text, String color) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size:14px; -fx-font-weight:bold; -fx-text-fill:" + color + ";");
        return l;
    }

    public static Label resultLabel() {
        Label l = new Label("");
        l.setStyle("-fx-font-family:'Georgia'; -fx-font-size:22px;" +
                   "-fx-font-weight:bold; -fx-text-fill:#f5d76e;" +
                   "-fx-effect:dropshadow(gaussian,#000,8,0,0,2);");
        l.setMinHeight(36);
        return l;
    }

    public static Label instructionLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size:13px; -fx-text-fill:#c0e0c0; -fx-opacity:0.85;");
        return l;
    }

    public static Label panelTitle(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-family:'Georgia'; -fx-font-size:13px;" +
                   "-fx-font-weight:bold; -fx-text-fill:#f5d76e;" +
                   "-fx-padding:0 0 8 0;");
        return l;
    }

    public static Label statLine(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size:12px; -fx-text-fill:#a0c8a0;");
        return l;
    }




    public static Region hSpacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }

  public static Background feltBackground() {
        return new Background(new BackgroundFill(
            new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#0d4f1c")),
                new Stop(1, Color.web("#072a0e"))),
            CornerRadii.EMPTY,
            javafx.geometry.Insets.EMPTY));
    }
}
