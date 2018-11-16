import java.util.HashSet;

public class TetrisObject {

	private int xMin=20;
	private int xMax=0;
	private int yMin=20;
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

	public void generateNew() {
		
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
