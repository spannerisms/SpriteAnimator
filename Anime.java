package SpriteAnimator;

import java.awt.Graphics2D;

import javax.swing.Timer;

/**
 * Frame class to handle drawing even more better
*/
public class Anime {
	int d; // duration
	Sprite[] l; // list of sprites in frame
	
	public Anime(Sprite[] spriteList, int duration) {
		d = duration;
		l = spriteList;
	}
	
	public void draw(Graphics2D g) {
		for (Sprite s : l)
			s.draw(g);
	}
	
	/**
	 * Set the timer to use the frame's lenght as its delay.
	 * @param t - Timer object
	 * @param m - multiplier
	 */
	public void setNextTick(Timer t, double m) {
		t.setDelay((int) (d * m * SpriteAnimator.FPS));
	}
}
