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

import SpriteAnimator.Listeners.*;

public class SpriteAnimator extends Component {
	private static final long serialVersionUID = 2114886855236406900L;

	private BufferedImage[] BGS = Backgrounds.getBackgrounds();
	private BufferedImage EQUIPMENT; {
		try {
			EQUIPMENT = ImageIO.read(SpriteAnimator.class.getResourceAsStream(
					"/SpriteAnimator/Images/Equipment sheet.png"));
		} catch (IOException e) {
	}};

	static final String[] ALLFRAMES = Database.ALLFRAMES;

	// Almost the length of a frame at 60 FPS
	// 1/60 approx. 16.66666...
	public static final int FPS = 16;

	// used for parsing frame data
	static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZαβ"
			.toUpperCase(); // toUppercase to visually distinguish alpha/beta

	// equipment names and prefixes
	static final String[] EQUIPNAMES = {
			"CANE", "BYRNA", "SOMARIA", "BLOCK", "SPARKLE",
			"ROD", "FIREROD", "ICEROD", "POWDER",
			"HAMMER", "NET", "HOOKSHOT", "BOOMERANG",
			"BOW", "BOOK", "PENDANT", "SHOVEL", "BED"
	};

	// fighter, fighter, master, tempered, butter
	static final String[] SWORDPREFIX = { "F", "F", "M", "T", "B" }; // default to fighter
	// fighter, fighter, red, mirror
	static final String[] SHIELDPREFIX = { "F", "F", "R", "M" }; // default to fighter

	// maximums
	private static final int MAXSPEED = 5; // maximum speed magnitude
	private static final int MAXZOOM = 7; // maximum zoom level

	// locals
	private BufferedImage[] mailImages = null; // sprite sheet
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

	// equipment
	private int bg = 0;
	private int mailLevel = 0;
	private int swordLevel = 0;
	private int shieldLevel = 0;
	private boolean showShadow = false;
	private boolean showEquipment = false;

	// change listeners
	private List<StepListener> stepListen = new ArrayList<StepListener>();
	private List<ModeListener> modeListen = new ArrayList<ModeListener>();
	private List<SpeedListener> speedListen = new ArrayList<SpeedListener>();
	private List<ZoomListener> zoomListen = new ArrayList<ZoomListener>();
	private List<EquipListener> equipListen = new ArrayList<EquipListener>();

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

	/**
	 * Multiplier that determines exactly how much faster animations move
	 */
	private double speedFactor() {
		double m = Math.pow(1.5, speed * -1);
		return m;
	}

	/**
	 * Current frame of current animation, not 0-indexed
	 */
	public String getFrame() {
		return "" + (frame + 1);
	}

	/**
	 * Highest frame of current animation, not 0-indexed
	 */
	public int maxFrame() {
		return maxFrame;
	}

	/**
	 * Set images to animate
	 * @param image
	 */
	public void setImage(BufferedImage[] image) {
		mailImages = image;
	}

	/**
	 * Set animation ID
	 * @param id
	 */
	public void setAnimation(int id) {
		if (mailImages == null) {
			return;
		}
		anime = id;
		makeAnimationFrames();
		reset();
	}

	/**
	 * Get animation mode ID#
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
	 * Reset based on mode
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
	 * Remakes animation when we need to add new images to the sprite
	 */
	public void hardReset() {
		tick.cancel();
		makeAnimationFrames();
		reset();
	}
	/**
	 * Reset speed to 0
	 */
	private void resetSpeed() {
		setSpeed(0);
	}

	/**
	 * Resets frame to 0
	 */
	private void resetFrame() {
		// force a step
		frame = -1;
		step();
	}

	/**
	 * Control self-animation permission
	 */
	private void setRunning(boolean r) {
		running = r;
	}

	/**
	 * Increments step speed by 1.
	 */
	public void faster() {
		setSpeed(speed+1);
	}

	/**
	 * Decrements step speed by 1
	 */
	public void slower() {
		setSpeed(speed-1);
	}

	/**
	 * Sets speed based on defined bounds
	 * @param s
	 */
	private void setSpeed(int s) {
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

	/**
	 * Get the speed multiplier as a percentage
	 */
	public String getSpeedPercent() {
		double speedM = speedFactor();
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

	/**
	 * Zooms in by 1x
	 */
	public void embiggen() {
		setZoom(zoom+1);
	}

	/**
	 * Zooms out by 1x
	 */
	public void ensmallen() {
		setZoom(zoom-1);
	}

	/**
	 * Sets zoom level based on defined bounds
	 * @param z
	 */
	private void setZoom(int z) {
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

	/**
	 * Zoom level
	 */
	public int getZoom() {
		return zoom;
	}

	/**
	 * Is Link too big?!
	 */
	public boolean tooBig() {
		return zoom >= MAXZOOM;
	}

	/**
	 * Is Link drawn at 100% size??? :thinking:
	 */
	public boolean vanillaSize() {
		return zoom == 1;
	}

	/**
	 * Adjust timer based on speed and current frame
	 */
	private void adjustTimer() {
		if (!running) {
			tick.cancel();
			return;
		}
		try {
			double speedM = speedFactor();
			long wait = frames[frame].nextTick(speedM);
			next = new SpriteTask(this);
			tick.schedule(next, wait);
		} catch (Exception e) {
			// do nothing
			// most errors seem to just let it keep going
		}
	}

	/**
	 * Return equipment display status.
	 */
	public void switchEquipment() {
		showEquipment = !showEquipment;
		fireEquipEvent();
	}

	/**
	 * Return equipment display status
	 */
	public boolean equipmentOn() {
		return showEquipment;
	}

	/**
	 * Switch shadow display status
	 */
	public void switchShadow() {
		showShadow = !showShadow;
		fireEquipEvent();
	}

	/**
	 * Return shadow display status
	 */
	public boolean shadowOn() {
		return showShadow;
	}

	/**
	 * Set mail level
	 * @param ml
	 */
	public void setMail(int ml) {
		mailLevel = ml;
		fireEquipEvent();
	}

	/**
	 * Set sword level
	 * @param sl
	 */
	public void setSword(int sl) {
		swordLevel = sl;
		fireEquipEvent();
	}

	/**
	 * Set shield level
	 * @param sl
	 */
	public void setShield(int sl) {
		shieldLevel = sl;
		fireEquipEvent();
	}

	/**
	 * Set the background
	 * @param b
	 */
	public void setBackground(int b) {
		bg = b;
		repaint(); // shouldn't need a new event
	}
	// end of equipment

	/**
	 * Draw every sprite
	 */
	public void paint(Graphics g) {
		// draw background
		int bgOffset = (zoom-1) * -7;
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(zoom, zoom);
		g2.drawImage(BGS[bg], bgOffset, bgOffset, null);
		// Catch null frames
		if (frames==null || frames[frame] == null) {
			return;
		}
		// catch other errors
		try {
			int scaleOffset = (8 - zoom) * 7 + 10;
			Anime t = frames[frame];
			t.draw(g2, scaleOffset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Change listeners
	 */
	/**
	 * Step listeners look for frame advances.
	 * @param s
	 */
	public synchronized void addStepListener(StepListener s) {
		stepListen.add(s);
	}

	public synchronized void fireStepEvent() {
		StepEvent s = new StepEvent(this);
		Iterator<StepListener> listening = stepListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Speed listeners look for changes in speed.
	 * @param s
	 */
	public synchronized void addSpeedListener(SpeedListener s) {
		speedListen.add(s);
	}

	public synchronized void fireSpeedEvent() {
		SpeedEvent s = new SpeedEvent(this);
		Iterator<SpeedListener> listening = speedListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Mode listeners look for changes in animation mode.
	 * @param s
	 */
	public synchronized void addModeListener(ModeListener s) {
		modeListen.add(s);
	}

	public synchronized void fireModeEvent() {
		ModeEvent s = new ModeEvent(this);
		Iterator<ModeListener> listening = modeListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Zoom listeners look for changes in zoom level.
	 * @param s
	 */
	public synchronized void addZoomListener(ZoomListener s) {
		zoomListen.add(s);
	}

	public synchronized void fireZoomEvent() {
		ZoomEvent s = new ZoomEvent(this);
		Iterator<ZoomListener> listening = zoomListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Equipment listeners look for changes in display:
	 * <ul>
	 * <li>Mail</li>
	 * <li>Sword</li>
	 * <li>Shield</li>
	 * <li>Equipped item</li>
	 * <li>Shadows</li>
	 * </ul>
	 * All these categories may require new sprites to be added to each frame,
	 * and as such should also prompt a "hard reset" from the listener.
	 * @param s
	 */
	public synchronized void addEquipListener(EquipListener s) {
		equipListen.add(s);
	}

	private synchronized void fireEquipEvent() {
		EquipEvent s = new EquipEvent(this);
		Iterator<EquipListener> listening = equipListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Makes an array of {@link Sprite}s based on the frame data.
	 * <br><br>
	 * Complex, pain in the ass function.
	 */
	// @link Sprite - lol get it?
	private void makeAnimationFrames() {
		// break if we have no images
		if (mailImages == null) {
			return;
		}

		// find which sheet to use for image
		BufferedImage sheet;

		// Raw data, without spaces, capitalized for comparing
		String animData = ALLFRAMES[anime].toUpperCase().replace(" ", ""); // CAPS and remove all whitespace

		// split into sections
		String[] animDataX = animData.split("[\\[\\]]+");

		// for rebuilding and reorganizing frame data
		String[] eachFrameRaw = animDataX[2].split(";"); // split by frame
		maxFrame = eachFrameRaw.length;
		String[] eachFrameBuilt = new String[maxFrame+1]; // +1 for a ghost
		String[] eachFrameRebuilt = new String[maxFrame+1];
		String[] eachFrame;

		// remove unnecessary sprites from each frame
		for (int i = 0; i < maxFrame; i++) {
			String[] wholeFrame = eachFrameRaw[i].split("@");
			String frameBuilt;
			String[] eachSprite = wholeFrame[0].split(":");
			String[] frameSprites = new String[eachSprite.length];
			int sprct = 0;
			// remove sprites we don't want
			// change equipment names
			String indexName;
			for (String fs : eachSprite) {
				indexName = stopAtNumber(fs);
				// check for shadow
				if (indexName.equalsIgnoreCase("SHADOW")) {
					if (showShadow) {
						frameSprites[sprct] = fs;
						sprct++;
					} // otherwise do nothing
				// check for equipment
				} else if (isEquipment(indexName)) {
					if (showEquipment) {
						frameSprites[sprct] = fs;
						sprct++;
					} // otherwise do nothing
				// check and replace for sword
				} else if (indexName.equalsIgnoreCase("SWORD")) {
					switch (swordLevel) {
						default : // catch all
						case 0 : // no sword
							// do nothing
							break;
						case 1 : // fighter sword
						case 2 : // master sword
						case 3 : // tempered sword
						case 4 : // butter sword
							frameSprites[sprct] = (SWORDPREFIX[swordLevel] + fs);
							sprct++;
							break;
					}
				// check and replace for shield
				} else if (indexName.equalsIgnoreCase("SHIELD")) {
					switch (shieldLevel) {
						default : // catch all
						case 0 : // no shield
							// do nothing
							break;
						case 1 : // fighter shield
						case 2 : // red shield
						case 3 : // mirror shield
							frameSprites[sprct] = (SHIELDPREFIX[shieldLevel] + fs);
							sprct++;
							break;
					}
				} else { // everything else can be added
					frameSprites[sprct] = fs;
					sprct++;
				}
			} // end of each sprite
			// add duration to frame raw
			String[] usedSprites = new String[sprct];
			for (int h = 0; h < sprct; h++) {
				usedSprites[h] = frameSprites[h];
			}
			frameBuilt = GUIHelpers.join(usedSprites, ":");
			frameBuilt += ("@" + wholeFrame[1]);
			eachFrameBuilt[i] = frameBuilt;
		}

		// add a ghost frame to halt duplicate process (it won't be added)
		eachFrameBuilt[maxFrame] = "GARBAGE0@0";
		// try to remove duplicates by comparing frames
		// only do it for > 1 frame
		if (maxFrame == 1) { // for 1 frame animations, just copy the frame over
			eachFrame = new String[1];
			eachFrame[0] = eachFrameBuilt[0];
		} else {
			// for longer animations, prep the first frame
			String addFrame, curFrame;
			String addFrameData, curFrameData;
			int addFrameLength, curFrameLength;
			addFrame = eachFrameBuilt[0];
			addFrameData = addFrame.substring(0, addFrame.indexOf('@'));
			addFrameLength = Integer.parseInt(
					addFrame.substring(addFrame.indexOf('@') + 1)
					);
			int builtFramesCount = 0;

			for (int n = 1; n < maxFrame+1; n++) {
				curFrame = eachFrameBuilt[n];
				curFrameData = curFrame.substring(0, curFrame.indexOf('@'));
				curFrameLength = Integer.parseInt(
						curFrame.substring(curFrame.indexOf('@') + 1)
						);
				// compare the frames
				// if the data is the same
				// just increase the length of the frame to add
				if (curFrameData.equalsIgnoreCase(addFrameData)) {
					addFrameLength += curFrameLength;

				// if different
				// replace add with cur
				// add the frame to be added in
				} else {
					eachFrameRebuilt[builtFramesCount] = addFrameData + "@" + addFrameLength;
					addFrameData = curFrameData;
					addFrameLength = curFrameLength;
					builtFramesCount++;
				}
			} // end loop
			// build a final list of frames with removed dupes
			eachFrame = new String[builtFramesCount];
			for (int x = 0; x < builtFramesCount; x++) {
				eachFrame[x] = eachFrameRebuilt[x];
			}
		}

		// build each frame with sprites
		maxFrame = eachFrame.length;
		frames = new Anime[maxFrame];
		int spriteCount = 0;
		for (int i = 0; i < maxFrame; i++) {
			// each sprite in frame
			String[] wholeFrame = eachFrame[i].split("@");
			int animSpeed;
			try {
				animSpeed = Integer.parseInt(wholeFrame[1]);
			} catch (Exception e) {
				animSpeed = 100;
			}

			String[] eachSprite = wholeFrame[0].split(":");
			spriteCount = eachSprite.length;
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
					sheet = mailImages[mailLevel];
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
					case "XT" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 8;
						height = 24;
						break;
					case "XW" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 24;
						height = 8;
						break;
					case "XL" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 16;
						height = 24;
						break;
					case "XXL" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 32;
						height = 24;
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
	/**
	 * Wrapper for flip()
	 */
	private static BufferedImage flipV(BufferedImage image){
		return flip(image, true);
	}

	/**
	 * Wrapper for flip()
	 */
	private static BufferedImage flipH(BufferedImage image){
		return flip(image, false);
	}

	/**
	 * Flips an image vertically if the second argument is <b>true<b>
	 * or horizontally if it is <b>false</b>
	 * @param image
	 * @param vertical - orientation
	 * @return
	 */
	private static BufferedImage flip(BufferedImage image, boolean vertical) {
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

	/**
	 * Perform
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		GUI G = new GUI();
		G.printGUI(args);
	}

	/**
	 * Converts a named equipment index to a numbered row.
	 * @param n
	 */
	private static int getIndexRow(String n) {
		int ret = 0;
		if (n.length() == 1) {
			ret = ALPHA.indexOf(n);
		} else {
			switch (n) {
				case "FSWORD" : // fighter's sword
					ret = 0;
					break;
				case "MSWORD" : // master sword
					ret = 2;
					break;
				case "TSWORD" : // tempered sword
					ret = 4;
					break;
				case "BSWORD" : // butter sword
					ret = 6;
					break;
				case "SHADOW" : // right under butter
				case "BOOK" :
					ret = 7;
					break;
				case "FSHIELD" : // figher's shield
					ret = 8;
					break;
				case "RSHIELD" : // red shield
					ret = 9;
					break;
				case "MSHIELD" : // mirror shield
					ret = 10;
					break;
				case "SOMARIA" : // canes
				case "BYRNA" :
				case "CANE" :
					ret = 11;
					break;
				case "FIREROD" : // rods
				case "ICEROD" :
				case "ROD" :
					ret = 12;
					break;
				case "HAMMER" : // hammer
					ret = 13;
					break;
				case "HOOKSHOOOT" : // shooty shoots
				case "BOOMERANG" :
					ret = 14;
					break;
				case "NET" : // aga's weakness
					ret = 15;
					break;
				case "BOW" : // more shooty shoots
					ret = 16;
					break;
				case "SHOVEL" : // *plays minigame music*
					ret = 17;
					break;
				case "BED" : // this big fucker
					ret = 18;
					break;
			}
		}

		return ret;
	}

	/**
	 * Finds a substring that starts at index 0 and contains
	 * all non-number characters up until the first number character.
	 * @param n - Index name
	 */
	private static String stopAtNumber(String n) {
		String ret = "";
		char[] split = n.toCharArray();
		for (char c : split) {
			if (Character.getType(c) == Character.DECIMAL_DIGIT_NUMBER) {
				break;
			} else {
				ret += c;
			}
		}
		return ret;
	}

	/**
	 * Tests to see if the name passed is considered equipment.
	 * @param item - test case
	 */
	private static boolean isEquipment(String item) {
		boolean ret = false;
		for (String s : EQUIPNAMES) {
			if (item.equalsIgnoreCase(s)) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}