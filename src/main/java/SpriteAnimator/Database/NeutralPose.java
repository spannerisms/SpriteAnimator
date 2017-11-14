package SpriteAnimator.Database;

import org.json.JSONObject;

public enum NeutralPose {
	NEUTRAL_RIGHT ("neutralRight"),
	NEUTRAL_LEFT ("neutralLeft"),
	NEUTRAL_UP ("neutralUp"),
	NEUTRAL_DOWN ("neutralDown");

	public final StepData data;

	private NeutralPose(String s) {
		JSONObject d = DatabaseJSON.ALL_DATA.getJSONObject(s);
		data = StepData.makeStep(d.getJSONArray("steps").getJSONObject(0));
	}
}