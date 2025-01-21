package org.terifan.ui.buttonlist;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.SwingUtilities;


public class FilterButtonModel
{
	private HashMap<FilterButton, State> mButtons;
	private FilterButtonStyle mStyle;
	private ArrayList<StateChangeListener> mStateChangeListeners;


	public static enum State
	{
		UNSELECTED, INCLUDE, EXCLUDE, SHADED
	}


	public FilterButtonModel()
	{
		mButtons = new HashMap<>();
		mStyle = new FilterButtonStyle();
		mStateChangeListeners = new ArrayList<>();
	}


	public FilterButton add(FilterButton aButton)
	{
		aButton.init(this);
		mButtons.put(aButton, State.UNSELECTED);
		return aButton;
	}


	public void remove(FilterButton aButton)
	{
		mButtons.remove(aButton);
	}


	public void clear()
	{
		mButtons.clear();
	}


	public void addStateChangeListener(StateChangeListener aStateChangeListener)
	{
		mStateChangeListeners.add(aStateChangeListener);
	}


	public interface StateChangeListener
	{
		void onChange(FilterButton aButton);
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
		assertOwner(aButton);
		return mButtons.get(aButton);
	}


	public int count(State aState)
	{
		return (int)mButtons.values().stream().filter(e -> e == aState).count();
	}


	public ArrayList<FilterButton> getAll(State aState)
	{
		ArrayList<FilterButton> buttons = new ArrayList<>();
		mButtons.forEach((button, state) ->
		{
			if (state == aState)
			{
				buttons.add(button);
			}
		});
		return buttons;
	}


	public boolean matchAll(String... aMatch)
	{
		for (Entry<FilterButton, State> entry : mButtons.entrySet())
		{
			if (entry.getValue() == State.EXCLUDE)
			{
				for (String s : aMatch)
				{
					if (s.equals(entry.getKey().getTitle()))
					{
						return false;
					}
				}
			}
			else if (entry.getValue() == State.INCLUDE)
			{
				boolean b = false;
				for (String s : aMatch)
				{
					if (s.equals(entry.getKey().getTitle()))
					{
						b = true;
						break;
					}
				}
				if (!b)
				{
					return false;
				}
			}
		}
		return true;
	}


	public boolean matchAny(String... aMatch)
	{
		for (Entry<FilterButton, State> entry : mButtons.entrySet())
		{
			if (entry.getValue() == State.EXCLUDE)
			{
				if (aMatch.length == 0 && entry.getKey().getTitle().isEmpty())
				{
					return false;
				}
				for (String s : aMatch)
				{
					if (s.equals(entry.getKey().getTitle()))
					{
						return false;
					}
				}
			}
		}
		boolean any = true;
		for (Entry<FilterButton, State> entry : mButtons.entrySet())
		{
			if (entry.getValue() == State.INCLUDE)
			{
				if (aMatch.length == 0 && entry.getKey().getTitle().isEmpty())
				{
					return true;
				}
				any = false;
				for (String s : aMatch)
				{
					if (s.equals(entry.getKey().getTitle()))
					{
						return true;
					}
				}
			}
		}
		return any;
	}


	public ArrayList<FilterButton> list()
	{
		return new ArrayList<>(mButtons.keySet());
	}


	public <T> ArrayList<T> listUserObjects(State aState)
	{
		ArrayList<T> list = new ArrayList<>();
		mButtons.forEach((button, state) ->
		{
			if (state == aState)
			{
				list.add((T)button.getUserObject());
			}
		});
		return list;
	}


	public FilterButton getButton(String aButtonTitle)
	{
		return mButtons.keySet().stream().filter(e -> e.getTitle().equals(aButtonTitle)).findFirst().orElse(null);
	}


	public void setState(FilterButton aButton, State aState)
	{
		assertOwner(aButton);
		mButtons.put(aButton, aState);
		mStateChangeListeners.forEach(c -> c.onChange(aButton));
	}


	private void assertOwner(FilterButton aButton) throws IllegalArgumentException
	{
		if (!mButtons.containsKey(aButton))
		{
			throw new IllegalArgumentException(aButton == null ? "<is null>" : "" + aButton);
		}
	}


	public boolean replaceState(FilterButton aButton, State aOldState, State aNewState)
	{
		assertOwner(aButton);
		if (mButtons.get(aButton) == aOldState)
		{
			setState(aButton, aNewState);
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
				setState(entry.getKey(), State.UNSELECTED);
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
				setState(aButton, State.UNSELECTED);
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


	@Override
	public String toString()
	{
		return "FilerButtonModel" + mButtons.keySet().stream().map(e -> e.getTitle()).collect(Collectors.toList());
	}
}
