package org.terifan.ui.taginput;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;


public class XTag extends JLabel
{
	private int BUTTON_WIDTH = 16;
	private TagInput mTagInput;


	XTag(TagInput aTagInput, String aText)
	{
		super(aText);

		mTagInput = aTagInput;

		super.setOpaque(true);
		super.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(64, 64, 64)), BorderFactory.createEmptyBorder(0, 2, 0, 2)));
		super.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent aEvent)
			{
				if (aEvent.getX() < getWidth() - BUTTON_WIDTH)
				{
					mTagInput.editTag(XTag.this);
				}
				else
				{
					mTagInput.removeTag(XTag.this);
				}
			}
		});
		this.mTagInput = aTagInput;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		int bi = getBorder().getBorderInsets(this).right;
		int x = getWidth() - 7 - 2 - bi;
		int y = getHeight() / 2 - 2;
		super.paintComponent(aGraphics);
		aGraphics.setColor(Color.red);
		aGraphics.drawLine(x, y, x + 5, y + 5);
		aGraphics.drawLine(x + 5, y, x, y + 5);
	}


	@Override
	public Dimension getPreferredSize()
	{
		Dimension d = super.getPreferredSize();
		d.width += BUTTON_WIDTH;
		return d;
	}
}
