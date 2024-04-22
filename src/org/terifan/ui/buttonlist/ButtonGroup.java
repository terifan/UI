package org.terifan.ui.buttonlist;

import java.util.HashSet;


class ButtonGroup
{
	private final HashSet<Button> mButtons;
	private final HashSet<Button> mSelected;


	public ButtonGroup()
	{
		mButtons = new HashSet<>();
		mSelected = new HashSet<>();
	}


	public HashSet<Button> getButtons()
	{
		return mButtons;
	}


	void clear()
	{
		mButtons.clear();
		mSelected.clear();
	}


	void clearSelection()
	{
		mSelected.clear();
	}


	void setSelected(Button aButton, boolean aState)
	{
		if (aState)
		{
			mSelected.add(aButton);
		}
		else
		{
			mSelected.remove(aButton);
		}
	}


	void add(Button aButton)
	{
		mButtons.add(aButton);
	}


	Button getButton(String aTitle)
	{
		for (Button button : mButtons)
		{
			if (button.getTitle().equals(aTitle))
			{
				return button;
			}
		}
		return null;
	}


	boolean isSelected(String aTitle)
	{
		for (Button button : mSelected)
		{
			if (button.getTitle().equals(aTitle))
			{
				return true;
			}
		}
		return false;
	}


	boolean isSelected(Button aButton)
	{
		for (Button button : mSelected)
		{
			if (button == aButton)
			{
				return true;
			}
		}
		return false;
	}


	boolean hasSelections()
	{
		return !mSelected.isEmpty();
	}


	HashSet<Button> getSelected()
	{
		return mSelected;
	}
}
