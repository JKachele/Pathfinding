package com.jkachele.pathfinding;

import com.jkachele.pathfinding.algorithms.AStar;
import com.jkachele.pathfinding.algorithms.AStarCardinal;
import com.jkachele.pathfinding.algorithms.DFS;
import com.jkachele.pathfinding.enums.PathfindingID;
import com.jkachele.pathfinding.util.MazeGen;
import com.jkachele.pathfinding.util.Window;

import java.awt.*;

public class Main {

    private static boolean running = true;
    private static boolean runPathfinding = false;
    private static boolean startedPathfinding = false;
    private static PathfindingID pathfindingID;
    private static boolean runMazeGen = false;
    private static boolean startedMazeGen = false;

    private static Environment environment;
    private static com.jkachele.pathfinding.util.Window window;
    private static boolean tempTitle = false;
    private static String title = "";
    private static int titleLength = 0;

    private static final int MAX_FPS = 2000;
    private static final int[] defaultWindowSize  = {800, 600};
    private static final int[] defaultGridSize = {60, 40};

    public static void main(String[] args){
        environment = new Environment(defaultGridSize[0], defaultGridSize[1], defaultWindowSize[0], defaultWindowSize[1]);
        window = new Window(environment, new Dimension(defaultWindowSize[0], defaultWindowSize[1]));
        environment.setFrame(window.getFrame());
        running = true;

        initializeAlgs();

        run();
    }

    /* ***************Getters and Setters*************** */
    //region
    public static void setTempTitle(boolean tempTitle) {
        Main.tempTitle = tempTitle;
    }

    public static void setTitle(String title) {
        Main.title = title;
    }

    public static void setTitleLength(int titleLength) {
        Main.titleLength = titleLength;
    }

    public static void setRunPathfinding(boolean runPathfinding) {
        Main.runPathfinding = runPathfinding;
    }

    public static void setStartedPathfinding(boolean startedPathfinding) {
        Main.startedPathfinding = startedPathfinding;
    }

    public static void setPathfindingID(PathfindingID pathfindingID) {
        Main.pathfindingID = pathfindingID;
    }

    public static boolean isRunMazeGen() {
        return runMazeGen;
    }

    public static void setRunMazeGen(boolean runMazeGen) {
        Main.runMazeGen = runMazeGen;
    }

    public static boolean isStartedMazeGen() {
        return startedMazeGen;
    }

    public static void setStartedMazeGen(boolean startedMazeGen) {
        Main.startedMazeGen = startedMazeGen;
    }
    //endregion
    /* ***************Methods*************** */
    private static void run(){
        environment.updateAll();
        environment.repaint();
        while (running) {
            Dimension size = window.getFrame().getSize();
            if(size.width != environment.getSurfaceSize().x || size.height != environment.getSurfaceSize().y){
                environment.setSurfaceSize(size.width, size.height);
                environment.updateAll();
            }
            if(environment.isUpdateDisplay()) {
                environment.repaint();
            }
            if(tempTitle){
                environment.setTempTitle(title, titleLength);
            }
            if(runPathfinding) {
                if(!startedPathfinding) {
                    switch(pathfindingID) {
                        case A_STAR -> AStar.startAlg();
                        case A_STAR_CARDINAL -> AStarCardinal.startAlg();
                        case DEPTH_FIRST_SEARCH -> DFS.startAlg();
                        default -> runPathfinding = false;
                    }
                    startedPathfinding = true;
                } else {
                    switch(pathfindingID) {
                        case A_STAR -> runPathfinding = AStar.algLoop();
                        case A_STAR_CARDINAL -> runPathfinding = AStarCardinal.algLoop();
                        case DEPTH_FIRST_SEARCH -> runPathfinding = DFS.algLoop();
                        default -> runPathfinding = false;
                    }
                }
            }
            if(runMazeGen) {
                if(!startedMazeGen) {
                    MazeGen.startAlg();
                    startedMazeGen = true;
                } else {
                    runMazeGen = MazeGen.algLoop();
                }
            }
            setMaxFPS(MAX_FPS);
        }
    }

    private static void initializeAlgs() {
        AStar.initialize(environment, window.getFrame());
        AStarCardinal.initialize(environment, window.getFrame());
        DFS.initialize(environment, window.getFrame());
        MazeGen.initialize(environment, window.getFrame());
    }

    public static void resetAlgs() {
        AStar.getOpenCells().clear();
        AStar.getClosedCells().clear();
        AStarCardinal.getOpenCells().clear();
        AStarCardinal.getClosedCells().clear();
    }

    public static void setTempTitle(String title, int titleLength){
        Main.title = title;
        Main.titleLength = titleLength * (MAX_FPS / 60);
        Main.tempTitle = true;
    }

    public static void setMaxFPS(int fps){
        wait(1000 / fps);
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        }catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
