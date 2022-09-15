import java.util.Random;
import javax.swing.ImageIcon;
/**
 * The class for the animal turtle.
 * @author 	Florence
 */
public class Turtle extends Animal {
	private Random rand = new Random();
	private Helper helper = new Helper();
	/**
	 * Class constructor
	 */
	public Turtle() {
		this.name = "Turtle";
		this.image = new ImageIcon("Turtle.png").getImage();
	}
	/*
	 * (non-Javadoc)
	 * @see Animal#moveMethod()
	 */
	public int[] moveMethod(){		
		int moveMod = rand.nextInt(2);
		int [] currentPos = {x,y};
		switch (moveMod) {
		case 0:
			int[][] allPos = helper.hippoPos(x,y);
			int movingMod = rand.nextInt(allPos.length);
			return allPos[movingMod];
		case 1:
			return currentPos;
		default:
			System.out.println("Err!");
			return currentPos;
		}		
	}	
	/*
	 * (non-Javadoc)
	 * @see Animal#isWin(Animal)
	 */
	public boolean isWin(Animal a){
		int win = rand.nextInt(2);
		switch(win){
		case 0:
			return true;
		case 1:
			return false;
		default:
			System.out.println("Error!");
			return true;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see Animal#allPos()
	 */
	public int[][] allPos(){
		return helper.hippoPos(x, y);
	}
}
