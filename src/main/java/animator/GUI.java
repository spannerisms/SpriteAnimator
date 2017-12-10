package animator;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import animator.cellsearch.CellFrame;
import animator.database.*;

import static javax.swing.SpringLayout.*;

public class GUI {
	private static final String[] ACCEPTED_FILE_TYPES =
			new String[] { ZSPRFile.EXTENSION, "sfc" /*, "png"*/ };

	private static final String[] MODES = {
			"Normal play",
			"Step-by-step",
			// "All steps" disabled for now
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

	private static final String[] MAIL_LEVELS = {
			"Green mail",
			"Blue mail",
			"Red mail",
			"Bunny"
		};

	public static final String[] GLOVE_LEVELS = {
			"No glove",
			"Power glove",
			"Titans mitt"
		};

	private static final int BLANK_HEIGHT = 10;

	/**
	 * Perform
	 */
	public static void main(String[] args) throws IOException {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new GUI().printGUI();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// use func
	public void printGUI() throws IOException {
		// try to set LaF
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e2) {
			// do nothing
		} //end System

		ToolTipManager.sharedInstance().setInitialDelay(100);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE); // 596:31:23.647
		final JFrame frame = new JFrame("Sprite Animator " + SpriteManipulator.ALTTPNG_VERSION);
		final Dimension d = new Dimension(800, 600);
		Border rightPad = BorderFactory.createEmptyBorder(0, 0, 0, 5);
		Border fullPad = BorderFactory.createEmptyBorder(3, 3, 3, 3);
		Dimension textDimension = new Dimension(50, 20);

		// layout
		final Container fullWrap = frame.getContentPane();
		SpringLayout l = new SpringLayout();
		fullWrap.setLayout(l);

		// Tool Tip constants info
		final String REBUILDS =
				"This action requires a rebuild of the animation data, resetting the current step to 1.";
		final String REBUILDS_PLURAL =
				"These actions require a rebuild of the animation data, resetting the current step to 1.";

		// control panel
		final JPanel controls = new JPanel(new GridBagLayout());
		controls.setBorder(fullPad);
		GridBagConstraints c = new GridBagConstraints();

		l.putConstraint(EAST, controls, -5, EAST, fullWrap);
		l.putConstraint(WEST, controls, -250, EAST, fullWrap);
		l.putConstraint(NORTH, controls, 5, NORTH, frame);
		frame.add(controls);

		// negative so everything can just ++
		c.gridy = -1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		// image loading
		final JTextField fileName = new JTextField("");
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 3;
		controls.add(fileName, c);

		// load buttons
		final JButton loadBtn = new JButton("Load sprite");
		final JButton reloadBtn = new JButton("Reload");
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
		final JComboBox<Animation> animOptions = new JComboBox<Animation>(Animation.values());
		final JLabel animationLabel = new JLabel("Animation:", SwingConstants.RIGHT);
		animationLabel.setBorder(rightPad);
		c.gridy++;
		c.gridx = 0;
		controls.add(animationLabel, c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(animOptions, c);
		c.gridwidth = 1;

		// animation mode
		final JComboBox<String> modeOptions = new JComboBox<String>(MODES);
		final JLabel modeLabel = new JLabel(" ", SwingConstants.RIGHT);
		modeLabel.setBorder(rightPad);
		c.gridwidth = 2;
		c.gridy++;
		c.gridx = 1;
		controls.add(modeOptions, c);
		c.gridwidth = 1;

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// background
		final JComboBox<Background> bgDisp = new JComboBox<Background>(Background.values());
		final JLabel backgroundLabel = new JLabel("Background:", SwingConstants.RIGHT);
		backgroundLabel.setBorder(rightPad);
		c.gridy++;
		c.gridx = 0;
		controls.add(backgroundLabel, c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(bgDisp, c);
		c.gridwidth = 1;

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// sword level
		final JComboBox<String> swordLevel = new JComboBox<String>(SWORD_LEVELS);
		final JLabel gearLabel = new JLabel("Gear:", SwingConstants.RIGHT); // not actually the word "Sword"
		gearLabel.setBorder(rightPad);
		setToolTip(gearLabel,
				"Controls the level and display of the sword and shield, " +
				"and the current mail palette.",
				REBUILDS_PLURAL);
		c.gridy++;
		c.gridx = 0;
		controls.add(gearLabel, c);
		c.gridwidth = 2;
		c.gridx = 1;
		controls.add(swordLevel, c);

		// shield level
		final JComboBox<String> shieldLevel = new JComboBox<String>(SHIELD_LEVELS);
		c.gridy++;
		c.gridx = 1;
		controls.add(shieldLevel, c);

		// mail level
		final JComboBox<String> mailLevel = new JComboBox<String>(MAIL_LEVELS);		
		c.gridy++;
		c.gridx = 1;
		controls.add(mailLevel, c);

		// glove level
		final JComboBox<String> gloveLevel = new JComboBox<String>(GLOVE_LEVELS);		
		c.gridy++;
		c.gridx = 1;
		controls.add(gloveLevel, c);

		c.gridwidth = 1;

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// other equipment
		final JButton equipBtn = new JButton("Toggle");
		final JLabel thePhraseMiscellaneousSpritesWithAColon = new JLabel("Misc. sprites:", SwingConstants.RIGHT);
		final JLabel equipStatus = new JLabel("ON", SwingConstants.CENTER);
		thePhraseMiscellaneousSpritesWithAColon.setBorder(rightPad);
		setToolTip(thePhraseMiscellaneousSpritesWithAColon,
				"When <b>miscellaneous sprites</b> are on, " +
						"animations that contain sprites other than the playable character " +
						"will display their relevant sprites.",
				"This option does not affect the display of swords, shields, or swag duck.",
				REBUILDS);
		c.gridy++;
		c.gridx = 0;
		controls.add(thePhraseMiscellaneousSpritesWithAColon, c);
		c.gridx = 1;
		controls.add(equipStatus, c);
		c.gridx = 2;
		controls.add(equipBtn, c);

		// shadows
		final JButton shadowBtn = new JButton("Toggle");
		final JLabel shadowLabel = new JLabel("Shadows:", SwingConstants.RIGHT);
		final JLabel shadowStatus = new JLabel("--", SwingConstants.CENTER);
		shadowLabel.setBorder(rightPad);
		setToolTip(shadowLabel,
				"When <b>shadows</b> are on, " +
						"certain animations will display a shadow below the character sprite.",
				"This option will not affect shadows that belong to other sprites.",
				REBUILDS);
		c.gridy++;
		c.gridx = 0;
		controls.add(shadowLabel, c);
		c.gridx = 1;
		controls.add(shadowStatus, c);
		c.gridx = 2;
		controls.add(shadowBtn, c);

		// shadows
		final JButton neutralBtn = new JButton("Toggle");
		final JLabel neutralLabel = new JLabel("Neutral:", SwingConstants.RIGHT);
		final JLabel neutralStatus = new JLabel("--", SwingConstants.CENTER);
		neutralLabel.setBorder(rightPad);
		setToolTip(neutralLabel,
				"When <b>neutral poses</b> are on, " +
						"certain animations will begin or end with the character sprite at a " +
						"neutral standing position.",
				REBUILDS);
		c.gridy++;
		c.gridx = 0;
		controls.add(neutralLabel, c);
		c.gridx = 1;
		controls.add(neutralStatus, c);
		c.gridx = 2;
		controls.add(neutralBtn, c);

		// blank
		c.gridy++;
		c.ipady = BLANK_HEIGHT * 2;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// zoom
		final JLabel zoomLevel = new JLabel("x-", SwingConstants.RIGHT);
		final JButton bigBtn = new JButton("Zoom+");
		final JButton lilBtn = new JButton("Zoom-");
		setAllSizes(zoomLevel, textDimension);
		zoomLevel.setBorder(rightPad);

		c.gridy++;
		c.gridx = 0;
		controls.add(zoomLevel, c);
		c.gridx = 1;
		controls.add(lilBtn, c);
		c.gridx = 2;
		controls.add(bigBtn, c);

		// speed
		final JButton fasterBtn = new JButton("Speed+");
		final JButton slowerBtn = new JButton("Speed-");
		final JLabel speedLevel = new JLabel("--%", SwingConstants.RIGHT);
		setAllSizes(speedLevel, textDimension);
		speedLevel.setBorder(rightPad);

		c.gridy++;
		c.gridx = 0;
		controls.add(speedLevel, c);
		c.gridx = 1;
		controls.add(slowerBtn, c);
		c.gridx = 2;
		controls.add(fasterBtn, c);

		// blank
		c.gridy++;
		c.ipady = 20;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// step counter
		final JLabel stepLabel = new JLabel("Animation step:", SwingConstants.RIGHT);
		final JLabel stepCur = new JLabel("-", SwingConstants.RIGHT);
		final JLabel stepMax = new JLabel("/ -");
		stepCur.setBorder(rightPad);
		stepMax.setBorder(rightPad);
		setAllSizes(stepCur, textDimension);

		c.gridy++;
		c.gridx = 0;
		c.weightx = 9;
		controls.add(stepLabel, c);
		c.weightx = 0;
		c.gridx = 1;
		controls.add(stepCur, c);
		c.gridx = 2;
		controls.add(stepMax, c);

		// play step reset
		final JButton playBtn = new JButton("Play");
		final JButton stepBtn = new JButton("--");
		final JButton resetBtn = new JButton("Reset");

		c.gridy++;
		c.gridx = 0;
		controls.add(playBtn, c);
		c.gridx = 1;
		controls.add(stepBtn, c);
		c.gridx = 2;
		controls.add(resetBtn, c);

		// blank
		c.gridy++;
		c.ipady = 20;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// sprite info
		final JLabel spriteInfo = new JLabel("");
		c.gridwidth = 3;
		c.gridy++;
		c.gridx = 0;
		controls.add(spriteInfo, c);

		// control panel done

		// other frame organization
		final SpriteAnimator animated = new SpriteAnimator();
		l.putConstraint(WEST, animated, 5, WEST, fullWrap);
		l.putConstraint(EAST, animated, -5, WEST, controls);
		l.putConstraint(NORTH, animated, 5, NORTH, fullWrap);
		l.putConstraint(SOUTH, animated, -5, SOUTH, fullWrap);
		fullWrap.add(animated);

		// acknowledgements
		ImageIcon mapIcon = new ImageIcon(GUI.class.getResource("/images/map.png"));
		final JDialog aboutFrame = new JDialog(frame, "Acknowledgements");
		aboutFrame.setIconImage(mapIcon.getImage());

		final TextArea peepsList = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		peepsList.setEditable(false);
		peepsList.append("Written by fatmanspanda"); // hey, that's me
		peepsList.append("\n\nAnimation research:\n");
		peepsList.append("http://alttp.mymm1.com/sprites/includes/animations.txt\n");
		peepsList.append(String.join(", ",
				new String[]{
						"MikeTrethewey", // it's mike
						"TWRoxas", // helped mike with his site
						"RyuTech"
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
						"Veetorp", // provided most valuable documentation
						"Zarby89", // various documentation and answers
						"Sosuke3" // various snes code answers
				}));
		peepsList.append("\n\nTesting and feedback:\n");
		peepsList.append(String.join(", ",
				new String[]{
						"Jighart",
				}));
		aboutFrame.add(peepsList);
		aboutFrame.setSize(600, 300);
		aboutFrame.setLocation(150, 150);
		aboutFrame.setResizable(false);
		// end credits

		// menu
		final JMenuBar menu = new JMenuBar();
		frame.setJMenuBar(menu);

		// file menu
		final JMenu fileMenu = new JMenu("File");
		menu.add(fileMenu);

		// reverse lookup
		CellFrame looker = new CellFrame(frame);
		final JMenuItem lookUp = new JMenuItem("Sprite lookup");
		ImageIcon net = new ImageIcon(GUI.class.getResource("/images/net.png"));
		looker.setIconImage(net.getImage());
		lookUp.setIcon(net);
		fileMenu.add(lookUp);
		lookUp.addActionListener(arg0 -> looker.setVisible(true));

		// gif creation
		ImageIcon cane = new ImageIcon(GUI.class.getResource("/images/cane.png"));
		final Dimension gifD = new Dimension(300, 150);
		JDialog gifGuy = new JDialog(frame, "Make a GIF");
		gifGuy.setIconImage(cane.getImage());
		gifGuy.setPreferredSize(gifD);
		gifGuy.setMinimumSize(gifD);
		gifGuy.setResizable(false);
		gifGuy.setModal(true);

		JPanel gifControls = new JPanel();
		gifControls.setLayout(new GridBagLayout());
		GridBagConstraints x = new GridBagConstraints();
		gifControls.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		gifGuy.add(gifControls);

		x.fill = GridBagConstraints.HORIZONTAL;
		x.gridy = -1;
		x.weightx = 0;

		// label
		final JLabel gifLabel = new JLabel("Choose GIF settings", SwingConstants.CENTER);
		x.gridy++;
		x.gridx = 0;
		x.gridwidth = 3;
		gifControls.add(gifLabel, x);
		x.gridwidth = 1;

		// gif zoom
		final JLabel gifZoomLabel = new JLabel("Zoom", SwingConstants.RIGHT);
		final JSlider gifZoomLevel = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
		gifZoomLevel.setPaintTicks(true);
		gifZoomLevel.setMajorTickSpacing(1);
		gifZoomLevel.setSnapToTicks(true);

		final JLabel gifZoomText = new JLabel("x1", SwingConstants.RIGHT);
		x.gridy++;
		x.gridx = 0;
		gifControls.add(gifZoomLabel, x);
		x.gridx = 1;
		x.weightx = 5;
		gifControls.add(gifZoomLevel, x);
		x.gridx = 2;
		x.weightx = 0;
		gifControls.add(gifZoomText, x);

		gifZoomLevel.addChangeListener(
			arg0 -> {
				gifZoomText.setText("x" + gifZoomLevel.getValue());
			});

		// gif speed
		final int maxSpeed = 4;
		final JLabel gifSpeedLabel = new JLabel("Speed", SwingConstants.RIGHT);
		final JSlider gifSpeedLevel = new JSlider(JSlider.HORIZONTAL, 1, maxSpeed, maxSpeed);
		final JLabel gifSpeedText = new JLabel("100%", SwingConstants.RIGHT);
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
		final JButton makeGif = new JButton("Create");
		x.gridy++;
		x.gridx = 1;
		gifControls.add(makeGif, x);

		// wait for gif
		final Dimension waitD = new Dimension(200, 30);
		JDialog gifWait = new JDialog(frame, "Making gif");

		ImageIcon clockIcon = new ImageIcon(GUI.class.getResource("/images/clock.png"));
		gifWait.setIconImage(clockIcon.getImage());

		gifWait.setPreferredSize(waitD);
		gifWait.setMinimumSize(waitD);
		gifWait.setResizable(false);

		makeGif.addActionListener(
			arg0 -> {
				gifGuy.setVisible(false);

				gifWait.setLocation(menu.getLocationOnScreen());
				gifWait.setVisible(true);

				String loc;
				try {
					loc = animated.makeGif(gifZoomLevel.getValue(), (maxSpeed + 1 - gifSpeedLevel.getValue()));
				} catch (Exception e) {
					gifWait.setVisible(false);
					JOptionPane.showMessageDialog(frame,
							"Error making GIF",
							"Dang",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				gifWait.setVisible(false);
				JOptionPane.showMessageDialog(frame,
						"GIF written to: \n" + loc,
						"OH YEAH!",
						JOptionPane.PLAIN_MESSAGE);
			});

		// animated gifs
		final JMenuItem giffer = new JMenuItem("Make GIF");
		giffer.setIcon(cane);
		fileMenu.add(giffer);
		giffer.addActionListener(
			arg0 -> {
				gifGuy.setLocation(menu.getLocationOnScreen());
				gifGuy.setVisible(true);
			});

		// exit
		final JMenuItem exit = new JMenuItem("Exit");
		ImageIcon mirror = new ImageIcon(GUI.class.getResource("/images/mirror.png"));
		exit.setIcon(mirror);
		fileMenu.add(exit);
		exit.addActionListener(arg0 -> System.exit(0));

		// end file menu

		// help menu
		final JMenu helpMenu = new JMenu("Help");
		menu.add(helpMenu);

		// Acknowledgements
		final JMenuItem peeps = new JMenuItem("About");
		peeps.setIcon(mapIcon);
		peeps.addActionListener(arg0 -> aboutFrame.setVisible(true));
		helpMenu.add(peeps);

		// end help menu

		// But what if Ganon dabs back?
		ImageIcon ico = new ImageIcon(GUI.class.getResource("/images/DABSMALL.png"));
		ImageIcon icoTask = new ImageIcon(GUI.class.getResource("/images/DAB.png"));

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
		final BetterJFileChooser explorer = new BetterJFileChooser();
		FileNameExtensionFilter sprFilter =
				new FileNameExtensionFilter("ALttP sprite files", new String[] { ZSPRFile.EXTENSION });
		FileNameExtensionFilter romFilter =
				new FileNameExtensionFilter("ALttP rom files", new String[] { "sfc" });
//		FileNameExtensionFilter pngFilter =
//				new FileNameExtensionFilter("PNG images", new String[] { "png" });
		explorer.setAcceptAllFileFilterUsed(false);
		explorer.setFileFilter(sprFilter);
		explorer.addChoosableFileFilter(romFilter);

		// can't clear text due to wonky code
		// have to set a blank file instead
		final File EEE = new File("");

		explorer.setCurrentDirectory(new File(".")); // quick way to set to current .jar loc

		// clear focusability of all useless components
		for (Component comp : controls.getComponents()) {
			if (comp instanceof JLabel ||
					comp instanceof JButton) {
				comp.setFocusable(false);
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
					spriteInfo.setText(animated.getSpriteInfo());
				} catch (Exception e) {
					// nothing
				}
			};

		// listen for speed changes
		animated.addSpeedListener(
			arg0 -> {
				if (btnAllowed("speed", animated.getMode())) {
					fasterBtn.setEnabled(!animated.atMaxSpeed());
					slowerBtn.setEnabled(!animated.atMinSpeed());
					speedLevel.setText(animated.getSpeedPercent());
				} else {
					speedLevel.setText("");
					fasterBtn.setEnabled(false);
					slowerBtn.setEnabled(false);
				}
			});

		// listen for mode changes
		animated.addModeListener(
			arg0 -> {
				int mode = animated.getMode();
				stepBtn.setEnabled(btnAllowed("step", mode));
				slowerBtn.setEnabled(btnAllowed("speed", mode));
				fasterBtn.setEnabled(btnAllowed("speed", mode));

				if (!btnAllowed("speed", mode)) {
					speedLevel.setText("");
				}

				String stepWord;
				switch (mode) {
					case 0 :
					case 2 :
						stepWord = "Pause";
						animated.removeStepListener(spriteInfoWatcher);
						break;
					case 1 :
					default :
						stepWord = "Step";
						animated.addStepListener(spriteInfoWatcher);
						break;
				}

				stepBtn.setText(stepWord);
				modeOptions.setSelectedIndex(mode);
				playBtn.setEnabled(!animated.isRunning());

				try {
					spriteInfo.setText(animated.getSpriteInfo());
				} catch (Exception e) {
					// nothing
				}
			});

		// listen for zoom changes
		animated.addZoomListener(
			arg0 -> {
				bigBtn.setEnabled(!animated.tooBig());
				lilBtn.setEnabled(!animated.vanillaSize());
				zoomLevel.setText("x" + animated.getZoom());
			});

		// listen for display changes
		animated.addRebuildListener(
			arg0 -> {
				try {
					animated.hardReset();
				} catch (Exception e) {
					// do nothing
				}
				stepMax.setText("/ " + animated.maxStep());
				equipStatus.setText(animated.equipmentOn() ? "ON" : "OFF");
				shadowStatus.setText(animated.shadowOn() ? "ON" : "OFF");
				neutralStatus.setText(animated.neutralOn() ? "ON" : "OFF");
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

		// mode select
		modeOptions.addActionListener(
			arg0 -> {
				try {
					animated.setMode(modeOptions.getSelectedIndex());
				} catch (Exception e) {
					// do nothing
				}
			});

		// zoom buttons
		bigBtn.addActionListener(arg0 ->animated.embiggen());
		lilBtn.addActionListener(arg0 ->animated.ensmallen());

		// speed buttons
		fasterBtn.addActionListener(arg0 -> animated.faster());
		slowerBtn.addActionListener(arg0 -> animated.slower());

		// play button
		playBtn.addActionListener(arg0 -> animated.setMode(0));

		// step button
		stepBtn.addActionListener(
			arg0 -> {
				switch (animated.getMode()) {
					case 0 :
						animated.pause();
						break;
					case 1 :
						animated.step();
						break;
				}
			});

		// reset button
		resetBtn.addActionListener(
			arg0 -> {
				try {
					animated.repaint();
					animated.reset();
				} catch (Exception e) {
					// do nothing
				}
			});

		// item toggle
		equipBtn.addActionListener(
			arg0 -> {
				animated.switchEquipment();
			});

		// shadow toggle
		shadowBtn.addActionListener(
			arg0 -> {
				animated.switchShadow();
			});

		// neutral toggle
		neutralBtn.addActionListener(
			arg0 -> {
				animated.switchNeutral();
			});

		// gear settings
		mailLevel.addActionListener(
			arg0 -> {
				int level = mailLevel.getSelectedIndex();
				animated.setMail(level);
			});

		gloveLevel.addActionListener(
			arg0 -> {
				int level = gloveLevel.getSelectedIndex();
				animated.setGlove(level);
			});

		swordLevel.addActionListener(
			arg0 -> {
				int level = swordLevel.getSelectedIndex();
				animated.setSword(level);
			});

		shieldLevel.addActionListener(
			arg0 -> {
				int level = shieldLevel.getSelectedIndex();
				animated.setShield(level);
			});

		// background display
		bgDisp.addActionListener(
			arg0 -> {
				Background bg = (Background) bgDisp.getSelectedItem();
				animated.setBackground(bg);
			});

		// turn on
		frame.setVisible(true);
	}

	/**
	 * See what buttons are allowed for what modes
	 */
	private static boolean btnAllowed(String n, int mode) {
		boolean allowed = false;
		n = n.toLowerCase();
		switch (n) {
			// both speed buttons
			case "speed" : {
				switch (mode) {
					case 0 :
					case 2 :
						allowed = true;
						break;
					case 1 :
						allowed = false;
						break;
				} // end mode switch
				break;
			} // end speed case
			// step button
			case "step" : {
				switch (mode) {
					case 2 :
						allowed = false;
						break;
					case 0 :
					case 1 :
						allowed = true;
						break;
				} // end mode switch
				break;
			} // end step case
			// other buttons (currently all are allowed)
			case "reset" :
			case "zoom" :
				allowed = true;
				break;
			default :
				allowed = false;
				break;
		}
		return allowed;
	}

	/**
	 * Set min max and preferred size for a component
	 * @param c
	 * @param d
	 */
	private void setAllSizes(Component c, Dimension d) {
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
	private void setToolTip(JComponent c, String... text) {
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

		if (fileType.equalsIgnoreCase(ZSPRFile.EXTENSION)) {
			ZSPRFile temp = ZSPRFile.readFile(fileName);
			spriteData = temp.getSpriteData();
			palData = temp.getPalData();
			glovesData = temp.getGlovesData();
		} else if (fileType.equalsIgnoreCase("sfc")) {
			byte[] temp = SpriteManipulator.readFile(fileName);
			spriteData = SpriteManipulator.getSpriteDataFromROM(temp);
			palData = SpriteManipulator.getPaletteDataFromROM(temp);
			glovesData = SpriteManipulator.getGlovesDataFromROM(temp);
		} else if (fileType.equalsIgnoreCase("png")) {
			return;
		} else {
			return;
		}

		// turn spr into useable images
		byte[][][] ebe = SpriteManipulator.makeSpr8x8(spriteData);
		BufferedImage[][] mails = SpriteManipulator.makeAllMails(ebe, palData, glovesData);
		a.setSprite(spriteName, mails);
	}
}