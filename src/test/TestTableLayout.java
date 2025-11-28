package test;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import org.terifan.ui.Orientation;
import org.terifan.ui.layout.FlowLayout;
import org.terifan.ui.layout.TableLayout;


public class TestTableLayout
{
	public static void main(String ... args)
	{
		try
		{
			TableLayout layout = new TableLayout();
			JPanel panel = new JPanel(layout);
			panel.add(new JLabel("Aaaaaaaa1"));
			panel.add(new JLabel("Bbbbbbbb1"));
			layout.advanceRow();
			panel.add(new JLabel("Aaaaaaaa2"));
			panel.add(new JLabel("Bbbbbbbb2"));
			layout.advanceRow();
			panel.add(new JLabel("Aaaaaaaa3"));
			panel.add(new JLabel("Bbbbbbbb3"));
			layout.advanceRow();
			panel.add(new JLabel("Aaaaaaaa4"));
			panel.add(new JLabel("Bbbbbbbb4"));
			layout.advanceRow();
			panel.add(new JLabel("Aaaaaaaa5"));
			panel.add(new JLabel("Bbbbbbbb5"));
			layout.advanceRow();
			panel.add(new JLabel("Aaaaaaaa6"));
			panel.add(new JLabel("Bbbbbbbb6"));
			layout.advanceRow();
			panel.add(new JLabel("Aaaaaaaa7"));
			panel.add(new JLabel("Bbbbbbbb7"));
			layout.advanceRow();
			panel.add(new JLabel("Aaaaaaaa8"));
			panel.add(new JLabel("Bbbbbbbb8"));
			layout.advanceRow();
			panel.add(new JLabel("Aaaaaaaa9"));
			panel.add(new JLabel("Bbbbbbbb9"));
			layout.advanceRow();

			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("title", new JScrollPane(panel));

			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JTextArea(), tabbedPane);

			JFrame frame = new JFrame();
			frame.add(splitPane);
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


	public static void xmain(String... args)
	{
		try
		{
			JPanel panel = createTestTable(0);

			JPanel outer = new JPanel(new FlowLayout(Orientation.HORIZONTAL));
			outer.setBackground(Color.WHITE);
			outer.add(panel);
			outer.add(new JLabel("X"));

			JFrame frame = new JFrame();
//			frame.add(new JScrollPane(panel));
			frame.add(outer);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
			System.exit(0);
		}
	}

	private static int _testCount = 7;


	private static JPanel createTestTable(int aLevel)
	{
		Random rnd = new Random(_testCount++);

		TableLayout layout = new TableLayout(5, 5);

		JPanel panel = new JPanel(layout);
		panel.setBorder(BorderFactory.createLineBorder(new Color(Color.HSBtoRGB(rnd.nextFloat(), 1f, 0.9f)), 10));
		panel.setBackground(new Color(200, 255, 200));

		for (int r = 0, rn = 1 + rnd.nextInt(4); r < rn; r++)
		{
			for (int c = 0, cn = 1 + rnd.nextInt(4); c < cn; c++)
			{
				if (aLevel == 0 && rnd.nextInt(8) == 0)
				{
					panel.add(createTestTable(aLevel + 1));
				}
				else
				{
					JLabel label = new JLabel("<==" + c + "," + r + "==>");
					label.setFont(new Font("segeo ui", Font.PLAIN, 8 + rnd.nextInt(50)));
					label.setBackground(new Color(200, 200, 200));
					label.setBorder(BorderFactory.createLineBorder(new Color(Color.HSBtoRGB(rnd.nextFloat(), 1f, 0.9f)), 10));
					label.setOpaque(true);
					label.addMouseListener(new MouseAdapter()
					{
						@Override
						public void mousePressed(MouseEvent aEvent)
						{
							JComponent comp = (JComponent)aEvent.getSource();
							comp.setBackground(Color.RED);
							comp.setBorder(BorderFactory.createLineBorder(Color.RED, 10));
						}
					});
					panel.add(label);
				}
			}

			layout.advanceRow();
		}

		return panel;
	}
}
