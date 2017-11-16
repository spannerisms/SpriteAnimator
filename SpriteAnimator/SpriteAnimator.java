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

	// set equipment image
	private static final BufferedImage EQUIPMENT;
	static {
		BufferedImage temp;
		try {
			temp = ImageIO.read(SpriteAnimator.class.getResourceAsStream(
					"/SpriteAnimator/images/equipment.png"));
		} catch (IOException e) {
			temp = new BufferedImage(192, 448, BufferedImage.TYPE_4BYTE_ABGR);
		}
		EQUIPMENT = temp;
	};

	// Almost the length of a frame at 60 FPS
	// 1/60 approx. 16.66666...
	public static final int FPS = 17;

	// background size
	private static final int BG_WIDTH = 224;
	private static final int BG_HEIGHT = 208;

	// maximums
	private static final int MAXSPEED = 5; // maximum speed magnitude
	private static final int MAXZOOM = 5; // maximum zoom level

	// locals
	private int speed; // speed; 0 = normal; positive = faster; negative = slower
	private int mode; // animation mode
	private int step; // animation step (not 0 indexed)
	private int maxStep; // highest animation step (not 0 indexed)
	private int zoom = 2; // default zoom
	private boolean running; // self-running status
	private Animation anime; // current animation
	private Anime[] steps = null; // each step of animation, as an object
	private BufferedImage[] mailImages = null; // sprite sheet
	private Timer tick; // runs for steps
	private TimerTask next; // controls steps

	// display
	private Background bg = Background.EMPTY;
	private int posX = 40;
	private int posY = 40;
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
		this.setFocusable(true);
		anime = Animation.STAND;
		speed = 0;
		mode = 0;
		step = 0;
		maxStep = 0;
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
	 * Current step of current animation, not 0-indexed
	 */
	public String getStep() {
		return "" + (step + 1);
	}

	/**
	 * Current step's sprite list
	 */
	public String getSpriteInfo() {
		String ret = "";
		switch (mode) {
			case 0 :
			case 2 :
				ret = "Sprite information disabled in this mode.";
				break;
			case 1 :
				ret = steps[step].printAll();
				break;
		}
		return ret;
	}

	/**
	 * Highest step of current animation, not 0-indexed
	 */
	public int maxStep() {
		return maxStep;
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
		makeAnimation();
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
	 * <li>{@code 2} - all steps</li>
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
	 * Step forward 1 animation step.
	 * Resets step to 0 if we reach the end in modes that loop.
	 * Stops running if we reach the end of the animation in "All steps" mode.
	 */
	public void step() {
		step++;
		if (step >= maxStep) {
			if (mode == 2) {
				step = maxStep-1;
				running = false;
			} else {
				step = 0;
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
				resetStep();
				break;
			case 1 :
				setRunning();
				resetStep();
				break;
			case 2 :
				setRunning();
				resetStep();
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
		makeAnimation();
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
	 * Resets step to 0
	 */
	private void resetStep() {
		// forcing a step to get to 0 will fire step events and run animation functions
		step = -1;
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
	 * Adjust timer based on speed and current step
	 */
	private void adjustTimer() {
		if (!running) {
			tick.cancel();
			return;
		}
		try {
			double speedM = speedFactor();
			long wait = steps[step].nextTick(speedM);
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

	/*
	 * When zoom level is higher than 2,
	 * the image doesn't fit in the window at lowest size
	 * This function tries to pad offset the image to
	 * put Link in sort of the middle
	 */
	private int offset(int i) {
		int offset = 0;
		if (zoom > 2) {
			offset = -i + 75 - 10 * (zoom - 1); // these numbers are random, because I'm stupid
			if (offset > 0) { // don't offset at all if the top left corner is at the origin
				offset = 0;
			}
		}
		return offset;
	}

	/**
	 * Draw every sprite
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(zoom, zoom); // zoom in
		int xOffset = offset(posX);
		int yOffset = offset(posY);
		g2.translate(xOffset, yOffset);

		// draw background
		g2.drawImage(bg.getImage(), 0, 0, null);

		// Catch null steps; but they shouldn't happen
		if (steps == null || steps[step] == null) {
			return;
		}
		// catch other errors
		try {
			Anime t = steps[step];
			t.draw(g2, posX, posY);
		} catch (Exception e) {
			e.printStackTrace(); // thread conflicts cause errors? idk
		}
	}

	/*
	 * Change listeners
	 */
	/**
	 * Step listeners look for step advances.
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
	 * <li>Misc. sprites</li>
	 * <li>Shadows</li>
	 * </ul>
	 * All these categories may require new sprites to be added to each step,
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
	 * Makes an array of {@link Sprite}s based on the step data.
	 */
	// @link Sprite - lol get it?
	private void makeAnimation() {
		if (mailImages == null) { // break if we have no images
			return;
		}

		// find which sheet to use for image
		BufferedImage sheet;
		ArrayList<StepData> config = anime.customizeMergeAndFinalize(
				swordLevel, shieldLevel, showShadow, showEquipment, showNeutral);
		steps = new Anime[config.size()];
		maxStep = config.size();

		for (int i = 0; i < steps.length; i++) { // for each animation step
			StepData stepX = config.get(i);
			int sprCount = stepX.countSprites();
			
			Sprite[] list = new Sprite[sprCount]; // list of sprites for step
			for (int j = 0; j < sprCount; j++) { // for each sprite in step
				SpriteData curSprite = stepX.getSprite(j);

				if (curSprite.row.isLinkPart) { // find image to use
					if (curSprite.isZap) {
						sheet = mailImages[4];
					} else {
						sheet = mailImages[mailLevel];
					}
				} else {
					sheet = EQUIPMENT;
				}

				Sprite newSpr = new Sprite(sheet, curSprite);
				list[sprCount-j-1] = newSpr; // put it in backwards to preserve z-order
			}

			steps[i] = new Anime(stepX, list);
		}
	} // end makeAnimation

	/**
	 * Used to add mouse listeners
	 */
	private final void addMouse() {
		this.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent arg0) {
				if (SpriteAnimator.this.hasFocus()) {
					moveToPoint(arg0.getPoint());
				} else {
					SpriteAnimator.this.requestFocus();
				}
			}

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});

		this.addMouseMotionListener(new MouseMotionListener() {
			final int WAIT_TIME = 4;
			int throttle = WAIT_TIME;

			public void mouseDragged(MouseEvent arg0) {
				if (SpriteAnimator.this.hasFocus()) {
					if (throttle == 0) {
						moveToPoint(arg0.getPoint());
						throttle = WAIT_TIME;
					} else {
						throttle--;
					}
				}
			}

			public void mouseMoved(MouseEvent arg0) {}
		});
	} // end addMouse

	/**
	 * Moves to a point
	 */
	private void moveToPoint(Point p) {
		posX = (p.x - 8) / zoom - offset(posX); // subtract 8 to use the middle of sprite
		posY = (p.y - 8) / zoom - offset(posY);

		if (posX > BG_WIDTH - 18) { // try to pad a little
			posX = BG_WIDTH - 18;
		} else if (posX < 4) {
			posX = 4;
		}

		if (posY > BG_HEIGHT - 24) { // try to pad a little
			posY = BG_HEIGHT - 24;
		} else if (posY < 4) {
			posY = 4;
		}

		repaint();
	}
}