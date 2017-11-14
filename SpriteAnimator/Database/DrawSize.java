package SpriteAnimator.Database;

public enum DrawSize {
	FULL (0, 0, 16, 16), // old token: F
	TOP_HALF (0, 0, 16, 8), // old token: T
	BOTTOM_HALF (8, 0, 16, 8), // old token: B
	RIGHT_HALF (0, 8, 8, 16), // old token: R
	LEFT_HALF (0, 0, 8, 16), // old token: L
	TOP_RIGHT (0, 8, 8, 8), // old token: TR
	TOP_LEFT (0, 0, 8, 8), // old token: TL
	BOTTOM_RIGHT (8, 8, 8, 8), // old token: BR
	BOTTOM_LEFT (8, 0, 8, 8), // old token: BL
	TALL_8X24 (0, 0, 8, 24), // old token: XT
	WIDE_24X8 (0, 0, 24, 8), // old token: XW
	LARGE_16X24 (0, 0, 16, 24), // old token: XL
	LARGE_32X24 (0, 0, 32, 24), // old token: XXL
	EMPTY (0, 0, 1, 1);  // old token: E

	public final int x; // pixels to move along the x axis from the top-left of the cell
	public final int y; // pixels to move along the y axis from the top-left of the cell
	public final int w; // width of rectangle to cut with top-left corner at position {x,y}
	public final int h; // height of rectangle to cut with top-left corner at position {x,y}

	private DrawSize(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
}