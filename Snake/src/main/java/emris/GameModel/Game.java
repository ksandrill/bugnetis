package emris.GameModel;


import Network.protocol.SnakesProto;
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
import java.util.function.Consumer;

public class Game implements Runnable {
    ArrayList<Snake> snakes = new ArrayList<>();
    ArrayList<Food> food = new ArrayList<>();
    final SnakesProto.GameConfig config;
    final GameDrawer drawer;
    final Object gameLock;
    private SnakesProto.GameState gameState;
    private SnakesProto.GamePlayers gamePlayers = SnakesProto.GamePlayers.newBuilder().build();


    public Game(final Consumer<EventHandler<KeyEvent>> keyBindConsumer, MasterConfig config, GameDrawer gameDrawer, Object gameLock) {
        this.gameLock = gameLock;
        this.config =createGameConfig(config);
        this.gameState = gameStateInit(this.config);
        var playerSnake = createSnake("dafaq", Color.CRIMSON, config.getWidth() / 2, config.getHeight() / 2);
        snakes.add(playerSnake);
        addKeyHandler(keyBindConsumer, KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, playerSnake);
        createFood();
        drawer = gameDrawer;
        drawer.addToTable(playerSnake);


        SnakesProto.GamePlayer self = SnakesProto.GamePlayer.newBuilder()
                .setId(1)
                .setName("dafaq")
                .setPort(8081)
                .setRole(SnakesProto.NodeRole.MASTER)
                .setIpAddress("127.0.0.1")
                .setScore(0)
                .build();
        gamePlayers = gameState.getPlayers().toBuilder().addPlayers(self).build();
        gameState = gameState.toBuilder().setPlayers(gamePlayers).build();
        System.out.println(gamePlayers.getPlayersList().get(0));




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
                snake.move(config.getHeight(), config.getWidth());
//                System.out.println(snake.getCells().get(0).getX());
//                System.out.println(snake.getCells().get(0).getY());
            }
            checkCollision();
            createFood();

        }


    }


    private void createFood() {
        int foodCount = (int) (config.getFoodStatic() + config.getFoodPerPlayer() * snakes.size());
        int curFood = food.size();
//        System.out.println("curFood: " + curFood);
//        System.out.println("FoodCount: " + foodCount);
        foodCount -= curFood;
        int curCount = 0;
        boolean isEmptyCell;
        Random rand = new Random();
        while (curCount < foodCount) {
            int x = rand.nextInt(config.getWidth() - 1);
            int y = rand.nextInt(config.getHeight() - 1);
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
            if (diceProc <= config.getDeadFoodProb()) {
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


    private SnakesProto.GameState gameStateInit(SnakesProto.GameConfig gameConfig) {
        SnakesProto.GameState.Builder builder = SnakesProto.GameState.newBuilder()
                .setConfig(gameConfig)
                .setPlayers(gamePlayers)
                .setStateOrder(0);
        return builder.build();
    }

    public  SnakesProto.GameConfig createGameConfig(MasterConfig config) {
        return SnakesProto.GameConfig.newBuilder()
                .setWidth(config.getWidth())
                .setHeight(config.getHeight())
                .setFoodStatic(config.getFoodStatic())
                .setFoodPerPlayer(config.getFoodPerPlayer())
                .setStateDelayMs(config.getStateDelayMs())
                .setDeadFoodProb(config.getDeadFoodProb())
                .setPingDelayMs(config.getPingDelayMs())
                .setNodeTimeoutMs(config.getNodeTimeoutMs())
                .build();
    }

    public SnakesProto.GameConfig getConfig() {
        return config;
    }

    public SnakesProto.GamePlayers getGamePlayers() {
        return gamePlayers;
    }
}
