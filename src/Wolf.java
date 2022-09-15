import javax.swing.ImageIcon;

/**
 * The class for the animal wolf.
 * @author 	Florence
 */
public class Wolf extends Canine {
	/**
	 * Class constructor
	 */
	public Wolf() {
		this.name = "Wolf";
		this.image = new ImageIcon("Wolf.png").getImage();
	}
}
