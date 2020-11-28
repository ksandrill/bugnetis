package emris.GameModel.GUI.Scenes;

import emris.GameModel.GUI.SceneSheet;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class MainMenu extends SceneSheet {
    Button gameBtn;
    Button infoBtn;
    Button exitBtn;
    public MainMenu(double h, double w, Image image) {
        super(h, w, image);
        gameBtn = new Button("game");
        infoBtn = new Button("info");
        exitBtn = new Button("exit");
        elements.getChildren().addAll(gameBtn,infoBtn,exitBtn);
    }
}
