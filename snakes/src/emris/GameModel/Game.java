package emris.GameModel;


import emris.GameModel.Entity.Cell;
import emris.GameModel.Entity.Direction;
import emris.GameModel.Entity.Food;
import emris.GameModel.Entity.Snake;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Game extends TimerTask {
    ArrayList<Snake> snakes = new ArrayList<>();
    ArrayList<Food> food = new ArrayList<>();
    final int HEIGHT_COUNT_CELLS;
    final int WIDTH_COUNT_CELLS;
    final double FOOD_PER_PLAYER = 1;
    final int FOOD_STATIC = 5;
    final float DEAD_FOOD_PROB = 0.9F;
    final GameDrawer drawer;
    final Object gameLock;


    public Game(final Consumer<EventHandler<KeyEvent>> keyBindConsumer, int widthCountCells, int HeightCountCells, GameDrawer gameDrawer, Object gameLock) {
        HEIGHT_COUNT_CELLS = HeightCountCells;
        WIDTH_COUNT_CELLS = widthCountCells;
        this.gameLock = gameLock;
        var playerSnake = createSnake("dad", Color.CRIMSON, WIDTH_COUNT_CELLS / 2, HeightCountCells / 2);
        var playerSnake1 = createSnake("boy", Color.GREEN, 2, 2);
        snakes.add(playerSnake);
        snakes.add(playerSnake1);
        addKeyHandler(keyBindConsumer, KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, playerSnake1);
        addKeyHandler(keyBindConsumer, KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, playerSnake);
        createFood();
        drawer = gameDrawer;
        drawer.addToTable(playerSnake);
        drawer.addToTable(playerSnake1);

    }

    @Override
    public void run() {
        masterStep();
        drawer.draw(food, snakes);

    }


    private Snake createSnake(String playerName, Color color, int x, int y) {
        var snake = new Snake(playerName, color);
        snake.setDirection(Direction.UP);
        snake.addCell(x, y);
        snake.addCell(x, y + 1);
        return snake;
    }

    private void addKeyHandler(final Consumer<EventHandler<KeyEvent>> keyBindConsumer, KeyCode up, KeyCode down, KeyCode left, KeyCode right, Snake snake) {
        keyBindConsumer.accept(key -> {
            if (key.getCode() == up) {
                snake.setDirection(Direction.UP);
                System.out.println("UP");

            }
            if (key.getCode() == left) {
                snake.setDirection(Direction.LEFT);
                System.out.println("LEFT");

            }
            if (key.getCode() == down) {
                snake.setDirection(Direction.DOWN);
                System.out.println("DOWN");

            }
            if (key.getCode() == right) {
                snake.setDirection(Direction.RIGHT);
                System.out.println("RIGHT");

            }

        });

    }


    private void masterStep() {
        synchronized (gameLock) {
            for (Snake snake : snakes) {
                snake.move(HEIGHT_COUNT_CELLS, WIDTH_COUNT_CELLS);
                System.out.println(snake.getCells().get(0).getX());
                System.out.println(snake.getCells().get(0).getY());
            }
            checkCollision();
            createFood();

        }


    }


    private void createFood() {
        int foodCount = (int) (FOOD_STATIC + FOOD_PER_PLAYER * snakes.size());
        int curFood = food.size();
        System.out.println("curFood: " + curFood);
        System.out.println("FoodCount: " + foodCount);
        foodCount -= curFood;
        int curCount = 0;
        boolean isEmptyCell;
        Random rand = new Random();
        while (curCount < foodCount) {
            int x = rand.nextInt(WIDTH_COUNT_CELLS - 1);
            int y = rand.nextInt(HEIGHT_COUNT_CELLS - 1);
            isEmptyCell = true;
            for (Food foodIter : food) {
                var foodCell = foodIter.getCell();
                if (foodCell.equalsCoords(x, y)) {
                    isEmptyCell = false;
                }

            }
            for (Snake curSnake : snakes) {
                if (curSnake.isMyPartCoords(x, y)) {
                    isEmptyCell = false;
                    break;
                }
            }
            if (isEmptyCell) {
                this.food.add(new Food(x, y, Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble())));
                ++curCount;
            }
        }
    }

    private void tryCreateDeadFood(Snake snake) {
        var snakeCells = snake.getCells();
        Random rand = new Random();

        for (Cell cell : snakeCells) {
            float diceProc = rand.nextFloat();
            if (diceProc <= DEAD_FOOD_PROB) {
                this.food.add(new Food(cell.getX(), cell.getY(), Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble())));

            }


        }
    }

    private void checkCollision() {
        var deffSnakes = new LinkedList<Snake>();
        int snakesSize = snakes.size();
        for (int i = 0; i < snakesSize; ++i) {
            var curSnake = snakes.get(i);
            checkEatFood(curSnake);
            if (curSnake.isEatSelf()) {
                deffSnakes.add(curSnake);
            }
            for (int j = i + 1; j < snakesSize; ++j) {
                var otherSnake = snakes.get(j);
                collideSnake(curSnake, otherSnake, deffSnakes);
            }
        }
        for (var deff : deffSnakes) {
            if (snakes.contains(deff)) {
                snakes.remove(deff);
                tryCreateDeadFood(deff);
            }
        }
        deffSnakes.clear();
    }

    private void collideSnake(Snake cur, Snake other, LinkedList<Snake> deffered) {
        var curCells = cur.getCells();
        var curCellsSize = curCells.size();
        var otherCells = other.getCells();
        var otherCellsSize = otherCells.size();
        for (int i = 0; i < curCellsSize; ++i) {
            for (int j = 0; j < otherCellsSize; ++j) {
                if (curCells.get(i).equals(otherCells.get(j))) {
                    if (i == 0 && j != 0) {
                        deffered.add(cur);
                        other.setScore(other.getScore() + 1);

                    } else if (i != 0 && j == 0) {
                        deffered.add(other);
                        cur.setScore(cur.getScore() + 1);

                    } else {
                        deffered.add(cur);
                        deffered.add(other);
                    }
                }
            }
        }


    }


    private void checkEatFood(Snake snake) {
        var deffFood = new LinkedList<Food>();
        for (Food foodIter : food) {
            if (foodIter.getCell().equals(snake.getCells().get(0))) { ///осторожно
                deffFood.add(foodIter);
                snake.addCell(-1, -1);
                snake.setScore(snake.getScore() + 1);
            }
        }
        for (Food remFood : deffFood)
            food.removeIf(foodItem -> (foodItem == remFood));
        deffFood.clear();
    }


}
