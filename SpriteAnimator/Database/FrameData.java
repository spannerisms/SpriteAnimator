package SpriteAnimator.Database;

import java.util.ArrayList;

import org.json.*;

public class FrameData {
	private final ArrayList<SpriteData> sprites = new ArrayList<SpriteData>();
	public final int l; // length
	public final char shadow;
	private FrameData(JSONObject jo) {
		JSONArray sprites = (JSONArray) jo.get("sprites");
		for (Object o : sprites) {
			assert o instanceof JSONObject;
			JSONObject sprObj = (JSONObject) o;
			this.sprites.add(new SpriteData(sprObj));
		}

		l = jo.getInt("length");
		String shadow = jo.getString("shadow");
		switch (shadow) {
			case "TRUE" :
				this.shadow = 'b';
				break;
			default :
			case "FALSE" :
				this.shadow = '0';
				break;
			case "SMALL" :
				this.shadow = 's';
				break;
		}
	}

	// no other way to get neutral frames sorted properly
	public static FrameData makeFrame(JSONObject jo) {
		// catch neutral frames
		boolean isNeutral = true;
		String neutralFrame = "";
		try { 
			neutralFrame = jo.getString("neutralFrame");
		} catch (JSONException e) { // catch an error for no value
			isNeutral = false; // that means we're not a neutral frame
		}

		if (isNeutral) {
			return NeutralFrames.valueOf(neutralFrame).data;
		} else {
			return new FrameData(jo);
		}
		
	}
}
