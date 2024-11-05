package org.terifan.ui;


public class DelegateMethodNotFoundException extends RuntimeException
{
	public DelegateMethodNotFoundException(String aMessage, Throwable aCause)
	{
		super(aMessage, aCause);
	}
}
