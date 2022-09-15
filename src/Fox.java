import javax.swing.ImageIcon;

/**
 * The class for the animal fox.
 * @author 	Florence
 */
public class Fox extends Canine {
	/**
	 * Class constructor
	 */
	public Fox() {
		this.name = "Fox";
		this.image = new ImageIcon("Fox.png").getImage();
	}
	/*
	 * (non-Javadoc)
	 * @see Canine#isWin(Animal)
	 */
	public boolean isWin(Animal a){
		if(a instanceof Cat){
			return true;
		}else{
			return super.isWin(a);			
		}
	}
}
