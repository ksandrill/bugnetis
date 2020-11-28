package emris.GameModel;

import emris.GameModel.Entity.Cell;
import emris.GameModel.Entity.Food;
import emris.GameModel.Entity.Snake;
import emris.GameModel.GUI.Table.Table;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;

public class GameDrawer {
    GraphicsContext renderer;
    Table table;

    final int CELL_SIZE;

    public GameDrawer(GraphicsContext renderer, Table table, int CELL_SIZE) {
        this.renderer = renderer;
        this.CELL_SIZE = CELL_SIZE;
        this.table = table;
    }

    public void draw(Collection<Food> food, Collection<Snake> snakes) {
        Platform.runLater(() -> {
            updateScore(snakes);
            renderer.clearRect(0, 0, renderer.getCanvas().getWidth(), renderer.getCanvas().getHeight());
            renderer.strokeRect(0, 0, renderer.getCanvas().getHeight(), renderer.getCanvas().getWidth());
            renderSnakes(snakes);
            renderFood(food);

        });
    }

    public void addToTable(Snake snake) {
        table.addRow(snake.getPlayerName(), 0);
    }


    private void renderSnakes(Collection<Snake> snakes) {
        for (Snake snake : snakes) {
            renderSnake(snake);
        }
    }

    private void updateScore(Collection<Snake> snakes) {
        for (Snake snake : snakes) {
            System.out.println(snake.getPlayerName() + ": " + snake.getScore());
            table.UpdateScore(snake.getPlayerName(), snake.getScore());
        }

    }

    private String subPlayerName(String playerName, int limit) {
        return playerName.codePointCount(0, playerName.length()) > limit ?
                playerName.substring(0, playerName.offsetByCodePoints(0, limit)) + "..." : playerName;

    }

    private void renderSnake(Snake snake) {
        renderer.setFill(snake.getColor());
        for (Cell snakeCell : snake.getCells()) {
            renderer.fillRect(snakeCell.getX() * CELL_SIZE + 1, snakeCell.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            renderer.strokeRect(snakeCell.getX() * CELL_SIZE, snakeCell.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
        renderer.strokeText(subPlayerName(snake.getPlayerName(), 5), snake.getCells().get(0).getX() * CELL_SIZE, snake.getCells().get(0).getY() * CELL_SIZE);
    }

    private void renderFood(Collection<Food> food) {
        for (Food foodIter : food) {
            renderer.setFill(foodIter.getColor());
            renderer.fillOval(foodIter.getCell().getX() * CELL_SIZE, foodIter.getCell().getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            renderer.strokeOval(foodIter.getCell().getX() * CELL_SIZE, foodIter.getCell().getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }
}
