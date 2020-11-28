package emris.GameModel;

import emris.GameModel.GUI.Table.Table;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Timer;

public class App extends Application {
    Timer timer = new Timer(true);
    final long TIMEOUT = 500;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("fucking snakes");
        primaryStage.setHeight(600);
        primaryStage.setWidth(1024);
        Object gameLock = new Object();

        HBox root = new HBox();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        Canvas canvas = new Canvas(600, 600);
        var table = new Table();
        root.getChildren().addAll(canvas, table.getTable());
        GraphicsContext renderer = canvas.getGraphicsContext2D();
        var gameDrawer = new GameDrawer(renderer, table, 30, gameLock);

        timer.scheduleAtFixedRate(new Game(callback -> theScene.addEventFilter(KeyEvent.KEY_PRESSED, callback), 20, 20, gameDrawer, gameLock), 0, TIMEOUT);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }

}
