package emris.GameModel;


import emris.GameModel.Entity.Cell;
import emris.GameModel.Entity.Direction;
import emris.GameModel.Entity.Food;
import emris.GameModel.Entity.Snake;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TimerTask;

public class GameThread extends TimerTask {
    GraphicsContext renderer;
    ArrayList<Snake> snakes = new ArrayList<>();
    ArrayList<Food> food = new ArrayList<>();
    Snake playerSnake;
    final int HEIGHT_COUNT_CELLS;
    final int WIDTH_COUNT_CELLS;
    final int CELL_SIZE;

    public GameThread(GraphicsContext renderer, Scene scene, int widthCountCells, int HeightCountCells, int cellSize) {
        this.renderer = renderer;
        HEIGHT_COUNT_CELLS = HeightCountCells;
        WIDTH_COUNT_CELLS = widthCountCells;
        CELL_SIZE = cellSize;
        var x = widthCountCells / 2;
        var y = HeightCountCells / 2;
        playerSnake = new Snake(Color.CRIMSON);
        playerSnake.setDirection(Direction.UP);
        playerSnake.addCell(x, y);
        playerSnake.addCell(x, y + 1);////переписать с генереацией места выбора
        addFood(0, 0, Color.BLUE);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.W) {
                playerSnake.setDirection(Direction.UP);
                System.out.println("UP");
            }
            if (key.getCode() == KeyCode.A) {
                playerSnake.setDirection(Direction.LEFT);
                System.out.println("LEFT");
            }
            if (key.getCode() == KeyCode.S) {
                playerSnake.setDirection(Direction.DOWN);
                System.out.println("DOWN");
            }
            if (key.getCode() == KeyCode.D) {
                playerSnake.setDirection(Direction.RIGHT);
                System.out.println("RIGHT");
            }


        });
    }

    @Override
    public void run() {
        step();


        Platform.runLater(() -> {
            renderer.clearRect(0, 0, renderer.getCanvas().getWidth(), renderer.getCanvas().getHeight());
            renderer.strokeRect(0, 0, renderer.getCanvas().getHeight(), renderer.getCanvas().getWidth());
            renderer.setFill(playerSnake.getColor());
            for (Cell snakeCell : playerSnake.getCells()) {
                renderer.fillRect(snakeCell.getX() * CELL_SIZE, snakeCell.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                renderer.strokeRect(snakeCell.getX() * CELL_SIZE, snakeCell.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);


            }
            for (Food foodIter : food) {
                renderer.setFill(foodIter.getColor());

                renderer.fillOval(foodIter.getCell().getX() * CELL_SIZE, foodIter.getCell().getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                renderer.strokeOval(foodIter.getCell().getX() * CELL_SIZE, foodIter.getCell().getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }

        });


    }

    private void step() {
        changeState();
    }

    private void changeState() {
        playerSnake.move(HEIGHT_COUNT_CELLS, WIDTH_COUNT_CELLS);
        checkCollision();


    }
    

    private void addFood(int x, int y, Color color) {
        this.food.add(new Food(x, y, color));
    }

    private void checkCollision() {
        var deffFood= new LinkedList<Food>();
        for (Food foodIter : food) {
            if (foodIter.getCell().equals(playerSnake.getCells().get(0))){
                deffFood.add(foodIter);
                playerSnake.addCell(-1,-1);

            }
        }
        for(Food remFood: deffFood)
        food.removeIf(foodItem ->(foodItem == remFood));

    }

    private void AddSnake(int x, int y, Color color) {
        var snake = new Snake(color);
        snake.addCell(x, y);
        snake.addCell(x, y + 1);////переписать с генереацией места выбора
        snakes.add(snake);

    }


}
