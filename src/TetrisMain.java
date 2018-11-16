import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.net.ssl.ExtendedSSLSession;

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class TetrisMain extends Application {
	
	private static int windowWidth = 600;
	private static int windowHeight = 600;
	private static int topPaneHeight = 50;
	private static int leftPaneWidth = 50;
	private static int rightPaneWidth = 50;
	private static int bottomPaneHeight = 50;
	private static int numOfRows = 20;
	private static int numOfColums = 12;
	private static int centerCanvasWidth = windowWidth-leftPaneWidth-rightPaneWidth;
	private static int centerCanvasHeight = windowHeight-topPaneHeight-bottomPaneHeight;
	private static double gridRowHeight = centerCanvasHeight/numOfRows;
	private static double gridColumnWidth = (double)centerCanvasWidth/numOfColums;
	private static TetrisObject fixedObjects = new TetrisObject(new HashSet<XYPair>());

	private static void displayOnGrid(TetrisObject t, GraphicsContext gc) {
		gc.clearRect(0, 0, centerCanvasWidth, centerCanvasHeight);
		for (XYPair xyPair : t.getObjectCoordinates()) {
			gc.setFill(Color.RED);
			gc.fillRect(xyPair.getxCoordinate()*gridColumnWidth, xyPair.getyCoordinate()*gridRowHeight, gridColumnWidth, gridRowHeight);
		}
	}
	
	private static boolean isBottom(TetrisObject currentObject) {
		if(fixedObjects.getObjectCoordinates().isEmpty()) {
			if(currentObject.getyMax()==numOfRows-1) {
				System.out.println("Bottom");
				return true;
			}
		}
		
		return false;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		try {

			BorderPane root = new BorderPane();
			
			//Create surrounding panes
			BackgroundFill bf = new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY);
			Background bg = new Background(bf);
			
			// Create Buttons at top
			HBox topButtonBox = new HBox();
			topButtonBox.setPrefHeight(topPaneHeight);
			Button btnDown = new Button("Down");
			Button btnLeft = new Button("Left");
			Button btnRight = new Button("Right");
			topButtonBox.getChildren().add(btnLeft);
			topButtonBox.getChildren().add(btnDown);
			topButtonBox.getChildren().add(btnRight);
			root.setTop(topButtonBox);
			
			//Create left Pane
			Pane leftPane = new Pane();
			leftPane.setPrefWidth(leftPaneWidth);
			leftPane.setBackground(bg);
			root.setLeft(leftPane);
			
			//Create right pane
			Pane rightPane = new Pane();
			rightPane.setPrefWidth(rightPaneWidth);
			rightPane.setBackground(bg);
			root.setRight(rightPane);
			
			//Create bottom Pane
			Pane bottomPane = new Pane();
			bottomPane.setPrefHeight(bottomPaneHeight);
			bottomPane.setBackground(bg);
			root.setBottom(bottomPane);
			
			
			//Create Center Pane
			Pane centerPane = new Pane();
			
			//Create Grid
			Canvas gridCanvas = new Canvas(centerCanvasWidth,centerCanvasHeight );
			GraphicsContext gc = gridCanvas.getGraphicsContext2D();
			//Draw Grid Rows
			for(int i=0;i<=numOfRows;i++) {
				gc.strokeLine(0, i*gridRowHeight, centerCanvasWidth, i*gridRowHeight);
			}
			//Draw Grid Columns
			for(int j=0;j<=numOfColums;j++) {
				gc.strokeLine(j*gridColumnWidth, 0, j*gridColumnWidth, centerCanvasHeight);
			}
			centerPane.getChildren().add(gridCanvas);
			
			
			
			//Create current TetrisObject
			Canvas currentObjectCanvas = new Canvas(centerCanvasWidth,centerCanvasHeight);
			GraphicsContext currentObjectGC = currentObjectCanvas.getGraphicsContext2D();
			HashSet<XYPair> set = new HashSet<>();
			set.add(new XYPair(6, 0));
			set.add(new XYPair(6, 1));
			set.add(new XYPair(6, 2));
			set.add(new XYPair(7, 2));
			TetrisObject[] objectArray = new TetrisObject[4];
			objectArray[0] = new TetrisObject(set);
			HashSet<XYPair> set2 = new HashSet<>();
			set2.add(new XYPair(6, 0));
			set2.add(new XYPair(6, 1));
			set2.add(new XYPair(6, 2));
			set2.add(new XYPair(6, 3));
			objectArray[1] = new TetrisObject(set2);
			HashSet<XYPair> set3 = new HashSet<>();
			set3.add(new XYPair(6, 0));
			set3.add(new XYPair(6, 1));
			set3.add(new XYPair(7, 0));
			set3.add(new XYPair(7, 1));
			objectArray[2] = new TetrisObject(set3);
			HashSet<XYPair> set4 = new HashSet<>();
			set4.add(new XYPair(6, 0));
			set4.add(new XYPair(6, 1));
			set4.add(new XYPair(6, 2));
			set4.add(new XYPair(5, 2));
			objectArray[3] = new TetrisObject(set4);
			
			TetrisObject currentObject = objectArray[1];
			displayOnGrid(currentObject,currentObjectGC);
			centerPane.getChildren().add(currentObjectCanvas);
			currentObjectCanvas.toBack();
			root.setCenter(centerPane);
			
			
			
			
			
			
			
			

			Stage stage = new Stage();
			Scene scene = new Scene(root, windowWidth, windowHeight);
			stage.setScene(scene);
			stage.show();
			stage.setTitle("Tetris");

			
			
			//Event Handlers Left Button
			btnLeft.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					if(currentObject.getxMin()>0) {
						currentObject.moveLeft();
						displayOnGrid(currentObject, currentObjectGC);
					}
					
				}
			});
			
			
			//Event Handler Right Button
			btnRight.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					if(currentObject.getxMax()<numOfColums-1)
					{
						currentObject.moveRight();
						displayOnGrid(currentObject, currentObjectGC);
					}
				}
			});
			
			
			//Eventhandler Down Button
			EventHandler<MouseEvent> eventHandlerBtnDown = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					if(currentObject.getyMax()<numOfRows-1)
					{
						currentObject.moveDown();
						displayOnGrid(currentObject, currentObjectGC);
					}
					if(isBottom(currentObject)) {		//If Object is at the bottom, make it fixed
						currentObject.setFixed();
						HashSet<XYPair> set = fixedObjects.getObjectCoordinates();
						set.addAll(currentObject.getObjectCoordinates());
						fixedObjects = new TetrisObject(set);
						TetrisObject newObject = new TetrisObject(set4);
						
						displayOnGrid(newObject, currentObjectGC);
					}

					
				}
			};
			btnDown.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerBtnDown);
			
			
			
			
		}
			 

		 
			 catch (Exception e) {
			// TODO: handle exception
		}

	}

}
