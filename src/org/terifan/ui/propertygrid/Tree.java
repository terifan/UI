package org.terifan.ui.propertygrid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


public class Tree<T> extends JPanel implements Scrollable
{
	protected BufferedImage mExpandIcon;
	protected BufferedImage mCollapseIcon;
	protected HashMap<Integer, Color> mIndentBackgroundColor;
	protected HashMap<Integer, Color> mIndentLineColor;
	protected ArrayList<Column> mColumns;
	protected TreeNode mTreeRoot;
	protected TreeNode mRolloverNode;
	protected TreeNode mSelectedNode;
	protected int mIndentWidth;
	protected int mRowHeight;
	protected int mColumnHeaderHeight;
	protected int mIconWidth;
	protected int mIconTextSpacing;
	protected boolean mPaintRootNode;
	protected boolean mPaintIndentLines;
	protected boolean mPaintHeaderRow;
	protected boolean PaintHorizontalLines;
	protected boolean PaintVerticalLines;
	public boolean mWindowFocused;
	protected boolean mHighlightFullRow;
	protected int mExpandWidth;
	protected int mIconStyle;
	protected int mCellRightMargin;
	protected int mCellLeftMargin;
	protected FieldValueProvider<T> mFieldValueProvider;
	protected Color mVerticalLineColor;
	protected Color mHorizontalLineColor;


	public Tree()
	{
		mRowHeight = 24;
		mIndentWidth = 19;
		mColumnHeaderHeight = 20;
		mIconWidth = 20;
		mExpandWidth = 20;
		mIconTextSpacing = 4;
		mCellLeftMargin = 5;
		mCellRightMargin = 5;
		mPaintRootNode = true;
		mPaintHeaderRow = true;
		mColumns = new ArrayList<>();
		mFieldValueProvider = new FieldValueProvider<>();
		mHorizontalLineColor = new Color(0xE0EAF9);

		super.setBackground(Color.WHITE);
		super.setOpaque(true);
		super.setFocusable(true);

		// override JScrollPane actions...
		AbstractAction action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent aEvent)
			{
			}
		};
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
		super.registerKeyboardAction(action, KeyStroke.getKeyStroke(KeyEvent.VK_END, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);

		MouseAdapter mouseAdapter = new TreeMouseListener(this);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}


	public void setRoot(TreeNode aTreeNode)
	{
		mTreeRoot = aTreeNode;
	}


	public TreeNode getRoot()
	{
		return mTreeRoot;
	}


	public void addColumn(Column aName)
	{
		mColumns.add(aName);
	}


	public ArrayList<Column> getColumns()
	{
		return mColumns;
	}


	public boolean isPaintRootNode()
	{
		return mPaintRootNode;
	}


	public void setPaintRootNode(boolean aPaintRootNode)
	{
		mPaintRootNode = aPaintRootNode;
	}


	public boolean isPaintIndentLines()
	{
		return mPaintIndentLines;
	}


	public void setPaintIndentLines(boolean aPaintIndentLines)
	{
		mPaintIndentLines = aPaintIndentLines;
	}


	public boolean isPaintHeaderRow()
	{
		return mPaintHeaderRow;
	}


	public Tree<T> setPaintHeaderRow(boolean aPaintHeaderRow)
	{
		mPaintHeaderRow = aPaintHeaderRow;
		configureEnclosingScrollPane();
		return this;
	}


	public boolean isPaintHorizontalLines()
	{
		return PaintHorizontalLines;
	}


	public Tree<T> setPaintHorizontalLines(boolean aPaintHorizontalLines)
	{
		PaintHorizontalLines = aPaintHorizontalLines;
		return this;
	}


	public boolean isPaintVerticalLines()
	{
		return PaintVerticalLines;
	}


	public Tree<T> setPaintVerticalLines(boolean aPaintVerticalLines)
	{
		PaintVerticalLines = aPaintVerticalLines;
		return this;
	}


	public Color getIndentBackgroundColor(int aIndex)
	{
		return mIndentBackgroundColor == null ? null : mIndentBackgroundColor.get(aIndex);
	}


	public void setIndentBackgroundColor(int aIndex, Color aColor)
	{
		if (mIndentBackgroundColor == null)
		{
			mIndentBackgroundColor = new HashMap<>();
		}
		mIndentBackgroundColor.put(aIndex, aColor);
	}


	public Color getIndentLineColor(int aIndex)
	{
		return mIndentLineColor == null ? null : mIndentLineColor.get(aIndex);
	}


	public void setIndentLineColor(int aIndex, Color aColor)
	{
		if (mIndentLineColor == null)
		{
			mIndentLineColor = new HashMap<>();
		}
		mIndentLineColor.put(aIndex, aColor);
	}


	public int getIconWidth()
	{
		return mIconWidth;
	}


	public Tree<T> setIconWidth(int aIconWidth)
	{
		mIconWidth = aIconWidth;
		return this;
	}


	public int getIconTextSpacing()
	{
		return mIconTextSpacing;
	}


	public Tree<T> setIconTextSpacing(int aIconTextSpacing)
	{
		mIconTextSpacing = aIconTextSpacing;
		return this;
	}


	public int getIndentWidth()
	{
		return mIndentWidth;
	}


	public Tree<T> setIndentWidth(int aIndentWidth)
	{
		mIndentWidth = aIndentWidth;
		return this;
	}


	public int getColumnHeaderHeight()
	{
		return mColumnHeaderHeight;
	}


	public Tree<T> setColumnHeaderHeight(int aColumnHeaderHeight)
	{
		mColumnHeaderHeight = aColumnHeaderHeight;
		return this;
	}


	public FieldValueProvider<T> getFieldValueProvider()
	{
		return mFieldValueProvider;
	}


	public Tree<T> setFieldValueProvider(FieldValueProvider aFieldValueProvider)
	{
		mFieldValueProvider = aFieldValueProvider;
		return this;
	}


	public boolean isHighlightFullRow()
	{
		return mHighlightFullRow;
	}


	public Tree<T> setHighlightFullRow(boolean aHighlightFullRow)
	{
		mHighlightFullRow = aHighlightFullRow;
		return this;
	}


	public Color getVerticalLineColor()
	{
		return new Color(0xE0EAF9);
	}


	public Tree<T> setVerticalLineColor(Color aVerticalLineColor)
	{
		mVerticalLineColor = aVerticalLineColor;
		return this;
	}


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		int y = 0;
		int w = getWidth();
		int h = getHeight();

		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, y, w, h - y);

		if (mTreeRoot != null)
		{
			mTreeRoot.paintComponent(this, aGraphics, w, y, 0);
		}
	}


	@Override
	public Dimension getPreferredSize()
	{
		Dimension result;
		if (mTreeRoot == null)
		{
			result = new Dimension();
		}
		else
		{
			result = mTreeRoot.getPreferredSize(this, 0);
		}
		return result;
	}


	protected void setRollover(TreeNode aNode)
	{
		if (mRolloverNode != null)
		{
			mRolloverNode.mRollover = false;
		}

		mRolloverNode = aNode;

		if (mRolloverNode != null)
		{
			mRolloverNode.mRollover = true;
		}

		repaint();
	}


	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}


	@Override
	public int getScrollableUnitIncrement(Rectangle aVisibleRect, int aOrientation, int aDirection)
	{
		int v = aOrientation == SwingConstants.VERTICAL ? mRowHeight : mRowHeight;
		return v;
	}


	@Override
	public int getScrollableBlockIncrement(Rectangle aVisibleRect, int aOrientation, int aDirection)
	{
		if (getParent() instanceof JViewport)
		{
			JViewport vp = (JViewport)getParent();
			return aOrientation == SwingConstants.VERTICAL ? vp.getHeight() : vp.getWidth();
		}

		int v = aOrientation == SwingConstants.VERTICAL ? mRowHeight : mRowHeight;
		return v;
	}


	@Override
	public boolean getScrollableTracksViewportWidth()
	{
		return getParent() instanceof JViewport && (((JViewport)getParent()).getWidth() > getPreferredSize().width);
	}


	@Override
	public boolean getScrollableTracksViewportHeight()
	{
		return getParent() instanceof JViewport && (((JViewport)getParent()).getHeight() > getPreferredSize().height);
	}


	protected JScrollPane getEnclosingScrollPane()
	{
		return (JScrollPane)SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
	}


	@Override
	public void addNotify()
	{
		super.addNotify();
		configureEnclosingScrollPane();
	}


	public void configureEnclosingScrollPane()
	{
		JScrollPane scrollPane = getEnclosingScrollPane();

		if (scrollPane != null)
		{
			JViewport viewport = scrollPane.getViewport();
			viewport.setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);

			if (mPaintHeaderRow)
			{
				JPanel columnHeaderView = new JPanel(new BorderLayout());
				columnHeaderView.add(new TreeColumnHeader(this), BorderLayout.NORTH);

				scrollPane.setColumnHeaderView(columnHeaderView);
				scrollPane.setBorder(null);
			}
			else
			{
				scrollPane.setColumnHeaderView(null);
				scrollPane.setBorder(null);
			}

			JScrollBar vsb = scrollPane.getVerticalScrollBar();
			vsb.setUnitIncrement(mRowHeight);
		}
	}


	protected void setSelectedNode(TreeNode aNode)
	{
		if (mSelectedNode != null)
		{
			mSelectedNode.mSelected = false;
		}

		if (aNode == null || aNode.isSelectable())
		{
			mSelectedNode = aNode;

			if (mSelectedNode != null)
			{
				mSelectedNode.mSelected = true;
			}
		}
	}


	public Tree<T> setIconStyle(int aIconStyle)
	{
		mIconStyle = aIconStyle;
		mExpandIcon = null;
		return this;
	}


	protected BufferedImage getIcon(boolean aExpand)
	{
		if (mExpandIcon == null)
		{
			try
			{
				BufferedImage icons = ImageIO.read(TreeNode.class.getResource("icons.png"));
				if (mIconStyle == 0)
				{
					mExpandIcon = icons.getSubimage(11 * 16, 0, 16, 16);
					mCollapseIcon = icons.getSubimage(10 * 16, 0, 16, 16);
				}
				else
				{
					mExpandIcon = icons.getSubimage(3 * 16, 0, 16, 16);
					mCollapseIcon = icons.getSubimage(4 * 16, 0, 16, 16);
				}
			}
			catch (IOException e)
			{
				throw new IllegalStateException(e);
			}
		}

		return aExpand ? mExpandIcon : mCollapseIcon;
	}


	public Color getHorizontalLineColor()
	{
		return mHorizontalLineColor;
	}
}
