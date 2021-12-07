package com.jkachele.pathfinding.util;

import com.jkachele.pathfinding.enums.CellID;

import java.awt.*;
import java.util.*;

public class Cell {
    /* ***************Instance Variables*************** */
    private static int sizeX;
    private static int sizeY;
    private int posX;
    private int posY;
    private Vector2 gridPosition;
    private CellID id;
    private Color color;
    private Color prevColor;

    private Cell parentCell;
    private int gCost = 0;
    private int hCost = 0;
    private int fCost = 0;

    /* ***************Constructors*************** */
    public Cell(){
        this(0, 0, CellID.BLANK, -1, -1);
    }

    public Cell(int posX, int posY) {
        this(posX, posY, CellID.BLANK, -1, -1);
    }

    public Cell(CellID id) {
        this(0, 0, id, -1, -1);
    }

    public Cell(CellID id, int gridX, int gridY) {
        this(0, 0, id, gridX, gridY);
    }

    public Cell(int posX, int posY, CellID id) {
        this(posX, posY, id, -1, -1);
    }


    public Cell(int posX, int posY, CellID id, int gridX, int gridY) {
        this.posX = posX;
        this.posY = posY;
        this.id = id;
        this.gridPosition = new Vector2(gridX, gridY);

        setColor();
    }
    /* ***************Getters and Setters*************** */
    //region
    public static int getSizeX() {
        return sizeX;
    }

    public static void setSizeX(int sizeX) {
        Cell.sizeX = sizeX;
    }

    public static int getSizeY() {
        return sizeY;
    }

    public static void setSizeY(int sizeY) {
        Cell.sizeY = sizeY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Vector2 getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Vector2 gridPosition) {
        this.gridPosition = gridPosition;
    }

    public void setGridPosition(int x, int y) {
        this.gridPosition.x = x;
        this.gridPosition.y = y;
    }

    public CellID getId() {
        return id;
    }

    public void setId(CellID id) {
        this.id = id;
        setColor();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Cell getParentCell() {
        return parentCell;
    }

    public void setParentCell(Cell parentCell) {
        this.parentCell = parentCell;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
        this.fCost = this.hCost + this.gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
        this.fCost = this.hCost + this.gCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }
    //endregion
    /* ***************Methods*************** */
    private void setColor() {
        switch (id) {
            case BLANK -> this.color = Color.WHITE;
            case OBSTACLE -> this.color = Color.BLACK;
            case START -> this.color = Color.BLUE;
            case TARGET -> this.color = Color.MAGENTA;
            case OPEN -> this.color = Color.GREEN;
            case CLOSED -> this.color = Color.RED;
            case PATH -> this.color = Color.ORANGE;
        }
    }

    public void highlight() {
        Color[] colors = {Color.WHITE,Color.BLUE, Color.MAGENTA, Color.GREEN, Color.RED, Color.ORANGE};

        if(Arrays.asList(colors).contains(color)){
            prevColor = this.color;
            this.color = color.darker();
        }else if(color.equals(Color.BLACK)){
            this.color = Color.DARK_GRAY;
        }
    }

    public void unHighlight() {
        Color[] colors = {Color.WHITE, Color.BLACK, Color.BLUE, Color.MAGENTA, Color.GREEN, Color.RED, Color.ORANGE};

        if(color.equals(Color.DARK_GRAY)){
            this.color = Color.BLACK;
        } else if(!(Arrays.asList(colors).contains(color))){
            this.color = prevColor;
        }
    }
}
