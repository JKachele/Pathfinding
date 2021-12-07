package com.jkachele.pathfinding;

import com.jkachele.pathfinding.enums.CellID;
import com.jkachele.pathfinding.enums.ControlID;
import com.jkachele.pathfinding.enums.PathfindingID;
import com.jkachele.pathfinding.util.Cell;
import com.jkachele.pathfinding.util.Control;
import com.jkachele.pathfinding.util.Vector2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Environment extends JPanel implements ItemListener, ActionListener {
    /* ***************Instance Variables*************** */
    //variables for the cell grid
    private Vector2 gridSize = new Vector2();
    private Cell[][] cells;
    private int[] cellPosX;
    private int[] cellPosY;
    private Vector2 cellSize = new Vector2();
    private final int[] cellBounds = new int[4];
    private Cell startCell;
    private Cell targetCell;

    //variables for Controls
    private Control[] controls;
    private final Vector2 controlSize = new Vector2();
    private int controlPosY;
    private PathfindingID selectedAlg;

    //Input Controls
    private JComboBox<PathfindingID> comboBox;
    private Dimension comboBoxSize;
    private TextField textFieldX;
    private TextField textFieldY;
    private Dimension textFieldSize;
    private JButton gridSizeButton;
    private Dimension gridButtonSize;

    //variables for general use in the class
    private Vector2 surfaceSize = new Vector2();
    private boolean updateDisplay = true;
    private Boolean lastMouseButtonLeft = true; //true = left click, false = right click
    private final Vector2 lastCellIn = new Vector2();
    private int lastControlIn = -1;
    private boolean setStart = false;
    private boolean setTarget = false;
    private JFrame frame;
    private String title;
    private int frameCount = 0;

    //constants
    private static final int BORDER_WIDTH = 50;
    private static final int BORDER_HEIGHT = 50;
    private static final String DEFAULT_TITLE = "Welcome to Pathfinding";
    private static final PathfindingID[] pathfindingIDs = {PathfindingID.A_STAR_CARDINAL, PathfindingID.A_STAR, PathfindingID.DEPTH_FIRST_SEARCH};

    /* ***************Constructors*************** */
    public Environment(int gridX, int gridY, int surfaceX, int surfaceY) {
        this.setLayout(null);
        gridSize.x = gridX;
        gridSize.y = gridY;
        surfaceSize.x = surfaceX;
        surfaceSize.y = surfaceY;
        super.setPreferredSize(new Dimension(surfaceX, surfaceY));
        controlSize.y = (int)((surfaceSize.x - (BORDER_HEIGHT * 2)) * 0.05);
        title = DEFAULT_TITLE;

        addListeners();

        initializeAll();
        updateAll();
    }

    /* ***************Getters and Setters*************** */
    //region
    public Vector2 getGridSize() {
        return gridSize;
    }

    public void setGridSize(Vector2 gridSize) {
        this.gridSize = gridSize;
    }

    public void setGridSize(int gridX, int gridY) {
        this.gridSize.x = gridX;
        this.gridSize.y = gridY;
    }

    public int getGridX(){
        return gridSize.x;
    }

    public void setGridX(int gridX){
        this.gridSize.x = gridX;
    }

    public int getGridY(){
        return gridSize.y;
    }

    public void setGridY(int gridY){
        this.gridSize.y = gridY;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Vector2 getSurfaceSize() {
        return surfaceSize;
    }

    public void setSurfaceSize(Vector2 surfaceSize) {
        this.surfaceSize = surfaceSize;
    }

    public void setSurfaceSize(int surfaceX, int surfaceY){
        this.surfaceSize.x = surfaceX;
        this.surfaceSize.y = surfaceY;
        super.setPreferredSize(new Dimension(surfaceX, surfaceY));
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public int[] getCellPosX() {
        return cellPosX;
    }

    public void setCellPosX(int[] cellPosX) {
        this.cellPosX = cellPosX;
    }

    public int[] getCellPosY() {
        return cellPosY;
    }

    public void setCellPosY(int[] cellPosY) {
        this.cellPosY = cellPosY;
    }

    public Vector2 getCellSize() {
        return cellSize;
    }

    public void setCellSize(Vector2 cellSize) {
        this.cellSize = cellSize;
    }

    public Cell getStartCell() {
        return startCell;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setStartCell(Cell start) {
        this.startCell = start;
        start.setId(CellID.START);
    }

    public void setTargetCell(Cell target) {
        this.targetCell = target;
        target.setId(CellID.TARGET);
    }

    public boolean isUpdateDisplay() {
        return updateDisplay;
    }

    public int getBorderWidth() {
        return BORDER_WIDTH;
    }

    public int getBorderTop() {
        return BORDER_HEIGHT;
    }

    public void setFrame(JFrame frame){
        this.frame = frame;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getDefaultTitle() {
        return DEFAULT_TITLE;
    }

    public PathfindingID getSelectedAlg() {
        return selectedAlg;
    }
    //endregion
    /* ***************Methods*************** */

    // Prints all the components of the display to the JPanel so that they will show up
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        String font = "TimesRoman";

        //Cells
        for(int x=0; x<gridSize.x; x++){
            for(int y=0; y<gridSize.y; y++) {
                Cell cell = cells[x][y];
                g.setColor(cell.getColor());
                g.fillRect(cell.getPosX(), cell.getPosY(), Cell.getSizeX(), Cell.getSizeY());
            }
        }

        //Vertical grid Lines
        for(int x=0; x<=gridSize.x; x++){
            g.setColor(Color.BLACK);
            try {
                g.drawLine(cellPosX[x], cellPosY[0], cellPosX[x], cellPosY[cellPosY.length-1] + cellSize.y);
            } catch (Exception e){
                g.drawLine(cellPosX[x-1] + cellSize.x, cellPosY[0], cellPosX[x-1] + cellSize.x,
                        cellPosY[cellPosY.length-1] + cellSize.y);
            }
        }

        //Horizontal grid Lines
        for(int y=0; y<=gridSize.y; y++) {
            g.setColor(Color.BLACK);
            try {
                g.drawLine(cellPosX[0], cellPosY[y], cellPosX[cellPosX.length-1] + cellSize.x, cellPosY[y]);
            } catch (Exception e){
                g.drawLine(cellPosX[0], cellPosY[y-1] + cellSize.y, cellPosX[cellPosX.length-1] + cellSize.x,
                        cellPosY[y-1] + cellSize.y);
            }
        }

        //Controls
        for(Control i: controls){
            //i.setTextVars();
            g.setColor(i.getColor());
            g.fillRect(i.getPosX(), i.getPosY(), Control.getSizeX(), Control.getSizeY());
            //Text
            g.setFont(new Font(font, Font.PLAIN, i.getTextSize()));
            g.setColor(i.getTextColor());
            g.drawString(i.getText(),i.getTextPos().x,i.getTextPos().y);
        }

        //Title
        g.setFont(new Font(font, Font.PLAIN, 20));
        g.setColor(Color.BLACK);
        g.drawString(title,surfaceSize.x / 2 - 110,20);

        //Combo Box Label
        g.setFont(new Font(font, Font.PLAIN, 15));
        g.drawString("Select Algorithm", surfaceSize.x - (comboBoxSize.width + 130), comboBoxSize.height + 5);

        //Grid Size Input Label
        g.setFont(new Font(font, Font.PLAIN, 15));
        g.drawString("Enter Grid Size", 10, 45);

        updateDisplay = false;
    }

    // adds mouseListeners that detect if a mouse button is pressed or if the mouse moved
    // It runs the specified method whenever a mouse event occurs
    private void addListeners(){
        super.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1){
                    Vector2 cell = calcMouseCell(e.getX(), e.getY());
                    lastMouseButtonLeft = true;
                    if(cell.greaterThan(-1) && cells[cell.x][cell.y].getId() == CellID.BLANK) {
                        if(setStart){
                            setStartTarget(cell, CellID.START);
                        } else if(setTarget){
                            setStartTarget(cell, CellID.TARGET);
                        } else{
                            cells[cell.x][cell.y].setId(CellID.OBSTACLE);
                        }
                    } else if(!cell.greaterThan(-1)){
                        int controlIndex = calMouseControl(e.getX(), e.getY());
                        if(controlIndex >= 0){
                            controlPressed(controlIndex);
                        }
                    }
                } else if(e.getButton() == MouseEvent.BUTTON3){
                    Vector2 cell = calcMouseCell(e.getX(), e.getY());
                    lastMouseButtonLeft = false;
                    if(cell.greaterThan(-1)){
                        if(cells[cell.x][cell.y].getId() == CellID.START){
                            startCell = null;
                        } else if(cells[cell.x][cell.y].getId() == CellID.TARGET){
                            targetCell = null;
                        }
                        cells[cell.x][cell.y].setId(CellID.BLANK);
                    }
                }
            }
        });

        super.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Vector2 cell = calcMouseCell(e.getX(), e.getY());
                // highlights cell mouse is over
                if(cell.greaterThan(-1) && (cell.x != lastCellIn.x || cell.y != lastCellIn.y)) {
                    if(lastCellIn.greaterThan(-1)){
                        cells[lastCellIn.x][lastCellIn.y].unHighlight();
                    }
                    if(lastControlIn >= 0){
                        controls[lastControlIn].unHighlight();
                        lastControlIn = -1;
                    }
                    cells[cell.x][cell.y].highlight();
                    lastCellIn.updateVector(cell.x, cell.y);   //Updates the current cell the mouse is in
                    updateDisplay = true;
                } else if(!cell.greaterThan(-1)){
                    //unhighlight the last cell the mouse is in and sets the last cell to (-1, -1)
                    if(lastCellIn.greaterThan(-1)){
                        cells[lastCellIn.x][lastCellIn.y].unHighlight();
                        lastCellIn.updateVector(-1, -1);
                        updateDisplay = true;
                    }
                    int controlIndex = calMouseControl(e.getX(), e.getY());
                    if(controlIndex >= 0 && controlIndex != lastControlIn){
                        if(lastControlIn >= 0){
                            controls[lastControlIn].unHighlight();
                            updateDisplay = true;
                        }
                        controls[controlIndex].highlight();
                        lastControlIn = controlIndex;
                        updateDisplay = true;
                    } else if(controlIndex == -1 && lastControlIn >= 0){
                        controls[lastControlIn].unHighlight();
                        lastControlIn = -1;
                        updateDisplay = true;
                    }
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                Vector2 cell = calcMouseCell(e.getX(), e.getY());
                if(lastMouseButtonLeft){
                    if(cell.greaterThan(-1) && cells[cell.x][cell.y].getId() == CellID.BLANK) {
                        cells[cell.x][cell.y].setId(CellID.OBSTACLE);
                        updateDisplay = true;
                    }

                } else{
                    if(cell.greaterThan(-1)){
                        cells[cell.x][cell.y].setId(CellID.BLANK);
                        updateDisplay = true;
                    }
                }
            }
        });
    }

    //The item Listener that detects the state of the ComboBox.
    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource() == comboBox) {
            this.selectedAlg = (PathfindingID)(comboBox.getSelectedItem());
        }
    }

    @Override
    public void actionPerformed(ActionEvent a) {

        String s = a.getActionCommand();
        if(s.equals("Enter")) {
            try {
                int x = Integer.parseInt(textFieldX.getText());
                int y = Integer.parseInt(textFieldY.getText());

                if(x <=0 || y <= 0)
                    Main.setTempTitle("Inputs must be > 0", 200);
                else
                    setCellGridSize(x, y);

            } catch(Exception e) {
                Main.setTempTitle("Invalid Input", 200);
            }

            // set the text of field to blank
            textFieldX.setText("");
            textFieldY.setText("");
        }
    }

    //creates a 2d array of cells
    private void initializeCells(){
        cells = new Cell[gridSize.x][gridSize.y];
        cellPosX = new int[gridSize.x];
        cellPosY = new int[gridSize.y];
        updateCellPositions();
        Cell.setSizeX(cellSize.x);
        Cell.setSizeY(cellSize.y);
        for(int x=0; x<gridSize.x; x++){
            for(int y=0; y<gridSize.y; y++) {
                cells[x][y] = new Cell(cellPosX[x], cellPosY[y], CellID.BLANK, x, y);
            }
        }
    }

    // updates the cells with their positions on screen and their sizes.
    private void updateCells() {
        updateCellPositions();
        Cell.setSizeX(cellSize.x);
        Cell.setSizeY(cellSize.y);
        for(int x=0; x<gridSize.x; x++){
            for(int y=0; y<gridSize.y; y++) {
                Cell cell = cells[x][y];
                cell.setPosX(cellPosX[x]);
                cell.setPosY(cellPosY[y]);
            }
        }
    }

    //updates the arrays containing the positions of all the cells
    //updates the bounding box of the full grid of cells
    private void updateCellPositions() {
        for(int i = 0; i < cellPosX.length; i++) {
            int cellAreaX = surfaceSize.x - (BORDER_WIDTH * 2);
            cellSize.x = cellAreaX / gridSize.x;
            cellPosX[i] = BORDER_WIDTH + i * (cellSize.x);
        }
        for(int i = 0; i < cellPosY.length; i++) {
            int cellAreaY = surfaceSize.y - ((BORDER_HEIGHT * 2) + controlSize.y);
            cellSize.y = cellAreaY / gridSize.y;
            cellPosY[i] = BORDER_HEIGHT + i * (cellSize.y);
        }
        cellBounds[0] = cellPosX[0];
        cellBounds[1] = cellPosY[0];
        cellBounds[2] = cellSize.x * gridSize.x;
        cellBounds[3] = cellSize.y * gridSize.y;
    }

    //creates an array of the controls
    private void initializeControls(){
        controls = new Control[]{new Control(ControlID.CLEAR), new Control(ControlID.RESET),
                new Control(ControlID.START_ALGORITHM), new Control(ControlID.START_CELL),
                new Control(ControlID.TARGET_CELL), new Control(ControlID.GENERATE_MAZE),
                new Control(ControlID.QUIT)};
        updateControls();
    }

    //updates the controls with the proper size and spacing for the window size
    private void updateControls(){
        controlPosY = cellBounds[1] + cellBounds[3] + 10;

        Control.setSizeX(controlSize.x);
        Control.setSizeY(controlSize.y);

        for(Control i: controls){
            i.setPosY(controlPosY);
        }
        for(int i=0; i < controls.length; i++) {
            controls[i].setPosX(BORDER_WIDTH + (i * controlSize.x));
        }
    }

    //initializes the ComboBox with the array of pathfinding algorithm ids
    private void initializeComboBox() {
        selectedAlg = pathfindingIDs[0];

        comboBox = new JComboBox<>(pathfindingIDs);
        comboBox.addItemListener(this);

        comboBoxSize = comboBox.getPreferredSize();
        comboBox.setBounds(surfaceSize.x - (comboBoxSize.width + 20), 10, comboBoxSize.width, comboBoxSize.height);

        this.add(comboBox);
    }

    //updates the position of the ComboBox
    private void updateComboBox() {
        comboBoxSize = comboBox.getPreferredSize();
        comboBox.setBounds(surfaceSize.x - (comboBoxSize.width + 20), 10, comboBoxSize.width, comboBoxSize.height);
    }

    private void initializeTextBox() {
        //Grid Size
        textFieldX = new TextField(2);
        textFieldY = new TextField(2);
        gridSizeButton = new JButton("Enter");
        gridSizeButton.addActionListener(this);

        textFieldSize = textFieldX.getPreferredSize();
        gridButtonSize = gridSizeButton.getPreferredSize();

        textFieldX.setBounds(10, 10, textFieldSize.width, textFieldSize.height);
        textFieldY.setBounds(textFieldSize.width + 15, 10, textFieldSize.width, textFieldSize.height);
        gridSizeButton.setBounds((textFieldSize.width * 2) + 20, 10, gridButtonSize.width, gridButtonSize.height);

        this.add(textFieldX);
        this.add(textFieldY);
        this.add(gridSizeButton);
    }

    private void updateTextBox() {
        textFieldSize = textFieldX.getPreferredSize();
        gridButtonSize = gridSizeButton.getPreferredSize();

        textFieldX.setBounds(10, 10, textFieldSize.width, textFieldSize.height);
        textFieldY.setBounds(textFieldSize.width + 15, 10, textFieldSize.width, textFieldSize.height);

        gridSizeButton.setBounds((textFieldSize.width * 2) + 20, 10, gridButtonSize.width, gridButtonSize.height);
    }

    //Overall method to be called when the sizes and positions of the objects needs to be updated
    public void updateAll() {
        controlSize.y = (int)((surfaceSize.y - (BORDER_HEIGHT * 2)) * 0.05);
        updateCells();
        controlSize.x = cellBounds[2] / controls.length;
        updateControls();
        updateComboBox();
        updateTextBox();
    }

    private void initializeAll() {
        initializeComboBox();
        initializeCells();
        initializeControls();
        initializeTextBox();
    }

    //calculates the cell index for a given pixel coordinate
    //if coordinate isn't in a cell, returns (-1, -1)
    private Vector2 calcMouseCell(int x, int y){
        Vector2 cell = new Vector2();
        if(x >= BORDER_WIDTH && x < cellBounds[0] + cellBounds[2] && y >= BORDER_HEIGHT && y < cellBounds[1] + cellBounds[3]){
            cell.x = (x - BORDER_WIDTH) / cellSize.x;
            cell.y = (y - BORDER_HEIGHT) / cellSize.y;
        }
        return cell;
    }

    //calculates the control index for a given pixel coordinate
    //returns -1 when coordinate isn't in a control
    private int calMouseControl(int x, int y){
        int controlIndex = -1;
        if(y >= controlPosY && y < controlPosY + controlSize.y && x >= BORDER_WIDTH){
            controlIndex = (x - BORDER_WIDTH) / controlSize.x;
        }
        if(controlIndex >= controls.length || controlIndex < 0) {
            return -1;
        }
        return controlIndex;
    }

    //This Method Determines what happens when a specified Control is activated
    private void controlPressed(int index){
        switch(controls[index].getId()){
            case CLEAR -> resetCells(true);
            case RESET -> resetCells(false);
            case START_ALGORITHM -> startAlgorithm();
            case START_CELL -> {
                setStart = true;
                Main.setTempTitle("Set Start Cell", 100);
            }
            case TARGET_CELL -> {
                setTarget = true;
                Main.setTempTitle("Set Target Cell", 100);
            }
            case GENERATE_MAZE -> Main.setRunMazeGen(true);
            case QUIT -> quit();
        }
    }

    //resets the cell grid. If the parameter is true, all cells will be reverted to blank
    // If it is false, Start, Target, And obstacles will remain.
    public void resetCells(boolean all) {
        Main.setRunPathfinding(false);
        Main.setStartedPathfinding(false);
        Main.resetAlgs();
        this.title = DEFAULT_TITLE;
        for(int x=0; x<gridSize.x; x++){
            for(int y=0; y<gridSize.y; y++) {
                if(all) {
                    cells[x][y].setId(CellID.BLANK);
                    startCell = null;
                    targetCell = null;
                } else {
                    CellID id = cells[x][y].getId();
                    if(id == CellID.OPEN || id == CellID.CLOSED || id == CellID.PATH){
                        cells[x][y].setId(CellID.BLANK);
                    }
                }
            }
        }
        repaint();
    }

    //Starts the pathfinding by passing the selected algorithm and setting run to true
    private void startAlgorithm() {
        Main.setPathfindingID(this.selectedAlg);
        Main.setRunPathfinding(true);
    }

    //sets the start and target cells if the control was pressed
    private void setStartTarget(Vector2 cell, CellID id){
        if(id == CellID.START){
            if (startCell != null) {
                startCell.setId(CellID.BLANK);
            }
            cells[cell.x][cell.y].setId(id);
            startCell = cells[cell.x][cell.y];
            setStart = false;
        } else if(id == CellID.TARGET){
            if(targetCell != null){
                targetCell.setId(CellID.BLANK);
            }
            cells[cell.x][cell.y].setId(id);
            targetCell = cells[cell.x][cell.y];
            setTarget = false;
        }
    }

    //temporally sets the title to a message for a defined time
    public void setTempTitle(String title, int frames){
        if(frameCount < frames){
            this.title = title;
            frameCount++;
        } else {
            this.title = DEFAULT_TITLE;
            frameCount = 0;
            Main.setTempTitle(false);
            repaint();
        }
    }

    private void setCellGridSize(int x, int y) {
        gridSize.updateVector(x, y);
        initializeCells();
        repaint();
    }

    //closes the window and terminates the code
    private void quit() {
        frame.dispose();
        System.exit(0);
    }
}
