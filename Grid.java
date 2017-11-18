import java.io.Serializable;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * This class represents the grid of the game. 
 * The object of the class also contains the information of the current player of the game, 
 * all the eliminated players, size of the grid, etc.
 * The object also contains an m x n array of Cell objects, which are in the game.
 *
 * @author  Aditya Singh
 * @author  Shivin Dass
 */

public class Grid implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * Height of the grid, in terms of the number of cells.  
     */
	int m = 9;
	
	/**
     * Width of the grid, in terms of the number of cells.  
     */
	int n = 6;
	
	/**
     * m x n array of all the cells in the game.
     */
	Cell[][] cells = new Cell[9][6];
	
	/**
     * Array, with the i'th element true if the i'th player 
     * eliminated from the current game.
     */
	boolean[] eliminatedPlayer = new boolean[8];

	/**
     * Array of Rectangle objects surrounding each cell, used to
     * colour the cells according to the colour of the current player.
     */
	transient Rectangle[][] rectangles = new Rectangle[9][6];
	
	/**
     * Array of Pane objects of each cell in the grid.
     * It's a property of the grid class so that Cell objects can 
     * access the Pane objects of the other cells in the grid.
     */
	transient Pane[][] panes = new Pane[9][6];
	
	/**
     * Array to keep the count of the number of cells owned by the i'th player.
     */
	int[] noOfCellsOfPlayer = new int[8];
	
	/**
     * Player with the next turn.  
     */
	int currentPlayer = 0;
	
	/**
     * Instance of the settings class to access the user settings.  
     */
	Settings settings;
	
	/**
     * True when the first turn has been played by each of the players.
     */
	boolean firstTurn = false;
	
	/**
     * True when the game has ended.
     */
	boolean gameEnd = false;
	
	/**
     * Pane object of the game screen, consisting of the grid and the cells. 
     * Used to add controls and nodes to the current game.
     */
	transient Pane root;
}