package org.terifan.ui.inputdialog;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.terifan.ui.Utilities;


public class Test
{
	public static void main(String ... args)
    {
        try
        {
			Utilities.setSystemLookAndFeel();

			JTextField name = new JTextField();
			JRadioButton male = new JRadioButton("Male");
			JRadioButton female = new JRadioButton("Female", true);
			JComboBox role = new JComboBox(new Object[]{"User","Administrator","Super user"});
			JTextArea description = new JTextArea();
			JCheckBox bold = new JCheckBox("Bold", true);
			JCheckBox italic = new JCheckBox("Italic", true);
			JCheckBox underline = new JCheckBox("Underline");
			JRadioButton red = new JRadioButton("Red");
			JRadioButton green = new JRadioButton("Green", true);
			JRadioButton blue = new JRadioButton("Blue");
			JRadioButton yellow = new JRadioButton("Yellow");
			JRadioButton cyan = new JRadioButton("Cyan");
			JRadioButton magenta = new JRadioButton("Magenta");
			JRadioButton white = new JRadioButton("White", true);
			JRadioButton black = new JRadioButton("Black");
			ButtonGroup grp1 = new ButtonGroup();
			ButtonGroup grp2 = new ButtonGroup();

			InputDialog dialog = new InputDialog(null, "Personal details", "Enter personal details for account");
			dialog.add("Name:", name);
			dialog.add("Sex:", male, female);
			dialog.add("Handedness:", grp1, new JRadioButton("Right"));
			dialog.add("", grp1, new JRadioButton("Left"));
			dialog.addSection("test");
			dialog.add("Role:", role);
			dialog.add("Description:", description);
			dialog.addSection("test");
			dialog.beginColumns();
			dialog.add("Field X:", new JTextField());
			dialog.add("Field Y:", new JTextField());
			dialog.beginGroup("Text", new JCheckBox("Enable custom text style", false));
			dialog.add("Style:", bold, italic, underline);
			dialog.endGroup();
			dialog.beginGroup("Color");
			dialog.add("Foreground:", grp2, red, green, blue);
			dialog.add("", grp2, yellow, cyan, magenta);
			dialog.add("Background:", white, black);
			dialog.addSection("test");
			dialog.add("Field S:", new JTextField());
			dialog.add("Field T:", new JTextField());
			dialog.endGroup();
			dialog.beginGroup(null, new JCheckBox("Enable text", false));
			dialog.add("Field I:", new JTextArea());
			dialog.add("Field J:", new JTextField());
			dialog.endGroup();
			dialog.beginGroup(null, new JCheckBox("Mark occurrences of symbols", true));
			dialog.add(null, new JCheckBox("Types"));
			dialog.add(null, new JCheckBox("Methods"));
			dialog.add(null, new JCheckBox("Constants"));
			dialog.endGroup();
			dialog.add("Field A:", new JTextField());
			dialog.add("Field B:", new JTextField());
			dialog.add("Field C:", new JTextField());
			dialog.endColumns();
			dialog.beginGroup("Preferences");
			dialog.add("Field G:", new JTextField("a"), new JTextField("b"), new JTextField("c"), new JTextField("d"));
			dialog.endGroup();
			dialog.add("E-mail:", new JTextField());

			dialog = new InputDialog(null, "Personal details", "Enter personal details for account");
			dialog.beginColumns();
			dialog.add("Name:", name);
			dialog.add("Sex:", male, female);
			dialog.add("Role:", role);
			dialog.add("Description:", description);
			dialog.endColumns();

			int o = dialog.show();

			if (o == InputDialog.OK_OPTION)
			{
				System.out.println("okay");
				System.out.println("name="+name.getText());
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
