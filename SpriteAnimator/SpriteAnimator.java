package SpriteAnimator;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;

import SpriteAnimator.Database.Animation;
import SpriteAnimator.Database.SpriteData;
import SpriteAnimator.Database.StepData;
import SpriteAnimator.Listeners.*;

public class SpriteAnimator extends Component {
	// version and serial
	public static final String VERSION = "v1.5";
	private static final long serialVersionUID = 2114886855236406900L;

	private BufferedImage EQUIPMENT; {
		try {
			EQUIPMENT = ImageIO.read(SpriteAnimator.class.getResourceAsStream(
					"images/equipment.png"));
		} catch (IOException e) {
	}};

	// Almost the length of a frame at 60 FPS
	// 1/60 approx. 16.66666...
	public static final int FPS = 17;

	// used for parsing frame data
	static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZαβ"
			.toUpperCase(); // toUppercase to visually distinguish alpha/beta

	// equipment names and prefixes
	static final String[] EQUIPNAMES = {
			"CANE", "BYRNA", "SOMARIA", "BLOCK", "SPARKLE",
			"ROD", "FIREROD", "ICEROD", "POWDER", "ITEMSHADOW",
			"HAMMER", "NET", "HOOKSHOT", "BOOMERANG", "BUSH",
			"BOW", "BOOK", "PENDANT", "CRYSTAL", "SHOVEL", "BED", "DUCK"
	};

	// (default: Fighter), Fighter, Master, Tempered, Butter
	static final String[] SWORDPREFIX = { "F", "F", "M", "T", "B" };

	// (default: Fighter), Fighter, Red, Mirror
	static final String[] SHIELDPREFIX = { "F", "F", "R", "M" };

	// maximums
	private static final int MAXSPEED = 5; // maximum speed magnitude
	private static final int MAXZOOM = 5; // maximum zoom level

	// locals
	private BufferedImage[] mailImages = null; // sprite sheet
	private Animation anime; // current animation
	private int speed; // speed; 0 = normal; positive = faster; negative = slower
	private int mode; // animation mode
	private int frame; // animation step (not 0 indexed)
	private int maxFrame; // highest animation step (not 0 indexed)
	private boolean running; // self-running status
	private int zoom = 3; // default zoom
	private Anime[] frames = null; // each frame of animation, as an object
	private Timer tick; // runs for steps
	private TimerTask next; // controls steps

	// display
	private Background bg = Background.EMPTY;
	private int posX = 0;
	private int posY = 0;
	private int mailLevel = 0;
	private int swordLevel = 0;
	private int shieldLevel = 0;
	private boolean showShadow = true;
	private boolean showEquipment = true;
	private boolean showNeutral = true;

	// change listeners
	private List<StepListener> stepListen = new ArrayList<StepListener>();
	private List<ModeListener> modeListen = new ArrayList<ModeListener>();
	private List<SpeedListener> speedListen = new ArrayList<SpeedListener>();
	private List<ZoomListener> zoomListen = new ArrayList<ZoomListener>();
	private List<RebuildListener> rebuildListen = new ArrayList<RebuildListener>();

	// default initialization
	public SpriteAnimator() {
		anime = Animation.STAND;
		speed = 0;
		mode = 0;
		frame = 0;
		maxFrame = 0;
		tick = new Timer();
		setRunning();
		addMouse();
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
	 * Current frame's sprite list
	 */
	public String getFrameInfo() {
		String ret = "";
		switch (mode) {
			case 0 :
			case 2 :
				ret = "Sprite information disabled in this mode.";
				break;
			case 1 :
				ret = frames[frame].printAll();
				break;
		}
		return ret;
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
	public void setAnimation(Animation a) {
		if (mailImages == null) {
			return;
		}
		anime = a;
		makeAnimationFrames();
		fireRebuildEvent();
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
	 * <li>{@code 0} - normal animation</li>
	 * <li>{@code 1} - step-by-step</li>
	 * <li>{@code 2} - all frames</li>
	 * </ul>
	 * @param m - mode 
	 */
	public void setMode(int m) {
		mode = m;
		setRunning();
		adjustTimer();
		fireModeEvent();
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
				running = false;
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
			resetTimer();
		} catch(Exception e) {};
		switch (mode) {
			case 0 :
				setRunning();
				resetFrame();
				break;
			case 1 :
				setRunning();
				resetFrame();
				break;
			case 2 :
				setRunning();
				resetFrame();
				break;
		}
		fireModeEvent();
		repaint();
	}

	/**
	 * Set mode to 1, allowing stepwise animation
	 */
	public void pause() {
		setMode(1);
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
	 * Reset the timer
	 */
	private void resetTimer() {
		tick.cancel();
		tick = new Timer();
		fireSpeedEvent();
	}

	/**
	 * Fires every event to refresh the GUI.
	 */
	public void firePurge() {
		fireStepEvent();
		fireSpeedEvent();
		fireModeEvent();
		fireZoomEvent();
		fireRebuildEvent();
	}

	/**
	 * Reset speed to 0
	 */
	@SuppressWarnings("unused")
	private void resetSpeed() {
		setSpeed(0);
	}

	/**
	 * Resets frame to 0
	 */
	private void resetFrame() {
		// forcing a step to get to 0 will fire step events and run animation functions
		frame = -1;
		step();
	}

	/**
	 * Control self-animation permission
	 */
	private void setRunning() {
		switch (mode) {
			case 0 :
				running = true;
				break;
			case 1 :
				running = false;
				break;
			case 2 :
				running = true;
				break;
		}
		if (running) {
			resetTimer();
		}
	}

	/**
	 * Get run status
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Increments step speed by 1
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
			zoom = 3; // but if we do, reset the zoom
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
	 * Switch equipment display status
	 */
	public void switchEquipment() {
		showEquipment = !showEquipment;
		fireRebuildEvent();
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
		fireRebuildEvent();
	}

	/**
	 * Return shadow display status
	 */
	public boolean shadowOn() {
		return showShadow;
	}

	/**
	 * Switch neutral display status
	 */
	public void switchNeutral() {
		showNeutral = !showNeutral;
		fireRebuildEvent();
	}

	/**
	 * Return neutral display status
	 */
	public boolean neutralOn() {
		return showNeutral;
	}

	/**
	 * Set mail level
	 * @param ml
	 */
	public void setMail(int ml) {
		mailLevel = ml;
		fireRebuildEvent();
	}

	/**
	 * Set sword level
	 * @param sl
	 */
	public void setSword(int sl) {
		swordLevel = sl;
		fireRebuildEvent();
	}

	/**
	 * Set shield level
	 * @param sl
	 */
	public void setShield(int sl) {
		shieldLevel = sl;
		fireRebuildEvent();
	}

	/**
	 * Set the background
	 * @param b
	 */
	public void setBackground(Background b) {
		bg = b;
		repaint(); // shouldn't need a new event
	}

	/**
	 * Draw every sprite
	 */
	public void paint(Graphics g) {
		// draw background
		int offsetX = (zoom-1) * -7;
		int offsetY = (zoom-1) * -7;
		Graphics2D g2 = (Graphics2D) g;

		g2.scale(zoom, zoom);

		g2.drawImage(bg.getImage(), offsetX, offsetY, null);
		// Catch null frames
		if (frames == null || frames[frame] == null) {
			return;
		}
		// catch other errors
		try {
			int scaleOffsetX = posX + offsetX;
			int scaleOffsetY = posY + offsetY;
			Anime t = frames[frame];
			t.draw(g2, scaleOffsetX, scaleOffsetY);
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

	public synchronized void removeStepListener(StepListener s) {
		stepListen.remove(s);
	}

	private synchronized void fireStepEvent() {
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

	private synchronized void fireSpeedEvent() {
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

	private synchronized void fireModeEvent() {
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

	private synchronized void fireZoomEvent() {
		ZoomEvent s = new ZoomEvent(this);
		Iterator<ZoomListener> listening = zoomListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Rebuild listeners look for changes in display:
	 * <ul>
	 * <li>Animation change</li>
	 * <li>Mail</li>
	 * <li>Sword</li>
	 * <li>Shield</li>
	 * <li>Misc sprites</li>
	 * <li>Shadows</li>
	 * </ul>
	 * All these categories may require new sprites to be added to each frame,
	 * and as such should also prompt a "hard reset" from the listener.
	 * @param s
	 */
	public synchronized void addRebuildListener(RebuildListener s) {
		rebuildListen.add(s);
	}

	private synchronized void fireRebuildEvent() {
		RebuildEvent s = new RebuildEvent(this);
		Iterator<RebuildListener> listening = rebuildListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Makes an array of {@link Sprite}s based on the frame data.
	 */
	// @link Sprite - lol get it?
	private void makeAnimationFrames() {
		// break if we have no images
		if (mailImages == null) {
			return;
		}

		// find which sheet to use for image
		BufferedImage sheet;
		ArrayList<StepData> config = anime.customizeMergeAndFinalize(
				swordLevel, shieldLevel, showShadow, showEquipment);
		frames = new Anime[config.size()];
		maxFrame = config.size();
		for (int i = 0; i < frames.length; i++) {
			StepData frameX = config.get(i);
			int sprCount = frameX.countSprites();
			Sprite[] list = new Sprite[sprCount];
			for (int j = 0; j < sprCount; j++) {
				SpriteData curSprite = frameX.getSprite(j);
				if (curSprite.row.isLinkPart) {
					if (curSprite.isZap) {
						sheet = mailImages[4];
					} else {
						sheet = mailImages[mailLevel];
					}
				} else {
					sheet = EQUIPMENT;
				}
				Sprite newSpr = new Sprite(sheet, curSprite);
				list[sprCount-j-1] = newSpr;
			}
			frames[i] = new Anime(frameX, list);
		}
	} // end makeAnimationFrames

	/**
	 * Used to add mouse listeners
	 */
	private final void addMouse() {
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent arg0) {
				moveToPoint(arg0.getPoint());
			}

			public void mousePressed(MouseEvent arg0) {
				moveToPoint(arg0.getPoint());
			}

			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});

		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent arg0) {
				moveToPoint(arg0.getPoint());
			}

			public void mouseMoved(MouseEvent arg0) {

			}});
	} // end addMouse

	/**
	 * Moves to a point
	 */
	private void moveToPoint(Point p) {
		posX = p.x / zoom;
		posY = p.y / zoom;
		System.out.println(String.format(
				"{%s, %s} : {%s, %s}",
				p.x, p.y, posX, posY
				));
		repaint();
	}
}