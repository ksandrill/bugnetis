package UI.SIgnAuth.Auth;

import UI.SIgnAuth.Auth.Settings.ShowUserScene;
import UI.SIgnAuth.Users;
import UI.SceneSheet;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class MainScene extends SceneSheet {
    public Button getProxyBtn() {
        return proxyBtn;
    }
    public Button getRegBtn() {
        return regBtn;
    }

    Button proxyBtn;
    Button regBtn;

    public Button getShowUsersBtn() {
        return showUsersBtn;
    }

    Button showUsersBtn;

    public Button getSettingsBtn() {
        return settingsBtn;
    }

    Button settingsBtn;


    public MainScene(double h, double w, Image image) {
        super(h, w, image);
        regBtn = new Button("Create new user");
        settingsBtn = new Button("Settings");
        proxyBtn = new Button("Proxy");
        showUsersBtn = new Button("Users");
        elements.getChildren().addAll(regBtn,settingsBtn,showUsersBtn,proxyBtn);



    }

    public void initSwitchButton(Button button, Stage stage, Scene newScene,Users users) {

            button.setOnAction(event -> {
                if(users.isRootFlag()) {
                    stage.setScene(newScene);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);

                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("ROOT ONLY");

                    alert.showAndWait();
                }

            });


        }

    }






