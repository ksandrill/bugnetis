package UI.Proxy;

import Proxy.Proxy;
import UI.SceneSheet;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import Proxy.MOD;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.util.HashMap;

public class ProxyScene extends SceneSheet {
    Button turnOn;
    Button turnOff;
    RadioButton AUTH;
    RadioButton NO_AUTH;
    Button selectBtn;
    ToggleGroup group;

    Thread serverThread;
    short flag = 1;
    MOD serverMOD = null;
    public Button getBack() {
        return back;
    }

    Button back;

    public Thread getServerThread() {
        return serverThread;
    }

    public ProxyScene(double h, double w, Image image) throws IOException {
        super(h, w, image);
        back = new Button("Back");
        turnOn = new Button("Turn On");
        turnOff = new Button("Turn OFF");
        AUTH = new RadioButton("AUTH");
        NO_AUTH = new RadioButton("NO AUTH");
         group = new ToggleGroup();
        AUTH.setToggleGroup(group);
        NO_AUTH.setToggleGroup(group);
        selectBtn = new Button("Select");
        FlowPane root = new FlowPane(Orientation.VERTICAL, 10, 10);
        root.getChildren().addAll(AUTH,NO_AUTH,selectBtn);

        elements.getChildren().addAll(back,root, turnOn, turnOff);
        elements.setPadding((new Insets(10, 10, 10, 10)));


    }
    public  void initSelector(){
        selectBtn.setOnAction(event -> {
            RadioButton selection = (RadioButton) group.getSelectedToggle();
            if(selection == NO_AUTH){
                serverMOD = MOD.NO_AUTH;

            }else {
                serverMOD = MOD.AUTH;
            }
        });
    }


    public void initTurnOnBtn(HashMap<String, String> users) {

        turnOn.setOnAction(e -> {
            try {
                if(serverMOD == null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("WARNING");
                    alert.setHeaderText(null);
                    alert.setContentText("PLEASE SET METHOD of SERVER(NO AUTH OR AUTH");

                    alert.showAndWait();

                }
                else if (flag == 1) {
                    serverThread = new Thread(new Proxy(1080, users, MOD.NO_AUTH), "serverThread");
                    serverThread.start();
                    flag = 0;
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });


    }

    public void initTurnOffBtn() {
        turnOff.setOnAction(e -> {
            if (flag == 0) {
                System.out.println("close  thread");
                serverThread.interrupt();
                flag = 1;

            }


        });

    }
}