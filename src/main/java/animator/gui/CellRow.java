package animator.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import animator.Sprite;
import animator.database.DrawSize;
import animator.database.SpriteData;
import animator.database.Transformation;

public class CellRow extends JComponent {
	private static final long serialVersionUID = -2029866876659740341L;

	private static final Dimension L_SIZE = new Dimension(40, 36);
	private static final Dimension BIG_SIZE = new Dimension(70, 36);

	private static final BufferedImage CHECKER;
	static {
		BufferedImage temp;
		try {
			temp = ImageIO.read(CellRow.class.getResourceAsStream("/images/meta/checker-small.png"));
		} catch (IOException e) {
			temp = new BufferedImage(192, 448, BufferedImage.TYPE_4BYTE_ABGR);
		}
		CHECKER = temp;
	};

	private final BufferedImage icon;
	private final String cell;
	private final String size;
	private final String trans;

	public CellRow(Sprite s) {
		SpriteData dt = s.getData();

		DrawSize d = dt.d;
		int x,y;

		switch (d) {
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
			case LEFT_HALF:
			case RIGHT_HALF:
			case TOP_LEFT:
			case TOP_RIGHT:
				x = 4;
				break;
			default:
				x = 0;
				break;
		}

		switch (d) {
			case BOTTOM_HALF:
			case BOTTOM_LEFT:
			case BOTTOM_RIGHT:
			case TOP_HALF:
			case TOP_LEFT:
			case TOP_RIGHT:
				y = 4;
				break;
			default:
				y = 0;
				break;
		}

		if (d != DrawSize.FULL) {
			cell = dt.toIndexString();
			size = d.toString();
		} else if (d == DrawSize.EMPTY) {
			cell = "Empty";
			size = "";
		} else {
			cell = dt.toIndexString();
			size = "";
		}

		Transformation t = dt.t;

		if (t != null) {
			trans = t.toString();
		} else {
			trans = "";
		}

		BufferedImage temp = s.getRawImage();
		icon = new BufferedImage(36, 36, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g = icon.createGraphics();
		g.scale(2, 2);
		g.drawImage(CHECKER, 0, 0, null);
		g.drawImage(temp, x + 1, y + 1, null);
		g.dispose();

		initialize();
	}

	private final void initialize() {
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;

		JLabel ico = new JLabel();
		ico.setPreferredSize(L_SIZE);
		ico.setIcon(new ImageIcon(icon));
		c.gridx = 0;
		this.add(ico, c);

		JLabel cellLabel = new JLabel(cell);
		cellLabel.setPreferredSize(L_SIZE);
		c.gridx = 1;
		this.add(cellLabel, c);

		JLabel sizeLabel = new JLabel(size, SwingConstants.CENTER);
		sizeLabel.setPreferredSize(BIG_SIZE);
		c.gridx = 2;
		this.add(sizeLabel, c);

		JLabel transLabel = new JLabel(trans, SwingConstants.CENTER);
		transLabel.setPreferredSize(BIG_SIZE);
		c.gridx = 3;
		this.add(transLabel, c);
		this.setBackground(Color.black);
	}
}