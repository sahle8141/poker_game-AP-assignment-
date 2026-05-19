package poker.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import poker.*;

import java.util.List;


public class PokerApp extends Application implements ControlBar.ButtonActions {

    private static final int STARTING_CHIPS = 1000;

    private GameEngine engine;
    private Player     player;


    private HeaderBar     headerBar;
    private GameArea      gameArea;
    private PaytablePanel paytablePanel;
    private StatsPanel    statsPanel;
    private ControlBar    controlBar;


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




    private void refreshAll() {
        headerBar .refresh(player);
        statsPanel.refresh(player);
        controlBar.setMaxBet(player.getChips());
    }



    public static void main(String[] args) {
        launch(args);
    }
}
