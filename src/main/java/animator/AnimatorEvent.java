package animator;

import java.util.EventObject;

public class AnimatorEvent extends EventObject {
	private static final long serialVersionUID = -5594768352326438346L;

	public AnimatorEvent(Object o) {
		super(o);
	}

	public AnimatorEvent(SpriteAnimator o) {
		super(o);
	}
}