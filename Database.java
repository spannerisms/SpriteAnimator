package SpriteAnimator;

public abstract class Database {
	/* taken and modified from
	 * http://alttp.mymm1.com/sprites/includes/animations.txt
	 * credit:
	 * Mike Trethewey
	 * TWRoxas
	 * RyuTech
	 * 
	 * Format:
	 * [<ANIMNAME>]<INDEX>{<XPOS>,<YPOS>}{<SPRITESIZE>}{<TRANSFORM>}
	 * : delimits sprites in the same frame
	 * ; delimits entire frames
	 * @ at the end of each frame denotes how long it lasts
	 * SPRITESIZE is a flag determining what part of the sprite to draw from
	 *		F   : Full 16x16
	 *		T   : Top 16x8
	 *		B   : Bottom 16x8
	 *		R   : Right 8x16
	 *		L   : Left 8x16
	 *		TR  : Top-right 8x8 ; alias : turtle rock
	 *		TL  : Top-left 8x8
	 *		BR  : Bottom-right 8x8
	 *		BL  : Bottom-left 8x8
	 *		XT  : 8x24
	 *		XW  : 24x8
	 *		XL  : 16x24
	 *		XXL : 32x24
	 *		E   : Empty frame 0x0
	 * TRANSFORM is a flag determining how to flip the sprite
	 *		0   : No transform
	 *		U   : Mirror along X-axis
	 *		M   : Mirror along y-axis
	 *		UM  : Mirror along both axes
	 */
	public static final String[] ALLFRAMES = {
			// stand
			"[stand]A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 4",
			// standUp
			"[standUp]A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 4",
			// standDown
			"[standDown]SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 4",
			// walk
			"[walk]A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{5,2}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{-2,-2}{F}:B1{-1,7}{F}:SHIELD1{5,0}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"K3{-1,-2}{F}:B2{-1,7}{F}:SHIELD1{5,0}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"K4{-2,-1}{F}:Q7{-1,7}{F}:SHIELD1{5,1}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{-2,-1}{F}:S4{-1,7}{F}:SHIELD1{5,2}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{-2,-2}{F}:R6{-1,7}{F}:SHIELD1{5,1}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"K3{-1,-2}{F}:R7{-1,7}{F}:SHIELD1{5,0}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"K4{-1,-1}{F}:S3{-1,7}{F}:SHIELD1{5,1}{F}:SHADOW0{0,7}{F} @ 1 ",
			// walkUp
			"[walkUp]A2{0,0}{F}:B6{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,-1}{F}:C0{0,8}{F}:SHIELD2{5,2}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-2}{F}:S7{0,8}{F}:SHIELD2{5,1}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,0}{F}:T3{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,0}{F}:T7{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,-1}{F}:T4{0,8}{F}:SHIELD2{5,2}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-2}{F}:T5{0,8}{F}:SHIELD2{5,1}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,0}{F}:T6{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ",
			// walkDown
			"[walkDown]SHIELD0{-4,8}{F}:A1{0,0}{F}:B4{0,8}{F,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"SHIELD0{-4,7}{F}:A1{0,-1}{F}:B5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"SHIELD0{-4,5}{F}:A1{0,-2}{F}:S5{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SHIELD0{-4,6}{F}:A1{0,0}{F}:S6{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SHIELD0{-4,8}{F}:A1{0,0}{F}:B4{0,8}{F,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"SHIELD0{-4,7}{F}:A1{0,-1}{F}:B5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"SHIELD0{-4,5}{F}:A1{0,-2}{F}:S5{0,8}{F,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"SHIELD0{-4,6}{F}:A1{0,0}{F}:S6{0,8}{F,M}:SHADOW0{0,7}{F} @ 1 ",
			// bonk
			"[bonk]SWORD0{0,4}{R,M}:F3{0,0}{F}:G3{0,16}{T}:SHIELD2{-3,5}{F} @ 3 ",
			// bonkUp
			"[bonkUp]F4{0,0}{F}:G4{0,16}{T}:SWORD0{-4,0}{R,M}:SHIELD2{8,3}{F} @ 3 ",
			// bonkDown
			"[bonkDown]SHIELD0{-4,6}{F}:F2{0,0}{F}:G2{0,16}{T}:SWORD0{12,1}{R} @ 3 ",
			// swim
			"[swim]P0{4,14}{TL}:I7{-3,5}{F}:H5{0,0}{F} @ 8 ;" +
				"J5{-1,4}{L,U}:J0{-5,4}{F}:H6{0,0}{F} @ 8 ;" +
				"I7{-3,5}{F}:H5{0,0}{F} @ 8 ;" +
				"P0{11,12}{BL,M}:J1{-5,4}{F}:H7{0,0}{F} @ 8 ",
			// swimUp
			"[swimUp]A2{0,0}{F}:I5{0,8}{F}:P0{-6,7}{BL,U}:P0{14,7}{BL,UM} @ 8 ;" +
				"A2{0,0}{F}:I6{0,8}{F,M}:J5{-5,13}{TL,U}:J5{13,13}{TL,UM} @ 8 ;" +
				"A2{0,0}{F}:I5{0,8}{F} @ 8 ;" +
				"E4{0,0}{F}:I6{0,8}{F}:P0{-2,2}{TL,U}:P0{10,2}{TL,UM} @ 8 ",
			// swimDown
			"[swimDown]P0{-5,14}{BL}:P0{14,14}{BL,M}:I3{0,0}{F}:J3{0,8}{F} @ 8 ;" +
				"J5{-6,7}{TL}:J5{14,7}{TL,M}:I4{0,0}{F,M}:J3{0,8}{F,M} @ 8 ;" +
				"I3{0,0}{F}:J3{0,8}{F} @ 8 ;" +
				"P0{-1,20}{TL}:P0{9,20}{TL,M}:I4{0,0}{F}:J4{0,8}{F} @ 8 ",
			// treadingWater
			"[treadingWater]A0{-1,1}{F}:L0{0,3}{F} @ 16 ;" +
				"A0{-1,0}{F}:L0{0,3}{F,M} @ 16 ",
			// treadingWaterUp
			"[treadingWaterUp]A2{0,0}{F}:J2{0,7}{F} @ 16 ;" +
				"A2{0,1}{F}:J2{0,7}{F,M} @ 16 ",
			// treadingWaterDown
			"[treadingWaterDown]A1{0,0}{F}:J2{0,7}{F} @ 16 ;" +
				"A1{0,1}{F}:J2{0,7}{F,M} @ 16 ",
			// attack
			"[attack]A0{-2,0}{F}:C2{-1,7}{F}:SWORD0{5,-4}{L}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,0}{F}:C2{-1,7}{F}:SWORD0{9,-4}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,0}{F}:C3{-1,7}{F}:SWORD1{11,-3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{0,0}{F}:C3{-1,7}{F}:SWORD2{14,-3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"Z6{10,11}{BL}:A0{0,1}{F}:C4{-1,7}{F,0}{0}:SWORD2{17,0}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"Z6{14,11}{BL}:A0{3,1}{F}:Α7{-1,7}{F}:SWORD3{20,10}{XW}:SHADOW0{0,7}{F} @ 4 ;" +
				"Z6{10,11}{BL}:A0{0,1}{F}:C4{-1,7}{F}:SWORD2{16,15}{F,U}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD5{13,19}{F,U}:A0{-1,0}{F,1}{0}:C5{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD6{8,20}{F,U}:A0{-1,0}{F,1}{0}:C5{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ",
			// attackUp
			"[attackUp]F1{0,0}{F}:G1{0,16}{T}:SHIELD1{16,6}{R}:SWORD3{12,8}{B}:SHADOW0{0,7}{F} @ 1;" +
				"F1{0,0}{F}:G1{0,16}{T}:SHIELD1{16,6}{R}:SWORD4{11,5}{B}:SHADOW0{0,7}{F} @ 1;" +
				"A2{0,-1}{F}:D1{0,8}{F}:SHIELD1{16,4}{R}:SWORD5{9,-7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"A2{0,-1}{F}:D1{0,8}{F}:SHIELD1{16,4}{R}:SWORD7{8,-19}{XL}:SHADOW0{0,7}{F} @ 1 ;" +
				"A2{0,-2}{F}:D2{0,8}{F}:SHIELD1{16,2}{R}:SWORD7{3,-21}{XL}:SHADOW0{0,7}{F} @ 1 ;" +
				"A2{0,-5}{F}:β1{0,8}{F}:SHIELD1{16,0}{R}:SWORD8{-1,-24}{XT}:SHADOW0{0,7}{F} @ 4 ;" +
				"A2{0,-2}{F}:D2{0,8}{F}:SHIELD1{16,3}{R}:SWORD7{-13,-19}{XL,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"A2{0,0}{F}:L4{0,8}{F}:SHIELD1{16,4}{R}:SWORD1{-15,-6}{F,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"A2{0,0}{F}:L4{0,8}{F}:SHIELD1{16,4}{R}:SWORD2{-16,-2}{F,M}:SHADOW0{0,7}{F} @ 2 ",
			// attackDown
			"[attackDown]SWORD3{-9,10}{B,M}:F0{0,0}{F}:G0{0,16}{T}:SHIELD1{-7,-1}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD4{-9,14}{B,UM}:SHIELD1{-7,-1}{R,M}:F0{0,0}{F}:G0{0,16}{T}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD5{-8,17}{F,UM}:SHIELD1{-7,2}{R,M}:A1{0,1}{F}:C6{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD9{-7,19}{F,UM}:SHIELD1{-7,2}{R,M}:A1{0,1}{F}:C6{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD9{-3,21}{F,UM}:SHIELD1{-8,4}{R,M}:A4{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD8{8,24}{XT,U}:SHIELD1{-8,6}{R,M}:A4{0,5}{F}:Β0{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"SWORD9{12,19}{F,U}:SHIELD1{-8,4}{R,M}:A4{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD1{14,17}{F,U}:SHIELD1{-8,4}{R,M}:A3{0,1}{F}:L3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"SWORD10{16,7}{F,U}:SHIELD1{-8,4}{R,M}:A3{0,1}{F}:L3{0,8}{F}:SHADOW0{0,7}{F} @ 2 ",
			// dashRelease
			"[dashRelease]SWORD3{10,11}{B}:A0{-2,-1}{F}:M6{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD3{10,12}{B}:K3{-2,0}{F}:V1{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD3{10,13}{B}:K4{-2,1}{F}:M7{-1,7}{F}:SHADOW0{0,7}{F} @ 2;",
			// dashReleaseUp
			"[dashReleaseUp]A2{0,0}{F}:M3{0,8}{F}:SWORD0{0,-7}{R}:SHIELD1{15,3}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,1}{F}:M4{0,8}{F}:SWORD0{0,-6}{R}:SHIELD1{15,4}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,2}{F}:M5{0,8}{F}:SWORD0{0,-5}{R}:SHIELD1{15,5}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,0}{F}:M3{0,8}{F}:SWORD0{0,-7}{R}:SHIELD1{15,3}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,1}{F}:M4{0,8}{F,M}:SWORD0{0,-6}{R}:SHIELD1{15,4}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{0,2}{F}:M5{0,8}{F,M}:SWORD0{0,-5}{R}:SHIELD1{15,5}{R}:SHADOW0{0,7}{F} @ 2 " ,
			// dashReleaseDown TODO: 100% confirm
			"[dashReleaseDown]A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:M1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,2}{F}:M2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:β2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,2}{F}:β3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ",
			// spinAttack
			"[spinAttack]A0{-2,-1}{F}:I0{-1,7}{F}:SWORD4{-14,4}{B,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"SWORD4{-15,11}{B,UM}:A0{-3,-1}{F}:P1{-1,7}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{-2,-1}{F}:I0{-1,7}{F}:SWORD4{-14,4}{B,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{-2,-1}{F}:B0{-1,7}{F}:SWORD0{-2,-10}{R,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{-2,-1}{F}:B0{-1,7}{F}:SWORD0{8,-10}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A1{-1,-1}{F}:P3{-1,7}{F}:SWORD4{15,5}{B}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD4{15,14}{B,U}:A1{-1,-1}{F}:P3{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD0{7,19}{R,U}:A0{0,-1}{F,M}:B0{-1,7}{F,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD0{-2,19}{R,UM}:A0{0,-1}{F,M}:B0{-1,7}{F,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD4{-17,13}{B,UM}:A2{-1,-1}{F}:P2{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A2{-1,-1}{F}:P2{-1,7}{F}:SWORD4{-17,5}{B,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{-2,-1}{F}:I0{-1,7}{F}:SWORD4{-14,4}{B,M}:SHADOW0{0,7}{F} @ 6 ",
			// spinAttackLeft
			"[spinAttackLeft]SWORD4{13,15}{B,U}:A0{2,-1}{F,M}:I1{1,7}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"SWORD4{16,7}{B}:A0{3,-1}{F,M}:I2{1,7}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SWORD4{13,15}{B,U}:A0{2,-1}{F,M}:I1{1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD0{9,19}{R,U}:A0{2,-1}{F,M}:B0{1,7}{F,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD0{0,19}{R,UM}:A0{2,-1}{F,M}:B0{1,7}{F,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD4{-15,13}{B,UM}:A2{1,-1}{F}:P2{1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD4{-15,5}{B,M}:A2{1,-1}{F}:P2{1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{0,-1}{F}:B0{1,7}{F}:SWORD0{0,-10}{R,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{0,-1}{F}:B0{1,7}{F}:SWORD0{10,-10}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A1{1,-1}{F}:P3{1,7}{F}:SWORD4{17,5}{B}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD4{17,14}{B,U}:A1{1,-1}{F}:P3{1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD4{13,15}{B,U}:A0{2,-1}{F,M}:I1{1,7}{F}:SHADOW0{0,7}{F} @ 6",
			// spinAttackUp TODO: 100% confirm
			"[spinAttackUp]A2{0,-3}{F}:D1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"F1{0,-3}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:D1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:P2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,-3}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:P3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,-3}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:D1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ",
			// spinAttackDown TODO: 100% confirm
			"[spinAttackDown]A1{0,-3}{F}:C6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"F0{0,-3}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:C6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:P3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,-3}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:P2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,-3}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:C6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ",
			// dashSpinup
			"[dashSpinup]A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 4 ;" +
				"A0{-2,-2}{F}:B1{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"K3{-1,-2}{F}:B2{-1,7}{F}:SHIELD1{13,0}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"K4{-2,-1}{F}:Q7{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{-2,-1}{F}:S4{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"A0{-2,-2}{F}:R6{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 2 ;" +
				"K3{-1,-2}{F}:R7{-1,7}{F}:SHIELD1{13,0}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"K4{-1,-1}{F}:S3{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,-2}{F}:B1{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"K3{-1,-2}{F}:B2{-1,7}{F}:SHIELD1{13,0}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"K4{-2,-1}{F}:Q7{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,-1}{F}:S4{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,-2}{F}:R6{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"K3{-1,-2}{F}:R7{-1,7}{F}:SHIELD1{13,0}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"K4{-1,-1}{F}:S3{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,-2}{F}:B1{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"K3{-1,-2}{F}:B2{-1,7}{F}:SHIELD1{13,0}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"K4{-2,-1}{F}:Q7{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,-1}{F}:S4{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ",
			// dashSpinupUp TODO: 100% confirm
			"[dashSpinupUp]A2{0,-3}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:B6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:C0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:S7{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T7{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:B6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:C0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:S7{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T7{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:B6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:C0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:S7{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T7{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,-3}{F}:T5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ",
			// dashSpinupDown TODO: 100% confirm
			"[dashSpinupDown]A1{0,-3}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S6{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S6{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:B5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,-3}{F}:S6{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ",
			// salute
			"[salute]SWORD1{9,-6}{F,M}:SHIELD0{-5,9}{F}:A3{0,0}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ",
			// itemGet
			"[itemGet]PENDANT2{5,-15}{F}:SHIELD1{-5,6}{R,M}:L1{-1,-1}{F}:L2{-1,7}{F}:SHADOW0{0,7}{F} @ 3 ",
			// flute
			"[swagDuck]DUCK2{8,-21}{F}:L1{-1,-8}{F}:L2{-1,0}{F} @ 4 ;" +
				"DUCK3{8,-21}{F}:L1{-1,-8}{F}:L2{-1,0}{F} @ 8 ",
			// triforceGet
			"[triforceGet]Z2{0,0}{F}:β4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ",
			// readBook
			"[readBook]A2{1,0}{F}:K6{0,8}{F}:BOOK4{-4,0}{F}:SHADOW0{0,7}{F} @ 3 ",
			// prayer
			"[prayer]R1{0,0}{F}:S1{0,16}{T}:SHADOW0{0,7}{F} @ 157 ;" +
				"A5{0,1}{F}:Q1{0,8}{F}:SHADOW0{0,7}{F} @ 47 ;" +
				"A5{0,1}{F}:Q0{0,8}{F}:SHADOW0{0,7}{F} @ 47 ;" +
				"S1{0,0}{B}:T1{0,8}{F}:SHADOW0{0,7}{F} @ 60 ",
			// fall 
			"[fall]G0{0,0}{B}:G1{16,0}{BL}:H0{0,8}{F}:H1{16,8}{L} @ 9 ;" +
				"E5{8,4}{F} @ 10 ;" +
				"E6{8,4}{F} @ 10 ;" +
				"H4{12,8}{TR} @ 10 ;" +
				"H4{12,8}{BR} @ 10 ;" +
				"G4{12,8}{BR} @ 10 ;" +
				"G4{0,0}{E} @ 10 ",
			// grab - Animation is based on input, so use half second loop
			"[grab]A0{0,-1}{F}:X2{-1,7}{F}:SHADOW0{0,7}{F} @ 30 ;" +
				"J5{-8,12}{BL,U}:Z3{-5,0}{F}:Z4{-1,7}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"J5{-10,14}{BL,U}:Z3{-5,0}{F}:Z4{-1,7}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"Z3{-5,0}{F}:Z4{-1,7}{F}:SHADOW0{0,7}{F} @ 13 ",
			// grabUp - Animation is based on input, so use half second loop
			"[grabUp]Z0{0,1}{F}:O5{0,8}{F}:SHADOW0{0,7}{F} @ 30 ;" +
				"J5{13,5}{BL,UM}:J5{-5,5}{BL,U}:Y6{0,0}{F}:Z6{0,16}{T}:SHADOW0{0,7}{F} @ 10 ;" +
				"J5{16,11}{BL,UM}:J5{-8,11}{BL,U}:Y6{0,0}{F}:Z6{0,16}{T}:SHADOW0{0,7}{F} @ 10 ;" +
				"Y6{0,0}{F}:Z6{0,16}{T}:SHADOW0{0,7}{F} @ 10 " ,
			// grabDown - Animation is based on input, so use half second loop
			"[grabDown]E3{0,3}{F}:X5{0,8}{F}:SHADOW0{0,7}{F} @ 30 ;" +
				"J5{13,6}{BL,M}:J5{-5,6}{BL}:P7{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"J5{16,1}{BL,M}:J5{-8,1}{BL}:U0{0,2}{F}:P7{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"U0{0,2}{F}:P7{0,8}{F}:SHADOW0{0,7}{F} @ 13 ",
			// lift
			"[lift]BUSH3{12,-4}{F}:ITEMSHADOW0{12,5}{F}:E2{1,1}{F}:U5{-1,7}{F}:SHADOW0{0,7}{F} @ 5 ;" +
				"BUSH3{12,-4}{F}:ITEMSHADOW0{12,5}{F}:J5{-6,7}{BL}:U1{-2,1}{F}:U6{-1,7}{F}:SHADOW0{0,7}{F} @ 7 ;" +
				"BUSH3{12,-4}{F}:ITEMSHADOW0{12,5}{F}:J5{-9,11}{BL,U}:U1{-2,1}{F}:U6{-1,7}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"BUSH3{9,-7}{F}:ITEMSHADOW0{9,5}{F}:J5{-9,11}{BL,U}:U1{-2,1}{F}:U6{-1,7}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{4,-10}{F}:ITEMSHADOW0{4,5}{F}:L6{-2,-1}{F}:O2{-1,7}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{-1,-12}{F}:L6{-2,-1}{F}:O2{-1,7}{F}:SHADOW0{0,7}{F} @ 4 ",
			// liftUp
			"[liftUp]BUSH3{0,-8}{F}:ITEMSHADOW0{0,6}{F}:U2{0,2}{F}:U7{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"J5{-7,7}{BL}:J5{15,7}{BL,M}:BUSH3{0,-8}{F}:ITEMSHADOW0{0,6}{F}:A2{0,2}{F}:V0{0,8}{F}:SHADOW0{0,7}{F} @ 7 ;"+
				"J5{-9,10}{BL,U}:J5{17,10}{BL,UM}:BUSH3{0,-8}{F}:ITEMSHADOW0{0,6}{F}:A2{0,2}{F}:V0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"J5{-9,10}{BL,U}:J5{17,10}{BL,UM}:BUSH3{0,-9}{F}:ITEMSHADOW0{0,6}{F}:A2{0,2}{F}:V0{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-10}{F}:ITEMSHADOW0{0,6}{F}:L7{0,0}{F}:O5{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-11}{F}:ITEMSHADOW0{0,6}{F}:L7{0,0}{F}:O5{0,8}{F}:SHADOW0{0,7}{F} @ 20 ",
			// liftDown
			"[liftDown]BUSH3{0,5}{F}:E3{0,3}{F}:U4{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"BUSH3{0,5}{F}:J5{-8,5}{BL}:J5{16,5}{BL,M}:U0{0,3}{F}:U4{0,8}{F}:SHADOW0{0,7}{F} @ 7 ;" +
				"BUSH3{0,5}{F}:J5{-10,8}{BL,U}:J5{18,8}{BL,UM}:U0{0,3}{F}:U4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"BUSH3{0,-5}{F}:J5{-10,8}{BL,U}:J5{18,8}{BL,UM}:U0{0,3}{F}:U4{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-17}{F}:L5{0,0}{F}:N7{0,8}{F}:SHADOW0{0,7}{F} @ 30 ",
			// carry
			"[carry]BUSH3{-1,-12}{F}:L6{-2,-1}{F}:O2{-1,7}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{-1,-11}{F}:L6{-2,0}{F}:O3{-1,7}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{-1,-10}{F}:L6{-2,1}{F}:O4{-1,7}{F}:SHADOW0{0,7}{F} @ 4 ",
			// carryUp
			"[carryUp]BUSH3{0,-11}{F}:L7{0,0}{F}:O5{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-10}{F}:L7{0,1}{F}:O6{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-9}{F}:L7{0,2}{F}:O7{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-11}{F}:L7{0,0}{F}:O5{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-10}{F}:L7{0,1}{F}:O6{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-9}{F}:L7{0,2}{F}:O7{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ",
			// carryDown
			"[carryDown]BUSH3{0,-11}{F}:L5{0,0}{F}:N7{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-10}{F}:L5{0,1}{F}:O0{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-9}{F}:L5{0,2}{F}:O1{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-11}{F}:L5{0,0}{F}:N7{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-10}{F}:L5{0,1}{F}:O0{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"BUSH3{0,-9}{F}:L5{0,2}{F}:O1{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;",
			// treePull
			"[treePull]Y6{0,0}{F}:Z6{0,16}{T}:SHADOW0{0,7}{F} @ 30 ; " +
				"P7{0,8}{F,UM}:E7{0,12}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"N0{0,8}{F}:A1{0,14}{F,U}:SHADOW0{0,7}{F} @ 3 ;" +
				"K2{0,8}{F}:K0{0,12}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"K1{0,11}{F}:β7{8,16}{BR}:SHADOW0{0,7}{F} @ 3 ;" +
				"F4{0,0}{F}:G4{0,16}{T}:SHADOW0{0,7}{F} @ 3 ;" +
				"P7{0,8}{F,UM}:E7{0,12}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"N0{0,8}{F}:A1{0,14}{F,U}:SHADOW0{0,7}{F} @ 3 ;" +
				"K2{0,8}{F}:K0{0,12}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"K1{0,11}{F}:β7{8,16}{BR}:SHADOW0{0,7}{F} @ 3 ",
			// throw
			"[throw]BUSH3{-1,-12}{F}:L6{-2,-1}{F}:O2{-1,7}{F}:SHADOW0{0,7}{F} @ 20 ;" +
				"BUSH3{-1,-12}{F}:ITEMSHADOW0{-1,5}{F}:A0{-2,-1}{F}:M6{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{2,-12}{F}:ITEMSHADOW0{2,5}{F}:A0{-2,-1}{F}:M6{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{6,-12}{F}:ITEMSHADOW0{6,5}{F}:A0{-2,-1}{F}:M6{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{10,-12}{F}:ITEMSHADOW0{10,5}{F}:A0{-2,-1}{F}:M6{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{14,-12}{F}:ITEMSHADOW0{14,5}{F}:A0{-2,-1}{F}:M6{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{18,-12}{F}:ITEMSHADOW0{18,5}{F}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{5,2}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{22,-11}{F}:ITEMSHADOW0{22,5}{F}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{5,2}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{26,-10}{F}:ITEMSHADOW0{26,5}{F}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{5,2}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{30,-9}{F}:ITEMSHADOW0{30,5}{F}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{5,2}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{34,-9}{F}:ITEMSHADOW0{34,5}{F}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{5,2}{F}:SHADOW0{0,7}{F} @ 1 ",
			// throwUp
			"[throwUp]BUSH3{0,-11}{F}:L7{0,0}{F}:O5{0,8}{F}:SHADOW0{0,7}{F} @ 20 ;" +
				"BUSH3{0,-11}{F}:ITEMSHADOW0{0,6}{F}:A2{0,0}{F}:M3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-15}{F}:ITEMSHADOW0{0,2}{F}:A2{0,0}{F}:M3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-19}{F}:ITEMSHADOW0{0,-2}{F}:A2{0,0}{F}:M3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-23}{F}:ITEMSHADOW0{0,-6}{F}:A2{0,0}{F}:M3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-27}{F}:ITEMSHADOW0{0,-10}{F}:A2{0,0}{F}:M3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-31}{F}:ITEMSHADOW0{0,-14}{F}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-35}{F}:ITEMSHADOW0{0,-18}{F}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-39}{F}:ITEMSHADOW0{0,-22}{F}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-43}{F}:ITEMSHADOW0{0,-26}{F}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-47}{F}:ITEMSHADOW0{0,-30}{F}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ",
			// throwDown
			"[throwDown]BUSH3{0,-11}{F}:L5{0,0}{F}:N7{0,8}{F}:SHADOW0{0,7}{F} @ 20 ;" +
				"BUSH3{0,-11}{F}:ITEMSHADOW0{0,6}{F}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-8}{F}:ITEMSHADOW0{0,9}{F}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,-4}{F}:ITEMSHADOW0{0,13}{F}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,0}{F}:ITEMSHADOW0{0,17}{F}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,4}{F}:ITEMSHADOW0{0,21}{F}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,8}{F}:ITEMSHADOW0{0,25}{F}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,12}{F}:ITEMSHADOW0{0,29}{F}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,16}{F}:ITEMSHADOW0{0,33}{F}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,20}{F}:ITEMSHADOW0{0,37}{F}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BUSH3{0,24}{F}:ITEMSHADOW0{0,41}{F}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ",
			// push
			"[push]U1{-1,-1}{F}:X2{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U1{-1,0}{F}:X3{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U1{-1,1}{F}:X4{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U1{-1,-1}{F}:X2{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U1{-1,0}{F}:X3{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U1{-1,-1}{F}:X2{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U1{-1,0}{F}:X3{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U1{-1,1}{F}:X4{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ",
			// pushUp
			"[pushUp]U2{0,0}{F}:M3{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U2{0,1}{F}:M4{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U2{0,2}{F}:M5{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U2{0,0}{F}:M3{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U2{0,1}{F}:M4{0,8}{F,M}:SHADOW0{0,7}{F} @ 8 ;" +
				"U2{0,0}{F}:M3{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U2{0,1}{F}:M4{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U2{0,2}{F}:M5{0,8}{F}:SHADOW0{0,7}{F} @ 8 ",
			// pushDown
			"[pushDown]U0{0,2}{F}:X5{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U0{0,3}{F}:X6{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U0{0,4}{F}:X7{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U0{0,2}{F}:X5{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U0{0,3}{F}:X6{0,8}{F,M}:SHADOW0{0,7}{F} @ 8 ;" +
				"U0{0,2}{F}:X5{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U0{0,3}{F}:X6{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"U0{0,4}{F}:X7{0,8}{F}:SHADOW0{0,7}{F} @ 8 ",
			// shovel
			"[shovel]SHOVEL0{8,15}{F}:B7{-1,-2}{F}:D7{-1,7}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"SHOVEL0{12,16}{F}:A0{-1,-1}{F}:F5{-1,7}{F}:SHADOW0{0,7}{F} @ 19 ;" +
				"A0{-2,-1}{F}:C7{-1,7}{F}:SHOVEL1{-1,-5}{TL}:SHADOW0{0,7}{F} @ 17 ",
			// boomerang
			"[boomerang]BOOMERANG2{-8,-3}{F}:S2{-1,-1}{B}:T2{-1,7}{F}:SHIELD1{13,1}{R}:SHADOW0{0,7}{F} @ 8 ;" +
				"BOOMERANG2{10,6}{F}:A0{-1,0}{F}:C4{-1,7}{F}:SHIELD2{-1,5}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{12,6}{F}:A0{-1,0}{F}:C4{-1,7}{F}:SHIELD2{-1,5}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{14,6}{F}:A0{-1,0}{F}:C4{-1,7}{F}:SHIELD2{-1,5}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{16,8}{F,U}:A0{-1,0}{F}:C4{-1,7}{F}:SHIELD2{-1,5}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{18,8}{F,U}:A0{-1,0}{F}:C4{-1,7}{F}:SHIELD2{-1,5}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{20,8}{F,U}:A0{-1,0}{F}:C4{-1,7}{F}:SHIELD2{-1,5}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{22,8}{F,U}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{18,8}{F,UM}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{16,8}{F,UM}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{14,8}{F,UM}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{12,8}{F,UM}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{10,6}{F,M}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{8,6}{F,M}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{6,6}{F,M}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{4,6}{F,M}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{13,2}{R}:SHADOW0{0,7}{F} @ 1 ",
			// boomerangUp
			"[boomerangUp]BOOMERANG2{-9,-3}{F}:R2{0,0}{F}:S2{0,16}{T}:SHIELD2{5,4}{F}:SHADOW0{0,7}{F} @ 7 ;" +
				"BOOMERANG2{1,-11}{F}:SHIELD1{15,4}{R}:A2{0,0}{F}:Q6{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,-13}{F}:SHIELD1{15,4}{R}:A2{0,0}{F}:Q6{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,-15}{F}:SHIELD1{15,4}{R}:A2{0,0}{F}:Q6{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,-17}{F,M}:SHIELD1{15,4}{R}:A2{0,0}{F}:Q6{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,-19}{F,M}:SHIELD1{15,4}{R}:A2{0,0}{F}:Q6{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,-21}{F,M}:SHIELD1{15,4}{R}:A2{0,0}{F}:Q6{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,-19}{F,M}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,-15}{F,UM}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,-13}{F,UM}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,-11}{F,UM}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,-9}{F,UM}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,-7}{F,U}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 " +
				"A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,3}{F}:SHADOW0{0,7}{F} @ 1 ",
			// boomerangDown
			"[boomerangDown]BOOMERANG2{10,-1}{F,M}:SHIELD0{-4,9}{F}:A1{0,1}{F}:Q5{0,8}{F}:SHADOW0{0,7}{F} @ 7 ;" +
				"BOOMERANG2{-1,15}{F,M}:SHIELD1{-8,5}{R,M}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,17}{F,M}:SHIELD1{-8,5}{R,M}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,19}{F,M}:SHIELD1{-8,5}{R,M}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,23}{F,UM}:SHIELD1{-8,5}{R,M}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,25}{F,UM}:SHIELD1{-8,5}{R,M}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,27}{F,UM}:SHIELD1{-8,5}{R,M}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,29}{F,UM}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,31}{F,U}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,29}{F,U}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,27}{F,U}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,25}{F,U}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,21}{F}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,19}{F}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{1,17}{F}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"BOOMERANG2{-1,13}{F,M}:SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,7}{F}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 1 ",
			// rod
			"[rod]ROD0{6,-4}{L}:G2{-1,-1}{BR}:G3{7,-1}{BL}:H2{-1,7}{R}:H3{7,7}{L}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"ROD2{10,0}{F}:A0{-1,0}{F}:C4{-1,7}{F}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"ROD1{10,13}{T}:A0{-1,0}{F}:N4{-1,7}{F}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 6 ;",
			// rodUp
			"[rodUp]ROD0{-2,-9}{R}:G3{0,0}{BR}:G4{8,0}{BL}:H3{0,8}{R}:H4{8,8}{L}:SHIELD1{15,4}{R}:SHADOW0{0,7}{F} @ 3 ;" +
				"SHIELD1{15,3}{R}:A2{0,-1}{F}:D2{0,8}{F}:ROD0{-2,-4}{L}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD1{15,3}{R}:A2{0,-1}{F}:N5{0,8}{F}:ROD3{-2,-2}{L}:SHADOW0{0,7}{F} @ 6 ",
			// rodDown
			"[rodDown]ROD0{10,-4}{L}:SHIELD1{-8,5}{R,M}:G1{0,0}{BR}:G2{8,0}{BL}:H1{0,8}{R}:H2{8,8}{L}:SHADOW0{0,7}{F} @ 3 ;" +
				"ROD1{10,10}{BL}:SHIELD1{-8,5}{R,M}:A3{0,1}{F}:N6{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"ROD0{9,19}{R,U}:SHIELD1{-8,5}{R,M}:A3{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ",
			// hammer
			"[hammer]HAMMER0{7,-4}{L}:G2{0,-1}{BR}:G3{8,-1}{BL}:H2{0,7}{R}:H3{8,7}{L}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"HAMMER3{11,0}{F}:A0{0,0}{F}:C4{0,7}{F}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"HAMMER2{12,13}{T}:A0{0,0}{F}:N4{0,7}{F}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 18 ",
			// hammerUp
				"[hammerUp]HAMMER1{-2,-8}{L}:G3{0,0}{BR}:G4{8,1}{BL}:H3{0,8}{R}:H4{8,8}{L}:SHIELD1{15,4}{R}:SHADOW0{0,7}{F} @ 4 ;" +
				"A2{0,-1}{F}:D2{0,8}{F}:HAMMER0{-2,-4}{R}:SHIELD1{15,3}{R}:SHADOW0{0,7}{F} @ 5 ;" +
				"A2{0,-1}{F}:N5{0,8}{F}:HAMMER1{-2,-2}{R}:SHIELD1{15,3}{R}:SHADOW0{0,7}{F} @ 18",
			// hammerDown
			"[hammerDown]HAMMER0{10,-2}{R}:SHIELD1{-8,5}{R,M}:G1{0,0}{BR}:G2{8,0}{BL}:H1{0,8}{R}:H2{8,8}{L}:SHADOW0{0,7}{F} @ 4 ;" +
				"HAMMER2{10,10}{BL}:SHIELD1{-8,5}{R,M}:A3{0,1}{F}:N6{0,8}{F}:SHADOW0{0,7}{F} @ 5 ;" +
				"HAMMER1{9,18}{L,U}:SHIELD1{-8,5}{R,M}:A3{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 18 ",
			// powder
			"[powder]A0{-2,0}{F}:C2{-1,7}{F}:SHIELD2{-2,2}{F}:SHADOW0{0,7}{F} @ 5 ;" +
				"A0{-2,0}{F}:C3{-1,7}{F}:SHIELD2{-2,2}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{-1,0}{F}:C4{-1,7}{F}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{-2,0}{F}:C5{-1,7}{F}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 13 ",
			// powderUp
			"[powderUp]SHIELD0{6,10}{F}:F1{0,0}{F}:G1{0,16}{T}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{8,6}{F}:A2{0,0}{F}:D1{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,-1}{F}:D2{0,8}{F}:SHIELD1{16,4}{R}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:L4{0,8}{F}:SHIELD1{16,4}{R}:SHADOW0{0,7}{F} @ 13 ",
			// powderDown
			"[powderDown]F0{0,0}{F}:G0{0,16}{T}:SHIELD2{-5,-1}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"A1{0,1}{F}:C6{0,8}{F}:SHIELD2{-7,1}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A3{0,1}{F}:L3{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 13 ",
			// cane
			"[cane]CANE2{-14,-1}{F,M}:A0{-4,-1}{F}:I2{-2,7}{F,M}:SHADOW0{-1,7}{F} @ 3 ;" +
				"CANE0{3,-4}{L,M}:L6{-3,-1}{F}:O2{-2,7}{F}:SHADOW0{-1,7}{F} @ 4 ;" +
				"CANE1{13,11}{T}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 6 ",
			// caneUp
			"[caneUp]CANE0{-1,4}{L}:F1{0,0}{F,M}:G1{0,16}{T,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"CANE0{-3,-3}{L}:A2{0,0}{F,M}:P5{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"A2{0,-2}{F}:D2{0,8}{F}:CANE3{-3,-8}{L}:SHADOW0{0,7}{F} @ 6 ",
			// caneDown
			"[caneDown]F0{0,0}{F,M}:G0{0,16}{T,M}:CANE0{9,-8}{L}:SHADOW0{0,7}{F} @ 3 ;" +
				"CANE0{12,-3}{L}:A1{0,0}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"CANE0{8,16}{R,U}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ",
			// bow
				"[bow]A0{-2,-1}{F}:B0{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"BOW2{1,9}{F}:A0{-2,-1}{F}:M6{-1,7}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"BOW0{11,6}{L}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BOW0{11,6}{L}:A0{-2,-1}{F}:P6{-1,7}{F}:SHADOW0{0,7}{F} @ 9 ",
			// bowUp
			"[bowUp]A2{0,0}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 2 ; " +
				"A2{0,0}{F}:C1{0,8}{F}:BOW0{-3,5}{L,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,1}{F}:M4{0,8}{F}:BOW1{-5,2}{T}:SHADOW0{0,7}{F} @ 4 ;" +
				"A2{0,0}{F}:P5{0,8}{F}:BOW1{-5,2}{T}:SHADOW0{0,7}{F} @ 9 ",
			// bowDown
			"[bowDown]A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ; " +
				"BOW0{10,9}{L}:A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ; " +
				"BOW1{2,14}{T,U}:A1{0,1}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"BOW1{2,14}{T,U}:A1{0,0}{F}:P4{0,8}{F}:SHADOW0{0,7}{F} @ 9 ",
			// bombos
			"[bombos]SHIELD0{-4,8}{F}:SWORD0{7,18}{R,UM}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{-6,3}{R,M}:SWORD4{-11,16}{B,UM}:A0{1,0}{F,M}:M6{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:M3{0,8}{F}:SHIELD2{5,2}{F}:SWORD0{1,-6}{L}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{14,3}{R}:SWORD4{11,16}{B,U}:A0{-1,0}{F}:M6{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{7,18}{R,UM}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{-6,3}{R,M}:SWORD4{-11,16}{B,UM}:A0{1,0}{F,M}:M6{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:M3{0,8}{F}:SHIELD2{5,2}{F}:SWORD0{1,-6}{L}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{14,3}{R}:SWORD4{11,16}{B,U}:A0{-1,0}{F}:M6{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SWORD0{14,-3}{R}:SHIELD0{-5,2}{F}:A1{0,0}{F}:P3{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-5,2}{F}:A1{0,0}{F}:P4{0,8}{F,M}:SWORD0{12,-6}{L}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-5,2}{F}:A1{0,0}{F}:P4{0,8}{F,M}:SWORD0{8,-7}{R,M}:SHADOW0{0,7}{F} @ 20 ;" +
				"SWORD0{14,-3}{R}:SHIELD0{-5,2}{F}:A1{0,0}{F}:P3{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD3{16,10}{B}:SHIELD1{-8,5}{R,M}:A1{0,1}{F}:L3{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD4{16,15}{B,U}:SHIELD1{-8,5}{R,M}:A1{0,1}{F}:A1{0,1}{F}:L3{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD5{14,17}{F,U}:SHIELD1{-8,5}{R,M}:A1{0,1}{F}:L3{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD0{12,19}{R,U}:SHIELD1{-8,5}{R,M}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"SWORD0{9,20}{L,U}:SHIELD1{-8,5}{R,M}:A1{0,2}{F}:D0{0,8}{F}:SHADOW0{0,7}{F} @ 60 ",
			// ether
			"[ether]SHIELD0{-4,8}{F}:SWORD0{7,18}{R,UM}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{-6,3}{R,M}:SWORD4{-11,16}{B,UM}:A0{1,0}{F,M}:M6{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:M3{0,8}{F}:SHIELD2{5,2}{F}:SWORD0{1,-6}{L}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{14,3}{R}:SWORD4{11,16}{B,U}:A0{-1,0}{F}:M6{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{7,18}{R,UM}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{-6,3}{R,M}:SWORD4{-11,16}{B,UM}:A0{1,0}{F,M}:M6{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:M3{0,8}{F}:SHIELD2{5,2}{F}:SWORD0{1,-6}{L}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{14,3}{R}:SWORD4{11,16}{B,U}:A0{-1,0}{F}:M6{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD0{-4,8}{F}:SWORD4{16,13}{B,U}:A1{0,0}{F}:P3{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{14,-6}{R}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 9 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 15 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 2 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:A7{0,-1}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{11,-6}{L}:ZAPA7{0,-1}{F}:ZAPPP4{0,8}{F,M}:SHADOW0{0,7}{F} @ 4 ;",
			// quake
			"[quake]SHIELD0{-4,8}{F}:SWORD0{7,18}{R,UM}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{-6,3}{R,M}:SWORD4{-11,16}{B,UM}:A0{1,0}{F,M}:M6{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:M3{0,8}{F}:SHIELD2{5,2}{F}:SWORD0{1,-6}{L}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{14,3}{R}:SWORD4{11,16}{B,U}:A0{-1,0}{F}:M6{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{7,18}{R,UM}:A1{0,0}{F}:M0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{-6,3}{R,M}:SWORD4{-11,16}{B,UM}:A0{1,0}{F,M}:M6{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:M3{0,8}{F}:SHIELD2{5,2}{F}:SWORD0{1,-6}{L}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{14,3}{R}:SWORD4{11,16}{B,U}:A0{-1,0}{F}:M6{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD0{-5,8}{F}:SWORD4{17,13}{B,U}:A1{0,0}{F}:P3{0,8}{F}:SHADOW0{0,7}{F} @ 8 ;" +
				"SHIELD0{-5,8}{F}:SWORD0{14,-5}{R}:A1{0,0}{F}:P4{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"SHIELD1{-5,-1}{R,M}:SWORD0{11,-8}{L}:L5{0,0}{F}:N7{0,8}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-3}{R,M}:SWORD0{11,-10}{L}:L5{0,-2}{F}:N7{0,6}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-5}{R,M}:SWORD0{11,-12}{L}:L5{0,-4}{F}:N7{0,4}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-7}{R,M}:SWORD0{11,-14}{L}:L5{0,-6}{F}:N7{0,2}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-9}{R,M}:SWORD0{11,-16}{L}:L5{0,-8}{F}:N7{0,0}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-11}{R,M}:SWORD0{11,-18}{L}:L5{0,-10}{F}:N7{0,-2}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-13}{R,M}:SWORD0{11,-20}{L}:L5{0,-12}{F}:N7{0,-4}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-15}{R,M}:SWORD0{11,-22}{L}:L5{0,-14}{F}:N7{0,-6}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-16}{R,M}:SWORD0{11,-23}{L}:L5{0,-15}{F}:N7{0,-7}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-17}{R,M}:SWORD0{11,-24}{L}:L5{0,-16}{F}:N7{0,-8}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-19}{R,M}:SWORD0{11,-26}{L}:L5{0,-18}{F}:N7{0,-10}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-20}{R,M}:SWORD0{11,-27}{L}:L5{0,-19}{F}:N7{0,-11}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-21}{R,M}:SWORD0{11,-28}{L}:L5{0,-20}{F}:N7{0,-12}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-22}{R,M}:SWORD0{11,-29}{L}:L5{0,-21}{F}:N7{0,-13}{F}:SHADOW1{0,7}{F} @ 2 ;" +
				"SHIELD1{-5,-23}{R,M}:SWORD0{11,-30}{L}:L5{0,-22}{F}:N7{0,-14}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD1{-5,-24}{R,M}:SWORD0{11,-31}{L}:L5{0,-23}{F}:N7{0,-15}{F}:SHADOW1{0,7}{F} @ 6 ;" +
				"SHIELD0{-4,-15}{F}:SWORD0{5,-4}{L,U}:A1{0,-21}{F}:Q1{0,-15}{F}:SHADOW1{0,7}{F} @ 3 ;" +
				"SHIELD0{-4,-14}{F}:SWORD0{5,-3}{L,U}:A1{0,-20}{F}:Q1{0,-14}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,-13}{F}:SWORD0{5,-2}{L,U}:A1{0,-19}{F}:Q1{0,-13}{F}:SHADOW1{0,7}{F} @ 2 ;" +
				"SHIELD0{-4,-12}{F}:SWORD0{5,-1}{L,U}:A1{0,-18}{F}:Q1{0,-12}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,-11}{F}:SWORD0{5,0}{L,U}:A1{0,-17}{F}:Q1{0,-11}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,-10}{F}:SWORD0{5,1}{L,U}:A1{0,-16}{F}:Q1{0,-10}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,-8}{F}:SWORD0{5,3}{L,U}:A1{0,-14}{F}:Q1{0,-8}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,-7}{F}:SWORD0{5,4}{L,U}:A1{0,-13}{F}:Q1{0,-7}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,-6}{F}:SWORD0{5,5}{L,U}:A1{0,-12}{F}:Q1{0,-6}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,-4}{F}:SWORD0{5,7}{L,U}:A1{0,-10}{F}:Q1{0,-4}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,-2}{F}:SWORD0{5,9}{L,U}:A1{0,-8}{F}:Q1{0,-2}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,0}{F}:SWORD0{5,11}{L,U}:A1{0,-6}{F}:Q1{0,0}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,2}{F}:SWORD0{5,13}{L,U}:A1{0,-4}{F}:Q1{0,2}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,4}{F}:SWORD0{5,15}{L,U}:A1{0,-2}{F}:Q1{0,4}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,6}{F}:SWORD0{5,17}{L,U}:A1{0,0}{F}:Q1{0,6}{F}:SHADOW1{0,7}{F} @ 1 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{5,19}{L,U}:A1{0,2}{F}:Q1{0,8}{F}:SHADOW1{0,7}{F} @ 2 ;" +
				"SHIELD0{-4,8}{F}:SWORD0{5,19}{BL,U}:A1{0,2}{F}:Q1{0,8}{F}:SHADOW0{0,7}{F} @ 133 ;",
			// hookshot
			"[hookshot]HOOKSHOT1{14,7}{R}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT1{18,7}{R}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT1{22,7}{R}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1;" +
				"HOOKSHOT1{26,7}{R}:HOOKSHOT1{18,11}{BL,U}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1;" +
				"HOOKSHOT1{30,7}{R}:HOOKSHOT1{22,11}{BL,U}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1;" +
				"HOOKSHOT1{26,7}{R}:HOOKSHOT1{18,11}{BL}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1;" +
				"HOOKSHOT1{22,7}{R}:HOOKSHOT0{14,11}{TL}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT1{18,7}{R}:HOOKSHOT0{14,11}{TL}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT1{14,7}{R}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT1{10,7}{R}:HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{14,11}{TL}:A0{-1,0}{F}:C4{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ",
			// hookshotUp
			"[hookshotUp]HOOKSHOT0{0,0}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,-4}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,-8}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT1{4,-4}{BL}:HOOKSHOT0{0,-12}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT1{4,-8}{BL}:HOOKSHOT0{0,-16}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT1{4,-4}{BL,M}:HOOKSHOT0{0,-12}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,-8}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,-4}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,0}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,4}{B}:A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ;" +
				"A2{0,-1}{F}:D2{0,8}{F}:HOOKSHOT0{4,2}{TR}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 1 ",
			// hookshotDown
			"[hookshotDown]HOOKSHOT0{0,24}{B,U}:HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,28}{B,U}:HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,32}{B,U}:HOOKSHOT1{4,23}{BL}:HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,36}{B,U}:HOOKSHOT1{4,27}{BL}:HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,32}{B,U}:HOOKSHOT1{4,23}{BL,M}:HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,28}{B,U}:HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,24}{B,U}:HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{0,20}{B,U}:HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;" +
				"HOOKSHOT0{4,17}{TR}:A3{0,1}{F}:D0{0,8}{F}:SHIELD1{-8,5}{R,M}:SHADOW0{0,7}{F} @ 1 ;",
			// zap
			"[zap]S0{0,0}{B}:T0{0,8}{F} @ 2 ;" + "A0{0,0}{E} @ 1 ;" +
				"ZAPS0{0,0}{B}:ZAPT0{0,8}{F} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"ZAPS0{0,0}{B}:ZAPT0{0,8}{F} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"R0{0,0}{F}:S0{0,16}{T} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"ZAPR0{0,0}{F}:ZAPS0{0,16}{T} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"ZAPR0{0,0}{F}:ZAPS0{0,16}{T} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"S0{0,0}{B}:T0{0,8}{F} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"ZAPS0{0,0}{B}:ZAPT0{0,8}{F} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"ZAPS0{0,0}{B}:ZAPT0{0,8}{F} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"R0{0,0}{F}:S0{0,16}{T} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"ZAPR0{0,0}{F}:ZAPS0{0,16}{T} @ 1 ;" + "A0{0,0}{E} @ 1 ;" +
				"ZAPR0{0,0}{F}:ZAPS0{0,16}{T} @ 1 ;" + "A0{0,0}{E} @ 1 ",
			// bunnyStand
			"[bunnyStand]α4{-2,-1}{F}:α5{-1,7}{F}:SHADOW0{0,7}{F} @ 3 ",
			// bunnyStandUp
			"[bunnyStandUp]α1{0,0}{F}:α2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ",
			// bunnyStandDown 
			"[bunnyStandDown]Z5{0,0}{F}:α0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ",
			// bunnyWalk
			"[bunnyWalk]α4{-2,-1}{F}:α5{-1,7}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"α4{-2,0}{F}:α6{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"α4{-2,-1}{F}:α5{-1,7}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"α4{-2,0}{F}:α6{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"α4{-2,-1}{F}:α5{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"α4{-2,0}{F}:α6{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"α4{-2,-1}{F}:α5{-1,7}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"α4{-2,0}{F}:α6{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"α4{-2,-1}{F}:α5{-1,7}{F}:SHADOW0{0,7}{F} @ 1 ;" +
				"α4{-2,0}{F}:α6{-1,7}{F}:SHADOW0{0,7}{F} @ 2 ",
			// bunnyWalkUp
			"[bunnyWalkUp]α1{0,0}{F}:α2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"α1{0,1}{F}:α3{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"α1{0,0}{F}:α2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"α1{0,1}{F}:α3{0,8}{F,M}:SHADOW0{0,7}{F} @ 2 ",
			// bunnyWalkDown
			"[bunnyWalkDown]Z5{0,0}{F}:α0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"Z5{0,1}{F}:Z7{0,8}{F}:SHADOW0{0,7}{F} @ 2 ;" +
				"Z5{0,0}{F}:α0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"Z5{0,1}{F}:Z7{0,8}{F,M}:SHADOW0{0,7}{F} @ 2 ",
			// walkUpstairs1F TODO: 100% confirm
			"[walkUpstairs1F]A2{0,1}{F}:V5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,2}{F}:V6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,0}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,-1}{F}:Y3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,0}{F}:Y4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,1}{F}:Y5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,-1}{F}:Y3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,0}{F}:Y4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,1}{F}:Y5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,-1}{F}:Y3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,0}{F}:Y4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,2}{F}:V2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{-1,0}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,2}{F}:V2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{-1,0}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,2}{F}:V2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{-1,0}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,2}{F}:V2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{-1,0}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ",
			// walkDownstairs1F TODO: 100% confirm
			"[walkDownstairs1F]A0{0,2}{F,M}:V2{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,1}{F,M}:V1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,0}{F,M}:Y1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,1}{F,M}:Y2{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,-1}{F,M}:Y0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,0}{F,M}:Y1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,1}{F,M}:Y2{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,-1}{F,M}:Y0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,0}{F,M}:Y1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:V3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,2}{F}:V4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:V3{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,2}{F}:V4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:V3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,2}{F}:V4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:V3{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,0}{F}:S6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:B3{0,9}{F}:SHADOW0{0,7}{F} @ 3 ",
			// walkUpstairs2F TODO: 100% confirm
			"[walkUpstairs2F]A0{0,0}{F}:B0{1,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,1}{F}:V1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,0}{F}:Y1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,1}{F}:Y2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,-1}{F}:Y0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,0}{F}:Y1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,1}{F}:Y2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,-1}{F}:Y0{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,0}{F}:Y1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"B7{0,1}{F}:Y2{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:V3{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,2}{F}:V4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,1}{F}:V3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,2}{F}:V4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 3 ",
			// walkDownstairs2F TODO: 100% confirm
			"[walkDownstairs2F]A2{0,0}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,1}{F}:V5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,2}{F}:V6{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,0}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,1}{F}:D4{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,2}{F}:M5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,0}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"A2{0,1}{F}:V5{0,8}{F}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,0}{F,M}:Y4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,1}{F,M}:Y5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,-1}{F,M}:Y3{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,0}{F,M}:Y4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,1}{F,M}:Y5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,-1}{F,M}:Y3{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,0}{F,M}:Y4{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,1}{F,M}:Y5{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"X1{0,-1}{F,M}:Y3{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,1}{F,M}:V1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,2}{F,M}:V2{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,1}{F,M}:V1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,2}{F,M}:V2{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,1}{F,M}:V1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,2}{F,M}:V2{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,1}{F,M}:V1{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{0,2}{F,M}:V2{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 3 ",
			// deathSpin
			"[deathSpin]A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{-1,0}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{-1,0}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A1{0,0}{F}:B3{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{1,0}{F,M}:B0{0,8}{F,M}:SHADOW0{0,7}{F} @ 6 ;" +
				"A2{0,0}{F}:C1{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"A0{-1,0}{F}:B0{0,8}{F}:SHADOW0{0,7}{F} @ 6 ;" +
				"E2{2,0}{F}:J5{0,8}{R}:J6{8,8}{L}:SHADOW0{0,7}{F} @ 4 ;" +
				"J6{1,8}{R}:J7{9,8}{F} @ 60 ",
			// poke
			"[poke]SWORD3{12,13}{B}:A0{-2,0}{F}:N2{-1,7}{F}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"SWORD3{16,11}{B}:A0{0,1}{F}:F6{-1,7}{F}:SHIELD2{0,2}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"SWORD3{8,8}{B}:A0{-2,0}{F}:N3{-1,7}{F}:SHIELD2{-1,2}{F}:SHADOW0{0,7}{F} @ 4 ",
			// pokeUp
			"[pokeUp]A2{0,0}{F}:L4{0,8}{F}:SWORD0{-2,-4}{L}:SHIELD1{16,5}{R}:SHADOW0{0,7}{F} @ 4; " +
				"E4{0,0}{F}:D2{0,8}{F}:SWORD0{2,-8}{R}:SHIELD1{16,3}{R}:SHADOW0{0,7}{F} @ 4 ;" +
				"A2{0,0}{F}:F7{0,8}{F}:SWORD0{-2,1}{L}:SHIELD1{16,5}{R}:SHADOW0{0,7}{F} @ 4 ",
			// pokeDown
			"[pokeDown]SWORD0{11,13}{L,U}:SHIELD1{-8,5}{R,M}:A1{0,1}{F}:N1{0,8}{F}:SHADOW0{0,7}{F} @ 4 ;" +
				"SWORD0{7,20}{R,UM}:SHIELD1{-8,6}{R,M}:E3{0,2}{F}:G7{0,8}{F}:SHADOW0{0,7}{F} @ 4; " +
				"SWORD0{11,17}{L,U}:SHIELD1{-8,5}{R,M}:A1{0,1}{F}:N1{0,8}{F}:SHADOW0{0,7}{F} @ 4",
			// tallGrass
			"[tallGrass]GRASS0{-1,15}{T}:A0{-2,-1}{F}:B0{-1,7}{F}:SHIELD1{5,2}{F} @ 3 ;" +
				"GRASS0{-1,15}{B}:A0{-2,0}{F}:V1{-1,7}{F}:SHIELD1{5,3}{F} @ 3 ;" +
				"GRASS1{-1,15}{T}:A0{-2,1}{F}:V2{-1,7}{F}:SHIELD1{5,4}{F} @ 3 ",
			// tallGrassUp
			"[tallGrassUp]GRASS0{0,16}{T}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,2}{F} @ 4 ;" +
				"GRASS0{0,16}{B}:A2{0,1}{F}:V5{0,8}{F}:SHIELD2{5,3}{F} @ 4 ;" +
				"GRASS1{0,16}{T}:A2{0,2}{F}:V6{0,8}{F}:SHIELD2{5,4}{F} @ 4 ;" +
				"GRASS0{0,16}{T}:A2{0,0}{F}:C1{0,8}{F}:SHIELD2{5,2}{F} @ 4 ;" +
				"GRASS0{0,16}{B}:A2{0,1}{F}:D4{0,8}{F}:SHIELD2{5,3}{F} @ 4 ;" +
				"GRASS1{0,16}{T}:A2{0,2}{F}:M5{0,8}{F,M}:SHIELD2{5,4}{F} @ 4 ",
			// tallGrassDown
			"[tallGrassDown]GRASS0{0,16}{T}:SHIELD0{-4,8}{F}:A1{0,0}{F}:B3{0,8}{F} @ 4 ;" +
				"GRASS0{0,16}{B}:SHIELD0{-4,9}{F}:A1{0,1}{F}:V3{0,8}{F} @ 4 ;" +
				"GRASS0{0,16}{T}:SHIELD0{-4,8}{F}:A1{0,0}{F}:B3{0,8}{F} @ 4 ;" +
				"GRASS0{0,16}{T}:SHIELD0{-4,8}{F}:A1{0,0}{F}:B3{0,8}{F} @ 4 ;" +
				"GRASS0{0,16}{B}:SHIELD0{-4,9}{F}:A1{0,1}{F}:V3{0,8}{F,M} @ 4 ;" +
				"GRASS1{0,16}{T}:SHIELD0{-4,10}{F}:A1{0,2}{F}:V4{0,8}{F,M} @ 4 " +
			// mapDungeon
			"[mapDungeon]K7{0,0}{F} @ 3 ",
			// mapWorld
			"[mapWorld]Y7{0,0}{F} @ 17; " +
				"Y7{0,0}{E} @ 17 ",
			// sleep
			"[sleep]A6{0,0}{F}:BED0{-8,8}{XXL}:D3{0,5}{F} @ 3 ",
			// awake
			"[awake]E3{0,-1}{F}:BED2{-8,8}{XXL}:D3{0,6}{F} @ 3 "
		};
}