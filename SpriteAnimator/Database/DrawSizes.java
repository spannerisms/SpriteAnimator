package SpriteAnimator.Database;

public enum DrawSizes {
	FULL (0, 0, 16, 16),
	TOP_HALF (0, 0, 16, 8),
	BOTTOM_HALF (8, 0, 16, 8),
	RIGHT_HALF (0, 8, 8, 16),
	LEFT_HALF (0, 0, 8, 16),
	TOP_RIGHT (0, 8, 8, 8),
	TOP_LEFT (0, 0, 8, 8),
	BOTTOM_RIGHT (8, 8, 8, 8),
	BOTTOM_LEFT (8, 0, 8, 8),
	TALL_8X24 (0, 0, 8, 24),
	WIDE_24X8 (0, 0, 24, 8),
	LARGE_16X24 (0, 0, 16, 24),
	LARGE_32X24 (0, 0, 32, 24),
	EMPTY (0, 0, 1, 1);

	public final int x;
	public final int y;
	public final int w;
	public final int h;

	private DrawSizes(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
}