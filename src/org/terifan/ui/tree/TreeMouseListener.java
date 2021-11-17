package org.terifan.ui.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;


public class TreeMouseListener extends MouseAdapter
{
	protected Tree mTree;


	public TreeMouseListener(Tree aTree)
	{
		mTree = aTree;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		AtomicInteger level = new AtomicInteger();

		TreeNode node = mTree.getRoot().intersect(mTree, aEvent, new AtomicInteger(), level);
		if (node != null)
		{
			if (aEvent.getX() < level.get() * mTree.mIndent)
			{
				node.mExpanded = !node.mExpanded;
			}
			else
			{
				mTree.setSelectedNode(node);
			}

			mTree.invalidate();
			mTree.repaint();
		}
	}


	@Override
	public void mouseEntered(MouseEvent aEvent)
	{
		mouseMoved(aEvent);
	}


	@Override
	public void mouseExited(MouseEvent aEvent)
	{
		mTree.setRollover(null);
		mTree.invalidate();
		mTree.repaint();
	}


	@Override
	public void mouseMoved(MouseEvent aEvent)
	{
		TreeNode node = mTree.getRoot().intersect(mTree, aEvent, new AtomicInteger(), new AtomicInteger());

		if (mTree.mRolloverNode != node)
		{
			mTree.setRollover(node);
			mTree.invalidate();
			mTree.repaint();
		}
	}
}
