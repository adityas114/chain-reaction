import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
		if (settings.m == 9) {
			selectGridSize.setValue("9 x 6");
		}
		else {
			selectGridSize.setValue("15 x 10");
		}
		selectGridSize.setOnAction(event -> {
			if (selectGridSize.getValue() == "9 x 6") {
				settings.m = 9;
				settings.n = 6;
			} else {
				settings.m = 15;
				settings.n = 10;
			}
		});
		gridSize.setTranslateX(50);
		gridSize.setTranslateY(150);
		selectGridSize.setTranslateX(220);
		selectGridSize.setTranslateY(125);
		settingsPage.getChildren().add(gridSize);
		settingsPage.getChildren().add(selectGridSize);
	
		ChoiceBox[] selectPlayers = new ChoiceBox[8];
		for (int i = 0; i < 8; i++) {
			Text player = new Text();
			player.setText("Player " + (i + 1) + " colour");
			ChoiceBox<String> selectPlayer = new ChoiceBox<String>(FXCollections.observableArrayList(
				"Red", "Green", "Blue", "White", "Yellow", "Light Blue", "Purple", "Pink"
			));
			selectPlayers[i] = selectPlayer;
			
			selectPlayer.setValue(settings.players.get(i).colour);
			player.setTranslateX(50);
			player.setTranslateY(200 + 50 * i);
			selectPlayer.setTranslateX(220);
			selectPlayer.setTranslateY(175 + 50 * i);
			if (i < settings.noOfPlayers) {
				settingsPage.getChildren().add(selectPlayer);
				settingsPage.getChildren().add(player);
			}
			
			settings.players.get(i).colour = selectPlayer.getValue();
		}
		
		for (int i = 0; i < 8; i++) {
			int k = i;
			Player _player = settings.players.get(i);
			ChoiceBox<String> selectPlayer = selectPlayers[i];
			selectPlayer.setOnAction(event -> {
				String colour = _player.colour;
				for (int j = 0; j < 8; j++) {
					if (settings.players.get(j).colour == selectPlayer.getValue()) {
						selectPlayers[j].setValue(colour);
						settings.presentColours[j] = colour;
					}
				}
				settings.presentColours[k] = selectPlayer.getValue();
				_player.colour = selectPlayer.getValue();
		    });
		}
		
		
//		Player _player = settings.players.get(i);
//		selectPlayer.setOnAction(event -> {
//			_player.colour = selectPlayer.getValue();
//	    });
		
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
	
	public Parent createContent() {
		grid = new Grid();
		grid.settings = settings;
		grid.m = settings.m;
		grid.n = settings.n;
		grid.root = root;
		root.getChildren().clear();
		stage.setHeight(grid.m * 50 + 200);
		stage.setWidth(grid.n * 50 + 140);
        root.setStyle("-fx-background-color: #000000;");
        Cell.grid = grid;
        grid.cells = new Cell[grid.m][grid.n];
        grid.rectangles = new Rectangle[grid.m][grid.n];
        grid.panes = new Pane[grid.m][grid.n];
        
        for (int i = 0; i < grid.m; i++) {
            for (int j = 0; j < grid.n; j++) {
                Cell cell = new Cell(i, j);
                cell.setTranslateX(70 + j * 50);
                cell.setTranslateY(100 + i * 50);
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
