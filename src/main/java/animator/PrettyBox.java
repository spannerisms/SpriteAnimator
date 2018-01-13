package animator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class PrettyBox<T> extends JComboBox<T> {
	private static final long serialVersionUID = 8555753499586233018L;

	private static final Border BORDER_OFF_OUT = BorderFactory.createBevelBorder(BevelBorder.RAISED);

	public PrettyBox(T[] set) {
		super(set);

		this.setBorder(BORDER_OFF_OUT);
	}
}