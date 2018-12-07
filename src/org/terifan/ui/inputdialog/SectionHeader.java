package org.terifan.ui.inputdialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;
import org.terifan.ui.Utilities;


class SectionHeader extends JComponent
{
	private Border mBorder;
	private String mLabel;


	public SectionHeader(String aLabel)
	{
		mBorder = UIManager.getBorder("TitledBorder.border");
		mLabel = aLabel;
	}


	@Override
	protected void paintComponent(Graphics g)
	{
		Utilities.enableTextAntialiasing(g);

		int y = getHeight()/2;

		if (mBorder == null) // cant find the correct shadow color of the titled border
		{
			g.setColor(getBackground().darker());
			g.drawLine(0,y,getWidth(),y);
			g.setColor(Color.WHITE);
			g.drawLine(0,y+1,getWidth(),y+1);
		}
		else // this code can fail on other then Windows PLAF.
		{
			int h = mBorder.getBorderInsets(this).top;
			Graphics g2 = g.create(0, y-h/2, getWidth(), h);
			mBorder.paintBorder(this, g2, -10, 0, getWidth()+20, 100);
			g2.dispose();
		}

		new TextBox(" "+mLabel+" ").setPadding(0,2,0,2).setBounds(5, 0, getWidth(), getHeight()).setAnchor(Anchor.WEST).setForeground(Color.BLACK).setHighlight(getBackground()).setMaxLineCount(1).render(g);
	}


	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(1,20);
	}
}
