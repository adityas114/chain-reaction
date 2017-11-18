import java.io.Serializable;


/**
 * Holds the name and color of each player in the game.
 * 
 * @author  Aditya Singh
 * @author  Shivin Dass
 */
public class Player implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
     * Name of the player.  
     */
	String name;
	
	/**
     * Color of the player.  
     */
	String colour;
}
