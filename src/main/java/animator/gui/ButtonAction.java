package animator.gui;

import javax.swing.KeyStroke;
import static java.awt.event.KeyEvent.*;

enum ButtonAction {
	STEP (VK_CLOSE_BRACKET),
	STEP_BACK (VK_OPEN_BRACKET),
	PLAY (0),
	PAUSE (0),
	PLAY_PAUSE (VK_SPACE),
	RESET (VK_EQUALS),
	NEXT_ANIM (VK_PERIOD),
	PREV_ANIM (VK_COMMA),
	;

	public final int key;
	public final KeyStroke press;

	private ButtonAction(int key) {
		this.key = key;
		press = KeyStroke.getKeyStroke(key, 0);
	}
}