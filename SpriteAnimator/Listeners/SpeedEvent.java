package SpriteAnimator.Listeners;

import java.util.EventObject;
import SpriteAnimator.SpriteAnimator;

public class SpeedEvent extends EventObject {
	private static final long serialVersionUID = 299512753812026171L;

	public SpeedEvent(Object o) {
		super(o);
	}

	public SpeedEvent(SpriteAnimator o) {
		super(o);
	}
}