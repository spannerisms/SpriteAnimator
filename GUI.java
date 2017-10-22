package SpriteAnimator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI {
	static final String[] ALLFRAMES = Database.ALLFRAMES;
	static final String[] MODES = {
			"Normal play",
			"Step-by-step",
			//"All frames" disabled for now
	};

	static final String[] SWORDLEVELS = {
			"No sword",
			"Fighter's sword",
			"Master sword",
			"Tempered sword",
			"Butter sword"
	};

	static final String[] SHIELDLEVELS = {
			"No shield",
			"Fighter's shield",
			"Red shield",
			"Mirror shield"
	};
	// use func
	public void printGUI(String[] args) throws IOException {
		//try to set LaF
		try {
			UIManager.setLookAndFeel("metal");
		} catch (UnsupportedLookAndFeelException
				| ClassNotFoundException
				| InstantiationException
				| IllegalAccessException e) {
			// try to set System default
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (UnsupportedLookAndFeelException
					| ClassNotFoundException
					| InstantiationException
					| IllegalAccessException e2) {
					// do nothing
			} //end System
		} // end Nimbus
		final JFrame frame = new JFrame("Sprite Animator");
		final Dimension d = new Dimension(600, 400);
		final Dimension minD = new Dimension(400, 300);
		Border rightPad = BorderFactory.createEmptyBorder(0,0,0,5);
		Border fullPad = BorderFactory.createEmptyBorder(3,3,3,3);
		Dimension textDimension = new Dimension(50,20);

		// image loading
		final JPanel loadWrap = new JPanel(new BorderLayout());
		loadWrap.setBorder(fullPad);
		final JTextField fileName = new JTextField("");
		final JButton loadBtn = new JButton("Load Sprite");

		loadWrap.add(loadBtn,BorderLayout.EAST);
		loadWrap.add(fileName,BorderLayout.CENTER);

		// controls panel
		final JPanel controls = new JPanel(new GridBagLayout());
		final JPanel controlsWrap = new JPanel(new BorderLayout());
		controls.setBorder(fullPad);
		GridBagConstraints c = new GridBagConstraints();

		// negative so everything can just ++
		c.gridy = -1;
		// animation playing
		final JComboBox<String> modeOptions = new JComboBox<String>(MODES);
		final JComboBox<String> animOptions = new JComboBox<String>(getAnimNames());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy++;
		c.gridwidth = 3;
		c.gridx = 0;
		controls.add(animOptions, c);
		c.gridy++;
		controls.add(modeOptions, c);

		// blank
		c.gridy++;
		c.ipady = 10;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// sword and shield levels
		c.fill = GridBagConstraints.HORIZONTAL;
		final JComboBox<String> swordLevel = new JComboBox<String>(SWORDLEVELS);
		final JComboBox<String> shieldLevel = new JComboBox<String>(SHIELDLEVELS);

		c.gridwidth = 3;
		c.gridy++;
		c.gridx = 0;
		controls.add(swordLevel, c);
		c.gridy++;
		controls.add(shieldLevel, c);

		// other equipment
		final JButton equipBtn = new JButton("Toggle");
		final JLabel theWordEquipmentWithAColon = new JLabel("Equipment:", SwingConstants.RIGHT);
		final JLabel equipStatus = new JLabel("OFF", SwingConstants.CENTER);
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		controls.add(theWordEquipmentWithAColon, c);
		c.gridx = 1;
		controls.add(equipStatus, c);
		c.gridx = 2;
		controls.add(equipBtn, c);
		
		// shadows
		final JButton shadowBtn = new JButton("Toggle");
		final JLabel theWordShadowsWithAColon = new JLabel("Shadows:", SwingConstants.RIGHT);
		final JLabel shadowStatus = new JLabel("OFF", SwingConstants.CENTER);
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		controls.add(theWordShadowsWithAColon, c);
		c.gridx = 1;
		controls.add(shadowStatus, c);
		c.gridx = 2;
		controls.add(shadowBtn, c);

		// blank
		c.gridy++;
		c.ipady = 20;
		controls.add(new JLabel(), c);
		c.ipady = 0;

		// zoom
		final JLabel zoomLevel = new JLabel("x3", SwingConstants.RIGHT);
		final JButton bigBtn = new JButton("Zoom+");
		final JButton lilBtn = new JButton("Zoom-");
		setAllSizes(zoomLevel,textDimension);
		zoomLevel.setBorder(rightPad);
		c.gridy++;
		c.gridwidth = 1;
		c.gridx = 0;
		controls.add(zoomLevel, c);
		c.gridx = 1;
		controls.add(lilBtn, c);
		c.gridx = 2;
		controls.add(bigBtn, c);

		// speed
		final JButton fasterBtn = new JButton("Speed+");
		final JButton slowerBtn = new JButton("Speed-");
		final JLabel speedLevel = new JLabel("100%", SwingConstants.RIGHT);
		setAllSizes(speedLevel,textDimension);
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

		// frame counter
		final JLabel theWordFrameWithAColon = new JLabel("Frame:", SwingConstants.RIGHT);
		final JLabel frameCur = new JLabel("1", SwingConstants.RIGHT);
		final JLabel frameMax = new JLabel("/ 1");
		frameCur.setBorder(rightPad);
		frameMax.setBorder(rightPad);
		setAllSizes(frameCur,textDimension);
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		c.weightx = 9;
		controls.add(theWordFrameWithAColon, c);
		c.weightx = 0;
		c.gridx = 1;
		controls.add(frameCur, c);
		c.gridx = 2;
		controls.add(frameMax, c);

		// step
		final JButton stepBtn = new JButton("Step");
		stepBtn.setEnabled(false);
		c.gridwidth = 3;
		c.gridy++;
		c.gridx = 0;
		controls.add(stepBtn, c);

		// reset
		c.gridwidth = 3;
		c.gridy++;
		c.gridx = 0;
		final JButton resetBtn = new JButton("Reset");
		controls.add(resetBtn, c);
		// control panel done

		final JPanel bottomStuffWrap = new JPanel(new BorderLayout());
		final JPanel bottomStuff = new JPanel(new BorderLayout());

		final SpriteAnimator imageArea = new SpriteAnimator();
		final SpriteAnimator run = imageArea; // just a shorter name
		bottomStuffWrap.add(imageArea,BorderLayout.CENTER);
		bottomStuffWrap.add(bottomStuff,BorderLayout.EAST);

		// Credits
		final JFrame aboutFrame = new JFrame("Acknowledgements");
		final JMenuItem peeps = new JMenuItem("About");
		final TextArea peepsList = new TextArea("", 0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		peepsList.setEditable(false);
		peepsList.append("Written by fatmanspanda"); // hey, that's me
		peepsList.append("\n\nFrame resources:\n");
		peepsList.append("http://alttp.mymm1.com/sprites/includes/animations.txt\n");
		peepsList.append(GUIHelpers.join(new String[]{
				"\tMikeTrethewey", // it's mike
				"TWRoxas", // provided most valuable documentation
				}, ", "));// forced me to do this and falls in every category
		peepsList.append("\n\nAnimation research:\n\tRyuTech");
		peepsList.append("\n\nCode contribution:\n");
		peepsList.append(GUIHelpers.join(new String[]{
				"MikeTretheway", // God dammit, so being so helpful
				"Zarby89", // spr conversion
				}, ", "));
		peepsList.append("\n\nResources and development:\n");
		peepsList.append(GUIHelpers.join(new String[]{
				"Veetorp", // provided most valuable documentation
				"Zarby89", // various documentation and answers
				"Sosuke3" // various snes code answers
				}, ", "));
		peepsList.append("\n\nTesting and feedback:\n");
		peepsList.append(GUIHelpers.join(new String[]{
				"Jighart",
				}, ", "));
		aboutFrame.add(peepsList);

		peeps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aboutFrame.setVisible(true);
			}});
		aboutFrame.setSize(600,300);
		aboutFrame.setLocation(150,150);
		aboutFrame.setResizable(false);
		// end credits

		// menu
		final JMenuBar menu = new JMenuBar();
		final JMenu aboutMenu = new JMenu("About");
		aboutMenu.add(peeps);
		menu.add(aboutMenu);

		final JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}});
		aboutMenu.add(exit);

		// other frame organization
		frame.add(bottomStuffWrap, BorderLayout.CENTER);
		controlsWrap.add(controls,BorderLayout.NORTH);
		frame.add(controlsWrap,BorderLayout.EAST);
		frame.add(loadWrap,BorderLayout.NORTH);
		frame.setSize(d);
		frame.setMinimumSize(minD);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(200,200);
		frame.setJMenuBar(menu);

		// file explorer
		final JFileChooser explorer = new JFileChooser();
		FileNameExtensionFilter sprFilter =
				new FileNameExtensionFilter("ALttP Sprite files", new String[] { "spr" });
		// can't clear text due to wonky code
		// have to set a blank file instead

		final File EEE = new File("");
		// TODO: uncomment this for exports
		//explorer.setCurrentDirectory(new File(".")); // quick way to set to current .jar loc

		// clear focusability of all components
		for (Component comp : controls.getComponents()) {
			if (comp instanceof JLabel ||
					comp instanceof JButton ||
					comp instanceof JComboBox) {
				comp.setFocusable(false);
			}
		}

		/*
		 * 
		 * Action listeners
		 * 
		 * 
		 */
		// read steps and count them
		run.addStepListener(new StepListener() {
			public void eventReceived(StepEvent arg0) {
				frameCur.setText(run.frameDis());
			}
		});

		// listen for speed changes
		run.addSpeedListener(new SpeedListener() {
			public void eventReceived(SpeedEvent arg0) {
				if (btnAllowed("speed", run.getMode())) {
					fasterBtn.setEnabled(!run.atMaxSpeed());
					slowerBtn.setEnabled(!run.atMinSpeed());
					speedLevel.setText(run.getSpeedPercent());
				} else {
					speedLevel.setText("");
					fasterBtn.setEnabled(false);
					slowerBtn.setEnabled(false);
				}
			}
		});

		// listen for mode changes
		run.addModeListener(new ModeListener() {
			public void eventReceived(ModeEvent arg0) {
				stepBtn.setEnabled(btnAllowed("step", run.getMode()));
				slowerBtn.setEnabled(btnAllowed("speed", run.getMode()));
				fasterBtn.setEnabled(btnAllowed("speed", run.getMode()));
				if (!btnAllowed("speed", run.getMode())) {
					speedLevel.setText("");
				}
			}
		});

		// listen for Zoom changes
		run.addZoomListener(new ZoomListener() {
			public void eventReceived(ZoomEvent arg0) {
				bigBtn.setEnabled(!run.tooBig());
				lilBtn.setEnabled(!run.vanillaSize());
				zoomLevel.setText("x" + run.getZoom());
			}
		});

		// listen for display changes
		// read steps and count them
		run.addEquipListener(new EquipListener() {
			public void eventReceived(EquipEvent arg0) {
				try {
					run.hardReset();
				} catch (Exception e) {
					// do nothing
				}
			}
		});

		// load sprite file
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				explorer.setSelectedFile(EEE);
				explorer.setFileFilter(sprFilter);
				int option = explorer.showOpenDialog(loadBtn);
				explorer.removeChoosableFileFilter(sprFilter);
				if (option == JFileChooser.CANCEL_OPTION) {
					return;
				}
				String n = "";
				try {
					n = explorer.getSelectedFile().getPath();
				} catch (NullPointerException e) {
					// do nothing
				} finally {
					if (GUIHelpers.testFileType(n,"spr")) {
						fileName.setText(n);
					}
					else {
						return;
					}
				}

				byte[] sprite;
				try {
					sprite = SpriteManipulator.readSprite(fileName.getText());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame,
							"Error reading sprite",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					byte[][][] ebe = SpriteManipulator.sprTo8x8(sprite);
					byte[][] palette = SpriteManipulator.getPal(sprite);
					byte[] src = SpriteManipulator.makeRaster(ebe,palette);

					run.setImage(SpriteManipulator.makeSheet(src));
				} catch(Exception e) {
					JOptionPane.showMessageDialog(frame,
							"Error converting sprite",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					run.setAnimation(0);
					animOptions.setSelectedIndex(0);
					run.reset();
				} catch(Exception e) {

				}
			}});

		// 
		animOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					run.setAnimation(animOptions.getSelectedIndex());
				} catch(Exception e) {
					run.setAnimation(0);
					animOptions.setSelectedIndex(0);
					JOptionPane.showMessageDialog(frame,
							"This animation caused a problem.",
							"OH NO",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				resetBtn.getActionListeners()[0].actionPerformed(
						new ActionEvent(resetBtn, ActionEvent.ACTION_PERFORMED,"",0,0));
				frameMax.setText("/ " + run.maxFrame());
			}});

		modeOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					run.setMode(modeOptions.getSelectedIndex());
					run.reset();
				} catch (Exception e) {
					// do nothing
				}
			}});

		bigBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.embiggen();
			}});

		lilBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.ensmallen();
			}});

		fasterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.faster();
			}});

		slowerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.slower();
			}});

		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					run.repaint();
					run.reset();
				} catch (Exception e) {
					// do nothing
				}
			}});

		stepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.step();
			}});

		equipBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.switchEquipment();
				equipStatus.setText(run.equipmentOn() ? "ON" : "OFF");
			}});

		shadowBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.switchShadow();
				shadowStatus.setText(run.shadowOn() ? "ON" : "OFF");
			}});

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
					case 0 :
					case 2 :
						allowed = false;
						break;
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

	public static String[] getAnimNames() {
		String[] ret = new String[ALLFRAMES.length];
		for (int i = 0; i < ALLFRAMES.length; i++) {
			String r = ALLFRAMES[i];
			String[] animDataX = r.split("[\\[\\]]");
			ret[i] = animDataX[1];
		}
		return ret;
	}

	private void setAllSizes(Component c, Dimension d) {
		c.setPreferredSize(d);
		c.setMaximumSize(d);
		c.setMinimumSize(d);
	}
}