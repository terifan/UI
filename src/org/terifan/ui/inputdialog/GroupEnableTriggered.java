package org.terifan.ui.inputdialog;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;



public class GroupEnableTriggered implements ActionListener
{
	JPanel group;


	public GroupEnableTriggered(JPanel group)
	{
		this.group = group;
	}


	@Override
	public void actionPerformed(ActionEvent e)
	{
		setEnable(group, ((JCheckBox) e.getSource()).isSelected());
	}


	public void setEnable(Container aComponent, boolean aState)
	{
		aComponent.setEnabled(aState);
		for (int i = 0; i < aComponent.getComponentCount(); i++)
		{
			setEnable((Container) aComponent.getComponent(i), aState);
		}
	}
}
