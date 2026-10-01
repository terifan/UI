package org.terifan.ui;

import static org.testng.Assert.*;
import org.testng.annotations.Test;


public class TextBoxNGTest
{
	public TextBoxNGTest()
	{
	}


	@Test
	public void assertClonePreservesText()
	{
		TextBox original = new TextBox("hello world").setBounds(0, 0, 100, 20);
		original.pack();

		TextBox copy = original.clone();
		copy.setText("goodbye");

		assertEquals("hello world", original.getText());
		assertFalse(copy.measure().isEmpty());
	}
}
