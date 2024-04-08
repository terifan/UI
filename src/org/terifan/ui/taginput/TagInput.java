package org.terifan.ui.taginput;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
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


public class TagInput extends JComponent implements LayoutManager
{
	private final static int LABEL_INPUT_SPACING = 1;

	protected JPanel mLabelsPanel;
	protected JTextField mTextInput;
	protected JPopupMenu mPopupMenu;
	protected int mComputedLabelsWidth;
	protected int mTextInputWidth;
	protected String mLastFilter;
	protected List<String> mOptions;
	protected SelectionListener mSelectionListener;
	private String mEditTag;


	public TagInput(List<String> aOptions, List<String> aSelections)
	{
		mTextInputWidth = 100;

		mOptions = aOptions;

		mTextInput = new JTextField();
//		mTextInput.setBorder(BorderFactory.createEmptyBorder());
		mTextInput.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent aEvent)
			{
				if (aEvent.getKeyCode() == KeyEvent.VK_ENTER)
				{
					createTag();
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

		mLabelsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
		for (String option : aSelections)
		{
			mLabelsPanel.add(new Label(this, option));
		}

		super.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		super.setLayout(this);
		super.add(mLabelsPanel);
		super.add(mTextInput);
	}


	public TagInput setTextInputWidth(int aTextInputWidth)
	{
		mTextInputWidth = aTextInputWidth;
		return this;
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
			mPopupMenu.show(mTextInput, 0, mTextInput.getBounds().height);
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
		mLabelsPanel.add(new Label(this, text));
		mTextInput.setText("");
		if (mEditTag != null)
		{
			fireTagChanged(mEditTag, text);
			mEditTag = null;
		}
		else
		{
			fireTagAdded(text);
		}
		revalidate();
	}


	protected void removeTag(Label aLabel)
	{
		mLabelsPanel.remove(aLabel);
		fireTagRemoved(aLabel.getText());
		revalidate();
	}


	protected void editTag(Label aLabel)
	{
		mEditTag = aLabel.getText();
		mTextInput.setText(mEditTag);
		mLabelsPanel.remove(aLabel);
		revalidate();
		mTextInput.requestFocus();
		mTextInput.selectAll();
	}


	protected void fireTagAdded(String aText)
	{
		mSelectionListener.tagAdded(aText);
	}


	protected void fireTagChanged(String aFromText, String aToText)
	{
		mSelectionListener.tagChanged(aFromText, aToText);
	}


	protected void fireTagRemoved(String aText)
	{
		mSelectionListener.tagRemoved(aText);
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


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
//		aGraphics.setColor(Color.YELLOW);
//		aGraphics.fillRect(0, 0, getWidth(), getHeight());

		super.paintComponent(aGraphics);
	}


	@Override
	public void addLayoutComponent(String aName, Component aComp)
	{
	}


	@Override
	public void removeLayoutComponent(Component aComp)
	{
	}


	@Override
	public Dimension preferredLayoutSize(Container aParent)
	{
		Insets bi = getBorder().getBorderInsets(this);

		Dimension dd = mLabelsPanel.getPreferredSize();
		dd.height = Math.max(dd.height, 20);

		if (mLabelsPanel.getComponentCount() == 0)
		{
			dd.width = 0;
		}

		Dimension d = new Dimension(0, 0);
		mComputedLabelsWidth = dd.width;
		d.width += mComputedLabelsWidth;
		d.width += LABEL_INPUT_SPACING;
		d.width += mTextInputWidth;
		d.height = dd.height + bi.bottom + bi.top;

		return d;
	}


	@Override
	public Dimension minimumLayoutSize(Container aParent)
	{
		return preferredLayoutSize(aParent);
	}


	@Override
	public void layoutContainer(Container aParent)
	{
		Dimension d = preferredLayoutSize(aParent);
		mLabelsPanel.setBounds(1, 1, mComputedLabelsWidth, d.height - 2);
		int lw = Math.max(mComputedLabelsWidth, 1);
		mTextInput.setBounds(lw + LABEL_INPUT_SPACING, 1, d.width - lw - LABEL_INPUT_SPACING - 1, d.height - 2);
	}
}
