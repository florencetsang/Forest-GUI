import java.util.Random;

import java.util.Random;
import javax.swing.ImageIcon;

/**
 * The class for the animal hippo.
 * @author 	Florence
 */
public class Hippo extends Animal {
	private Random rand = new Random();
	private Helper helper = new Helper();
	/**
	 * Class constructor
	 */
	public Hippo() {
		this.name = "Hippo";
		this.image = new ImageIcon("Hippo.png").getImage();
	}
	/*
	 * (non-Javadoc)
	 * @see Animal#moveMethod()
	 */
	public int[] moveMethod(){
		//return helper.hippoMove(x, y);
		int[][] allPos = helper.hippoPos(x,y);
		int moveMod = rand.nextInt(allPos.length);
		return allPos[moveMod];
	}
	/*
	 * (non-Javadoc)
	 * @see Animal#allPos()
	 */
	public int[][] allPos(){
		return helper.hippoPos(x, y);
	}
}
