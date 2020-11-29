package emris.GameModel.Entity;

import javafx.scene.paint.Color;

public class Food {
    private Color color;
    private Cell cell;

    public Food(int x, int y, Color color) {
        this.color = color;
        this.cell = new Cell(x, y);
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


}
