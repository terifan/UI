package org.terifan.ui.propertygrid;


public class Column
{
	protected String mName;
	protected String mFieldName;
	protected int mWidth;
	protected int mMinimumWidth;


	public Column(String aName)
	{
		mName = aName;
		mWidth = 100;
	}


	public String getName()
	{
		return mName;
	}


	public Column setName(String aName)
	{
		mName = aName;
		return this;
	}


	public String getFieldName()
	{
		return mFieldName;
	}


	public Column setFieldName(String aFieldName)
	{
		mFieldName = aFieldName;
		return this;
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


	public int getMinimumWidth()
	{
		return mMinimumWidth;
	}


	public Column setMinimumWidth(int aMinimumWidth)
	{
		mMinimumWidth = aMinimumWidth;
		return this;
	}


	@Override
	public String toString()
	{
		return "Column{" + "mName=" + mName + ", mFieldName=" + mFieldName + '}';
	}
}
