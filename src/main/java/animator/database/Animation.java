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
	SWIM ("swim"),
	SWIM_UP ("swimUp"),
	SWIM_DOWN ("swimDown"),
	TREADING_WATER ("treadingWater"),
	TREADING_WATER_UP ("treadingWaterUp"),
	TREADING_WATER_DOWN ("treadingWaterDown"),
	ATTACK ("attack"),
	ATTACK_UP ("attackUp"),
	ATTACK_DOWN ("attackDown"),
	SPIN_ATTACK ("spinAttack"),
	SPIN_ATTACK_LEFT ("spinAttackLeft"),
	SPIN_ATTACK_UP ("spinAttackUp"),
	SPIN_ATTACK_DOWN ("spinAttackDown"),
	DASH_SPINUP ("dashSpinup"),
	DASH_SPINUP_UP ("dashSpinupUp"),
	DASH_SPINUP_DOWN ("dashSpinupDown"),
	DASH_RELEASE ("dashRelease"),
	DASH_RELEASE_UP ("dashReleaseUp"),
	DASH_RELEASE_DOWN ("dashReleaseDown"),
	BONK ("bonk"),
	BONK_UP ("bonkUp"),
	BONK_DOWN ("bonkDown"),
	FALL ("fall"),
	ZAP ("zap"),
	DEATH_SPIN ("deathSpin"),
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
	SALUTE ("salute"),
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
	WALK_UPSTAIRS1_F ("walkUpstairs1F"),
	WALK_UPSTAIRS2_F ("walkUpstairs2F"),
	WALK_DOWNSTAIRS2_F ("walkDownstairs2F"),
	WALK_DOWNSTAIRS1_F ("walkDownstairs1F"),
	POKE ("poke"),
	POKE_UP ("pokeUp"),
	POKE_DOWN ("pokeDown"),
	TALL_GRASS ("tallGrass"),
	TALL_GRASS_UP ("tallGrassUp"),
	TALL_GRASS_DOWN ("tallGrassDown"),
	MAP_DUNGEON ("mapDungeon"),
	MAP_WORLD ("mapWorld"),
	SLEEP ("sleep"),
	AWAKE ("awake");

	// local vars
	private final ArrayList<StepData> steps;
	private final String name;
	private final String vague;
	private final String disambig;
	private final Category ctg;

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

		this.ctg = Category.valueOf(data.getString("category"));
		ctg.addToList(this);
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

	public static enum Category {
		ALL ("All"),
		NEUTRAL ("Neutral poses"),
		MOVEMENT ("Movement animations"),
		SWIMMING ("Swimming animations"),
		COMBAT ("Combat animations"),
		INTERACTIONS ("Basic interactions"),
		ITEMS ("Item-use animations"),
		BUNNY ("Bunny sprite");

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