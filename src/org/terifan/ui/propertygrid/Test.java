package org.terifan.ui.propertygrid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.function.Function;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.terifan.ui.Utilities;


public class Test
{
	public static void main(String ... args)
	{
		try
		{
			Utilities.setSystemLookAndFeel();

			PropertyGridModel model = new PropertyGridModel()
				.addProperty(new PropertyList(true, "(General)")
					.addProperty("Text", "value")
					.addProperty(new PropertyList("Icon")
						.addProperty(new TextProperty("Path", "d:\\").setFunction(e->JOptionPane.showInputDialog("Value", e.getValue())))
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
					.addProperty("Color", new Color(0xff8000))
				)
				.addProperty(new PropertyList(true, "Region Settings")
					.addProperty("Language", "Swedish")
					.addProperty("Short Date", "a")
					.addProperty("Long Date", "b")
					.addProperty("Short time", "c")
					.addProperty("Long time", "d")
				)
				;

			System.out.println(model.marshal().marshalJSON(false));

			PropertyGrid prop1 = new PropertyGrid(model);
//			PropertyGrid prop2 = new PropertyGrid(model.clone(), new StyleSheet(PropertyGrid.class, PropertyGrid.class.getResource("resources/stylesheet_dark.json")));

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try (ObjectOutputStream oos = new ObjectOutputStream(baos))
			{
				oos.writeObject(model);
			}
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
			PropertyGridModel modelCopy = (PropertyGridModel)ois.readObject();
			Function<Property, Function<Property, Object>> funcFact = e->{
				if (e.getLabel().equals("Path"))
				{
					return prop->JOptionPane.showInputDialog("Value", prop.getValue());
				}
				return null;
			};
			PropertyGrid prop2 = new PropertyGrid(modelCopy, funcFact, new StyleSheet(PropertyGrid.class, PropertyGrid.class.getResource("resources/stylesheet_dark.json")));

			JPanel panel = new JPanel(new BorderLayout());
			panel.add(prop1, BorderLayout.NORTH);
			panel.add(prop2, BorderLayout.SOUTH);

			JFrame frame = new JFrame();
			frame.add(panel);
			frame.setSize(400, 800);
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
