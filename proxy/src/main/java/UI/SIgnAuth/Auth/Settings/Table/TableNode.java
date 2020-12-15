package UI.SIgnAuth.Auth.Settings.Table;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableNode {
    private SimpleStringProperty login;
    private SimpleStringProperty password;
    private  SimpleStringProperty WHO;

    private final StringProperty who = new SimpleStringProperty();

    public final StringProperty whoProperty() {
        return who;
    }

    public final String getWho() {
        return who.get();
    }

    public final void setWho(String value) {
        who.set(value);
    }


    public  TableNode(String login, String password, String who){
        this.login = new SimpleStringProperty(login);
        this.password = new SimpleStringProperty(password);
        setWho(who);

    }
    public String getLogin() {
        return login.get();
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }
}
