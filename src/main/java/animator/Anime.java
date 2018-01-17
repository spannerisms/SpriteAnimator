package animator;

import java.awt.Graphics2D;

import animator.database.StepData;

public class Anime {
	private int length; // duration
	private Sprite[] list; // list of sprites in frame

	public Anime(StepData step, Sprite[] spriteList) {
		list = spriteList;
		length = step.l;
	}

	public void draw(Graphics2D g, int offsetX, int offsetY) {
		for (Sprite s : list) {
			s.draw(g, offsetX, offsetY);
		}
	}

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

	public int getLength() {
		return length;
	}

	/**
	 * Send length of current frame
	 * @param m - speed multiplier
	 */
	public long nextTick(double m) {
		return (long) (length * m * SpriteAnimator.FPS);
	}
}