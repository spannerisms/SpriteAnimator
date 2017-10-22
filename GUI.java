package SpriteAnimator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class GUI {
	static final GuiHelpers GH = new GuiHelpers();
	static final SprManipulator SPRMANIP = new SprManipulator();
	static final String[] ALLFRAMES = Database.ALLFRAMES;
	static final String[] MODES = {
			"Normal play",
			"Step-by-step",
			"All frames"
	};

	private static final JComboBox<String> modeOptions =
			new JComboBox<String>(MODES);

	private static final JComboBox<String> animOptions =
			new JComboBox<String>(getAnimNames());

	public static String[] getAnimNames() {
		String[] ret = new String[ALLFRAMES.length];
		for (int i = 0; i < ALLFRAMES.length; i++) {
			String r = ALLFRAMES[i];
			String[] animDataX = r.split("[\\[\\]]");
			ret[i] = animDataX[1];
		}
		return ret;
	}

	public void printGUI(String[] args) throws IOException {
		//try to set Nimbus
		try {
			NimbusLookAndFeel lookAndFeel = new NimbusLookAndFeel();
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (UnsupportedLookAndFeelException e) {
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
		final Dimension d = new Dimension(400,282);
		final JTextField fileName = new JTextField("");
		final JButton loadBtn = new JButton("Load SPR");
		final JButton stepBtn = new JButton("Step");
		final JButton fasterBtn = new JButton("Speed+");
		final JButton slowerBtn = new JButton("Speed-");
		final JButton bigBtn = new JButton("Zoom+");
		final JButton lilBtn = new JButton("Zoom-");
		final JButton resetBtn = new JButton("Reset");
		final JLabel frameCur = new JLabel("1");
		final JLabel frameMax = new JLabel("/ 1");
		final JLabel zoomLevel = new JLabel("x3");
		final JLabel speedLevel = new JLabel("100%");
		final JPanel loadWrap = new JPanel(new BorderLayout());
		final JPanel controls = new JPanel(new GridBagLayout());
		final JPanel controlsWrap = new JPanel(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		// row 1 and 2 : comboboxes
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		controls.add(animOptions,c);
		c.gridy++;
		controls.add(modeOptions,c);

		// row 3 : zoom
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		controls.add(zoomLevel,c);
		c.gridx = 1;
		controls.add(lilBtn, c); // filler
		c.gridx = 2;
		controls.add(bigBtn,c);

		// speed
		c.gridx = 0;
		c.gridy++;
		controls.add(speedLevel,c);
		c.gridx = 1;
		controls.add(slowerBtn, c); // filler
		c.gridx = 2;
		controls.add(fasterBtn,c);

		// step
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy++;
		controls.add(stepBtn,c);

		// reset
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy++;
		controls.add(resetBtn,c);

		// frame counter
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		c.weightx = 9;
		controls.add(new JLabel("Frame:"),c);
		c.weightx = 0;
		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;

		// text box sizing
		Dimension dd = new Dimension(20,20);
		frameCur.setPreferredSize(dd);
		frameCur.setMaximumSize(dd);
		frameCur.setMinimumSize(dd);
		zoomLevel.setPreferredSize(dd);
		zoomLevel.setMaximumSize(dd);
		zoomLevel.setMinimumSize(dd);
		speedLevel.setPreferredSize(dd);
		speedLevel.setMaximumSize(dd);
		speedLevel.setMinimumSize(dd);

		controls.add(frameCur,c);
		c.gridx = 2;
		controls.add(frameMax,c);

		final JPanel bottomStuffWrap = new JPanel(new BorderLayout());
		final JPanel bottomStuff = new JPanel(new BorderLayout());
		stepBtn.setEnabled(false);

		final SpriteAnimator imageArea = new SpriteAnimator();
		final SpriteAnimator run = imageArea; // just a shorter name

		bottomStuffWrap.add(imageArea,BorderLayout.CENTER);
		bottomStuffWrap.add(bottomStuff,BorderLayout.EAST);
		loadWrap.add(loadBtn,BorderLayout.EAST);
		loadWrap.add(fileName,BorderLayout.CENTER);

		// Credits
		final JFrame aboutFrame = new JFrame("About");
		final JMenuItem peeps = new JMenuItem("About");
		final TextArea peepsList = new TextArea("", 0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		peepsList.setEditable(false);
		peepsList.append("Written by fatmanspanda"); // hey, that's me
		peepsList.append("\n\nFrame resources:\n");
		peepsList.append("http://alttp.mymm1.com/sprites/includes/animations.txt\n");
		peepsList.append(GH.join(new String[]{
				"\tMikeTrethewey", // it's mike
				"TWRoxas", // provided most valuable documentation
				}, ", "));// forced me to do this and falls in every category
		peepsList.append("\n\nAnimation research:\n\tRyuTech");
		peepsList.append("\n\nCode contribution:\n");
		peepsList.append(GH.join(new String[]{
				"MikeTretheway", // God dammit, so being so helpful
				"Zarby89", // spr conversion
				}, ", "));
		peepsList.append("\n\nResources and development:\n");
		peepsList.append(GH.join(new String[]{
				"Veetorp", // provided most valuable documentation
				"Zarby89", // various documentation and answers
				"Sosuke3" // various snes code answers
				}, ", "));
		peepsList.append("\n\nTesting and feedback:\n");
		peepsList.append(GH.join(new String[]{
				"Jighart",
				}, ", "));
		aboutFrame.add(peepsList);
		final JMenuBar menu = new JMenuBar();
		menu.add(peeps);
		peeps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aboutFrame.setVisible(true);
			}});
		aboutFrame.setSize(600,300);
		aboutFrame.setLocation(150,150);
		aboutFrame.setResizable(false);
		// end credits

		frame.add(bottomStuffWrap, BorderLayout.CENTER);
		controlsWrap.add(controls,BorderLayout.NORTH);
		frame.add(controlsWrap,BorderLayout.EAST);
		frame.add(loadWrap,BorderLayout.NORTH);
		frame.setSize(d);
		frame.setMinimumSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLocation(300,300);
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
					if (GH.testFileType(n,"spr")) {
						fileName.setText(n);
					}
					else {
						return;
					}
				}

				byte[] sprite;
				try {
					sprite = SPRMANIP.readSprite(fileName.getText());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame,
							"Error reading sprite",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					byte[][][] ebe = SPRMANIP.sprTo8x8(sprite);
					byte[][] palette = SPRMANIP.getPal(sprite);
					byte[] src = SPRMANIP.makeRaster(ebe,palette);

					run.setImage(SPRMANIP.makeSheet(src));
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
}