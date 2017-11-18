import java.io.*;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * This class represent each cell of the grid of the game. 
 * Each cell of the grid is an object of this class.
 *
 * @author  Aditya Singh
 * @author  Shivin Dass
 */

public class Cell extends StackPane implements Serializable {
	static final long serialVersionUID = 1L;
	
	/**
     * X coordinate of the cell.  
     */
	int x;
	
	/**
     * Y coordinate of the cell.
     */
	int y;
	
	/**
     * Main Pane object of the Cell objects.
     * Used to add controls and nodes to the game. 
     */
	transient Pane pane = new Pane();
	
	/**
     * Number of orbs in the cell at any given time.
     */
	int numberOfOrbs = 0;
	
	/**
     * Maximum orbs the cell can hold.
     */
	int maxOrbs = 0;
	
	/**
     * Instance of the game class.
     * Used for serialization.
     */
	static Game game;
	
	/**
     * Instance of the grid class.
     * Used to access the information stored in the grid object.
     */
	static Grid grid;
	
	/**
     * Current player who holds the cell.
     * Equal to -1 if the cell is empty.
     */
	int currentPlayer = -1;
	
	/**
     * Array consisting of Color objects, used to colour the circles 
     * and the rectangles in this class.
     */
	transient Paint[] colourVals = {Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.YELLOW, Color.AQUA, Color.BLUEVIOLET, Color.HOTPINK};
	
	/**
     * Pane object of the game screen, consisting of the grid and the cells. 
     * Used to add controls and nodes to the current game.
     */
	transient Pane root;
	
	/**
     * True if the click on the cell lead to an explosion.
     */
	boolean transition = false;
	
	/**
     * The length of the chain, during the chain reaction.
     */
	int chain = 0;
	
	/**
     * Object used to rotate the orbs in the cell.
     */
	transient Timeline rot;
	
	/**
     * True when the mouse click event on the cell was simulated by the program, 
     * False when the mouse click event was created by the user.
     */
	boolean fire = false;
	
	/**
     * Constructs a cell object with the coordinates as specified by the parameters.
     * Defines the action handler when the user clicks the cell.
     * 
     * @param root Pane object of the game screen, used to add controls and nodes to the game.
     * @param x X coordinate of the cell.
     * @param y Y coordinate of the cell.
     */
    public Cell(Pane root, int x, int y) {
    	this.root = root;
    	Group branch = new Group();
    	this.x = x;
    	this.y = y;
    	
        Rectangle border = new Rectangle(50, 50);
        border.setFill(null);
        border.setStroke(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
        
        Rectangle trans = new Rectangle(50, 50);
        trans.setFill(null);
        trans.setStroke(Color.TRANSPARENT);
        
        setAlignment(Pos.CENTER);
        getChildren().addAll(border, branch);
        branch.getChildren().addAll(trans, pane);
        
        this.rot = new Timeline();
        rot.setCycleCount(Timeline.INDEFINITE);
        rot.setRate(2);
        rot.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(branch.rotateProperty(), 0)), new KeyFrame(Duration.seconds(5), new KeyValue(branch.rotateProperty(), 360)));
        rot.playFromStart();
        
        grid.rectangles[x][y] = border;
        grid.panes[x][y] = pane;
        
        setOnMouseClicked(event -> {
        	onClick();
        });
    }

    /**
     * This function is called when the user clicks the cell.
     * It handles the cases of when there is an explosion and when an orb is added to the cell.
     * In the case of an explosion, it first renders the animation, and then calls the function
     * which handles the logic of the explosion, asynchronously, by simulating mouse clicks around
     * the cell.  
     */
    public void onClick() {
    	if (grid.gameEnd) {
    		return;
    	}
    	if (grid.currentPlayer == grid.settings.noOfPlayers - 1) {
    		grid.firstTurn = true;
    	}
    	if (this.chain >= 2) {
    		grid.currentPlayer = currentPlayer;
    	}
    	if (currentPlayer == -1 || currentPlayer == grid.currentPlayer) {
    		if(fire==false)
    		{
    			ObjectOutputStream out=null;
    			try {
    				out=new	ObjectOutputStream(new FileOutputStream("./src/undo.ser"));
    				out.writeObject(game);
    				out.close();
    			} catch (FileNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		if (currentPlayer == -1) {
    			grid.noOfCellsOfPlayer[grid.currentPlayer]++;
    		}
        	currentPlayer = grid.currentPlayer;
    		
    		Circle circle1 = new Circle();
        	circle1.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
        	circle1.setRadius(7.5);
        	
        	if (numberOfOrbs == maxOrbs - 1) {
        		fire=true;
        		transition = true;        
        		numberOfOrbs = 0;
        		if (maxOrbs == 2) {
            		pane.getChildren().clear();
            		
            		Circle tcircle1 = new Circle();
        			Circle tcircle2 = new Circle();
        			tcircle1.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle1.setRadius(7.5);
            		tcircle2.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle2.setRadius(7.5);
                	tcircle1.setLayoutX(70 + y * 50);
                    tcircle1.setLayoutY(100 + x * 50);
                    tcircle2.setLayoutX(70 + y * 50);
                    tcircle2.setLayoutY(100 + x * 50);
                    
            		if (x == 0 && y == 0) {                			
            			transition = true;
                        root.getChildren().add(tcircle1);
                        root.getChildren().add(tcircle2);
                        Path path1 = new Path();
                        path1.getElements().add(new MoveTo(25,25));
                        path1.getElements().add(new HLineTo(75));
                        PathTransition pathTransition1 = new PathTransition();
                        pathTransition1.setDuration(Duration.millis(500));
                        pathTransition1.setPath(path1);
                        pathTransition1.setNode(tcircle1);
                        pathTransition1.setCycleCount(1);
                        pathTransition1.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle1);
                        });
                        Path path2 = new Path();
                        path2.getElements().add(new MoveTo(25,25));
                        path2.getElements().add(new VLineTo(75));
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(500));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(tcircle2);
                        pathTransition2.setCycleCount(1);

            			grid.cells[this.x + 1][this.y].chain = this.chain + 1;
            			grid.cells[this.x][this.y + 1].chain = this.chain + 1;
                        
                        pathTransition2.setOnFinished(e -> {

                        	root.getChildren().remove(tcircle2);
                        	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                			
                			grid.cells[this.x+1][this.y].fire=true;
                			Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                			
                			if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
           
                			grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                			grid.cells[this.x][this.y+1].fire=true;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	
                    		currentPlayer = -1;
                    		grid.noOfCellsOfPlayer[grid.currentPlayer]--;
                        	
                        	if (grid.firstTurn) {
                        		int cnt = 0;
                            	int k = -1;
                            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                            		if (grid.noOfCellsOfPlayer[i] > 0) {
                            			cnt++;
                            			k = i;
                            		}
                            		else {
                            			grid.eliminatedPlayer[i] = true;
                            		}
                            	}
                            	if (cnt == 1) {
                        			Text message = new Text();
                        			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                        			File z=new File("./src/resume.ser");
                            		z.delete();
                        			message.setTranslateX(100);
                        			message.setTranslateY(80);
                        			message.setStroke(colourVals[3]);
                        			grid.root.getChildren().add(message);
                        			for (int i = 0; i < grid.m; i++) {
                                		for (int j = 0; j < grid.n; j++) {
                                			grid.panes[i][j].getChildren().clear();
                                		}
                                	}
                            		grid.gameEnd = true;
                            	}
                        	}
                        	
                        	if (chain == 0) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	}
                        	}
                        	
                        	for (int i = 0; i < grid.m; i++) {
                        		for (int j = 0; j < grid.n; j++) {
                        			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                        			grid.rectangles[i][j].setStroke(colourVals[ind]);
                        		}
                        	}
                        	
                        	
                        });
                        pathTransition1.play();
                        pathTransition2.play();
                        
            		} else if (x == 0 && y == grid.n - 1) {
            			transition = true;
            			root.getChildren().add(tcircle1);
                        root.getChildren().add(tcircle2);
                        Path path1 = new Path();
                        path1.getElements().add(new MoveTo(25,25));
                        path1.getElements().add(new HLineTo(-25));
                        PathTransition pathTransition1 = new PathTransition();
                        pathTransition1.setDuration(Duration.millis(500));
                        pathTransition1.setPath(path1);
                        pathTransition1.setNode(tcircle1);
                        pathTransition1.setCycleCount(1);
                        pathTransition1.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle1);
                        });
                        Path path2 = new Path();
                        path2.getElements().add(new MoveTo(25,25));
                        path2.getElements().add(new VLineTo(75));
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(500));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(tcircle2);
                        pathTransition2.setCycleCount(1);
                        
                        grid.cells[this.x + 1][this.y].chain = this.chain + 1;
            			grid.cells[this.x][this.y - 1].chain = this.chain + 1;
                        
                        pathTransition2.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle2);
                        	
                        	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                			grid.cells[this.x+1][this.y].fire=true;
                			Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                			grid.cells[this.x][this.y-1].fire=true;
                			Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                    		
                    		currentPlayer = -1;
                    		grid.noOfCellsOfPlayer[grid.currentPlayer]--;

                        	
                        	if (grid.firstTurn) {
                        		int cnt = 0;
                            	int k = -1;
                            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                            		if (grid.noOfCellsOfPlayer[i] > 0) {
                            			cnt++;
                            			k = i;
                            		}
                            		else {
                            			grid.eliminatedPlayer[i] = true;
                            		}
                            	}
                            	if (cnt == 1) {
                        			Text message = new Text();
                        			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                        			File z=new File("./src/resume.ser");
                            		z.delete();
                        			message.setTranslateX(100);
                        			message.setTranslateY(80);
                        			message.setStroke(colourVals[3]);
                        			grid.root.getChildren().add(message);
                        			for (int i = 0; i < grid.m; i++) {
                                		for (int j = 0; j < grid.n; j++) {
                                			grid.panes[i][j].getChildren().clear();
                                		}
                                	}
                            		grid.gameEnd = true;
                            	}
                        	}
                        	
                        	if (chain == 0) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	}
                        	}
                        	
                        	for (int i = 0; i < grid.m; i++) {
                        		for (int j = 0; j < grid.n; j++) {
                        			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                        			grid.rectangles[i][j].setStroke(colourVals[ind]);
                        		}
                        	}
                        	
                        	
                        });
                        pathTransition1.play();
                        pathTransition2.play();
            			
            			
            		} else if (x == grid.m - 1 && y == 0) {
            			transition = true;
            			root.getChildren().add(tcircle1);
                        root.getChildren().add(tcircle2);
                        Path path1 = new Path();
                        path1.getElements().add(new MoveTo(25,25));
                        path1.getElements().add(new HLineTo(75));
                        PathTransition pathTransition1 = new PathTransition();
                        pathTransition1.setDuration(Duration.millis(500));
                        pathTransition1.setPath(path1);
                        pathTransition1.setNode(tcircle1);
                        pathTransition1.setCycleCount(1);
                        pathTransition1.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle1);
                        });
                        Path path2 = new Path();
                        path2.getElements().add(new MoveTo(25,25));
                        path2.getElements().add(new VLineTo(-25));
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(500));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(tcircle2);
                        pathTransition2.setCycleCount(1);
                        
                        grid.cells[this.x - 1][this.y].chain = this.chain + 1;
            			grid.cells[this.x][this.y + 1].chain = this.chain + 1;
                        
                        pathTransition2.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle2);
                        	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                			grid.cells[this.x-1][this.y].fire=true;
                			Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                			grid.cells[this.x][this.y+1].fire=true;
                			Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                    		
                    		currentPlayer = -1;
                    		grid.noOfCellsOfPlayer[grid.currentPlayer]--;
                        	
                        	
                        	if (grid.firstTurn) {
                        		int cnt = 0;
                            	int k = -1;
                            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                            		if (grid.noOfCellsOfPlayer[i] > 0) {
                            			cnt++;
                            			k = i;
                            		}
                            		else {
                            			grid.eliminatedPlayer[i] = true;
                            		}
                            	}
                            	if (cnt == 1) {
                        			Text message = new Text();
                        			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                        			File z=new File("./src/resume.ser");
                            		z.delete();
                        			message.setTranslateX(100);
                        			message.setTranslateY(80);
                        			message.setStroke(colourVals[3]);
                        			grid.root.getChildren().add(message);
                        			for (int i = 0; i < grid.m; i++) {
                                		for (int j = 0; j < grid.n; j++) {
                                			grid.panes[i][j].getChildren().clear();
                                		}
                                	}
                            		grid.gameEnd = true;
                            	}
                        	}
                        	
                        	if (chain == 0) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	}
                        	}
                        	
                        	for (int i = 0; i < grid.m; i++) {
                        		for (int j = 0; j < grid.n; j++) {
                        			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                        			grid.rectangles[i][j].setStroke(colourVals[ind]);
                        		}
                        	}
                        	
                        	
                        });
                        pathTransition1.play();
                        pathTransition2.play();
            			
            			
            		} else if (x == grid.m - 1 && y == grid.n - 1) {
            			transition = true;
            			root.getChildren().add(tcircle1);
                        root.getChildren().add(tcircle2);
                        Path path1 = new Path();
                        path1.getElements().add(new MoveTo(25,25));
                        path1.getElements().add(new HLineTo(-25));
                        PathTransition pathTransition1 = new PathTransition();
                        pathTransition1.setDuration(Duration.millis(500));
                        pathTransition1.setPath(path1);
                        pathTransition1.setNode(tcircle1);
                        pathTransition1.setCycleCount(1);
                        pathTransition1.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle1);
                        });
                        Path path2 = new Path();
                        path2.getElements().add(new MoveTo(25,25));
                        path2.getElements().add(new VLineTo(-25));
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(500));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(tcircle2);
                        pathTransition2.setCycleCount(1);
                        
                        grid.cells[this.x][this.y - 1].chain = this.chain + 1;
            			grid.cells[this.x - 1][this.y].chain = this.chain + 1;
                        
                        pathTransition2.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle2);
                        	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                			grid.cells[this.x-1][this.y].fire=true;
                			Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                			grid.cells[this.x][this.y-1].fire=true;
                			Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                    		
                    		currentPlayer = -1;
                    		grid.noOfCellsOfPlayer[grid.currentPlayer]--;
                        	
                        	
                        	if (grid.firstTurn) {
                        		int cnt = 0;
                            	int k = -1;
                            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                            		if (grid.noOfCellsOfPlayer[i] > 0) {
                            			cnt++;
                            			k = i;
                            		}
                            		else {
                            			grid.eliminatedPlayer[i] = true;
                            		}
                            	}
                            	if (cnt == 1) {
                        			Text message = new Text();
                        			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                        			File z=new File("./src/resume.ser");
                            		z.delete();
                        			message.setTranslateX(100);
                        			message.setTranslateY(80);
                        			message.setStroke(colourVals[3]);
                        			grid.root.getChildren().add(message);
                        			for (int i = 0; i < grid.m; i++) {
                                		for (int j = 0; j < grid.n; j++) {
                                			grid.panes[i][j].getChildren().clear();
                                		}
                                	}
                            		grid.gameEnd = true;
                            	}
                        	}
                        	
                        	if (chain == 0) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	}
                        	}
                        	
                        	for (int i = 0; i < grid.m; i++) {
                        		for (int j = 0; j < grid.n; j++) {
                        			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                        			grid.rectangles[i][j].setStroke(colourVals[ind]);
                        		}
                        	}
                        	
                        	
                        });
                        pathTransition1.play();
                        pathTransition2.play();
            		}
            	}
        		
        		else if (maxOrbs == 3) {
        			pane.getChildren().clear();
        			
        			Circle tcircle1 = new Circle();
        			Circle tcircle2 = new Circle();
        			Circle tcircle3 = new Circle();
        			tcircle1.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle1.setRadius(7.5);
                	tcircle1.setLayoutX(70 + y * 50);
                    tcircle1.setLayoutY(100 + x * 50);
            		tcircle2.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle2.setRadius(7.5);
                    tcircle2.setLayoutX(70 + y * 50);
                    tcircle2.setLayoutY(100 + x * 50);
                    tcircle3.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle3.setRadius(7.5);
                    tcircle3.setLayoutX(70 + y * 50);
                    tcircle3.setLayoutY(100 + x * 50);
        			
                    
        			if (x == 0) {
        				transition = true;
        				root.getChildren().add(tcircle1);
                        root.getChildren().add(tcircle2);
                        root.getChildren().add(tcircle3);
                        Path path1 = new Path();
                        path1.getElements().add(new MoveTo(25,25));
                        path1.getElements().add(new HLineTo(-25));
                        PathTransition pathTransition1 = new PathTransition();
                        pathTransition1.setDuration(Duration.millis(500));
                        pathTransition1.setPath(path1);
                        pathTransition1.setNode(tcircle1);
                        pathTransition1.setCycleCount(1);
                        pathTransition1.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle1);
                        });
                        Path path2 = new Path();
                        path2.getElements().add(new MoveTo(25,25));
                        path2.getElements().add(new HLineTo(75));
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(500));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(tcircle2);
                        pathTransition2.setCycleCount(1);
                        pathTransition2.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle2);
                        });
                        Path path3 = new Path();
                        path3.getElements().add(new MoveTo(25,25));
                        path3.getElements().add(new VLineTo(75));
                        PathTransition pathTransition3 = new PathTransition();
                        pathTransition3.setDuration(Duration.millis(500));
                        pathTransition3.setPath(path3);
                        pathTransition3.setNode(tcircle3);
                        pathTransition3.setCycleCount(1);
                        
                        grid.cells[this.x + 1][this.y].chain = this.chain + 1;
            			grid.cells[this.x][this.y + 1].chain = this.chain + 1;
            			grid.cells[this.x][this.y - 1].chain = this.chain + 1;
                        
                        pathTransition3.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle3);
                        	if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
            				grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
            				grid.cells[this.x][this.y-1].fire=true;
            				Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                			grid.cells[this.x+1][this.y].fire=true;
                			Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                    		grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                    		grid.cells[this.x][this.y+1].fire=true;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                    		
                    		currentPlayer = -1;
                    		grid.noOfCellsOfPlayer[grid.currentPlayer]--;

                        	
                        	
                        	if (grid.firstTurn) {
                        		int cnt = 0;
                            	int k = -1;
                            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                            		if (grid.noOfCellsOfPlayer[i] > 0) {
                            			cnt++;
                            			k = i;
                            		}
                            		else {
                            			grid.eliminatedPlayer[i] = true;
                            		}
                            	}
                            	if (cnt == 1) {
                        			Text message = new Text();
                        			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                        			File z=new File("./src/resume.ser");
                            		z.delete();
                        			message.setTranslateX(100);
                        			message.setTranslateY(80);
                        			message.setStroke(colourVals[3]);
                        			grid.root.getChildren().add(message);
                        			for (int i = 0; i < grid.m; i++) {
                                		for (int j = 0; j < grid.n; j++) {
                                			grid.panes[i][j].getChildren().clear();
                                		}
                                	}
                            		grid.gameEnd = true;
                            	}
                        	}
                        	
                        	if (chain == 0) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	}
                        	}

                        	for (int i = 0; i < grid.m; i++) {
                        		for (int j = 0; j < grid.n; j++) {
                        			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                        			grid.rectangles[i][j].setStroke(colourVals[ind]);
                        		}
                        	}
                        	
                        	
                        });
                        pathTransition1.play();
                        pathTransition2.play();
                        pathTransition3.play();
        			}
        			
        			else if (y == 0) {
        				transition = true;
        				root.getChildren().add(tcircle1);
                        root.getChildren().add(tcircle2);
                        root.getChildren().add(tcircle3);
                        Path path1 = new Path();
                        path1.getElements().add(new MoveTo(25,25));
                        path1.getElements().add(new HLineTo(75));
                        PathTransition pathTransition1 = new PathTransition();
                        pathTransition1.setDuration(Duration.millis(500));
                        pathTransition1.setPath(path1);
                        pathTransition1.setNode(tcircle1);
                        pathTransition1.setCycleCount(1);
                        pathTransition1.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle1);
                        });
                        Path path2 = new Path();
                        path2.getElements().add(new MoveTo(25,25));
                        path2.getElements().add(new VLineTo(-25));
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(500));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(tcircle2);
                        pathTransition2.setCycleCount(1);
                        pathTransition2.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle2);
                        });
                        Path path3 = new Path();
                        path3.getElements().add(new MoveTo(25,25));
                        path3.getElements().add(new VLineTo(75));
                        PathTransition pathTransition3 = new PathTransition();
                        pathTransition3.setDuration(Duration.millis(500));
                        pathTransition3.setPath(path3);
                        pathTransition3.setNode(tcircle3);
                        pathTransition3.setCycleCount(1);
                        
                        grid.cells[this.x - 1][this.y].chain = this.chain + 1;
        				grid.cells[this.x + 1][this.y].chain = this.chain + 1;
        				grid.cells[this.x][this.y + 1].chain = this.chain + 1;
                        
                        pathTransition3.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle3);
                        	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
            				grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
            				grid.cells[this.x-1][this.y].fire=true;
            				Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                        	
                    		grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                    		grid.cells[this.x+1][this.y].fire=true;
                    		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                    		grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                    		grid.cells[this.x][this.y+1].fire=true;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                    		
                    		currentPlayer = -1;
                    		grid.noOfCellsOfPlayer[grid.currentPlayer]--;
                        	
                        	if (grid.firstTurn) {
                        		int cnt = 0;
                            	int k = -1;
                            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                            		if (grid.noOfCellsOfPlayer[i] > 0) {
                            			cnt++;
                            			k = i;
                            		}
                            		else {
                            			grid.eliminatedPlayer[i] = true;
                            		}
                            	}
                            	if (cnt == 1) {
                        			Text message = new Text();
                        			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                        			File z=new File("./src/resume.ser");
                            		z.delete();
                        			message.setTranslateX(100);
                        			message.setTranslateY(80);
                        			message.setStroke(colourVals[3]);
                        			grid.root.getChildren().add(message);
                        			for (int i = 0; i < grid.m; i++) {
                                		for (int j = 0; j < grid.n; j++) {
                                			grid.panes[i][j].getChildren().clear();
                                		}
                                	}
                            		grid.gameEnd = true;
                            	}
                        	}
                        	
                        	if (chain == 0) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	}
                        	}

                        	for (int i = 0; i < grid.m; i++) {
                        		for (int j = 0; j < grid.n; j++) {
                        			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                        			grid.rectangles[i][j].setStroke(colourVals[ind]);
                        		}
                        	}
                        	
                        });
                        pathTransition1.play();
                        pathTransition2.play();
                        pathTransition3.play();			
            		}
        			
        			else if (x == grid.m - 1) {
        				transition = true;
        				root.getChildren().add(tcircle1);
                        root.getChildren().add(tcircle2);
                        root.getChildren().add(tcircle3);
                        Path path1 = new Path();
                        path1.getElements().add(new MoveTo(25,25));
                        path1.getElements().add(new HLineTo(75));
                        PathTransition pathTransition1 = new PathTransition();
                        pathTransition1.setDuration(Duration.millis(500));
                        pathTransition1.setPath(path1);
                        pathTransition1.setNode(tcircle1);
                        pathTransition1.setCycleCount(1);
                        pathTransition1.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle1);
                        });
                        Path path2 = new Path();
                        path2.getElements().add(new MoveTo(25,25));
                        path2.getElements().add(new HLineTo(-25));
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(500));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(tcircle2);
                        pathTransition2.setCycleCount(1);
                        pathTransition2.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle2);
                        });
                        Path path3 = new Path();
                        path3.getElements().add(new MoveTo(25,25));
                        path3.getElements().add(new VLineTo(-25));
                        PathTransition pathTransition3 = new PathTransition();
                        pathTransition3.setDuration(Duration.millis(500));
                        pathTransition3.setPath(path3);
                        pathTransition3.setNode(tcircle3);
                        pathTransition3.setCycleCount(1);
                        
                        grid.cells[this.x - 1][this.y].chain = this.chain + 1;
            			grid.cells[this.x][this.y + 1].chain = this.chain + 1;
            			grid.cells[this.x][this.y - 1].chain = this.chain + 1;
            			
                        pathTransition3.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle3);
                        	if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
            				grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
            				grid.cells[this.x][this.y-1].fire=true;
            				Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                			grid.cells[this.x-1][this.y].fire=true;
                			Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                    		grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                    		grid.cells[this.x][this.y+1].fire=true;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                    		
                    		currentPlayer = -1;
                    		grid.noOfCellsOfPlayer[grid.currentPlayer]--;
                        	
                        	if (grid.firstTurn) {
                        		int cnt = 0;
                            	int k = -1;
                            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                            		if (grid.noOfCellsOfPlayer[i] > 0) {
                            			cnt++;
                            			k = i;
                            		}
                            		else {
                            			grid.eliminatedPlayer[i] = true;
                            		}
                            	}
                            	if (cnt == 1) {
                        			Text message = new Text();
                        			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                        			File z=new File("./src/resume.ser");
                            		z.delete();
                        			message.setTranslateX(100);
                        			message.setTranslateY(80);
                        			message.setStroke(colourVals[3]);
                        			grid.root.getChildren().add(message);
                        			for (int i = 0; i < grid.m; i++) {
                                		for (int j = 0; j < grid.n; j++) {
                                			grid.panes[i][j].getChildren().clear();
                                		}
                                	}
                            		grid.gameEnd = true;
                            	}
                        	}
                        	
                        	if (chain == 0) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	}
                        	}

                        	for (int i = 0; i < grid.m; i++) {
                        		for (int j = 0; j < grid.n; j++) {
                        			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                        			grid.rectangles[i][j].setStroke(colourVals[ind]);
                        		}
                        	}
                        });
                        pathTransition1.play();
                        pathTransition2.play();
                        pathTransition3.play();   
            		}
        			
        			else if (y == grid.n - 1) {
        				transition = true;
        				root.getChildren().add(tcircle1);
                        root.getChildren().add(tcircle2);
                        root.getChildren().add(tcircle3);
                        Path path1 = new Path();
                        path1.getElements().add(new MoveTo(25,25));
                        path1.getElements().add(new HLineTo(-25));
                        PathTransition pathTransition1 = new PathTransition();
                        pathTransition1.setDuration(Duration.millis(500));
                        pathTransition1.setPath(path1);
                        pathTransition1.setNode(tcircle1);
                        pathTransition1.setCycleCount(1);
                        pathTransition1.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle1);
                        });
                        Path path2 = new Path();
                        path2.getElements().add(new MoveTo(25,25));
                        path2.getElements().add(new VLineTo(-25));
                        PathTransition pathTransition2 = new PathTransition();
                        pathTransition2.setDuration(Duration.millis(500));
                        pathTransition2.setPath(path2);
                        pathTransition2.setNode(tcircle2);
                        pathTransition2.setCycleCount(1);
                        pathTransition2.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle2);
                        });
                        Path path3 = new Path();
                        path3.getElements().add(new MoveTo(25,25));
                        path3.getElements().add(new VLineTo(75));
                        PathTransition pathTransition3 = new PathTransition();
                        pathTransition3.setDuration(Duration.millis(500));
                        pathTransition3.setPath(path3);
                        pathTransition3.setNode(tcircle3);
                        pathTransition3.setCycleCount(1);
                        
                        grid.cells[this.x + 1][this.y].chain = this.chain + 1;
                        grid.cells[this.x - 1][this.y].chain = this.chain + 1;
            			grid.cells[this.x][this.y - 1].chain = this.chain + 1;
            			
                        pathTransition3.setOnFinished(e -> {
                        	root.getChildren().remove(tcircle3);
                        	if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
            				grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
            				grid.cells[this.x][this.y-1].fire=true;
            				Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                			grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                			grid.cells[this.x-1][this.y].fire=true;
                			Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));

                        	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[currentPlayer]++;
                			}
                    		grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                    		grid.cells[this.x+1][this.y].fire=true;
                    		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                    		
                    		currentPlayer = -1;
                    		grid.noOfCellsOfPlayer[grid.currentPlayer]--;
                        	
                        	if (grid.firstTurn) {
                        		int cnt = 0;
                            	int k = -1;
                            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                            		if (grid.noOfCellsOfPlayer[i] > 0) {
                            			cnt++;
                            			k = i;
                            		}
                            		else {
                            			grid.eliminatedPlayer[i] = true;
                            		}
                            	}
                            	if (cnt == 1) {
                        			Text message = new Text();
                        			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                        			File z=new File("./src/resume.ser");
                            		z.delete();
                        			message.setTranslateX(100);
                        			message.setTranslateY(80);
                        			message.setStroke(colourVals[3]);
                        			grid.root.getChildren().add(message);
                        			for (int i = 0; i < grid.m; i++) {
                                		for (int j = 0; j < grid.n; j++) {
                                			grid.panes[i][j].getChildren().clear();
                                		}
                                	}
                            		grid.gameEnd = true;
                            	}
                        	}
                        	
                        	if (chain == 0) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                            	}
                        	}

                        	for (int i = 0; i < grid.m; i++) {
                        		for (int j = 0; j < grid.n; j++) {
                        			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                        			grid.rectangles[i][j].setStroke(colourVals[ind]);
                        		}
                        	}
                        	
                        	
                        });
                        pathTransition1.play();
                        pathTransition2.play();
                        pathTransition3.play();
            		}
        		}
        		
        		else if (maxOrbs == 4) {
            		pane.getChildren().clear();

            		Circle tcircle1 = new Circle();
        			Circle tcircle2 = new Circle();
        			Circle tcircle3 = new Circle();
        			Circle tcircle4 = new Circle();
        			tcircle1.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle1.setRadius(7.5);
                	tcircle1.setLayoutX(70 + y * 50);
                    tcircle1.setLayoutY(100 + x * 50);
            		tcircle2.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle2.setRadius(7.5);
                    tcircle2.setLayoutX(70 + y * 50);
                    tcircle2.setLayoutY(100 + x * 50);
                    tcircle3.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle3.setRadius(7.5);
                    tcircle3.setLayoutX(70 + y * 50);
                    tcircle3.setLayoutY(100 + x * 50);
                    tcircle4.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	tcircle4.setRadius(7.5);
                    tcircle4.setLayoutX(70 + y * 50);
                    tcircle4.setLayoutY(100 + x * 50);
            		
                    transition = true;
                    root.getChildren().add(tcircle1);
                    root.getChildren().add(tcircle2);
                    root.getChildren().add(tcircle3);
                    root.getChildren().add(tcircle4);
                    Path path1 = new Path();
                    path1.getElements().add(new MoveTo(25,25));
                    path1.getElements().add(new HLineTo(75));
                    PathTransition pathTransition1 = new PathTransition();
                    pathTransition1.setDuration(Duration.millis(500));
                    pathTransition1.setPath(path1);
                    pathTransition1.setNode(tcircle1);
                    pathTransition1.setCycleCount(1);
                    pathTransition1.setOnFinished(e -> {
                    	root.getChildren().remove(tcircle1);
                    });
                    Path path2 = new Path();
                    path2.getElements().add(new MoveTo(25,25));
                    path2.getElements().add(new HLineTo(-25));
                    PathTransition pathTransition2 = new PathTransition();
                    pathTransition2.setDuration(Duration.millis(500));
                    pathTransition2.setPath(path2);
                    pathTransition2.setNode(tcircle2);
                    pathTransition2.setCycleCount(1);
                    pathTransition2.setOnFinished(e -> {
                    	root.getChildren().remove(tcircle2);
                    });
                    Path path3 = new Path();
                    path3.getElements().add(new MoveTo(25,25));
                    path3.getElements().add(new VLineTo(75));
                    PathTransition pathTransition3 = new PathTransition();
                    pathTransition3.setDuration(Duration.millis(500));
                    pathTransition3.setPath(path3);
                    pathTransition3.setNode(tcircle3);
                    pathTransition3.setCycleCount(1);
                    pathTransition3.setOnFinished(e -> {
                    	root.getChildren().remove(tcircle3);
                    });
                    Path path4 = new Path();
                    path4.getElements().add(new MoveTo(25,25));
                    path4.getElements().add(new VLineTo(-25));
                    PathTransition pathTransition4 = new PathTransition();
                    pathTransition4.setDuration(Duration.millis(500));
                    pathTransition4.setPath(path4);
                    pathTransition4.setNode(tcircle4);
                    pathTransition4.setCycleCount(1);
                    
                    grid.cells[this.x + 1][this.y].chain = this.chain + 1;
                    grid.cells[this.x - 1][this.y].chain = this.chain + 1;
        			grid.cells[this.x][this.y + 1].chain = this.chain + 1;
        			grid.cells[this.x][this.y - 1].chain = this.chain + 1;
                    
                    pathTransition4.setOnFinished(e -> {
                    	root.getChildren().remove(tcircle4);
                    	if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
            				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
            					grid.noOfCellsOfPlayer[currentPlayer]++;
            				}
            			}
            			else {
            				grid.noOfCellsOfPlayer[currentPlayer]++;
            			}
                		grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                		grid.cells[this.x][this.y-1].fire=true;
                		Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		
                    	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
            				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
            					grid.noOfCellsOfPlayer[currentPlayer]++;
            				}
            			}
            			else {
            				grid.noOfCellsOfPlayer[currentPlayer]++;
            			}
                		grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                		grid.cells[this.x-1][this.y].fire=true;
                		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));

                    	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
            				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
            					grid.noOfCellsOfPlayer[currentPlayer]++;
            				}
            			}
            			else {
            				grid.noOfCellsOfPlayer[currentPlayer]++;
            			}
                		grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                		grid.cells[this.x+1][this.y].fire=true;
                		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));

                    	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
            				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
            					grid.noOfCellsOfPlayer[currentPlayer]++;
            				}
            			}
            			else {
            				grid.noOfCellsOfPlayer[currentPlayer]++;
            			}
                		grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                		grid.cells[this.x][this.y+1].fire=true;
                		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		
                		currentPlayer = -1;
                		grid.noOfCellsOfPlayer[grid.currentPlayer]--;
                    	
                    	
                    	if (grid.firstTurn) {
                    		int cnt = 0;
                        	int k = -1;
                        	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                        		if (grid.noOfCellsOfPlayer[i] > 0) {
                        			cnt++;
                        			k = i;
                        		}
                        		else {
                        			grid.eliminatedPlayer[i] = true;
                        		}
                        	}
                        	if (cnt == 1) {
                    			Text message = new Text();
                    			message.setText("Player " + grid.settings.presentColours[k] + " won!");
                    			File z=new File("./src/resume.ser");
                        		z.delete();
                    			message.setTranslateX(100);
                    			message.setTranslateY(80);
                    			message.setStroke(colourVals[3]);
                    			grid.root.getChildren().add(message);
                    			for (int i = 0; i < grid.m; i++) {
                            		for (int j = 0; j < grid.n; j++) {
                            			grid.panes[i][j].getChildren().clear();
                            		}
                            	}
                        		grid.gameEnd = true;
                        	}
                    	}
                    	
                    	if (chain == 0) {
                    		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
                        	}
                    	}

                    	for (int i = 0; i < grid.m; i++) {
                    		for (int j = 0; j < grid.n; j++) {
                    			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                    			grid.rectangles[i][j].setStroke(colourVals[ind]);
                    		}
                    	}
                    });
                    pathTransition1.play();
                    pathTransition2.play();
                    pathTransition3.play();
                    pathTransition4.play();
            	}
        	}
        	
        	else if (numberOfOrbs == 0) {
        		this.rot.setRate(2);
        		circle1.setTranslateX(24);
                circle1.setTranslateY(24);
                
                pane.getChildren().clear();
            	pane.getChildren().add(circle1);    	
            	numberOfOrbs++; 
        	}
        	
        	else if (numberOfOrbs == 1) {
        		this.rot.setRate(4);
        		Circle circle2 = new Circle();
        		circle2.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
            	circle2.setRadius(7.5);
        		                                
        		circle1.setTranslateX(20);
                circle1.setTranslateY(25);
                circle2.setTranslateX(30);
                circle2.setTranslateY(25);
                pane.getChildren().clear();
                pane.getChildren().add(circle1);
                pane.getChildren().add(circle2);    	
            	numberOfOrbs++; 
        	}
        	
        	else {
        		this.rot.setRate(6);
        		Circle circle2 = new Circle();
        		circle2.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
            	circle2.setRadius(7.5);
            	Circle circle3 = new Circle();
        		circle3.setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
            	circle3.setRadius(7.5);                           
        		circle1.setTranslateX(17.5);
                circle1.setTranslateY(29.33);
                circle2.setTranslateX(32.5);
                circle2.setTranslateY(29.33);
                circle3.setTranslateX(25);
                circle3.setTranslateY(16.33);
                pane.getChildren().clear();
                pane.getChildren().add(circle1);
                pane.getChildren().add(circle2);
                pane.getChildren().add(circle3);    	
            	numberOfOrbs++; 
        	}
        	
        	if (!transition && chain == 0) {
        		if (grid.firstTurn) {
            		int cnt = 0;
                	int k = -1;
                	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                		if (grid.noOfCellsOfPlayer[i] > 0) {
                			cnt++;
                			k = i;
                		}
                		else {
                			grid.eliminatedPlayer[i] = true;
                		}
                	}
                	if (cnt == 1) {
            			Text message = new Text();
            			message.setText("Player " + grid.settings.presentColours[k] + " won!");
            			File z=new File("./src/resume.ser");
                		z.delete();
            			message.setTranslateX(100);
            			message.setTranslateY(80);
            			message.setStroke(colourVals[3]);
            			grid.root.getChildren().add(message);
            			for (int i = 0; i < grid.m; i++) {
                    		for (int j = 0; j < grid.n; j++) {
                    			grid.panes[i][j].getChildren().clear();
                    		}
                    	}
                		grid.gameEnd = true;
                	}
            	}
        		
        		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
            	while (grid.eliminatedPlayer[grid.currentPlayer]) {
            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
            	}
        		
            	if (!grid.gameEnd) {
            		for (int i = 0; i < grid.m; i++) {
                		for (int j = 0; j < grid.n; j++) {
                			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
                			grid.rectangles[i][j].setStroke(colourVals[ind]);
                		}
                	}
            	}
        	}
        	chain = 0;
        	transition = false;
        	
			ObjectOutputStream out=null;
			try {
				out=new	ObjectOutputStream(new FileOutputStream("./src/resume.ser"));
				out.writeObject(game);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	fire=false;
    }
}