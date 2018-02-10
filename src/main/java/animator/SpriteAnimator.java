package animator;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JLabel;

import animator.database.Animation;
import animator.database.SpriteData;
import animator.database.StepData;
import animator.gui.AnimatorGUI;
import spritemanipulator.SpriteManipulator;

public class SpriteAnimator extends JComponent {
	// version and serial
	private static final long serialVersionUID = 2114886855236406900L;

	// set equipment image
	private static final BufferedImage EQUIPMENT;
	static {
		BufferedImage temp;
		try {
			temp = ImageIO.read(SpriteAnimator.class.getResourceAsStream("/images/equipment.png"));
		} catch (IOException e) {
			temp = new BufferedImage(192, 448, BufferedImage.TYPE_4BYTE_ABGR);
		}
		EQUIPMENT = temp;
	};

	// frame info disabled object
	private static final JLabel NO_DISP = new JLabel("Sprite information disabled in this mode.");

	// Almost the length of a frame at 60 FPS
	// 1/60 approx. 16.66666...
	public static final int FPS = 17;

	// background size
	private static final int BG_WIDTH = 224;
	private static final int BG_HEIGHT = 208;

	private static enum Speed {
		B5 (10),
		B4 (25),
		B3 (33),
		B2 (50),
		B1 (66),
		NORMAL (100),
		P1 (120),
		P2 (150),
		P3 (200);

		final int val;
		final double mult;

		Speed(int v) {
			val = v;
			mult = (100D / v);
		}

	}

	private final static Speed[] SPEEDS = Speed.values();

	// maximums
	private static final int MAX_SPEED_N = SPEEDS.length - 1; // maximum speed index
	private static final int MIN_SPEED_N = 0; // minimum speed index
	private static final Speed MAX_SPEED = SPEEDS[MAX_SPEED_N];
	private static final Speed MIN_SPEED = SPEEDS[MIN_SPEED_N];
	private static final int MAX_ZOOM = 5;

	// locals
	private Speed speed = Speed.NORMAL;
	private int step; // animation step
	private int maxStep; // highest animation step
	private int zoom = 3; // default zoom
	private boolean running; // self-running status
	private String spriteName;
	private Animation anime; // current animation
	private AnimationMode mode;
	private Step[] steps = null; // each step of animation, as an object
	private BufferedImage[][] mailImages = null; // sprite sheet
	private Timer tick; // runs for steps
	private TimerTask next; // controls steps

	// display
	private Background bg = Background.EMPTY;
	private int posX = 40;
	private int posY = 40;
	private int mailLevel = 0;
	private int gloveLevel = 0;
	private int swordLevel = 0;
	private int shieldLevel = 0;
	private boolean showShadow = true;
	private boolean showEquipment = true;
	private boolean showNeutral = true;

	// default initialization
	public SpriteAnimator() {
		this.setFocusable(true);
		anime = Animation.STAND;
		speed = Speed.NORMAL;
		step = 0;
		maxStep = 0;
		mode = AnimationMode.PLAY;
		tick = new Timer();
		setRunning();
		addMouse();
		addKeys();
	}

	public String getSpriteName() {
		return spriteName;
	}

	/**
	 * Current step of current animation, not 0-indexed
	 */
	public String getStep() {
		return Integer.toString(step + 1);
	}

	/**
	 * Current step's sprite list
	 */
	public Component getSpriteInfo() {
		Component ret = null;
		switch (mode) {
			case PLAY :
			case ONCE :
				ret = NO_DISP;
				break;
			case STEP :
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
	public void setSprite(String name, BufferedImage[][] images) {
		spriteName = name;
		mailImages = images;
	}

	/**
	 * Get first image
	 */
	public BufferedImage getGreenMail() {
		if (mailImages == null) {
			return null;
		}
		return mailImages[0][0];
	}

	/**
	 * Set animation ID
	 * @param id
	 */
	public void setAnimation(Animation a) {
		if (mailImages == null) { return; }
		anime = a;
		makeAnimation();
		fireRebuildEvent();
		reset();
	}

	/**
	 * Current animation
	 */
	public Animation getAnimation() {
		return anime;
	}

	/**
	 * Get animation mode ID#
	 */
	public AnimationMode getMode() {
		return mode;
	}

	/**
	 * Set image mode and reset
	 * @param m - mode
	 */
	public void setMode(AnimationMode m) {
		mode = m;
		setRunning();
		adjustTimer();
		fireModeEvent();
	}

	/**
	 * Step forward 1 animation step.
	 */
	public void step() {
		step++;
		commitStep();
	}

	public void stepBack() {
		step--;
		commitStep();
	}

	/**
	 * Resets step to 0 if we reach the end in modes that loop.
	 * Stops running if we reach the end of the animation in "play once" mode.
	 */
	private void commitStep() {
		if (step >= maxStep) {
			if (mode == AnimationMode.ONCE) {
				step = maxStep-1;
				running = false;
			} else {
				step = 0;
			}
		} else if (step < 0) {
			step = maxStep-1;
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
			case PLAY :
				setRunning();
				resetStep();
				break;
			case STEP :
				setRunning();
				resetStep();
				break;
			case ONCE :
				setRunning();
				resetStep();
				break;
		}
		fireModeEvent();
		repaint();
	}

	/**
	 * Set mode to 1, allowing step-by-step animation
	 */
	public void pause() {
		setMode(AnimationMode.STEP);
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
	 * Fires every event to refresh the GUI
	 */
	public void firePurge() {
		fireStepEvent();
		fireSpeedEvent();
		fireModeEvent();
		fireZoomEvent();
		fireRebuildEvent();
	}

	/**
	 * Resets step to 0
	 */
	private void resetStep() {
		step = 0;
		commitStep();
	}

	/**
	 * Control self-animation permission
	 */
	private void setRunning() {
		switch (mode) {
			case PLAY :
			case ONCE :
				running = true;
				break;
			case STEP :
				running = false;
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
		setSpeed(+1);
	}

	/**
	 * Decrements step speed by 1
	 */
	public void slower() {
		setSpeed(-1);
	}

	/**
	 * Sets speed based on defined bounds
	 * @param s
	 */
	private void setSpeed(int s) {
		int curSpeed = speed.ordinal();
		int newSpeed = curSpeed + s;

		if (newSpeed < MIN_SPEED_N) {
			newSpeed = MIN_SPEED_N;
		} else if (newSpeed > MAX_SPEED_N) {
			newSpeed = MAX_SPEED_N;
		}

		if (curSpeed == newSpeed) {
			// do nothing
		} else if (newSpeed < curSpeed) {
			if (atMinSpeed()) {
				// do nothing
			} else {
				speed = SPEEDS[newSpeed];
			}
		} else if (newSpeed > curSpeed) {
			if (atMaxSpeed()) {
				// do nothing
			} else {
				speed = SPEEDS[newSpeed];
			}
		} else { // should never hit this
			speed = Speed.NORMAL; // but if we do, reset the speed
		}
		fireSpeedEvent();
	}

	/**
	 * Get the speed multiplier as a percentage
	 */
	public String getSpeedPercent() {
		return speed.val + "%";
	}

	/**
	 * Compares current step speed to maximum speed allowed
	 */
	public boolean atMaxSpeed() {
		return speed == MAX_SPEED;
	}

	/**
	 * Compares current step speed to minimum speed allowed
	 */
	public boolean atMinSpeed() {
		return speed == MIN_SPEED;
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
		return zoom >= MAX_ZOOM;
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
			long wait = steps[step].nextTick(speed.mult);
			next = new SpriteTask(this);
			tick.schedule(next, wait);
		} catch (Exception e) {
			// do nothing
			// errors seem to just let it keep going
		}
	}

	/**
	 * Set equipment display status
	 */
	public void setEquipment(boolean b) {
		if (showEquipment == b) { return; }
		showEquipment = b;
		fireRebuildEvent();
	}

	/**
	 * Return equipment display status
	 */
	public boolean equipmentOn() {
		return showEquipment;
	}

	/**
	 * Set shadow display status
	 */
	public void setShadow(boolean b) {
		if (showShadow == b) { return; }
		showShadow = b;
		fireRebuildEvent();
	}

	/**
	 * Return shadow display status
	 */
	public boolean shadowOn() {
		return showShadow;
	}

	/**
	 * Set neutral display status
	 */
	public void setNeutral(boolean b) {
		if (showNeutral == b) { return; }
		showNeutral = b;
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
		if (mailLevel == ml) { return; }
		mailLevel = ml;
		fireRebuildEvent();
	}

	/**
	 * Set sword level
	 * @param sl
	 */
	public void setSword(int sl) {
		if (swordLevel == sl) { return; }
		swordLevel = sl;
		fireRebuildEvent();
	}

	/**
	 * Set shield level
	 * @param sl
	 */
	public void setShield(int sl) {
		if (shieldLevel == sl) { return; }
		shieldLevel = sl;
		fireRebuildEvent();
	}


	/**
	 * Set lift level
	 * @param gl
	 */
	public void setGlove(int gl) {
		if (gloveLevel == gl) { return; }
		gloveLevel = gl;
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
	 * When zoom level is higher than 2,
	 * the image doesn't fit in the window at lowest size
	 * This function tries to pad offset the image to
	 * put Link in sort of the middle
	 */
	private int offset(int i) {
		int offset = 0;
		if (this.getWidth() < zoom * BG_WIDTH - 20 ||
				this.getHeight() < zoom * BG_HEIGHT - 20) {
			offset = -i + 100 - 5 * (zoom - 1); // these numbers are random, because I'm stupid
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
		if (steps == null || steps[step] == null) { return; }

		// catch other errors
		try {
			Step t = steps[step];
			t.draw(g2, posX, posY);
		} catch (Exception e) {
			e.printStackTrace(); // thread conflicts cause errors? idk
		}
	}

	/*
	 * Change listeners
	 */
	private List<AnimatorListener> stepListen = new ArrayList<AnimatorListener>();
	private List<AnimatorListener> modeListen = new ArrayList<AnimatorListener>();
	private List<AnimatorListener> speedListen = new ArrayList<AnimatorListener>();
	private List<AnimatorListener> zoomListen = new ArrayList<AnimatorListener>();
	private List<AnimatorListener> rebuildListen = new ArrayList<AnimatorListener>();

	/**
	 * Step listeners look for step advances
	 */
	public synchronized void addStepListener(AnimatorListener s) {
		stepListen.add(s);
	}

	public synchronized void removeStepListener(AnimatorListener s) {
		stepListen.remove(s);
	}

	private synchronized void fireStepEvent() {
		AnimatorEvent s = new AnimatorEvent(this);
		Iterator<AnimatorListener> listening = stepListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Speed listeners look for changes in speed
	 * @param s
	 */
	public synchronized void addSpeedListener(AnimatorListener s) {
		speedListen.add(s);
	}

	private synchronized void fireSpeedEvent() {
		AnimatorEvent s = new AnimatorEvent(this);
		Iterator<AnimatorListener> listening = speedListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Mode listeners look for changes in animation mode
	 * @param s
	 */
	public synchronized void addModeListener(AnimatorListener s) {
		modeListen.add(s);
	}

	private synchronized void fireModeEvent() {
		AnimatorEvent s = new AnimatorEvent(this);
		Iterator<AnimatorListener> listening = modeListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Zoom listeners look for changes in zoom level
	 * @param s
	 */
	public synchronized void addZoomListener(AnimatorListener s) {
		zoomListen.add(s);
	}

	private synchronized void fireZoomEvent() {
		AnimatorEvent s = new AnimatorEvent(this);
		Iterator<AnimatorListener> listening = zoomListen.iterator();
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
	public synchronized void addRebuildListener(AnimatorListener s) {
		rebuildListen.add(s);
	}

	private synchronized void fireRebuildEvent() {
		AnimatorEvent s = new AnimatorEvent(this);
		Iterator<AnimatorListener> listening = rebuildListen.iterator();
		while(listening.hasNext()) {
			(listening.next()).eventReceived(s);
		}
	}

	/**
	 * Makes an array of {@link Sprite}s based on the step data
	 */
	// @link Sprite - lol get it?
	private void makeAnimation() {
		if (mailImages == null) { return; }// break if we have no images

		// find which sheet to use for image
		BufferedImage sheet;
		ArrayList<StepData> config = anime.customizeMergeAndFinalize(
				swordLevel, shieldLevel, showShadow, showEquipment, showNeutral);
		steps = new Step[config.size()];
		maxStep = config.size();
		int gloveTemp = gloveLevel;

		if (anime == Animation.MAP_WORLD) { // accounts for the minor glitch where Link does not get glove colors on the map
			gloveTemp = 0;
		}

		for (int i = 0; i < steps.length; i++) { // for each animation step
			StepData stepX = config.get(i);
			int sprCount = stepX.countSprites();

			Sprite[] list = new Sprite[sprCount]; // list of sprites for step
			for (int j = 0; j < sprCount; j++) { // for each sprite in step
				SpriteData curSprite = stepX.getSprite(j);

				if (curSprite.row.isLinkPart) { // find image to use
					if (curSprite.isZap) {
						sheet = mailImages[4][0];
						gloveTemp = 0; // accounts for minor glitch where Link loses glove colors after zap palette
					} else {
						sheet = mailImages[mailLevel][gloveTemp];
					}
				} else {
					sheet = EQUIPMENT;
				}

				Sprite newSpr = new Sprite(sheet, curSprite);
				list[sprCount-j-1] = newSpr; // put it in backwards to preserve z-order
			}

			steps[i] = new Step(stepX, list);
		}
	} // end makeAnimation

	/**
	 * Add mouse for movement
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
			final int WAIT_TIME = 3;
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
	 * Add keys for precise movement
	 */
	private final void addKeys() {
		this.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				int movement = e.isShiftDown() ? 5 : 1;

				switch (key) {
					case KeyEvent.VK_UP :
						posY -= movement;
						adjustPosition();
						break;
					case KeyEvent.VK_DOWN :
						posY += movement;
						adjustPosition();
						break;
					case KeyEvent.VK_RIGHT :
						posX += movement;
						adjustPosition();
						break;
					case KeyEvent.VK_LEFT :
						posX -= movement;
						adjustPosition();
						break;
				}
			} //end keyPressed
		});
	} // end addKeys

	/**
	 * Moves to a point
	 */
	private void moveToPoint(Point p) {
		posX = (p.x / zoom) - 8 - offset(posX); // subtract 8 to use the middle of sprite
		posY = (p.y / zoom) - 8 - offset(posY);

		adjustPosition();
	}

	/**
	 * Adjust position to within valid bounds and repaint
	 */
	private static final int ORIGIN_PADDED = 4;
	private static final int POS_X_PAD = 18;
	private static final int POS_Y_PAD = 24;

	private void adjustPosition() {
		if (posX > BG_WIDTH - POS_X_PAD) { // try to pad a little
			posX = BG_WIDTH - POS_X_PAD;
		} else if (posX < ORIGIN_PADDED) {
			posX = ORIGIN_PADDED;
		}

		if (posY > BG_HEIGHT - POS_Y_PAD) { // try to pad a little
			posY = BG_HEIGHT - POS_Y_PAD;
		} else if (posY < ORIGIN_PADDED) {
			posY = ORIGIN_PADDED;
		}

		repaint();
	}

	// gif making
	private static final int GIF_PAD = 5;

	public String makeGif(int size, int speed, boolean crop) throws Exception {
		if (steps == null) { throw new Exception(); }

		String dir = AnimatorGUI.GIF_DIRECTORY.getAbsolutePath();
		String[] fNameParts = spriteName.split("[/\\\\]");
		String spriteFileName = fNameParts[fNameParts.length-1];
		File cDir = new File(String.format("%s/%s", dir, spriteFileName));

		if (cDir.exists()) {
			if (cDir.isDirectory()) {
				dir = cDir.getPath();
			} else {
				throw new Exception("/" + cDir.getName() + " is not a folder.");
			}
		} else {
			cDir.mkdirs();
			dir = cDir.getPath();
		}

		BufferedImage cur;
		Graphics2D g;

		final int X = posX;
		final int Y = posY;
		final BufferedImage BG;

		if (bg == Background.EMPTY) {
			BG = Background.WHITE.getImage();
		} else {
			BG = bg.getImage();
		}

		// draw images
		BufferedImage[] images = new BufferedImage[steps.length];

		int gifWidth = BG_WIDTH;
		int gifHeight = BG_HEIGHT;

		// draw images
		if (crop) {
			ImageSize is = new ImageSize(anime);
			gifWidth = is.canvasX + (GIF_PAD * 2);
			gifHeight = is.canvasY + (GIF_PAD * 2);
			int offsetX = 0 - is.minX;
			int offsetY = 0 - is.minY;

			for (int i = 0; i < steps.length; i++) {
				Step a = steps[i];
				cur = new BufferedImage(gifWidth, gifHeight, BufferedImage.TYPE_4BYTE_ABGR);
				g = cur.createGraphics();
				g.drawImage(BG, -X + GIF_PAD + offsetX, -Y + GIF_PAD + offsetY, null);
				a.draw(g, offsetX + GIF_PAD, offsetY + GIF_PAD);
				images[i] = cur;
			}
		} else {
			for (int i = 0; i < steps.length; i++) {
				Step a = steps[i];
				cur = new BufferedImage(BG_WIDTH, BG_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

				g = cur.createGraphics();
				g.drawImage(BG, 0, 0, null);
				a.draw(g, X, Y);
				images[i] = cur;
			}
		}

		// combine remove pixels that are the same, starting backwards
		BufferedImage[] newImages = new BufferedImage[steps.length];
		newImages[0] = images[0]; // background stays the same

		for (int i = steps.length - 1; i > 0; i--) {
			byte[] curRaster = SpriteManipulator.getImageRaster(images[i]);
			byte[] nextRaster = SpriteManipulator.getImageRaster(images[i-1]);

			pixelCompare :
			for (int j = 0; j < curRaster.length; j += 4) {

				for (int k = 0; k < 4; k++) { // check each pixel
					if (curRaster[j+k] != nextRaster[j+k]) {
						continue pixelCompare;
					}
				}

				for (int k = 0; k < 4; k++) { // overwrite pixels
					curRaster[j+k] = 0;
				}
			}

			// set image
			cur = new BufferedImage(gifWidth, gifHeight, BufferedImage.TYPE_4BYTE_ABGR);
			int[] rgb = new int[gifWidth * gifHeight];

			for (int v = 0, j = 0; v < rgb.length; v++) {
				int a1 = curRaster[j++] & 0xFF;
				int b1 = curRaster[j++] & 0xFF;
				int g1 = curRaster[j++] & 0xFF;
				int r1 = curRaster[j++] & 0xFF;
				rgb[v] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
			}

			cur.setRGB(0, 0, gifWidth, gifHeight, rgb, 0, gifWidth);
			newImages[i] = cur;
		}

		// images to frames
		AnimatedGIFWriter writer = new AnimatedGIFWriter(false);
		AnimatedGIFWriter.GIFFrame[] frames = new AnimatedGIFWriter.GIFFrame[steps.length];

		for (int i = 0; i < steps.length; i++) {
			BufferedImage a = newImages[i];
			int l = steps[i].getLength();
			cur = new BufferedImage(gifWidth * size, gifHeight * size, BufferedImage.TYPE_4BYTE_ABGR);

			g = cur.createGraphics();
			g.scale(size, size);
			g.drawImage(a, 0, 0, null);

			frames[i] = new AnimatedGIFWriter.GIFFrame(cur, l * FPS * speed,
					AnimatedGIFWriter.GIFFrame.DISPOSAL_LEAVE_AS_IS);
		}

		String path = String.format(
				"%s/%s (x%s zoom; %s%% speed).gif",
				dir,
				anime.toString(),
				size,
				100 / speed
			);
		path = path.replace("/", System.getProperty("file.separator"));
		File f = new File(path);
		System.out.println("Creating: " + f.getAbsolutePath());
		f.createNewFile();

		FileOutputStream output = new FileOutputStream(f);
		writer.writeAnimatedGIF(frames, output);

		return f.getPath();
	} // end gif make

	// Crossproduct tracker images
	private static final int CP_MAX = 128;
	private static final int CP_ZOOM = 4;
	private static final int CP_X = 0;
	private static final int CP_Y = 0;

	private static enum CP_FILE {
		G_SALUT ("tunic1", Animation.SALUTE_SAVE, 0),
		B_SALUT ("tunic2", Animation.SALUTE_SAVE, 1),
		R_SALUT ("tunic3", Animation.SALUTE_SAVE, 2),
		BUNNY_G ("tunicbunny1", Animation.BUNNY_STAND_DOWN, 3),
		BUNNY_B ("tunicbunny2", Animation.BUNNY_STAND_DOWN, 1),
		BUNNY_R ("tunicbunny3", Animation.BUNNY_STAND_DOWN, 2);

		final String fileName;
		final Animation pose;
		final int mail;

		CP_FILE (String fileName, Animation pose, int mail) {
			this.fileName = fileName;
			this.pose = pose;
			this.mail = mail;
		}
	}

	public String makeCrossproduct() throws Exception {
		if (mailImages == null) { throw new Exception(); }

		long time = System.currentTimeMillis();
		String dir = AnimatorGUI.CROSSPRODUCT_DIRECTORY.getAbsolutePath();
		String[] fNameParts = spriteName.split("[/\\\\]");
		String spriteFileName = fNameParts[fNameParts.length-1];
		File cDir = new File(String.format("%s/%s (%s)", dir, spriteFileName, time));

		if (cDir.exists()) {
			if (cDir.isDirectory()) {
				dir = cDir.getPath();
			} else {
				throw new Exception("/" + cDir.getName() + " is not a folder.");
			}
		} else {
			cDir.mkdirs();
			dir = cDir.getPath();
		}

		for (CP_FILE c : CP_FILE.values()) {
			/*
			 * Makes the sprite image
			 * Mostly copied from makeAnimation()
			 */
			BufferedImage sheet = mailImages[c.mail][0];
			ArrayList<StepData> config = c.pose.customizeMergeAndFinalize(0, 0, false, false, false);
			StepData stepX = config.get(0);
			int sprCount = stepX.countSprites();
			Sprite[] list = new Sprite[sprCount]; // list of sprites for pose

			for (int j = 0; j < sprCount; j++) { // for each sprite in step
				SpriteData curSprite = stepX.getSprite(j);
				Sprite newSpr = new Sprite(sheet, curSprite);
				list[sprCount-j-1] = newSpr; // put it in backwards to preserve z-order
			}

			Step pose = new Step(stepX, list); // we only care about this first step
			// end of copied code

			BufferedImage spriteImg = new BufferedImage(CP_MAX, CP_MAX, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D sprg = spriteImg.createGraphics();
			sprg.scale(CP_ZOOM, CP_ZOOM);
			pose.draw(sprg, 3, 3);

			BufferedImage output = new BufferedImage(CP_MAX, CP_MAX, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D imgg = output.createGraphics();
			imgg.drawImage(spriteImg, 16, 16, null);

			String path = String.format("%s/%s.png", dir, c.fileName);
			path = path.replace("/", System.getProperty("file.separator"));
			File f = new File(path);
			System.out.println("Creating: " + f.getAbsolutePath());
			f.createNewFile();

			try(FileOutputStream wr = new FileOutputStream(f)) {
				ImageIO.write(output, "png", wr); // try to write image
				wr.close();
			}
		} // end of file loop

		return dir;
	} // end tracker images

	// collages
	private static final int COLLAGE_PAD = 1;

	public String makeCollage() throws Exception {
		if (mailImages == null) { throw new Exception(); }

		String dir = AnimatorGUI.MIKES_DIRECTORY.getAbsolutePath();
		String[] fNameParts = spriteName.split("[/\\\\]");
		String spriteFileName = fNameParts[fNameParts.length-1];
		File cDir = new File(String.format("%s/%s", dir, spriteFileName));

		if (cDir.exists()) {
			if (cDir.isDirectory()) {
				dir = cDir.getPath();
			} else {
				throw new Exception("/" + cDir.getName() + " is not a folder.");
			}
		} else {
			cDir.mkdirs();
			dir = cDir.getPath();
		}

		// get mins and maxes
		ImageSize is = new ImageSize(anime);

		// calculate size of draw area sans padding
		int drawX = is.canvasX;
		int drawY = is.canvasY;

		// calculate drawing offset
		int offsetX = 0 - is.minX;
		int offsetY = 0 - is.minY;

		// calculate size of collage
		int imageX = (drawX + 2 * COLLAGE_PAD) * maxStep;
		int imageY = drawY + 2 * COLLAGE_PAD;
		BufferedImage collageImg = new BufferedImage(imageX, imageY, BufferedImage.TYPE_4BYTE_ABGR);

		// create animation steps
		Graphics2D g = (Graphics2D) collageImg.getGraphics();
		for (int i = 0; i < maxStep; i++) {
			Step a = steps[i];
			int x = COLLAGE_PAD + (i * (drawX + COLLAGE_PAD));
			a.draw(g, x + offsetX, offsetY + COLLAGE_PAD);
		}

		String path = String.format("%s/%s.png", dir, anime.toString());
		path = path.replace("/", System.getProperty("file.separator"));
		File f = new File(path);
		System.out.println("Creating: " + f.getAbsolutePath());
		f.createNewFile();

		try(FileOutputStream wr = new FileOutputStream(f)) {
			ImageIO.write(collageImg, "png", wr); // try to write image
			wr.close();
		}

		return f.getAbsolutePath();
	} // end collage

	private class ImageSize {
		final int minX, maxX, minY, maxY;
		final int canvasX, canvasY;

		ImageSize(Animation a) {
			// recreate the config info so we can find min and max draw points
			ArrayList<StepData> config = a.customizeMergeAndFinalize(
					swordLevel, shieldLevel, showShadow, showEquipment, showNeutral);
			int minDX, minDY, maxDX, maxDY;
			minDX = minDY = 999;
			maxDX = maxDY = -999;
			for (StepData sd : config) {
				for (SpriteData spd : sd.getSprites()) {
					int curX = spd.x;
					int curY = spd.y;
					if (curX > maxDX) {
						maxDX = curX;
					}
					if (curX < minDX) {
						minDX = curX;
					}
					if (curY > maxDY) {
						maxDY = curY;
					}
					if (curY < minDY) {
						minDY = curY;
					}
				} // end sprite data
			} // end step data
			minX = minDX;
			maxX = maxDX;
			minY = minDY;
			maxY = maxDY;
			canvasX = maxX - minX + 16;
			canvasY = maxY - minY + 16;
		}
	}
}