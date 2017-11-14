package SpriteAnimator.Database;

import java.util.ArrayList;

import org.json.*;

public class StepData {
	private final ArrayList<SpriteData> sprites = new ArrayList<SpriteData>();
	public final int l; // length
	public final char shadow;

	// used for each sword in a step, replacing generic sword later
	private final SpriteData[] swords = new SpriteData[5];
	// used for each shield in a step, replacing generic shield later
	private final SpriteData[] shields = new SpriteData[4];

	// makes a step from sprite objects
	private StepData(char shadow, int l, ArrayList<SpriteData> s) {
		this.shadow = shadow;
		this.l = l;
		for (SpriteData e : s) {
			sprites.add(e);
		}
	}

	private StepData(JSONObject jo) {
		JSONArray sprites = (JSONArray) jo.get("sprites");
		for (Object o : sprites) {
			assert o instanceof JSONObject;
			JSONObject sprObj = (JSONObject) o;
			// place all swords and all shields into the same thing
			// take them out later
			SheetRow row = SheetRow.valueOf(sprObj.getString("row"));
			if (row == SheetRow.SWORD) {
				for (int i = 0; i < 5; i++) {
					swords[i] = new SpriteData(sprObj, i);
				}
			} else if (row == SheetRow.SHIELD) {
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
	 * Creates a new {@code StepData} object based on this one,
	 * but with the requested changes
	 * @param swordLevel
	 * @param shieldLevel
	 * @param shadow
	 * @param showEquipment
	 */
	public StepData customizeStep(int swordLevel, int shieldLevel,
			char shadow, boolean showEquipment) {
		ArrayList<SpriteData> s = new ArrayList<SpriteData>();
		for (SpriteData e : sprites) {
			if (e.row == SheetRow.SWORD) { // replace sword with correct sword
				if (swordLevel > 0) {
					s.add(swords[swordLevel]);
				} else {
					// do nothing; don't add
				}
			} else if (e.row == SheetRow.SHIELD) { // replace shield with correct shield
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

		return new StepData(shadow, this.l, s);
	}

	// no other way to get neutral steps sorted properly
	public static StepData makeStep(JSONObject jo) {
		// catch neutral poses
		boolean isNeutral = true;
		String neutralPose = "";
		try { 
			neutralPose = jo.getString("neutralPose");
		} catch (JSONException e) { // catch an error for no value
			isNeutral = false; // that means we're not a neutral step
		}

		if (isNeutral) {
			return NeutralPose.valueOf(neutralPose).data;
		} else {
			return new StepData(jo);
		}
		
	}

	// compares all data points
	public boolean equals(Object o) {
		if (!(o instanceof StepData)) {
			return false;
		}
		assert o instanceof StepData;
		StepData f = (StepData) o;
		// compare number of sprites first
		boolean ret = true;
		
		return false;
	}
}