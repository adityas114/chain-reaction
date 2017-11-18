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
     * Objects used to define the animation path.
     */
	transient PathTransition[] pathTransitions = new PathTransition[4];
	
	/**
     * Objects used to create the animation orbs.
     */
	transient Circle[] tcircles = new Circle[4];
	
	/**
     * Objects used to draw the orbs on the cell.
     */
	transient Circle[] circles = new Circle[3];
	
	/**
     * Used to define the X coordinates of the adjoining cells during an explosion.
     */
    int[] xCoordinates = new int[4];
    
    /**
     * Used to define the Y coordinates of the adjoining cells during an explosion.
     */
    int[] yCoordinates = new int[4];
    
    /**
     * Used to keep the count of the number of simultaneous clicks, during a chain reaction.
     */
    int clickCount = 0;
    
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
        
        rot.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(
                        branch.rotateProperty(), 0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(branch
                        .rotateProperty(), 360)));
        rot.playFromStart();
        
        grid.rectangles[x][y] = border;
        grid.panes[x][y] = pane;
        
        setOnMouseClicked(event -> { 
        	onClick();
        });
    }
    
    /**
     * Used to set the coordinates of the cells surrounding the current cell, when there is an explosion.
     * 
     * @param n number of cells surrounding the current cell.
     * @param x X coordinate of the cell.
     * @param y Y coordinate of the cell.
     */
    public void setCoordinates(int n, int x, int y) {
    	if (n == 2) {
            if (x == 0 && y == 0) {
                xCoordinates[0] = x + 1;
                yCoordinates[0] = y;
                xCoordinates[1] = x;
                yCoordinates[1] = y + 1;
            }
            else if (x == 0 && y == grid.n - 1) {
                xCoordinates[0] = x + 1;
                yCoordinates[0] = y;
                xCoordinates[1] = x;
                yCoordinates[1] = y - 1;
            }
            else if (x == grid.m - 1 && y == 0) {
                xCoordinates[0] = x - 1;
                yCoordinates[0] = y;
                xCoordinates[1] = x;
                yCoordinates[1] = y + 1;
            }
            else if (x == grid.m - 1 && y == grid.n - 1) {
                xCoordinates[0] = x - 1;
                yCoordinates[0] = y;
                xCoordinates[1] = x;
                yCoordinates[1] = y - 1;
            }
        }

        if (n == 3) {
            if (x == 0) {
                xCoordinates[0] = x + 1;
                yCoordinates[0] = y;
                xCoordinates[1] = x;
                yCoordinates[1] = y + 1;
                xCoordinates[2] = x;
                yCoordinates[2] = y - 1;
            }
            else if (y == 0) {
                xCoordinates[0] = x - 1;
                yCoordinates[0] = y;
                xCoordinates[1] = x + 1;
                yCoordinates[1] = y;
                xCoordinates[2] = x;
                yCoordinates[2] = y + 1;
            }
            else if (x == grid.m - 1) {
                xCoordinates[0] = x - 1;
                yCoordinates[0] = y;
                xCoordinates[1] = x;
                yCoordinates[1] = y + 1;
                xCoordinates[2] = x;
                yCoordinates[2] = y - 1;
            }
            else if (y == grid.n - 1) {
                xCoordinates[0] = x - 1;
                yCoordinates[0] = y;
                xCoordinates[1] = x;
                yCoordinates[1] = y - 1;
                xCoordinates[2] = x + 1;
                yCoordinates[2] = y;
            }
        }
        else if (n == 4) {
            xCoordinates[0] = x - 1;
            yCoordinates[0] = y;
            xCoordinates[1] = x;
            yCoordinates[1] = y - 1;
            xCoordinates[2] = x + 1;
            yCoordinates[2] = y;
            xCoordinates[3] = x;
            yCoordinates[3] = y + 1;
        }
    }
  
    /**
     * This function is called when the user clicks the cell.
     * It handles the cases of when there is an explosion and when a orb is added to the cell.  
     */
    public void onClick() {    	
    	for (int i = 0; i < 4; i++) {
    		pathTransitions[i] = new PathTransition();
    	}
    	
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
    		if(fire == false)
    		{
    			ObjectOutputStream out=null;
    			try {
    				out=new	ObjectOutputStream(new FileOutputStream("./src/undo.ser"));
    				out.writeObject(game);
    				out.close();
    			} catch (FileNotFoundException e) {
    				e.printStackTrace();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		
    		if (currentPlayer == -1) {
    			grid.noOfCellsOfPlayer[grid.currentPlayer]++;
    		}
        	currentPlayer = grid.currentPlayer;
    		
        	if (numberOfOrbs == maxOrbs - 1) {
        		fire=true;
        		transition = true;        
        		numberOfOrbs = 0;
        		
        		if (maxOrbs == 2) {
        			setCoordinates(2, x, y);
            		createTransitions(2, x, y);
            	}
        		
        		else if (maxOrbs == 3) {
        			setCoordinates(3, x, y);
            		createTransitions(3, x, y);
        		}
        		
        		else if (maxOrbs == 4) {
        			setCoordinates(4, x, y);
            		createTransitions(4, x, y);
        		}
        	}
        	else {
        		for (int i = 0; i < 3; i++) {
                    circles[i] = new Circle();
                    circles[i].setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                    circles[i].setRadius(7.5);
        		}
        		
        		if (numberOfOrbs == 0) {
            		this.rot.setRate(2);
            		circles[0].setTranslateX(24);
                    circles[0].setTranslateY(24);
                    
                    pane.getChildren().clear();
                	pane.getChildren().add(circles[0]);    	
                	numberOfOrbs++; 
            	}
            	
            	else if (numberOfOrbs == 1) {
            		this.rot.setRate(4);       
            		circles[0].setTranslateX(20);
                    circles[0].setTranslateY(25);
                    circles[1].setTranslateX(30);
                    circles[1].setTranslateY(25);
                    pane.getChildren().clear();
                    pane.getChildren().add(circles[0]);
                    pane.getChildren().add(circles[1]);    	
                	numberOfOrbs++; 
            	}
            	
            	else {
            		this.rot.setRate(6);                         
            		circles[0].setTranslateX(17.5);
                    circles[0].setTranslateY(29.33);
                    circles[1].setTranslateX(32.5);
                    circles[1].setTranslateY(29.33);
                    circles[2].setTranslateX(25);
                    circles[2].setTranslateY(16.33);
                    pane.getChildren().clear();
                    pane.getChildren().add(circles[0]);
                    pane.getChildren().add(circles[1]);
                    pane.getChildren().add(circles[2]);    	
                	numberOfOrbs++; 
            	}
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
        	clickCount--;
        	if (clickCount == 0) {
        		this.chain = 0;
        	}
        	transition = false;
        	
			ObjectOutputStream out=null;
			try {
				out=new	ObjectOutputStream(new FileOutputStream("./src/resume.ser"));
				out.writeObject(game);
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	fire=false;
    }
    
    /**
     * It renders the animation of the explosion of a cell.
     * It call the chainReaction() method after the animation is over, to handle the logic of the explosion.
     * 
     * @param n number of cells surrounding the current cell.
	 * @param x X coordinate of the cell.
	 * @param y Y coordinate of the cell.
	 */
	public void createTransitions(int n, int x, int y) {
		for (int i = 0; i < 4; i++) {
    		tcircles[i] = new Circle();
    		tcircles[i].setFill(colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
        	tcircles[i].setRadius(7.5);
        	tcircles[i].setLayoutX(70 + y * 50);
            tcircles[i].setLayoutY(100 + x * 50);
		}
		
		pane.getChildren().clear();
		boolean[] t = new boolean[4];
		
		if (n == 2) {
            if (x == 0 && y == 0) {
                t[0] = true;
                t[2] = true;
            }
            else if (x == 0 && y == grid.n - 1) {
                t[1] = true;
                t[2] = true;
            }
            else if (x == grid.m - 1 && y == 0) {
                t[0] = true;
                t[3] = true;
            }
            else if (x == grid.m - 1 && y == grid.n - 1) {
                t[1] = true;
                t[3] = true;
            }
        }

        if (n == 3) {
            if (x == 0) {
                t[0] = true;
                t[1] = true;
                t[2] = true;
            }
            else if (y == 0) {
                t[0] = true;
                t[2] = true;
                t[3] = true;
            }
            else if (x == grid.m - 1) {
                t[0] = true;
                t[1] = true;
                t[3] = true;
            }
            else if (y == grid.n - 1) {
                t[1] = true;
                t[2] = true;
                t[3] = true;
            }
        }
        else if (n == 4) {
                t[0] = true;
                t[1] = true;
                t[2] = true;
                t[3] = true;
        }
        
  		for (int i = 0; i < n; i++) {
  			grid.cells[xCoordinates[i]][yCoordinates[i]].chain = this.chain + 1;
  			grid.cells[xCoordinates[i]][yCoordinates[i]].clickCount++;
  		}
  		
  		for (int i = 0; i < 4; i++) {
  			if (t[i]) {
  	  			root.getChildren().add(tcircles[i]);
  			}
  		}
	    
		if (t[0]) {
			Path path1 = new Path();
			path1.getElements().add(new MoveTo(25,25));
			path1.getElements().add(new HLineTo(75));
			pathTransitions[0].setDuration(Duration.millis(500));
			pathTransitions[0].setPath(path1);
			pathTransitions[0].setNode(tcircles[0]);
			pathTransitions[0].setCycleCount(1);
		}
		
		if (t[1]) {
			Path path2 = new Path();
			path2.getElements().add(new MoveTo(25,25));
			path2.getElements().add(new HLineTo(-25));
			pathTransitions[1].setDuration(Duration.millis(500));
			pathTransitions[1].setPath(path2);
			pathTransitions[1].setNode(tcircles[1]);
			pathTransitions[1].setCycleCount(1);
		}
		
		if (t[2]) {
			Path path3 = new Path();
			path3.getElements().add(new MoveTo(25,25));
			path3.getElements().add(new VLineTo(75));
			pathTransitions[2].setDuration(Duration.millis(500));
			pathTransitions[2].setPath(path3);
			pathTransitions[2].setNode(tcircles[2]);
			pathTransitions[2].setCycleCount(1);
		}
		
		if (t[3]) {
			Path path4 = new Path();
			path4.getElements().add(new MoveTo(25,25));
			path4.getElements().add(new VLineTo(-25));
			pathTransitions[3].setDuration(Duration.millis(500));
			pathTransitions[3].setPath(path4);
			pathTransitions[3].setNode(tcircles[3]);
			pathTransitions[3].setCycleCount(1);
		}

		for (int i = 3; i >=0; i--) {
			if (t[i]) {
				pathTransitions[i].setOnFinished(e -> {
		        	chainReaction(n, x, y);
		        });
				break;
			}
		}
        
		for (int i = 0; i < 4; i++) {
			if (t[i]) {
				pathTransitions[i].play();
			}
		}
	}

	/**
	 * It handles the explosion of the cell, by simulating a mouse click on the surrounding cells.
	 * 
	 * @param n number of cells surrounding the current cell.
	 * @param x X coordinate of the cell.
	 * @param y Y coordinate of the cell.
	 */
	public void chainReaction(int n, int x, int y) {
		for (int i = 0; i < 4; i++) {
			root.getChildren().remove(tcircles[i]);
		}
		
	    for (int i = 0; i < n; i++) {
	        if (grid.cells[xCoordinates[i]][yCoordinates[i]].currentPlayer != -1) {
	            if (grid.cells[xCoordinates[i]][yCoordinates[i]].currentPlayer != currentPlayer) {
	                grid.noOfCellsOfPlayer[grid.cells[xCoordinates[i]][yCoordinates[i]].currentPlayer]--;
	                grid.noOfCellsOfPlayer[currentPlayer]++;
	            }
	        }
	        else {
	            grid.noOfCellsOfPlayer[currentPlayer]++;
	        }
	        grid.cells[xCoordinates[i]][yCoordinates[i]].currentPlayer = currentPlayer;
	        grid.cells[xCoordinates[i]][yCoordinates[i]].fire=true;
	        Event.fireEvent(grid.cells[xCoordinates[i]][yCoordinates[i]], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
	    }
	
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
	}
}