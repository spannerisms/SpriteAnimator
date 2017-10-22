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
	static final SprManipulator SprManip = new SprManipulator();
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
		controls.add(lilBtn,c);
		c.gridx = 1;
		controls.add(new JPanel(), c); // filler
		c.gridx = 2;
		controls.add(bigBtn,c);

		// speed
		c.gridx = 0;
		c.gridy++;
		controls.add(slowerBtn,c);
		c.gridx = 1;
		controls.add(new JPanel(), c); // filler
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

		Dimension dd = new Dimension(20,20);
		frameCur.setPreferredSize(dd);
		frameCur.setMaximumSize(dd);
		frameCur.setMinimumSize(dd);

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
		run.addStepListener(new StepListener() {
			public void eventReceived(StepEvent arg0) {
				frameCur.setText(run.frameDis());
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
					sprite = SprManip.readSprite(fileName.getText());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame,
							"Error reading sprite",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					byte[][][] ebe = SprManip.sprTo8x8(sprite);
					byte[][] palette = SprManip.getPal(sprite);
					byte[] src = SprManip.makeRaster(ebe,palette);

					run.setImage(SprManip.makeSheet(src));
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
				int animMode;
				try {
					run.setMode(modeOptions.getSelectedIndex());
					animMode = run.getMode();
					run.reset();
				} catch (Exception e) {
					animMode = modeOptions.getSelectedIndex();
				}
				// button disabling
				switch(animMode) {
					case 0 :
						fasterBtn.setEnabled(true);
						slowerBtn.setEnabled(true);
						resetBtn.setEnabled(true);
						stepBtn.setEnabled(false);
						break;
					case 1 :
						fasterBtn.setEnabled(false);
						slowerBtn.setEnabled(false);
						resetBtn.setEnabled(true);
						stepBtn.setEnabled(true);
						break;
					case 2 :
						fasterBtn.setEnabled(false);
						slowerBtn.setEnabled(false);
						resetBtn.setEnabled(true);
						stepBtn.setEnabled(false);
						break;
				}
			}});

		bigBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lilBtn.setEnabled(true);
				if (run.embiggen()) {
					bigBtn.setEnabled(false);
				}
			}});

		lilBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bigBtn.setEnabled(true);
				if (run.ensmallen()) {
					lilBtn.setEnabled(
							false);
				}
			}});

		fasterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				slowerBtn.setEnabled(true);
				if (run.faster()) {
					fasterBtn.setEnabled(false);
				}
			}});

		slowerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fasterBtn.setEnabled(true);
				if (run.slower()) {
					slowerBtn.setEnabled(false);
				}
			}});

		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int animMode = run.getMode();
				try {
					run.repaint();
					run.reset();
				} catch (Exception e) {
					// do nothing
				}
				// button disabling
				switch (animMode) {
					case 0 :
						fasterBtn.setEnabled(true);
						slowerBtn.setEnabled(true);
						break;
					case 1 :
						// nothing
						break;
					case 2 :
						// nothing
						break;
				}
			}});

		stepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.step();
			}});

		// turn on
		frame.setVisible(true);
	}
}