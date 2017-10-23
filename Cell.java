import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Cell extends StackPane {
	int x;
	int y;
	Pane pane = new Pane();
	int numberOfOrbs = 0;
	int maxOrbs = 0;
	static Grid grid;
	Player currentPlayer = grid.settings.players.get(0);
	
    public Cell() {
        Rectangle border = new Rectangle(100, 100);
        border.setFill(null);
        border.setStroke(Color.CYAN);
        setAlignment(Pos.CENTER);
        getChildren().addAll(border, pane);
        
        setOnMouseClicked(event -> {

    		Circle circle1 = new Circle();
        	circle1.setFill(grid.settings.colourValues[grid.settings.colours.indexOf(currentPlayer.colour)]);
        	circle1.setRadius(10);
        	
        	if (numberOfOrbs == maxOrbs - 1) {
        		numberOfOrbs = 0;
        		if (maxOrbs == 2) {
            		pane.getChildren().clear();
            		
            		if (x == 0 && y == 0) {
            			Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
            		} else if (x == 0 && y == grid.n - 1) {
            			Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
            		} else if (x == grid.m - 1 && y == 0) {
            			Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
            		} else if (x == grid.m - 1 && y == grid.n - 1) {
            			Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
            		}
            	}
        		
        		if (maxOrbs == 3) {
        			pane.getChildren().clear();
        			if (x == 0) {
            			Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
            		}
        			
        			else if (y == 0) {
                		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
            		}
        			
        			else if (x == grid.m - 1) {
            			Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
            		}
        			
        			else if (y == grid.n - 1) {
            			Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
            		}
        		}
        		
        		if (maxOrbs == 4) {
            		pane.getChildren().clear();
            		Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                            0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                            true, true, true, true, true, true, null));
            		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                            0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                            true, true, true, true, true, true, null));
            		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                            0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                            true, true, true, true, true, true, null));
            		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                            0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                            true, true, true, true, true, true, null));
            	}
        	}
        	
        	else if (numberOfOrbs == 0) {
        		circle1.setTranslateX(50);
                circle1.setTranslateY(50);
                
                pane.getChildren().clear();
            	pane.getChildren().add(circle1);    	
            	numberOfOrbs++; 
        	}
        	
        	else if (numberOfOrbs == 1) {
        		Circle circle2 = new Circle();
        		circle2.setFill(grid.settings.colourValues[grid.settings.colours.indexOf(currentPlayer.colour)]);
            	circle2.setRadius(10);
        		                                
        		circle1.setTranslateX(35);
                circle1.setTranslateY(50);
                circle2.setTranslateX(65);
                circle2.setTranslateY(50);
                pane.getChildren().clear();
                pane.getChildren().add(circle1);
                pane.getChildren().add(circle2);    	
            	numberOfOrbs++; 
        	}
        	
        	else {
        		Circle circle2 = new Circle();
        		circle2.setFill(grid.settings.colourValues[grid.settings.colours.indexOf(currentPlayer.colour)]);
            	circle2.setRadius(10);
            	Circle circle3 = new Circle();
        		circle3.setFill(grid.settings.colourValues[grid.settings.colours.indexOf(currentPlayer.colour)]);
            	circle3.setRadius(10);
        		                                
        		circle1.setTranslateX(35);
                circle1.setTranslateY(65);
                circle2.setTranslateX(65);
                circle2.setTranslateY(65);
                circle3.setTranslateX(50);
                circle3.setTranslateY(35);
                pane.getChildren().clear();
                pane.getChildren().add(circle1);
                pane.getChildren().add(circle2);
                pane.getChildren().add(circle3);    	
            	numberOfOrbs++; 
        	}
                   	
        });
    }
}