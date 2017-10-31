package SpriteAnimator;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import SpriteManipulator.SpriteManipulator;

public abstract class GUIHelpers {
	
	
	/**
	 * Loads an SPR file and resets the animation
	 * @param fileName - name of file to load
	 * @param selectedAnimation - index of the selected animation
	 * @param run - animation handler
	 * @param frame - main jframe
	 */
	public static void loadSPR(String fileName, int selectedAnimation, SpriteAnimator run, JFrame frame) {
		// read the file
		byte[] sprite;
		try {
			sprite = SpriteManipulator.readFile(fileName);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(frame,
					"Error reading sprite",
					"Oops",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// turn spr into useable images
		try {
			byte[][][] ebe = SpriteManipulator.sprTo8x8(sprite);
			byte[][] palette = SpriteManipulator.getPal(sprite);
			BufferedImage[] mails = SpriteManipulator.makeAllMails(ebe, palette);
			run.setImage(mails);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(frame,
					"Error converting sprite",
					"Oops",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// reset animator, forcing it to update
		try {
			run.setAnimation(selectedAnimation);
		} catch(Exception e) {
			// nothing
		}
	}
	
	/**
	 * gives file extension name from a string
	 * @param s - test case
	 * @return extension type
	 */
	public static String getFileType(String s) {
		String ret = s.substring(s.lastIndexOf(".") + 1);
		return ret;
	}

	/**
	 * Test a file against multiple extensions.
	 * The way <b>getFileType</b> works should allow
	 * both full paths and lone file types to work.
	 * 
	 * @param s - file name or extension
	 * @param type - list of all extensions to test against
	 * @return <tt>true</tt> if any extension is matched
	 */
	public static boolean testFileType(String s, String[] type) {
		boolean ret = false;
		String filesType = getFileType(s);
		for (String t : type) {
			if (filesType.equalsIgnoreCase(t)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * Test a file against a single extension.
	 * 
	 * @param s - file name or extension
	 * @param type - extension
	 * @return <tt>true</tt> if extension is matched
	 */
	public static boolean testFileType(String s, String type) {
		return testFileType(s, new String[] { type });
	}

	/**
	 * Join array of strings together with a delimiter.
	 * @param s - array of strings
	 * @param c - delimiter
	 * @return A single <tt>String</tt>.
	 */
	public static String join(String[] s, String c) {
		String ret = "";
		for (int i = 0; i < s.length; i++) {
			ret += s[i];
			if (i != s.length-1) {
				ret += c;
			}
		}
		return ret;
	}
}