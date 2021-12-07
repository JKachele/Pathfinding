package com.jkachele.pathfinding.util;

public class Vector2 {
    public int x;
    public int y;

    public Vector2(){
        this(-1, -1);
    }

    public Vector2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void updateVector(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean greaterThan(int num) {
        return x > num && y > num;
    }

    public boolean greaterThan(int x, int y) {
        return this.x > x && this.y > y;
    }

    public String toString(){
        return String.format("(%d, %d)", x, y);
    }
}
