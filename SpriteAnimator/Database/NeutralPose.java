package SpriteAnimator.Database;

import org.json.JSONObject;

public enum NeutralFrames {
	NEUTRAL_RIGHT ("neutralRight"),
	NEUTRAL_LEFT ("neutralLeft"),
	NEUTRAL_UP ("neutralUp"),
	NEUTRAL_DOWN ("neutralDown");

	public final FrameData data;
	private NeutralFrames(String s) {
		JSONObject d = DatabaseJSON.ALL_DATA.getJSONObject(s);
		data = FrameData.makeFrame(d);
	}
}
