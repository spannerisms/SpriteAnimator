package SpriteAnimator;

import java.util.EventObject;

public class EquipEvent extends EventObject {
	private static final long serialVersionUID = 1344319042400882891L;

	public EquipEvent(Object o) {
		super(o);
	}

	public EquipEvent(SpriteAnimator o) {
		super(o);
	}
}
