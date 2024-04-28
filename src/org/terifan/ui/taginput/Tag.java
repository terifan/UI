package org.terifan.ui.taginput;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;


public class Tag extends JComponent
{
//	private int BUTTON_WIDTH = 16;
	private TagInput mTagInput;
	private JButton mLabelButton;
	private JButton mDeleteButton;


	Tag(TagInput aTagInput, String aText)
	{
		mTagInput = aTagInput;

		mDeleteButton = new JButton("x");
		mDeleteButton.setFocusPainted(false);
		mDeleteButton.setFocusable(false);
		mDeleteButton.setIconTextGap(0);
		mDeleteButton.setIcon(null);
		mDeleteButton.setRolloverEnabled(false);
		mDeleteButton.setOpaque(false);
		mDeleteButton.setForeground(Color.RED);
//		mDeleteButton.setMargin(new Insets(3,3,3,3));
//		mDeleteButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(64, 64, 64)), BorderFactory.createEmptyBorder(0, 2, 0, 2)));
		mDeleteButton.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		mDeleteButton.setBorderPainted(false);
		mDeleteButton.addActionListener(aEvent ->
		{
			mTagInput.removeTag(Tag.this);
		});

		mLabelButton = new JButton(aText);
		mLabelButton.setFocusPainted(false);
		mLabelButton.setFocusable(false);
		mLabelButton.setIconTextGap(0);
		mLabelButton.setIcon(null);
		mLabelButton.setRolloverEnabled(false);
		mLabelButton.setOpaque(true);
//		mLabelButton.setMargin(new Insets(3,3,3,3));
//		mLabelButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(64, 64, 64)), BorderFactory.createEmptyBorder(0, 2, 0, 2)));
		mLabelButton.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		mLabelButton.addActionListener(aEvent ->
		{
			mTagInput.editTag(Tag.this);
		});

		super.setLayout(new FlowLayout(FlowLayout.LEFT, 0 ,0));
		super.add(mLabelButton);
		super.add(mDeleteButton);
	}


	public String getText()
	{
		return mLabelButton.getText();
	}


	public void setText(String aText)
	{
		mLabelButton.setText(aText);
	}


//	@Override
//	protected void paintComponent(Graphics aGraphics)
//	{
//		int bi = getBorder().getBorderInsets(this).right;
//		int x = getWidth() - 7 - 2 - bi;
//		int y = getHeight() / 2 - 2;
//
//		super.paintComponent(aGraphics);
//
//		aGraphics.setColor(Color.red);
//		aGraphics.drawLine(x, y, x + 5, y + 5);
//		aGraphics.drawLine(x + 5, y, x, y + 5);
//	}
//
//
//	@Override
//	public Dimension getPreferredSize()
//	{
//		Dimension d = super.getPreferredSize();
//		d.width += BUTTON_WIDTH;
//		return d;
//	}
}
