package emris.GameModel;

public class MasterConfig {
     int width;
     int height;
     int foodStatic;
     float foodPerPlayer;
     int stateDelayMs;
     float deadFoodProb;
     int pingDelayMs;
     int nodeTimeoutMs;

    public MasterConfig(int width, int height, int foodStatic, float foodPerPlayer, int stateDelayMs, float deadFoodProb, int pingDelayMs, int nodeTimeoutMs) {
        this.width = width;
        this.height = height;
        this.foodStatic = foodStatic;
        this.foodPerPlayer = foodPerPlayer;
        this.stateDelayMs = stateDelayMs;
        this.deadFoodProb = deadFoodProb;
        this.pingDelayMs = pingDelayMs;
        this.nodeTimeoutMs = nodeTimeoutMs;
    }
    public MasterConfig(){}

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFoodStatic() {
        return foodStatic;
    }

    public void setFoodStatic(int foodStatic) {
        this.foodStatic = foodStatic;
    }

    public float getFoodPerPlayer() {
        return foodPerPlayer;
    }

    public void setFoodPerPlayer(float foodPerPlayer) {
        this.foodPerPlayer = foodPerPlayer;
    }

    public int getStateDelayMs() {
        return stateDelayMs;
    }

    public void setStateDelayMs(int stateDelayMs) {
        this.stateDelayMs = stateDelayMs;
    }

    public float getDeadFoodProb() {
        return deadFoodProb;
    }

    public void setDeadFoodProb(float deadFoodProb) {
        this.deadFoodProb = deadFoodProb;
    }

    public int getPingDelayMs() {
        return pingDelayMs;
    }

    public void setPingDelayMs(int pingDelayMs) {
        this.pingDelayMs = pingDelayMs;
    }

    public int getNodeTimeoutMs() {
        return nodeTimeoutMs;
    }

    public void setNodeTimeoutMs(int nodeTimeoutMs) {
        this.nodeTimeoutMs = nodeTimeoutMs;
    }
}
