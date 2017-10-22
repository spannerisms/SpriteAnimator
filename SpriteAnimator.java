package SpriteAnimator;

//import java.awt.BorderLayout;
import java.awt.Component;
//import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;

//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFileChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JMenuBar;
//import javax.swing.JMenuItem;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
import javax.swing.Timer;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class SpriteAnimator extends Component {
	static final String[] ALLFRAMES = Database.ALLFRAMES;
	private static final long serialVersionUID = 2114886855236406900L;

	static final int PALETTESIZE = 0x78; // not simplified to understand the numbers

	// Almost the length of a frame at 60 FPS
	// 1/60 approx. 16.66666...
	public static final int FPS = 16;
	
	// used for parsing frame data
	static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZαβ"
			.toUpperCase(); // toUppercase to visually distinguish alpha/beta

	/*
	 * Image controller
	 */
	private BufferedImage img = null; // sprite sheet
	private int anime; // animation id
	private int speed; // speed; 0 = normal; positive = faster; negative = slower
	private int mode; // animation mode
	private int frame;
	private int maxFrame;
	private boolean running;
	private int zoom = 3;
	private Anime[] frames = null;
	private Timer tick;
	private static final int MAXSPEED = 6; // maximum speed magnitude
	private static final int MAXZOOM = 7;
	// default initialization
	public SpriteAnimator() {
		anime = 0;
		speed = 0;
		mode = 0;
		frame = 0;
		maxFrame = 0;
		running = true;
		tick = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isRunning()) {
					step();
				}
			}
		});
	}
	
	public int getFrame() {
		return frame;
	}
	
	public String frameDis() {
		return "" + (frame + 1);
	}
	public int maxFrame() {
		return maxFrame;
	}
	/**
	 * Set image to animate.
	 * @param image
	 */
	public void setImage(BufferedImage image) {
		img = image;
	}

	/**
	 * Set animation ID.
	 * @param id
	 */
	public void setAnimation(int id) {
		if (img == null) {
			return;
		}
		anime = id;
		makeAnimationFrames();
		reset();
	}
	
	/**
	 * Get animation mode ID#.
	 */
	public int getMode() {
		return mode;
	}
	/**
	 * Set image mode and reset.
	 * <ul style="list-style:none">
	 * <li><b>0</b> - normal animation</li>
	 * <li><b>1</b> - step-by-step</li>
	 * <li><b>2</b> - all frames</li>
	 * </ul>
	 * @param m - mode 
	 */
	public void setMode(int m) {
		mode = m;
		reset();
	}
	
	/**
	 * Step forward 1 animation frame.
	 * Resets frame to 0 if we reach the end in modes that loop.
	 * Stops running if we reach the end of the animation in "All frames" mode.
	 * @return Frame # painted
	 */
	public void step() {
		frame++;
		if (frame >= maxFrame) {
			frame = 0;
			if (mode == 2) {
				setRunning(false);
			}
		}
		repaint();
	}

	/**
	 * Reset based on mode.
	 */
	public void reset() {
		switch (mode) {
			case 0 :
				resetFrame();
				resetSpeed();
				setRunning(true);
				break;
			case 1 :
				resetFrame();
				setRunning(false);
				break;
			case 2 :
				resetFrame();
				resetSpeed();
				setRunning(true);
				break;
		}
		repaint();
	}
	
	/**
	 * Reset speed to 0.
	 */
	private void resetSpeed() {
		speed = 0;
	}
	
	/**
	 * Resets frame to 0.
	 */
	private void resetFrame() {
		frame = 0;
	}
	
	/**
	 * Control self-animation permission.
	 */
	private void setRunning(boolean r) {
		running = r;
		if (r)
			tick.start();
		else
			tick.stop();
	}
	
	/**
	 * @return <b>true</b> if active.
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * @return Timer object
	 */
	public Timer getTimer() {
		return tick;
	}
	/**
	 * Increments step speed by 1.
	 * @return <b>true</b> if speed reaches max.
	 */
	public boolean faster() {
		if (speed < MAXSPEED) {
			speed++;
		}
		return atMaxSpeed();
	}
	
	/**
	 * Decrements step speed by 1.
	 * @return <b>true</b> if speed reaches min.
	 */
	public boolean slower() {
		if (speed > (MAXSPEED * -1)) {
			speed--;
		}
		return atMinSpeed();
	}
	
	/**
	 * Zooms in by 1x.
	 * @return <b>true</b> if we're really big.
	 */
	public boolean embiggen() {
		if (zoom < MAXZOOM) {
			zoom++;
		}
		repaint();
		return (zoom >= MAXZOOM);
	}
	
	/**
	 * Zooms out by 1x.
	 * @return <b>true</b> if we're vanilla size.
	 */
	public boolean ensmallen() {
		if (zoom > 1) {
			zoom--;
		}
		repaint();
		return (zoom <= 1);
	}
	/**
	 * Adjusts timer based on speed
	 */
	private void adjustTimer() {
		double speedM = Math.pow(1.5, speed * -1);
		frames[frame].setNextTick(tick, speedM);
	}
	/**
	 * Compares current step speed to maximum speed allowed.
	 */
	public boolean atMaxSpeed() {
		return speed == MAXSPEED;
	}
	/**
	 * Compares current step speed to minimum speed allowed.
	 */
	public boolean atMinSpeed() {
		return speed == (-1 * MAXSPEED);
	}

	/**
	 * Draw every sprite
	 */
	public void paint(Graphics g) {
		if (frames==null || frames[frame] == null) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(zoom, zoom);
		Anime t = frames[frame];
		t.draw(g2);
		adjustTimer();
	}

	// @link Sprite - lol get it?
	/**
	 * Makes an array of {@link Sprite}s based on the frame data.
	 */
	private void makeAnimationFrames() {
		if (img == null) {
			return;
		}
		String animData = ALLFRAMES[anime].toUpperCase().replace(" ", ""); // CAPS and remove all whitespace
		// split into sections

		String[] animDataX = animData.split("[\\[\\]]+");
		String[] eachFrame = animDataX[3].split(";"); // split by frame
		// get duration

		maxFrame = eachFrame.length;
		frames = new Anime[maxFrame];
		// each frame
		for (int i = 0; i < maxFrame; i++) {
			String[] wholeFrame = eachFrame[i].split("@");
			int animSpeed;
			try {
				animSpeed = Integer.parseInt(wholeFrame[1]);
			} catch (Exception e) {
				animSpeed = 100;
			}
			String[] eachSprite = wholeFrame[0].split(":");
			int spriteCount = eachSprite.length;
			// each sprite in frame
			Sprite[] sprList = new Sprite[spriteCount];
			frames[i] = new Anime(sprList, animSpeed);
			for (int j = 0; j < spriteCount; j++) {
				// split into info sections
				String[] spriteSplit = eachSprite[j].split("[\\{\\}]{1,2}");
				char[] sprIndex = spriteSplit[0].toCharArray();
				String[] pos = spriteSplit[1].split(",");
				String sprSize = spriteSplit[2];
				String sprTrans = spriteSplit[3];
				// sprite position
				int xpos = Integer.parseInt(pos[0]);
				int ypos = Integer.parseInt(pos[1]);
				int drawY = ALPHA.indexOf(sprIndex[0]) * 16;
				int drawX = Integer.parseInt((sprIndex[1] + "")) * 16;
				int drawYoffset, drawXoffset, width, height;
				
				// determine offset from initial position
				switch (sprSize) {
					case "F" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 16;
						height = 16;
						break;
					case "T" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 16;
						height = 8;
						break;
					case "B" :
						drawYoffset = 8;
						drawXoffset = 0;
						width = 16;
						height = 8;
						break;
					case "R" :
						drawYoffset = 0;
						drawXoffset = 8;
						width = 8;
						height = 16;
						break;
					case "L" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 8;
						height = 16;
						break;
					case "TR" :
						drawYoffset = 0;
						drawXoffset = 8;
						width = 8;
						height = 8;
						break;
					case "TL" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 8;
						height = 8;
						break;
					case "BR" :
						drawYoffset = 8;
						drawXoffset = 8;
						width = 8;
						height = 8;
						break;
					case "BL" :
						drawYoffset = 8;
						drawXoffset = 0;
						width = 8;
						height = 8;
						break;
					default :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 16;
						height = 16;
						break;
				}
				drawX += drawXoffset;
				drawY += drawYoffset;
				BufferedImage spreet;
				// blank sprite frame
				if (sprSize.equals("E"))
					spreet = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR_PRE);
				else
					spreet = img.getSubimage(drawX, drawY, width, height);
				
				// transformations
				switch (sprTrans) {
					case "M" :
						spreet = flipH(spreet);
						break;
					case "U" :
						spreet = flipV(spreet);
						break;
					case "UM" :
						spreet = flipH(spreet);
						spreet = flipV(spreet);
						break;
					default :
						// nothing
						break;
				}
				// put it in backwards to preserve draw order
				sprList[spriteCount-1-j] = new Sprite(spreet, xpos, ypos);
			}
		}
	}


	// error controller
	static final SpriteAnimator controller = new SpriteAnimator();
	
	
	// transformations
	public static BufferedImage flipV(BufferedImage image){
		return flip(image, true);
	}
	
	public static BufferedImage flipH(BufferedImage image){
		return flip(image, false);
	}
	
	public static BufferedImage flip(BufferedImage image, boolean vertical) {
		AffineTransform at = new AffineTransform();
		if (vertical) {
			at.concatenate(AffineTransform.getScaleInstance(1, -1));
			at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
		} else {
			at.concatenate(AffineTransform.getScaleInstance(-1, 1));
			at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		}
		BufferedImage newImage =
				new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}
	
	public static void main(String[] args) throws IOException {
		GUI G = new GUI();
		G.printGUI(args);
	}
}
