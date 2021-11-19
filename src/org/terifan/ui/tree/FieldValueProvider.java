package org.terifan.ui.tree;

import java.lang.reflect.Field;


public class FieldValueProvider<T>
{
	public String getText(Tree<T> aTree, int aColumnIndex, T aValue)
	{
		Column column = aTree.getColumns().get(aColumnIndex);

		try
		{
			String name = column.getFieldName();
			if (name == null)
			{
				name = column.getName();
			}

			Class<? extends Object> cls = aValue.getClass();

			Field field = null;
			try
			{
				field = cls.getField(name);
			}
			catch (Exception e)
			{
				try
				{
					field = cls.getDeclaredField(name);
				}
				catch (Exception ee)
				{
				}
			}

			field.setAccessible(true);
			Object o = field.get(aValue);

			return o == null ? "" : o.toString();
		}
		catch (Exception e)
		{
			System.out.println("No field for column: " + column);
			return "";
		}
	}
}
