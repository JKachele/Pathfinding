package com.jkachele.pathfinding.util;

import com.jkachele.pathfinding.*;
import com.jkachele.pathfinding.enums.CellID;

import javax.swing.*;
import java.util.*;

public class MazeGen {
    /* ***************Instance Variables*************** */
    private static Environment environment;
    private static JFrame frame;
    private static Cell[][] cells;

    private static Cell[][] oddCellArray;
    private static ArrayList<Cell[]> frontier = new ArrayList<>();
    private static Random rand;

    /* ***************Constructors*************** */
    public static void initialize(Environment panel, JFrame frame) {
        MazeGen.environment = panel;
        MazeGen.frame = frame;
        MazeGen.cells = panel.getCells();
        rand = new Random();
    }

    public static void initialize(Environment panel, JFrame frame, int seed) {
        MazeGen.environment = panel;
        MazeGen.frame = frame;
        MazeGen.cells = panel.getCells();
        rand = new Random(seed);
    }

    /* ***************Getters and Setters*************** */
    //region
    public static ArrayList<Cell[]> getFrontier() {
        return frontier;
    }

    public static void setFrontier(ArrayList<Cell[]> frontier) {
        MazeGen.frontier = frontier;
    }
    //endregion
    /* ***************Methods*************** */
    public static void startAlg() {
        frame.setResizable(false);
        getVariables();
        environment.setTitle("Started Maze Generation");
        environment.resetCells(true);
        for (Cell[] columns : cells) {
            for (Cell cell : columns) {
                cell.setId(CellID.OBSTACLE);
            }
        }
        setToOddSize();
        frontier.add(new Cell[] {oddCellArray[0][0], oddCellArray[0][0]});
        environment.repaint();
    }

    public static boolean algLoop() {
        if(frontier.isEmpty()) {
            Main.setStartedMazeGen(false);
            frame.setResizable(true);
            environment.setTitle(Environment.getDefaultTitle());
            environment.setStartCell(cells[0][cells[0].length - 1]);
            environment.setTargetCell(cells[cells.length - 1][cells[0].length - 1]);
            return false;
        }
        final Cell[] fCells = frontier.remove(rand.nextInt(frontier.size()));

        int x = fCells[1].getGridPosition().x;
        int y = fCells[1].getGridPosition().y;

        if(fCells[1].getId() == CellID.OBSTACLE) {
            fCells[0].setId(CellID.BLANK);
            fCells[1].setId(CellID.BLANK);
            environment.repaint();

            if(x == cells.length || y == cells[0].length)
                return true;

            if (x >= 2 && oddCellArray[x - 2][y].getId() == CellID.OBSTACLE)
                frontier.add(new Cell[]{oddCellArray[x - 1][y], oddCellArray[x - 2][y]});

            if (y >= 2 && oddCellArray[x][y - 2].getId() == CellID.OBSTACLE)
                frontier.add(new Cell[]{oddCellArray[x][y - 1], oddCellArray[x][y - 2]});

            if (x < oddCellArray.length - 2 && oddCellArray[x + 2][y].getId() == CellID.OBSTACLE)
                frontier.add(new Cell[]{oddCellArray[x + 1][y], oddCellArray[x + 2][y]});

            if (y < oddCellArray[0].length - 2 && oddCellArray[x][y + 2].getId() == CellID.OBSTACLE)
                frontier.add(new Cell[]{oddCellArray[x][y + 1], oddCellArray[x][y + 2]});
        }
        return true;
    }

    private static void getVariables() {
        cells = environment.getCells();
    }

    //the algorithm needs an odd sized array to go to all the edges. this method sets the array to an odd size with blank cells
    private static void setToOddSize() {
        if(cells.length % 2 == 1 && cells[0].length % 2 == 1) {
            oddCellArray = cells;
        } else if(cells.length % 2 == 1) {
            oddCellArray = new Cell[cells.length][cells[0].length + 1];
            for(int x=0; x<cells.length; x++) {
                System.arraycopy(cells[x], 0, oddCellArray[x], 0, cells[x].length);
                oddCellArray[x][cells[x].length] = new Cell(CellID.OBSTACLE, x, cells[x].length);
            }
        } else if(cells[0].length % 2 == 1) {
            oddCellArray = new Cell[cells.length + 1][cells[0].length];
            System.arraycopy(cells, 0, oddCellArray, 0, cells.length);
            for(int y=0; y<oddCellArray[0].length; y++) {
                oddCellArray[cells.length][y] = new Cell(CellID.OBSTACLE, cells.length, y);
            }
        } else {
            oddCellArray = new Cell[cells.length + 1][cells[0].length + 1];
            for(int x=0; x<cells.length; x++) {
                System.arraycopy(cells[x], 0, oddCellArray[x], 0, cells[x].length);
                oddCellArray[x][cells[x].length] = new Cell(CellID.OBSTACLE, x, cells[x].length);
            }
            for(int y=0; y<oddCellArray[0].length; y++) {
                oddCellArray[cells.length][y] = new Cell(CellID.OBSTACLE, cells.length, y);
            }
        }

    }
}