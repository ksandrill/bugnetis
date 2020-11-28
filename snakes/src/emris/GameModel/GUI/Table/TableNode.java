package emris.GameModel.GUI.Table;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableNode {
    private SimpleStringProperty name;
    private SimpleIntegerProperty score;

    public void setName(String name) {
        this.name.set(name);
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getScore() {
        return score.get();
    }

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }

    public TableNode(String name) {
        this.name = new SimpleStringProperty(name);
        score = new SimpleIntegerProperty(0);
    }

    public TableNode(String name, int score) {
        this.name = new SimpleStringProperty(name);
        this.score = new SimpleIntegerProperty(score);
    }
}



