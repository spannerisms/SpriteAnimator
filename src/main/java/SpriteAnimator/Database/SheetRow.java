package SpriteAnimator.Database;

public enum SheetRow {
	A (0, true, false),
	B (1, true, false),
	C (2, true, false),
	D (3, true, false),
	E (4, true, false),
	F (5, true, false),
	G (6, true, false),
	H (7, true, false),
	I (8, true, false),
	J (9, true, false),
	K (10, true, false),
	L (11, true, false),
	M (12, true, false),
	N (13, true, false),
	O (14, true, false),
	P (15, true, false),
	Q (16, true, false),
	R (17, true, false),
	S (18, true, false),
	T (19, true, false),
	U (20, true, false),
	V (21, true, false),
	W (22, true, false),
	X (23, true, false),
	Y (24, true, false),
	Z (25, true, false),
	AA (26, true, false),
	ALPHA (26, true, false),
	AB (27, true, false),
	BETA (27, true, false),
	// equipment
	// swords
	SWORD (0, false, false), // use generic sword and swap later
	FSWORD (0, false, false),
	MSWORD (2, false, false),
	TSWORD (4, false, false),
	BSWORD (6, false, false),
	// shields
	SHIELD (8, false, false), // use generic shield and swap later
	FSHIELD (8, false, false),
	RSHIELD (9, false, false),
	MSHIELD (10, false, false),
	// items
	SHADOW (7, false, false),
	ITEMSHADOW (7, false, true),
	BOOK (7, false, true),
	PENDANT (7, false, true),
	CRYSTAL (7, false, true),
	BUSH (7, false, true),
	CANE (11, false, true),
	ROD (12, false, true),
	HAMMER (13, false, true),
	HOOKSHOT (14, false, true),
	BOOMERANG (14, false, true),
	NET (15, false, true),
	BOW (16, false, true),
	SHOVEL (17, false, true),
	DUCK (17, false, true),
	BED (18, false, true),
	GRASS (20, false, true);

	public final int val;
	public final boolean isLinkPart;
	public final boolean isEquipment;

	private SheetRow(int n, boolean isLink, boolean isEquipment) {
		val = n;
		isLinkPart = isLink;
		this.isEquipment = isEquipment;
	}
}