package org.terifan.ui.propertygrid;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.terifan.util.Strings;


public class Test
{
	public static void main(String ... args)
	{
		try
		{
			PropertyGridModel model1 = new PropertyGridModel();
			model1.add(new Property("Text", "value"));
			model1.add(new Property("Icon", new PropertyList()
					.add(new Property("Path", "d:\\")
						.setValueProducer(e->Strings.replaceNull(JOptionPane.showInputDialog("Value", e.getComponentValue()), (String)e.getComponentValue()))
					)
					.add(new Property("Size", new PropertyList()
						.add(new Property("Width", 32))
						.add(new Property("Height", 32))
						.add(new Property("DPI", new PropertyList()
							.add(new Property("X", 100))
							.add(new Property("Y", 100))
						))
						.add(new Property("ComboBox", new String[]{"aaa","bbbbb"}))
						.add(new Property("CheckBox", true))
					))
				)
			);
			model1.add(new Property("Number", 17));
			model1.add(new Property("Color", new ColorChooser("ff800")));
			model1.add(new Property("Region Settings",
				new PropertyList()
					.add(new Property("Language", "Swedish"))
					.add(new Property("Short Date", "a"))
					.add(new Property("Long Date", "b"))
					.add(new Property("Short time", "c"))
					.add(new Property("Long time", "d"))
				)
				.setGroup(true)
			);

			PropertyGrid prop1 = new PropertyGrid(model1);
			PropertyGrid prop2 = new PropertyGrid(model1.clone(), new StyleSheet(PropertyGrid.class, PropertyGrid.class.getResource("resources/stylesheet_dark.json")));

			JPanel panel = new JPanel(new BorderLayout());
			panel.add(prop1, BorderLayout.NORTH);
			panel.add(prop2, BorderLayout.SOUTH);

			JFrame frame = new JFrame();
			frame.add(panel);
			frame.setSize(400, 768);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
