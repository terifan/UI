package org.terifan.ui.layout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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

		mColumnWidths = columnWidths;
		mRowHeights = rowHeights;
		mWidth = columnWidths.stream().mapToInt(e -> e).sum() + mSpacingX * (maxColumns - 1);
		mHeight = rowHeights.stream().mapToInt(e -> e).sum() + mSpacingY * (rowCount - 1);
	}


	@Override
	public synchronized void layoutContainer(Container aParent)
	{
		int rowCount = getRowCount();

		int rowY = aParent.getInsets().top;
		for (int iy = 0; iy < mRowHeights.size() && iy < rowCount; iy++)
		{
			int rowX = aParent.getInsets().left;

			for (int ix = 0; ix < mColumnWidths.size() && ix < mComponents.get(iy).size(); ix++)
			{
				JComponent comp = (JComponent)mComponents.get(iy).get(ix);
				Dimension dim = comp.getPreferredSize();
				dim.width += comp.getInsets().left + comp.getInsets().right;
				dim.height += comp.getInsets().top + comp.getInsets().bottom;

				int compX = rowX;
				int compY = rowY;
				int cellW = mColumnWidths.get(ix);
				int cellH = mRowHeights.get(iy);

				Fill fill = Fill.BOTH;
				Anchor anchor = Anchor.CENTER;

				Rectangle compBounds = new Rectangle(compX, compY, dim.width, dim.height);
				Rectangle cellBounds = new Rectangle(compX, compY, cellW, cellH);

				fill.scale(compBounds, cellBounds);
				anchor.translate(compBounds, cellBounds);

				comp.setBounds(compBounds);

				rowX += mColumnWidths.get(ix) + mSpacingX;
			}

			rowY += mRowHeights.get(iy) + mSpacingY;
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


	public synchronized void nextRow()
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

			JFrame frame = new JFrame();
			frame.add(new JScrollPane(panel));
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


	private static int _testCount=7;
	private static JPanel createTestTable(int aLevel)
	{
		Random rnd = new Random(_testCount++);

		TableLayout layout = new TableLayout(5, 5);

		JPanel panel = new JPanel(layout);
		Color color = new Color(Color.HSBtoRGB(rnd.nextFloat(), 1f, 0.9f));
		panel.setBorder(BorderFactory.createLineBorder(color, 10));

		for (int r = 0; r < 4; r++)
		{
			for (int c = 0, n = 1+rnd.nextInt(4); c < n; c++)
			{
				if (aLevel == 0 && rnd.nextInt(8) == 0)
				{
					panel.add(createTestTable(aLevel + 1));
				}
				else
				{
					JLabel label = new JLabel("<==" + c + "," + r + "==>");
					color = new Color(Color.HSBtoRGB(rnd.nextFloat(), 1f, 0.9f));
					label.setFont(new Font("segeo ui",Font.PLAIN,8+rnd.nextInt(50)));
					label.setBackground(new Color(200,200,200));
					label.setBorder(BorderFactory.createLineBorder(color, 10));
					label.setOpaque(true);
					panel.add(label);
				}
			}

			layout.nextRow();
		}

		return panel;
	}
}
