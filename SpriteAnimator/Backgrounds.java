package SpriteAnimator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Backgrounds {

	/**
	 * Names of current backgrounds available
	 */
	public static final String[] BACKGROUNDNAMES = {
		"Empty",
		"House",
		"Grass",
		"Houlihan",
		"Still water",
		"Cave",
		"Spike cave",
		"Tile room",
		"Dungeon-Eastern Palace",
		"Dungeon-Desert Palace",
		"Dungeon-Ice Palace",
		"Dungeon-Misery Mire",
		"Dungeon-Turtle Rock",
		"Boss-Moldorm",
		"Boss-Mothula",
		"Custom-Super Mario World" // Added for RyuTech
	};

	/**
	 * Looks for the resources <tt><<b></b>S>_BG.png</tt>
	 * and puts them into a list of images for use.
	 */
	public static final BufferedImage[] getBackgrounds() {
		BufferedImage[] ret = new BufferedImage[BACKGROUNDNAMES.length];
		for (int i = 0; i < ret.length; i++) {
			try {
				String bgFilename = "/images/bg-" + BACKGROUNDNAMES[i].toLowerCase().replace(" ", "") + ".png";
				ret[i] = ImageIO.read(Backgrounds.class.getResourceAsStream(bgFilename));
			} catch (IOException e) {
			};
		}
		return ret;
	}
}