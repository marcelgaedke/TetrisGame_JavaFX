//import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.Timer;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
import javafx.stage.Stage;
import javafx.util.Duration;


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
	private static HashSet<XYPair> fixedObjects = new HashSet<>();
	private static Canvas gridCanvas = new Canvas(centerCanvasWidth,centerCanvasHeight );
	private static GraphicsContext gridCanvasGC = gridCanvas.getGraphicsContext2D();
	private static Canvas fixedObjectsCanvas = new Canvas(centerCanvasWidth, centerCanvasHeight);
	private static GraphicsContext fixedObjectsGC = fixedObjectsCanvas.getGraphicsContext2D();
	private static Canvas currentObjectCanvas = new Canvas(centerCanvasWidth,centerCanvasHeight);
	private static GraphicsContext currentObjectGC = currentObjectCanvas.getGraphicsContext2D();
	private static TetrisObject currentObject;
	private static int score=0;
	
	private static void displayCurrentObject() {
		currentObjectGC.clearRect(0, 0, centerCanvasWidth, centerCanvasHeight);
		for (XYPair xyPair : currentObject.getObjectCoordinates()) {
			currentObjectGC.setFill(Color.RED);
			currentObjectGC.fillRect(xyPair.getxCoordinate()*gridColumnWidth, xyPair.getyCoordinate()*gridRowHeight, gridColumnWidth, gridRowHeight);
		}
	}
	
	private static void displayFixedObjects(GraphicsContext gc) {
		fixedObjectsGC.clearRect(0, 0, centerCanvasWidth, centerCanvasHeight);
		for(XYPair xyPair:fixedObjects) {
			gc.setFill(Color.PURPLE);
			gc.fillRect(xyPair.getxCoordinate()*gridColumnWidth, xyPair.getyCoordinate()*gridRowHeight, gridColumnWidth, gridRowHeight);
		}
	}
	
	private static TetrisObject generateNewTetrisObject() {
		TetrisObject[] objectArray = new TetrisObject[4];
		HashSet<XYPair> set1 = new HashSet<>();
		set1.add(new XYPair(6, 0));
		set1.add(new XYPair(6, 1));
		set1.add(new XYPair(6, 2));
		set1.add(new XYPair(7, 2));
		objectArray[0] = new TetrisObject(set1);
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

		int r = new Random().nextInt(4);
		return objectArray[r];
	}
	
	//Method to check if object is at bottom
	private static boolean isBottom() {
		if(currentObject.getyMax()>=numOfRows-1) {
			return true;
		}
		for (XYPair xyPair : fixedObjects) {	//check if current Object is directly above the fixed objects
			for (XYPair xyPair2 : currentObject.getObjectCoordinates()) {
				if(xyPair.getxCoordinate()==xyPair2.getxCoordinate()) {
					if(xyPair2.getyCoordinate()+1>=xyPair.getyCoordinate()) {
						return true;
					}
				}
			}
		}	
		return false;
	}
	
	//This method is called when Object is at bottom
	private static void performIfObjectAtBottom() {
		currentObject.moveDown();
		displayCurrentObject();
		
		//if Object is at bottom then fix it and generate new current Object
		if(isBottom()) {		
			currentObject.setFixed();			
			fixedObjects.addAll(currentObject.getObjectCoordinates());
			//displayFixedObjects(fixedObjectsGC);
			TetrisObject newObject = generateNewTetrisObject();
			currentObject=newObject;
			displayCurrentObject();
			
			//Check if any rows are complete
			LinkedList<Integer> completeRowNos = new LinkedList<Integer>();
			for(int i =20;i>0;i--) {
				HashSet<Integer> rowSet =new HashSet<>();
				for (XYPair xyPair : fixedObjects) {
					if(xyPair.getyCoordinate()==i) {
						rowSet.add(xyPair.getxCoordinate());
					}
				}
				if(rowSet.size()==12) {	
					System.out.println("Row Nr. "+i+"is complete");
					score++;
					System.out.println("Score: "+score);
					completeRowNos.add(new Integer(i));
				}
				
			}
			
			//Remove complete rows
			if(!completeRowNos.isEmpty()) {
				
				//If rows are complete highlight them in yellow and then remove
				for (Integer integer : completeRowNos) {
					fixedObjectsGC.setFill(Color.YELLOW);
					fixedObjectsGC.fillRect(0, integer*gridRowHeight, centerCanvasWidth, gridRowHeight);
				}
				
				
				PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
				pauseTransition.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						// TODO Auto-generated method stub
						System.out.println("Pause over");
						removeRows(completeRowNos);
						displayFixedObjects(fixedObjectsGC);	
					}
				} );
				pauseTransition.play();
				
			}else {
				//redraw fixed objects
				displayFixedObjects(fixedObjectsGC);	
			}
			
			
			
			
		}	
	}
	
	private static void removeRows(LinkedList<Integer> rowNo) {

		HashSet<XYPair> newSet = new HashSet<>();
		for (XYPair xyPair : fixedObjects) {				//Delete all XYPairs from full rows
			if(!rowNo.contains(xyPair.getyCoordinate())) {
				newSet.add(xyPair);
			}
		}
		fixedObjects=newSet;
		//Remove empty rows (move upper rows down after complete rows get deleted)
		Collections.sort(rowNo);
		for (Integer integer : rowNo) {
			for (XYPair xyPair : fixedObjects) {
				if(xyPair.getyCoordinate()<integer) {
					xyPair.setyCoordinate(xyPair.getyCoordinate()+1);
				}
			}	
		}
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
			
			//Draw Grid Rows
			for(int i=0;i<=numOfRows;i++) {
				gridCanvasGC.strokeLine(0, i*gridRowHeight, centerCanvasWidth, i*gridRowHeight);
			}
			//Draw Grid Columns
			for(int j=0;j<=numOfColums;j++) {
				gridCanvasGC.strokeLine(j*gridColumnWidth, 0, j*gridColumnWidth, centerCanvasHeight);
			}
			centerPane.getChildren().add(gridCanvas);
			
			
			//Create fixed Objects
			fixedObjects.add(new XYPair(3, 18));
			fixedObjects.add(new XYPair(3, 19));
			fixedObjects.add(new XYPair(0, 19));
			fixedObjects.add(new XYPair(1, 19));
			fixedObjects.add(new XYPair(2, 19));
			fixedObjects.add(new XYPair(6, 19));
			fixedObjects.add(new XYPair(4, 19));
			fixedObjects.add(new XYPair(5, 19));
			fixedObjects.add(new XYPair(7, 19));
			fixedObjects.add(new XYPair(8, 19));
			fixedObjects.add(new XYPair(9, 19));
			fixedObjects.add(new XYPair(3, 18));
			fixedObjects.add(new XYPair(3, 18));
			fixedObjects.add(new XYPair(0, 18));
			fixedObjects.add(new XYPair(1, 18));
			fixedObjects.add(new XYPair(2, 18));
			fixedObjects.add(new XYPair(6, 18));
			fixedObjects.add(new XYPair(4, 18));
			fixedObjects.add(new XYPair(5, 18));
			fixedObjects.add(new XYPair(7, 18));
			fixedObjects.add(new XYPair(8, 18));
			fixedObjects.add(new XYPair(9, 18));
			displayFixedObjects(fixedObjectsGC);
			centerPane.getChildren().add(fixedObjectsCanvas);
			fixedObjectsCanvas.toBack();
			
			//Create current TetrisObject
			currentObject = generateNewTetrisObject();
			displayCurrentObject();
			centerPane.getChildren().add(currentObjectCanvas);
			currentObjectCanvas.toBack();
			root.setCenter(centerPane);
				
			//show Stage and Scene
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
						displayCurrentObject();
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
						displayCurrentObject();
					}
				}
			});
			
			
			//Eventhandler Down Button
			EventHandler<MouseEvent> eventHandlerBtnDown = new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {			
					performIfObjectAtBottom();
				}
			};
			btnDown.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandlerBtnDown);
			
			
			
			
			//Create Timer for down movement
			Timer timer = new Timer(500, new ActionListener() {
				
				

				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// TODO Auto-generated method stub
					performIfObjectAtBottom();
				}
			});
			timer.start();
			
			
			
			
		}
			 

		 
			 catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		System.exit(0);
	}

}
