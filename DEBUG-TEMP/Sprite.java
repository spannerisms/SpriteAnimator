package SpriteAnimator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Sprite class to handle drawing better
 */
public class Sprite {
	private int x;
	private int y;
	private String name;
	private BufferedImage img;

	public Sprite(BufferedImage image, int xpos, int ypos) {
		img = image;
		x = xpos;
		y = ypos;
	}

	public Sprite(BufferedImage image, int xpos, int ypos, String t) {
		img = image;
		x = xpos;
		y = ypos;
		name = t;
	}
	
	//TODO REMOVE DEBUG
	public String getVal() {
		return name + "{"+x+","+y+"}";
	}
	public String name() { return name; }
	public void up() {
		if (name.equalsIgnoreCase("SHADOW0")) {
			System.out.println("SHADOW CANNOT BE MOVED");
			return;
		}
		y--;
		}
	public void down() {
		if (name.equalsIgnoreCase("SHADOW0")) {
			System.out.println("SHADOW CANNOT BE MOVED");
			return;
		}
		y++;
		}
	public void right() {
		if (name.equalsIgnoreCase("SHADOW0")) {
			System.out.println("SHADOW CANNOT BE MOVED");
			return;
		}
		x++;
		}
	public void left() {
		if (name.equalsIgnoreCase("SHADOW0")) {
			System.out.println("SHADOW CANNOT BE MOVED");
			return;
		}
		x--;
		}

	/**
	 * Attaches itself to a {@link Graphics2D} object and draws itself accordingly.
	 * @param g - Graphics2D object
	 */
	public void draw(Graphics2D g, int xoffset, int yoffset) {
		g.drawImage(img, x + xoffset, y + yoffset, null);
	}
}