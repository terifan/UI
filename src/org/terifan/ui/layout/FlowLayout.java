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
	private Dimension mTotal;

	private int[] mStripSize0 = new int[100];
	private int[] mStripSize1 = new int[100];
	private int[] mStripLength = new int[100];
	private int mStripCount;


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


	private Dimension layoutSize(Container aTarget, boolean aMinimum)
	{
		Insets insets = aTarget.getInsets();

		boolean hor = mOrientation == Orientation.HORIZONTAL;

		int total0 = 0;
		int total1 = 0;

		int target0 = hor ? aTarget.getWidth() - (insets.left + insets.right) : aTarget.getHeight() - (insets.top + insets.bottom);
		int target1 = hor ? aTarget.getHeight() - (insets.top + insets.bottom) : aTarget.getWidth() - (insets.left + insets.right);

		System.out.println(target0 + " x " + target1);

		synchronized (aTarget.getTreeLock())
		{
			int n = aTarget.getComponentCount();

			mStripSize0 = new int[100];
			mStripSize1 = new int[100];
			mStripLength = new int[100];
			mStripCount = 0;

			for (int i = 0; i < n; i++)
			{
				Component comp = aTarget.getComponent(i);
				if (comp.isVisible())
				{
					Dimension compDimp = aMinimum ? comp.getMinimumSize() : comp.getPreferredSize();

					int s0 = hor ? compDimp.width + mPadding.width : compDimp.height + mPadding.height;
					int s1 = hor ? compDimp.height + mPadding.height : compDimp.width + mPadding.width;
					int g = hor ? mGap.width : mGap.height;

					if (mWrap != Wrap.NONE && mStripLength[mStripCount] > 0 && mStripSize0[mStripCount] + g + s0 > target0)
					{
						mStripCount++;
					}

					mStripSize0[mStripCount] += (mStripLength[mStripCount] > 0 ? g : 0) + s0;
					mStripSize1[mStripCount] = Math.max(mStripSize1[mStripCount], s1);
					mStripLength[mStripCount]++;
				}
			}

			mStripCount++;

			for (int i = 0; i < mStripCount; i++)
			{
				total0 += mStripSize0[i];
				total1 = Math.max(total1, mStripSize1[i]);
			}
		}

		total0 += hor ? insets.left + insets.right : insets.top + insets.bottom;
		total1 += hor ? insets.top + insets.bottom : insets.left + insets.right;

		mTotal = hor ? new Dimension(total0, total1) : new Dimension(total1, total0);

		return new Dimension(0, 0);
	}


	@Override
	public void layoutContainer(Container aTarget)
	{
		Insets insets = aTarget.getInsets();

		boolean hor = mOrientation == Orientation.HORIZONTAL;
		int target0 = hor ? aTarget.getWidth() - (insets.left + insets.right) : aTarget.getHeight() - (insets.top + insets.bottom);
		int target1 = hor ? aTarget.getHeight() - (insets.top + insets.bottom) : aTarget.getWidth() - (insets.left + insets.right);

		synchronized (aTarget.getTreeLock())
		{
			Dimension parentDim = aTarget.getSize();

			int parentDim0 = hor ? parentDim.width : parentDim.height;
			int parentDim1 = hor ? parentDim.height : parentDim.width;
			int g0 = hor ? mGap.width : mGap.height;
			int g1 = hor ? mGap.height : mGap.width;

			layoutSize(aTarget, false);

			int axis1 = 0;

			for (int i = 0, stripIndex = 0; stripIndex < mStripCount; stripIndex++)
			{
				int stripSize0 = mStripSize0[stripIndex];
				int stripSize1 = mStripSize1[stripIndex];

//				switch (mAnchor)
//				{
//					case NORTH_WEST:
//					case NORTH:
//					case NORTH_EAST:
//						axis1 = hor ? insets.top : insets.left;
//						break;
//					case CENTER:
//					case WEST:
//					case EAST:
//						axis1 = Math.max(0, (parentDim1 - stripSize1) / 2);
//						break;
//					default:
//						axis1 = Math.max(0, parentDim1 - (hor ? insets.bottom : insets.right) - stripSize1);
//						break;
//				}
				int axis0 = 0;
				int anc0 = 0;
				int anc1 = 0;

				for (int j = 0; j < mStripLength[stripIndex]; j++, i++)
				{
					Component comp = aTarget.getComponent(i);

					if (comp.isVisible())
					{
						Dimension compDimp = comp.getPreferredSize();
						int size0 = hor ? compDimp.width + mPadding.width : compDimp.height + mPadding.height;
						int size1 = hor ? compDimp.height + mPadding.height : compDimp.width + mPadding.width;

						if (mFill == Fill.BOTH || mFill == Fill.VERTICAL)
						{
							if (hor)
							{
								size1 = mStripSize1[stripIndex];
							}
							else
							{
								if (stripIndex < mStripCount - 1)
								{
									size0 += (target0 - mStripSize0[stripIndex]) / mStripLength[stripIndex];
								}
							}
						}
						if (mFill == Fill.BOTH || mFill == Fill.HORIZONTAL)
						{
							if (!hor)
							{
								size1 = mStripSize1[stripIndex];
							}
							else
							{
								size0 += (target0 - mStripSize0[stripIndex]) / mStripLength[stripIndex];
							}
						}

						int adjust1 = 0;
						switch (mAlignment)
						{
							case LEFT:
								adjust1 = insets.left;
								break;
							case RIGHT:
								adjust1 = stripSize1 - size1 - insets.right;
								break;
							case CENTER:
								adjust1 = (stripSize1 - size1) / 2 + insets.left;
								break;
						}

//						switch (mAnchor)
//						{
//							case NORTH_WEST:
//							case WEST:
//							case SOUTH_WEST:
//								axis0 += adjust1;
//								break;
//							case CENTER:
//							case NORTH:
//							case SOUTH:
//								axis0 += (parentDim0 - stripSize0) / 2 + adjust1;
//								break;
//							default:
//								axis0 += parentDim0 - stripSize0 + adjust1;
//								break;
//						}
						if (hor)
						{
							comp.setBounds(axis0 + anc0, axis1 + anc1 + adjust1, size0, size1);
						}
						else
						{
							comp.setBounds(axis1 + anc1 + adjust1, axis0 + anc0, size1, size0);
						}
						comp.revalidate();

						axis0 += size0 + g0;
					}
				}

				axis1 += stripSize1 + g1;

//				stripOffset0 += mStripSize0[stripIndex] + g0;
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
