package org.terifan.ui.inputdialog;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;


class ButtonAction extends AbstractAction
{
	private int mValue;
	private final InputDialog mInputDialog;


	public ButtonAction(InputDialog aInputDialog, String aName, int aValue)
	{
		super(aName);
		mValue = aValue;
		mInputDialog = aInputDialog;
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		mInputDialog.setOption(mValue);
		mInputDialog.getDialog().setVisible(false);
	}
}
