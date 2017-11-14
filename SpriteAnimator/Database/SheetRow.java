package SpriteAnimator.Database;

public enum SheetRow {
	A (0, true),
	B (1, true),
	C (2, true),
	D (3, true),
	E (4, true),
	F (5, true),
	G (6, true),
	H (7, true),
	I (8, true),
	J (9, true),
	K (10, true),
	L (11, true),
	M (12, true),
	N (13, true),
	O (14, true),
	P (15, true),
	Q (16, true),
	R (17, true),
	S (18, true),
	T (19, true),
	U (20, true),
	V (21, true),
	W (22, true),
	X (23, true),
	Y (24, true),
	Z (25, true),
	AA (26, true),
	ALPHA (26, true),
	AB (27, true),
	BETA (27, true),
	// equipment
	// swords
	SWORD (0), // use generic sword and swap later
	FSWORD (0),
	MSWORD (2),
	TSWORD (4),
	BSWORD (6),
	// shields
	SHIELD (8), // use any shield and swap later
	FSHIELD (8),
	RSHIELD (9),
	MSHIELD (10),
	// items
	SHADOW (7),
	ITEMSHADOW (7),
	BOOK (7),
	PENDANT (7),
	CRYSTAL (7),
	BUSH (7),
	CANE (11),
	ROD (12),
	HAMMER (13),
	HOOKSHOT (14),
	BOOMERANG (14),
	NET (15),
	BOW (16),
	SHOVEL (17),
	DUCK (17),
	BED (18),
	GRASS (20);

	public final int val;
	public final boolean isLinkPart;

	private SheetRow(int n) {
		this(n, false);
	}

	private SheetRow(int n, boolean isLink) {
		val = n;
		isLinkPart = isLink;
	}
}