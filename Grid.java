import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Grid {
	public int m = 9;
	public int n = 6;
	public Cell[][] cells = new Cell[1][];
	public boolean[] eliminatedPlayer = new boolean[8];
	public Rectangle[][] rectangles = new Rectangle[1][];
	public Pane[][] panes = new Pane[1][];
	public int[] noOfCellsOfPlayer = new int[8];
	int currentPlayer = 0;
	Settings settings;
	public boolean firstTurn = false;
	public boolean gameEnd = false;
	public Pane root;
}
