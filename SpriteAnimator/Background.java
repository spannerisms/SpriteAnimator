package SpriteAnimator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Background {
		EMPTY ("Empty", "empty"),
		HOUSE ("House", "house"),
		GRASS ("Grass", "grass"),
		HOULIHAN ("Houlihan", "houlihan"),
		STILL_WATER ("Still water", "stillwater"),
		MOSAIC ("Mosaic", "mosaic"),
		CAVE ("Cave", "cave"),
		SPIKE_CAVE ("Spike cave", "spikecave"),
		TILE_ROOM ("Tile room", "tileroom"),
		DUNGEON_EASTERN_PALACE ("Eastern Palace", "dungeon-easternpalace"),
		DUNGEON_DESERT_PALACE ("Desert Palace", "dungeon-desertpalace"),
		DUNGEON_PALACE_OF_DARKNESS ("Palace of Darkness", "dungeon-palaceofdarkness"),
		DUNGEON_SKULL_WOODS ("Skull Woods", "dungeon-skullwoods"),
		DUNGEON_ICE_PALACE ("Ice Palace", "dungeon-icepalace"),
		DUNGEON_MISERY_MIRE ("Misery Mire", "dungeon-miserymire"),
		DUNGEON_TURTLE_ROCK ("Turtle Rock", "dungeon-turtlerock"),
		BOSS_MOLDORM ("Moldorm", "boss-moldorm"),
		BOSS_ARGHUS ("Arrghus", "boss-arrghus"),
		BOSS_MOTHULA ("Mothula", "boss-mothula"),
		BOSS_BLIND ("Blind", "boss-blind"),
		BOSS_VITREOUS ("Vitreous", "boss-vitreous");

	// local
	private String name;
	private BufferedImage img;

	private Background(String name, String path) {
		this.name = name;
		try {
			String bgFilename = "images/bg-" + path + ".png";
			img = ImageIO.read(Background.class.getResourceAsStream(bgFilename));
		} catch (IOException e) {
		};
	}

	public String toString() {
		return name;
	}
	
	public BufferedImage getImage() {
		return img;
	}
}