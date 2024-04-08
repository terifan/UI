package org.terifan.ui.taginput;


public interface SelectionListener
{
	void tagAdded(String aText);


	void tagChanged(String aFromText, String aToText);


	void tagRemoved(String aText);
}
