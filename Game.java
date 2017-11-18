import java.io.*;

import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;

/**
 * This is the main class where the start() function 
 * for the GUI is present.
 * This class plays the role of managing the UI, 
 * handling the graphics and creating the different pages 
 * of the game chain reaction.
 * 
 * @author  Aditya Singh
 * @author  Shivin Dass
 */

public class Game extends Application implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * Pane holding the state of the game.  
     */
	private transient Pane root = new Pane();
	
	/**
     * Pane holding the state of the main menu.  
     */
	private transient Pane menu = new Pane();
	
	/**
     * Pane holding the state of the settings menu.  
     */
	private transient Pane settingsPage = new Pane();
	
	/**
     * Pane holding the state of the load menu.  
     */
	private transient Pane loadMenu = new Pane();
	
	/**
     * Pane responsible for creating the save menu.  
     */
	private transient Pane saveMenu = new Pane();
	
	//
	private transient Stage stage;
	
	/**
     * Instance if the settings class.
     * Used to access and change information in settings object.  
     */
	private Settings settings = new Settings();	
	
	/**
     * Instance of the grid class.
     * Used to access the information stored in the grid object.
     */
	private Grid grid = new Grid();
	
	/**
     * Temporarily holds the name of the progress to be saved.
     */
	private String name;
	//private int index;
	
	/**
     * True when a file is loaded.
     * False otherwise.
     */
	private boolean loadFlag;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/**
     * Creates the interface for the settings page along with GUI.
     * Initializes the settings.
     * 
     * @return Pane This returns the state of the settings page.
     */
	private Parent createSettingsPage() {
		settingsPage.getChildren().clear();
		settingsPage.setStyle("-fx-background-color: #000000;");
		stage.setHeight(700);
		stage.setWidth(600);
		Button back = new Button("BACK");
		back.setTranslateX(20);
		back.setTranslateY(45);
		settingsPage.getChildren().add(back);
		back.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createMenu());
        });
		
		InnerShadow is = new InnerShadow();
		is.setOffsetX(4);
		is.setOffsetY(4);
		is.setColor(Color.GREEN);
		//System.out.println(Font.getFamilies());
		Text t = new Text();
		t.setEffect(is);	
		t.setText("SETTINGS");
		t.setFill(Color.BLACK);
		t.setFont(Font.font("Papyrus", FontWeight.BOLD, 45));
		t.setTranslateX(180);
		t.setTranslateY(80);
		settingsPage.getChildren().add(t);
		
		Text noOfPlayers = new Text();
		noOfPlayers.setFill(Color.WHITE);
		noOfPlayers.setText("Number of players:");
		ChoiceBox<String> selectNoOfPlayers = new ChoiceBox<String>(FXCollections.observableArrayList(
			"2", "3", "4", "5", "6", "7", "8"
		));
		selectNoOfPlayers.setValue(Integer.toString(settings.noOfPlayers));
		selectNoOfPlayers.setOnAction(event -> {
			settings.noOfPlayers = Integer.parseInt((String) selectNoOfPlayers.getValue());
			this.stage.getScene().setRoot(createSettingsPage());
		});
		noOfPlayers.setTranslateX(160);
		noOfPlayers.setTranslateY(137);
		selectNoOfPlayers.setTranslateX(330);
		selectNoOfPlayers.setTranslateY(125);
		settingsPage.getChildren().add(selectNoOfPlayers);
		settingsPage.getChildren().add(noOfPlayers);
		
		Text gridSize = new Text();
		gridSize.setFill(Color.WHITE);
		gridSize.setText("Grid size:");
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
		gridSize.setTranslateX(160);
		gridSize.setTranslateY(187);
		selectGridSize.setTranslateX(330);
		selectGridSize.setTranslateY(175);
		settingsPage.getChildren().add(gridSize);
		settingsPage.getChildren().add(selectGridSize);
	
		ChoiceBox[] selectPlayers = new ChoiceBox[8];
		for (int i = 0; i < 8; i++) {
			Text player = new Text();
			player.setFill(Color.WHITE);
			player.setText("Player " + (i + 1) + " colour:");
			ChoiceBox<String> selectPlayer = new ChoiceBox<String>(FXCollections.observableArrayList(
				"Red", "Green", "Blue", "White", "Yellow", "Light Blue", "Purple", "Pink"
			));
			selectPlayers[i] = selectPlayer;
			
			selectPlayer.setValue(settings.players.get(i).colour);
			player.setTranslateX(160);
			player.setTranslateY(240 + 50 * i);
			selectPlayer.setTranslateX(330);
			selectPlayer.setTranslateY(225 + 50 * i);
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
	
	
	/**
     * Creates the interface for the main page along with GUI.
     * Used for navigating between different pages.
     * 
     * @return Pane This returns the state of the main page.
     */
	private Parent createMenu() {
		menu.getChildren().clear();
		menu.setStyle("-fx-background-color: #000000;");
		loadFlag=false;
		stage.setHeight(700);
		stage.setWidth(600);
		
		
		InnerShadow is = new InnerShadow();
		is.setOffsetX(4);
		is.setOffsetY(4);
		is.setColor(Color.RED);
		
		//System.out.println(Font.getFamilies());
		Text t = new Text();
		t.setEffect(is);	
		t.setText("CHAIN REACTION");
		t.setFill(Color.BLACK);
		t.setFont(Font.font("Papyrus", FontWeight.BOLD, 45));

		t.setTranslateX(110);
		t.setTranslateY(80);
		menu.getChildren().add(t);
		
		
		File z=new File("./src/resume.ser");
		
		Button resume = new Button("RESUME");
		resume.setTranslateX(254);
		resume.setTranslateY(225);
		menu.getChildren().add(resume);
		resume.setDisable(true);
		if(z.exists()==true)
		{
			resume.setDisable(false);
			resume.setOnMouseClicked(event -> {
				ObjectInputStream in=null;	
				try {
					in=new ObjectInputStream(new FileInputStream("./src/"+z.getName()));
					Game x=(Game)in.readObject();
					this.settings=x.settings;
					this.grid=x.grid;
					loadFlag=true;
					in.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	        	this.stage.getScene().setRoot(createContent());
	        });
		}
		
		
		Button play = new Button("NEW GAME");
		play.setTranslateX(242);
		play.setTranslateY(150);
		menu.getChildren().add(play);
		play.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createContent());
        });
		
		Button load = new Button("LOAD");
		load.setTranslateX(263);
		load.setTranslateY(300);
		menu.getChildren().add(load);
		load.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createLoad());
        });
		
		Button settingsPage = new Button("SETTINGS");
		settingsPage.setTranslateX(251);
		settingsPage.setTranslateY(375);
		menu.getChildren().add(settingsPage);
		settingsPage.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createSettingsPage());
        });
		
		Button exit = new Button("EXIT");
		exit.setTranslateX(269);
		exit.setTranslateY(450);
		menu.getChildren().add(exit);
		exit.setOnMouseClicked(event -> {
        	Platform.exit();
        });
		
		return menu;
	}
	
	
	/**
     * Creates the interface for the game along with menu options and GUI.
     * Used for initializing the grid class appropriately.
     * 
     * @return Pane This returns the state of the game.
     */
	public Parent createContent() {
		if(loadFlag==false){
			grid = new Grid();
		}
		grid.settings = settings;
		grid.m = settings.m;
		grid.n = settings.n;
		grid.root = root;
		root.getChildren().clear();
		stage.setHeight(grid.m * 50 + 200);
		stage.setWidth(grid.n * 50 + 140);
        root.setStyle("-fx-background-color: #000000;");
        Cell.grid = grid;
        Cell.game=this;
        grid.rectangles = new Rectangle[grid.m][grid.n];
        grid.panes = new Pane[grid.m][grid.n];
        
        if(loadFlag==false)
        {
        	grid.cells = new Cell[grid.m][grid.n];
        
        	for (int i = 0; i < grid.m; i++) {
        		for (int j = 0; j < grid.n; j++) {
        			Cell cell = new Cell(root, i, j);
        			cell.setTranslateX(70 + j * 50);
        			cell.setTranslateY(100 + i * 50);
                          
        			if (i == 0 && j == 0 || i == 0 && j == grid.n - 1 || i == grid.m - 1 && j == 0 || i == grid.m - 1 && j == grid.n - 1) {
        				cell.maxOrbs = 2;
        			} else if (i == 0 || j == 0 || i == grid.m - 1 || j == grid.n - 1) {
        				cell.maxOrbs = 3;
        			} else {
        				cell.maxOrbs = 4;
        			}
        			grid.cells[i][j] = cell;
        			root.getChildren().add(grid.cells[i][j]);
        		}
        	}
        }
        else
        {
        	for (int i = 0; i < grid.m; i++) {
        		for (int j = 0; j < grid.n; j++) {
        			Cell cell = new Cell(root, i, j);
        			cell.setTranslateX(70 + j * 50);
        			cell.setTranslateY(100 + i * 50);
               
        			cell.numberOfOrbs=grid.cells[i][j].numberOfOrbs;
        			cell.maxOrbs=grid.cells[i][j].maxOrbs;
        			cell.currentPlayer=grid.cells[i][j].currentPlayer;
        			grid.cells[i][j]=cell;
        			root.getChildren().add(grid.cells[i][j]);
        			
        			if(cell.numberOfOrbs>0)
        			{
        				Circle circle1=new Circle();
                		circle1.setFill(cell.colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.cells[i][j].currentPlayer).colour)]);
                    	circle1.setRadius(7.5);
                    	

            			
            			if (cell.numberOfOrbs == 1) {
            				cell.rot.setRate(2);
                    		circle1.setTranslateX(24);
                            circle1.setTranslateY(24);
                            
                            cell.pane.getChildren().clear();
                        	cell.pane.getChildren().add(circle1); 
                    	}
                    	
                    	else if (cell.numberOfOrbs == 2) {
                    		                                
                        	cell.rot.setRate(4);
                    		Circle circle2 = new Circle();
                    		circle2.setFill(cell.colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.cells[i][j].currentPlayer).colour)]);
                        	circle2.setRadius(7.5);
                    		                                
                    		circle1.setTranslateX(20);
                            circle1.setTranslateY(25);
                            circle2.setTranslateX(30);
                            circle2.setTranslateY(25);
                            cell.pane.getChildren().clear();
                            cell.pane.getChildren().add(circle1);
                            cell.pane.getChildren().add(circle2);    		 
                        	
                    	}
                    	
                    	else if(cell.numberOfOrbs==3) {
                                  		                                
                        	cell.rot.setRate(6);
                    		Circle circle2 = new Circle();
                    		circle2.setFill(cell.colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.cells[i][j].currentPlayer).colour)]);
                        	circle2.setRadius(7.5);
                        	Circle circle3 = new Circle();
                    		circle3.setFill(cell.colourVals[grid.settings.colours.indexOf(grid.settings.players.get(grid.cells[i][j].currentPlayer).colour)]);
                        	circle3.setRadius(7.5);                           
                    		circle1.setTranslateX(17.5);
                            circle1.setTranslateY(29.33);
                            circle2.setTranslateX(32.5);
                            circle2.setTranslateY(29.33);
                            circle3.setTranslateX(25);
                            circle3.setTranslateY(16.33);
                            cell.pane.getChildren().clear();
                            cell.pane.getChildren().add(circle1);
                            cell.pane.getChildren().add(circle2);
                            cell.pane.getChildren().add(circle3);   	
                    	}
        			}
        		}
        	}        			
        }
        Button undo = new Button("UNDO");
		undo.setTranslateX(70);
		undo.setTranslateY(20);
		root.getChildren().add(undo);
		undo.setOnMouseClicked(event -> {
			ObjectInputStream in=null;	
			ObjectOutputStream out=null;
			try {
				in=new ObjectInputStream(new FileInputStream("./src/undo.ser"));
				Game y=(Game)in.readObject();
				this.settings=y.settings;
				this.grid=y.grid;
				loadFlag=true;
				in.close();
				out=new	ObjectOutputStream(new FileOutputStream("./src/resume.ser"));
				out.writeObject(y);
				out.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.stage.getScene().setRoot(createContent());
		});
		
        Button save = new Button("SAVE");
		save.setTranslateX(150);
		save.setTranslateY(20);
		root.getChildren().add(save);
		save.setOnMouseClicked(event -> {
				this.stage.getScene().setRoot(createSave());
        });
        
        /*Button back = new Button("BACK");
		back.setTranslateX(130);
		back.setTranslateY(20);
		root.getChildren().add(back);
		back.setOnMouseClicked(event -> {
			root.getChildren().clear();
        	this.stage.getScene().setRoot(createMenu());
        });*/
		
		ChoiceBox<String> option = new ChoiceBox<String>(FXCollections.observableArrayList(
				"OPTIONS","BACK", "NEW GAME"
			));
			option.setValue("OPTIONS");
			option.setOnAction(event -> {
				if (option.getValue().equals("BACK")) 
				{
					this.stage.getScene().setRoot(createMenu());
				}
				else if(option.getValue().equals("NEW GAME"))
				{
					loadFlag=false;
					this.stage.getScene().setRoot(createContent());
				}
			});
		
		option.setTranslateX(220);
		option.setTranslateY(20);
		root.getChildren().add(option);
		ObjectOutputStream out=null;
		try {
			out=new	ObjectOutputStream(new FileOutputStream("./src/undo.ser"));
			out.writeObject(this);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return root;
	}
	
	
	/**
     * Creates the interface for the load page along with GUI.
     * Used for loading previous progress.
     * 
     * @return Pane This returns the state of the load page.
     */
	private Parent createLoad(){
		loadMenu.getChildren().clear();
		loadMenu.setStyle("-fx-background-color: #000000;");
		stage.setHeight(600);
		stage.setWidth(600);
		Button back = new Button("BACK");
		back.setTranslateX(20);
		back.setTranslateY(20);
		loadMenu.getChildren().add(back);
		back.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createMenu());
        });
		File z=new File("./Load");
		File[] lof=z.listFiles();
		Button[] choose=new Button[10];
		if(lof.length==0)
		{
			Text N = new Text();
			N.setFill(Color.GRAY);
			N.setText("NO PREVIOUS SAVED PROGRESS");
			N.setFont(Font.font(null, FontWeight.BOLD, 25));
			N.setTranslateX(100);
			N.setTranslateY(110);
			loadMenu.getChildren().add(N);
		}
		else
		{
			Text M = new Text();
			M.setFill(Color.GRAY);
			M.setText("CHOOSE PREVIOUS SAVED PROGRESS:-");
			M.setFont(Font.font(null, FontWeight.BOLD, 15));
			M.setTranslateX(100);
			M.setTranslateY(100);
			loadMenu.getChildren().add(M);
		for(int j=0; j<lof.length; j++)
		{			
			Text N = new Text();
			N.setFill(Color.WHITE);
			N.setText(lof[j].getName());
			N.setTranslateX(100);
			N.setTranslateY(50*j+140);
			loadMenu.getChildren().add(N);
		
			
			choose[j] = new Button("Choose");
			choose[j].setTranslateX(20);
			choose[j].setTranslateY(50*j+120);
			loadMenu.getChildren().add(choose[j]);
			//index=j;
			choose[j].setOnMouseClicked(event -> {
				System.out.println(event.getSceneY());
				int n=((int)event.getSceneY()-100)/50;
				ObjectInputStream in=null;	
				try {
					in=new ObjectInputStream(new FileInputStream("./Load/"+lof[n].getName()));
					Game x=(Game)in.readObject();
					this.settings=x.settings;
					this.grid=x.grid;
					loadFlag=true;
					in.close();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	        	this.stage.getScene().setRoot(createContent());
	        });
		}
		}
		/*ObjectInputStream in=null;	
		try {
			in=new ObjectInputStream(new FileInputStream("./Load/name.ser"));
			Game x=(Game)in.readObject();
			this.settings=x.settings;
			this.grid=x.grid;
			loadFlag=true;
			in.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		Button d = new Button("B");
		d.setTranslateX(100);
		d.setTranslateY(20);
		loadMenu.getChildren().add(d);
		d.setOnMouseClicked(event -> {
        	this.stage.getScene().setRoot(createContent());
        });*/
		return loadMenu;
	}
	
	/**
     * Creates the interface for the save page along with GUI.
     * Used for saving the game progress.
     * 
     * @return Pane This returns the state of the save page.
     */
	private Parent createSave(){
		stage.setHeight(650);
		stage.setWidth(440);
		saveMenu.getChildren().clear();
		saveMenu.setStyle("-fx-background-color: #000000;");
		
		Button back = new Button("BACK");
		back.setTranslateX(20);
		back.setTranslateY(20);
		saveMenu.getChildren().add(back);
		back.setOnMouseClicked(event -> {
			stage.setHeight(grid.m * 50 + 200);
			stage.setWidth(grid.n * 50 + 140);
        	this.stage.getScene().setRoot(root);
        });
		
		Text text1=new Text("Enter Save Progress Name:");
		text1.setFill(Color.WHITE);
		TextField f1=new TextField();
		Button submit=new Button("Submit");
		submit.setOnAction(event -> {
			name=f1.getText();
			//this.stage.getScene().setRoot(root);
			ObjectOutputStream out=null;
			try{
				try {
					out=new	ObjectOutputStream(new FileOutputStream("./Load/"+name+".ser"));
					out.writeObject(this);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			finally
			{
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			stage.setHeight(grid.m * 50 + 200);
			stage.setWidth(grid.n * 50 + 140);
			this.stage.getScene().setRoot(root);
		});
		
		
		text1.setTranslateX(20);
		text1.setTranslateY(100);
		f1.setTranslateX(220);
		f1.setTranslateY(82);
		submit.setTranslateX(20);
		submit.setTranslateY(150);
		
		
		saveMenu.getChildren().add(text1);
		saveMenu.getChildren().add(f1);
		saveMenu.getChildren().add(submit);
		
		
		/*File x=new File("./src/Load");
		File[] lof=x.listFiles();
		for(int i=0; i<lof.length; i++)
		{
			if((lof[i].getName().length()>4))
				System.out.println(lof[i].getName());
			
		}*/
		return saveMenu;
	}	
	
	@Override
	public void start(Stage primaryStage) {
//		System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
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
