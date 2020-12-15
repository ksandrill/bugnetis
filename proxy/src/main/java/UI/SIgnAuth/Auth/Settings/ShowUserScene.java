package UI.SIgnAuth.Auth.Settings;

import Proxy.MOD;
import UI.SIgnAuth.Auth.Settings.Table.Table;
import UI.SIgnAuth.Users;
import UI.SIgnAuth.WHO;
import UI.SceneSheet;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;

import java.util.HashMap;
import java.util.Map;

public class ShowUserScene extends SceneSheet {
    ToggleGroup group;
    Button adminBtn;
    Button userBtn;
    Table table;

    public Button getBack() {
        return back;
    }

    Button back;

    public ShowUserScene(double h, double w, Image image) {
        super(h, w, image);
        back = new Button("back");
        table = new Table();
        group = new ToggleGroup();
        adminBtn = new Button("show admins");
        userBtn = new Button("show users");
        FlowPane root = new FlowPane(10, 10);
        root.getChildren().addAll(adminBtn, userBtn, table.getTable());
        elements.getChildren().addAll(back, root);
    }
    public void initAdminBtn(HashMap<String,String>map) {
        adminBtn.setOnAction(e -> {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                table.addRow(entry,WHO.ADMIN);
            }

        });
    }
    public void initUserBtn(HashMap<String,String>map) {
        userBtn.setOnAction(e -> {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                table.addRow(entry,WHO.USER);
            }

        });
    }
    /*

    }


     */






}
