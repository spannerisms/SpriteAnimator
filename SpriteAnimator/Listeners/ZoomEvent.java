package SpriteAnimator.Listeners;

import java.util.EventObject;
import SpriteAnimator.SpriteAnimator;

public class ZoomEvent extends EventObject {
	private static final long serialVersionUID = -7292653834209301725L;

	public ZoomEvent(Object o) {
		super(o);
	}

	public ZoomEvent(SpriteAnimator o) {
		super(o);
	}
}