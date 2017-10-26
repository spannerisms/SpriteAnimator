package SpriteAnimator;

import java.awt.Graphics2D;

public class Anime {
	private int d; // duration
	private Sprite[] l; // list of sprites in frame
	private int xOffset;
	private int yOffset;
	public Anime(Sprite[] spriteList, int duration) {
		d = duration;
		l = spriteList;
		xOffset = 0;
		yOffset = 0;
	}

	public Anime(Sprite[] spriteList, int duration, int x, int y) {
		d = duration;
		l = spriteList;
		xOffset = x;
		yOffset = y;
	}

	public void draw(Graphics2D g, int scaleOffset) {
		for (Sprite s : l) {
			s.draw(g, xOffset + scaleOffset, yOffset + scaleOffset);
		}
	}

	public Sprite[] list() {
		return l;
	}
	
	public void print() {
		for (int i = l.length-1; i != 0; i--) {
			System.out.println("Sprite: " + (l[i].getVal()));
		}
	}
	/**
	 * Send length of current frame.
	 * @param m - multiplier
	 */	
	public long nextTick(double m) {
		return (long) (d * m * SpriteAnimator.FPS);
	}
}