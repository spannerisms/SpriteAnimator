package animator.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import spritemanipulator.*;
import animator.AnimationMode;
import animator.AnimatorListener;
import animator.Background;
import animator.SpriteAnimator;
import animator.gui.cellsearch.CellFrame;
import animator.database.*;

import static javax.swing.SpringLayout.*;
import static animator.gui.ButtonAction.*;

public class AnimatorGUI {
	public static final String VERSION;

	private static final String VERSION_PATH = "/version";
	private static final String VERSION_URL = "https://raw.githubusercontent.com/fatmanspanda/SpriteAnimator/master/version";
	private static final boolean VERSION_GOOD;

	static {
		String line = "v0.0";
		try (
				BufferedReader br = new BufferedReader(new InputStreamReader(
						AnimatorGUI.class.getResourceAsStream(VERSION_PATH),
						StandardCharsets.UTF_8)
				);
			) {
				line = br.readLine();
			} catch (Exception e) {
				e.printStackTrace();
			}
		VERSION = line;
		System.out.println("Current version: " + VERSION);
		VERSION_GOOD = amIUpToDate();
		System.out.println("Up to date: " + VERSION_GOOD);
	}

	private static final String WIKI_LINK = "https://github.com/fatmanspanda/ALttPNG/wiki";
	private static final String UPDATES_LINK = "https://github.com/fatmanspanda/ALttPNG/releases/latest";

	private static final String[] ACCEPTED_FILE_TYPES = new String[] {
			ZSPRFile.EXTENSION,
			"sfc",
			"png"
		};

	private static final String[] MAIL_LEVELS = {
			"No mail",
			"Green mail",
			"Blue mail",
			"Red mail",
			"Bunny"
		};

	private static final String[] SWORD_LEVELS = {
			"No sword",
			"Fighter's sword",
			"Master sword",
			"Tempered sword",
			"Butter sword"
		};

	private static final String[] SHIELD_LEVELS = {
			"No shield",
			"Fighter's shield",
			"Red shield",
			"Mirror shield"
		};

	public static final String[] GLOVE_LEVELS = {
			"No glove",
			"Power glove",
			"Titan's mitt"
		};

	private static final Animation[] ALL_ANIMATIONS = Animation.values();

	private static final int GAP = 5;
	private static final int BLANK_HEIGHT = 10;

	// Animator's folder
	public static final boolean JAR_DIR_FOUND;
	public static final File JAR_DIRECTORY;
	public static final File GIF_DIRECTORY;
	public static final File CROSSPRODUCT_DIRECTORY;
	public static final File MIKES_DIRECTORY;
	public static final File VT_DIRECTORY;

	static {
		new VTGrabber();
		File temp;

		try {
			temp = new File(AnimatorGUI.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (Exception e) {
			temp = null;
			System.out.println("Can't find a folder to use.");
		}

		JAR_DIRECTORY = new File(temp.getParent());
		JAR_DIR_FOUND = temp != null;

		if (!JAR_DIR_FOUND) {
			GIF_DIRECTORY = null;
			CROSSPRODUCT_DIRECTORY = null;
			MIKES_DIRECTORY = null;
			VT_DIRECTORY = null;
		} else {
			String folderPath = JAR_DIRECTORY.getAbsolutePath();
			GIF_DIRECTORY = new File(
					String.format("%s%s%s", folderPath, System.getProperty("file.separator"), "/gifs"));
			CROSSPRODUCT_DIRECTORY = new File(
					String.format("%s%s%s", folderPath, System.getProperty("file.separator"), "/CPTrackerImages"));
			MIKES_DIRECTORY = new File(
					String.format("%s%s%s", folderPath, System.getProperty("file.separator"), "/collages"));
			VT_DIRECTORY = new File(
					String.format("%s%s%s", folderPath, System.getProperty("file.separator"), "/VT"));
		}
	}

	public static void main(String[] args) throws IOException {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					printGUI();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// use func
	@SuppressWarnings("serial")
	private static void printGUI() throws IOException {
		// try to set LaF
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e2) {
			// do nothing
		} //end System

		ToolTipManager.sharedInstance().setInitialDelay(100);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // 596:31:23.647

		JFrame frame = new JFrame("Sprite Animator " + VERSION);
		Dimension d = new Dimension(1000, 750);
		Border rightPad = BorderFactory.createEmptyBorder(0, 0, 0, GAP);
		Border fullPad = BorderFactory.createEmptyBorder(3, 3, 3, 3);
		Dimension textDimension = new Dimension(50, 20);

		// layout
		JPanel fullWrap = (JPanel) frame.getContentPane();
		SpringLayout l = new SpringLayout();
		fullWrap.setLayout(l);

		// keyboard controls
		InputMap keysss = fullWrap.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap doThings = fullWrap.getActionMap();

		// control panel
		JPanel controls = new JPanel(new GridBagLayout());
		controls.setBorder(fullPad);
		GridBagConstraints c = new GridBagConstraints();

		l.putConstraint(EAST, controls, -GAP, EAST, fullWrap);
		l.putConstraint(WEST, controls, -250, EAST, fullWrap);
		l.putConstraint(NORTH, controls, GAP, NORTH, frame);
		frame.add(controls);

		// animation panel
		SpriteAnimator animated = new SpriteAnimator();
		animated.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		l.putConstraint(WEST, animated, GAP, WEST, fullWrap);
		l.putConstraint(EAST, animated, -GAP, WEST, controls);
		l.putConstraint(NORTH, animated, GAP, NORTH, fullWrap);
		l.putConstraint(SOUTH, animated, -GAP, SOUTH, fullWrap);
		l.putConstraint(SOUTH, animated, -GAP, SOUTH, fullWrap);
		fullWrap.add(animated);

		// controls formatting
		// negative so everything can just ++
		c.gridy = -1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.BOTH;

		// image loading
		JTextField fileName = new JTextField("");
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 3;
		controls.add(fileName, c);

		// load buttons
		PrettyButton loadBtn = new PrettyButton("Load sprite");
		PrettyButton reloadBtn = new PrettyButton("Reload");
		c.gridy++;
		c.gridx = 1;
		c.gridwidth = 1;
		controls.add(loadBtn, c);
		c.gridx = 2;
		c.gridwidth = 1;
		controls.add(reloadBtn, c);

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// animation playing
		JLabel animationLabel = new JLabel("Animation:", SwingConstants.RIGHT);
		PrettyBox<Animation> animOptions = new PrettyBox<Animation>(ALL_ANIMATIONS);
		animationLabel.setBorder(rightPad);
		c.gridheight = 2;
		c.gridy++;
		c.gridx = 0;
		controls.add(animationLabel, c);
		c.gridheight = 1;
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(animOptions, c);
		c.gridwidth = 1;

		// animation button
		PrettyButton animListBtn = new PrettyButton("As list"); // button for popup window here
		c.gridwidth = 2;
		c.gridy++;
		c.gridx = 1;
		controls.add(animListBtn, c);
		c.gridwidth = 1;

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// background
		PrettyBox<Background> bgDisp = new PrettyBox<Background>(Background.values());
		JLabel backgroundLabel = new JLabel("Background:", SwingConstants.RIGHT);
		backgroundLabel.setBorder(rightPad);
		c.gridy++;
		c.gridx = 0;
		controls.add(backgroundLabel, c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(bgDisp, c);

		JCheckBox animatedBG = new JCheckBox("Animate");
		c.gridy++;
		c.gridx = 2;
		c.gridwidth = 1;
		controls.add(animatedBG, c);

		animatedBG.addChangeListener(
			arg0 -> {
				animated.setBackgroundAnimated(animatedBG.isSelected());
			});
		animatedBG.setSelected(true);
		animatedBG.setFocusable(false);

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// gear label
		JLabel gearLabel = new JLabel("<html><b>Display:</b></html>", SwingConstants.RIGHT);
		gearLabel.setVerticalAlignment(SwingConstants.TOP);
		gearLabel.setBorder(rightPad);
		setToolTip(gearLabel,
				"Controls the level and display of the sword and shield, " +
				"and the current mail palette.",
				"These actions require a rebuild of the animation data, resetting the current step to 1.");
		c.gridy++;
		c.gridx = 0;
		controls.add(gearLabel, c);

		// gear
		JPanel gearControls = new JPanel(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		Image clearGear = ImageIO.read(AnimatorGUI.class.getResource("/images/meta/no-thing.png"));
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridy = -1;
		g.ipadx = 4;
		g.ipady = 4;

		// mails
		ButtonGroup mailChoice = new ButtonGroup();
		JLabel mailLabel = new JLabel("Mail:", SwingConstants.RIGHT);
		mailLabel.setBorder(rightPad);

		GearChange mailChange = (i) -> {
				animated.setMail(i);
				frame.repaint();
			};

		g.gridy++;
		g.gridx = 0;
		gearControls.add(mailLabel, g);
		g.gridx++;

		for (int i = 1; i < 5; i++) {
			Image mailXIco = ImageIO.read(AnimatorGUI.class.getResource("/images/meta/mail-" + i + ".png"));
			IconButton mailX = new IconButton(mailXIco);
			mailChoice.add(mailX);
			g.gridx++;
			gearControls.add(mailX, g);
			int a = i - 1;
			mailX.addActionListener(arg0 -> mailChange.go(a));
			mailX.setToolTipText(MAIL_LEVELS[i]);
			if (i == 1) {
				mailX.setSelected(true);
			}
		}

		// swords
		ButtonGroup swordChoice = new ButtonGroup();
		JLabel swordLabel = new JLabel("Sword:", SwingConstants.RIGHT);
		swordLabel.setBorder(rightPad);

		IconButton noSword = new IconButton(clearGear);
		swordChoice.add(noSword);

		GearChange swordChange = (i) -> {
				animated.setSword(i);
				frame.repaint();
			};
		noSword.addActionListener(arg0 -> swordChange.go(0));
		noSword.setSelected(true);
		noSword.setToolTipText(SWORD_LEVELS[0]);

		g.gridy++;
		g.gridx = 0;
		gearControls.add(swordLabel, g);
		g.gridx++;
		gearControls.add(noSword, g);

		for (int i = 1; i < 5; i++) {
			Image swordXIco = ImageIO.read(AnimatorGUI.class.getResource("/images/meta/sword-" + i + ".png"));
			IconButton swordX = new IconButton(swordXIco);
			swordChoice.add(swordX);
			g.gridx++;
			gearControls.add(swordX, g);
			int a = i;
			swordX.addActionListener(arg0 -> swordChange.go(a));
			swordX.setToolTipText(SWORD_LEVELS[i]);
		}

		// shields
		ButtonGroup shieldChoice = new ButtonGroup();
		JLabel shieldLabel = new JLabel("Shield:", SwingConstants.RIGHT);
		shieldLabel.setBorder(rightPad);

		IconButton noShield = new IconButton(clearGear);
		shieldChoice.add(noShield);

		GearChange shieldChange = (i) -> {
				animated.setShield(i);
				frame.repaint();
			};
		noShield.addActionListener(arg0 -> shieldChange.go(0));
		noShield.setSelected(true);
		noShield.setToolTipText(SHIELD_LEVELS[0]);

		g.gridy++;
		g.gridx = 0;
		gearControls.add(shieldLabel, g);
		g.gridx++;
		gearControls.add(noShield, g);

		for (int i = 1; i < 4; i++) {
			Image shieldXIco = ImageIO.read(AnimatorGUI.class.getResource("/images/meta/shield-" + i + ".png"));
			IconButton shieldX = new IconButton(shieldXIco);
			shieldChoice.add(shieldX);
			g.gridx++;
			gearControls.add(shieldX, g);
			int a = i;
			shieldX.addActionListener(arg0 -> shieldChange.go(a));
			shieldX.setToolTipText(SHIELD_LEVELS[i]);
		}

		// gloves
		ButtonGroup glovesChoice = new ButtonGroup();
		JLabel glovesLabel = new JLabel("Glove:", SwingConstants.RIGHT);
		glovesLabel.setBorder(rightPad);

		IconButton noGloves = new IconButton(clearGear);
		glovesChoice.add(noGloves);

		GearChange glovesChange = (i) -> {
				animated.setGlove(i);
				frame.repaint();
			};
		noGloves.addActionListener(arg0 -> glovesChange.go(0));
		noGloves.setSelected(true);
		noGloves.setToolTipText(GLOVE_LEVELS[0]);

		g.gridy++;
		g.gridx = 0;
		gearControls.add(glovesLabel, g);
		g.gridx++;
		gearControls.add(noGloves, g);

		for (int i = 1; i < 3; i++) {
			Image glovesXIco = ImageIO.read(AnimatorGUI.class.getResource("/images/meta/gloves-" + i + ".png"));
			IconButton glovesX = new IconButton(glovesXIco);
			glovesChoice.add(glovesX);
			g.gridx++;
			gearControls.add(glovesX, g);
			int a = i;
			glovesX.addActionListener(arg0 -> glovesChange.go(a));
			glovesX.setToolTipText(GLOVE_LEVELS[i]);
		}

		c.gridy++;
		c.gridwidth = 3;
		controls.add(gearControls, c);
		c.gridwidth = 1;

		// blank
		g.gridy++;
		g.ipady = BLANK_HEIGHT;
		gearControls.add(new JLabel(), g);
		g.ipady = 0;

		// misc
		JLabel thePhraseMiscellaneousSpritesWithAColon = new JLabel("Misc. sprites:", SwingConstants.RIGHT);
		thePhraseMiscellaneousSpritesWithAColon.setBorder(rightPad);
		setToolTip(thePhraseMiscellaneousSpritesWithAColon,
				"When <b>miscellaneous sprites</b> are on, " +
						"animations that contain sprites other than the playable character " +
						"will display their relevant sprites.",
				"This option does not affect the display of swords, shields, or swag duck.");

		ButtonGroup miscChoice = new ButtonGroup();
		IconButton noMisc = new IconButton(clearGear);
		miscChoice.add(noMisc);
		Image miscIco = ImageIO.read(AnimatorGUI.class.getResource("/images/meta/misc-on.png"));
		IconButton yesMisc = new IconButton(miscIco);
		miscChoice.add(yesMisc);

		MiscChange miscChange = (b) -> {
			animated.setEquipment(b);
			frame.repaint();
		};
		noMisc.addActionListener(arg0 -> miscChange.go(false));
		yesMisc.addActionListener(arg0 -> miscChange.go(true));
		yesMisc.setSelected(true);

		g.gridy++;
		g.gridx = 0;
		gearControls.add(thePhraseMiscellaneousSpritesWithAColon, g);
		g.gridx = 1;
		gearControls.add(noMisc, g);
		g.gridx = 2;
		gearControls.add(yesMisc, g);

		// shadow
		JLabel shadowLabel = new JLabel("Shadows:", SwingConstants.RIGHT);
		shadowLabel.setBorder(rightPad);
		setToolTip(shadowLabel,
				"When <b>shadows</b> are on, " +
						"certain animations will display a shadow below the character sprite.",
				"This option will not affect shadows that belong to other sprites.");

		ButtonGroup shadowChoice = new ButtonGroup();
		IconButton noShadow = new IconButton(clearGear);
		shadowChoice.add(noShadow);
		Image shadowIco = ImageIO.read(AnimatorGUI.class.getResource("/images/meta/shadow-on.png"));
		IconButton yesShadow = new IconButton(shadowIco);
		shadowChoice.add(yesShadow);

		MiscChange shadowChange = (b) -> {
			animated.setShadow(b);
			frame.repaint();
		};
		noShadow.addActionListener(arg0 -> shadowChange.go(false));
		yesShadow.addActionListener(arg0 -> shadowChange.go(true));
		yesShadow.setSelected(true);

		g.gridy++;
		g.gridx = 0;
		gearControls.add(shadowLabel, g);
		g.gridx = 1;
		gearControls.add(noShadow, g);
		g.gridx = 2;
		gearControls.add(yesShadow, g);

		// neutral
		JLabel neutralLabel = new JLabel("Neutral poses:", SwingConstants.RIGHT);
		neutralLabel.setBorder(rightPad);
		setToolTip(neutralLabel,
				"When <b>neutral poses</b> are on, " +
						"certain animations will begin or end with the character sprite at a " +
						"neutral standing position.");

		ButtonGroup neutralChoice = new ButtonGroup();
		IconButton noNeutral = new IconButton(clearGear);
		neutralChoice.add(noNeutral);
		Image neutralIco = ImageIO.read(AnimatorGUI.class.getResource("/images/meta/neutral-on.png"));
		IconButton yesNeutral = new IconButton(neutralIco);
		neutralChoice.add(yesNeutral);

		MiscChange neutralChange = (b) -> {
			animated.setNeutral(b);
			frame.repaint();
		};
		noNeutral.addActionListener(arg0 -> neutralChange.go(false));
		yesNeutral.addActionListener(arg0 -> neutralChange.go(true));
		yesNeutral.setSelected(true);

		g.gridy++;
		g.gridx = 0;
		gearControls.add(neutralLabel, g);
		g.gridx = 1;
		gearControls.add(noNeutral, g);
		g.gridx = 2;
		gearControls.add(yesNeutral, g);

		// add gear
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 3;
		controls.add(gearControls, c);
		c.gridwidth = 1;

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT * 2;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// zoom
		JLabel zoomLevel = new JLabel("×-", SwingConstants.RIGHT);
		PrettyButton lilBtn = new PrettyButton("Zoom-");
		PrettyButton bigBtn = new PrettyButton("Zoom+");
		setAllSizes(zoomLevel, textDimension);

		zoomLevel.setBorder(rightPad);
		zoomLevel.setHorizontalTextPosition(SwingConstants.LEFT);
		ImageIcon zoomIco = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/zoom.png"));
		zoomLevel.setIcon(zoomIco);

		c.gridy++;
		c.gridx = 0;
		controls.add(zoomLevel, c);
		c.gridx = 1;
		controls.add(lilBtn, c);
		c.gridx = 2;
		controls.add(bigBtn, c);

		// speed
		PrettyButton fasterBtn = new PrettyButton("Speed+");
		PrettyButton slowerBtn = new PrettyButton("Speed-");
		JLabel speedLevel = new JLabel("--%", SwingConstants.RIGHT);
		setAllSizes(speedLevel, textDimension);

		speedLevel.setBorder(rightPad);
		speedLevel.setHorizontalTextPosition(SwingConstants.LEFT);
		ImageIcon speedIco = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/speed.png"));
		speedLevel.setIcon(speedIco);

		c.gridy++;
		c.gridx = 0;
		controls.add(speedLevel, c);
		c.gridx = 1;
		controls.add(slowerBtn, c);
		c.gridx = 2;
		controls.add(fasterBtn, c);

		// playing
		PrettyButton playBtn = new PrettyButton("Play");
		PrettyButton playOnceBtn = new PrettyButton("Play 1");
		PrettyButton resetBtn = new PrettyButton("Reset");

		ImageIcon playIco = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/play.png"));
		ImageIcon playOnceIco = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/play-once.png"));
		ImageIcon resetIco = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/reset.png"));

		playBtn.setIcon(playIco);
		playOnceBtn.setIcon(playOnceIco);
		resetBtn.setIcon(resetIco);

		c.gridy++;
		c.gridx = 0;
		controls.add(playBtn, c);
		c.gridx = 1;
		controls.add(playOnceBtn, c);
		c.gridx = 2;
		controls.add(resetBtn, c);

		// stepwise
		PrettyButton stepBackBtn = new PrettyButton("Step");
		PrettyButton pauseBtn = new PrettyButton("Pause");
		PrettyButton stepBtn = new PrettyButton("Step");

		ImageIcon stepBackIco = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/step-back.png"));
		ImageIcon stepForwardIco = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/step-forward.png"));
		ImageIcon pauseIco = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/pause.png"));

		stepBackBtn.setIcon(stepBackIco);
		stepBackBtn.setHorizontalTextPosition(SwingConstants.RIGHT);

		pauseBtn.setIcon(pauseIco);
		stepBtn.setIcon(stepForwardIco);

		c.gridy++;
		c.gridx = 0;
		controls.add(stepBackBtn, c);
		c.gridx = 1;
		controls.add(pauseBtn, c);
		c.gridx = 2;
		controls.add(stepBtn, c);
		c.gridwidth = 1;

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT * 2;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// step counter
		JLabel stepLabel = new JLabel("Step:", SwingConstants.RIGHT);
		JLabel stepCur = new JLabel("-", SwingConstants.RIGHT);
		JLabel stepMax = new JLabel("/ -");
		stepCur.setBorder(rightPad);
		stepMax.setBorder(rightPad);
		setAllSizes(stepCur, textDimension);

		c.gridy++;
		c.gridx = 0;
		controls.add(stepLabel, c);
		c.gridx = 1;
		controls.add(stepCur, c);
		c.gridx = 2;
		controls.add(stepMax, c);

		// sprite info
		JPanel spriteInfo = new JPanel();
		c.gridwidth = 3;
		c.gridy++;
		c.gridx = 0;
		controls.add(spriteInfo, c);

		// control panel done

		// animations as buttons
		AnimationListDialog animList =
				new AnimationListDialog(frame,
					arg0 -> {
						Object o = arg0.getSource();
						assert o instanceof AnimationListDialog.AnimButton;
						AnimationListDialog.AnimButton b = (AnimationListDialog.AnimButton) o;
						Animation a = b.anim;
						animated.setAnimation(a);
					});

		animListBtn.addActionListener(
			arg0 -> {
				if (animList.isVisible()) {
					animList.requestFocus();
				} else {
					animList.setLocation(animListBtn.getLocationOnScreen());
					animList.setVisible(true);
				}
			});

		// acknowledgements
		ImageIcon mapIcon = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/map.png"));
		JDialog aboutFrame = new JDialog(frame, "Acknowledgements");
		aboutFrame.setIconImage(mapIcon.getImage());

		TextArea peepsList = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		peepsList.setEditable(false);
		peepsList.append(String.format("Sprite Animator version %s\n", VERSION));
		peepsList.append(String.format("ALttPNG version %s\n\n", SpriteManipulator.ALTTPNG_VERSION));
		peepsList.append("Written by fatmanspanda"); // hey, that's me
		peepsList.append("\n\nAnimation research:\n");
		peepsList.append("http://alttp.mymm1.com/sprites/includes/animations.txt\n");
		peepsList.append(String.join(", ",
				new String[]{
						"MikeTrethewey", // it's mike
						"TWRoxas", // helped mike with his site
				}));
		peepsList.append("\n\nCode contribution:\n");
		peepsList.append(String.join(", ",
				new String[]{
						"MikeTrethewey", // God dammit, stop being so helpful
						"Zarby89", // spr conversion
						"Roxas232", // reload button
						"Hyphen-ated" // mavenizing shit
				}));
		peepsList.append("\n\nResources and development:\n");
		peepsList.append(String.join(", ",
				new String[]{
						"Veetorp", // documentation
						"Sosuke3", // various snes code answers
						"Fish" // Crossproduct tracker icon
				}));
		peepsList.append("\n\nTesting and feedback:\n");
		peepsList.append(String.join(", ",
				new String[]{
						"Jighart",
						"Feed4Fame"
				}));

		aboutFrame.add(peepsList);
		aboutFrame.setSize(600, 300);
		aboutFrame.setResizable(false);

		// end credits

		// menu
		JMenuBar menu = new JMenuBar();
		frame.setJMenuBar(menu);

		// file menu
		JMenu fileMenu = new JMenu("File");
		menu.add(fileMenu);

		// animated gifs
		JMenuItem giffer = new JMenuItem("Make GIF");
		ImageIcon cane = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/cane.png"));
		giffer.setIcon(cane);
		fileMenu.add(giffer);

		// crossproduct tracker images
		JMenuItem cross = new JMenuItem("Make tracker images");
		ImageIcon crossProductsFace = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/cross.png"));
		cross.setIcon(crossProductsFace);
		fileMenu.add(cross);

		// collages
		JMenuItem collage = new JMenuItem("Make collage");
		ImageIcon openBook = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/openbook.png"));
		collage.setIcon(openBook);
		fileMenu.add(collage);

		// separator
		fileMenu.addSeparator();

		// exit
		JMenuItem exit = new JMenuItem("Exit");
		ImageIcon mirror = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/mirror.png"));
		exit.setIcon(mirror);
		fileMenu.add(exit);
		exit.addActionListener(arg0 -> System.exit(0));

		// end file menu

		// tools menu
		JMenu toolsMenu = new JMenu("Tools");
		menu.add(toolsMenu);

		// reverse lookup
		CellFrame looker = new CellFrame(frame);
		JMenuItem lookUp = new JMenuItem("Sheet trawler");
		ImageIcon net = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/net.png"));
		looker.setIconImage(net.getImage());
		lookUp.setIcon(net);
		toolsMenu.add(lookUp);
		lookUp.addActionListener(arg0 -> looker.setVisible(true));

		// end tools menu

		// help menu
		JMenu helpMenu = new JMenu("Help");
		menu.add(helpMenu);

		// refresh VT sprites fold
		JMenuItem vtRefresh = new JMenuItem("Refresh live sprites");
		ImageIcon quack = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/vtduck.png"));
		vtRefresh.setIcon(quack);
		helpMenu.add(vtRefresh);

		// link to wiki
		JMenuItem wikiLink = new JMenuItem("ALttPNG wiki");
		ImageIcon shovel = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/shovel.png"));
		wikiLink.setIcon(shovel);
		helpMenu.add(wikiLink);

		wikiLink.addActionListener(
			arg0 -> {
				try {
					openLink(WIKI_LINK);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame,
							"It didn't work",
							"How to internet",
							JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				}
			});

		// look for updates
		JMenuItem updates = new JMenuItem("Check for updates");
		ImageIcon hammer = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/hammer.png"));
		updates.setIcon(hammer);
		helpMenu.add(updates);

		updates.addActionListener(
			arg0 -> {
				try {
					openLink(UPDATES_LINK);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(frame,
							"uhhh...",
							"How to internet",
							JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
				}
			});

		if (!VERSION_GOOD) {
			updates.setOpaque(true);
			updates.setBackground(Color.RED);
			updates.setForeground(Color.WHITE);
			updates.setText("Update available");

			helpMenu.setOpaque(true);
			helpMenu.setBackground(Color.RED);
			helpMenu.setForeground(Color.WHITE);
			helpMenu.setIcon(hammer);
		}

		// acknowledgements
		JMenuItem peeps = new JMenuItem("About");
		peeps.setIcon(mapIcon);
		peeps.addActionListener(
			arg0 -> {
				if (aboutFrame.isVisible()) { return; }
				aboutFrame.setLocation(frame.getLocationOnScreen());
				aboutFrame.setVisible(true);
			});
		helpMenu.add(peeps);

		// end help menu

		// But what if Ganon dabs back?
		ImageIcon ico = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/DABSMALL.png"));
		ImageIcon icoTask = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/DAB.png"));

		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(ico.getImage());
		icons.add(icoTask.getImage());
		frame.setIconImages(icons);

		// frame setting
		frame.setSize(d);
		frame.setMinimumSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(150, 150);

		// file explorer
		BetterJFileChooser explorer = new BetterJFileChooser();
		FileNameExtensionFilter sprFilter =
				new FileNameExtensionFilter("ALttP sprite files", new String[] { ZSPRFile.EXTENSION });
		FileNameExtensionFilter romFilter =
				new FileNameExtensionFilter("ALttP ROM files", new String[] { "sfc" });
		FileNameExtensionFilter pngFilter =
				new FileNameExtensionFilter("PNG images", new String[] { "png" });
		explorer.setAcceptAllFileFilterUsed(false);
		explorer.setFileFilter(sprFilter);
		explorer.addChoosableFileFilter(romFilter);
		explorer.addChoosableFileFilter(pngFilter);

		explorer.setCurrentDirectory(VT_DIRECTORY);

		// can't clear text due to wonky libraries
		// have to set a blank file instead
		File EEE = new File("");

		// try to set current sprite to vanilla
		try {
			loadSprite(animated, AnimatorGUI.class.getResource("/Link.zspr").getPath()); // for debugging
		} catch (Exception e) {
			try { // try again using the VT folder
				loadSprite(animated, VT_DIRECTORY.getAbsolutePath() + "/001.link.1.zspr");
			} catch (Exception e2) {
				// do nothing
			}
		}

		/*
		 * Action listeners
		 */
		// read steps and count them
		animated.addStepListener(arg0 -> stepCur.setText(animated.getStep()));

		// Updates sprite info, but only in the correct mode
		AnimatorListener spriteInfoWatcher =
			arg0 -> {
				try {
					spriteInfo.removeAll();
					spriteInfo.add(animated.getSpriteInfo());
					spriteInfo.revalidate();
					spriteInfo.repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

		// listen for speed changes
		animated.addSpeedListener(
			arg0 -> {
				fasterBtn.setEnabled(!animated.atMaxSpeed());
				slowerBtn.setEnabled(!animated.atMinSpeed());
				speedLevel.setText(animated.getSpeedPercent());
			});

		// listen for mode changes
		animated.addModeListener(
			arg0 -> {
				AnimationMode mode = animated.getMode();

				switch (mode) {
					default:
					case PLAY :
						animated.removeStepListener(spriteInfoWatcher);
						playBtn.setEnabled(true);
						break;
					case ONCE :
						animated.removeStepListener(spriteInfoWatcher);
						playBtn.setEnabled(true);
						break;
					case STEP :
						animated.addStepListener(spriteInfoWatcher);
						playBtn.setEnabled(true);
						break;
				}

				try {
					spriteInfo.removeAll();
					spriteInfo.add(animated.getSpriteInfo());
					spriteInfo.revalidate();
					spriteInfo.repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}

			});

		// listen for zoom changes
		animated.addZoomListener(
			arg0 -> {
				bigBtn.setEnabled(!animated.tooBig());
				lilBtn.setEnabled(!animated.vanillaSize());
				zoomLevel.setText("×" + animated.getZoom());
			});

		// listen for display changes
		animated.addRebuildListener(
			arg0 -> {
				spriteInfo.removeAll();
				try {
					animated.hardReset();
				} catch (Exception e) {
					e.printStackTrace();
				}
				stepMax.setText("/ " + animated.maxStep());
				Animation a = animated.getAnimation();
				animList.setAnimation(a);
				animOptions.setSelectedItem(a);
			});

		// update GUI
		animated.firePurge();

		// load sprite file
		loadBtn.addActionListener(
			arg0 -> {
				explorer.setSelectedFile(EEE);
				int option = explorer.showOpenDialog(loadBtn);
				if (option == JFileChooser.CANCEL_OPTION) { return; }

				// read the file
				String n = "";
				try {
					n = explorer.getSelectedFile().getPath();
					loadSprite(animated, n);
				} catch (ZSPRFormatException e) {
					JOptionPane.showMessageDialog(frame,
							e.getMessage(),
							"PROBLEM",
							JOptionPane.WARNING_MESSAGE);
					return;
				} catch (Exception e) { // all other errors
					JOptionPane.showMessageDialog(frame,
							"Error reading sprite",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				if (SpriteManipulator.testFileType(n, ACCEPTED_FILE_TYPES)) {
					fileName.setText(n);
				}

				// reset animated, forcing it to update
				try {
					looker.setSprite(animated.getGreenMail());
					animated.setAnimation((Animation) animOptions.getSelectedItem());
				} catch(Exception e) {
					// nothing
				}
			});

		reloadBtn.addActionListener(
			arg0 -> {
				String n = fileName.getText();
				if (!SpriteManipulator.testFileType(n, ACCEPTED_FILE_TYPES)) {
					JOptionPane.showMessageDialog(frame,
							"Please select a sprite first.",
							"C'mon dude",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				// read the file
				try {
					loadSprite(animated, fileName.getText());
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame,
							"Error reading sprite",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				} catch (ZSPRFormatException e) {
					JOptionPane.showMessageDialog(frame,
							e.getMessage(),
							"PROBLEM",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				// reset animated, forcing it to update
				try {
					looker.setSprite(animated.getGreenMail());
					animated.setAnimation((Animation) animOptions.getSelectedItem());
				} catch(Exception e) {
					// nothing
				}
			});

		// animation select
		animOptions.addActionListener(
			arg0 -> {
				try {
					animated.setAnimation((Animation) animOptions.getSelectedItem());
				} catch(Exception e) {
					String animName = animOptions.getSelectedItem().toString();
					animated.setAnimation(Animation.STAND);
					animOptions.setSelectedIndex(0);
					JOptionPane.showMessageDialog(frame,
							"There's a problem with the animation:\n" +
									animName,
							"OH NO",
							JOptionPane.WARNING_MESSAGE);
					e.printStackTrace();
					return;
				}
				resetBtn.getActionListeners()[0].actionPerformed(
						new ActionEvent(resetBtn, ActionEvent.ACTION_PERFORMED,"", 0, 0));
			});

		// zoom buttons
		bigBtn.addActionListener(arg0 -> animated.embiggen());
		lilBtn.addActionListener(arg0 -> animated.ensmallen());

		// speed buttons
		fasterBtn.addActionListener(arg0 -> animated.faster());
		slowerBtn.addActionListener(arg0 -> animated.slower());

		// play buttons
		playBtn.addActionListener(arg0 -> animated.setMode(AnimationMode.PLAY));
		playOnceBtn.addActionListener(
			arg0 -> {
				animated.reset();
				animated.setMode(AnimationMode.ONCE);
			});

		// pause button
		pauseBtn.addActionListener(arg0 -> animated.pause());

		AbstractAction playPause = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (animated.isRunning()) {
					animated.pause();
				} else {
					animated.play();
				}
			}
		};

		keysss.put(PLAY_PAUSE.press, PLAY_PAUSE);
		doThings.put(PLAY_PAUSE, playPause);

		// step
		AbstractAction step = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				switch (animated.getMode()) {
					case PLAY :
					case ONCE :
						animated.pause();
						break;
					case STEP :
						animated.step();
						break;
				}
			}
		};

		
		stepBtn.addActionListener(step);
		keysss.put(STEP.press, STEP);
		doThings.put(STEP, step);

		// step back
		AbstractAction stepBack = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				switch (animated.getMode()) {
					case PLAY :
					case ONCE :
						animated.pause();
						break;
					case STEP :
						animated.stepBack();
						break;
				}
			}
		};

		stepBackBtn.addActionListener(stepBack);
		keysss.put(STEP_BACK.press, STEP_BACK);
		doThings.put(STEP_BACK, stepBack);

		// reset
		AbstractAction reset = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				try {
					animated.repaint();
					animated.reset();
				} catch (Exception v) {
					// do nothing
				}
			}
		};

		resetBtn.addActionListener(reset);
		keysss.put(RESET.press, RESET);
		doThings.put(RESET, reset);

		// animation cycling
		AbstractAction nextAnimation = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Animation a = animated.getAnimation().getAnimationInDirection(1);
				animated.setAnimation(a);
			}
		};

		keysss.put(NEXT_ANIM.press, NEXT_ANIM);
		doThings.put(NEXT_ANIM, nextAnimation);

		AbstractAction prevAnimation = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Animation a = animated.getAnimation().getAnimationInDirection(-1);
				animated.setAnimation(a);
			}
		};

		keysss.put(PREV_ANIM.press, PREV_ANIM);
		doThings.put(PREV_ANIM, prevAnimation);

		// background display
		bgDisp.addActionListener(
			arg0 -> {
				Background bg = (Background) bgDisp.getSelectedItem();
				animated.setBackground(bg);
			});

		/*
		 * Wait dialog
		 */
		Dimension waitD = new Dimension(200, 30);
		JDialog plsWait = new JDialog(frame, "Busy");

		ImageIcon clockIcon = new ImageIcon(AnimatorGUI.class.getResource("/images/meta/clock.png"));
		plsWait.setIconImage(clockIcon.getImage());

		plsWait.setPreferredSize(waitD);
		plsWait.setMinimumSize(waitD);
		plsWait.setResizable(false);

		/*
		 * Animated GIF controls
		 */
		Dimension gifD = new Dimension(300, 170);
		JDialog gifGuy = new JDialog(frame, "Make a GIF");
		gifGuy.setIconImage(cane.getImage());
		gifGuy.setPreferredSize(gifD);
		gifGuy.setMinimumSize(gifD);
		gifGuy.setResizable(false);
		gifGuy.setModal(true);

		JPanel gifControls = new JPanel();
		gifControls.setLayout(new GridBagLayout());
		GridBagConstraints x = new GridBagConstraints();
		gifControls.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		gifGuy.add(gifControls);

		x.fill = GridBagConstraints.HORIZONTAL;
		x.gridy = -1;
		x.weightx = 0;

		// label
		JLabel gifLabel = new JLabel("Choose GIF settings", SwingConstants.CENTER);
		x.gridy++;
		x.gridx = 0;
		x.gridwidth = 3;
		gifControls.add(gifLabel, x);
		x.gridwidth = 1;

		// gif zoom
		int maxZoom = 3;
		JLabel gifZoomLabel = new JLabel("Zoom", SwingConstants.RIGHT);
		JSlider gifZoomLevel = new JSlider(JSlider.HORIZONTAL, 1, maxZoom, 1);
		gifZoomLevel.setPaintTicks(true);
		gifZoomLevel.setMajorTickSpacing(1);
		gifZoomLevel.setSnapToTicks(true);

		JLabel gifZoomText = new JLabel("x1", SwingConstants.RIGHT);
		x.gridy++;
		x.gridx = 0;
		gifControls.add(gifZoomLabel, x);
		x.gridx = 1;
		x.weightx = 5;
		gifControls.add(gifZoomLevel, x);
		x.gridx = 2;
		x.weightx = 0;
		gifControls.add(gifZoomText, x);

		gifZoomLevel.addChangeListener(arg0 -> gifZoomText.setText("x" + gifZoomLevel.getValue()));

		// gif speed
		int maxSpeed = 4;
		JLabel gifSpeedLabel = new JLabel("Speed", SwingConstants.RIGHT);
		JSlider gifSpeedLevel = new JSlider(JSlider.HORIZONTAL, 1, maxSpeed, maxSpeed);
		JLabel gifSpeedText = new JLabel("100%", SwingConstants.RIGHT);
		setAllSizes(gifSpeedText, textDimension);

		gifSpeedLevel.setPaintTicks(true);
		gifSpeedLevel.setMajorTickSpacing(1);
		gifSpeedLevel.setSnapToTicks(true);

		x.gridy++;
		x.gridx = 0;
		gifControls.add(gifSpeedLabel, x);
		x.gridx = 1;
		x.weightx = 5;
		gifControls.add(gifSpeedLevel, x);
		x.gridx = 2;
		x.weightx = 0;
		gifControls.add(gifSpeedText, x);

		gifSpeedLevel.addChangeListener(
			arg0 -> {
				int spd = 10000 / (maxSpeed + 1 - gifSpeedLevel.getValue());
				String perc = String.format("%s.%02d%%", spd / 100, spd % 100);
				gifSpeedText.setText(perc);
			});

		// create button
		JCheckBox crop = new JCheckBox("Crop image");
		crop.setHorizontalAlignment(SwingConstants.RIGHT);
		x.gridy++;
		x.gridx = 1;
		x.gridwidth = 2;
		gifControls.add(crop, x);
		x.gridwidth = 1;

		// create button
		PrettyButton makeGif = new PrettyButton("Create");
		x.gridy++;
		x.gridx = 1;
		gifControls.add(makeGif, x);

		makeGif.addActionListener(
			arg0 -> {
				gifGuy.setVisible(false);
				plsWait.setTitle("Making GIF");
				plsWait.setLocation(menu.getLocationOnScreen());
				plsWait.setVisible(true);

				String loc;
				try {
					loc = animated.makeGif(
							gifZoomLevel.getValue(),
							(maxSpeed + 1 - gifSpeedLevel.getValue()),
							crop.isSelected()
						);
				} catch (Exception e) {
					e.printStackTrace();
					plsWait.setVisible(false);
					JOptionPane.showMessageDialog(frame,
							"Error making GIF",
							"Dang",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				plsWait.setVisible(false);
				JOptionPane.showMessageDialog(frame,
						"GIF written to: \n" + loc,
						"OH YEAH!",
						JOptionPane.PLAIN_MESSAGE);
			});

		giffer.addActionListener(
				arg0 -> {
					gifGuy.setLocation(menu.getLocationOnScreen());
					gifGuy.setVisible(true);
				});

		/*
		 * Crossproduct tracker images
		 */
		cross.addActionListener(
			arg0 -> {
				plsWait.setTitle("Making images");
				plsWait.setLocation(menu.getLocationOnScreen());
				plsWait.setVisible(true);

				String loc;
				try {
					loc = animated.makeCrossproduct();
				} catch (Exception e) {
					e.printStackTrace();
					plsWait.setVisible(false);
					JOptionPane.showMessageDialog(frame,
							"Error making images",
							"Dang",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				plsWait.setVisible(false);
				JOptionPane.showMessageDialog(frame,
						"Files written to folder: \n" + loc,
						"OH YEAH!",
						JOptionPane.PLAIN_MESSAGE);
			});

		/*
		 * Collage images
		 */
		collage.addActionListener(
			arg0 -> {
				plsWait.setTitle("Making image");
				plsWait.setLocation(menu.getLocationOnScreen());
				plsWait.setVisible(true);

				String loc;
				try {
					loc = animated.makeCollage();
				} catch (Exception e) {
					e.printStackTrace();
					plsWait.setVisible(false);
					JOptionPane.showMessageDialog(frame,
							"Error making image",
							"Dang",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				plsWait.setVisible(false);
				JOptionPane.showMessageDialog(frame,
						"File written to: \n" + loc,
						"OH YEAH!",
						JOptionPane.PLAIN_MESSAGE);
			});

		// sprite fetching
		vtRefresh.addActionListener(
			arg0 -> {
				plsWait.setTitle("Fetching sprites");
				plsWait.setLocation(menu.getLocationOnScreen());
				plsWait.setVisible(true);
				VTGrabber.run();
				plsWait.setVisible(false);
			});
		// turn on
		frame.setVisible(true);
	}

	/**
	 * Set min max and preferred size for a component
	 * @param c
	 * @param d
	 */
	private static void setAllSizes(Component c, Dimension d) {
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
		c.setSize(d);
	}

	/**
	 * Tool tips are stupid omfg
	 */
	private static final String[] TOOLTIP_STYLES = {
			"width: 150px",
			"max-width: 150px",
			"padding: 2px",
	};

	/**
	 * Set a tool tip with text and warning in the preferred format
	 * @param c
	 * @param text
	 * @param warning
	 */
	private static void setToolTip(JComponent c, String... text) {
		c.setToolTipText(
				"<html>" +
				"<div style=\"" + String.join(";", TOOLTIP_STYLES) + "\">" +
				String.join("<br /><br />", text) +
				"</div>" +
				"</html>");

		// set an underline for the component
		if (c instanceof JLabel) {
			String s =  ((JLabel) c).getText();
			((JLabel) c).setText(
					"<html>" +
					"<div style=\"border-bottom: 1px dotted;\">" +
					s +
					"</div>" +
					"</html>"
					);
		}
	}

	private static void loadSprite(SpriteAnimator a, String fileName)
			throws IOException, ZSPRFormatException {
		// read the file
		String fileType = SpriteManipulator.getFileType(fileName);
		String[] tempN = fileName.split("/");
		String spriteName = tempN[tempN.length-1].replace("." + fileType,"");

		// sprite data
		byte[] spriteData;

		// palette data
		byte[] palData;
		byte[] glovesData;

		// sprite files
		if (fileType.equalsIgnoreCase(ZSPRFile.EXTENSION)) {
			ZSPRFile temp = ZSPRFile.readFile(fileName);
			spriteData = temp.getSpriteData();
			palData = temp.getPalData();
			glovesData = temp.getGlovesData();
		// rom files
		} else if (fileType.equalsIgnoreCase("sfc")) {
			byte[] temp = SpriteManipulator.readFile(fileName);
			spriteData = SpriteManipulator.getSpriteDataFromROM(temp);
			palData = SpriteManipulator.getPaletteDataFromROM(temp);
			glovesData = SpriteManipulator.getGlovesDataFromROM(temp);
		// png files
		} else if (fileType.equalsIgnoreCase("png")) {
			File temp = new File(fileName);
			BufferedImage img = ImageIO.read(temp);
			int w = img.getWidth();
			int h = img.getHeight();
			if (w != 128 || h != 448) {
				throw new ZSPRFormatException(
						"Invalid dimensions of {" + w + "," + h + "}\n" +
						"Image dimensions must be 128x448");
			}

			BufferedImage mails[][] = new BufferedImage[5][3];
			for (int i = 0; i < mails.length; i++) {
				for (int j = 0; j < mails[i].length; j++) {
					mails[i][j] = img;
				}
			}

			a.setSprite(spriteName, mails);
			return;
		} else {
			return;
		}

		// turn spr into useable images
		byte[][][] ebe = SpriteManipulator.makeSpr8x8(spriteData);
		BufferedImage[][] mails = SpriteManipulator.makeAllMails(ebe, palData, glovesData);
		a.setSprite(spriteName, mails);
	}

	private static void openLink(String url) throws IOException, URISyntaxException {
		URL aa;
		aa = new URL(url);
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(aa.toURI());
		}
	}

	private static interface GearChange {
		void go(int i);
	}

	private static interface MiscChange {
		void go(boolean b);
	}

	private static boolean amIUpToDate() {
		boolean ret = true;
		URL vURL;
		try {
			vURL = new URL(VERSION_URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}

		try (
			InputStream s = vURL.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(s, StandardCharsets.UTF_8));
		) {
			String line = br.readLine();
			System.out.println("Discovered version: " + line);
			if (!line.equalsIgnoreCase(VERSION)) {
				ret = false;
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}