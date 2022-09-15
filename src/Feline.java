import java.util.Random;
/**
 * The abstract superclass of Cat, Tiger and Lion.
 * @author 	Florence
 */
abstract public class Feline extends Animal {
	private Random rand = new Random();
	private Helper helper = new Helper();
	/*
	 * (non-Javadoc)
	 * @see Animal#moveMethod()
	 */
	public int[] moveMethod(){
		//return helper.felineMove(x, y);
		int[][] allPos = helper.felinePos(x,y);
		int moveMod = rand.nextInt(allPos.length);
		return allPos[moveMod];
	}
	/*
	 * (non-Javadoc)
	 * @see Animal#isWin(Animal)
	 */
	public boolean isWin(Animal a){
		if(a instanceof Canine){
			return true;
		}else{
			return super.isWin(a);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see Animal#allPos()
	 */
	public int[][] allPos(){
		return helper.felinePos(x, y);
	}
}

