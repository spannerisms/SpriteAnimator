package SpriteAnimator;

import java.util.EventObject;

public class ModeEvent extends EventObject {
	private static final long serialVersionUID = 738803889403236061L;

	public ModeEvent(Object o) {
		super(o);
	}

	public ModeEvent(SpriteAnimator o) {
		super(o);
	}
}