package org.terifan.ui.propertygrid;

import java.awt.BorderLayout;
import java.awt.font.FontRenderContext;
import java.util.ArrayList;
import java.util.function.Function;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class PropertyGrid extends JPanel
{
	protected ArrayList<ChangeListener> mListeners;
	protected PropertyGridModel mModel;
	protected int mDividerPosition;
	protected JScrollPane mScrollPane;
	protected PropertyGridListPane mPanel;
	protected Property mSelectedProperty;
	protected StyleSheet mStyleSheet;
	protected Function<Property,Function<Property,Object>> mFunctionFactory;


	public PropertyGrid(PropertyGridModel aPropertyGridModel)
	{
		this(aPropertyGridModel, null, null);
	}


	public PropertyGrid(PropertyGridModel aPropertyGridModel, Function<Property, Function<Property, Object>> aFunctionFactory)
	{
		this(aPropertyGridModel, aFunctionFactory, null);
	}


	public PropertyGrid(PropertyGridModel aModel, StyleSheet aStyleSheet)
	{
		this(aModel, null, aStyleSheet);
	}


	public PropertyGrid(PropertyGridModel aModel, Function<Property, Function<Property, Object>> aFunctionFactory, StyleSheet aStyleSheet)
	{
		super(new BorderLayout());

		mListeners = new ArrayList<>();
		mFunctionFactory = aFunctionFactory;

		if (aStyleSheet == null)
		{
			aStyleSheet = new StyleSheet(PropertyGrid.class, PropertyGrid.class.getResource("resources/stylesheet.json"));
		}

		setStyleSheet(aStyleSheet);

		mPanel = new PropertyGridListPane(this);
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


	public Function<Property, Function<Property, Object>> getFunctionFactory()
	{
		return mFunctionFactory;
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


	/**
	 * Sets the model without changing the function factory property of this PropertyGrid.
	 */
	public void setModel(PropertyGridModel aPropertyGridModel)
	{
		mModel = aPropertyGridModel;

		mPanel.removeAll();

		buildComponentTree(mModel.getChildren(), 0);
	}


	public void setModel(PropertyGridModel aPropertyGridModel, Function<Property, Function<Property, Object>> aFunctionFactory)
	{
		mFunctionFactory = aFunctionFactory;
		setModel(aPropertyGridModel);
	}


	protected void buildComponentTree(ArrayList<Property> aList, int aIndent)
	{
		for (Property item : aList)
		{
			item.buildItem(this, mPanel, aIndent);

			if (item instanceof PropertyList)
			{
				buildComponentTree(((PropertyList)item).getChildren(), aIndent + 1);
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
			if (mListeners.size() > 0)
			{
				ChangeEvent event = new ChangeEvent(aProperty);
				for (ChangeListener o : mListeners)
				{
					o.stateChanged(event);
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


	public int getRowHeight()
	{
		return mStyleSheet.getInt("row_padding") + (int)mStyleSheet.getFont("item_font").getLineMetrics("Aj]", new FontRenderContext(null, true, false)).getHeight();
	}
}