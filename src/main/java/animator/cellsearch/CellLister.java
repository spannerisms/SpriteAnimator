package animator.cellsearch;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import static animator.cellsearch.CellFrame.ZOOM;
import static SpriteManipulator.SpriteManipulator.SPRITE_SHEET_HEIGHT;
import static SpriteManipulator.SpriteManipulator.SPRITE_SHEET_WIDTH;

public class CellLister extends JComponent {
	private static final long serialVersionUID = -1654440890005532326L;

	private static final Dimension D = new Dimension(SPRITE_SHEET_WIDTH * ZOOM + 2, SPRITE_SHEET_HEIGHT * ZOOM + 2);

	static final BufferedImage LINK;
	static final BufferedImage INDEX_NAMES;
	static final BufferedImage NOTHING;

	static {
		BufferedImage temp;
		try {
			temp = ImageIO.read(CellLister.class.getResourceAsStream(
					"/images/Vanilla Link.png"));
		} catch (IOException e) {
			temp = new BufferedImage(SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		}
		LINK = temp;

		try {
			temp = ImageIO.read(CellLister.class.getResourceAsStream(
					"/images/Index names.png"));
		} catch (IOException e) {
			temp = new BufferedImage(SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		}
		INDEX_NAMES = temp;

		NOTHING = new BufferedImage(SPRITE_SHEET_WIDTH, SPRITE_SHEET_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
	}

	private SpriteBlock[] list = new SpriteBlock[SpriteCell.values().length];
	private SpriteBlock curSel;
	private BufferedImage curSprite;
	private SheetOption curBG = SheetOption.VANILLA;
	private BufferedImage curBGImg = curBG.defaultImage;

	public CellLister() {
		curSprite = SheetOption.SELECTED.defaultImage;
		initialize();
	}

	public void setSprite(BufferedImage i) {
		curSprite = i;
		if (curBG == SheetOption.SELECTED) {
			curBGImg = curSprite;
			repaint();
		}
	}

	public void setOption(SheetOption o) {
		curBG = o;
		switch(o) {
			case VANILLA :
			case INDEX :
				curBGImg = o.defaultImage;
				break;
			case SELECTED :
				curBGImg = curSprite;
				break;
		}
		repaint();
	}

	public void setHideUnused(boolean b) {
		for (SpriteBlock c : list) {
			c.setHideUnused(b);
		}
		repaint();
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(ZOOM, ZOOM);
		g2.drawImage(curBGImg, 0, 0, null);
		for (SpriteBlock c : list) {
			c.paint(g);
		}
	}

	private final void initialize() {
		this.setLayout(null);

		this.setPreferredSize(D);
		this.setMinimumSize(D);
		this.setMaximumSize(D);

		ActionListener selector =
				arg0 -> {
					if (arg0.getID() == 1) {
						SpriteBlock b = (SpriteBlock) arg0.getSource();
						if (curSel == null) {
							// do nothing special
						} else if (curSel == b) {
							return;
						} else {
							curSel.setSelected(false);
						}
						b.setSelected(true);
						curSel = b;
						CellLister.this.fireActionEvent(arg0);
					}
					CellLister.this.revalidate();
					CellLister.this.repaint();
				};

		for (int i = 0; i < list.length; i++) {
			SpriteCell cell = SpriteCell.values()[i];
			SpriteBlock add = new SpriteBlock(cell);
			add.addActionListener(selector);
			this.add(add);
			list[i] = add;
		}
	}

	public synchronized void addActionListener(ActionListener s) {
		listenerList.add(ActionListener.class, s);
	}

	private synchronized void fireActionEvent(ActionEvent e) {
		ActionListener[] listening = listenerList.getListeners(ActionListener.class);
		for (ActionListener al : listening) {
			al.actionPerformed(e);
		}
	}
}