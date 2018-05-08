package animator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Background {
		// empty
		EMPTY ("Empty", "empty"),
		WHITE ("White", "empty (white)"),
		GREEN ("Green screen", "greenscreen"),
		STATIC ("Static", "static", true),
		// overworld areas
		LIGHT_WORLD ("Light World", "lightworld", true),
		DARK_WORLD ("Dark World", "darkworld", true),
		DEATH_MOUNTAIN ("Death Mountain", "deathmountain", true),
		// rooms
		HOUSE ("House", "house"),
		HOULIHAN ("Houlihan", "houlihan"),
		STILL_WATER ("Still water", "stillwater"),
		MOSAIC ("Mosaic", "mosaic", true),
		CAVE ("Cave", "cave", true),
		// meme rooms
		SPIKE_CAVE ("Spike cave", "spikecave", true),
		TILE_ROOM ("Tile room", "tileroom"),
		EXPLORATION_GLITCH ("Exploration glitch", "explorationglitch", true),
		// interfaces
		WORLD_MAP ("World map", "worldmap"),
		GAME_OVER ("Game Over", "gameover"),
		// light world dungeons
		EASTERN_PALACE ("Eastern Palace", "dungeon-easternpalace"),
		DESERT_PALACE ("Desert Palace", "dungeon-desertpalace", true),
		TOWER_OF_HERA ("Tower of Hera", "dungeon-towerofhera", true),
		AGAS_TOWER ("Agahnim's Tower", "dungeon-agastower", true),
		// dark world dungeons
		PALACE_OF_DARKNESS ("Palace of Darkness", "dungeon-palaceofdarkness"),
		SWAMP_PALACE ("Swamp Palace", "dungeon-swamppalace", true),
		SKULL_WOODS ("Skull Woods", "dungeon-skullwoods"),
		THIEVES_TOWN ("Thieves' Town", "dungeon-thievestown", true, 2),
		ICE_PALACE ("Ice Palace", "dungeon-icepalace"),
		MISERY_MIRE ("Misery Mire", "dungeon-miserymire", true),
		TURTLE_ROCK ("Turtle Rock", "dungeon-turtlerock", true),
		GANONS_TOWER ("Ganon's Tower", "dungeon-ganonstower"),
		// boss rooms
		MOLDORM ("Moldorm", "boss-moldorm"),
		ARRGHUS ("Arrghus", "boss-arrghus", true),
		MOTHULA ("Mothula", "boss-mothula"),
		BLIND ("Blind", "boss-blind"),
		VITREOUS ("Vitreous", "boss-vitreous", true);

	// local
	private String name;
	private BufferedImage img;
	boolean animated = false;

	private BufferedImage[] animateds;
	private final int maxFrames;
	private static int frame = 0;

	private Background(String name, String path) {
		maxFrames = 1;
		this.name = name;
		try {
			String bgFilename = String.format("/images/backgrounds/%s.png", path);
			img = ImageIO.read(Background.class.getResourceAsStream(bgFilename));
		} catch (IOException e) {
			// idk what to do here
		};
	}

	private Background(String name, String path, boolean animated) {
		this(name, path, animated, 3);
	}

	private Background(String name, String path, boolean animated, int frames) {
		this.name = name;
		this.animated = animated;
		maxFrames = frames;
		animateds = new BufferedImage[maxFrames];
		for (int i = 0; i < maxFrames; i++) {
			try {
				String bgFilename = String.format("/images/backgrounds/%s.%d.png", path, i);
				animateds[i] = ImageIO.read(Background.class.getResourceAsStream(bgFilename));
			} catch (IOException e) {
				// idk what to do here
			};
		}
	}

	public void step() {
		if (!animated) { return; }
		frame++;
		if (frame == maxFrames) {
			frame = 0;
		}
	}

	public void resetFrame() {
		frame = 0;
	}

	public int getMaxFrame() {
		return maxFrames;
	}

	public String toString() {
		return name;
	}

	public BufferedImage getImage() {
		if (animated) {
			return animateds[frame];
		}
		return img;
	}
}