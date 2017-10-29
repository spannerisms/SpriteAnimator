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

	// TODO: Can this be done without HTML? lol
	public String printAll() {
		String ret = "<html>" +
				"<b>Sprites used:</b>" +
				"<table style=\"width: 150px;\">";
		for (int i = l.length - 1; i >= 0; i--) {
			String[] spriteInfo = l[i].getInfo();
			if (spriteInfo != null) {
				ret += "<tr>" +
						"<td style=\"width: 20%; text-align: right;\">" + spriteInfo[0] + "</td>" +
						"<td style=\"width: 40%;\">" + spriteInfo[1] + "</td>" +
						"<td style=\"width: 40%;\">" + spriteInfo[2] + "</td>" +
						"</tr>";
			}
		}

		ret += "</table></html>";
		return ret;
	}

	/**
	 * Send length of current frame.
	 * @param m - multiplier
	 */	
	public long nextTick(double m) {
		return (long) (d * m * SpriteAnimator.FPS);
	}
}