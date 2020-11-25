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
import java.util.Random;
import java.util.TimerTask;

public class Game extends TimerTask {
    GraphicsContext renderer;
    ArrayList<Snake> snakes = new ArrayList<>();
    ArrayList<Food> food = new ArrayList<>();
    Snake playerSnake;
    final int HEIGHT_COUNT_CELLS;
    final int WIDTH_COUNT_CELLS;
    final int CELL_SIZE;
    final double FOOD_PER_PLAYER = 1;
    final int FOOD_STATIC = 10;
    final int ALIVE = 1; /// заглушка


    public Game(GraphicsContext renderer, Scene scene, int widthCountCells, int HeightCountCells, int cellSize) {
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
        createFood();
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
        createFood();
        playerSnake.move(HEIGHT_COUNT_CELLS, WIDTH_COUNT_CELLS);
        checkCollision();


    }


    private void createFood() {
        int foodCount = (int) (FOOD_STATIC + FOOD_PER_PLAYER * ALIVE);
        int curFood = food.size();
        foodCount -= curFood;
        int curCount = 0;
        boolean isEmptyCell;
        Random rand = new Random();
        while (curCount != foodCount) {
            int x = rand.nextInt(WIDTH_COUNT_CELLS - 1);
            int y = rand.nextInt(HEIGHT_COUNT_CELLS - 1);
            isEmptyCell = true;
            System.out.println(x + " " + y);
            for (Food foodIter : food) {
                var foodCell = foodIter.getCell();
                if (foodCell.equalsCoords(x, y)) {
                    isEmptyCell = false;
                }

            }
            if (playerSnake.isMyPartCoords(x, y)) {
                isEmptyCell = false;
                System.out.println("food is lost");

            }
            if (isEmptyCell) {
                System.out.println("add food");
                this.food.add(new Food(x, y, Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble())));
                ++curCount;
            }
        }
        System.out.println("all food is created");
    }

    private void checkCollision() {
        checkEatFood(playerSnake);
        if (playerSnake.isEatSelf()) {
            playerSnake = null;
        }
    }


    private void checkEatFood(Snake snake) {
        var deffFood = new LinkedList<Food>();
        for (Food foodIter : food) {
            if (foodIter.getCell().equals(snake.getCells().get(0))) {
                deffFood.add(foodIter);
                snake.addCell(-1, -1);
            }
        }
        for (Food remFood : deffFood)
            food.removeIf(foodItem -> (foodItem == remFood));
        deffFood.clear();
    }


}
