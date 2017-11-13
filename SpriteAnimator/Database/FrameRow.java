package SpriteAnimator.Database;

public enum FrameRow {
	A (0),
	B (1),
	C (2),
	D (3),
	E (4),
	F (5),
	G (6),
	H (7),
	I (8),
	J (9),
	K (10),
	L (11),
	M (12),
	N (13),
	O (14),
	P (15),
	Q (16),
	R (17),
	S (18),
	T (19),
	U (20),
	V (21),
	W (22),
	X (23),
	Y (24),
	Z (25),
	AA (26),
	ALPHA (26),
	AB (27),
	BETA (27),
	// equipment
	// swords
	FSWORD (0),
	MSWORD (2),
	TSWORD (4),
	BSWORD (6),
	// shields
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
	BOOMERANGE (14),
	NET (15),
	BOW (16),
	SHOVEL (17),
	DUCK (17),
	BED (18),
	GRASS (20)
	;

	public final int val;
	private FrameRow(int n) {
		val = n;
	}
}