package SpriteAnimator.Database;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public enum Transformation {
	Y_FLIP ( img -> {
			return flip(img, false);
		}),
	X_FLIP ( img -> {
			return flip(img, true);
		}),
	XY_FLIP ( img -> {
			return flip(flip(img, true), false);
		});

	// operation
	private final Transform T;

	private Transformation (Transform T) {
		this.T = T;
	}

	public final BufferedImage trans(BufferedImage img) {
		return T.transform(img);
	}

	/**
	 * Flips an image over the X-axis if the second argument is {@code true}
	 * or over the Y-axis if it is {@code false}
	 * @param image
	 * @param hor
	 */
	private static BufferedImage flip(BufferedImage image, boolean hor) {
		AffineTransform at = new AffineTransform();
		if (hor) {
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

	/**
	 * Runs transformation operation on a {@code BufferedImage} and returns a new image.
	 */
	private interface Transform {
		public BufferedImage transform(BufferedImage i);
	}
}