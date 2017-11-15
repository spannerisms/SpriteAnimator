package SpriteAnimator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Background {
		// empty
		EMPTY ("Empty", "empty"),
		// overworld areas
		HOUSE ("House", "house"),
		GRASS ("Grass", "grass"),
		HOULIHAN ("Houlihan", "houlihan"),
		STILL_WATER ("Still water", "stillwater"),
		MOSAIC ("Mosaic", "mosaic"),
		CAVE ("Cave", "cave"),
		// meme rooms
		SPIKE_CAVE ("Spike cave", "spikecave"),
		TILE_ROOM ("Tile room", "tileroom"),
		// light world dungeons
		EASTERN_PALACE ("Eastern Palace", "dungeon-easternpalace"),
		DESERT_PALACE ("Desert Palace", "dungeon-desertpalace"),
		TOWER_OF_HERA ("Tower of Hera", "dungeon-towerofhera"),
		// dark world dungeons
		PALACE_OF_DARKNESS ("Palace of Darkness", "dungeon-palaceofdarkness"),
		SWAMP_PALACE ("Swamp Palace", "dungeon-swamppalace"),
		SKULL_WOODS ("Skull Woods", "dungeon-skullwoods"),
		THIEVES_TOWN ("Thieves' Town", "dungeon-thievestown"),
		ICE_PALACE ("Ice Palace", "dungeon-icepalace"),
		MISERY_MIRE ("Misery Mire", "dungeon-miserymire"),
		TURTLE_ROCK ("Turtle Rock", "dungeon-turtlerock"),
		GANONS_TOWER ("Ganon's Tower", "dungeon-ganonstower"),
		// boss rooms
		MOLDORM ("Moldorm", "boss-moldorm"),
		ARGHUS ("Arrghus", "boss-arrghus"),
		MOTHULA ("Mothula", "boss-mothula"),
		BLIND ("Blind", "boss-blind"),
		VITREOUS ("Vitreous", "boss-vitreous");

	// local
	private String name;
	private BufferedImage img;

	private Background(String name, String path) {
		this.name = name;
		try {
			String bgFilename = "/images/bg-" + path + ".png";
			img = ImageIO.read(Background.class.getResourceAsStream(bgFilename));
		} catch (IOException e) {
			// idk what to do here
		};
	}

	public String toString() {
		return name;
	}

	public BufferedImage getImage() {
		return img;
	}
}