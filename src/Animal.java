import java.util.Random;
import javax.swing.*;
import java.awt.*;
/**
 * The abstract base class of all animals
 * @author 	Florence
 */
abstract public class Animal {
	private Random rand = new Random();
	public int x;
	public int y;	
	public String name;
	public boolean isAlive = true;
	public Image image;
	/**
	 * This setLocation method sets the x y location of the animal.
	 * @param x		x-coordinates of the location
	 * @param y		y-coordinates of the location
	 */
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	/**
	 * This moveMethod implements the way of movement of the animal.
	 * @return 	a array of size 2 storing the new location
	 */
	public abstract int[] moveMethod();
	/**
	 * This allPos method method returns all possible moving positions of an animal
	 * @return an array of storing all possible locations
	 */
	public abstract int[][] allPos();
	/**
	 * This isWin method determines if it wins another animal a
	 * @param a		the animal of which we want to know whether we will win it or lose it
	 * @return		true of we win animal a; false if animal a wins
	 */
	public boolean isWin(Animal a){
		if(a instanceof Turtle){
			int win = rand.nextInt(10);
			if(win <= 2){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}