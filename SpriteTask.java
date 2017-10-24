package SpriteAnimator.Sprite;

import java.util.TimerTask;

import SpriteAnimator.SpriteAnimator;

public class SpriteTask extends TimerTask {
	private SpriteAnimator r;
	public SpriteTask(SpriteAnimator run) {
		r = run;
	}

	public void run() {
		r.step();
	}
}