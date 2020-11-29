package emris.GameModel.Entity;

import javafx.scene.paint.Color;

import java.util.LinkedList;

public class Snake {
    private int score;
    private Color color;
    private int playerId;
    private String playerName;
    private Direction direction;
    LinkedList<Cell> cells = new LinkedList<>();

    public Snake(String playerName, Color color) {
        this.playerName = playerName;
        this.color = color;
        this.score = 0;

    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }


    public boolean isMyPartCoords(int x, int y) {
        for (Cell cell : cells) {
            if (cell.equalsCoords(x, y)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMyPartCoordsWithoutHead(int x, int y) {
        for (int i = 1; i < cells.size(); ++i){
            var cell = cells.get(i);
            if (cell.equalsCoords(x, y)) {
                return true;
            }
        }

        return false;
    }

    public void move(int countHeightCells, int countWidthCells) {
        moveTail();
        moveHead(countHeightCells, countWidthCells);
    }

    public boolean isEatSelf() {
        var head = cells.get(0);
        int cellsSize = cells.size();
        for (int i = 1; i < cellsSize; ++i) {
            if (head.equals(cells.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void moveTail() {
        for (int i = cells.size() - 1; i >= 1; i--) {
            cells.get(i).setX(cells.get(i - 1).getX());
            cells.get(i).setY(cells.get(i - 1).getY());
        }


    }

    private void moveHead(int countHeightCells, int countWidthCells) {
        var head = cells.get(0);
        switch (direction) {
            case UP:
                int new_y = head.getY() - 1;
                head.setY(new_y < 0 ? countHeightCells - 1 : new_y);
                break;
            case DOWN:
                new_y = head.getY() + 1;
                head.setY(new_y >= countHeightCells ? 0 : new_y);
                break;
            case LEFT:
                int new_x = head.getX() - 1;
                head.setX(new_x < 0 ? countWidthCells - 1 : new_x);
                break;
            case RIGHT:
                new_x = head.getX() + 1;
                head.setX(new_x >= countWidthCells ? 0 : new_x);
                break;

        }

    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        System.out.println("direction changed");
        this.direction = direction;
    }

    public void addCell(int x, int y) {
        cells.add(new Cell(x, y));
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public LinkedList<Cell> getCells() {
        return cells;
    }

    public void setCells(LinkedList<Cell> cells) {
        this.cells = cells;
    }
}