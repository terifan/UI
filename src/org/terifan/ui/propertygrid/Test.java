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
			PropertyGridModel model = new PropertyGridModel()
				.addProperty(new PropertyList("(General)")
					.addProperty("Text", "value")
					.addProperty(new PropertyList("Icon")
						.addProperty(new Property("Path", "d:\\")
							.setPopupHandler(e->Strings.replaceNull(JOptionPane.showInputDialog("Value", e.getComponentValue()), (String)e.getComponentValue()))
						)
						.addProperty(new PropertyList("Size")
							.addProperty("Width", 32)
							.addProperty("Height", 32)
							.addProperty(new PropertyList("DPI")
								.addProperty("X", 100)
								.addProperty("Y", 100)
							)
							.addProperty("ComboBox", new String[]{"aaa","bbbbb"})
							.addProperty("CheckBox", true)
						)
					)
					.addProperty("Number", 17)
					.addProperty("Color", new ColorChooser("ff800"))
				)
				.addProperty(new PropertyList("Region Settings")
					.addProperty("Language", "Swedish")
					.addProperty("Short Date", "a")
					.addProperty("Long Date", "b")
					.addProperty("Short time", "c")
					.addProperty("Long time", "d")
				);

			PropertyGrid prop1 = new PropertyGrid(model);
			PropertyGrid prop2 = new PropertyGrid(model.clone(), new StyleSheet(PropertyGrid.class, PropertyGrid.class.getResource("resources/stylesheet_dark.json")));

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
