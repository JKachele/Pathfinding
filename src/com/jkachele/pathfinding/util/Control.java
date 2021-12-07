package com.jkachele.pathfinding.util;

import com.jkachele.pathfinding.enums.ControlID;

import java.awt.*;
import java.util.Arrays;

public class Control {
    /* ***************Instance Variables*************** */
    private static int sizeX;
    private static int sizeY;
    private int posX;
    private int posY;
    private ControlID id;
    private Color color;
    private Color prevColor;
    private final Color[] colors = {Color.LIGHT_GRAY, Color.DARK_GRAY, Color.GREEN,
            Color.BLUE, Color.MAGENTA, Color.CYAN, Color.RED};

    private String text;
    private Color textColor;
    private int textSize;
    private Vector2 textPos = new Vector2();

    /* ***************Constructors*************** */
    public Control(){
        this(0, 0, ControlID.CLEAR);
    }

    public Control(int posX, int posY) {
        this(posX, posY, ControlID.CLEAR);
    }

    public Control(ControlID id) {
        this(0,0, id);
    }

    public Control(int posX, int posY, ControlID id) {
        this.posX = posX;
        this.posY = posY;
        this.id = id;

        setControl();
        setTextVars();
    }

    /* ***************Getters and Setters*************** */
    //region
    public static int getSizeX() {
        return sizeX;
    }

    public static void setSizeX(int sizeX) {
        Control.sizeX = sizeX;
    }

    public static int getSizeY() {
        return sizeY;
    }

    public static void setSizeY(int sizeY) {
        Control.sizeY = sizeY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
        setTextVars();
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
        setTextVars();
    }

    public ControlID getId() {
        return id;
    }

    public void setId(ControlID id) {
        this.id = id;
        setControl();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Vector2 getTextPos() {
        return textPos;
    }

    public void setTextPos(Vector2 textPos) {
        this.textPos = textPos;
    }
    //endregion
    /* ***************Methods*************** */
    public void setControl(){
        switch (id) {
            case CLEAR -> {
                color = Color.LIGHT_GRAY;
                textColor = Color.BLACK;
                text = "Clear";
            }
            case RESET -> {
                color = Color.DARK_GRAY;
                textColor = Color.WHITE;
                text = "Reset";
            }
            case START_ALGORITHM -> {
                color = Color.GREEN;
                textColor = Color.BLACK;
                text = "Start Pathfinding";
            }
            case START_CELL -> {
                color = Color.BLUE;
                textColor = Color.BLACK;
                text = "Set Start";
            }
            case TARGET_CELL -> {
                color = Color.MAGENTA;
                textColor = Color.BLACK;
                text = "Set Target";
            }
            case GENERATE_MAZE -> {
                color = Color.CYAN;
                textColor = Color.BLACK;
                text = "Generate Maze";
            }
            case QUIT -> {
                color = Color.RED;
                textColor = Color.BLACK;
                text = "Quit";
            }
        }
    }

    public void setTextVars(){
        switch (id) {
            case CLEAR -> {
                textSize = (int)(sizeY * 0.7);
                textPos.x = (int)(posX + (sizeX * 0.2));
                textPos.y = (posY + (sizeY / 2)) + (textSize / 2);
            }
            case RESET -> {
                textSize = (int)(sizeY * 0.65);
                textPos.x = (int)(posX + (sizeX * 0.2));
                textPos.y = (posY + (sizeY / 2)) + (textSize / 2);
            }
            case START_ALGORITHM, GENERATE_MAZE -> {
                textSize = (int)(sizeY * 0.35);
                textPos.x = (int)(posX + (sizeX * 0.05));
                textPos.y = (posY + (sizeY / 2)) + (textSize / 2);
            }
            case START_CELL -> {
                textSize = (int)(sizeY * 0.5);
                textPos.x = (int)(posX + (sizeX * 0.15));
                textPos.y = (posY + (sizeY / 2)) + (textSize / 2);
            }
            case TARGET_CELL -> {
                textSize = (int)(sizeY * 0.5);
                textPos.x = (int)(posX + (sizeX * 0.07));
                textPos.y = (posY + (sizeY / 2)) + (textSize / 2);
            }
            case QUIT -> {
                textSize = (int)(sizeY * 0.8);
                textPos.x = (int)(posX + (sizeX * 0.2));
                textPos.y = (int)((posY + ((float)(sizeY) / 2)) + (textSize * 0.4));
            }
        }
    }

    public void highlight() {

        if(Arrays.asList(colors).contains(color)){
            prevColor = this.color;
            this.color = color.darker();
        }
    }

    public void unHighlight() {

        if(!(Arrays.asList(colors).contains(color))){
            this.color = prevColor;
        }
    }

}