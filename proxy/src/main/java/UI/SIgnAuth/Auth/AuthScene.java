package UI.SIgnAuth.Auth;

import UI.SIgnAuth.CHECKER;
import UI.SIgnAuth.Users;
import UI.SceneSheet;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AuthScene extends SceneSheet {
    Button logInBtn;
    TextField login;
    TextField password;
    Label loginLabel;
    Label passwordLabel;
    public AuthScene(double h, double w, Image image) {
        super(h, w, image);
        loginLabel = new Label("Login");
        login = new TextField();
        passwordLabel = new Label("Password");
        password = new TextField();
        logInBtn = new Button("Log in");
        VBox  box = new VBox();
        box.getChildren().addAll(loginLabel,login,passwordLabel,password,logInBtn);
        elements.getChildren().add(box);
        FlowPane.setMargin(box,new Insets(10,0,0,0));
    }
    public void initLogbtn(Users users, Stage stage, Scene scene){
        logInBtn.setOnAction(event -> {
            String text;
            CHECKER state = users.CheckAuth(login.getText(),password.getText());
            switch (state){
                case CORRECT:{
                    text = "SUCCESS";
                    break;
                }

                case INCORRECT_ALL:{
                    text = "SOMETHING WENT WRONG!!!";
                    break;
                }



                default:
                    throw new IllegalStateException("Unexpected value: " + state);
            }
            login.clear();
            password.clear();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText(text);

            alert.showAndWait();
            if(state == CHECKER.CORRECT){
                stage.setScene(scene);
            }


        });

    }
}
