package org.terifan.ui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import org.terifan.ui.Anchor;
import org.terifan.ui.Alignment;
import org.terifan.ui.Fill;
import org.terifan.ui.Orientation;


public class FlowLayout implements LayoutManager
{
	private final Dimension mGap;
	private final Dimension mPadding;
	private Anchor mAnchor;
	private Fill mFill;
	private Alignment mAlignment;
	private Orientation mOrientation;
	private Wrap mWrap;


	public static enum Wrap
	{
		NONE,
		EXACT,
		FILL,
		SQUEEZE
	}


	public FlowLayout(Orientation aOrientation)
	{
		this(aOrientation, Anchor.NORTH_WEST, Alignment.LEFT);
	}


	public FlowLayout(Orientation aOrientation, Anchor aAnchor)
	{
		this(aOrientation, aAnchor, Alignment.LEFT);
	}


	public FlowLayout(Orientation aOrientation, Anchor aAnchor, Alignment aAlignment)
	{
		mOrientation = aOrientation;
		mAnchor = aAnchor;
		mAlignment = aAlignment;
		mFill = Fill.NONE;
		mWrap = Wrap.NONE;
		mPadding = new Dimension();
		mGap = new Dimension();
	}


	public Wrap getWrap()
	{
		return mWrap;
	}


	public FlowLayout setWrap(Wrap aWrap)
	{
		mWrap = aWrap;
		return this;
	}


	public Orientation getOrientation()
	{
		return mOrientation;
	}


	public FlowLayout setOrientation(Orientation aOrientation)
	{
		mOrientation = aOrientation;
		return this;
	}


	public Alignment getAlignment()
	{
		return mAlignment;
	}


	public FlowLayout setAlignment(Alignment aAlignment)
	{
		mAlignment = aAlignment;
		return this;
	}


	public FlowLayout setAnchor(Anchor aAnchor)
	{
		mAnchor = aAnchor;
		return this;
	}


	public FlowLayout setFill(Fill aFill)
	{
		mFill = aFill;
		return this;
	}


	public Dimension getGap()
	{
		return mGap;
	}


	public FlowLayout setGap(Dimension aGap)
	{
		mGap.setSize(aGap);
		return this;
	}


	public Dimension getPadding()
	{
		return mPadding;
	}


	public FlowLayout setPadding(Dimension aPadding)
	{
		mPadding.setSize(aPadding);
		return this;
	}

	private int[] mStripWidth = new int[100];
	private int[] mStripHeight = new int[100];
	private int[] mStripSize = new int[100];
	private int mStripCount;


	private Dimension layoutSize(Container aTarget, boolean aMinimum)
	{
		Insets insets = aTarget.getInsets();

		Dimension total = new Dimension(0, 0);

		if (aMinimum)
		{
			return total;
		}

		int targetWidth = aTarget.getWidth() - (insets.left + insets.right);
		int targetHeight = aTarget.getHeight() - (insets.top + insets.bottom);

		synchronized (aTarget.getTreeLock())
		{
			int n = aTarget.getComponentCount();

			mStripWidth = new int[100];
			mStripHeight = new int[100];
			mStripSize = new int[100];
			mStripCount = 0;

			for (int i = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				if (comp.isVisible())
				{
					Dimension compDimp = aMinimum ? comp.getMinimumSize() : comp.getPreferredSize();

					if (mStripSize[mStripCount] > 0 && mStripHeight[mStripCount] + mGap.height + compDimp.height + mPadding.height > targetHeight)
					{
						mStripCount++;
					}

					mStripWidth[mStripCount] = Math.max(mStripWidth[mStripCount], compDimp.width + mPadding.width);
					mStripHeight[mStripCount] += (mStripSize[mStripCount] > 0 ? mGap.height : 0) + compDimp.height + mPadding.height;
					mStripSize[mStripCount]++;
				}
			}

			mStripCount++;

			for (int i = 0; i < mStripCount; i++)
			{
				total.width += mStripWidth[i];
				total.height = Math.max(total.height, mStripHeight[i]);
			}
		}
		System.out.println(aMinimum + " " + total + " " + targetWidth + " " + targetHeight);

		total.width += insets.left + insets.right;
		total.height += insets.top + insets.bottom;
		return total;
	}


	@Override
	public void layoutContainer(Container aTarget)
	{
		Insets insets = aTarget.getInsets();

		int targetWidth = aTarget.getWidth();
		int targetHeight = aTarget.getHeight();

		synchronized (aTarget.getTreeLock())
		{
			Dimension parentDim = aTarget.getSize();

			layoutSize(aTarget, false);

			for (int i = 0, stripIndex = 0, stripOffset = 0; stripIndex < mStripCount; stripIndex++)
			{
				int stripWidth = mStripWidth[stripIndex];
				int stripHeight = mStripHeight[stripIndex];

				int y;
				if (mFill == Fill.BOTH || mFill == Fill.VERTICAL)
				{
					y = insets.top;
				}
				else
				{
					switch (mAnchor)
					{
						case NORTH_WEST:
						case NORTH:
						case NORTH_EAST:
							y = insets.top;
							break;
						case CENTER:
						case WEST:
						case EAST:
							y = Math.max(0, (parentDim.height - stripHeight) / 2);
							break;
						default:
							y = Math.max(0, parentDim.height - insets.bottom - stripHeight);
							break;
					}
				}

				for (int j = 0; j < mStripSize[stripIndex]; j++, i++)
				{
					Component comp = aTarget.getComponent(i);

					if (comp.isVisible())
					{
						Dimension compDimp = comp.getPreferredSize();
						int compWidth = compDimp.width + mPadding.width;
						int compHeight = compDimp.height + mPadding.height;

						int x = stripOffset;
						if (mFill == Fill.BOTH || mFill == Fill.VERTICAL)
						{
							compHeight += (parentDim.height - stripHeight) / mStripSize[stripIndex];
						}
						if (mFill == Fill.BOTH || mFill == Fill.HORIZONTAL)
						{
							x += insets.left;
							compWidth = parentDim.width - insets.left - insets.right;
						}
						else
						{
							int adjust = compWidth;
							switch (mAlignment)
							{
								case LEFT:
									adjust = insets.left;
									break;
								case JUSTIFY:
									adjust = -insets.left - insets.right;
									compWidth = stripWidth;
									break;
								case RIGHT:
									adjust = stripWidth - compWidth - insets.right;
									break;
								case CENTER:
									adjust = stripWidth / 2 - compWidth / 2 + insets.left;
									break;
							}
							switch (mAnchor)
							{
								case NORTH_WEST:
								case WEST:
								case SOUTH_WEST:
									x += adjust;
									break;
								case CENTER:
								case NORTH:
								case SOUTH:
									x += (parentDim.width - stripWidth) / 2 + adjust;
									break;
								default:
									x += parentDim.width - stripWidth + adjust;
									break;
							}
						}

						comp.setBounds(x, y, compWidth, compHeight);
						comp.revalidate();

						y += compHeight + mGap.height;
					}
				}

				stripOffset += mStripWidth[stripIndex] + mGap.width;
			}
		}
	}


//	private int computeHeight(Container aParent)
//	{
//		int height = 0;
//		for (int i = 0; i < aParent.getComponentCount(); i++)
//		{
//			Component c = aParent.getComponent(i);
//			Dimension cd = c.getPreferredSize();
//			height += cd.height + mGap;
//		}
//		height -= mGap;
//		return height;
//	}
	@Override
	public Dimension minimumLayoutSize(Container parent)
	{
		return layoutSize(parent, true);
	}


	@Override
	public Dimension preferredLayoutSize(Container parent)
	{
		return layoutSize(parent, false);
	}


	@Override
	public void addLayoutComponent(String name, Component comp)
	{
	}


	@Override
	public void removeLayoutComponent(Component comp)
	{
	}


	@Override
	public String toString()
	{
		return getClass().getName() + "[gap=" + mGap + ", anchor=" + mAnchor + "]";
	}
}
