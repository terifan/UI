package org.terifan.ui.tree;


public class Column
{
	protected String mName;
	protected int mWidth;


	public Column(String aName)
	{
		mName = aName;
		mWidth = 100;
	}


	public String getName()
	{
		return mName;
	}


	public void setName(String aName)
	{
		this.mName = aName;
	}


	public int getWidth()
	{
		return mWidth;
	}


	public Column setWidth(int aWidth)
	{
		mWidth = aWidth;
		return this;
	}
}
