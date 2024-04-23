package org.terifan.ui.buttonlist;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;


public class FilterButtonModel
{
	private HashMap<FilterButton, State> mButtons;
	private FilterButtonStyle mStyle;


	public static enum State
	{
		NORMAL, INCLUDE, EXCLUDE, SHADED
	}


	public FilterButtonModel()
	{
		mButtons = new HashMap<>();
		mStyle = new FilterButtonStyle();
	}


	public FilterButton add(FilterButton aButton)
	{
		aButton.init(this);
		mButtons.put(aButton, State.NORMAL);
		return aButton;
	}


	public FilterButtonStyle getStyle()
	{
		return mStyle;
	}


	public void setStyle(FilterButtonStyle aStyle)
	{
		this.mStyle = aStyle;
	}


	public void forEach(Consumer<FilterButton> aConsumer)
	{
		forEach(null, aConsumer);
	}


	public void forEach(State aState, Consumer<FilterButton> aConsumer)
	{
		mButtons.forEach((button, state) ->
		{
			if (aState == null || state == aState)
			{
				aConsumer.accept(button);
			}
		});
	}


	public State getState(FilterButton aButton)
	{
		return mButtons.get(aButton);
	}


	public int count(State aState)
	{
		return (int)mButtons.values().stream().filter(e -> e == aState).count();
	}


	public ArrayList<FilterButton> list(State aState)
	{
		ArrayList<FilterButton> keys = new ArrayList<>();
		mButtons.forEach((button, state) ->
		{
			if (state == aState)
			{
				keys.add(button);
			}
		});
		return keys;
	}


	public FilterButton getButton(String aButtonTitle)
	{
		return mButtons.keySet().stream().filter(e -> e.getTitle().equals(aButtonTitle)).findFirst().get();
	}


	public void setState(FilterButton aButton, State aState)
	{
		mButtons.put(aButton, aState);
	}


	public boolean replaceState(FilterButton aButton, State aOldState, State aNewState)
	{
		if (mButtons.get(aButton) == aOldState)
		{
			mButtons.put(aButton, aNewState);
			return true;
		}
		return false;
	}


	public void clearState(State aState)
	{
		for (Entry<FilterButton, State> entry : mButtons.entrySet())
		{
			if (entry.getValue() == aState)
			{
				mButtons.put(entry.getKey(), State.NORMAL);
			}
		}
	}


	public void handleMouseEvent(FilterButton aButton, MouseEvent aEvent)
	{
		State oldState = getState(aButton);
		State newState = SwingUtilities.isRightMouseButton(aEvent) ? State.EXCLUDE : State.INCLUDE;

		if (aEvent.isControlDown())
		{
			if (oldState == newState)
			{
				setState(aButton, State.NORMAL);
			}
			else
			{
				setState(aButton, newState);
			}
		}
		else
		{
			int c = count(newState);
			clearState(newState);
			if (oldState != newState || c > 1)
			{
				setState(aButton, newState);
			}
		}

		forEach(e -> e.repaint());

		if (aButton.getOnChange() != null)
		{
			aButton.getOnChange().accept(aButton);
		}
	}
}
