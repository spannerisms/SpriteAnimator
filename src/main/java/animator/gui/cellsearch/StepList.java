package animator.gui.cellsearch;

import java.util.ArrayList;

public class StepList {
	public final String animName;
	public final int[] list;

	public StepList(String animName, ArrayList<Integer> list) {
		this.animName = animName;
		this.list = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			this.list[i] = list.get(i);
		}
	}
}