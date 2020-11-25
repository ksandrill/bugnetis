package emris.GameModel;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.Timer;

public class App extends Application {
    Timer timer = new Timer(true);
    final long TIMEOUT = 250;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("fucking snakes");
        primaryStage.setHeight(600);
        primaryStage.setWidth(1024);

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        Canvas canvas = new Canvas(600, 600);
        root.getChildren().add(canvas);
        GraphicsContext renderer = canvas.getGraphicsContext2D();
        timer.scheduleAtFixedRate(new Game(renderer, theScene, 20, 20, 30), 0, TIMEOUT);
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }

}
