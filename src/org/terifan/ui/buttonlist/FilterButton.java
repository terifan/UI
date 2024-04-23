package org.terifan.ui.buttonlist;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.util.function.Consumer;
import javax.swing.JComponent;
import org.terifan.ui.TextBox;
import org.terifan.ui.buttonlist.FilterButtonModel.State;


public class FilterButton extends JComponent
{
	private final static FontRenderContext FRC = new FontRenderContext(null, true, false);

	private transient Consumer<FilterButton> mOnChange;
	private transient FilterButtonModel mModel;
	private String mTitle;
	private Integer mInfo;


	public FilterButton(String aTitle)
	{
		mTitle = aTitle;

		addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent aEvent)
			{
				mModel.handleMouseEvent(FilterButton.this, aEvent);
			}
		});
	}


	void init(FilterButtonModel aButtonList)
	{
		mModel = aButtonList;
	}


	public String getTitle()
	{
		return mTitle;
	}


	public void setTitle(String aTitle)
	{
		this.mTitle = aTitle;
	}


	public Integer getInfo()
	{
		return mInfo;
	}


	public FilterButton setInfo(Integer aValue)
	{
		mInfo = aValue;
		return this;
	}


	public Consumer<FilterButton> getOnChange()
	{
		return mOnChange;
	}


	public FilterButton setOnChange(Consumer<FilterButton> aAction)
	{
		mOnChange = aAction;
		return this;
	}


	@Override
	public Dimension getPreferredSize()
	{
		FilterButtonStyle style = mModel.getStyle();
		int h = 24;
		int tw = (int)getFont().getStringBounds(getVisibleTitle(), FRC).getWidth();
		int w = 2 * style.padding + Math.max(style.minWidth != null ? style.minWidth : 0, tw);
		return new Dimension(w, h);
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		FilterButtonStyle style = mModel.getStyle();
		int w = getWidth();
		int h = getHeight();

		TextBox.enableAntialiasing(aGraphics);

		aGraphics.setFont(getFont());
		aGraphics.setColor(style.backgroundColor);
		aGraphics.fillRect(0, 0, w, h);

		State state = mModel.getState(this);
		if (state == State.INCLUDE || state == State.EXCLUDE)
		{
			aGraphics.setColor(state == State.EXCLUDE ? style.textColorExcluded : style.textColorIncluded);
			aGraphics.fillRect(0, 0, 3, h);
		}

		new TextBox()
			.setBounds(0, 0, w, h)
			.setPaddingLeft(style.padding)
			.setPaddingRight(style.padding)
			.setText(getVisibleTitle())
			.setMaxLineCount(1)
			.setAnchor(style.textAlignment)
			.setForeground(style.getTextColor(state))
			.render(aGraphics);
	}


	protected String getVisibleTitle()
	{
		return mInfo == null ? mTitle : mTitle + " (" + mInfo + ")";
	}


	@Override
	public String toString()
	{
		return "FilterButton{title="+mTitle+"}";
	}
}
