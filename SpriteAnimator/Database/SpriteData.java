package SpriteAnimator.Database;

import org.json.*;

public class SpriteData {
	// class constants
	// Fighter, Master, Tempered, Butter
	private static final String[] SWORD_PREFIX = { "", "F", "M", "T", "B" };
	// Fighter, Red, Mirror
	private static final String[] SHIELD_PREFIX = { "", "F", "R", "M" };

	// local vars
	public final FrameRow row;
	public final int col;
	public final int x;
	public final int y;
	public final DrawSize d;
	public final boolean isEquipment;
	public SpriteData(JSONObject jo) {
		this(jo, 0);
	}

	// for swords and shields
	public SpriteData(JSONObject jo, int level) {
		String rowName = jo.getString("row");
		FrameRow passedRow = FrameRow.valueOf(rowName);

		if (passedRow == FrameRow.SWORD) {
			row = FrameRow.valueOf(SWORD_PREFIX[level] + rowName);
		} else if (passedRow == FrameRow.SHIELD) {
			row = FrameRow.valueOf(SHIELD_PREFIX[level] + rowName);
		} else {
			row = passedRow;
		}

		col = jo.getInt("col");
		JSONArray pos = jo.getJSONArray("pos");
		x = pos.getInt(0);
		y = pos.getInt(1);
		d = DrawSize.valueOf(jo.getString("size"));
		
		boolean equipment;
		try {
			equipment = jo.getBoolean("isEquipment");
		} catch (JSONException e) {
			equipment = false;
		}
		isEquipment = equipment;
	}

	// compares all data points
	public boolean equals(Object o) {
		if (!(o instanceof SpriteData)) {
			return false;
		}
		assert o instanceof SpriteData;
		SpriteData s = (SpriteData) o;
		return (this.row == s.row) &&
				(this.col == s.col) &&
				(this.x == s.x) &&
				(this.y == s.y) &&
				(this.d == s.d);
	}
}