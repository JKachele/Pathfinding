package com.jkachele.pathfinding.algorithms;

import com.jkachele.pathfinding.*;
import com.jkachele.pathfinding.enums.CellID;
import com.jkachele.pathfinding.util.Cell;

import javax.swing.*;
import java.util.*;

public class AStar {
    /* ***************Instance Variables*************** */
    private static Environment environment;
    private static JFrame frame;
    private static Cell[][] cells;
    private static Cell startCell;
    private static Cell targetCell;

    private static ArrayList<Cell> openCells = new ArrayList<>();
    private static ArrayList<Cell> closedCells = new ArrayList<>();

    /* ***************Constructors*************** */
    public static  void initialize(Environment panel, JFrame frame) {
        AStar.environment = panel;
        AStar.frame = frame;
        AStar.cells = panel.getCells();
        AStar.startCell = panel.getStartCell();
        AStar.targetCell = panel.getTargetCell();
    }

    /* ***************Getters and Setters*************** */
    //region
    public static ArrayList<Cell> getOpenCells() {
        return openCells;
    }

    public static void setOpenCells(ArrayList<Cell> openCells) {
        AStar.openCells = openCells;
    }

    public static ArrayList<Cell> getClosedCells() {
        return closedCells;
    }

    public static void setClosedCells(ArrayList<Cell> closedCells) {
        AStar.closedCells = closedCells;
    }
    //endregion
    /* ***************Methods*************** */
    public static void startAlg() {
        frame.setResizable(false);
        getVariables();
        if(startCell == null || targetCell == null){
            Main.setTempTitle("Need Start and Target Cells", 200);
            Main.setRunPathfinding(false);
            return;
        }
        environment.setTitle("Started Pathfinding");
        environment.resetCells(false);
        environment.repaint();
        calcHCosts();
        setFirstCell();
        Main.setRunPathfinding(true);
    }

    public static boolean algLoop() {
        Cell currentCell = findLowestFCost();
        if(currentCell != null) {
            openCells.remove(currentCell);
            closedCells.add(currentCell);
            currentCell.setId(CellID.CLOSED);
            environment.repaint();

            if(currentCell.getHCost() == 0){
                currentCell.setId(CellID.TARGET);
                makePath();
                Main.setStartedPathfinding(false);
                frame.setResizable(true);
                environment.setTitle(Environment.getDefaultTitle());
                return false;
            } else {
                openSurroundingCells(currentCell);
                return true;
            }
        }
        Main.setTempTitle("No Path Found", 200);
        Main.setStartedPathfinding(false);
        frame.setResizable(true);
        return false;
    }

    private static void getVariables() {
        AStar.cells = environment.getCells();
        AStar.startCell = environment.getStartCell();
        AStar.targetCell = environment.getTargetCell();
    }

    private static void setFirstCell() {
        closedCells.add(startCell);
        openSurroundingCells(startCell);
    }

    private static void openSurroundingCells(Cell centerCell) {
        for(int x=-1; x<2; x++) {
            for(int y=-1; y<2; y++) {
                if(isValidGrid(centerCell.getGridPosition().x + x, centerCell.getGridPosition().y + y)){
                    Cell cell = cells[centerCell.getGridPosition().x + x][centerCell.getGridPosition().y + y];
                    if(cell.getId() == CellID.OBSTACLE || closedCells.contains(cell)){
                        continue;
                    }
                    int gCost = (int)((Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) * 10) + centerCell.getGCost());
                    if(!openCells.contains(cell)){
                        cell.setId(CellID.OPEN);
                        cell.setGCost(gCost);
                        openCells.add(cell);
                        cell.setParentCell(centerCell);
                        environment.repaint();
                    } else if(cell.getGCost() > gCost){
                        cell.setGCost(gCost);
                        cell.setParentCell(centerCell);
                    }
                    environment.repaint();
                }
            }
        }
    }

    private static boolean isValidGrid(int x, int y) {
        if(x < 0 || x >= environment.getGridX()){
            return false;
        }
        return y >= 0 && y < environment.getGridY();
    }

    private static void calcHCosts() {
        final int D = 10;     //Distance to adjacent cells in 4 cardinal directions
        final int D2 = 14;    //distance to adjacent cells in 4 diagonal directions

        for(int x=0; x<cells.length; x++){
            for(int y=0; y<cells[x].length; y++) {
                int dx = Math.abs(x - targetCell.getGridPosition().x);
                int dy = Math.abs(y - targetCell.getGridPosition().y);

                int hCost = D * D * (dx + dy) + (D2 - 2 * D) * Math.min(dx, dy);

                cells[x][y].setHCost(hCost);
            }
        }
    }

    private static Cell findLowestFCost() {
        Cell lowestCell;
        try{
            //find the lowest f-cost of all the open cells
            int lowF = Integer.MAX_VALUE;
            for(Cell i: openCells){
                if(i.getFCost() < lowF){
                    lowF = i.getFCost();
                }
            }

            //find all the cells with the lowest f-cost
            ArrayList<Cell> lowFCells = new ArrayList<>();
            for(Cell i: openCells){
                if(i.getFCost() == lowF){
                    lowFCells.add(i);
                }
            }

            //if there are multiple cells with the lowest f-cost, find cell with the lowest h-cost
            //doesn't matter if multiple cells with same f and h cost, picks first one
            if(lowFCells.size() != 1){
                int lowH = Integer.MAX_VALUE;
                int lowHCell = 0;
                for(int i=0; i<lowFCells.size(); i++) {
                    if(lowFCells.get(i).getHCost() < lowH){
                        lowH = lowFCells.get(i).getHCost();
                        lowHCell = i;
                    }
                }
                lowestCell = lowFCells.get(lowHCell);
            } else {
                lowestCell = lowFCells.get(0);
            }

        } catch (Exception e) {
            return null;
        }
        return lowestCell;
    }

    private static void makePath(){
        Cell currentCell = targetCell.getParentCell();
        while(currentCell != startCell) {
            currentCell.setId(CellID.PATH);
            currentCell = currentCell.getParentCell();
        }
    }

}