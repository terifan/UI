package org.terifan.ui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
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


public class Tree extends JPanel implements Scrollable
{
	protected ArrayList<Column> mColumns;
	protected TreeNode mTreeRoot;
	protected TreeNode mRolloverNode;
	protected TreeNode mSelectedNode;
	protected int mGap;
	protected int mIndent;
	protected int mRowHeight;
	protected int mColumnHeaderHeight;
	protected int mIconWidth;
	protected boolean mPaintRootNode;
	protected boolean mPaintIndentLines;
	protected boolean mWindowFocused;


	public Tree()
	{
		mGap = 0 * 5;
		mRowHeight = 20;
		mIndent = 20;
		mColumnHeaderHeight = 20;
		mIconWidth = 20;
		mPaintRootNode = true;
		mColumns = new ArrayList<>();

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


	@Override
	protected void paintComponent(Graphics aGraphics)
	{
		int y = 0;
		int w = getWidth();
		int h = getHeight();

		aGraphics.setColor(getBackground());
		aGraphics.fillRect(0, y, w, h - y);

		mTreeRoot.paintComponent(this, aGraphics, w, y, 0);
	}


	@Override
	public Dimension getPreferredSize()
	{
		Dimension result = mTreeRoot.getPreferredSize(this, 0);
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
		return (Dimension)getPreferredSize().clone();
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
		return getParent() instanceof JViewport && (((JViewport)getParent()).getHeight() > getMinimumSize().height);
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

			JPanel columnHeaderView = new JPanel(new BorderLayout());
			columnHeaderView.add(new TreeColumnHeader(this), BorderLayout.NORTH);

			scrollPane.setColumnHeaderView(columnHeaderView);
			scrollPane.setBorder(null);

			JScrollBar vsb = scrollPane.getVerticalScrollBar();
			vsb.setUnitIncrement(mRowHeight);
			vsb.setBlockIncrement(mRowHeight * 10);
		}
	}


	protected void setSelectedNode(TreeNode aNode)
	{
		if (mSelectedNode != null)
		{
			mSelectedNode.mSelected = false;
		}

		mSelectedNode = aNode;

		if (mSelectedNode != null)
		{
			mSelectedNode.mSelected = true;
		}
	}
}
