import java.awt.Image;
import java.util.Random;
/**
 * This Helper class implements various methods that is needed in the forest simulation application.
 * @author 	Florence
 */
public class Helper {
	private Random rand = new Random();
	/**
	 * This outOfBoard method determines if a location is out of the 12 * 12 board.
	 * @param x		x coordiante of the location being determined
	 * @param y 	y coordiante of the location being determined
	 * @return		true if the location is out of board; false otherwise
	 */
	public boolean outOfBoard(int x, int y){
		if(x < 0 || x >= 15 || y < 0 || y >= 15){
			return true;
		}else{
			return false;
		}
	}	
	/**
	 * This felinePos method returns all possible moving positions of a feline
	 * @param x		x coordinate of the original location
	 * @param y		y coordinate of the original location
	 * @return		an array of storing all possible locations
	 */
	public int[][] felinePos(int x, int y){
		int[] f1 = {x, y+1};
		int[] f2 = {x+1, y+1};
		int[] f3 = {x+1, y};
		int[] f4 = {x+1, y-1};
		int[] f5 = {x, y-1};
		int[] f6 = {x-1, y-1};
		int[] f7 = {x-1, y};
		int[] f8 = {x-1, y+1};
		int[][] allPos = {f1,f2,f3,f4,f5,f6,f7,f8};
		return allPos;		
	}	
	/**
	 * This caninePos method returns all possible moving positions of a canine
	 * @param x		x coordinate of the original location
	 * @param y		y coordinate of the original location
	 * @return		an array of storing all possible locations
	 */
	public int[][] caninePos(int x, int y){
		int[] f1 = {x, y+1};
		int[] f2 = {x, y+2};
		int[] f3 = {x+1, y};
		int[] f4 = {x+2, y};
		int[] f5 = {x, y-1};
		int[] f6 = {x, y-2};
		int[] f7 = {x-1, y};
		int[] f8 = {x-2, y};
		int[][] allPos = {f1,f2,f3,f4,f5,f6,f7,f8};
		return allPos;		
	}
	/**
	 * This hippoPos method returns all possible moving positions of a hippo
	 * @param x		x coordinate of the original location
	 * @param y		y coordinate of the original location
	 * @return		an array of storing all possible locations
	 */
	public int[][] hippoPos(int x, int y){
		int[] f1 = {x, y+1};
		int[] f2 = {x+1, y};
		int[] f3 = {x, y-1};
		int[] f4 = {x-1, y};
		int[][] allPos = {f1,f2,f3,f4};
		return allPos;		
	}
}