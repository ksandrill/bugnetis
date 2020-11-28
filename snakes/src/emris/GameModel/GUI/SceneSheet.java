package emris.GameModel.GUI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SceneSheet {
    public Scene getScene() {
        return scene;
    }

    protected Scene scene;
    protected Background background;
    protected VBox elements;
    protected double h, w;

    public SceneSheet(double h, double w, Image image) {
        elements = new VBox();
        scene = new Scene(elements, h, w);
        this.h = h;
        this.w = w;
        setBackground(image);
    }

    private void setBackground(Image image) {
        BackgroundImage aux = new BackgroundImage(image,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        background = new Background(aux);
        elements.setBackground(background);
    }

    ;

    public void initSwitchButton(Button button, Stage stage, Scene newScene) {
        button.setOnAction(event -> stage.setScene(newScene));

    }

}
