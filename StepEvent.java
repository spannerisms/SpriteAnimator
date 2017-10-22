package SpriteAnimator;

import java.util.EventObject;

public class StepEvent extends EventObject {
	private static final long serialVersionUID = -5576103844258368996L;

	public StepEvent(Object o) {
		super(o);
	}

	public StepEvent(SpriteAnimator o) {
		super(o);
	}
}