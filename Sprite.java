package SpriteAnimator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Sprite class to handle drawing better
 */
public class Sprite {
		int x;
		int y;
		int z;
		BufferedImage img;
		public Sprite(BufferedImage image, int xpos, int ypos) {
			img = image;
			x = xpos;
			y = ypos;
		}
		
		/**
		 * Attaches itself to a {@link Graphics2D} object and draws itself accordingly.
		 * @param g - Graphics2D object
		 */
		public void draw(Graphics2D g) {
			g.drawImage(img, x + 10, y + 10, null);
		}
}
