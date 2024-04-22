package org.terifan.ui.buttonlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;


public class ButtonList
{
	ButtonGroup mIncludeButtonGroup;
	ButtonGroup mExcludeButtonGroup;
	private ArrayList<Button> mButtons;


	public ButtonList()
	{
		mIncludeButtonGroup = new ButtonGroup();
		mExcludeButtonGroup = new ButtonGroup();
		mButtons = new ArrayList<>();
	}


	public void visitIncluded(Consumer<Button> aConsumer)
	{
		mIncludeButtonGroup.getButtons().forEach(aConsumer);
	}


	public boolean hasIncluded()
	{
		return mIncludeButtonGroup.hasSelections();
	}


	public boolean isIncluded(String aKey)
	{
		return mIncludeButtonGroup.isSelected(aKey);
	}


	public boolean isIncluded(Button aButton)
	{
		return mIncludeButtonGroup.isSelected(aButton);
	}


	public int countIncluded()
	{
		return mIncludeButtonGroup.getSelected().size();
	}


	public boolean hasExcluded()
	{
		return mExcludeButtonGroup.hasSelections();
	}


	public boolean isExcluded(String aKey)
	{
		return mExcludeButtonGroup.isSelected(aKey);
	}


	public boolean isExcluded(Button aButton)
	{
		return mExcludeButtonGroup.isSelected(aButton);
	}


	public int countExcludes()
	{
		return mExcludeButtonGroup.getSelected().size();
	}


	public Iterable<Button> listIncluded()
	{
		return mIncludeButtonGroup.getButtons();
	}


	public Button getIncluded(String aKey)
	{
		return mIncludeButtonGroup.getButton(aKey);
	}


	public Button getButton(String aKey)
	{
		for (Button button : mButtons)
		{
			if (button.getTitle().equals(aKey))
			{
				return button;
			}
		}
		return null;
	}


	public void setIncluded(Button aButton, boolean aState)
	{
		if (aState)
		{
			mExcludeButtonGroup.setSelected(aButton, false);
		}
		mIncludeButtonGroup.setSelected(aButton, aState);
	}


	public void clearIncluded()
	{
		mIncludeButtonGroup.clear();
	}


	public void add(Button aButton)
	{
		aButton.init(this);
		mButtons.add(aButton);
	}
}
