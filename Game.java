import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application {
	private Pane root = new Pane();
	private Pane menu = new Pane();
	private Pane settingsPage = new Pane();
	private Stage stage;
	private Settings settings = new Settings();	
	private Grid grid = new Grid();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Parent createSettingsPage() {
		settingsPage.getChildren().clear();
		stage.setHeight(600);
		stage.setWidth(600);
		Button back = new Button("BACK");
		back.setTranslateX(20);
		back.setTranslateY(20);
		settingsPage.getChildren().add(back);
		back.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createMenu());
        });
		
		Text noOfPlayers = new Text();
		noOfPlayers.setText("Number of players");
		ChoiceBox<String> selectNoOfPlayers = new ChoiceBox<String>(FXCollections.observableArrayList(
			"2", "3", "4", "5", "6", "7", "8"
		));
		selectNoOfPlayers.setValue(Integer.toString(settings.noOfPlayers));
		selectNoOfPlayers.setOnAction(event -> {
			settings.noOfPlayers = Integer.parseInt((String) selectNoOfPlayers.getValue());
			this.stage.getScene().setRoot(createSettingsPage());
		});
		noOfPlayers.setTranslateX(50);
		noOfPlayers.setTranslateY(100);
		selectNoOfPlayers.setTranslateX(220);
		selectNoOfPlayers.setTranslateY(75);
		settingsPage.getChildren().add(selectNoOfPlayers);
		settingsPage.getChildren().add(noOfPlayers);
		
		Text gridSize = new Text();
		gridSize.setText("Grid size");
		ChoiceBox<String> selectGridSize = new ChoiceBox<String>(FXCollections.observableArrayList(
			"9 x 6", "15 x 10"
		));
		selectGridSize.setValue("9 x 6");
//		selectGridSize.setOnAction(event -> {
//			System.out.println(selectNoOfPlayers.getValue().length());
//			grid.m = Integer.parseInt(selectNoOfPlayers.getValue().substring(0, 1));
//			grid.n = Integer.parseInt(selectNoOfPlayers.getValue().substring(4, 5));
//		});
		gridSize.setTranslateX(50);
		gridSize.setTranslateY(150);
		selectGridSize.setTranslateX(220);
		selectGridSize.setTranslateY(125);
		settingsPage.getChildren().add(gridSize);
		settingsPage.getChildren().add(selectGridSize);
	
		for (int i = 0; i < settings.noOfPlayers; i++) {
			Text player = new Text();
			player.setText("Player " + (i + 1) + " colour");
			ChoiceBox<String> selectPlayer = new ChoiceBox<String>(FXCollections.observableArrayList(
				"Red", "Green", "Blue", "White", "Yellow", "Beige", "Brown", "Pink"
			));
			if (i == 0) {
				selectPlayer.setOnAction(event -> {
		        	settings.players.get(0).colour = selectPlayer.getValue();
		        });
			}
			
			selectPlayer.setValue(settings.players.get(i).colour);
			player.setTranslateX(50);
			player.setTranslateY(200 + 50 * i);
			selectPlayer.setTranslateX(220);
			selectPlayer.setTranslateY(175 + 50 * i);
			settingsPage.getChildren().add(selectPlayer);
			settingsPage.getChildren().add(player);
			
			settings.players.get(i).colour = selectPlayer.getValue();
		}
		
		return settingsPage;
	}
	
	private Parent createMenu() {
		stage.setHeight(600);
		stage.setWidth(600);
		Button play = new Button("PLAY");
		play.setTranslateX(280);
		play.setTranslateY(150);
		menu.getChildren().add(play);
		play.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createContent());
        });
		
		Button settingsPage = new Button("SETTINGS");
		settingsPage.setTranslateX(267);
		settingsPage.setTranslateY(250);
		menu.getChildren().add(settingsPage);
		settingsPage.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createSettingsPage());
        });
		
		Button exit = new Button("EXIT");
		exit.setTranslateX(282);
		exit.setTranslateY(350);
		menu.getChildren().add(exit);
		exit.setOnMouseClicked(event -> {
        	Platform.exit();
        });
		
		return menu;
	}
	
	private Parent createContent() {
		root.getChildren().clear();
		stage.setHeight(750);
		stage.setWidth(920);
        root.setStyle("-fx-background-color: #073e42;");
        Cell.grid = grid;
        grid.cells = new Cell[6][9];
        
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = new Cell();
                cell.x = i;	
                cell.y = j;
                cell.setTranslateX(j * 100);
                cell.setTranslateY(100 + i * 100);
                grid.cells[i][j] = cell;
               
                if (i == 0 && j == 0 || i == 0 && j == grid.n - 1 || i == grid.m - 1 && j == 0 || i == grid.m - 1 && j == grid.n - 1) {
                	cell.maxOrbs = 2;
                } else if (i == 0 || j == 0 || i == grid.m - 1 || j == grid.n - 1) {
                	cell.maxOrbs = 3;
                } else {
                	cell.maxOrbs = 4;
                }
                
                root.getChildren().add(cell);
            }
        }
        
        Button back = new Button("BACK");
		back.setTranslateX(20);
		back.setTranslateY(20);
		root.getChildren().add(back);
		back.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createMenu());
        });
        
        return root;
	}
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		stage.setHeight(600);
		stage.setWidth(600);
		stage.setResizable(false);
		stage.setTitle("Chain Reaction");
		grid.settings = settings;
		stage.setScene(new Scene(createMenu()));
		stage.show();
	}
}
