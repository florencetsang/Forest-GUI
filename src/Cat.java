import javax.swing.ImageIcon;

/**
 * The class for the animal cat.
 * @author 	Florence
 */
public class Cat extends Feline{
	/**
	 * Class constructor
	 */
	public Cat() {
		this.name = "Cat";
		this.image = new ImageIcon("Cat.png").getImage();		
	}
}
