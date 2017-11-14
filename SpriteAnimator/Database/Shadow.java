package SpriteAnimator.Database;

import org.json.JSONArray;
import org.json.JSONObject;

public enum Shadow {
	NORM (0),
	SMALL (1),
	NONE (-1);

	public final SpriteData spr;

	public static final char NO_SHADOW = '\0';

	private Shadow(int col) {
		if (col == -1) {
			spr = null;
			return;
		}
		JSONObject sprData = new JSONObject();
		sprData.put("row", "SHADOW");
		sprData.put("col", col);
		sprData.put("size", "FULL");
		JSONArray pos = new JSONArray();
		pos.put(0);
		pos.put(7);
		sprData.put("pos", pos);
		spr = new SpriteData(sprData);
	}
}