package org.terifan.ui.taginput;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;


public class TagInput extends JComponent
{
	protected JPanel mLabelsPanel;
	protected JTextField mTextInput;
	protected JPopupMenu mPopupMenu;
	protected String mLastFilter;
	protected Label mEditingLabel;
	protected List<String> mOptions;
	protected SelectionListener mSelectionListener;


	public TagInput(List<String> aOptions, List<String> aSelections)
	{
		mOptions = aOptions;

		mTextInput = new JTextField(10);
		mTextInput.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent aEvent)
			{
				if (aEvent.getKeyCode() == KeyEvent.VK_ENTER)
				{
					createTag();
				}
				else if (aEvent.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					cancelEdit();
				}
				else if (aEvent.getKeyCode() == KeyEvent.VK_DOWN && mPopupMenu != null)
				{
					createPopupMenu(true);
					mPopupMenu.setSelected(mPopupMenu.getComponent(0));
				}
			}
		});
		mTextInput.addCaretListener(new CaretListener()
		{
			@Override
			public void caretUpdate(CaretEvent aEvent)
			{
				createPopupMenu(false);
			}
		});
		mTextInput.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent aEvent)
			{
				cancelEdit();
			}
		});

		mLabelsPanel = new JPanel(new SimpleLayoutManager());
		for (String option : aSelections)
		{
			mLabelsPanel.add(new Label(this, option));
		}
		mLabelsPanel.add(mTextInput);

		super.setLayout(new FlowLayout(FlowLayout.LEFT));
		super.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		super.add(mLabelsPanel);
	}


	public TagInput setSelectionListener(SelectionListener aSelectionListener)
	{
		mSelectionListener = aSelectionListener;
		return this;
	}


	protected void createPopupMenu(boolean aFocusable)
	{
		String filter = mTextInput.getText();
		if (mPopupMenu != null && filter.equals(mLastFilter) && mPopupMenu.isFocusable() == aFocusable)
		{
			return;
		}

		mLastFilter = filter;

		if (mPopupMenu != null)
		{
			mPopupMenu.setVisible(false);
			mPopupMenu = null;
		}

		List<String> list = filter.isEmpty() ? Collections.EMPTY_LIST : mOptions.stream().filter(option -> option.startsWith(filter)).toList();
		if (list.isEmpty())
		{
			if (mPopupMenu != null)
			{
				mPopupMenu.setVisible(false);
				mPopupMenu = null;
			}
		}
		else
		{
			mPopupMenu = new JScrollPopupMenu();
			for (String option : list)
			{
				mPopupMenu.add(new JMenuItem(new MenuItemAction(option)));
			}
			mPopupMenu.setFocusable(aFocusable);
			mPopupMenu.addMenuKeyListener(new MenuKeyListener()
			{
				@Override
				public void menuKeyPressed(MenuKeyEvent aEvent)
				{
					if (aEvent.getKeyCode() == KeyEvent.VK_UP && mPopupMenu != null && mPopupMenu.getSelectionModel().getSelectedIndex() == 0)
					{
						createPopupMenu(false);
						mTextInput.requestFocus();
					}
				}


				@Override
				public void menuKeyReleased(MenuKeyEvent aEvent)
				{
				}


				@Override
				public void menuKeyTyped(MenuKeyEvent aEvent)
				{
				}
			});
			mLabelsPanel.validate();
			mPopupMenu.show(mTextInput, 0, mTextInput.getBounds().height);
		}
	}


	protected void cancelEdit()
	{
		if (mEditingLabel != null)
		{
			mEditingLabel.setVisible(true);
			mEditingLabel = null;
			mTextInput.setText("");
			((SimpleLayoutManager)mLabelsPanel.getLayout()).setEditingLabel(null);
		}
	}


	protected void createTag()
	{
		if (mPopupMenu != null && mPopupMenu.isVisible())
		{
			mPopupMenu.setVisible(false);
			mPopupMenu = null;
		}

		String text = mTextInput.getText();
		mTextInput.setText("");

		if (mEditingLabel != null)
		{
			String old = mEditingLabel.getText();
			mEditingLabel.setText(text);
			fireTagChanged(old, text);
			cancelEdit();
		}
		else
		{
			if (!text.isEmpty())
			{
				mLabelsPanel.add(new Label(this, text));
				fireTagAdded(text);
			}
			revalidate();
		}
	}


	protected void removeTag(Label aLabel)
	{
		mLabelsPanel.remove(aLabel);
		fireTagRemoved(aLabel.getText());
		revalidate();
	}


	protected void editTag(Label aLabel)
	{
		if (!mTextInput.getText().isEmpty())
		{
			createTag();
		}

		((SimpleLayoutManager)mLabelsPanel.getLayout()).setEditingLabel(aLabel);

		mEditingLabel = aLabel;
		mTextInput.setText(mEditingLabel.getText());
		revalidate();
		mTextInput.requestFocus();
	}


	protected void fireTagAdded(String aText)
	{
		if (mSelectionListener != null)
		{
			mSelectionListener.tagAdded(aText);
		}
	}


	protected void fireTagChanged(String aFromText, String aToText)
	{
		if (mSelectionListener != null)
		{
			mSelectionListener.tagChanged(aFromText, aToText);
		}
	}


	protected void fireTagRemoved(String aText)
	{
		if (mSelectionListener != null)
		{
			mSelectionListener.tagRemoved(aText);
		}
	}


	private class MenuItemAction extends AbstractAction
	{
		public MenuItemAction(String aText)
		{
			super(aText);
		}


		@Override
		public void actionPerformed(ActionEvent aEvent)
		{
			mTextInput.setText(((JMenuItem)aEvent.getSource()).getText());
			mPopupMenu.setVisible(false);
			mPopupMenu = null;
			createTag();
		}
	};
}
