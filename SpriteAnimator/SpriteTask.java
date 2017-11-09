package SpriteAnimator;

import java.util.TimerTask;

class SpriteTask extends TimerTask {
	private SpriteAnimator r;
	public SpriteTask(SpriteAnimator run) {
		r = run;
	}

	public void run() {
		r.step();
	}
}