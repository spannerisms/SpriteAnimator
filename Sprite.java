package SpriteAnimator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Sprite class to handle drawing better
 */
public class Sprite {
	private int x;
	private int y;
	private String name;
	private String size;
	private String trans;
	private BufferedImage img;
	private String[] info;
	public Sprite(BufferedImage image, int xpos, int ypos, String t, String s, String tr) {
		img = image;
		x = xpos;
		y = ypos;
		name = t;
		size = s;
		trans = tr;
		makeInfo();
	}

	/**
	 * If the sprite is part of link, it is listed. If it isn't, send out null.
	 */
	private final static String[][] FLAGS_TO_VAL = {
			// ignore Flag 'F' - full 16x16 is implicit default
			{ "T", "Top half" },
			{ "B", "Bottom half" },
			{ "R", "Right half" },
			{ "L", "Left half" },
			{ "TR", "Top-right" },
			{ "TL", "Top-left" },
			{ "BR", "Bottom-right" },
			{ "BL", "Bottom-left" },
			{ "E", "" },
	};
	private final static String[][] TRANS_TO_VAL = {
			// ignore Flag '0' - no transformation is implicit default
			{ "U", "X-flipped" },
			{ "M", "Y-flipped" },
			{ "UM", "XY-flipped" },
			{ "MU", "XY-flipped" },
	};

	private void makeInfo() {
		// Remove ZAP palette references
		String n = name.replace("ZAP", "");
		// long strings aren't part of Link, so ignore.
		if (n.length() > 2) {
			info = null;
			return;
		}
		// replace Alpha and Beta with Mike's index codes
		if (n.equalsIgnoreCase("α")) {
			n = "AA";
		} else if (n.equalsIgnoreCase("β")) {
			n = "AB";
		} else { // pad all other indices with a single space
			n = " " + n;
		}
		// get draw size
		String flagW = "";
		if (size.equalsIgnoreCase("E")) {
			n = "Empty frame";
		} else {
			for (String[] f : FLAGS_TO_VAL) {
				if (size.equalsIgnoreCase(f[0])) {
					flagW = f[1];
					break;
				}
			}
		}

		// get transformation
		String transW = "";

		// get draw size
		for (String[] t : TRANS_TO_VAL) {
			if (trans.equalsIgnoreCase(t[0])) {
				transW = t[1];
				break;
			}
		}
		
		info = new String[] {n,flagW,transW};
	}

	public String[] getInfo() {
		return info;
	}

	/**
	 * Attaches itself to a {@link Graphics2D} object and draws itself accordingly.
	 * @param g - Graphics2D object
	 */
	public void draw(Graphics2D g, int xoffset, int yoffset) {
		g.drawImage(img, x + xoffset, y + yoffset, null);
	}
}