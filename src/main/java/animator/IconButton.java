package animator;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class IconButton extends JRadioButton {
	private static final long serialVersionUID = -5000882409015582760L;

	private static final int D_SIZE = 24;
	private static final Dimension D = new Dimension(D_SIZE, D_SIZE);

	private static final Border BORDER_ON = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	private static final Border BORDER_OFF = BorderFactory.createBevelBorder(BevelBorder.RAISED);

	private static final Color BG_ON = new Color(192, 224, 192);
	private static final Color BG_OFF = new Color(224, 224, 224);
	private static final Color BG_HOVER = new Color(120, 192, 248);

	public IconButton(Image b) {
		super();
		this.setPreferredSize(D);
		this.setMinimumSize(D);
		this.setMaximumSize(D);

		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setIcon(new ImageIcon(b));

		setBorderPainted(true);
		this.setBorder(BORDER_OFF);
		this.setBackground(BG_OFF);

		this.addChangeListener(
			arg0 -> {
				if (this.isSelected()) {
					this.setBackground(BG_ON);
					this.setBorder(BORDER_ON);
				} else {
					this.setBackground(BG_OFF);
					this.setBorder(BORDER_OFF);
				}
			});
		this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}

			public void mouseEntered(MouseEvent e) {
				setBackground(BG_HOVER);
			}

			public void mouseExited(MouseEvent e) {
				if (isSelected()) {
					setBackground(BG_ON);
				} else {
					setBackground(BG_OFF);
				}
			}
		});
	}
}