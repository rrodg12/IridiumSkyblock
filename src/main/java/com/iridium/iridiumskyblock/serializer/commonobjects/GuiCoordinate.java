package com.iridium.iridiumskyblock.serializer.commonobjects;

public class GuiCoordinate {


    private int x;
    private int y;

    public GuiCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
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
}