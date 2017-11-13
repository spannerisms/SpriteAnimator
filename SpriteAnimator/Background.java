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
		"Mosaic",
		"Cave",
		"Spike cave",
		"Tile room",
		"Dungeon-Eastern Palace",
		"Dungeon-Desert Palace",
		"Dungeon-Palace of Darkness",
		"Dungeon-Skull Woods",
		"Dungeon-Ice Palace",
		"Dungeon-Misery Mire",
		"Dungeon-Turtle Rock",
		"Boss-Moldorm",
		"Boss-Arrghus",
		"Boss-Mothula",
		"Boss-Blind",
		"Boss-Vitreous"
		};

	/**
	 * Looks for the resources {@code <s>_BG.png}
	 * and puts them into a list of images for use.
	 */
	public static final BufferedImage[] getBackgrounds() {
		BufferedImage[] ret = new BufferedImage[BACKGROUNDNAMES.length];
		for (int i = 0; i < ret.length; i++) {
			try {
				String bgFilename = "images/bg-" + BACKGROUNDNAMES[i].toLowerCase().replace(" ", "") + ".png";
				ret[i] = ImageIO.read(Backgrounds.class.getResourceAsStream(bgFilename));
			} catch (IOException e) {
			};
		}
		return ret;
	}
}
