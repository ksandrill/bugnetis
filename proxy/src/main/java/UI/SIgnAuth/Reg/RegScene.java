package UI.SIgnAuth.Reg;

import UI.SIgnAuth.CHECKER;
import UI.SIgnAuth.Users;
import UI.SIgnAuth.WHO;
import UI.SceneSheet;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class RegScene extends SceneSheet {
    TextField login;
    TextField password;
    Label loginLabel;
    Label passwordLabel;


    public Button getAddUserBtn() {
        return AddUserBtn;
    }

    Button AddUserBtn;

    public Button getAddAdminBtn() {
        return AddAdminBtn;
    }

    Button AddAdminBtn;

    public Button getBack() {
        return back;
    }

    Button back;
    public RegScene(double h, double w, Image image) {
        super(h, w, image);
        back = new Button("Back");
        loginLabel = new Label("Login");
        login = new TextField();
        passwordLabel = new Label("Password");
        password = new TextField();
        AddAdminBtn = new Button("Create privileged user");
        AddUserBtn = new Button("create user");

        VBox box = new VBox();
        FlowPane sigIn = new FlowPane();
        sigIn.getChildren().addAll(AddUserBtn,AddAdminBtn);
        box.getChildren().addAll(back,loginLabel,login,passwordLabel,password,sigIn);
        elements.getChildren().add(box);
    }

    public void initSigBtn(Button btn, Users users, WHO who){
        btn.setOnAction(event -> {
            try {
                CHECKER state;
                String text;
                if(who == WHO.USER){
                    state = users.addUsers(users.getUserMap(),login.getText(),password.getText(),users.getUserFile());

                }else {
                    state = users.addUsers(users.getAdminMap(),login.getText(),password.getText(),users.getAdminFile());
                }
                switch (state){
                    case CORRECT:{
                        text = "SUCCESS";
                        break;
                    }
                    case INCORRECT_ALL:
                    {
                        text = "INCORRECT ALL";
                        break;
                    }
                    case INCORRECT_LOGIN:{
                        text = "INCORRECT LOGIN";
                        break;
                    }
                    case INCORRECT_PASSWORD:{
                        text = "INCORRECT PASSWORD";
                        break;
                    }
                    case USER_EXIST:{
                        text = "USER ALREADY EXISTS";
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


            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }


        });


    }
}
