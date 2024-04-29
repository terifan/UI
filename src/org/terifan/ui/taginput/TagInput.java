package org.terifan.ui.taginput;

import java.awt.Component;
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
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;


public class TagInput extends JComponent
{
	protected JTextField mCreateField;
	protected JTextField mEditorField;
	protected JPopupMenu mPopupMenu;
	protected String mLastFilter;
	protected Tag mEditingTag;
	protected List<String> mOptions;
	protected SelectionListener mSelectionListener;
	protected JLabel mTitle;
	protected TagInputLayoutManager mLayout;


	private KeyAdapter mKeyListener = new KeyAdapter()
	{
		@Override
		public void keyPressed(KeyEvent aEvent)
		{
			if (aEvent.getKeyCode() == KeyEvent.VK_ENTER)
			{
				if (mEditingTag != null)
				{
					updateTag();
				}
				else
				{
					createTag();
				}
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
	};

	public TagInput(String aTitle, List<String> aOptions, List<String> aSelections)
	{
		mOptions = aOptions;

		CaretListener caretListener = new CaretListener()
		{
			@Override
			public void caretUpdate(CaretEvent aEvent)
			{
				createPopupMenu(false);
			}
		};
		FocusAdapter focusAdapter = new FocusAdapter()
		{
			@Override
			public void focusLost(FocusEvent aEvent)
			{
				cancelEdit();
			}
		};

		mEditorField = new JTextField();
		mEditorField.addKeyListener(mKeyListener);
		mEditorField.addCaretListener(caretListener);
		mEditorField.addFocusListener(focusAdapter);

		mCreateField = new JTextField(10);
		mCreateField.addKeyListener(mKeyListener);
		mCreateField.addCaretListener(caretListener);

		mTitle = new JLabel(aTitle);
		mLayout = new TagInputLayoutManager(mTitle, mCreateField, mEditorField);

		super.add(mTitle);
		super.setLayout(mLayout);
		for (String option : aSelections)
		{
			super.add(new Tag(this, option));
		}
		super.add(mCreateField);
		super.add(mEditorField);

		super.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	}


	public TagInput setSelectionListener(SelectionListener aSelectionListener)
	{
		mSelectionListener = aSelectionListener;
		return this;
	}


	protected void createPopupMenu(boolean aFocusable)
	{
		JTextField field = mEditingTag == null ? mCreateField : mEditorField;
		String filter = field.getText();
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
						field.requestFocus();
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
			validate();
			mPopupMenu.show(field, 0, field.getBounds().height);
		}
	}


	protected void cancelEdit()
	{
		mEditorField.setVisible(false);
		mEditorField.setText("");
		if (mEditingTag!=null)
		{
			mEditingTag.setVisible(true);
			mEditingTag = null;
		}
		mLayout.setEditingLabel(null);
		mCreateField.requestFocus();
	}


	protected void createTag()
	{
		if (mPopupMenu != null && mPopupMenu.isVisible())
		{
			mPopupMenu.setVisible(false);
			mPopupMenu = null;
		}

		String text = mCreateField.getText();
		if (!text.isEmpty())
		{
			mCreateField.setText("");
			addTag(text, true);
			revalidate();
		}
	}


	protected void updateTag()
	{
		if (mPopupMenu != null && mPopupMenu.isVisible())
		{
			mPopupMenu.setVisible(false);
			mPopupMenu = null;
		}

		String newText = mEditorField.getText();
		String oldText = mEditingTag.getText();

		if (newText.isEmpty())
		{
			cancelEdit();
			removeTag(oldText, true);
		}
		else
		{
			mEditingTag.setText(newText);
			fireTagChanged(oldText, newText);
			cancelEdit();
		}
	}


	public void addTag(String aTag, boolean aFireListener)
	{
		if (findTag(aTag) == null)
		{
			add(new Tag(this, aTag));
			if (aFireListener)
			{
				fireTagAdded(aTag);
			}
		}
	}


	public void removeTag(String aTag, boolean aFireListener)
	{
		Tag tag = findTag(aTag);
		if (tag != null)
		{
			remove(tag);
			if (aFireListener)
			{
				fireTagRemoved(aTag);
			}
		}
	}


	private Tag findTag(String aTag)
	{
		for (int i = 0; i < getComponentCount(); i++)
		{
			Component tag = getComponent(i);
			if (tag instanceof Tag v && v.getText().equals(aTag))
			{
				return v;
			}
		}
		return null;
	}


	protected void removeTag(Tag aTag)
	{
		remove(aTag);
		fireTagRemoved(aTag.getText());
		revalidate();
	}


	protected void editTag(Tag aTag)
	{
		if (mEditingTag != null)
		{
			cancelEdit();
		}

		mEditingTag = aTag;
		mLayout.setEditingLabel(mEditingTag);
		mEditorField.setText(mEditingTag.getText());
		mEditorField.requestFocus();
		revalidate();
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
			if (mEditingTag != null)
			{
				mEditorField.setText(((JMenuItem)aEvent.getSource()).getText());
				mPopupMenu.setVisible(false);
				mPopupMenu = null;
				updateTag();
			}
			else
			{
				mCreateField.setText(((JMenuItem)aEvent.getSource()).getText());
				mPopupMenu.setVisible(false);
				mPopupMenu = null;
				createTag();
			}
		}
	};
}
