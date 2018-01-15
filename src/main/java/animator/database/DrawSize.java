package animator.database;

public enum DrawSize {
	FULL ("", 0, 0, 16, 16),
	TOP_HALF ("T", 0, 0, 16, 8),
	BOTTOM_HALF ("B", 0, 8, 16, 8),
	RIGHT_HALF ("R", 8, 0, 8, 16),
	LEFT_HALF ("L", 0, 0, 8, 16),
	TOP_RIGHT ("TR", 8, 0, 8, 8),
	TOP_LEFT ("TL", 0, 0, 8, 8),
	BOTTOM_RIGHT ("BR", 8, 8, 8, 8),
	BOTTOM_LEFT ("BL", 0, 8, 8, 8),
	TALL_8X24 ("XT", 0, 0, 8, 24),
	WIDE_24X8 ("XW", 0, 0, 24, 8),
	LARGE_16X24 ("XL", 0, 0, 16, 24),
	LARGE_32X24 ("XXL", 0, 0, 32, 24),
	EMPTY ("E", 0, 0, 1, 1);

	public final String token;
	public final int x; // pixels to move along the x axis from the top-left of the cell
	public final int y; // pixels to move along the y axis from the top-left of the cell
	public final int w; // width of rectangle to cut with top-left corner at position {x,y}
	public final int h; // height of rectangle to cut with top-left corner at position {x,y}

	private DrawSize(String token, int x, int y, int w, int h) {
		this.token = token;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
}