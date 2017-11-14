package SpriteAnimator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import SpriteAnimator.Database.DrawSize;
import SpriteAnimator.Database.SpriteData;
import SpriteAnimator.Database.Transformation;

public class Sprite {
	private int x;
	private int y;
	private BufferedImage img;
	private String[] info;

	public Sprite(BufferedImage i, SpriteData s) {
		x = s.x;
		y = s.y;
		DrawSize d = s.d;
		Transformation t = s.t;
		switch (d) {
			case EMPTY :
				new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR_PRE);
				break;
			default :
				img = i.getSubimage(d.x, d.y, d.w, d.h);
				if (t != null) {
					img = t.trans(img);
				}
				break;
		}

		char[] size = d.name().toLowerCase().toCharArray();
		size[0] = Character.toUpperCase(size[0]);
		char[] trans;
		if (t == null) {
			trans = new char[0];
		} else {
			trans= s.t.name().toLowerCase().toCharArray();
			trans[0] = Character.toUpperCase(trans[0]);
		}

		info = new String[] {
				s.row.name() + s.col,
				size.toString(),
				trans.toString()
		};
	}

	public String[] getInfo() {
		return info;
	}

	/**
	 * Attaches itself to a {@link Graphics2D} object and draws itself accordingly.
	 * @param g - Graphics2D object
	 */
	public void draw(Graphics2D g, int xoffset, int yoffset) {
		g.drawImage(img, x + xoffset, y + yoffset, null);
	}
}