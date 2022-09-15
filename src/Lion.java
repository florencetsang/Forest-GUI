import javax.swing.ImageIcon;

/**
 * The class for the animal lion.
 * @author 	Florence
 */
public class Lion extends Feline{
	/**
	 * Class constructor
	 */
	public Lion() {
		this.name = "Lion";
		this.image = new ImageIcon("Lion.png").getImage();
	}
	/*
	 * (non-Javadoc)
	 * @see Feline#isWin(Animal)
	 */
	public boolean isWin(Animal a){
		if(a instanceof Hippo){
			return true;
		}else{
			return super.isWin(a);
		}
	}
}
