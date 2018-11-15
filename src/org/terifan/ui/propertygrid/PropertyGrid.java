package org.terifan.ui.propertygrid;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.terifan.ui.TextRenderer;


public class PropertyGrid extends JPanel
{
	protected ArrayList<ChangeListener> mListeners;
	protected PropertyGridModel mModel;
	protected int mDividerPosition;
	protected JScrollPane mScrollPane;
	protected PropertyGridListPane mPanel;
	protected Property mSelectedProperty;
	protected TextRenderer mTextRenderer;
	protected StyleSheet mStyleSheet;


	public PropertyGrid(PropertyGridModel aPropertyGridModel)
	{
		this(aPropertyGridModel, new StyleSheet(PropertyGrid.class, PropertyGrid.class.getResource("resources/stylesheet.json")));
	}


	public PropertyGrid(PropertyGridModel aModel, StyleSheet aStyleSheet)
	{
		super(new BorderLayout());

		setStyleSheet(aStyleSheet);

		mTextRenderer = new TextRenderer();

		mPanel = new PropertyGridListPane(this);
		mListeners = new ArrayList<>();
		mScrollPane = new JScrollPane(mPanel);
		mScrollPane.setBorder(null);

		add(mScrollPane, BorderLayout.CENTER);

		setOpaque(true);
		setModel(aModel);
	}


	public StyleSheet getStyleSheet()
	{
		return mStyleSheet;
	}


	public void setStyleSheet(StyleSheet aStyleSheet)
	{
		mStyleSheet = aStyleSheet;

		mDividerPosition = mStyleSheet.getInt("divider_position");
	}


	public void setDividerPosition(int aDividerPosition)
	{
		mDividerPosition = aDividerPosition;
	}


	public int getDividerPosition()
	{
		return mDividerPosition;
	}


	public void addChangeListener(ChangeListener aChangeListener)
	{
		mListeners.add(aChangeListener);
	}


	public void removeChangeListener(ChangeListener aChangeListener)
	{
		mListeners.remove(aChangeListener);
	}


	public PropertyGridModel getModel()
	{
		return mModel;
	}


	public void setModel(PropertyGridModel aPropertyGridModel)
	{
		mModel = aPropertyGridModel;

		mPanel.removeAll();

		Font valueFont = mStyleSheet.getFont("item");

		for (Iterator<Property> it = mModel.getRecursiveIterator(); it.hasNext();)
		{
			Property item = it.next();

			item.setPropertyGrid(this);

			mPanel.add(item.getIndentComponent());
			mPanel.add(item.getLabelComponent());

			JComponent component = item.getValueComponent();
			if (component != null)
			{
				mPanel.add(component);
				component.addFocusListener(new PropertyGridEditorListener(item));
				component.setFont(valueFont);
			}

			JButton button = item.getDetailButton();
			if (button != null)
			{
				mPanel.add(button);
			}
		}
	}


	protected Property getSelectedProperty()
	{
		return mSelectedProperty;
	}


	protected void setSelectedProperty(Property aProperty)
	{
		mSelectedProperty = aProperty;

		if (aProperty != null)
		{
//			if (aProperty.commitValue())
			{
				if (mListeners.size() > 0)
				{
					ChangeEvent event = new ChangeEvent(aProperty);
					for (ChangeListener o : mListeners)
					{
						o.stateChanged(event);
					}
				}
			}

			mSelectedProperty.getValueComponent().requestFocus();
		}
	}


	protected void redisplay()
	{
		mPanel.invalidate();
		mPanel.validate();
		mScrollPane.invalidate();
		mScrollPane.validate();
		invalidate();
		validate();
		repaint();
	}


	protected TextRenderer getTextRenderer()
	{
		return mTextRenderer;
	}
}