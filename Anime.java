package SpriteAnimator;

import java.awt.Graphics2D;
/**
 * Frame class to handle drawing even more better
*/
public class Anime {
	private int d; // duration
	private Sprite[] l; // list of sprites in frame

	public Anime(Sprite[] spriteList, int duration) {
		d = duration;
		l = spriteList;
	}

	public void draw(Graphics2D g) {
		for (Sprite s : l)
			s.draw(g);
	}

	/**
	 * Send length of current frame.
	 * @param m - multiplier
	 */	
	public long nextTick(double m) {
		return (long) (d * m * SpriteAnimator.FPS);
	}
}