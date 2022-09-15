import javax.swing.ImageIcon;

/**
 * The class for the animal dog.
 * @author 	Florence
 */
public class Dog extends Canine {
	/**
	 * Class constructor
	 */
	public Dog() {
		this.name = "Dog";
		this.image = new ImageIcon("Dog.png").getImage();
	}
}
