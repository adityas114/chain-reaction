import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Settings {
	public int m = 9;
	public int n = 6;
	public int noOfPlayers;
	public ArrayList<Player> players = new ArrayList<Player>();
	public ArrayList<String> colours = new ArrayList<String>();
	public Paint[] colourValues = {Color.RED, Color.GREEN, Color.BLUE, Color.WHITE, Color.YELLOW, Color.AQUA, Color.BLUEVIOLET, Color.HOTPINK};
	public String[] presentColours = {"Red", "Green", "Blue", "White", "Yellow", "Light Blue", "Purple", "Pink"};
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
