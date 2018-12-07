package org.terifan.ui.inputdialog.demo;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.terifan.ui.Utilities;
import org.terifan.ui.inputdialog.InputDialog;


public class Small
{
	public static void main(String... args)
	{
		try
		{
			Utilities.setSystemLookAndFeel();

			JTextField name = new JTextField();
			JRadioButton male = new JRadioButton("Male");
			JRadioButton female = new JRadioButton("Female", true);
			JComboBox role = new JComboBox(new String[]
			{
				"User", "Administrator", "Super user"
			});
			JTextArea description = new JTextArea(4, 40);
			ButtonGroup sexGroup = new ButtonGroup();

			InputDialog dialog = new InputDialog(null, "Personal details", "Enter personal details for account");
			dialog.beginColumns();
			dialog.add("Name:", name);
			dialog.add("Role:", role);
			dialog.add("Description:", description);
			dialog.beginGroup("Sex:", null, false, true);
			dialog.add(null, sexGroup, male);
			dialog.add(null, sexGroup, female);
			dialog.endGroup();
			dialog.endColumns();

			if (dialog.show() == InputDialog.OK_OPTION)
			{
				System.out.println("name=" + name.getText());
			}
			else
			{
				System.out.println("cancelled");
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
