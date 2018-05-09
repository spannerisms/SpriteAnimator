package animator.gui.cellsearch;

import animator.database.Animation;
import animator.database.DrawSize;
import animator.database.SheetRow;
import animator.database.SpriteData;
import animator.database.StepData;

import static animator.database.DrawSize.*;
import static animator.database.SheetRow.*;

import java.util.ArrayList;

public enum SpriteCell {
	// A
	A0 (A, 0, FULL),
	A1 (A, 1, FULL),
	A2 (A, 2, FULL),
	A3 (A, 3, FULL),
	A4 (A, 4, FULL),
	A5 (A, 5, FULL),
	A6 (A, 6, FULL),
	A7 (A, 7, FULL),
	// B
	B0 (B, 0, FULL),
	B1 (B, 1, FULL),
	B2 (B, 2, FULL),
	B3 (B, 3, FULL),
	B4 (B, 4, FULL),
	B5 (B, 5, FULL),
	B6 (B, 6, FULL),
	B7 (B, 7, FULL),
	// C
	C0 (C, 0, FULL),
	C1 (C, 1, FULL),
	C2 (C, 2, FULL),
	C3 (C, 3, FULL),
	C4 (C, 4, FULL),
	C5 (C, 5, FULL),
	C6 (C, 6, FULL),
	C7 (C, 7, FULL),
	// D
	D0 (D, 0, FULL),
	D1 (D, 1, FULL),
	D2 (D, 2, FULL),
	D3 (D, 3, FULL),
	D4 (D, 4, FULL),
	D5 (D, 5, FULL, false),
	D6 (D, 6, FULL, false),
	D7 (D, 7, FULL),
	// E
	E0 (E, 0, FULL, false),
	E1 (E, 1, FULL, false),
	E2 (E, 2, FULL),
	E3 (E, 3, FULL),
	E4 (E, 4, FULL),
	E5 (E, 5, FULL),
	E6 (E, 6, FULL),
	E7 (E, 7, FULL),
	// F
	F0 (F, 0, FULL),
	F1 (F, 1, FULL),
	F2 (F, 2, FULL),
	F3 (F, 3, FULL),
	F4 (F, 4, FULL),
	F5 (F, 5, FULL),
	F6 (F, 6, FULL),
	F7 (F, 7, FULL),
	// G
	G0_T (G, 0, TOP_HALF),
	G0_B (G, 0, BOTTOM_HALF),
	G1_T (G, 1, TOP_HALF),
	G1_BL (G, 1, BOTTOM_LEFT),
	G1_BR (G, 1, BOTTOM_RIGHT),
	G2_T (G, 2, TOP_HALF),
	G2_BL (G, 2, BOTTOM_LEFT),
	G2_BR (G, 2, BOTTOM_RIGHT),
	G3_T (G, 3, TOP_HALF),
	G3_BL (G, 3, BOTTOM_LEFT),
	G3_BR (G, 3, BOTTOM_RIGHT),
	G4_T (G, 4, TOP_HALF),
	G4_BL (G, 4, BOTTOM_LEFT),
	G4_BR (G, 4, BOTTOM_RIGHT),
	G5 (G, 5, FULL, false),
	G6 (G, 6, FULL, false),
	G7 (G, 7, FULL),
	// H
	H0 (H, 0, FULL),
	H1_L (H, 1, LEFT_HALF),
	H1_R (H, 1, RIGHT_HALF),
	H2_L (H, 2, LEFT_HALF),
	H2_R (H, 2, RIGHT_HALF),
	H3_L (H, 3, LEFT_HALF),
	H3_R (H, 3, RIGHT_HALF),
	H4_L (H, 4, LEFT_HALF),
	H4_TR (H, 4, TOP_RIGHT),
	H4_BR (H, 4, BOTTOM_RIGHT),
	H5 (H, 5, FULL),
	H6 (H, 6, FULL),
	H7 (H, 7, FULL),
	// I
	I0 (I, 0, FULL),
	I1 (I, 1, FULL),
	I2 (I, 2, FULL),
	I3 (I, 3, FULL),
	I4 (I, 4, FULL),
	I5 (I, 5, FULL),
	I6 (I, 6, FULL),
	I7 (I, 7, FULL),
	// J
	J0 (J, 0, FULL),
	J1 (J, 1, FULL),
	J2 (J, 2, FULL),
	J3 (J, 3, FULL),
	J4 (J, 4, FULL),
	J5_TL (J, 5, TOP_LEFT),
	J5_BL (J, 5, BOTTOM_LEFT),
	J5_R (J, 5, RIGHT_HALF),
	J6_L (J, 6, LEFT_HALF),
	J6_R (J, 6, RIGHT_HALF),
	J7 (J, 7, FULL),
	// K
	K0 (K, 0, FULL),
	K1 (K, 1, FULL),
	K2 (K, 2, FULL),
	K3 (K, 3, FULL),
	K4 (K, 4, FULL),
	K5 (K, 5, FULL, false),
	K6 (K, 6, FULL),
	K7 (K, 7, FULL),
	// L
	L0 (L, 0, FULL),
	L1 (L, 1, FULL),
	L2 (L, 2, FULL),
	L3 (L, 3, FULL),
	L4 (L, 4, FULL),
	L5 (L, 5, FULL),
	L6 (L, 6, FULL),
	L7 (L, 7, FULL),
	// M
	M0 (M, 0, FULL),
	M1 (M, 1, FULL),
	M2 (M, 2, FULL),
	M3 (M, 3, FULL),
	M4 (M, 4, FULL),
	M5 (M, 5, FULL),
	M6 (M, 6, FULL),
	M7 (M, 7, FULL),
	// N
	N0 (N, 0, FULL),
	N1 (N, 1, FULL),
	N2 (N, 2, FULL),
	N3 (N, 3, FULL),
	N4 (N, 4, FULL),
	N5 (N, 5, FULL),
	N6 (N, 6, FULL),
	N7 (N, 7, FULL),
	// O
	O0 (O, 0, FULL),
	O1 (O, 1, FULL),
	O2 (O, 2, FULL),
	O3 (O, 3, FULL),
	O4 (O, 4, FULL),
	O5 (O, 5, FULL),
	O6 (O, 6, FULL),
	O7 (O, 7, FULL),
	// P
	P0_TL (P, 0, TOP_LEFT),
	P0_BL (P, 0, BOTTOM_LEFT),
	P0_R (P, 0, RIGHT_HALF, false),
	P1 (P, 1, FULL),
	P2 (P, 2, FULL),
	P3 (P, 3, FULL),
	P4 (P, 4, FULL),
	P5 (P, 5, FULL),
	P6 (P, 6, FULL),
	P7 (P, 7, FULL),
	// Q
	Q0 (Q, 0, FULL),
	Q1 (Q, 1, FULL),
	Q2 (Q, 2, FULL, false),
	Q3 (Q, 3, FULL, false),
	Q4 (Q, 4, FULL, false),
	Q5 (Q, 5, FULL),
	Q6 (Q, 6, FULL),
	Q7 (Q, 7, FULL),
	// R
	R0 (R, 0, FULL),
	R1 (R, 1, FULL),
	R2 (R, 2, FULL),
	R3 (R, 3, FULL, false),
	R4 (R, 4, FULL, false),
	R5 (R, 5, FULL, false),
	R6 (R, 6, FULL),
	R7 (R, 7, FULL),
	// S
	S0_T (S, 0, TOP_HALF),
	S0_B (S, 0, BOTTOM_HALF),
	S1_T (S, 1, TOP_HALF),
	S1_B (S, 1, BOTTOM_HALF),
	S2_T (S, 2, TOP_HALF),
	S2_B (S, 2, BOTTOM_HALF),
	S3 (S, 3, FULL),
	S4 (S, 4, FULL),
	S5 (S, 5, FULL),
	S6 (S, 6, FULL),
	S7 (S, 7, FULL),
	// T
	T0 (T, 0, FULL),
	T1 (T, 1, FULL),
	T2 (T, 2, FULL),
	T3 (T, 3, FULL),
	T4 (T, 4, FULL),
	T5 (T, 5, FULL),
	T6 (T, 6, FULL),
	T7 (T, 7, FULL),
	// U
	U0 (U, 0, FULL),
	U1 (U, 1, FULL),
	U2 (U, 2, FULL),
	U3 (U, 3, FULL, false),
	U4 (U, 4, FULL),
	U5 (U, 5, FULL),
	U6 (U, 6, FULL),
	U7 (U, 7, FULL),
	// V
	V0 (V, 0, FULL),
	V1 (V, 1, FULL),
	V2 (V, 2, FULL),
	V3 (V, 3, FULL),
	V4 (V, 4, FULL),
	V5 (V, 5, FULL),
	V6 (V, 6, FULL),
	V7 (V, 7, FULL, false),
	// W
	W0 (W, 0, FULL, false),
	W1 (W, 1, FULL, false),
	W2 (W, 2, FULL, false),
	W3 (W, 3, FULL, false),
	W4 (W, 4, FULL, false),
	W5 (W, 5, FULL, false),
	W6 (W, 6, FULL, false),
	W7 (W, 7, FULL, false),
	// X
	X0 (X, 0, FULL, false),
	X1 (X, 1, FULL),
	X2 (X, 2, FULL),
	X3 (X, 3, FULL),
	X4 (X, 4, FULL),
	X5 (X, 5, FULL),
	X6 (X, 6, FULL),
	X7 (X, 7, FULL),
	// Y
	Y0 (Y, 0, FULL),
	Y1 (Y, 1, FULL),
	Y2 (Y, 2, FULL),
	Y3 (Y, 3, FULL),
	Y4 (Y, 4, FULL),
	Y5 (Y, 5, FULL),
	Y6 (Y, 6, FULL),
	Y7 (Y, 7, FULL),
	// Z
	Z0 (Z, 0, FULL),
	Z1 (Z, 1, FULL, false),
	Z2 (Z, 2, FULL),
	Z3 (Z, 3, FULL),
	Z4 (Z, 4, FULL),
	Z5 (Z, 5, FULL),
	Z6_T (Z, 6, TOP_HALF),
	Z6_BL (Z, 6, BOTTOM_LEFT),
	Z6_BR (Z, 6, BOTTOM_RIGHT, false),
	Z7 (Z, 7, FULL),
	// AA
	AA0 (AA, 0, FULL),
	AA1 (AA, 1, FULL),
	AA2 (AA, 2, FULL),
	AA3 (AA, 3, FULL),
	AA4 (AA, 4, FULL),
	AA5 (AA, 5, FULL),
	AA6 (AA, 6, FULL),
	AA7 (AA, 7, FULL),
	// AB
	AB0 (AB, 0, FULL),
	AB1 (AB, 1, FULL),
	AB2 (AB, 2, FULL),
	AB3 (AB, 3, FULL),
	AB4 (AB, 4, FULL),
	AB5 (AB, 5, FULL, false),
	AB6 (AB, 6, FULL, false),
	AB7 (AB, 7, FULL);

	public final String name;
	public final SheetRow row;
	public final int r;
	public final int c;
	public final DrawSize d;
	public final boolean isUsed;
	public final ArrayList<StepList> usedBy;
	public final ArrayList<Animation> usedIn;
	public final String fullName;
	public final String HTMLret;

	private SpriteCell(SheetRow s, int column, DrawSize drawSize, boolean used) {
		row = s;
		r = s.val;
		c = column;
		d = drawSize;
		name = s.name() + c;
		fullName = String.format("%s%s%s",
				name,
				d != DrawSize.FULL ? ":" : "",
				d.token);

		isUsed = used;
		usedBy = new ArrayList<StepList>();
		usedIn = new ArrayList<Animation>();
		findAnimationFrames();

		if (used) {
			String[] list = new String[usedBy.size()];
			for (int i = 0; i < usedBy.size(); i++) {
				StepList use = usedBy.get(i);
				int[] l = use.list;
				String[] listAgain = new String[l.length];
				int j = 0;
				for (int k : l) {
					listAgain[j++] = Integer.toString(k);
				}
				String allNums = String.join("; ", listAgain);
				list[i] = String.join("",
						new String[] {
								"<div style=\"border:2px outset;",
								"padding-left:3px;",
								"font-weight:bold;",
								"background: #D8D8D8;\">",
								use.animName,
								"</div>",
								"<div style=\"margin-left:10px;",
								"font-family:consolas;",
								"margin-bottom: 5px;",
								"width: 200px;\">",
								allNums,
								"</div>"
							}
						);
			}
			HTMLret = String.join("", list);
		} else {
			HTMLret = String.join("",
					new String[] {
					"<div style=\"border:2px outset;",
					"padding-left:3px;",
					"font-weight:bold;",
					"background: #D8D8D8;\">",
					"Unused",
					"</div>"
				}
			);
		}
	}

	private SpriteCell(SheetRow s, int column, DrawSize drawSize) {
		this(s, column, drawSize, true);
	}

	private final void findAnimationFrames() {
		if (!this.isUsed) { return; }

		ArrayList<Integer> counter = new ArrayList<Integer>();
		String animName;
		int stepCount;
		boolean usedHere = false;
		for (Animation a : Animation.values()) {
			usedHere = false;
			counter.clear();
			stepCount = 1;
			animName = a.toString();

			for (StepData f : a.customizeMergeAndFinalize(1, 1, true, true, false)) {
				spriteSearch :
				for (SpriteData s : f.getSprites()) {
					if (s.equalsIndex(this)) {
						counter.add(stepCount);
						if (!usedHere) {
							usedHere = true;
							usedIn.add(a);
						}
						break spriteSearch;
					}
				} // end sprite loop
				stepCount++;
			} // end step loop

			if (!counter.isEmpty()) {
				usedBy.add(new StepList(animName, counter));
			}
		} // end animation loop
	}

	public boolean isUsedInAnimation(Animation e) {
		for (Animation a : usedIn) {
			if (a == e) {
				return true;
			}
		}
		return false;
	}

	public String toString() {
		return fullName;
	}

	public String toHTML() {
		return HTMLret;
	}
}