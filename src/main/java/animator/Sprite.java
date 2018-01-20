package animator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import animator.database.DrawSize;
import animator.database.SpriteData;
import animator.database.Transformation;

public class Sprite {
	// class constants
	public static final int CELL_SIZE = 16;

	// local vars
	private int x;
	private int y;
	private BufferedImage img;
	private BufferedImage rawimg;
	private final SpriteData data;

	public Sprite(BufferedImage i, SpriteData s) {
		data = s;
		x = s.x;
		y = s.y;
		DrawSize d = s.d;
		Transformation t = s.t;
		switch (d) {
			case EMPTY :
				img = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR_PRE);
				break;
			default :
				int r = s.row.val * CELL_SIZE;
				int c = s.col * CELL_SIZE;
				img = i.getSubimage(c + d.x, r + d.y, d.w, d.h);
				rawimg = i.getSubimage(c + d.x, r + d.y, d.w, d.h);
				if (t != null) {
					img = t.trans(img);
				}
				break;
		}
	}

	public BufferedImage getRawImage() {
		return rawimg;
	}

	public SpriteData getData() {
		return data;
	}

	/**
	 * Attaches itself to a {@link Graphics2D} object and draws itself accordingly.
	 * @param g - Graphics2D object
	 */
	public void draw(Graphics2D g, int xoffset, int yoffset) {
		g.drawImage(img, x + xoffset, y + yoffset, null);
	}
}