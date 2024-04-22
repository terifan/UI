package org.terifan.ui.buttonlist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class Button extends JComponent
{
	private final static FontRenderContext FRC = new FontRenderContext(null, true, true);

	private final static Color C1 = new Color(100, 100, 255);
	private final static Color C2 = new Color(230, 230, 230);
	private final static Color C3 = new Color(23, 23, 23);
	private final static Color C4 = new Color(48, 48, 48);
	private final static Color C5 = new Color(90, 90, 90);
	private final static Color C6 = new Color(255, 0, 0);
	private final static int PADDING = 10;


	private String mTitle;
	private Runnable mAction;
	private ButtonList mButtonList;
	private Integer mInfo;
	private Integer mMinWidth;
	private Anchor mTextAlignment;


	public Button(String aTitle)
	{
		mTitle = aTitle;
		mTextAlignment = Anchor.WEST;


		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent aEvent)
			{
				boolean rb = SwingUtilities.isRightMouseButton(aEvent);
				ButtonGroup targt = rb ? mButtonList.mExcludeButtonGroup : mButtonList.mIncludeButtonGroup;
				ButtonGroup other = rb ? mButtonList.mIncludeButtonGroup : mButtonList.mExcludeButtonGroup;

				other.setSelected(Button.this, false);

				if (aEvent.isControlDown() || targt.isSelected(Button.this))
				{
					targt.setSelected(Button.this, !targt.isSelected(Button.this));
				}
				else
				{
					targt.clearSelection();
					targt.setSelected(Button.this, true);
				}

				mAction.run();
			}
		});
	}

	void init(ButtonList aButtonList)
	{
		mButtonList = aButtonList;
		mButtonList.mIncludeButtonGroup.add(this);
		if (mButtonList.mExcludeButtonGroup != null)
		{
			mButtonList.mExcludeButtonGroup.add(this);
		}
	}


	public Button setTextAlignment(Anchor aTextAlignment)
	{
		mTextAlignment = aTextAlignment;
		return this;
	}


	public Button setMinWidth(int aMinWidth)
	{
		mMinWidth = aMinWidth;
		return this;
	}


	public String getTitle()
	{
		return mTitle;
	}


	public Button setInfo(Integer aValue)
	{
		mInfo = aValue;
		return this;
	}


	public Button setAction(Runnable aAction)
	{
		mAction = aAction;
		return this;
	}


	@Override
	public Dimension getPreferredSize()
	{
		int h = 24;
		int w0 = (int)getFont().getStringBounds(getVisibleTitle(), FRC).getWidth();
		int w1 = 2 * PADDING + Math.max(mMinWidth != null ? mMinWidth : 0, w0);
		return new Dimension(w1, h);
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		aGraphics.setFont(getFont());
		TextBox.enableAntialiasing(aGraphics);

		int w = getWidth();
		int h = getHeight();

		aGraphics.setColor(C3);
		aGraphics.fillRect(0, 0, w, h);

		boolean exclude = mButtonList.mExcludeButtonGroup != null && mButtonList.mExcludeButtonGroup.isSelected(Button.this);
		boolean include = !exclude && mButtonList.mIncludeButtonGroup.isSelected(Button.this);
		if (include || exclude)
		{
			aGraphics.setColor(exclude ? C6 : C1);
			aGraphics.fillRect(0, 0, 3, h);
		}

		TextBox textBox = new TextBox().setBounds(PADDING, 0, w - 2 * PADDING, h).setText(getVisibleTitle()).setMaxLineCount(1).setAnchor(mTextAlignment);

		if (mButtonList.mExcludeButtonGroup != null && mButtonList.mExcludeButtonGroup.isSelected(this))
		{
			textBox.setForeground(exclude ? C6 : C2);
		}
		else
		{
			boolean other = mInfo == null;
			textBox.setForeground(include ? C1 : other ? C5 : C2);
		}

		textBox.render(aGraphics);
	}


	protected String getVisibleTitle()
	{
		return mInfo == null ? mTitle : mTitle + " (" + mInfo + ")";
	}
}
