package SpriteAnimator.Database;

import java.util.ArrayList;

import org.json.*;

public class StepData {
	private final ArrayList<SpriteData> sprites = new ArrayList<SpriteData>();
	public final int l; // length

	// used for each sword in a step, replacing generic sword later
	private final SpriteData[] swords = new SpriteData[5];
	// used for each shield in a step, replacing generic shield later
	private final SpriteData[] shields = new SpriteData[4];

	// makes a step from sprite objects
	private StepData(ArrayList<SpriteData> s, int length) {
		this.l = length;
		for (SpriteData e : s) {
			sprites.add(e);
		}
	}

	// make a step from json data
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

		// add shadow to sprite list
		Shadow shad = Shadow.valueOf(jo.getString("shadow"));

		switch (shad) {
			case NORM :
			case SMALL :
				this.sprites.add(shad.spr);
				break;
			case NONE :
				break;
		}

		l = jo.getInt("length");
	}

	public int countSprites() {
		return sprites.size();
	}

	public ArrayList<SpriteData> getSprites() {
		return sprites;
	}

	public SpriteData getSprite(int i) {
		return sprites.get(i);
	}

	/**
	 * Creates a new {@code StepData} object based on this one,
	 * but with the requested changes
	 */
	public StepData customizeStep(int swordLevel, int shieldLevel,
			boolean showShadow, boolean showEquipment) {
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
			} else if (e.row == SheetRow.SHADOW) { // check for shadow
				if (showShadow) {
					s.add(e);
				} else {
					// do nothing; don't add
				}
			} else { // add everything else
				s.add(e);
			}
		}

		return new StepData(s, this.l);
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
			return NeutralPose.valueOf("NEUTRAL_"+neutralPose).data;
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

		boolean ret = true;
		ArrayList<SpriteData> fSpr = f.getSprites();
		if (fSpr.size() != sprites.size()) { // compare number of sprites first
			return false;
		}

		// compare each sprite
		for (int i = 0; i < sprites.size(); i++) {
			if (!sprites.get(i).equals(fSpr.get(i))) {
				ret = false;
				break;
			}
		}

		return ret;
	}

	/**
	 * Merges any and all consecutive and identical frames
	 */
	public static ArrayList<StepData> mergeAll(ArrayList<StepData> list) {
		ArrayList<StepData> ret = new ArrayList<StepData>();
		int pos = 0; // current position, changes after merging to prevent double merge
		while (pos < list.size()) {
			StepData cur = list.get(pos);
			ArrayList<SpriteData> curPose = cur.getSprites();
			int curLength = cur.l;
			matchFind:
			for (int i = pos+1; i < list.size(); i++, pos++) {
				StepData next = list.get(i);
				if (next.equals(cur)) {
					curLength += next.l; // only increase length of animation
				} else {
					break matchFind;
				}
			}
			pos++;
			ret.add(new StepData(curPose, curLength));
		} // end merging
		return ret;
	}
}