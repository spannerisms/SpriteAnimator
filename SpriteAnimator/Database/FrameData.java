package SpriteAnimator.Database;

import java.util.ArrayList;

import org.json.*;

public class FrameData {
	private final ArrayList<SpriteData> sprites = new ArrayList<SpriteData>();
	public final int l; // length
	public final char shadow;

	// used for each sword in a frame, replacing generic sword later
	private final SpriteData[] swords = new SpriteData[5];
	// used for each shield in a frame, replacing generic shield later
	private final SpriteData[] shields = new SpriteData[4];

	// makes a frame from sprite objects
	private FrameData(char shadow, int l, ArrayList<SpriteData> s) {
		this.shadow = shadow;
		this.l = l;
		for (SpriteData e : s) {
			sprites.add(e);
		}
	}

	private FrameData(JSONObject jo) {
		JSONArray sprites = (JSONArray) jo.get("sprites");
		for (Object o : sprites) {
			assert o instanceof JSONObject;
			JSONObject sprObj = (JSONObject) o;
			// place all swords and all shields into the same thing
			// take them out later
			FrameRow row = FrameRow.valueOf(sprObj.getString("row"));
			if (row == FrameRow.SWORD) {
				for (int i = 0; i < 5; i++) {
					swords[i] = new SpriteData(sprObj, i);
				}
			} else if (row == FrameRow.SHIELD) {
				for (int i = 0; i < 4; i++) {
					shields[i] = new SpriteData(sprObj, i);
				}
			}
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

	public int countSprites() {
		return sprites.size();
	}

	/**
	 * Creates a new {@code FrameData} object based on this one,
	 * but with the requested changes
	 * @param swordLevel
	 * @param shieldLevel
	 * @param shadow
	 * @param showEquipment
	 */
	public FrameData customizeFrame(int swordLevel, int shieldLevel,
			char shadow, boolean showEquipment) {
		ArrayList<SpriteData> s = new ArrayList<SpriteData>();
		for (SpriteData e : sprites) {
			if (e.row == FrameRow.SWORD) { // replace sword with correct sword
				if (swordLevel > 0) {
					s.add(swords[swordLevel]);
				} else {
					// do nothing; don't add
				}
			} else if (e.row == FrameRow.SHIELD) { // replace shield with correct shield
				if (shieldLevel > 0) {
					s.add(shields[shieldLevel]);
				} else {
					// do nothing; don't add
				}
			} else if (e.isEquipment) { // check for equipment
				if (showEquipment) {
					s.add(e);
				} else {
					// do nothing; don't add
				}
			} else { // add everything else
				s.add(e);
			}
		}

		return new FrameData(shadow, this.l, s);
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

	// compares all data points
	public boolean equals(Object o) {
		if (!(o instanceof FrameData)) {
			return false;
		}
		assert o instanceof FrameData;
		FrameData f = (FrameData) o;
		// compare number of sprites first
		boolean ret = true;
		
		return false;
	}
}