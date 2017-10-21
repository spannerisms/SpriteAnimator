import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class SpriteAnimator extends Component {
	private static final long serialVersionUID = 2114886855236406900L;

	static final int SPRITESIZE = 896 * 32; // invariable lengths
	static final int PALETTESIZE = 0x78; // not simplified to understand the numbers
	static final int RASTERSIZE = 128 * 448 * 4;

	// Almost the length of a frame at 60 FPS
	// 1/60 approx. 16.66666...
	public static final int FPS = 16;
	
	// used for parsing frame data
	static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZαβ".toUpperCase(); // to uppercase to distinguish alpha/beta
	// format of snes 4bpp {row (r), bit plane (b)}
	// bit plane 0 indexed such that 1011 corresponds to 0123
	static final int BPPI[][] = {
			{0,0},{0,1},{1,0},{1,1},{2,0},{2,1},{3,0},{3,1},
			{4,0},{4,1},{5,0},{5,1},{6,0},{6,1},{7,0},{7,1},
			{0,2},{0,3},{1,2},{1,3},{2,2},{2,3},{3,2},{3,3},
			{4,2},{4,3},{5,2},{5,3},{6,2},{6,3},{7,2},{7,3}
	};
	/* taken and modified from
	 * http://alttp.mymm1.com/sprites/includes/animations.txt
	 * credit: mike trethewey
	 * 
	 * format:
	 * [<ANIMNAME>][<ANIMSPEED>]<INDEX>{<XPOS>,<YPOS>}{<SPRITESIZE>}{<TRANSFORM>}
	 * : delimits sprites in the same frame
	 * ; delimits entire frames
	 * @ at the end tells how long this frame lasts
	 * Frames defined as "//" copy the previous frame
	 * SPRITESIZE is a flag determining what part of the sprite to draw from
	 *		F  : Full 16x16
	 *		T  : Top 16x8
	 *		B  : Bottom 16x8
	 *		R  : Right 8x16
	 *		L  : Left 8x16
	 *		TR : Top-right 8x8 ; alias : turtle rock
	 *		TL : Top-left 8x8
	 *		BR : Bottom-right 8x8
	 *		BL : Bottom-left 8x8
	 *		E  : Empty frame 0x0
	 * TRANSFORM is a flag determining how to flip the sprite
	 *		0  : No transform
	 *		U  : Mirror along X-axis
	 *		M  : Mirror along y-axis
	 *		UM : Mirror along both axes
	 */
	static final String[] ALLFRAMES = {
		// stand - A0:B0
		"[stand][100]A0{0,0}{F}{0}:B0{1,8}{F}{0} @ 3 ",
		// standUp - A2:C1
		"[standUp][100]A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ",
		// standDown - A1:B3
		"[standDown][100]A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ", // 
		// walk - A0:B0,A0:B1,K3:B2,K4:Q7,A0:S4,A0:R6,K3:R7,K4:S3
		"[walk][100]A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B1{1,8}{F}{0} @ 3 ;" +
			"K3{0,0}{F}{0}:B2{1,8}{F}{0} @ 3 ;" +
			"K4{0,0}{F}{0}:Q7{1,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:S4{1,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:R6{1,8}{F}{0} @ 3 ;" +
			"K3{0,0}{F}{0}:R7{1,8}{F}{0} @ 3 ;" +
			"K4{0,0}{F}{0}:S3{1,8}{F}{0} @ 3 ;",
		// walkUp - A2:B6,A2:C0,A2:S7,A2:T3,A2:T7,A2:T4,A2:T5,A2:T6
		"[walkUp][100]A2{0,0}{F}{0}:B6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C0{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:S7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T3{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T4{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T5{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T6{0,8}{F}{0} @ 3 ",
		// walkDown - A1:B4,A1:B5,A1:S5,A1:S6,A1:B4-M,A1:B5-M,A1:S5-M,A1:S6-M
		"[walkDown][100]A1{0,0}{F}{0}:B4{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:S5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B5{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S5{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{M} @ 3 ",
		// bonk - F3
		"[bonk][100]F3{0,0}{F}{0}:G3{0,16}{T}{0} @ 3 ",
		// bonkUp - F4
		"[bonkUp][100]F4{0,0}{F}{0}:G4{0,16}{T}{0} @ 3 ",
		// bonkDown - F2
		"[bonkDown][100]F2{0,0}{F}{0}:G2{0,16}{T}{0} @ 3 ",
		// swim - H5:I7,H6:J0,H5:I7,H7:J1
		"[swim][100]H5{0,0}{F}{0}:I7{0,6}{F}{0} @ 3 ;" +
			"H6{0,0}{F}{0}:J0{0,6}{F}{0} @ 3 ;" +
			"H5{0,0}{F}{0}:I7{0,6}{F}{0} @ 3 ;" +
			"H7{0,0}{F}{0}:J1{0,6}{F}{0} @ 3 ",
		// swimUp - A2:I5,E4:I6
		"[swimUp][100]A2{0,0}{F}{0}:I5{0,8}{F}{0} @ 3 ;" +
			"E4{0,0}{F}{0}:I6{0,8}{F}{0} @ 3 ",
		// swimDown - I3:J3,I4:J4
		"[swimDown][100]I3{0,0}{F}{0}:J3{0,8}{F}{0} @ 3 ;" +
			"I4{0,0}{F}{0}:J4{0,8}{F}{0} @ 3 ",
		// swimFlap - P0,J5
		"[swimFlap][100]P0{0,0}{F}{0} @ 3 ;" +
			"J5{0,0}{F}{0} @ 3 ",
		// treadingWater - A0:L0,A0:L0-M
		"[treadingWater][100]A0{0,0}{F}{0}:L0{2,3}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:L0{2,3}{F}{M} @ 3 ",
		// treadingWaterUp - A2:J2,A2:J2-M
		"[treadingWaterUp][100]A2{0,0}{F}{0}:J2{0,7}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:J2{0,7}{F}{M} @ 3 ",
		// treadingWaterDown - A1:J2,A1:J2-M
		"[treadingWaterDown][100]A1{0,0}{F}{0}:J2{0,7}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:J2{0,7}{F}{M} @ 3 ",
		// attack - A0:C2,A0:C3,A0:C4,A0:α7,Z6,A0:C4,A0:C5
		"[attack][100]A0{0,0}{F}{0}:C2{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:C3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:C4{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:α7{0,8}{F}{0} @ 3 ;" +
			"Z6{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:C4{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:C5{0,8}{F}{0} @ 3 ",
		// attackUp - F1,A2:D1,A2:D2,A2:β1,A2:D2,A2:L4
		"[attackUp][100]F1{0,0}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:D1{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:D2{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:β1{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:D2{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:L4{0,8}{F}{0} @ 3 ",
		// attackDown - F0,A1:C6,A4:D0,A4:β0,A4:D0,A3:L3
		"[attackDown][100]F0{0,0}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:C6{0,8}{F}{0} @ 3 ;" +
			"A4{0,0}{F}{0}:D0{0,8}{F}{0} @ 3 ;" +
			"A4{0,0}{F}{0}:β0{0,8}{F}{0} @ 3 ;" +
			"A4{0,0}{F}{0}:D0{0,8}{F}{0} @ 3 ;" +
			"A3{0,0}{F}{0}:L3{0,8}{F}{0} @ 3 ",
		// dashRelease - A0:M6,K3:V1,A0:M6,K4:M7
		"[dashRelease][100]A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"K3{0,1}{F}{0}:V1{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"K4{0,2}{F}{0}:M7{0,8}{F}{0} @ 3 ",
		// dashReleaseUp - A2:M3,A2:M4,A2:M5,A2:M3,A2:M4-M,A2:M5-M,A2:M3-M
		"[dashReleaseUp][100]A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:M4{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:M5{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:M4{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:M5{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:M3{0,8}{F}{M} @ 3 ",
		// dashReleaseDown - A1:M0,A1:M1,A1:M2,A1:M0,A1:β2,A1:β3,A1:M0
		"[dashReleaseDown][100]A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:M1{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:M2{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:β2{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:β3{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ",
		// spinAttack - A0:I0,A0:P1,A0-M:I0,A0:B0,A1:P3,A0-M:B0-M,A2:P2,A0:I0
		"[spinAttack][100]A0{0,0}{F}{0}:I0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:P1{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:I0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:P2{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:I0{0,8}{F}{0} @ 3 ",
		// spinAttackLeft - A0-M:I1,A1:P3,A0:B0,A2:P2,A0-M:B0-M,A0-M:I1,A0-M:I2,A0-M:I1
		"[spinAttackLeft][100]A0{0,0}{F}{M}:I1{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:P2{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A0{0,0}{F}{M}:I1{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:I2{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:I1{0,8}{F}{0} @ 3 ",
		// spinAttackUp - A2:D1-M,F1-M,A2:D1-M,A2:P2,A0:B0,A1:P3,A0-M:B0-M,A2:D1-M
		"[spinAttackUp][100]A2{0,0}{F}{0}:D1{0,8}{F}{M} @ 3 ;" +
			"F1{0,0}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:D1{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:P2{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:D1{0,8}{F}{M} @ 3 ",
		// spinAttackDown - A1:C6,F0,A1:C6,A1:P3,A0-M:B0-M,A2:P2,A0:B0,A1:C6
		"[spinAttackDown][100]A1{0,0}{F}{0}:C6{0,8}{F}{0} @ 3 ;" +
			"F0{0,0}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:C6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:P2{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:C6{0,8}{F}{0} @ 3 ",
		// dashSpinup - A0:B0,A0:B1,K3:B2,K4:Q7,A0:S4,A0:R6,K3:R7,K4:S3,A0:B0,A0:B1,K3:B2,K4:Q7,A0:S4,A0:R6,K3:R7,K4:S3,A0:B0,A0:B1,K3:B2,K4:Q7,A0:S4,A0:R6,K3:R7,K4:S3
		"[dashSpinup][100]A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B1{0,8}{F}{0} @ 3 ;" +
			"K3{0,0}{F}{0}:B2{0,8}{F}{0} @ 3 ;" +
			"K4{0,0}{F}{0}:Q7{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:S4{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:R6{0,8}{F}{0} @ 3 ;" +
			"K3{0,0}{F}{0}:R7{0,8}{F}{0} @ 3 ;" +
			"K4{0,0}{F}{0}:S3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B1{0,8}{F}{0} @ 3 ;" +
			"K3{0,0}{F}{0}:B2{0,8}{F}{0} @ 3 ;" +
			"K4{0,0}{F}{0}:Q7{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:S4{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:R6{0,8}{F}{0} @ 3 ;" +
			"K3{0,0}{F}{0}:R7{0,8}{F}{0} @ 3 ;" +
			"K4{0,0}{F}{0}:S3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B1{0,8}{F}{0} @ 3 ;" +
			"K3{0,0}{F}{0}:B2{0,8}{F}{0} @ 3 ;" +
			"K4{0,0}{F}{0}:Q7{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:S4{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:R6{0,8}{F}{0} @ 3 ;" +
			"K3{0,0}{F}{0}:R7{0,8}{F}{0} @ 3 ;" +
			"K4{0,0}{F}{0}:S3{0,8}{F}{0} @ 3 ",
		// dashSpinupUp - A2:C1,A2:B6,A2:C0,A2:S7,A2:T3,A2:T7,A2:T4,A2:T5,A2:T6,A2:B6,A2:C0,A2:S7,A2:T3,A2:T7,A2:T4,A2:T5,A2:T6,A2:B6,A2:C0,A2:S7,A2:T3,A2:T7,A2:T4,A2:T5
		"[dashSpinupUp][100]A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:B6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C0{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:S7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T3{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T4{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T5{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:B6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C0{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:S7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T3{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T4{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T5{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:B6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C0{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:S7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T3{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T4{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:T5{0,8}{F}{0} @ 3 ",
		// dashSpinupDown - A1:B3,A1:B4-M,A1:B5,A1:S5,A1:S6,A1:B4-M,A1:B5-M,A1:S5-M,A1:S6-M,A1:B4-M,A1:B5,A1:S5,A1:S6,A1:B4-M,A1:B5-M,A1:S5-M,A1:S6-M,A1:B4-M,A1:B5,A1:S5,A1:S6,A1:B4-M,A1:B5-M,A1:S5-M,A1:S6-M
		"[dashSpinupDown][100]A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:S5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B5{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S5{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:S5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B5{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S5{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:S5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B5{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S5{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{M} @ 3 ",
		// salute - A3:P4-M
		"[salute][100]A3{0,0}{F}{0}:P4{0,8}{F}{M} @ 3 ",
		// itemGet - L1:L2
		"[itemGet][100]L1{0,0}{F}{0}:L2{0,8}{F}{0} @ 3 ",
		// triforceGet - Z2:β4
		"[triforceGet][100]Z2{0,0}{F}{0}:β4{0,8}{F}{0} @ 3 ",
		// readBook - K5:K6
		"[readBook][100]K5{0,0}{F}{0}:K6{0,8}{F}{0} @ 3 ;",
		// prayer R1,A5:Q1,A5:Q0,S1
		"[prayer][1000]R1{0,0}{F}{0}:S1{0,16}{T}{0} @ 3 ;" +
			"A5{0,1}{F}{0}:Q1{0,8}{F}{0} @ 3 ;" +
			"A5{0,1}{F}{0}:Q0{0,8}{F}{0} @ 3 ;" +
			"S1{0,0}{B}{0}:T1{0,8}{F}{0} @ 3 ",
		// fall - G0,E5,E6,H4-T,H4-B,G4-B
		"[fall][100]G0{0,0}{B}{0}:G1{16,0}{BL}{0}:H0{0,8}{F}{0}:H1{16,8}{L}{0} @ 3 ;" +
			"E5{0,0}{F}{0} @ 3 ;" +
			"E6{0,0}{F}{0} @ 3 ;" +
			"H4{0,0}{TR}{0} @ 3 ;" +
			"H4{0,0}{BR}{0} @ 3 ;" +
			"G4{0,0}{BR}{0} @ 3 ;" +
			"G4{0,0}{E}{0} @ 3 ",
		// grab - A0:X2,Z3:Z4
		"[grab][100]A0{0,0}{F}{0}:X2{0,8}{F}{0} @ 3 ;" +
			"Z3{0,0}{F}{0}:Z4{0,8}{F}{0} @ 3 ",
		// grabUp - Z0:V5,Y6
		"[grabUp][100]Z0{0,0}{F}{0}:V5{0,8}{F}{0} @ 3 ;" +
			"Y6{0,0}{F}{0} @ 3 ",
		// grabDown - E3:X5,U0:P7
		"[grabDown][100]E3{0,0}{F}{0}:X5{0,8}{F}{0} @ 3 ;" +
			"U0{0,0}{F}{0}:P7{0,8}{F}{0} @ 3 ",
		// lift - E2:U5,U1:U6,L6:O2
		"[lift][100]E2{0,0}{F}{0}:U5{0,8}{F}{0} @ 3 ;" +
			"U1{0,0}{F}{0}:U6{0,8}{F}{0} @ 3 ;" +
			"L6{0,0}{F}{0}:O2{0,8}{F}{0} @ 3 ",
		// liftUp - U2:U7,A2:V0,L7:O5
		"[liftUp][100]U2{0,0}{F}{0}:U7{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:V0{0,8}{F}{0} @ 3 ;" +
			"L7{0,0}{F}{0}:O5{0,8}{F}{0} @ 3 ",
		// liftDown - U0:U4,U0:U3,L5:N7
		"[liftDown][100]U0{0,0}{F}{0}:U4{0,8}{F}{0} @ 3 ;" +
			"U0{0,0}{F}{0}:U3{0,8}{F}{0} @ 3 ;" +
			"L5{0,0}{F}{0}:N7{0,8}{F}{0} @ 3 ",
		// carry - L6:O2,L6:O3,L6:O4
		"[carry][100]L6{0,0}{F}{0}:O2{0,8}{F}{0} @ 3 ;" +
			"L6{0,0}{F}{0}:O3{0,8}{F}{0} @ 3 ;" +
			"L6{0,0}{F}{0}:O4{0,8}{F}{0} @ 3 ",
		// carryUp - L7:O5,L7:O6,L7:O7
		"[carryUp][100]L7{0,0}{F}{0}:O5{0,8}{F}{0} @ 3 ;" +
			"L7{0,0}{F}{0}:O6{0,8}{F}{0} @ 3 ;" +
			"L7{0,0}{F}{0}:O7{0,8}{F}{0} @ 3 ",
		// carryDown - L5:N7,L5:O0,L5:O1
		"[carryDown][100]L5{0,0}{F}{0}:N7{0,8}{F}{0} @ 3 ;" +
			"L5{0,0}{F}{0}:O0{0,8}{F}{0} @ 3 ;" +
			"L5{0,0}{F}{0}:O1{0,8}{F}{0} @ 3 ",
		// treePull - P7-UM:E7,N0:A1-UM,K2:K0,K1,F4,N0:A1-UM,K2:K0,K1
		"[treePull][100]P7{0,0}{F}{UM}:E7{0,8}{F}{0} @ 3 ;" +
			"N0{0,0}{F}{0}:A1{0,8}{F}{UM} @ 3 ;" +
			"K2{0,0}{F}{0}:K0{0,8}{F}{0} @ 3 ;" +
			"K1{0,0}{F}{0} @ 3 ;" +
			"F4{0,0}{F}{0} @ 3 ;" +
			"N0{0,0}{F}{0}:A1{0,8}{F}{UM} @ 3 ;" +
			"K2{0,0}{F}{0}:K0{0,8}{F}{0} @ 3 ;" +
			"K1{0,0}{F}{0} @ 3 ",
		// throw - A0:M6,A0:B0
		"[throw][100]A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ",
		// throwUp - A2:M3,A2:C1
		"[throwUp][100]A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ",
		// throwDown - A1:M0,A1:B3
		"[throwDown][100]A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ",
		// push - U1:X2,U1:X3,U1:X4,U1:X2,U1:X3,U1:X2,U1:X3,U1:X4
		"[push][100]U1{0,0}{F}{0}:X2{0,8}{F}{0} @ 3 ;" +
			"U1{0,1}{F}{0}:X3{0,8}{F}{0} @ 3 ;" +
			"U1{0,2}{F}{0}:X4{0,8}{F}{0} @ 3 ;" +
			"U1{0,0}{F}{0}:X2{0,8}{F}{0} @ 3 ;" +
			"U1{0,1}{F}{0}:X3{0,8}{F}{0} @ 3 ;" +
			"U1{0,0}{F}{0}:X2{0,8}{F}{0} @ 3 ;" +
			"U1{0,1}{F}{0}:X3{0,8}{F}{0} @ 3 ;" +
			"U1{0,2}{F}{0}:X4{0,8}{F}{0} @ 3 ",
		// pushUp - U2:M3,U2:M4,U2:M5,U2:M3,U2:M4-M,U2:M3,U2:M4,U2:M5
		"[pushUp][100]U2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"U2{0,1}{F}{0}:M4{0,8}{F}{0} @ 3 ;" +
			"U2{0,2}{F}{0}:M5{0,8}{F}{0} @ 3 ;" +
			"U2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"U2{0,1}{F}{0}:M4{0,8}{F}{M} @ 3 ;" +
			"U2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"U2{0,1}{F}{0}:M4{0,8}{F}{0} @ 3 ;" +
			"U2{0,2}{F}{0}:M5{0,8}{F}{0} @ 3 ",
		// pushDown - U0:X5,U0:X6,U0:X7,U0:X5,U0:X6-M,U0:X5,U0:X6,U0:X7
		"[pushDown][100]U0{0,3}{F}{0}:X5{0,8}{F}{0} @ 3 ;" +
			"U0{0,4}{F}{0}:X6{0,8}{F}{0} @ 3 ;" +
			"U0{0,5}{F}{0}:X7{0,8}{F}{0} @ 3 ;" +
			"U0{0,3}{F}{0}:X5{0,8}{F}{0} @ 3 ;" +
			"U0{0,4}{F}{0}:X6{0,8}{F}{M} @ 3 ;" +
			"U0{0,3}{F}{0}:X5{0,8}{F}{0} @ 3 ;" +
			"U0{0,4}{F}{0}:X6{0,8}{F}{0} @ 3 ;" +
			"U0{0,5}{F}{0}:X7{0,8}{F}{0} @ 3 ",
		// shovel - B7:D7,A0:F5,A0:C7
		"[shovel][400]B7{0,-1}{F}{0}:D7{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:F5{0,8}{F}{0} @ 3 ;" +
			"A0{-1,0}{F}{0}:C7{0,8}{F}{0} @ 3 ",
		// boomerang - S2,A0:C4,A0:B0
		"[boomerang][100]S2{0,0}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:C4{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:B0{0,8}{F}{0} @ 3 ",
		// boomerangUp - R2,A2:Q6,A2:C1
		"[boomerangUp][100]R2{0,0}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:Q6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ",
		// boomerangDown - A1:Q5,A1:D0,A1:B3
		"[boomerangDown][100]A1{0,0}{F}{0}:Q5{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:D0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ",
		// rod - G2-R,A0:C4,A0:N4
		"[rod][100]G2{0,0}{BR}{0}:G3{8,0}{BL}{0}:H2{0,8}{R}{0}:H3{8,8}{L}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:N4{0,8}{F}{0} @ 3 ",
		// rodUp - G3-R,A2:D2,A2:N5
		"[rodUp][100]G1{0,0}{BR}{0}:G2{8,0}{BL}{0}:H1{0,8}{R}{0}:H2{8,8}{L}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:D2{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:N5{0,8}{F}{0} @ 3 ",
		// rodDown - G1-R,A1:N6,A1:D0
		"[rodDown][100]G3{0,0}{BR}{0}:G4{8,0}{BL}{0}:H3{0,8}{R}{0}:H4{8,8}{L}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:N6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:D0{0,8}{F}{0} @ 3 ",
		// powder - A0:C2,A0:C3,A0:C4,A0:C5
		"[powder][100]A0{0,1}{F}{0}:C2{0,8}{F}{0} @ 3 ;" +
			"A0{0,1}{F}{0}:C3{0,8}{F}{0} @ 3 ;" +
			"A0{0,1}{F}{0}:C4{0,8}{F}{0} @ 3 ;" +
			"A0{0,1}{F}{0}:C5{0,8}{F}{0} @ 3 ",
		// powderUp - F1,A2:D1,A2:D2,A2:L4
		"[powderUp][100]F1{0,0}{F}{0}:G1{0,16}{T}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:D1{0,8}{F}{0} @ 3 ;" +
			"A2{0,-1}{F}{0}:D2{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:L4{0,8}{F}{0} @ 3 ",
		// powderDown - F0,A1:C6,A3:D0,A3:L3
		"[powderDown][100]F0{0,0}{F}{0}:G0{0,16}{T}{0} @ 3 ;" +
			"A1{0,1}{F}{0}:C6{0,8}{F}{0} @ 3 ;" +
			"A3{0,1}{F}{0}:D0{0,8}{F}{0} @ 3 ;" +
			"A3{0,1}{F}{0}:L3{0,8}{F}{0} @ 3 ",
		// cane - A0:I2-M,L1:O2,A0:C4
		"[cane][100]A0{0,0}{F}{0}:I2{0,8}{F}{M} @ 3 ;" +
			"L1{0,0}{F}{0}:O2{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:C4{0,8}{F}{0} @ 3 ",
		// caneUp - F1-M,A2:P5-M,A2:D2
		"[caneUp][100]F1{0,0}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:P5{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:D2{0,8}{F}{0} @ 3 ",
		// caneDown - F0-M,A1:P4-M,A1:D0
		"[caneDown][100]F0{0,0}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:P4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:D0{0,8}{F}{0} @ 3 ",
		// bow - A0:M6,A0:C4,A0:P6
		"[bow][100]A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:C4{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:P6{0,8}{F}{0} @ 3 ",
		// bowUp - A2:C1,A2:M4,A2:P5
		"[bowUp][100]A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:M4{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:P5{0,8}{F}{0} @ 3 ",
		// bowDown - A1:B3,A1:B4,A1:B4
		"[bowDown][100]A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B4{0,8}{F}{0} @ 3 ",
		// bombos - A1:M0,A0-M:M6-M,A2:M3,A0:M6,A1:M0,A0-M:M6-M,A2:M3,A0:M6,A1:P3,A1:P4-M,A1:P3,A1:L3,A1:D0
		"[bombos][100]A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:M6{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:M6{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P3{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:P3{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:L3{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:D0{0,8}{F}{0} @ 3 ",
		// ether - A1:M0,A0-M:M6-M,A2:M3,A0:M6,A1:M0,A0-M:M6-M,A2:M3,A0:M6,A1:P3,A7:P4-M
		"[ether][100]A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:M6{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:M6{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P3{0,8}{F}{0} @ 3 ;" +
			"A7{0,0}{F}{0}:P4{0,8}{F}{M} @ 3 ",
		// quake - A1:M0,A0-M:M6-M,A2:M3,A0:M6,A1:M0,A0-M:M6-M,A2:M3,A0:M6,A1:P3,A1:P4-M,L5:N7,A1:Q1
		"[quake][100]A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:M6{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:M0{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{M}:M6{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:M3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:M6{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P3{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:P4{0,8}{F}{M} @ 3 ;" +
			"L5{0,0}{F}{0}:N7{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:Q1{0,8}{F}{0} @ 3 ",
		// hookshot - A0:C4
		"[hookshot][100]A0{0,0}{F}{0}:C4{0,8}{F}{0} @ 3 ",
		// hookshotUp - A2:D2
		"[hookshotUp][100]A2{0,0}{F}{0}:D2{0,8}{F}{0} @ 3 ",
		// hookshotDown - A1:D0
		"[hookshotDown][100]A1{0,0}{F}{0}:D0{0,8}{F}{0} @ 3 ",
		// zap - R0,S0
		"[zap][100]R0{0,0}{F}{0}:S0{0,16}{T}{0} @ 3 ;" +
			"S0{0,0}{B}{0}:T0{0,8}{F}{0} @ 3 ",
		// bunnyStand - α4:α5
		"[bunnyStand][100]α4{0,0}{F}{0}:α5{0,8}{F}{0} @ 3 ",
		// bunnyStandUp - α1:α2
		"[bunnyStandUp][100]α1{0,0}{F}{0}:α2{0,8}{F}{0} @ 3 ",
		// bunnyStandDown - Z5:α0
		"[bunnyStandDown][100]Z5{0,0}{F}{0}:α0{0,8}{F}{0} @ 3 ",
		// bunnyWalk - α4:α5,α4:α6
		"[bunnyWalk][100]α4{0,0}{F}{0}:α5{0,8}{F}{0} @ 3 ;" +
			"α4{0,0}{F}{0}:α6{0,8}{F}{0} @ 3 ",
		// bunnyWalkUp - α1:α2,α1:α3
		"[bunnyWalkUp][100]α1{0,0}{F}{0}:α2{0,8}{F}{0} @ 3 ;" +
			"α1{0,0}{F}{0}:α3{0,8}{F}{0} @ 3 ",
		// bunnyWalkDown - Z5:α0,Z5:Z7
		"[bunnyWalkDown][100]Z5{0,0}{F}{0}:α0{0,8}{F}{0} @ 3 ;" +
			"Z5{0,1}{F}{0}:Z7{0,8}{F}{0} @ 3 ",
		// walkUpstairs1F - A2:V5,A2:V6,A2:C1,X1:Y3,X1:Y4,X1:Y5,X1:Y3,X1:Y4,X1:Y5,X1:Y3,X1:Y4,A0:V2,A0:B0,A0:V2,A0:B0,A0:V2,A0:B0,A0:V2,A0:B0
		"[walkUpstairs1F][100]A2{0,1}{F}{0}:V5{0,8}{F}{0} @ 3 ;" +
			"A2{0,2}{F}{0}:V6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"X1{0,-1}{F}{0}:Y3{0,8}{F}{0} @ 3 ;" +
			"X1{0,0}{F}{0}:Y4{0,8}{F}{0} @ 3 ;" +
			"X1{0,1}{F}{0}:Y5{0,8}{F}{0} @ 3 ;" +
			"X1{0,-1}{F}{0}:Y3{0,8}{F}{0} @ 3 ;" +
			"X1{0,0}{F}{0}:Y4{0,8}{F}{0} @ 3 ;" +
			"X1{0,1}{F}{0}:Y5{0,8}{F}{0} @ 3 ;" +
			"X1{0,-1}{F}{0}:Y3{0,8}{F}{0} @ 3 ;" +
			"X1{0,0}{F}{0}:Y4{0,8}{F}{0} @ 3 ;" +
			"A0{0,2}{F}{0}:V2{0,8}{F}{0} @ 3 ;" +
			"A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A0{0,2}{F}{0}:V2{0,8}{F}{0} @ 3 ;" +
			"A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A0{0,2}{F}{0}:V2{0,8}{F}{0} @ 3 ;" +
			"A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A0{0,2}{F}{0}:V2{0,8}{F}{0} @ 3 ;" +
			"A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ",
		// walkDownstairs1F - A0-M:V2-M,A0-M:B0-M,A0-M:V1-M,B7-M:Y1-M,B7-M:Y2-M,B7-M:Y0-M,B7-M:Y1-M,B7-M:Y2-M,B7-M:Y0-M,B7-M:Y1-M,A1:V3,A1:V4,A1:B3,A1:V3-M,A1:V4-M,A1:B3,A1:V3,A1:V4,A1:B3,A1:V3-M,A1:S6,A1:B3
		"[walkDownstairs1F][100]A0{0,2}{F}{M}:V2{0,8}{F}{M} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A0{0,1}{F}{M}:V1{0,8}{F}{M} @ 3 ;" +
			"B7{0,0}{F}{M}:Y1{0,8}{F}{M} @ 3 ;" +
			"B7{0,1}{F}{M}:Y2{0,8}{F}{M} @ 3 ;" +
			"B7{0,-1}{F}{M}:Y0{0,8}{F}{M} @ 3 ;" +
			"B7{0,0}{F}{M}:Y1{0,8}{F}{M} @ 3 ;" +
			"B7{0,1}{F}{M}:Y2{0,8}{F}{M} @ 3 ;" +
			"B7{0,-1}{F}{M}:Y0{0,8}{F}{M} @ 3 ;" +
			"B7{0,0}{F}{M}:Y1{0,8}{F}{M} @ 3 ;" +
			"A1{0,1}{F}{0}:V3{0,8}{F}{0} @ 3 ;" +
			"A1{0,2}{F}{0}:V4{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A1{0,1}{F}{0}:V3{0,8}{F}{M} @ 3 ;" +
			"A1{0,2}{F}{0}:V4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A1{0,1}{F}{0}:V3{0,8}{F}{0} @ 3 ;" +
			"A1{0,2}{F}{0}:V4{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A1{0,1}{F}{0}:V3{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:S6{0,8}{F}{0} @ 3 ;" +
			"A1{0,1}{F}{0}:B3{0,9}{F}{0} @ 3 ",
		// walkUpstairs2F - A0:B0,A0:V1,B7:Y1,B7:Y2,B7:Y0,B7:Y1,B7:Y2,B7:Y0,B7:Y1,B7:Y2,A1:B3,A1:V3-M,A1:V4-M,A1:B3,A1:V3,A1:V4,A1:B3
		"[walkUpstairs2F][100]A0{0,0}{F}{0}:B0{1,8}{F}{0} @ 3 ;" +
			"A0{0,1}{F}{0}:V1{0,8}{F}{0} @ 3 ;" +
			"B7{0,0}{F}{0}:Y1{0,8}{F}{0} @ 3 ;" +
			"B7{0,1}{F}{0}:Y2{0,8}{F}{0} @ 3 ;" +
			"B7{0,-1}{F}{0}:Y0{0,8}{F}{0} @ 3 ;" +
			"B7{0,0}{F}{0}:Y1{0,8}{F}{0} @ 3 ;" +
			"B7{0,1}{F}{0}:Y2{0,8}{F}{0} @ 3 ;" +
			"B7{0,-1}{F}{0}:Y0{0,8}{F}{0} @ 3 ;" +
			"B7{0,0}{F}{0}:Y1{0,8}{F}{0} @ 3 ;" +
			"B7{0,1}{F}{0}:Y2{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A1{0,1}{F}{0}:V3{0,8}{F}{M} @ 3 ;" +
			"A1{0,2}{F}{0}:V4{0,8}{F}{M} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A1{0,1}{F}{0}:V3{0,8}{F}{0} @ 3 ;" +
			"A1{0,2}{F}{0}:V4{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ",
		// walkDownstairs2F - A2:C1,A2:V5,A2:V6,A2:C1,A2:D4,A2:M5-M,A2:C1,A2:V5,X1-M:Y4-M,X1-M:Y5-M,X1-M:Y3-M,X1-M:Y4-M,X1-M:Y5-M,X1-M:Y3-M,X1-M:Y4-M,X1-M:Y5-M,X1-M:Y3-M,A0-M:B0-M,A0-M:V1-M,A0-M:V2-M,A0-M:B0-M,A0-M:V1-M,A0-M:V2-M,A0-M:B0-M,A0-M:V1-M,A0-M:V2-M,A0-M:B0-M,A0-M:V1-M,A0-M:V2-M,A0-M:B0-M
		"[walkDownstairs2F][100]A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A2{0,1}{F}{0}:V5{0,8}{F}{0} @ 3 ;" +
			"A2{0,2}{F}{0}:V6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A2{0,1}{F}{0}:D4{0,8}{F}{0} @ 3 ;" +
			"A2{0,2}{F}{0}:M5{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A2{0,1}{F}{0}:V5{0,8}{F}{0} @ 3 ;" +
			"X1{0,0}{F}{M}:Y4{0,8}{F}{M} @ 3 ;" +
			"X1{0,1}{F}{M}:Y5{0,8}{F}{M} @ 3 ;" +
			"X1{0,-1}{F}{M}:Y3{0,8}{F}{M} @ 3 ;" +
			"X1{0,0}{F}{M}:Y4{0,8}{F}{M} @ 3 ;" +
			"X1{0,1}{F}{M}:Y5{0,8}{F}{M} @ 3 ;" +
			"X1{0,-1}{F}{M}:Y3{0,8}{F}{M} @ 3 ;" +
			"X1{0,0}{F}{M}:Y4{0,8}{F}{M} @ 3 ;" +
			"X1{0,1}{F}{M}:Y5{0,8}{F}{M} @ 3 ;" +
			"X1{0,-1}{F}{M}:Y3{0,8}{F}{M} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A0{0,1}{F}{M}:V1{0,8}{F}{M} @ 3 ;" +
			"A0{0,2}{F}{M}:V2{0,8}{F}{M} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A0{0,1}{F}{M}:V1{0,8}{F}{M} @ 3 ;" +
			"A0{0,2}{F}{M}:V2{0,8}{F}{M} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A0{0,1}{F}{M}:V1{0,8}{F}{M} @ 3 ;" +
			"A0{0,2}{F}{M}:V2{0,8}{F}{M} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A0{0,1}{F}{M}:V1{0,8}{F}{M} @ 3 ;" +
			"A0{0,2}{F}{M}:V2{0,8}{F}{M} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ",
		// deathSpin - A1:B3,A0-M:B0-M,A2:C1,A0:B0,A1:B3,A0-M:B0-M,A2:C1,A0:B0,A1:B3,A0-M:B0-M,A2:C1,A0:B0,E2:J6,J7
		"[deathSpin][100]A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A0{1,0}{F}{M}:B0{0,8}{F}{M} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"E2{2,0}{F}{0}:J5{0,8}{R}{0}:J6{8,8}{L}{0} @ 3 ;" +
			"J6{8,8}{R}{0}:J7{16,8}{F}{0} @ 3 ",
		// deathSplat - E2:J6,J7
		"[deathSplat][100]E2{2,0}{F}{0}:J5{0,8}{R}{0}:J6{8,8}{L}{0} @ 3 ;" +
			"J6{8,8}{R}{0}:J7{16,8}{F}{0} @ 3 ",
		// poke - A0:N3,A0:N2,A0:F6
		"[poke][100]A0{0,0}{F}{0}:N3{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:N2{0,8}{F}{0} @ 3 ;" +
			"A0{0,0}{F}{0}:F6{0,8}{F}{0} @ 3 ",
		// pokeUp - E4:D2,E4:F7,E4:L4
		"[pokeUp][100]E4{0,0}{F}{0}:D2{0,8}{F}{0} @ 3 ;" +
			"E4{0,1}{F}{0}:F7{0,8}{F}{0} @ 3 ;" +
			"E4{0,1}{F}{0}:L4{0,8}{F}{0} @ 3 ",
		// pokeDown - A1:N1,E3:G7
		"[pokeDown][100]A1{0,1}{F}{0}:N1{0,8}{F}{0} @ 3 ;" +
			"E3{0,2}{F}{0}:G7{0,8}{F}{0} @ 3 ",
		// tallGrass - A0:B0,A0:V1,A0:V2
		"[tallGrass][100]A0{-1,0}{F}{0}:B0{0,8}{F}{0} @ 3 ;" +
			"A0{-1,1}{F}{0}:V1{0,8}{F}{0} @ 3 ;" +
			"A0{-1,2}{F}{0}:V2{0,8}{F}{0} @ 3 ",
		// tallGrassUp - A2:V5,A2:V6,A2:C1,A2:D4,A2:M5-M
		"[tallGrassUp][100]A2{0,1}{F}{0}:V5{0,8}{F}{0} @ 3 ;" +
			"A2{0,2}{F}{0}:V6{0,8}{F}{0} @ 3 ;" +
			"A2{0,0}{F}{0}:C1{0,8}{F}{0} @ 3 ;" +
			"A2{0,1}{F}{0}:D4{0,8}{F}{0} @ 3 ;" +
			"A2{0,2}{F}{0}:M5{0,8}{F}{M} @ 3 ",
		// tallGrassDown - A1:B3,A1:V3,A1:V4
		"[tallGrassDown][100]A1{0,0}{F}{0}:B3{0,8}{F}{0} @ 3 ;" +
			"A1{0,1}{F}{0}:V3{0,8}{F}{0} @ 3 ;" +
			"A1{0,2}{F}{0}:V4{0,8}{F}{0} @ 3 ",
		// mapDungeon - K7
		"[mapDungeon][100]K7{0,0}{F}{0} @ 3 ",
		// mapWorld - Y7
		"[mapWorld][500]Y7{0,0}{F}{0};Y7{0,0}{E}{0} @ 3 ",
		// sleep - A6:D3
		"[sleep][100]A6{0,0}{F}{0}:D3{0,5}{F}{0} @ 3 ",
		// awake - E3:D3
		"[awake][100]E3{0,0}{F}{0}:D3{0,7}{F}{0} @ 3 "
	};

	/*
	 * GUI stuff
	 */
	private static final JComboBox<String> animOptions =
			new JComboBox<String>(getAnimNames());

	public static String[] getAnimNames() {
		String[] ret = new String[ALLFRAMES.length];
		for (int i = 0; i < ALLFRAMES.length; i++) {
			String r = ALLFRAMES[i];
			String[] animDataX = r.split("[\\[\\]]");
			ret[i] = animDataX[1];
		}
		return ret;
	}
	static final String[] MODES = {
			"Normal play",
			"Step-by-step",
			"All frames"
	};

	private static final JComboBox<String> modeOptions = new JComboBox<String>(MODES);
	/*
	 * Image controller
	 */
	private BufferedImage img = null; // sprite sheet
	private int anime; // animation id
	private int speed; // speed; 0 = normal; positive = faster; negative = slower
	private int mode; // animation mode
	private int frame;
	private int maxFrame;
	private boolean running;
	private int zoom = 3;
	private Anime[] frames = null;
	private Timer tick;
	private static final int MAXSPEED = 3; // maximum speed magnitude
	private static final int MAXZOOM = 10;
	// default initialization
	public SpriteAnimator() {
		anime = 0;
		speed = 0;
		mode = 0;
		frame = 0;
		maxFrame = 0;
		running = true;
		tick = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isRunning())
					step();
			}
		});
	}

	public void stop() {
		running = false;
		tick.stop();
	}
	
	public int getFrame() {
		return frame;
	}
	
	public String frameDis() {
		return "" + (frame + 1);
	}
	public int maxFrame() {
		return maxFrame;
	}
	/**
	 * Set image to animate.
	 * @param image
	 */
	public void setImage(BufferedImage image) {
		img = image;
	}

	/**
	 * Set animation ID.
	 * @param id
	 */
	public void setAnimation(int id) {
		if (img == null)
			return;
		anime = id;
		makeAnimationFrames();
		reset();
	}
	
	/**
	 * Get animation mode ID#.
	 */
	public int getMode() {
		return mode;
	}
	/**
	 * Set image mode and reset.
	 * <ul style="list-style:none">
	 * <li><b>0</b> - normal animation</li>
	 * <li><b>1</b> - step-by-step</li>
	 * <li><b>2</b> - all frames</li>
	 * </ul>
	 * @param m - mode 
	 */
	public void setMode(int m) {
		mode = m;
		reset();
		repaint();
	}
	
	/**
	 * Step forward 1 animation frame.
	 * Resets frame to 0 if we reach the end in modes that loop.
	 * Stops running if we reach the end of the animation in "All frames" mode.
	 * @return Frame # painted
	 */
	public void step() {
		frame++;
		if (frame >= maxFrame) {
			frame = 0;
			if (mode == 2)
				setRunning(false);
		}
		repaint();
	}

	/**
	 * Reset based on mode.
	 */
	public void reset() {
		switch (mode) {
			case 0 :
				resetFrame();
				resetSpeed();
				setRunning(true);
				break;
			case 1 :
				resetFrame();
				setRunning(false);
				break;
			case 2 :
				resetFrame();
				resetSpeed();
				setRunning(true);
				break;
		}
		adjustTimer();
	}
	
	/**
	 * Reset speed to 0.
	 */
	public void resetSpeed() {
		speed = 0;
	}
	
	/**
	 * Resets frame to 0.
	 */
	public void resetFrame() {
		frame = 0;
		repaint();
	}
	
	/**
	 * Control self-animation permission.
	 */
	public void setRunning(boolean r) {
		running = r;
		if (r)
			tick.start();
		else
			tick.stop();
	}
	
	/**
	 * @return <b>true</b> if active.
	 */
	public boolean isRunning() {
		return running;
	}
	/**
	 * @return Timer object
	 */
	public Timer getTimer() {
		return tick;
	}
	/**
	 * Increments step speed by 1.
	 * @return <b>true</b> if speed reaches max.
	 */
	public boolean faster() {
		if (speed < MAXSPEED)
			speed++;
		adjustTimer();
		return atMaxSpeed();
	}
	
	/**
	 * Decrements step speed by 1.
	 * @return <b>true</b> if speed reaches min.
	 */
	public boolean slower() {
		if (speed > (MAXSPEED * -1))
			speed--;
		adjustTimer();
		return atMinSpeed();
	}
	
	/**
	 * Zooms in by 1x.
	 * @return <b>true</b> if we're really big.
	 */
	public boolean embiggen() {
		if (zoom < MAXZOOM)
			zoom++;
		repaint();
		return (zoom >= MAXZOOM);
	}
	
	/**
	 * Zooms out by 1x.
	 * @return <b>true</b> if we're vanilla size.
	 */
	public boolean ensmallen() {
		if (zoom > 1)
			zoom--;
		repaint();
		return (zoom <= 1);
	}
	/**
	 * Adjusts timer based on speed
	 */
	public void adjustTimer() {
		double speedM = Math.pow(1.5, speed * -1);
		frames[frame].setNextTick(tick, speedM);
	}
	/**
	 * Compares current step speed to maximum speed allowed.
	 */
	public boolean atMaxSpeed() {
		return speed == MAXSPEED;
	}
	/**
	 * Compares current step speed to minimum speed allowed.
	 */
	public boolean atMinSpeed() {
		return speed == (-1 * MAXSPEED);
	}

	/**
	 * Draw every sprite
	 */
	public void paint(Graphics g) {
		if (frames==null || frames[frame] == null)
			return;
		Graphics2D g2 = (Graphics2D) g;
		g2.scale(zoom, zoom);
		Anime t = frames[frame];
		t.draw(g2);
		adjustTimer();
	}

	// @link Sprite - lol get it?
	/**
	 * Makes an array of {@link Sprite}s based on the frame data.
	 */
	public void makeAnimationFrames() {
		if (img == null)
			return;
		String animData = ALLFRAMES[anime].toUpperCase().replace(" ", ""); // CAPS and remove all whitespace
		// split into sections

		String[] animDataX = animData.split("[\\[\\]]+");
		String[] eachFrame = animDataX[3].split(";"); // split by frame
		// get duration

		maxFrame = eachFrame.length;
		frames = new Anime[maxFrame];
		// each frame
		for (int i = 0; i < maxFrame; i++) {
			String[] wholeFrame = eachFrame[i].split("@");
			int animSpeed;
			try {
				animSpeed = Integer.parseInt(wholeFrame[1]);
			} catch (Exception e) {
				animSpeed = 100;
			}
			String[] eachSprite = wholeFrame[0].split(":");
			int spriteCount = eachSprite.length;
			// each sprite in frame
			Sprite[] sprList = new Sprite[spriteCount];
			frames[i] = new Anime(sprList, animSpeed);
			for (int j = 0; j < spriteCount; j++) {
				// split into info sections
				String[] spriteSplit = eachSprite[j].split("[\\{\\}]{1,2}");
				char[] sprIndex = spriteSplit[0].toCharArray();
				String[] pos = spriteSplit[1].split(",");
				String sprSize = spriteSplit[2];
				String sprTrans = spriteSplit[3];
				// sprite position
				int xpos = Integer.parseInt(pos[0]);
				int ypos = Integer.parseInt(pos[1]);
				int drawY = ALPHA.indexOf(sprIndex[0]) * 16;
				int drawX = Integer.parseInt((sprIndex[1] + "")) * 16;
				int drawYoffset, drawXoffset, width, height;
				
				// determine offset from initial position
				switch (sprSize) {
					case "F" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 16;
						height = 16;
						break;
					case "T" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 16;
						height = 8;
						break;
					case "B" :
						drawYoffset = 8;
						drawXoffset = 0;
						width = 16;
						height = 8;
						break;
					case "R" :
						drawYoffset = 0;
						drawXoffset = 8;
						width = 8;
						height = 16;
						break;
					case "L" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 8;
						height = 16;
						break;
					case "TR" :
						drawYoffset = 0;
						drawXoffset = 8;
						width = 8;
						height = 8;
						break;
					case "TL" :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 8;
						height = 8;
						break;
					case "BR" :
						drawYoffset = 8;
						drawXoffset = 8;
						width = 8;
						height = 8;
						break;
					case "BL" :
						drawYoffset = 8;
						drawXoffset = 0;
						width = 8;
						height = 8;
						break;
					default :
						drawYoffset = 0;
						drawXoffset = 0;
						width = 16;
						height = 16;
						break;
				}
				drawX += drawXoffset;
				drawY += drawYoffset;
				BufferedImage spreet;
				// blank sprite frame
				if (sprSize.equals("E"))
					spreet = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR_PRE);
				else
					spreet = img.getSubimage(drawX, drawY, width, height);
				
				// transformations
				switch (sprTrans) {
					case "M" :
						spreet = flipH(spreet);
						break;
					case "U" :
						spreet = flipV(spreet);
						break;
					case "UM" :
						spreet = flipH(spreet);
						spreet = flipV(spreet);
						break;
					default :
						// nothing
						break;
				}
				// put it in backwards to preserve draw order
				sprList[spriteCount-1-j] = new Sprite(spreet, xpos, ypos);
			}
		}
	}


	// error controller
	static final SpriteAnimator controller = new SpriteAnimator();
	
	public static void main(String[] args) throws IOException {
		//try to set Nimbus
		try {
			NimbusLookAndFeel lookAndFeel = new NimbusLookAndFeel();
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (UnsupportedLookAndFeelException e) {
			// try to set System default
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (UnsupportedLookAndFeelException
					| ClassNotFoundException
					| InstantiationException
					| IllegalAccessException e2) {
					// do nothing
			} //end System
		} // end Nimbus
		final JFrame frame = new JFrame("Sprite Animator");
		final Dimension d = new Dimension(400,282);
		final JTextField fileName = new JTextField("");
		final JButton loadBtn = new JButton("Load file");
		final JButton stepBtn = new JButton("Step");
		final JButton fasterBtn = new JButton("Speed+");
		final JButton slowerBtn = new JButton("Speed-");
		final JButton bigBtn = new JButton("Zoom+");
		final JButton lilBtn = new JButton("Zoom-");
		final JButton resetBtn = new JButton("Reset");
		final JLabel frameCur = new JLabel("1");
		final JLabel frameMax = new JLabel("/ 1");
		final JPanel loadWrap = new JPanel(new BorderLayout());
		final JPanel controls = new JPanel(new GridBagLayout());
		final JPanel controlsWrap = new JPanel(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		// row 1 and 2 : comboboxes
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		controls.add(animOptions,c);
		c.gridy++;
		controls.add(modeOptions,c);

		// row 3 : zoom
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		controls.add(lilBtn,c);
		c.gridx = 1;
		controls.add(new JPanel(), c); // filler
		c.gridx = 2;
		controls.add(bigBtn,c);
		
		// speed
		c.gridx = 0;
		c.gridy++;
		controls.add(slowerBtn,c);
		c.gridx = 1;
		controls.add(new JPanel(), c); // filler
		c.gridx = 2;
		controls.add(fasterBtn,c);
		
		// step
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy++;
		controls.add(stepBtn,c);
		
		// reset
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy++;
		controls.add(resetBtn,c);
		
		// frame counter
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy++;
		c.weightx = 9;
		controls.add(new JLabel("Frame:"),c);
		c.weightx = 0;
		c.gridx = 1;
		c.fill = GridBagConstraints.NONE;
		
		Dimension dd = new Dimension(20,20);
		frameCur.setPreferredSize(dd);
		frameCur.setMaximumSize(dd);
		frameCur.setMinimumSize(dd);

		controls.add(frameCur,c);
		c.gridx = 2;
		controls.add(frameMax,c);
		
		final JPanel bottomStuffWrap = new JPanel(new BorderLayout());
		final JPanel bottomStuff = new JPanel(new BorderLayout());
		stepBtn.setEnabled(false);

		final SpriteAnimator imageArea = new SpriteAnimator();
		final SpriteAnimator run = imageArea; // just a shorter name

		bottomStuffWrap.add(imageArea,BorderLayout.CENTER);
		bottomStuffWrap.add(bottomStuff,BorderLayout.EAST);
		loadWrap.add(loadBtn,BorderLayout.EAST);
		loadWrap.add(fileName,BorderLayout.CENTER);

		// Credits
		final JFrame aboutFrame = new JFrame("About");
		final JMenuItem peeps = new JMenuItem("About");
		final TextArea peepsList = new TextArea("", 0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
		peepsList.setEditable(false);
		peepsList.append("Written by fatmanspanda"); // hey, that's me
		peepsList.append("\n\nFrame resources:\n");
		peepsList.append("http://alttp.mymm1.com/sprites/includes/animations.txt\n");
		peepsList.append(join(new String[]{
				"\tMikeTrethewey", // it's mike
				"TWRoxas", // provided most valuable documentation
				}, ", "));// forced me to do this and falls in every category
		peepsList.append("\n\nCode contribution:\n");
		peepsList.append(join(new String[]{
				"Zarby89", // spr conversion
				}, ", "));
		peepsList.append("\n\nResources and development:\n");
		peepsList.append(join(new String[]{
				"Veetorp", // provided most valuable documentation
				"Zarby89", // various documentation and answers
				"Sosuke3" // various snes code answers
				}, ", "));
		peepsList.append("\n\nTesting and feedback:\n");
		peepsList.append(join(new String[]{
				"Jighart",
				}, ", "));
		aboutFrame.add(peepsList);
		final JMenuBar menu = new JMenuBar();
		menu.add(peeps);
		peeps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				aboutFrame.setVisible(true);
			}});
		aboutFrame.setSize(600,300);
		aboutFrame.setLocation(150,150);
		aboutFrame.setResizable(false);
		// end credits

		frame.add(bottomStuffWrap, BorderLayout.CENTER);
		controlsWrap.add(controls,BorderLayout.NORTH);
		frame.add(controlsWrap,BorderLayout.EAST);
		frame.add(loadWrap,BorderLayout.NORTH);
		frame.setSize(d);
		frame.setMinimumSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLocation(300,300);
		frame.setJMenuBar(menu);
		
		// file explorer
		final JFileChooser explorer = new JFileChooser();
		FileNameExtensionFilter sprFilter =
				new FileNameExtensionFilter("Sprite files", new String[] { "spr" });
		// can't clear text due to wonky code
		// have to set a blank file instead
		final File EEE = new File("");
		// TODO: uncomment this for exports
		//explorer.setCurrentDirectory(new File(".")); // quick way to set to current .jar loc
		Timer tock = run.getTimer();
		tock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frameCur.setText(run.frameDis());
			}
		});
		// load sprite file
		loadBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				explorer.setSelectedFile(EEE);
				explorer.setFileFilter(sprFilter);
				int option = explorer.showOpenDialog(loadBtn);
				if (option == JFileChooser.CANCEL_OPTION)
					return;
				String n = "";
				try {
					n = explorer.getSelectedFile().getPath();
				} catch (NullPointerException e) {
					// do nothing
				} finally {
					if (testFileType(n,"spr"))
						fileName.setText(n);
					else
						return;
				}
				explorer.removeChoosableFileFilter(sprFilter);

				byte[] sprite;
				try {
					sprite = readSprite(fileName.getText());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(frame,
							"Error reading sprite",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					byte[][][] ebe = sprTo8x8(sprite);
					byte[][] palette = getPal(sprite);
					byte[] src = makeRaster(ebe,palette);
					
					run.setImage(makeSheet(src));
				} catch(Exception e) {
					JOptionPane.showMessageDialog(frame,
							"Error converting sprite",
							"Oops",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					run.setAnimation(0);
					animOptions.setSelectedIndex(0);
				} catch(Exception e) {
						
				}
			}});
		
		// 
		animOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					run.setAnimation(animOptions.getSelectedIndex());
				} catch(Exception e) {
					run.setAnimation(0);
					animOptions.setSelectedIndex(0);
					JOptionPane.showMessageDialog(frame,
							"This animation caused a problem.",
							"OH NO",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				run.reset();
				resetBtn.getActionListeners()[0].actionPerformed(
						new ActionEvent(resetBtn, ActionEvent.ACTION_PERFORMED,"",0,0));
				frameMax.setText("/ " + run.maxFrame());
			}});
		
		modeOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.setMode(modeOptions.getSelectedIndex());
				int animMode = run.getMode();
				// button disabling
				switch(animMode) {
					case 0 :
						fasterBtn.setEnabled(true);
						slowerBtn.setEnabled(true);
						resetBtn.setEnabled(true);
						stepBtn.setEnabled(false);
						break;
					case 1 :
						fasterBtn.setEnabled(false);
						slowerBtn.setEnabled(false);
						resetBtn.setEnabled(true);
						stepBtn.setEnabled(true);
						break;
					case 2 :
						fasterBtn.setEnabled(false);
						slowerBtn.setEnabled(false);
						resetBtn.setEnabled(true);
						stepBtn.setEnabled(false);
						break;
				}
				run.reset();
			}});
		
		bigBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lilBtn.setEnabled(true);
				if (run.embiggen())
					bigBtn.setEnabled(false);
			}});
		
		lilBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bigBtn.setEnabled(true);
				if (run.ensmallen())
					lilBtn.setEnabled(false);
			}});
		
		fasterBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				slowerBtn.setEnabled(true);
				if (run.faster())
					fasterBtn.setEnabled(false);
			}});
		
		slowerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fasterBtn.setEnabled(true);
				if (run.slower())
					slowerBtn.setEnabled(false);
			}});
		
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int animMode = run.getMode();
				run.repaint();
				run.reset();
				// button disabling
				switch (animMode) {
					case 0 :
						fasterBtn.setEnabled(true);
						slowerBtn.setEnabled(true);
						break;
					case 1 :
						// nothing
						break;
					case 2 :
						// nothing
						break;
				}
				frameCur.setText(run.frameDis());
			}});
		
		stepBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				run.step();
				frameCur.setText(run.frameDis());
			}});
		// turn on
		frame.setVisible(true);
	}
	/**
	 * Reads a sprite file
	 * @throws IOException
	 */
	public static byte[] readSprite(String path) throws IOException {
		File file = new File(path);
		byte[] ret = new byte[(int) file.length()];
		FileInputStream s;
		try {
			s = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw e;
		}
		try {
			s.read(ret);
			s.close();
		} catch (IOException e) {
			throw e;
		}

		return ret;
	}

	/**
	 * Takes a sprite and turns it into 896 blocks of 8x8 pixels
	 * @param sprite
	 */
	public static byte[][][] sprTo8x8(byte[] sprite) {
		byte[][][] ret = new byte[896][8][8];

		// current block we're working on, each sized 32
		// start at -1 since we're incrementing at 0mod32
		int b = -1;
		// locate where in interlacing map we're reading from
		int g;
		for (int i = 0; i < SPRITESIZE; i++) {
			// find interlacing index
			g = i%32;
			// increment at 0th index
			if (g == 0)
				b++;
			// row to look at
			int r = BPPI[g][0];
			// bit plane of byte
			int p = BPPI[g][1];

			// byte to unravel
			byte q = sprite[i];

			// run through the byte
			for (int c = 0; c < 8; c++) {
				// AND with 1 shifted to the correct plane
				boolean bitOn = (q & (1 << (7-c))) != 0;
				// if true, OR with that plane in index map
				if (bitOn)
					ret[b][r][c] |= (1 << (p));
			}
		}
		return ret;
	}

	/**
	 * Splits a palette into RGB arrays.
	 * Only uses the first 16 colors.
	 * Automatically makes first index black.
	 */
	public static byte[][] getPal(byte[] sprite) {
		byte[][] ret = new byte[16][3];
		for (int i = 1; i < 16; i++) {
			short color = 0;
			int pos = SPRITESIZE + (i * 2) - 2;
			color = (short) unsignByte(sprite[pos+1]);
			color <<= 8;
			color |= (short) unsignByte(sprite[pos]);
			
			ret[i][0] = (byte) (((color >> 0) & 0x1F) << 3);
			ret[i][1] = (byte) (((color >> 5) & 0x1F) << 3);
			ret[i][2] = (byte) (((color >> 10) & 0x1F) << 3);
		}

		// make black;
		// separate operation just in case I don't wanna change pal's values
		ret[0][0] = 0;
		ret[0][1] = 0;
		ret[0][2] = 0;

		return ret;
	}

	/**
	 * Turn index map in 8x8 format into an array of ABGR values
	 */
	public static byte[] makeRaster(byte[][][] ebe, byte[][] palette) {
		byte[] ret = new byte[RASTERSIZE];
		int largeCol = 0;
		int intRow = 0;
		int intCol = 0;
		int index = 0;
		byte[] color;
		// read image
		for (int i = 0; i < RASTERSIZE / 4; i++) {
			// get pixel color index
			byte coli = ebe[index][intRow][intCol];
			// get palette color
			color = palette[coli];
			// index 0 = trans
			if (coli == 0)
				ret[i*4] = 0;
			else
				ret[i*4] = (byte) 255;

			// BGR
			ret[i*4+1] = color[2];
			ret[i*4+2] = color[1];
			ret[i*4+3] = color[0];

			// count up square by square
			// at 8, reset the "Interior column" which we use to locate the pixel in 8x8
			// increments the "Large column", which is the index of the 8x8 sprite on the sheet
			// at 16, reset the index and move to the next row
			// (so we can wrap around back to our old 8x8)
			// after 8 rows, undo the index reset, and move on to the next super row
			intCol++;
			if (intCol == 8) {
				index++;
				largeCol++;
				intCol = 0;
				if (largeCol == 16) {
					index -= 16;
					largeCol = 0;
					intRow++;
					if (intRow == 8) {
						index += 16;
						intRow = 0;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Turns a 4 byte raster {A,B,G,R} into an integer array and sets the image.
	 * @param raster
	 * @return
	 */
	public static BufferedImage makeSheet(byte[] raster) {
		BufferedImage image = new BufferedImage(128, 448, BufferedImage.TYPE_4BYTE_ABGR_PRE);
		int[] rgb = new int[128 * 448];
		for (int i = 0, j = 0; i < rgb.length; i++) {
			int a = raster[j++] & 0xff;
			int b = raster[j++] & 0xff;
			int g = raster[j++] & 0xff;
			int r = raster[j++] & 0xff;
			rgb[i] = (a << 24) | (r << 16) | (g << 8) | b;
		}
		image.setRGB(0, 0, 128, 448, rgb, 0, 128);
		
		return image;
	}

	/*
	 * GUI related functions
	 */
	/**
	 * gives file extension name from a string
	 * @param s - test case
	 * @return extension type
	 */
	public static String getFileType(String s) {
		String ret = s.substring(s.lastIndexOf(".") + 1);
		return ret;
	}

	/**
	 * Test a file against multiple extensions.
	 * The way <b>getFileType</b> works should allow
	 * both full paths and lone file types to work.
	 * 
	 * @param s - file name or extension
	 * @param type - list of all extensions to test against
	 * @return <tt>true</tt> if any extension is matched
	 */
	public static boolean testFileType(String s, String[] type) {
		boolean ret = false;
		String filesType = getFileType(s);
		for (String t : type) {
			if (filesType.equalsIgnoreCase(t)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * Test a file against a single extension.
	 * 
	 * @param s - file name or extension
	 * @param type - extension
	 * @return <tt>true</tt> if extension is matched
	 */
	public static boolean testFileType(String s, String type) {
		return testFileType(s, new String[] { type });
	}
	
	/**
	 * Join array of strings together with a delimiter.
	 * @param s - array of strings
	 * @param c - delimiter
	 * @return A single <tt>String</tt>.
	 */
	public static String join(String[] s, String c) {
		String ret = "";
		for (int i = 0; i < s.length; i++) {
			ret += s[i];
			if (i != s.length-1)
				ret += c;
		}
		return ret;
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	public static int unsignByte(byte b) {
		int ret = (b + 256) % 256;
		return ret;
	}
	
	// transformations
	public static BufferedImage flipV(BufferedImage image){
		return flip(image, true);
	}
	
	public static BufferedImage flipH(BufferedImage image){
		return flip(image, false);
	}
	
	public static BufferedImage flip(BufferedImage image, boolean vertical) {
		AffineTransform at = new AffineTransform();
		if (vertical) {
			at.concatenate(AffineTransform.getScaleInstance(1, -1));
			at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
		} else {
			at.concatenate(AffineTransform.getScaleInstance(-1, 1));
			at.concatenate(AffineTransform.getTranslateInstance(-image.getWidth(), 0));
		}
		BufferedImage newImage =
				new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}
}

/**
 * Sprite class to handle drawing better
 */
class Sprite {
	int x;
	int y;
	int z;
	BufferedImage img;
	public Sprite(BufferedImage image, int xpos, int ypos) {
		img = image;
		x = xpos;
		y = ypos;
	}
	
	/**
	 * Attaches itself to a {@link Graphics2D} object and draws itself accordingly.
	 * @param g - Graphics2D object
	 */
	public void draw(Graphics2D g) {
		g.drawImage(img, x + 10, y + 10, null);
	}
}

/**
 * Frame class to handle drawing even more better
*/
class Anime {
	int d; // duration
	Sprite[] l; // list of sprites in frame
	
	public Anime(Sprite[] spriteList, int duration) {
		d = duration;
		l = spriteList;
	}
	
	public void draw(Graphics2D g) {
		for (Sprite s : l)
			s.draw(g);
	}
	
	/**
	 * Set the timer to use the frame's lenght as its delay.
	 * @param t - Timer object
	 * @param m - multiplier
	 */
	public void setNextTick(Timer t, double m) {
		t.setDelay((int) (d * m * SpriteAnimator.FPS));
	}
}