import UI.Proxy.ProxyScene;
import UI.SIgnAuth.Auth.AuthScene;
import UI.SIgnAuth.Auth.MainScene;
import UI.SIgnAuth.Auth.Settings.SettingsScene;
import UI.SIgnAuth.Auth.Settings.ShowUserScene;
import UI.SIgnAuth.Reg.RegScene;
import UI.SIgnAuth.WHO;
import UI.SIgnAuth.Users;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Main extends Application {
    AuthScene authScene;
    MainScene mainScene;
    static ProxyScene proxyScene;
    RegScene regScene;
    SettingsScene settingsScene;
    ShowUserScene showUserScene;
    ArrayList<Image> images = new ArrayList<>();
    Users userList = new Users();
    final  double H = 400;
    final  double W = 400;

    public Main() {
    }

    @Override
    public void init() throws Exception {
        super.init();
        userList.getDataFromFile(userList.getUserMap(),userList.getUserFile().getCanonicalPath());
        userList.getDataFromFile(userList.getAdminMap(),userList.getAdminFile().getCanonicalPath());
        userList.getDataFromFile(userList.getRootMap(),userList.getRootFile().getCanonicalPath());
        images.add(new Image("file:src/background/socks.jpg", W, H, false,true));
        ///add reg background;
        images.add(new Image("file:src/background/delaem-igrushku-iz-noska-3.png",W,H,false,true));
        ///add loggin background;
        images.add(new Image("file:src/background/depositphotos_32936495-stock-photo-cute-sock-puppet-on-green.jpg", W, H, false,true));
        ///add proxy background;
        images.add(new Image("file:src/background/sock-puppet.jpg", W, H, false,true));



    }

    public static void main(String[] args) {
        launch(args);
        if(proxyScene.getServerThread().isAlive())
        proxyScene.getServerThread().interrupt();


    }


    @Override
    public void start(Stage stage) throws IOException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException {
        stage.setTitle("ProxyServerApp");
        stage.setHeight(H);
        stage.setWidth(W);
        authScene = new AuthScene(stage.getMaxHeight(),stage.getMaxWidth(),images.get(2));
        mainScene = new MainScene(stage.getMaxHeight(),stage.getMaxWidth(),images.get(0));
        regScene = new RegScene(stage.getMaxHeight(),stage.getMaxWidth(),images.get(1));
        proxyScene = new ProxyScene(stage.getMaxHeight(),stage.getMaxWidth(),images.get(3));
        settingsScene = new SettingsScene(stage.getMaxHeight(),stage.getMaxWidth(),images.get(0),userList);
        showUserScene = new ShowUserScene(stage.getMaxHeight(),stage.getMaxWidth(),images.get(0));
        authScene.initLogbtn(userList,stage,mainScene.getScene());
        mainScene.initSwitchButton(mainScene.getRegBtn(),stage,regScene.getScene());
        mainScene.initSwitchButton(mainScene.getSettingsBtn(),stage,settingsScene.getScene(),userList);
        mainScene.initSwitchButton(mainScene.getProxyBtn(),stage,proxyScene.getScene());
        mainScene.initSwitchButton(mainScene.getShowUsersBtn(),stage,showUserScene.getScene(),userList);
        proxyScene.initSwitchButton(proxyScene.getBack(),stage,mainScene.getScene());
        proxyScene.initTurnOnBtn(userList.getUserMap());
        proxyScene.initTurnOffBtn();
        proxyScene.initSelector();
        regScene.initSwitchButton(regScene.getBack(),stage,mainScene.getScene());
        regScene.initSigBtn(regScene.getAddAdminBtn(),userList, WHO.ADMIN);
        regScene.initSigBtn(regScene.getAddUserBtn(),userList,WHO.USER);
        settingsScene.initSwitchButton(settingsScene.getBack(),stage,mainScene.getScene());
        settingsScene.initchPwBtn();
        settingsScene.initDelUserbtn();
        showUserScene.initSwitchButton(showUserScene.getBack(),stage,mainScene.getScene());
        showUserScene.initAdminBtn(userList.getAdminMap());
        showUserScene.initUserBtn(userList.getUserMap());
        stage.setScene(authScene.getScene());

        stage.show();
    }
}
