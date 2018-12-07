package org.terifan.ui.inputdialog.demo;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.terifan.ui.Utilities;
import org.terifan.ui.inputdialog.InputDialog;


public class Demo
{
	public static void main(String... args)
	{
		try
		{
			Utilities.setSystemLookAndFeel();

			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			tabbedPane.addTab("Example D", wrap(createD()));
			tabbedPane.addTab("Example C", wrap(createC()));
			tabbedPane.addTab("Example B", wrap(createB()));
			tabbedPane.addTab("Example A", wrap(createA()));

			JFrame frame = new JFrame();
			frame.add(tabbedPane);
			frame.setSize(1024, 768);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}


	private static JPanel createA()
	{
		JTextField name = new JTextField();
		JRadioButton male = new JRadioButton("Male");
		JRadioButton female = new JRadioButton("Female", true);
		JComboBox role = new JComboBox(new String[]
		{
			"User", "Administrator", "Super user"
		});
		JTextArea description = new JTextArea(4, 40);
		ButtonGroup sexGroup = new ButtonGroup();

		InputDialog dialog = new InputDialog(null, null, null);
		dialog.beginColumns();
		dialog.add("Name:", name);
		dialog.add("Role:", role);
		dialog.add("Description:", description);
		dialog.beginGroup("Sex:", null, false, true);
		dialog.add(null, sexGroup, male);
		dialog.add(null, sexGroup, female);
		dialog.endGroup();
		dialog.endColumns();

		return dialog.getContentPanel();
	}


	private static JPanel createB()
	{
		JTextField name = new JTextField();
		JComboBox role = new JComboBox(new String[]
		{
			"User", "Administrator", "Super user"
		});
		JTextArea description = new JTextArea(4, 40);

		InputDialog dialog = new InputDialog(null, null, null);
		dialog.addSection("General");
		dialog.add("Name:", name);
		dialog.add("Role:", role);
		dialog.add("Description:", description);
		dialog.beginGroup("Tags");
		dialog.add(null, new JCheckBox("Aaaa"), new JCheckBox("Bbbb"), new JCheckBox("Cccc"), new JCheckBox("Ddddd"));
		dialog.endGroup();

		return dialog.getContentPanel();
	}


	private static JPanel createC()
	{
		JComboBox language = new JComboBox(new String[]
		{
			"All Languages"
		});

		InputDialog dialog = new InputDialog(null, null, null);
		dialog.beginGroup(null);
		dialog.add("Language:", language, new JCheckBox("Enabled Code Folding"));
		dialog.endGroup();
		dialog.beginGroup("Collapse by default");
		dialog.add(null, new JCheckBox("Code Blocks"));
		dialog.add(null, new JCheckBox("Comments"));
		dialog.add(null, new JCheckBox("Documentation"));
		dialog.add(null, new JCheckBox("Member"));
		dialog.endGroup();
		dialog.beginGroup("Display Options");
		dialog.add(null, new JCheckBox("Content preview"));
		dialog.add(null, new JCheckBox("Show summary"));
		dialog.endGroup();

		return dialog.getContentPanel();
	}


	private static JPanel createD()
	{
		JComboBox language = new JComboBox(new String[]
		{
			"All Languages"
		});
		JComboBox category = new JComboBox(new String[]
		{
			"Tabs And Idents"
		});
		JComboBox wrap= new JComboBox(new String[]
		{
			"Off",
			"After words"
		});

		InputDialog dialog = new InputDialog(null, null, null);
		dialog.beginGroup(null);
		dialog.add("Language:", language);
		dialog.add("Category:", category);
		dialog.endGroup();
		dialog.beginGroup("", new JCheckBox("Use All Languages Settings"));
		dialog.add("", new JCheckBox("Enabled Indentation"));
		dialog.add("", new JCheckBox("Expands Tabs To Spaces"));
		dialog.add("Number of Spaces per Indent:", new JSpinner(new SpinnerNumberModel(4, 4, 4, 0)));
		dialog.add("Tab Size:", new JSpinner(new SpinnerNumberModel(4, 4, 4, 0)));
		dialog.add("Right Margin:", new JSpinner(new SpinnerNumberModel(140, 140, 140, 0)));
		dialog.add("Line Wrap:", wrap);
		dialog.endGroup();
		dialog.beginGroup("");
		dialog.add("Continuation Indentation Size:", new JTextField("4", 4));
		dialog.add("Label Indentation:", new JTextField("4", 4));
		dialog.add("", new JCheckBox("Absolute Label Indentation"));
		dialog.add("", new JCheckBox("Indent Top Level Class Member"));
		dialog.endGroup();

		return dialog.getContentPanel();
	}


	private static JComponent wrap(JComponent aComponent)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(aComponent, BorderLayout.CENTER);
		return panel;
	}
}
