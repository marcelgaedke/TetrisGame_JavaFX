import java.util.HashSet;

public class TetrisObject {

	private int xMin=100;
	private int xMax=0;
	private int yMin=100;
	private int yMax=0;
	private int objectWidth;
	private int objectHeight;
	private boolean isFixed=false;
	
	
	private HashSet<XYPair> objectCoordinates = new HashSet<>();
	
	public TetrisObject(HashSet<XYPair> set) {
		this.objectCoordinates = set;
		for (XYPair xyPair : objectCoordinates) {

			if (xMin > xyPair.getxCoordinate()) {
				xMin = xyPair.getxCoordinate();
			}
			if (xMax < xyPair.getxCoordinate()) {
				xMax = xyPair.getxCoordinate();
			}
			if (yMin > xyPair.getyCoordinate()) {
				yMin = xyPair.getyCoordinate();
			}
			if (yMax < xyPair.getyCoordinate()) {
				yMax = xyPair.getyCoordinate();
			}
			objectWidth = xMax - xMin;			//actual width is +1
			objectHeight = yMax - yMin;			//actual height is +1
		}
	}


	
	public void moveRight() {
		if(this.isFixed==false) {
			for (XYPair xyPair : objectCoordinates) {
				xyPair.setxCoordinate(xyPair.getxCoordinate()+1);
				
			}
			xMax++;
			xMin++;
		}	
	}
	
	public void moveLeft() {
		if(this.isFixed==false) {
			for (XYPair xyPair : objectCoordinates) {
				xyPair.setxCoordinate(xyPair.getxCoordinate()-1);
			}
			xMax--;
			xMin--;
		}	
	}
	
	public void moveDown() {
		if(this.isFixed==false) {
			for (XYPair xyPair : objectCoordinates) {
				xyPair.setyCoordinate(xyPair.getyCoordinate()+1);
			}
			yMin++;
			yMax++;
		}
	}
	
	public void rotateClockwise() {

		if(this.isFixed==false) {
			int xOrigin = this.getxMin();
			int yOrigin = this. getyMin();
			boolean[][] matrix = new boolean[4][4];		//initialize Matrix to all false
			boolean[][] newMatrix = new boolean[4][4];
			for(int i=0;i<4;i++) {
				for(int j=0;j<4;j++){
					matrix[i][j]=false;
					newMatrix[i][j]=false;
				}
			}
			
			for (XYPair xyPair : this.getObjectCoordinates()) {		//Create boolean Matrix for rotation
				int x = xyPair.getxCoordinate()-xOrigin;
				int y = xyPair.getyCoordinate()-yOrigin;
				matrix[x][y]=true;
			}
			
			//Rotate 4x4 Boolean Matrix
			
			if(this.objectHeight==1 && this.objectWidth==1) {	//IF Matrix is 2x2 block don't change
				newMatrix=matrix;
			}else if(this.objectHeight==3 && this.objectWidth==0) {	//If object is 1x4 line
				newMatrix[0][0]=true;
				newMatrix[1][0]=true;
				newMatrix[2][0]=true;
				newMatrix[3][0]=true;
				
			}else if(this.objectHeight==0 && this.objectWidth==3) {	//If object is 4x1 line
				newMatrix[0][0]=true;
				newMatrix[0][1]=true;
				newMatrix[0][2]=true;
				newMatrix[0][3]=true;
				
			}else{
				//Rotate outer Ring	
				for(int i = 0;i<4;i++) {								
							
					newMatrix[i][0]=matrix[0][3-i];		
					newMatrix[3][i]=matrix[i][0];		
					newMatrix[i][3]=matrix[3][3-i];	
					newMatrix[0][i]=matrix[i][3];
				}
				
				//Rotate inner Ring
				for(int i =1;i<3;i++) {
					newMatrix[i][1]=matrix[1][3-i];
					newMatrix[2][i]=matrix[i][1];
					newMatrix[i][2]=matrix[2][3-i];
					newMatrix[1][i]=matrix[i][2];
				}
			}
			
			
			//Create new HashSet with new Coordinates after Rotation
			HashSet<XYPair> newSet = new HashSet<XYPair>();
			for (int x=0;x<4;x++) {		
				for (int y=0;y<4;y++) {
					if(newMatrix[x][y]==true) {
						newSet.add(new XYPair(x+xOrigin, y+yOrigin));
					}
				}
				
			}
					
			//adjust offset of Rotation of L-Shaped objects
			if(this.objectHeight==2 && this.objectWidth==1) {
				for (XYPair xyPair : newSet) {
					xyPair.setxCoordinate(xyPair.getxCoordinate()-1);
				}
			}
			
			if(this.objectHeight==1 && this.objectWidth==2) {
				for (XYPair xyPair : newSet) {
					xyPair.setxCoordinate(xyPair.getxCoordinate()-2);
				}
			}	
			
			this.objectCoordinates=newSet;	
			
			//Recalculate width, height and max x,y and min values
			xMin=100;
			xMax=0;
			yMin=100;
			yMax=0;
			
			for (XYPair xyPair : this.objectCoordinates) {

				if (xMin > xyPair.getxCoordinate()) {
					xMin = xyPair.getxCoordinate();
				}
				if (xMax < xyPair.getxCoordinate()) {
					xMax = xyPair.getxCoordinate();
				}
				if (yMin > xyPair.getyCoordinate()) {
					yMin = xyPair.getyCoordinate();
				}
				if (yMax < xyPair.getyCoordinate()) {
					yMax = xyPair.getyCoordinate();
				}
				objectWidth = xMax - xMin;			//actual width is +1
				objectHeight = yMax - yMin;			//actual height is +1
			}
		}
	
	}
	
	public void rotateCounterClockwise() {
		if(this.isFixed==false) {
			int xOrigin = this.getxMin();
			int yOrigin = this. getyMin();
			boolean[][] matrix = new boolean[4][4];		//initialize Matrix to all false
			boolean[][] newMatrix = new boolean[4][4];
			for(int i=0;i<4;i++) {
				for(int j=0;j<4;j++){
					matrix[i][j]=false;
					newMatrix[i][j]=false;
				}
			}
			for (XYPair xyPair : this.getObjectCoordinates()) {		//Create boolean Matrix for rotation
				int x = xyPair.getxCoordinate()-xOrigin;
				int y = xyPair.getyCoordinate()-yOrigin;
				matrix[x][y]=true;
			}
			
			//Rotate 4x4 Boolean Matrix
			
			//Rotate outer Ring	
			if(this.objectHeight==1 && this.objectWidth==1) {	//If object is 2x2 block leave unchanged
				newMatrix=matrix;
			}else if(this.objectHeight==3 && this.objectWidth==0){
				newMatrix[0][0]=true;
				newMatrix[1][0]=true;
				newMatrix[2][0]=true;
				newMatrix[3][0]=true;
			}else if(this.objectHeight==0 && this.objectWidth==3) {
				newMatrix[0][0]=true;
				newMatrix[0][1]=true;
				newMatrix[0][2]=true;
				newMatrix[0][3]=true;
			}
			else {
				
				for(int i = 0;i<4;i++) {								
					newMatrix[0][3-i]=matrix[i][0];		//Step 1 move top row to left column
					newMatrix[i][0]=matrix[3][i];		//Step 2 move last column to top row
					newMatrix[3][3-i]=matrix[i][3];		//Step 3 move bottom row to last column
					newMatrix[i][3]=matrix[0][i];		//Step 4 move first column to bottom row
				}
				
				//Rotate inner Ring
				for(int i =1;i<3;i++) {
					newMatrix[1][3-i]=matrix[i][1];		//Step 1 move top row to left column
					newMatrix[i][1]=matrix[2][i];		//Step 2 move last column to top row
					newMatrix[2][3-i]=matrix[i][2];		//Step 3 move bottom row to last column
					newMatrix[i][2]=matrix[1][i];		//Step 4 move first column to bottom row
				}
			}
			
			
			
			//Create new HashSet with new Coordinates after Rotation
			HashSet<XYPair> newSet = new HashSet<XYPair>();
			for (int x=0;x<4;x++) {		
				for (int y=0;y<4;y++) {
					if(newMatrix[x][y]==true) {
						newSet.add(new XYPair(x+xOrigin, y+yOrigin));
					}
				}
			}
			
			//adjust offset of Rotation of L-Shaped objects
			if(this.objectHeight==2 && this.objectWidth==1) {
				for (XYPair xyPair : newSet) {
					xyPair.setyCoordinate(xyPair.getyCoordinate()-1);
				}
			}
			
			if(this.objectHeight==1 && this.objectWidth==2) {
				for (XYPair xyPair : newSet) {
					xyPair.setyCoordinate(xyPair.getyCoordinate()-1);
				}
			}
			
			this.objectCoordinates=newSet;	
			
			//Recalculate width, height and max x,y and min values
			xMin=100;
			xMax=0;
			yMin=100;
			yMax=0;
			
			for (XYPair xyPair : this.objectCoordinates) {

				if (xMin > xyPair.getxCoordinate()) {
					xMin = xyPair.getxCoordinate();
				}
				if (xMax < xyPair.getxCoordinate()) {
					xMax = xyPair.getxCoordinate();
				}
				if (yMin > xyPair.getyCoordinate()) {
					yMin = xyPair.getyCoordinate();
				}
				if (yMax < xyPair.getyCoordinate()) {
					yMax = xyPair.getyCoordinate();
				}
				objectWidth = xMax - xMin;			//actual width is +1
				objectHeight = yMax - yMin;			//actual height is +1
			}
			
			
		}
	}


	public int getxMin() {
		return xMin;
	}



	public int getxMax() {
		return xMax;
	}



	public int getyMin() {
		return yMin;
	}



	public int getyMax() {
		return yMax;
	}



	public int getObjectWidth() {
		return objectWidth;
	}



	public int getObjectHeight() {
		return objectHeight;
	}



	public HashSet<XYPair> getObjectCoordinates() {
		return objectCoordinates;
	}

	public void setFixed() {
		this.isFixed=true;
	}
}
