package animator.listeners;

import java.util.EventObject;

import animator.SpriteAnimator;

public class ModeEvent extends EventObject {
	private static final long serialVersionUID = 738803889403236061L;

	public ModeEvent(Object o) {
		super(o);
	}

	public ModeEvent(SpriteAnimator o) {
		super(o);
	}
}