package animator.cellsearch;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import static spritemanipulator.SpriteManipulator.SPRITE_SHEET_HEIGHT;
import static spritemanipulator.SpriteManipulator.SPRITE_SHEET_WIDTH;
import static javax.swing.SpringLayout.*;

public class CellFrame extends JDialog {
	private static final long serialVersionUID = 2205482111878391757L;

	public static final int ZOOM = 2;
	private static final Dimension D =
			new Dimension(SPRITE_SHEET_WIDTH * ZOOM + 200 + 50 * ZOOM, SPRITE_SHEET_HEIGHT * ZOOM + 50);
	private static final Dimension D_SMALL =
			new Dimension(SPRITE_SHEET_WIDTH * ZOOM + 200 + 50 * ZOOM, SPRITE_SHEET_HEIGHT * 3 / 2);

	private CellLister lister;
	public CellFrame(JFrame f) {
		super(f);
		initialize();
		this.setResizable(false);
		this.setTitle("Sheet trawler");
	}

	private final void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int screenHeight = screenSize.height;
		boolean tooBig = screenHeight < (SPRITE_SHEET_HEIGHT * ZOOM - 50);
		Dimension curD = tooBig ? D_SMALL : D;

		this.setPreferredSize(curD);
		this.setMaximumSize(curD);
		this.setMinimumSize(curD);

		SpringLayout l = new SpringLayout();
		this.setLayout(l);

		// sheet controls
		lister = new CellLister();
		JScrollPane listScroll =
				new JScrollPane(lister,
						tooBig ? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS : JScrollPane.VERTICAL_SCROLLBAR_NEVER,
						JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listScroll.getViewport().setBorder(null);
		//listScroll.getViewport().setBackground(new Color(102,102,102));
		l.putConstraint(NORTH, listScroll, 5, NORTH, this);
		l.putConstraint(SOUTH, listScroll,
				tooBig ? (SPRITE_SHEET_HEIGHT * 3 / 2) - 50: SPRITE_SHEET_HEIGHT * ZOOM,
				NORTH, this);
		l.putConstraint(WEST, listScroll, 5, WEST, this);
		this.add(listScroll);

		// image shown
		JComboBox<SheetOption> sheet = new JComboBox<SheetOption>(SheetOption.values());
		l.putConstraint(NORTH, sheet, 5, NORTH, this);
		l.putConstraint(EAST, sheet, -15, EAST, this);
		this.add(sheet);

		// unused frames
		JCheckBox showUnused = new JCheckBox("Hide unused sprites");
		l.putConstraint(NORTH, showUnused , 5, SOUTH, sheet);
		l.putConstraint(EAST, showUnused , 0, EAST, sheet);
		this.add(showUnused);

		JLabel info = new JLabel("Step counts are with all sprites visible.");
		l.putConstraint(SOUTH, info, 0, SOUTH, listScroll);
		l.putConstraint(WEST, info, 15, EAST, listScroll);
		l.putConstraint(EAST,  info, 0, EAST, sheet);
		this.add(info);

		// text for frames
		@SuppressWarnings("serial")
		JLabel listed = new JLabel("") {
			public void setText(String t) {
				super.setText("<html><div style=\"padding:4px; max-width:200px\">" + t + "</div></html>");
			}
		};

		listed.setVerticalAlignment(SwingConstants.NORTH);

		JScrollPane list = new JScrollPane(listed,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		l.putConstraint(NORTH, list, 5, SOUTH, showUnused );
		l.putConstraint(SOUTH, list, -5, NORTH,  info);
		l.putConstraint(WEST, list, 15, EAST, listScroll);
		l.putConstraint(EAST, list, 0, EAST, sheet);
		this.add(list);

		sheet.addItemListener(
			arg0 -> {
				lister.setOption((SheetOption) arg0.getItem());
			});

		lister.addActionListener(
				arg0 -> {
					SpriteBlock b = (SpriteBlock) arg0.getSource();
					listed.setText(b.cell.HTMLret);
					repaint();
				});

		showUnused.addChangeListener(
				arg0 -> {
					lister.setHideUnused(showUnused.isSelected());
				});
		showUnused.setSelected(true);
	}

	public void setSprite(BufferedImage i) {
		lister.setSprite(i);
		repaint();
	}
}