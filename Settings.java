import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


/**
 * This class stores the information provided
 * by the user to manage the state of the game
 * along with managing the color of each player.
 * 
 * @author  Aditya Singh
 * @author  Shivin Dass
 */

public class Settings implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * Height of the grid, in terms of the number of cells.  
     * Default value is 9.
     */
	int m = 9;
	
	/**
     * Width of the grid, in terms of the number of cells. 
     * Default value is 6. 
     */
	int n = 6;
	
	/**
     * Number of players playing the game.  
     */
	int noOfPlayers;
	
	/**
     * Array of Player class, representing each player of the game.  
     */
	ArrayList<Player> players = new ArrayList<Player>();
	
	/**
     * Holds color of each player in the corresponding index.
     */
	ArrayList<String> colours = new ArrayList<String>();
	
	/**
     * A constant array of color values for each player.  
     */
	transient Paint[] colourValues = {Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.YELLOW, Color.AQUA, Color.BLUEVIOLET, Color.HOTPINK};
	
	/**
     * Initial colors of each player.  
     */
	String[] presentColours = {"Red", "Green", "Blue", "White", "Yellow", "Light Blue", "Purple", "Pink"};
	
	
	/**
     * Constructor of the settings class.
     * Initializes the color values of each player to default.   
     */
	public Settings() {
		colours.add("Red");
		colours.add("Green");
		colours.add("Blue");
		colours.add("White");
		colours.add("Yellow");
		colours.add("Light Blue");
		colours.add("Purple");
		colours.add("Pink");
		for (int i = 0; i < 8; i++) {
			Player player = new Player();
			player.name = "Player " + (i + 1);
			player.colour = colours.get(i);
			players.add(player);
		}
		noOfPlayers = 2;
	}
}
