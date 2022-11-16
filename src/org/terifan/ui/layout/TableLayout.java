package org.terifan.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.terifan.ui.Anchor;
import org.terifan.ui.Fill;


public class TableLayout implements LayoutManager2
{
	private ArrayList<Component> mCurrentRow;
	private ArrayList<ArrayList<Component>> mComponents;
	private ArrayList<Integer> mColumnWidths;
	private ArrayList<Integer> mRowHeights;
	private int mWidth;
	private int mHeight;
	private int mSpacingX;
	private int mSpacingY;


	public TableLayout()
	{
		this(0, 0);
	}


	public TableLayout(int aSpacingX, int aSpacingY)
	{
		mColumnWidths = new ArrayList<>();
		mRowHeights = new ArrayList<>();
		mComponents = new ArrayList<>();
		mSpacingX = aSpacingX;
		mSpacingY = aSpacingY;

		mCurrentRow = new ArrayList<>();
		mComponents.add(mCurrentRow);
	}


	@Override
	public synchronized void addLayoutComponent(Component aComp, Object aConstraints)
	{
		mCurrentRow.add(aComp);
	}


	@Override
	public synchronized void addLayoutComponent(String aName, Component aComp)
	{
		mCurrentRow.add(aComp);
	}


	@Override
	public synchronized void removeLayoutComponent(Component aComp)
	{
		mComponents.forEach(e -> e.remove(aComp));
	}


	@Override
	public Dimension minimumLayoutSize(Container aParent)
	{
		return preferredLayoutSize(aParent);
	}


	@Override
	public Dimension maximumLayoutSize(Container aTarget)
	{
		return preferredLayoutSize(aTarget);
	}


	@Override
	public synchronized Dimension preferredLayoutSize(Container aParent)
	{
		return new Dimension(mWidth, mHeight);
	}


	@Override
	public float getLayoutAlignmentX(Container aTarget)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	@Override
	public float getLayoutAlignmentY(Container aTarget)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	@Override
	public synchronized void invalidateLayout(Container aTarget)
	{
		ArrayList<Integer> columnWidths = new ArrayList<>();
		ArrayList<Integer> rowHeights = new ArrayList<>();

		int rowCount = getRowCount();
		int maxColumns = 0;

		for (int iy = 0; iy < rowCount; iy++)
		{
			int rowHeight = 0;

			for (int ix = 0; ix < mComponents.get(iy).size(); ix++)
			{
				JComponent comp = (JComponent)mComponents.get(iy).get(ix);
				Dimension dim = comp.getPreferredSize();

				int insetsW = comp.getInsets().left + comp.getInsets().right;
				int insetsH = comp.getInsets().top + comp.getInsets().bottom;

				if (comp.getLayout() instanceof TableLayout)insetsH=insetsW=0;

				rowHeight = Math.max(rowHeight, dim.height + insetsH);

				if (ix == columnWidths.size())
				{
					columnWidths.add(dim.width + insetsW);
				}
				else
				{
					int w = Math.max(dim.width + insetsW, columnWidths.get(ix));
					columnWidths.set(ix, w);
				}
			}

			rowHeights.add(rowHeight);
			maxColumns = Math.max(maxColumns, mComponents.get(iy).size());
		}

		int b = 1;//(aTarget.getParent().getLayout() instanceof TableLayout)?0:1;

		mColumnWidths = columnWidths;
		mRowHeights = rowHeights;
		mWidth = columnWidths.stream().mapToInt(e -> e).sum() + mSpacingX * (maxColumns - 1) + b * (aTarget.getInsets().left + aTarget.getInsets().right);
		mHeight = rowHeights.stream().mapToInt(e -> e).sum() + mSpacingY * (rowCount - 1) + b * (aTarget.getInsets().top + aTarget.getInsets().bottom);
	}


	@Override
	public synchronized void layoutContainer(Container aParent)
	{
		int rowCount = getRowCount();

		int extraW = 0;//aParent.getWidth() - mWidth - aParent.getInsets().left - aParent.getInsets().right;
		int extraH = 0;//aParent.getHeight() - mHeight - aParent.getInsets().top - aParent.getInsets().bottom;

		int maxCols = 0;
		for (int iy = 0; iy < mRowHeights.size() && iy < rowCount; iy++)
		{
			maxCols = Math.max(maxCols, mColumnWidths.size());
		}

		int rowY = aParent.getInsets().top;

		for (int iy = 0; iy < mRowHeights.size() && iy < rowCount; iy++)
		{
			int rowX = aParent.getInsets().left;

			ArrayList<Component> row = mComponents.get(iy);

			for (int ix = 0; ix < mColumnWidths.size() && ix < row.size(); ix++)
			{
				JComponent comp = (JComponent)row.get(ix);
				Dimension dim = comp.getPreferredSize();
//				dim.width += comp.getInsets().left + comp.getInsets().right;
//				dim.height += comp.getInsets().top + comp.getInsets().bottom;

				int compX = rowX;
				int compY = rowY;
				int cellW = mColumnWidths.get(ix) + extraW / maxCols;
				int cellH = mRowHeights.get(iy) + extraH / rowCount;

				Fill fill = Fill.NONE;
				Anchor anchor = Anchor.CENTER;

				Rectangle compBounds = new Rectangle(0, 0, dim.width, dim.height);
				Rectangle cellBounds = new Rectangle(compX, compY, cellW, cellH);

				fill.scale(compBounds, cellBounds);
				anchor.translate(compBounds, cellBounds);

				comp.setBounds(compBounds);

				rowX += mColumnWidths.get(ix) + mSpacingX + extraW / maxCols;
			}

			rowY += mRowHeights.get(iy) + mSpacingY + extraH / rowCount;
		}
	}


	private int getRowCount()
	{
		int rowCount;
		for (rowCount = mComponents.size(); rowCount > 0 && mComponents.get(rowCount - 1).isEmpty(); rowCount--)
		{
		}
		return rowCount;
	}


	public synchronized void advanceRow()
	{
		if (!mCurrentRow.isEmpty())
		{
			mCurrentRow = new ArrayList<>();
			mComponents.add(mCurrentRow);
		}
	}


	public static void main(String... args)
	{
		try
		{
			JPanel panel = createTestTable(0);

			JPanel outer = new JPanel(new FlowLayout());
			outer.add(panel);

			JFrame frame = new JFrame();
//			frame.add(new JScrollPane(panel));
			frame.add(outer);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
			System.exit(0);
		}
	}

	private static int _testCount = 7;


	private static JPanel createTestTable(int aLevel)
	{
		Random rnd = new Random(_testCount++);

		TableLayout layout = new TableLayout(5, 5);

		JPanel panel = new JPanel(layout);
		panel.setBorder(BorderFactory.createLineBorder(new Color(Color.HSBtoRGB(rnd.nextFloat(), 1f, 0.9f)), 10));
		panel.setBackground(new Color(200, 255, 200));

		for (int r = 0, rn = 1 + rnd.nextInt(4); r < rn; r++)
		{
			for (int c = 0, cn = 1 + rnd.nextInt(4); c < cn; c++)
			{
				if (aLevel == 0 && rnd.nextInt(8) == 0)
				{
					panel.add(createTestTable(aLevel + 1));
				}
				else
				{
					JLabel label = new JLabel("<==" + c + "," + r + "==>");
					label.setFont(new Font("segeo ui", Font.PLAIN, 8 + rnd.nextInt(50)));
					label.setBackground(new Color(200, 200, 200));
					label.setBorder(BorderFactory.createLineBorder(new Color(Color.HSBtoRGB(rnd.nextFloat(), 1f, 0.9f)), 10));
					label.setOpaque(true);
					panel.add(label);
				}
			}

			layout.advanceRow();
		}

		return panel;
	}
}
