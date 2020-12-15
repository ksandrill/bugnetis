package UI.SIgnAuth.Auth.Settings;

import UI.SIgnAuth.Auth.Settings.Table.Table;
import UI.SIgnAuth.Users;
import UI.SIgnAuth.WHO;
import UI.SceneSheet;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class SettingsScene extends SceneSheet {
    public Button getBack() {
        return back;
    }
    Settings settings;
    Button back;
    Label loginL;
    Label passwordL;
    TextField loginF;
    TextField passwordF;
    Button changePassBtn;
    Button deleteUserBtn;

    public SettingsScene(double h, double w, Image image,Users users) {
        super(h, w, image);
        back = new Button("back");
        settings = new Settings(users);
        loginL = new Label("Login");
        passwordL = new Label("Password");
        loginF = new TextField();
        passwordF = new TextField();
        changePassBtn = new Button("change password\n(write in password field \nnew password)");
        deleteUserBtn = new Button(" Delete user \n(write only his login    \n   press button)");
        VBox box  = new VBox();
        box.getChildren().addAll(loginL,loginF,passwordL,passwordF,changePassBtn,deleteUserBtn);


        elements.getChildren().addAll(back,box);
        elements.setOrientation(Orientation.VERTICAL);
    }
   public void initDelUserbtn(){
        deleteUserBtn.setOnAction(e->{
            String login = loginF.getText();
            WHO who;
            if(login.equals("root")){
                who = WHO.ROOT;
            }else if(settings.users.getUserMap().containsKey(login)){
                who = WHO.USER;
            } else {
                who = WHO.ADMIN;
            }
            if(who!=WHO.ROOT){
                try {
                    settings.deleteUser(login,who);
                    loginF.clear();
                    passwordF.clear();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InvalidKeyException ex) {
                    ex.printStackTrace();
                } catch (BadPaddingException ex) {
                    ex.printStackTrace();
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                } catch (IllegalBlockSizeException ex) {
                    ex.printStackTrace();
                } catch (NoSuchPaddingException ex) {
                    ex.printStackTrace();
                }
            }

        });
    }

   public void initchPwBtn(){
        changePassBtn.setOnAction(e->{
            String login = loginF.getText();
            String password = passwordF.getText();
            WHO who;
            if(login.equals("root")){
                who = WHO.ROOT;
            }else if(settings.users.getUserMap().containsKey(login)){
                who = WHO.USER;
            } else {
                who = WHO.ADMIN;
            }
            try {
                settings.changePassword(login,password,who);
                loginF.clear();
                passwordF.clear();
            } catch (BadPaddingException ex) {
                ex.printStackTrace();
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (IllegalBlockSizeException ex) {
                ex.printStackTrace();
            } catch (NoSuchPaddingException ex) {
                ex.printStackTrace();
            } catch (InvalidKeyException ex) {
                ex.printStackTrace();
            }
        });

    }




}



 class Settings {
    Users users;
    public Settings(Users userlist){
        users = userlist;

    }

  public   void changePassword(String login,String newPassword, WHO who) throws BadPaddingException, NoSuchAlgorithmException, IOException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
       if(who!=WHO.ROOT) {
           deleteUser(login, who);
           users.addUsers(getMap(who), login, newPassword, getFile(who));
       } else {
           users.getRootMap().remove("root");
           users.putDataInFile(users.getAdminMap(),users.getAdminFile().getCanonicalPath());
           users.addUsers(users.getAdminMap(),"root",newPassword,users.getAdminFile());

       }
    }

    HashMap<String,String> getMap(WHO who){
        return  who ==WHO.USER?users.getUserMap():users.getAdminMap();
    }
    String getPath(WHO who) throws IOException {
        return who == WHO.USER?users.getUserFile().getCanonicalPath():   users.getAdminFile().getCanonicalPath();
    }
    File getFile(WHO who){
        return who == WHO.USER?users.getUserFile():   users.getAdminFile();

    }

   public   void deleteUser(String login,WHO who) throws IOException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException {
         HashMap<String,String> map = getMap(who);
         map.remove(login);
         String pathName = getPath(who);
        users.putDataInFile(map,pathName);


    }

}
