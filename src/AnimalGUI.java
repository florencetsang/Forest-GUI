import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This AnimalGUI.java implements an application that simulates a forest. 
 * The forest is represented by a 15 * 15 array of cells, and 1-8 animals as selected by user.
 * The animals would move, attack each other and die.
 * @author 	Florence
 * @version 1.0
 * @since	14-3-2017
 */
public class AnimalGUI {
	private Random rand = new Random();
	private Helper helper = new Helper();
	private ArrayList<Animal> aliveAnimalsList = new ArrayList<Animal>();
	private ArrayList<Animal> animalsList = new ArrayList<Animal>();
	private Animal[] animals = new Animal[8];
	private int forestSize = 15;
	
	private ArrayList<JCheckBox> checkboxList;
	private JFrame startFrame = new JFrame("Startup Menu");
	private JFrame forestFrame = new JFrame("Forest Simulation");
	private ArrayList<Integer[]> boxHighlight = new ArrayList<Integer[]>();
	private Animal animal_current = null;
	private int mouseX, mouseY, mx, my;
	private Pane board = new Pane();
	private int boxSize = 58;
	private boolean isAuto = false;
	private Color bg = new Color(243,225,210); //background - wooden color 
	//private Color bg = new Color(255,255,255); //background - white
	private Runnable job = new MyRunnable();
	private Thread t = new Thread(job);
	private JPanel animalCol;
	private JButton auto2;
	private JButton iocnButtons[] = {new JButton(),new JButton(),new JButton(),new JButton(),new JButton(),new JButton(),new JButton(),new JButton()};
	/**
	 * This is the main method which controls the basic program flow.
	 * It makes use of the initializeForest method to populate the forest randomly with 8 animals, 
	 * then prints the forest with the printForest method.
	 * It makes use of the doGame method to carry out an iteration every time the user presses enter,
	 * and end when the user types 'exit'.
	 * At the end, it makes use of the termMsg() method to print a list of animals' condition at the end of the game.
	 */
	public static void main(String[] args) {
		AnimalGUI myForest = new AnimalGUI();
		myForest.go();	
	}
	/**
	 * This go method creates and displays the GUI for the startup menu.
	 * It defines all the contends inside the JFrame - startFrame.
	 * It also creates an ArrayList - checkboxList to store which animals are checked.
	 */
	private void go(){
		//create animal objects
		animals[0] = new Cat();
		animals[1] = new Dog();
		animals[2] = new Fox();
		animals[3] = new Hippo();
		animals[4] = new Lion();
		animals[5] = new Tiger();
		animals[6] = new Turtle();
		animals[7] = new Wolf();
		//scale animal images
		for(int i = 0 ; i < animals.length ; i++){
			animals[i].image = animals[i].image.getScaledInstance(boxSize-3, boxSize-3, Image.SCALE_DEFAULT);
		}
		
		startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		checkboxList = new ArrayList<JCheckBox>();			
		JPanel background = new JPanel(new BorderLayout());
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel startPanel = new JPanel(new BorderLayout());
	    startPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));		    	
		JPanel checkboxCol = new JPanel(new GridLayout(8,2,5,5));
		for(int i = 0 ; i < 8 ; i++){
			JCheckBox c = new JCheckBox();
			c.setSelected(true);
			checkboxList.add(c);
			checkboxCol.add(c);
		}	
		animalCol = new JPanel(new GridLayout(8,2,15,5));
		setAnimalCol();
		JPanel pickIconCol = new JPanel(new GridLayout(8,1,5,5));
		for(int i = 0 ; i < 8 ; i++){
			iocnButtons[i].setText("Pick an alternative icon");
			iocnButtons[i].setFont(new Font("sanSerif", Font.PLAIN, 18));
			pickIconCol.add(iocnButtons[i]);
			iocnButtons[i].addActionListener(new PickIconListener(i));
		}
		startPanel.add(checkboxCol, BorderLayout.WEST);
		startPanel.add(animalCol, BorderLayout.CENTER);
		startPanel.add(pickIconCol, BorderLayout.EAST);
		background.add(startPanel, BorderLayout.CENTER);
		
		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				initializeForest();
				forestGUI();
				startFrame.dispose();			
			}
		});
		start.setFont(new Font("sanSerif", Font.PLAIN, 18));
		background.add(start, BorderLayout.SOUTH);
		
		JPanel welcome = new JPanel(new GridLayout(2,1));
		JLabel wel1 = new JLabel("Florence's Forest Simulation Program");
		JLabel wel2 = new JLabel("Startup Menu");
		welcome.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		welcome.setBackground(new Color(170,215,250));
		welcome.add(wel1);
		welcome.add(wel2);		
		wel1.setFont(new Font("sanSerif", Font.PLAIN, 28));
		wel2.setFont(new Font("sanSerif", Font.PLAIN, 26));
		wel1.setHorizontalAlignment(JLabel.CENTER);
		wel2.setHorizontalAlignment(JLabel.CENTER);	
		background.add(welcome, BorderLayout.NORTH);
		startFrame.getContentPane().add(background,  BorderLayout.CENTER);
		startFrame.setSize(500, 700);
		startFrame.pack();
		startFrame.setVisible(true);			
	}
	/**
	 * This PickIocnListener class is an ActionListener for the "pick alternative icon" iocnButtons.
	 */
	class PickIconListener implements ActionListener{
		int i;
		PickIconListener(int i){
			this.i = i;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(startFrame);
			try{
				BufferedImage pic = ImageIO.read(fileOpen.getSelectedFile());
				animals[i].image = pic.getScaledInstance(boxSize-3, boxSize-3, Image.SCALE_DEFAULT);;
				iocnButtons[i].setText("New icon selected");
				animalCol.removeAll();
				setAnimalCol();
				animalCol.revalidate();
				startFrame.repaint();
			}catch(Exception ex){
				System.out.println("couldn't read the image file");
			}	
		}		
	}
	/**
	 * This setAnimalCol method adds animal names and their icons into the panel of the Statup Menu
	 * It is called at the beginning, and also after each time a new icon is selected.
	 */
	private void setAnimalCol(){
		for(int i = 0 ; i < 8 ; i++){
			JLabel name = new JLabel(animals[i].name);
			name.setSize(100,50);
			name.setFont(new Font("sanSerif", Font.PLAIN, 22));
			animalCol.add(name);
			JLabel label = new JLabel();
			ImageIcon img = new ImageIcon(animals[i].image);
			label.setIcon(img);
			animalCol.add(label);
		}
	}
	/**
	 * This initializeForest method populates the forest randomly with 8 animals, 
	 * namely a Dog, a Fox, a Wolf, a Cat, a Lion, a Tiger, a Hippo and a Turtle. 
	 */
	private void initializeForest(){		
		animalsList.clear();
		aliveAnimalsList.clear();
		for(int i = 0 ; i < 8 ; i++){
			JCheckBox jc = (JCheckBox) checkboxList.get(i);
			if(jc.isSelected()){
				animalsList.add(animals[i]);
			}
		}
		System.out.println("--------Start--------");
		for(int i = 0 ; i < animalsList.size() ; i++){
			int [] loc = {rand.nextInt(forestSize),rand.nextInt(forestSize)}; //determine location of animal
			while(occupied(loc[0] ,loc[1]) != -1){ //ensures that the location is not already occupied by another animal
				loc[0] = rand.nextInt(forestSize);
				loc[1] = rand.nextInt(forestSize);
			}
			animalsList.get(i).setLocation(loc[0], loc[1]); //set animal location
			System.out.println(animalsList.get(i).name+" initialized at "+loc[0]+" "+loc[1]);
			animalsList.get(i).isAlive = true;
			aliveAnimalsList.add(animalsList.get(i)); //add all animals to aliveAnimalsList
		}		
	}
	/**
	 * This forestGUI method creates the GUI of the forest board.
	 * It contains a JPanel board, which is the 15*15 board, and JButtons for auto
	 * This method also handles user interactions with the board in manual mode
	 */
	private void forestGUI(){
		forestFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel autoBackground = new JPanel();
		autoBackground.setBackground(bg);
		JButton auto1 = new JButton("Auto for 1 iteration");
		auto1.setFont(new Font("sanSerif", Font.PLAIN, 22));
		auto1.setBackground(new Color(150,110,100));
		auto1.setForeground(new Color(255,255,255));
		auto1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				boxHighlight.clear(); 
				runCycle();		
			}
		});		
		auto2 = new JButton("Auto Mode");
		auto2.setFont(new Font("sanSerif", Font.PLAIN, 22));
		auto2.setBackground(new Color(150,110,100));
		auto2.setForeground(new Color(255,255,255));
		auto2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!isAuto){
					if(t.getState().equals(Thread.State.NEW)){
						t = new Thread(job);
						t.start();
					}				
					isAuto = true;
					auto2.setText("Manual Mode");
				}else{
					auto2.setText("Auto Mode");	
					isAuto = false;
				}	
			}
		});			
		autoBackground.add(auto1);
		autoBackground.add(auto2);		
		autoBackground.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
		forestFrame.getContentPane().add(autoBackground, BorderLayout.SOUTH);
		board.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
		        int box[] = box(mouseX, mouseY);		        
		        if(isHighlighted(box)){
		        	doMove(animal_current, box);
		        	boxHighlight.clear(); 
		        }else if(occupied(box[0],box[1]) != -1 && animalsList.get(occupied(box[0],box[1])).isAlive){	
		        	boxHighlight.clear(); 
		        	animal_current = animalsList.get(occupied(box[0],box[1]));		        	
		        	for(int i = 0 ; i < animal_current.allPos().length ; i++){		        		
		        		Integer[] tempPos = new Integer[2];
		        		tempPos[0] = Integer.valueOf(animal_current.allPos()[i][0]);
		        		tempPos[1] = Integer.valueOf(animal_current.allPos()[i][1]);
		        		if(!helper.outOfBoard(tempPos[0], tempPos[1])){
		        			boxHighlight.add(tempPos);
		        		}
		        	}
		        }else{
		        	boxHighlight.clear(); 
		        }		        	
		        forestFrame.repaint();
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}			
		});
		board.addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent arg0) {}
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				forestFrame.repaint();
				mx = box(mouseX, mouseY)[0];
            	my = box(mouseX, mouseY)[1];
            	int startX = (board.getWidth() - (boxSize * forestSize)) / 2;
            	int startY = (board.getHeight() - (boxSize * forestSize)) / 2;
				if (mouseX > startX && mouseX < startX + boxSize * forestSize && mouseY > startY && mouseY < startY + boxSize * forestSize) {
					board.setCursor(new Cursor(Cursor.HAND_CURSOR));
		        } else {
		        	board.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		        }
			}			
		});
		JPanel boardBack = new JPanel(new BorderLayout());
		boardBack.setBackground(bg);
		boardBack.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		boardBack.add(board);
		forestFrame.getContentPane().add(boardBack, BorderLayout.CENTER);	
		forestFrame.setSize(Math.max(boxSize*15+120, 860),boxSize*15+180);		
		//frame.pack();
		forestFrame.setResizable(true);
		forestFrame.setVisible(true);			
	}
	/**
	 * This Pane inner class draws the graphics inside the 15 * 15 forest board.
	 * It is used by the forestGUI method.
	 */
	private class Pane extends JPanel{
		public void paintComponent(Graphics g){
			Graphics2D g2d = (Graphics2D) g;
			g2d.setColor(bg);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
			g2d.setColor(new Color(30,0,0));
			g2d.setStroke(new BasicStroke(3));
			int startX = (getWidth() - (boxSize * forestSize)) / 2;
			int startY = (getHeight() - (boxSize * forestSize)) / 2;
			int y = startY;
            for (int horz = 0; horz < forestSize; horz++) {
                int x = startX;
                for (int vert = 0; vert < forestSize; vert++) {
                	g2d.drawRect(x, y, boxSize, boxSize);                    
                    x += boxSize;
                }
                y += boxSize;
            }
            for(int i = 0 ; i < animalsList.size(); i++){
            	int xi = startX + animalsList.get(i).x * boxSize;
            	int yi = startY + animalsList.get(i).y * boxSize;
            	g2d.drawImage(animalsList.get(i).image, xi+2, yi+2, this);
            	if(!animalsList.get(i).isAlive){
            		g2d.setColor(new Color(50,50,50,50)); 
            		g2d.fillRect(xi + 2, yi + 2, boxSize -3 , boxSize-3);
            		g2d.setColor(new Color(200,10,10)); 
            		g2d.setStroke(new BasicStroke(5));
            		g2d.drawLine(xi, yi, xi+boxSize, yi+boxSize);
            		g2d.drawLine(xi, yi+boxSize, xi+boxSize, yi);      		
            	}
            }
            for(int i = 0 ; i < boxHighlight.size() ; i++){
            	if(boxHighlight.get(i)[0] == mx && boxHighlight.get(i)[1] == my){
            		g2d.setColor(new Color(15,40,255,200));
            	}else if(occupied(boxHighlight.get(i)[0],boxHighlight.get(i)[1]) != -1 && animalsList.get(occupied(boxHighlight.get(i)[0],boxHighlight.get(i)[1])).isAlive){
            		g2d.setColor(new Color(200,0,0,160));     //highlight box (pos occupied by alive animal), defender
            	}else if(occupied(boxHighlight.get(i)[0],boxHighlight.get(i)[1]) != -1 && !animalsList.get(occupied(boxHighlight.get(i)[0],boxHighlight.get(i)[1])).isAlive){
            		g2d.setColor(new Color(0,0,0,0));   //occupied by dead animal
            	}else{
            		g2d.setColor(new Color(230,170,0,200));		//other available pos
            	}
            	g2d.fillRect(startX + boxHighlight.get(i)[0] * boxSize + 2, startY + boxHighlight.get(i)[1] * boxSize + 2, boxSize -3 , boxSize-3);
            }
		}
	}
	/**
	 * This end method displays a drop-down end game message 
	 * and creates a "Start Over" button to start the simulation again.
	 */
	private void end(){
		Animal a = aliveAnimalsList.get(0);
		System.out.println("---------END---------");
		System.out.println("The survivor is "+a.name+" at "+a.x+","+a.y);
		JPanel endMsgPanel = new JPanel();
		endMsgPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));	
		JLabel endMsg1 = new JLabel("Game over. The forest winner is "+a.name+"! Press ");
		JButton startOver = new JButton("Start Over");
		JLabel endMsg2 = new JLabel(" to start a new simulation.");
		endMsgPanel.setBackground(new Color(255,50,150));
		endMsg1.setFont(new Font("sanSerif", Font.PLAIN, 20));
		endMsg2.setFont(new Font("sanSerif", Font.PLAIN, 20));
		startOver.setFont(new Font("sanSerif", Font.PLAIN, 18));
		endMsgPanel.add(endMsg1);
		endMsgPanel.add(startOver);
		endMsgPanel.add(endMsg2);
		startOver.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//start over from startup menu
				/*startFrame = new JFrame("Startup Menu");
				go();
				board.removeMouseListener();
				forestFrame.dispose();
				aliveAnimalsList = new ArrayList<Animal>();
				animalsList = new ArrayList<Animal>();	
				forestFrame = new JFrame("Forest Simulation");*/
				initializeForest();
				forestFrame.remove(endMsgPanel);
				forestFrame.repaint();	
				forestFrame.setVisible(true);
				t = new Thread(job);
				isAuto = false;
				auto2.setText("Auto Mode");
			}
		});
		forestFrame.getContentPane().add(endMsgPanel, BorderLayout.NORTH);
		forestFrame.setVisible(true);		
	}
	/**
	 * This runCycle method does one cycle of the forest simulation.
	 * For each animal, it determines its target location using determineTar method,
	 * and then use the doMove method to do the moving process of the animal
	 */
	private void runCycle() {
		//for each living animal:
		for (int i = 0 ; i < aliveAnimalsList.size() ; i++){					
			Animal current = (Animal) aliveAnimalsList.get(i);  
			int [] tar = determineTar(current);
			doMove(current, tar);					
		}
		forestFrame.repaint();
	}
	/**
	 * This MyRunnable class is the job for the thread t.
	 * It handles the cycles in auto mode.
	 */
	class MyRunnable implements Runnable{
		public void run(){
			while(aliveAnimalsList.size() != 1){
				if(isAuto == true){
					runCycle();						
				}	
				try{
					Thread.sleep(100);
				}catch(Exception ex){}
			}
		}
	}
	/**
	 * This determineTar method determines the target location of an animal according to animal's moving style.
	 * @param a		the animal of which its target location is being determined
	 * @return 		an array of size 2 storing the coordinates of the target location
	 */
	private int[] determineTar(Animal a){
		//determine a animal's target location using that animal's moving style
		int [] tar = a.moveMethod(); 
		//if the target location is out of board, choose another location again until the target location is inside board
		while(helper.outOfBoard(tar[0], tar[1])){ 
			tar = a.moveMethod();
		}
		return tar;
	}
	/**
	 * This doMove method does moving process of an animal after its target location has been determined.
	 * It determines whether its path is blocked by another animal.
	 *  If it is blocked by a dead animal. Do no move to target location. Do nothing
	 *  If it is blocked by a living animal. Attack the animal.
	 *   If it wins the attack, it moves to the target location by the moveToTar method and the blocker animal dies.
	 *   If it loses the attack, it dies.
	 * If the path is not blocked, move to target location using moveToTar method.
	 * Note that the process of determining whether target location is occupied, attacking animal at target location, etc. is done inside the moveToTar method instead
	 * , which has been called by this method.
	 * @param a		the animal who wants to move to target location
	 * @param tar	an array of size 2 storing the coordinates of the target location.
	 */
	private void doMove(Animal a, int[] tar){
		if(!(tar[0]==a.x && tar[1]==a.y) && pathBlocked(a.x, a.y, tar[0], tar[1]) != -1){ // if path is blocked
			Animal blocker = animalsList.get(pathBlocked(a.x, a.y, tar[0], tar[1]));
			if(!blocker.isAlive){ //if the animal who blocks the path is dead
				System.out.println(a.name + " moved from "+ a.x +","+ a.y +" to " + a.x + "," + a.y); //don't move to tar
			}else{
				boolean winning = a.isWin(blocker);
				if(winning){
					System.out.println(a.name + " from " +a.x+ "," +a.y+" attacked " + blocker.name +" at "+blocker.x+ "," +blocker.y+" and wins");
					moveToTar(a, tar); //tries to go to target location
					die(blocker); //blocker animal dies	
				}else{
					System.out.println(a.name + " from " +a.x+ "," +a.y+" attacked " + blocker.name +" at "+blocker.x+ "," +blocker.y+" and loses");
					die(a); //a animal dies
				}
			}
		}else{
			moveToTar(a, tar);
		}
	}
	/**
	 * This moveToTar method implements the process of an animal trying to reach target location.
	 * It determines whether the target location is already occupied.
	 *  If it is occupied by a dead animal. Do no move to target location. Do nothing
	 *  If it is occupied by a living animal. Attack the animal.
	 *   If it wins the attack, update its location to target location using updateMove method and the blocker animal dies.
	 *   If it loses the attack, it dies.
	 * If target location not occupied, update to target location using updateMove method.
	 * Note that this method does not consider the case where the path (of 2 steps) is blocked by another animal. This case is considered by doMove method.
	 * @param a		the animal who wants to move to target location
	 * @param tar	an array of size 2 storing the coordinates of the target location.
	 */
	private void moveToTar(Animal a, int[] tar){
		if(!(tar[0]==a.x && tar[1]==a.y) && occupied(tar[0], tar[1]) != -1){ //if target location is occupied
			Animal blocker = animalsList.get(occupied(tar[0], tar[1]));
			if(!blocker.isAlive){ //if the animal who occupies the target location is dead
				System.out.println(a.name + " moved from "+ a.x +","+ a.y +" to " + a.x + "," + a.y); //don't move to tar
			}else{		
				boolean winning = a.isWin(blocker);
				if(winning){
					System.out.println(a.name + " from " +a.x+ "," +a.y+" attacked " + blocker.name +" at "+blocker.x+ "," +blocker.y+" and wins");
					updateMove(a,tar); 	//update location to target location					
					die(blocker); //blocker animal dies						
				}else{
					System.out.println(a.name + " from " +a.x+ "," +a.y+" attacked " + blocker.name +" at "+blocker.x+ "," +blocker.y+" and loses");
					die(a); //a animal dies
				}
			}
		}else{ //else (if target location is free)
			updateMove(a,tar); //just move to that target location
		}
	}
	/**
	 * This updateMove method updates the location of animal to a new location.
	 * @param a		animal of which its location needs to be updated
	 * @param tar	an array of size 2 storing the new location of the animal
	 */
	private void updateMove(Animal a, int[] tar){
		System.out.println(a.name + " moved from "+ a.x +","+ a.y +" to " + tar[0] + "," + tar[1]);
		a.x = tar[0];
		a.y = tar[1];
	}
	/**
	 * The die method does the death process of an animal, including:
	 * moving the dead body to one of the nearby empty location with equal chance
	 * printing death message (loser_animal dies at ?, ?)
	 * changing the isAlive value of animal to false
	 * changing the dead animal's symbol to upper case
	 * removing animal from aliveAnimalsList
	 * @param a		the animal who dies
	 */
	private void die(Animal a){
		//determines dead body location (dead body moves in a similar way to felines)
		int[][] allPos = helper.felinePos(a.x, a.y);
		int moveMod = rand.nextInt(allPos.length);
		int [] tar = allPos[moveMod];
		//ensures that the location is neither occupied nor out of board
		while(occupied(tar[0], tar[1]) != -1 || helper.outOfBoard(tar[0], tar[1])){
			moveMod = rand.nextInt(allPos.length);
			tar = allPos[moveMod];
		}
		//update dead animal location to target location
		a.x = tar[0];
		a.y = tar[1];
		System.out.println(a.name +" died at "+a.x+ "," +a.y);	//print death message	
		a.isAlive = false; //change the isAlive value of animal to false
		aliveAnimalsList.remove(aliveAnimalsList.indexOf(a)); // remove animal from aliveAnimalsList
        if(aliveAnimalsList.size() == 1){
			end();
		}
	}
	/**
	 * This occupied method determines whether a position is occupied. And if so, occupied by whom.
	 * @param a x-coordinate of the position being checked
	 * @param b y-coordinate of the position being checked
	 * @return -1 if the position is not occupied; the array slot of the animal in animalsList occupying the position otherwise.
	 */
	private int occupied(int a, int b){
		for(int i = 0 ; i < animalsList.size() ; i++){
			if(a == animalsList.get(i).x && b == animalsList.get(i).y){
				return i;
			}
		}
		return -1;
	}
	/**
	 * This pathBlocked method determines whether a path from original location to target location is blocked
	 * @param u x-coordinate of the original position 
	 * @param v y-coordinate of the original position 
	 * @param x x-coordinate of the target position 
	 * @param y y-coordinate of the target position 
	 * @return -1 if the path is not blocked; the array slot of the animal in animalsList blocking the path otherwise.
	 */
	private int pathBlocked(int u, int v, int x, int y){
		if(y-v == 2 || v-y ==2){
			return occupied(x, (y+v)/2);
		}else if(x-u == 2 || u-x==2){
			return occupied((x+u)/2, y);
		}else{
			return -1;
		}		
	}
	/**
	 * This box method is to covert the mouse position relative to the board JPanel 
	 * into the box of 15 * 15 board which the mouse is located
	 * @param x			x coordinate of the mouse position
	 * @param y			y coordinate of the mouse position
	 * @param board		the 15 * 15 forest board, which is a JPanel
	 * @return	box 	an array of length 2, storing the coordinates of the box
	 */
	private int[] box(int x, int y){
		int boxX = (x-(board.getWidth() - (boxSize * forestSize)) / 2) / boxSize;
		int boxY = (y-(board.getHeight() - (boxSize * forestSize)) / 2) / boxSize;
		int box[] = {boxX, boxY};
		return box;		
	}
	/**
	 * This isHighligted method determines if a box is currently highlighted
	 * @param box	an array holding the x and y positions of the box
	 * @return		true if the box is currently highlighted; false otherwise
	 */
	private boolean isHighlighted(int[] box){	
		for(int i = 0 ; i < boxHighlight.size(); i++){
			int hx = boxHighlight.get(i)[0];
	    	int hy = boxHighlight.get(i)[1];
			if(hx == box[0] && hy == box[1]){
				return true;
			}
		}
		return false;
	}
}