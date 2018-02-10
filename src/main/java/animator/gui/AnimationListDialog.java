package animator.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import animator.database.Animation;
import static animator.database.Animation.Category;
import static javax.swing.SpringLayout.*;

public class AnimationListDialog extends JDialog {
	private static final long serialVersionUID = 4660210877407374764L;
	private static final Dimension D = new Dimension(470, 500);

	private final ActionListener doThing;
	private final ArrayList<AnimButton> allButtons = new ArrayList<AnimButton>();
	private Animation selected;

	public AnimationListDialog(JFrame frame, ActionListener a) {
		super(frame, "Animation list");
		doThing = a;
		initialize();
		selected = Animation.STAND;
	}

	private final void initialize() {
		SpringLayout l = new SpringLayout();
		this.setLayout(l);
		this.setPreferredSize(D);
		this.setMinimumSize(D);

		Category[] v = Category.values();
		AnimPanel[] catPanels = new AnimPanel[v.length];

		for (int i = 0; i < v.length; i++) {
			catPanels[i] = new AnimPanel(v[i], doThing);
		}

		PrettyBox<AnimPanel> catList = new PrettyBox<AnimPanel>(catPanels);
		JPanel listWrap = new JPanel();
		JScrollPane wrap = new JScrollPane(listWrap);

		JPanel w = (JPanel) this.getContentPane();
		l.putConstraint(NORTH, catList, 5, NORTH, w);
		l.putConstraint(EAST, catList, -5, EAST, w);
		l.putConstraint(WEST, catList, 5, WEST, w);
		w.add(catList);

		l.putConstraint(NORTH, wrap, 5, SOUTH, catList);
		l.putConstraint(SOUTH, wrap, -5, SOUTH, w);
		l.putConstraint(EAST, wrap, 0, EAST, catList);
		l.putConstraint(WEST, wrap, 0, WEST, catList);
		w.add(wrap);

		catList.addItemListener(
			arg0 -> {
				listWrap.removeAll();
				listWrap.add((AnimPanel) catList.getSelectedItem());
				this.revalidate();
				this.repaint();
			});

		listWrap.add(catPanels[0]);
	}

	public void setAnimation(Animation a) {
		for (AnimButton b : allButtons) {
			if (a == b.anim) {
				b.setSelected(true);
			} else if (selected == b.anim) {
				b.setSelected(false);
			}
		}
		selected = a;
		revalidate();
		repaint();
	}

	@SuppressWarnings("serial")
	private class AnimPanel extends JPanel {
		final String name;
		AnimPanel(Category ctg, ActionListener al) {
			super();
			this.name = ctg.name;

			this.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(0, 0, 1, 1);
			c.gridy = -1;
			c.gridx = -1;
			c.fill = GridBagConstraints.BOTH;
			Animation prev = null;

			for (Animation a : ctg.getList()) {
				if (a.compareVague(prev)) {
					c.gridx++;
				} else {
					c.gridy++;
					c.gridx = 0;
				}
				String text;
				if (c.gridx == 0) {
					text = a.toString();
				} else {
					text = a.getDisambig();
				}
				AnimButton j = new AnimButton(text, a, al);
				this.add(j, c);
				prev = a;
			}
		}

		public String toString() {
			return name;
		}
	}

	@SuppressWarnings("serial")
	class AnimButton extends PrettyButton {
		final Animation anim;

		public AnimButton(String text, Animation a, ActionListener l) {
			super(text);
			this.setVerticalAlignment(PrettyButton.EAST);
			anim = a;
			this.addActionListener(l);
			allButtons.add(this);
		}
	}
}