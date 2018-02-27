package animator.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class PrettyButton extends JButton {
	private static final long serialVersionUID = 5718321084051481555L;

	private static final Border BORDER_ON_OUT = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	private static final Border BORDER_OFF_OUT = BorderFactory.createBevelBorder(BevelBorder.RAISED);
	private static final Border BORDER_IN = BorderFactory.createEmptyBorder(2, 16, 2, 16);

	private static final CompoundBorder BORDER_ON = new CompoundBorder(BORDER_ON_OUT, BORDER_IN);
	private static final CompoundBorder BORDER_OFF = new CompoundBorder(BORDER_OFF_OUT, BORDER_IN);

	private static final Color BG_ON = new Color(192, 224, 192);
	private static final Color BG_OFF = new Color(224, 224, 224);

	private CompoundBorder curBorder;
	private boolean selected = false;

	public PrettyButton(String s) {
		super(s);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);

		this.setBorderPainted(true);
		this.setBorder(BORDER_OFF);
		this.setBackground(BG_OFF);

		this.setHorizontalTextPosition(SwingConstants.LEFT);
		this.setIconTextGap(1);

		this.setFocusable(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		curBorder = BORDER_OFF;

		this.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent e) {
				setBorder(BORDER_ON);
			}

			public void mouseReleased(MouseEvent e) {
				setBorder(curBorder);
			}

			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
	}

	public void setSelected(boolean e) {
		selected = e;
		curBorder = selected ? BORDER_ON : BORDER_OFF;
		setBorder(curBorder);
	}
}