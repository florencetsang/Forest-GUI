import java.util.Random;
/**
 * The abstract superclass of Dog, Fox and Wolf.
 * @author 	Florence
 */
abstract public class Canine extends Animal {
	private Random rand = new Random();
	private Helper helper = new Helper();
	/*
	 * (non-Javadoc)
	 * @see Animal#moveMethod()
	 */
	public int[] moveMethod(){
		int[][] allPos = helper.caninePos(x,y);
		int moveMod = rand.nextInt(allPos.length);
		return allPos[moveMod];
	}
	/*
	 * (non-Javadoc)
	 * @see Animal#isWin(Animal)
	 */
	public boolean isWin(Animal a){
		if(a instanceof Feline){
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
		}else{
			return super.isWin(a);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see Animal#allPos()
	 */
	public int[][] allPos(){
		return helper.caninePos(x, y);
	}
}
