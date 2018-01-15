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
	private String[] info;

	public Sprite(BufferedImage i, SpriteData s) {
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
				if (t != null) {
					img = t.trans(img);
				}
				break;
		}

		// make info
		if (!s.isLinkPart()) {
			return; // just stop if not part of link
		} else if (d == DrawSize.EMPTY) {
			info = new String[] { "Empty step", "", "" }; // unique thing for empty steps
			return; // and then quit here
		}
		char[] size;
		if (d == DrawSize.FULL) {
			size = new char[0]; // don't show "full", as it is an implicit default
		} else {
			size = d.name().toLowerCase().replace("_", "-").toCharArray();
			size[0] = Character.toUpperCase(size[0]);
		}

		char[] trans;
		if (t == null) {
			trans = new char[0]; // don't show "none", as it is an implicit default
		} else {
			trans= s.t.name().toLowerCase().replace("_", "-").toCharArray();
			trans[0] = Character.toUpperCase(trans[0]);
		}

		info = new String[] {
				s.row.name() + s.col,
				String.valueOf(size),
				String.valueOf(trans)
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