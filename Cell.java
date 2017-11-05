import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Cell extends StackPane {
	int x;
	int y;
	Pane pane = new Pane();
	int numberOfOrbs = 0;
	int maxOrbs = 0;
	static Grid grid;
	int currentPlayer = -1;
	
    public Cell(int x, int y) {
    	this.x = x;
    	this.y = y;
        Rectangle border = new Rectangle(50, 50);
        border.setFill(null);
        border.setStroke(grid.settings.colourValues[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
        setAlignment(Pos.CENTER);
        getChildren().addAll(border, pane);
        grid.rectangles[x][y] = border;
        grid.panes[x][y] = pane;
        
        setOnMouseClicked(event -> {
        	if (grid.gameEnd) {
        		return;
        	}
//        	System.out.println(grid.currentPlayer + " " + currentPlayer);
        	if (grid.currentPlayer == grid.settings.noOfPlayers - 1) {
        		grid.firstTurn = true;
        	}
        	if (currentPlayer == -1 || currentPlayer == grid.currentPlayer) {
        		if (currentPlayer == -1) {
        			grid.noOfCellsOfPlayer[grid.currentPlayer]++;
        		}
        		currentPlayer = grid.currentPlayer;
        		Circle circle1 = new Circle();
            	circle1.setFill(grid.settings.colourValues[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
            	circle1.setRadius(5);
            	
            	if (numberOfOrbs == maxOrbs - 1) {
            		numberOfOrbs = 0;
            		if (maxOrbs == 2) {
                		pane.getChildren().clear();
                		
                		if (x == 0 && y == 0) {
                			if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                			Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                        	
                			if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                		} else if (x == 0 && y == grid.n - 1) {
                			if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                			Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                		} else if (x == grid.m - 1 && y == 0) {
                			if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                			Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                		} else if (x == grid.m - 1 && y == grid.n - 1) {
                			if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                			Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                		}
                	}
            		
            		else if (maxOrbs == 3) {
            			pane.getChildren().clear();
            			
            			if (x == 0) {
            				if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
            				grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                			Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                    		grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                		}
            			
            			else if (y == 0) {
            				if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
            				grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                    		grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                    		grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                		}
            			
            			else if (x == grid.m - 1) {
            				if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
            				grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                			Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                    		grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                		}
            			
            			else if (y == grid.n - 1) {
            				if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
                				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
            				grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                			Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                			grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}

                        	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
                				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                    				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
                					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                				}
                			}
                			else {
                				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
                			}
                    		grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                    		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                    0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                    true, true, true, true, true, true, null));
                        	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                        		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                        	}
                		}
            		}
            		
            		else if (maxOrbs == 4) {
                		pane.getChildren().clear();

                		if (grid.cells[this.x][this.y - 1].currentPlayer != -1) {
            				if (grid.cells[this.x][this.y - 1].currentPlayer != currentPlayer) {
                				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y - 1].currentPlayer]--;
            					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
            				}
            			}
            			else {
            				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
            			}
                		grid.cells[this.x][this.y - 1].currentPlayer = currentPlayer;
                		Event.fireEvent(grid.cells[this.x][this.y - 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                    	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                    	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                    		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                    	}

                    	if (grid.cells[this.x - 1][this.y].currentPlayer != -1) {
            				if (grid.cells[this.x - 1][this.y].currentPlayer != currentPlayer) {
                				grid.noOfCellsOfPlayer[grid.cells[this.x - 1][this.y].currentPlayer]--;
            					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
            				}
            			}
            			else {
            				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
            			}
                		grid.cells[this.x - 1][this.y].currentPlayer = currentPlayer;
                		Event.fireEvent(grid.cells[this.x - 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                    	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                    	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                    		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                    	}

                    	if (grid.cells[this.x + 1][this.y].currentPlayer != -1) {
            				if (grid.cells[this.x + 1][this.y].currentPlayer != currentPlayer) {
                				grid.noOfCellsOfPlayer[grid.cells[this.x + 1][this.y].currentPlayer]--;
            					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
            				}
            			}
            			else {
            				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
            			}
                		grid.cells[this.x + 1][this.y].currentPlayer = currentPlayer;
                		Event.fireEvent(grid.cells[this.x + 1][this.y], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                    	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                    	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                    		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                    	}

                    	if (grid.cells[this.x][this.y + 1].currentPlayer != -1) {
            				if (grid.cells[this.x][this.y + 1].currentPlayer != currentPlayer) {
                				grid.noOfCellsOfPlayer[grid.cells[this.x][this.y + 1].currentPlayer]--;
            					grid.noOfCellsOfPlayer[grid.currentPlayer]++;
            				}
            			}
            			else {
            				grid.noOfCellsOfPlayer[grid.currentPlayer]++;
            			}
                		grid.cells[this.x][this.y + 1].currentPlayer = currentPlayer;
                		Event.fireEvent(grid.cells[this.x][this.y + 1], new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
                                0, 0, 0, MouseButton.PRIMARY, 1, true, true, true, true,
                                true, true, true, true, true, true, null));
                    	grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                    	while (grid.eliminatedPlayer[grid.currentPlayer]) {
                    		grid.currentPlayer = (grid.currentPlayer + grid.settings.noOfPlayers - 1) % grid.settings.noOfPlayers;
                    	}
                	}
            		currentPlayer = -1;
            		grid.noOfCellsOfPlayer[grid.currentPlayer]--;
            	}
            	
            	else if (numberOfOrbs == 0) {
            		circle1.setTranslateX(25);
                    circle1.setTranslateY(25);
                    
                    pane.getChildren().clear();
                	pane.getChildren().add(circle1);    	
                	numberOfOrbs++; 
            	}
            	
            	else if (numberOfOrbs == 1) {
            		Circle circle2 = new Circle();
            		circle2.setFill(grid.settings.colourValues[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	circle2.setRadius(5);
            		                                
            		circle1.setTranslateX(17.5);
                    circle1.setTranslateY(25);
                    circle2.setTranslateX(32.5);
                    circle2.setTranslateY(25);
                    pane.getChildren().clear();
                    pane.getChildren().add(circle1);
                    pane.getChildren().add(circle2);    	
                	numberOfOrbs++; 
            	}
            	
            	else {
            		Circle circle2 = new Circle();
            		circle2.setFill(grid.settings.colourValues[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	circle2.setRadius(5);
                	Circle circle3 = new Circle();
            		circle3.setFill(grid.settings.colourValues[grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour)]);
                	circle3.setRadius(5);
            		                                
            		circle1.setTranslateX(17.5);
                    circle1.setTranslateY(32.5);
                    circle2.setTranslateX(32.5);
                    circle2.setTranslateY(32.5);
                    circle3.setTranslateX(25);
                    circle3.setTranslateY(17.5);
                    pane.getChildren().clear();
                    pane.getChildren().add(circle1);
                    pane.getChildren().add(circle2);
                    pane.getChildren().add(circle3);    	
                	numberOfOrbs++; 
            	}
            	
            	if (grid.firstTurn) {
            		int cnt = 0;
                	int k = -1;
                	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
                		if (grid.noOfCellsOfPlayer[i] != 0) {
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
            			message.setTranslateX(100);
            			message.setTranslateY(80);
            			message.setStroke(grid.settings.colourValues[3]);
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
//            		int cnt = 0;
//            		for (int l = 0; l < grid.settings.noOfPlayers; l++) {
//            			if (!grid.eliminatedPlayer[l]) {
//            				cnt++;
//            			}
//            		}
//            		if (cnt == 0) {
//            			System.out.println("end");
//            			break;
//            		}
            		grid.currentPlayer = (grid.currentPlayer + 1) % grid.settings.noOfPlayers;
            	}
            	
            	for (int i = 0; i < grid.m; i++) {
            		for (int j = 0; j < grid.n; j++) {
            			int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
            			grid.rectangles[i][j].setStroke(grid.settings.colourValues[ind]);
            		}
            	}
            	
//            	int ind = grid.settings.colours.indexOf(grid.settings.players.get(grid.currentPlayer).colour);
//    			grid.rectangles[i][j].setStroke(grid.settings.colourValues[ind]);
            	
//            	for (int i = 0; i < grid.settings.noOfPlayers; i++) {
//            		System.out.println(grid.noOfCellsOfPlayer[i]);
//            	}
//            	System.out.println();
        	}
        });
    }
}