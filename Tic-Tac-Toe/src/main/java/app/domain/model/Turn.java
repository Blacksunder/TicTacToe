package app.domain.model;

public class Turn {
    private int x;
    private int y;
    private int value;

    public Turn(int y, int x, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void changeValue(int value) {
        this.value += value;
    }
}
