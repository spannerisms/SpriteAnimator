package animator.database;

import java.util.ArrayList;

import org.json.*;

public enum Animation {
	STAND ("stand"),
	STAND_UP ("standUp"),
	STAND_DOWN ("standDown"),
	WALK ("walk"),
	WALK_UP ("walkUp"),
	WALK_DOWN ("walkDown"),
	TALL_GRASS ("tallGrass"),
	TALL_GRASS_UP ("tallGrassUp"),
	TALL_GRASS_DOWN ("tallGrassDown"),
	WALK_UPSTAIRS_1F ("walkUpstairs1F"),
	WALK_UPSTAIRS_2F ("walkUpstairs2F"),
	WALK_DOWNSTAIRS_2F ("walkDownstairs2F"),
	WALK_DOWNSTAIRS_1F ("walkDownstairs1F"),
	SWIM ("swim"),
	SWIM_UP ("swimUp"),
	SWIM_DOWN ("swimDown"),
	TREAD_WATER ("treadWater"),
	TREAD_WATER_UP ("treadWaterUp"),
	TREAD_WATER_DOWN ("treadWaterDown"),
	ATTACK ("attack"),
	ATTACK_UP ("attackUp"),
	ATTACK_DOWN ("attackDown"),
	SWORD_PRIMED ("swordPrimed"),
	SWORD_PRIMED_UP ("swordPrimedUp"),
	SWORD_PRIMED_DOWN ("swordPrimedDown"),
	SPIN_ATTACK ("spinAttack"),
	SPIN_ATTACK_LEFT ("spinAttackLeft"),
	SPIN_ATTACK_UP ("spinAttackUp"),
	SPIN_ATTACK_DOWN ("spinAttackDown"),
	POKE ("poke"),
	POKE_UP ("pokeUp"),
	POKE_DOWN ("pokeDown"),
	DASH_CHARGE ("dashCharge"),
	DASH_CHARGE_UP ("dashChargeUp"),
	DASH_CHARGE_DOWN ("dashChargeDown"),
	DASH ("dash"),
	DASH_UP ("dashUp"),
	DASH_DOWN ("dashDown"),
	BONK ("bonk"),
	BONK_UP ("bonkUp"),
	BONK_DOWN ("bonkDown"),
	FALL ("fall"),
	ZAP ("zap"),
	DEATH ("death"),
	ASLEEP ("asleep"),
	AWAKE ("awake"),
	GRAB ("grab"),
	GRAB_UP ("grabUp"),
	GRAB_DOWN ("grabDown"),
	LIFT ("lift"),
	LIFT_UP ("liftUp"),
	LIFT_DOWN ("liftDown"),
	CARRY ("carry"),
	CARRY_UP ("carryUp"),
	CARRY_DOWN ("carryDown"),
	THROW ("throw"),
	THROW_UP ("throwUp"),
	THROW_DOWN ("throwDown"),
	PUSH ("push"),
	PUSH_UP ("pushUp"),
	PUSH_DOWN ("pushDown"),
	TREE_PULL ("treePull"),
	ITEM_GET ("itemGet"),
	CRYSTAL_GET ("crystalGet"),
	SALUTE_BOSS ("saluteBoss"),
	SALUTE_SAVE ("saluteSave"),
	MAP_DUNGEON ("mapDungeon"),
	MAP_WORLD ("mapWorld"),
	BOW ("bow"),
	BOW_UP ("bowUp"),
	BOW_DOWN ("bowDown"),
	BOOMERANG ("boomerang"),
	BOOMERANG_UP ("boomerangUp"),
	BOOMERANG_DOWN ("boomerangDown"),
	HOOKSHOT ("hookshot"),
	HOOKSHOT_UP ("hookshotUp"),
	HOOKSHOT_DOWN ("hookshotDown"),
	POWDER ("powder"),
	POWDER_UP ("powderUp"),
	POWDER_DOWN ("powderDown"),
	ROD ("rod"),
	ROD_UP ("rodUp"),
	ROD_DOWN ("rodDown"),
	BOMBOS ("bombos"),
	ETHER ("ether"),
	QUAKE ("quake"),
	HAMMER ("hammer"),
	HAMMER_UP ("hammerUp"),
	HAMMER_DOWN ("hammerDown"),
	SHOVEL ("shovel"),
	SWAG_DUCK ("swagDuck"),
	BUG_NET ("bugNet"),
	READ_BOOK ("readBook"),
	PRAYER ("prayer"),
	CANE ("cane"),
	CANE_UP ("caneUp"),
	CANE_DOWN ("caneDown"),
	BUNNY_STAND ("bunnyStand"),
	BUNNY_STAND_UP ("bunnyStandUp"),
	BUNNY_STAND_DOWN ("bunnyStandDown"),
	BUNNY_WALK ("bunnyWalk"),
	BUNNY_WALK_UP ("bunnyWalkUp"),
	BUNNY_WALK_DOWN ("bunnyWalkDown"),
	;

	// for easy access
	private static final Animation[] VALUES = Animation.values();
	private static final int COUNT = VALUES.length;

	// local vars
	private final ArrayList<StepData> steps;
	private final String name;
	private final String vague;
	private final String disambig;
	private final Category[] ctg;

	private Animation(String n) {
		steps = new ArrayList<StepData>();
		JSONObject data = DatabaseJSON.ALL_DATA.getJSONObject(n);

		JSONArray steps = data.getJSONArray("steps");
		for (Object o : steps) {
			assert o instanceof JSONObject;
			JSONObject fObj = (JSONObject) o;
			this.steps.add(StepData.makeStep(fObj));
		}
		this.name = data.getString("name");
		this.vague = data.getString("vague");

		if (this.name.indexOf('(') > -1) {
			this.disambig = this.name.replace(this.vague, "").trim();
		} else {
			this.disambig = this.name;
		}

		JSONArray ctgs = data.getJSONArray("category");
		int ctgCount = ctgs.length();
		this.ctg = new Category[ctgCount];
		for (int i = 0; i < ctgCount; i++) {
			String c = ctgs.getString(i);
			ctg[i] = Category.valueOf(c);
			ctg[i].addToList(this);
		}
		Category.ALL.addToList(this);
	}

	public String toString() {
		return name;
	}

	public String getDisambig() {
		return disambig;
	}

	public StepData[] getSteps() {
		return steps.toArray(new StepData[steps.size()]);
	}

	public ArrayList<StepData> customizeMergeAndFinalize(int swordLevel, int shieldLevel,
			boolean showShadow, boolean showEquipment, boolean showNeutral) {
		ArrayList<StepData> customized = new ArrayList<StepData>();
		for (StepData s : steps) {
			if (NeutralPose.isNeutral(s)) {
				if (!showNeutral) {
					continue;
				}
			}
			customized.add(s.customizeStep(swordLevel, shieldLevel, showShadow, showEquipment));
		}
		return StepData.mergeAll(customized);
	}

	public boolean compareVague(Animation e) {
		if (e == null) {
			return false;
		}
		return this.vague.equalsIgnoreCase(e.vague);
	}

	/**
	 * Returns the animation at relative index.
	 * @param d - offset
	 * @return An {@code Animation} or {@code null} if requested index is out of bounds
	 */
	public Animation getAnimationInDirection(int d) {
		Animation ret = null;

		int i = this.ordinal();
		i += d;

		if (i < COUNT && i >= 0) {
			ret = VALUES[i];
		} else { // allow wrapping
			if (d == 1) { // overflow to start
				ret = VALUES[0];
			} else if (d == -1) { // underflow to end
				ret = VALUES[COUNT-1];
			}
		}

		return ret;
	}

	public static enum Category {
		ALL ("All"),
		NEUTRAL ("Neutral poses"),
		MOVEMENT ("Movement animations"),
		SWIMMING ("Swimming animations"),
		COMBAT ("Combat animations"),
		INTERACTIONS ("Basic interactions"),
		ITEMS ("Item-use animations"),
		BUNNY ("Bunny sprite"),
		SWORD ("Uses sword"),
		SHIELD ("Uses shield"),
		;

		public final String name;
		private final ArrayList<Animation> list;

		private Category(String name) {
			this.name = name;
			list = new ArrayList<Animation>();
		}

		private void addToList(Animation a) {
			list.add(a);
		}

		public Animation[] getList() {
			return list.toArray(new Animation[list.size()]);
		}
	}
}