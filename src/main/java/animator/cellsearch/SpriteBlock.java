package animator.cellsearch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

import static animator.cellsearch.CellFrame.ZOOM;

public class SpriteBlock extends JComponent {
	private static final long serialVersionUID = 2627372484710178104L;

	static final Color UNUSED_BLACK = new Color(0, 0, 0, 255);
	static final Color HOVER_YELLOW = new Color(255, 255, 0, 200);
	static final Color SELECTED_BLUE = new Color(0, 0, 255, 255);

	public final int x;
	public final int y;
	public final int w;
	public final int h;

	public boolean hovered = false;
	public boolean selected = false;
	public boolean hideUnused = false;
	private final boolean unused;

	public final ActionEvent CLICK = new ActionEvent(this, 1, "click");
	public final ActionEvent HOV = new ActionEvent(this, 2, "click");

	public final SpriteCell cell;

	public SpriteBlock(SpriteCell s) {
		this.cell = s;
		unused = !s.isUsed;

		x = s.c * 16 + s.d.x;
		y = s.row.val  * 16 + s.d.y;

		w = s.d.w;
		h = s.d.h;

		this.setBounds(x * ZOOM + 1, y * ZOOM + 1, w * ZOOM, h * ZOOM);

		addMouse();
	}

	public void setHideUnused(boolean b) {
		hideUnused = b;
	}

	public void paint(Graphics g) {
		if (hideUnused && unused) {
			g.setColor(UNUSED_BLACK);
			g.fillRect(x, y, w, h);
		}

		if (hovered) {
			g.setColor(HOVER_YELLOW);
			g.fillRect(x, y, w, h);
		}

		if (selected) {
			g.setColor(SELECTED_BLUE);
			g.drawRect(x, y, w, h);
		}
	}

	public void setSelected(boolean b) {
		selected = b;
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