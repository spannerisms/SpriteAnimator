package animator.gui.cellsearch;

import java.awt.image.BufferedImage;

public enum SheetOption {
	VANILLA ("Vanilla Link", CellLister.LINK),
	INDEX ("Index names", CellLister.INDEX_NAMES),
	SELECTED ("Currently loaded sprite", CellLister.NOTHING);

	public final String name;
	public final BufferedImage defaultImage;

	SheetOption(String name, BufferedImage img) {
		this.name = name;
		defaultImage = img;
	};

	public String toString() {
		return name;
	}
}