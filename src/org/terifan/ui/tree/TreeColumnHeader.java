package org.terifan.ui.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;
import org.terifan.ui.Utilities;


public class TreeColumnHeader extends JPanel
{
	protected Tree mTree;


	protected TreeColumnHeader(Tree aTree)
	{
		mTree = aTree;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		ArrayList<Column> columns = mTree.getColumns();
		int y = 0;
		int w = getWidth();
		int h = getHeight();

		Utilities.enableTextAntialiasing(aGraphics);

		aGraphics.setColor(new Color(0xFCFCFC));
		aGraphics.fillRect(0, 0, w, h);
		aGraphics.setColor(new Color(0xE8F1FB));
		aGraphics.drawLine(0, h - 1, w, h - 1);

		for (int columnIndex = 0, x = 0; columnIndex < columns.size(); columnIndex++)
		{
			Column column = columns.get(columnIndex);

			int cw = columnIndex + 1 == columns.size() ? w - x : column.getWidth();

			if (columnIndex > 0)
			{
				aGraphics.setColor(new Color(0xE8F1FB));
				aGraphics.drawLine(x, 0, x, h - 1);
			}

			aGraphics.setColor(new Color(0, 0, 0));
			aGraphics.drawString(column.getName(), x + 5, y + 15);

			x += cw;
		}
	}


	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(10000, mTree.mColumnHeaderHeight);
	}
}
