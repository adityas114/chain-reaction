import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Settings {
	public int noOfPlayers;
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<String> colours = new ArrayList<String>();
	public Paint[] colourValues = {Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.YELLOW, Color.BEIGE, Color.BROWN, Color.HOTPINK};
	public Settings() {
		colours.add("Red");
		colours.add("Green");
		colours.add("Blue");
		colours.add("White");
		colours.add("Yellow");
		colours.add("Beige");
		colours.add("Brown");
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
