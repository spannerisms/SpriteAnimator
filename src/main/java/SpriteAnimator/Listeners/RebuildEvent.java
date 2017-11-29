package SpriteAnimator.Listeners;

import java.util.EventObject;

import SpriteAnimator.SpriteAnimator;

public class RebuildEvent extends EventObject {
	private static final long serialVersionUID = 1344319042400882891L;

	public RebuildEvent(Object o) {
		super(o);
	}

	public RebuildEvent(SpriteAnimator o) {
		super(o);
	}
}