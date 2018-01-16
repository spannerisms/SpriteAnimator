package animator.cellsearch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import animator.database.Animation;

import static animator.cellsearch.CellFrame.ZOOM;

public class SpriteBlock extends JComponent {
	private static final long serialVersionUID = 2627372484710178104L;

	static final Color UNUSED_COLOR =
			new Color(0, 0, 0, 200);
	static final Color UNUSED_BORDER =
			new Color(UNUSED_COLOR.getRed(), UNUSED_COLOR.getGreen(), UNUSED_COLOR.getBlue(), 255);

	static final Color HOVER_COLOR =
			new Color(255, 255, 0, 120);
	static final Color HOVER_BORDER =
			new Color(HOVER_COLOR.getRed(), HOVER_COLOR.getGreen(), HOVER_COLOR.getBlue(), 255);

	static final Color SELECTED_COLOR =
			new Color(0, 200, 255, 100);
	static final Color SELECTED_BORDER =
			new Color(SELECTED_COLOR.getRed(), SELECTED_COLOR.getGreen(), SELECTED_COLOR.getBlue(), 255);

	static final Color IN_ANIM_COLOR = new Color(0, 255, 120, 130);
	static final Color IN_ANIM_BORDER =
			new Color(IN_ANIM_COLOR.getRed(), IN_ANIM_COLOR.getGreen(), IN_ANIM_COLOR.getBlue(), 255);

	// locals
	public final int x;
	public final int y;
	public final int xmod;
	public final int ymod;
	public final int w;
	public final int h;

	public boolean hovered = false;
	public boolean selected = false;
	public boolean hideUnused = false;
	public boolean listed = false;
	public boolean showListed = false;
	private final boolean unused;
	private final BufferedImage index;
	public final ImageIcon indexIcon;

	public final ActionEvent CLICK = new ActionEvent(this, 1, "click");
	public final ActionEvent HOV = new ActionEvent(this, 2, "hover");

	public final SpriteCell cell;

	public SpriteBlock(SpriteCell s) {
		this.cell = s;
		unused = !s.isUsed;

		x = s.c * 16 + s.d.x;
		y = s.row.val  * 16 + s.d.y;
		xmod = (x / 16) * 16;
		ymod = (y / 16) * 16;

		w = s.d.w - 1;
		h = s.d.h - 1;

		this.setBounds(x * ZOOM, y * ZOOM, (w + 1) * ZOOM, (h + 1) * ZOOM);
		index = CellLister.INDEX_NAMES.getSubimage(xmod, ymod, 16, 16);
		indexIcon = new ImageIcon(index);
		addMouse();
	}

	public void setHideUnused(boolean b) {
		hideUnused = b;
	}

	public void paintBottom(Graphics g) {
		if (listed && showListed) {
			g.setColor(IN_ANIM_COLOR);
			g.fillRect(x, y, w, h);
			g.setColor(IN_ANIM_BORDER);
			g.drawRect(x, y, w, h);
		}
	}

	public void paint(Graphics g) {
		if (hideUnused && unused) {
			g.setColor(UNUSED_COLOR);
			g.fillRect(x, y, w, h);
			g.setColor(UNUSED_BORDER);
			g.drawRect(x, y, w, h);
		}

		if (hovered) {
			g.drawImage(index, xmod, ymod, null);

			g.setColor(HOVER_COLOR);
			g.fillRect(x, y, w, h);
			g.setColor(HOVER_BORDER);
			g.drawRect(x, y, w, h);
		}

		if (selected) {
			g.setColor(SELECTED_COLOR);
			g.fillRect(x, y, w, h);
			g.setColor(SELECTED_BORDER);
			g.drawRect(x, y, w, h);
		}
	}

	public void setSelected(boolean b) {
		selected = b;
	}

	public void setShowListed(boolean b) {
		showListed = b;
	}

	public void setUsedInAnimation(Animation a) {
		listed = cell.isUsedInAnimation(a);
		repaint();
	}

	private final void addMouse() {
		this.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent arg0) {
				fireActionEvent(CLICK);
			}

			public void mouseClicked(MouseEvent arg0) {}

			public void mouseEntered(MouseEvent arg0) {
				hovered = true;
				fireActionEvent(HOV);
			}

			public void mouseExited(MouseEvent arg0) {
				hovered = false;
				fireActionEvent(HOV);
			}

			public void mouseReleased(MouseEvent arg0) {}
		});
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