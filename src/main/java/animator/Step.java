package animator;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import animator.database.StepData;
import animator.gui.CellRow;

public class Step {
	private final int length; // duration
	private final Sprite[] list; // list of sprites in frame
	private final Container printer; // graphical list

	public Step(StepData step, Sprite[] spriteList) {
		list = spriteList;
		length = step.l;

		printer = new Container();
		printer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.ipady = 2;
		c.gridy = 0;
		c.gridx = 0;

		for (int i = list.length - 1; i >= 0; i--) { // in reverse for z-order
			Sprite s = list[i];
			if (!s.getData().isLinkPart()) {
				continue;
			}
			printer.add(new CellRow(s), c);
			c.gridy++;
		}
	}

	public void draw(Graphics2D g, int offsetX, int offsetY) {
		for (Sprite s : list) {
			s.draw(g, offsetX, offsetY);
		}
	}

	public Container printAll() {
		return printer;
	}

	public int getLength() {
		return length;
	}

	/**
	 * Send length of current frame
	 * @param m - speed multiplier
	 */
	public long nextTick(double m) {
		return (long) (length * m * SpriteAnimator.FPS);
	}
}