package SpriteAnimator;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class SpriteAnimator extends Component {
	private BufferedImage BG; {
		try {
			BG = ImageIO.read(this.getClass().getResource("Grass.png"));
		} catch (IOException e) {
	}};
	private BufferedImage EQUIPMENT; {
		try {
			EQUIPMENT = ImageIO.read(this.getClass().getResource("Equipment.png"));
		} catch (IOException e) {
	}};

	static final String[] ALLFRAMES = Database.ALLFRAMES;
	private static final long serialVersionUID = 2114886855236406900L;

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
	private TimerTask next;
	private static final int MAXSPEED = 5; // maximum speed magnitude
	private static final int MAXZOOM = 7; // maximum zoom level

	private List<StepListener> stepListen = new ArrayList<StepListener>();
	private List<ModeListener> modeListen = new ArrayList<ModeListener>();
	private List<SpeedListener> speedListen = new ArrayList<SpeedListener>();
	private List<ZoomListener> zoomListen = new ArrayList<ZoomListener>();

	// default initialization
	public SpriteAnimator() {
		anime = 0;
		speed = 0;
		mode = 0;
		frame = 0;
		maxFrame = 0;
		makeAnimationFrames();
		running = true;
		tick = new Timer();
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
			if (mode == 2) {
				frame = maxFrame-1;
				setRunning(false);
			} else {
				frame = 0;
			}
		}
		repaint();
		fireStepEvent();
		adjustTimer();
	}

	/**
	 * Reset based on mode.
	 */
	public void reset() {
		try {
			tick.cancel();
			tick = new Timer();
		} catch(Exception e) {};
		switch (mode) {
			case 0 :
				setRunning(true);
				resetFrame();
				resetSpeed();
				break;
			case 1 :
				setRunning(false);
				resetFrame();
				break;
			case 2 :
				setRunning(true);
				resetFrame();
				resetSpeed();
				break;
		}
		fireModeEvent();
		repaint();
	}
	
	/**
	 * Reset speed to 0.
	 */
	private void resetSpeed() {
		setSpeed(0);
	}
	
	/**
	 * Resets frame to 0.
	 */
	private void resetFrame() {
		// force a step
		frame = -1;
		step();
	}
	
	/**
	 * Control self-animation permission.
	 */
	private void setRunning(boolean r) {
		running = r;
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
	public void faster() {
		setSpeed(speed+1);
	}
	
	/**
	 * Decrements step speed by 1.
	 * @return <b>true</b> if speed reaches min.
	 */
	public void slower() {
		setSpeed(speed-1);
	}

	public void setSpeed(int s) {
		if (s == speed) {
			// do nothing
		} else if (s < speed) {
			if (atMinSpeed()) {
				// do nothing
			} else {
				speed = s;
			}
		} else if (s > speed) {
			if (atMaxSpeed()) {
				// do nothing
			} else {
				speed = s;
			}
		} else { // should never hit this
			speed = 0; // but if we do, reset the speed
		}
		fireSpeedEvent();
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public String getSpeedPercent() {
		double speedM = Math.pow(1.5, speed * -1);
		int s = (int) (100 / speedM);
		return s + "%";
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
		return (speed * -1) == MAXSPEED;
	}
	
	/*
	 * Zoom functions
	 */
	/**
	 * Zooms in by 1x.
	 * @return <b>true</b> if we're really big.
	 */
	public void embiggen() {
		setZoom(zoom+1);
	}
	
	/**
	 * Zooms out by 1x.
	 * @return <b>true</b> if we're vanilla size.
	 */
	public void ensmallen() {
		setZoom(zoom-1);
	}

	public void setZoom(int z) {
		if (z == zoom) {
			// do nothing
		} else if (z < zoom) {
			if (vanillaSize()) {
				// do nothing
			} else {
				zoom = z;
			}
		} else if (z > zoom) {
			if (tooBig()) {
				// do nothing
			} else {
				zoom = z;
			}
		} else { // should never hit this
			zoom = 0; // but if we do, reset the speed
		}
		fireZoomEvent();
		repaint();
	}

	public int getZoom() {
		return zoom;
	}
	public boolean tooBig() {
		return zoom >= MAXZOOM;
	}
	
	public boolean vanillaSize() {
		return zoom == 1;
	}

	/**
	 * Adjusts timer based on speed
	 */
	private void adjustTimer() {
		if (!running) {
			tick.cancel();
			return;
		}
		double speedM = Math.pow(1.5, speed * -1);
		long wait = frames[frame].nextTick(speedM);
		next = new SpriteTask(this);
		tick.schedule(next, wait);
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
		// TODO : Reenable background maybe
		// g2.drawImage(BG, 0, 0, null);
		Anime t = frames[frame];
		t.draw(g2);
	}

	/*
	 * Change listeners
	 */
	// Step
	public synchronized void addStepListener(StepListener s) {
		stepListen.add(s);
	}
	
	public synchronized void removeStepListener(StepListener s) {
		stepListen.remove(s);
	}
	
	public synchronized void fireStepEvent() {
		StepEvent s = new StepEvent(this);
		Iterator<StepListener> listening = stepListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	// Speed
	public synchronized void addSpeedListener(SpeedListener s) {
		speedListen.add(s);
	}
	
	public synchronized void removeSpeedListener(SpeedListener s) {
		speedListen.remove(s);
	}
	
	public synchronized void fireSpeedEvent() {
		SpeedEvent s = new SpeedEvent(this);
		Iterator<SpeedListener> listening = speedListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	// mode
	public synchronized void addModeListener(ModeListener s) {
		modeListen.add(s);
	}
	
	public synchronized void removeModeListener(ModeListener s) {
		modeListen.remove(s);
	}
	
	public synchronized void fireModeEvent() {
		ModeEvent s = new ModeEvent(this);
		Iterator<ModeListener> listening = modeListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}
	
	// zoom
	public synchronized void addZoomListener(ZoomListener s) {
		zoomListen.add(s);
	}
	
	public synchronized void removeZoomListener(ZoomListener s) {
		zoomListen.remove(s);
	}
	
	public synchronized void fireZoomEvent() {
		ZoomEvent s = new ZoomEvent(this);
		Iterator<ZoomListener> listening = zoomListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}
	// @link Sprite - lol get it?
	/**
	 * Makes an array of {@link Sprite}s based on the frame data.
	 */
	private void makeAnimationFrames() {
		if (img == null) {
			return;
		}
		BufferedImage sheet;
		String animData = ALLFRAMES[anime].toUpperCase().replace(" ", ""); // CAPS and remove all whitespace
		// split into sections

		String[] animDataX = animData.split("[\\[\\]]+");
		String[] eachFrame = animDataX[2].split(";"); // split by frame
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
				
				// sprite sheet and index
				String sprIndex = spriteSplit[0];
				String sprIndexRow = sprIndex.substring(0, sprIndex.length()-1);
				int sprIndexRowVal = getIndexRow(sprIndexRow);
				char sprIndexCol = sprIndex.charAt(sprIndex.length()-1);

				// assume 1 character rows are always link
				// and all equipment is named longer than 0
				if (sprIndexRow.length() == 1) {
					sheet = img;
				} else {
					sheet = EQUIPMENT;
				}
				// sprite position
				String[] pos = spriteSplit[1].split(",");
				int xpos = Integer.parseInt(pos[0]);
				int ypos = Integer.parseInt(pos[1]);
				int drawY = sprIndexRowVal * 16;
				int drawX = Integer.parseInt((sprIndexCol + "")) * 16;
				int drawYoffset, drawXoffset, width, height;

				// sprite size and transformation
				String sprSize = spriteSplit[2];
				String sprTrans = spriteSplit[3];

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
					spreet = sheet.getSubimage(drawX, drawY, width, height);
				
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
	
	private static int getIndexRow(String row) {
		int ret = 0;
		if (row.length() == 1) {
			ret = ALPHA.indexOf(row);
		} else {
			switch (row) {
				case "FSWORD" : // fighter's sword
					ret = 0;
					break;
				case "MSWORD" : // master sword
					ret = 1;
					break;
				case "TSWORD" : // tempered sword
					ret = 2;
					break;
				case "BSWORD" : // butter sword
					ret = 3;
					break;
				case "FSHIELD" : // figher's shield
					ret = 4;
					break;
				case "RSHIELD" : // red shield
					ret = 5;
					break;
				case "MSHIELD" : // mirror shield
					ret = 6;
					break;
				case "SHADOW" :
					ret = 7;
					break;
			}
		}
		
		return ret;
	}
}