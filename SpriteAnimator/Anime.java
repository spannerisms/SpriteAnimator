package SpriteAnimator;

import java.awt.Graphics2D;

import SpriteAnimator.Database.StepData;

public class Anime {
	private int length; // duration
	private Sprite[] list; // list of sprites in frame
	private int xOffset;
	private int yOffset;

	public Anime(Sprite[] spriteList, int duration) {
		length = duration;
		list = spriteList;
		xOffset = 0;
		yOffset = 0;
	}

	public Anime(Sprite[] spriteList, int duration, int x, int y) {
		length = duration;
		
		xOffset = x;
		yOffset = y;
	}

	public Anime(StepData step, Sprite[] spriteList) {
		list = spriteList;
		length = step.l;
		list = new Sprite[step.countSprites()];
		xOffset = 0;
		yOffset = 0;
	}

	public void draw(Graphics2D g, int scaleOffsetX, int scaleOffsetY) {
		for (Sprite s : list) {
			s.draw(g, xOffset + scaleOffsetX, yOffset + scaleOffsetY);
		}
	}

	// TODO: Can this be done without HTML? lol
	public String printAll() {
		String ret = "<html>" +
				"<b>Sprite indices used:</b>" +
				"<table style=\"width: 150px;\">";
		for (int i = list.length - 1; i >= 0; i--) {
			String[] spriteInfo = list[i].getInfo();
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
		return (long) (length * m * SpriteAnimator.FPS);
	}
}