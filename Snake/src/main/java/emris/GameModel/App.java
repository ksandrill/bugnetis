package emris.GameModel;

import Network.NetNode;
import Network.SendUtils.AnnouncementSendMaster;
import Network.SendUtils.SendSlave;
import emris.GameModel.Table.Table;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App extends Application {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
    NetNode netNode;
    MasterConfig config;


    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("fucking snakes");
        primaryStage.setHeight(600);
        primaryStage.setWidth(1024);
        Object gameLock = new Object();
        config = new MasterConfig(20, 20, 3, 0.5f, 500, 0.3f, 1000, 500);

        HBox root = new HBox();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        Canvas canvas = new Canvas(600, 600);
        var table = new Table();
        root.getChildren().addAll(canvas, table.getTable());
        GraphicsContext renderer = canvas.getGraphicsContext2D();

        netNode = new NetNode(8080);
        var gameDrawer = new GameDrawer(renderer, table, 30, gameLock);
        var game = new Game(callback -> theScene.addEventFilter(KeyEvent.KEY_PRESSED, callback), config, gameDrawer, gameLock);

        netNode.initSendSlave();
        netNode.InitRecvSlave();
        netNode.initMulticastRecvSlave();
        netNode.initRecvMaster();
        netNode.initAnnouncementSendMaster(game.getConfig(), game.getGamePlayers(), executorService);
        netNode.initPingSendMaster(0, config.pingDelayMs, executorService);
        executorService.scheduleAtFixedRate(game, 0, config.getStateDelayMs(), TimeUnit.MILLISECONDS);

        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            executorService.shutdown();
        });


    }


    public static void main(String[] args) {
        launch(args);
    }

}
