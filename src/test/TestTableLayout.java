package test;

import java.awt.Color;
import java.awt.Insets;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.terifan.ui.Anchor;
import org.terifan.ui.Fill;
import org.terifan.ui.Orientation;
import org.terifan.ui.layout.TableLayout;


public class TestTableLayout
{
	public static void main(String... args)
	{
		try
		{
			TableLayout.LayoutParams labelParams = new TableLayout.LayoutParams().setAnchor(Anchor.EAST).setMargins(new Insets(0, 0, 8, 0));
			TableLayout.LayoutParams inputParams = new TableLayout.LayoutParams().setAnchor(Anchor.WEST);
			TableLayout layout = new TableLayout(Orientation.VERTICAL, 0f, 1f);

			JPanel panel = new JPanel(layout);
			panel.add(label("Title"), new TableLayout.LayoutParams().setAnchor(Anchor.CENTER).setColSpan(2));
			panel.add(label("Aaaaaaaa1"), labelParams);
			panel.add(new JTextField("Bbbbbbbb1"), inputParams);
			panel.add(label("Aaaaaaaa2"), labelParams);
			panel.add(new JTextField("Bbbbbbbb2"), inputParams);
			panel.add(label("Aaaaaaaa3"), labelParams);
			panel.add(new JTextField("Bbbbbbbb3"), inputParams);
			panel.add(label("Aaaaaaaa4"), labelParams);
			panel.add(new JTextField("Bbbbbbbb4"), inputParams);
			panel.add(label("Aaaaaaaa5"), labelParams);
			panel.add(new JTextField("Bbbbbbbb5"), inputParams);
			panel.add(label("Aaaaaaaa6"), labelParams);
			panel.add(new JTextField("Bbbbbbbb6"), inputParams);
			panel.add(label("Aaaaaaaa7"), labelParams);
			panel.add(new JTextField("Bbbbbbbb7"), inputParams);
			panel.add(label("Aaaaaaaa8"), labelParams);
			panel.add(new JTextField("Bbbbbbbb8"), inputParams);
			panel.add(label("Aaaaaaaa9"), labelParams);
			panel.add(new JTextField("Bbbbbbbb9"), inputParams);

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


	private static JLabel label(String aText)
	{
		JLabel label = new JLabel(aText);
		label.setOpaque(true);
		label.setBackground(new Color(new Random().nextInt(0xffffff)));
		return label;
	}

//	public static void xmain(String... args)
//	{
//		try
//		{
//			JPanel panel = createTestTable(0);
//
//			JPanel outer = new JPanel(new FlowLayout(Orientation.HORIZONTAL));
//			outer.setBackground(Color.WHITE);
//			outer.add(panel);
//			outer.add(new JLabel("X"));
//
//			JFrame frame = new JFrame();

////			frame.add(new JScrollPane(panel));
//			frame.add(outer);
//			frame.pack();
//			frame.setLocationRelativeTo(null);
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.setVisible(true);
//			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace(System.out);
//			System.exit(0);
//		}
//	}
//
//	private static int _testCount = 7;
//
//
//	private static JPanel createTestTable(int aLevel)
//	{
//		Random rnd = new Random(_testCount++);
//
//		TableLayout layout = new TableLayout(5, 5);
//
//		JPanel panel = new JPanel(layout);
//		panel.setBorder(BorderFactory.createLineBorder(new Color(Color.HSBtoRGB(rnd.nextFloat(), 1f, 0.9f)), 10));
//		panel.setBackground(new Color(200, 255, 200));
//
//		for (int r = 0, rn = 1 + rnd.nextInt(4); r < rn; r++)
//		{
//			for (int c = 0, cn = 1 + rnd.nextInt(4); c < cn; c++)
//			{
//				if (aLevel == 0 && rnd.nextInt(8) == 0)
//				{
//					panel.add(createTestTable(aLevel + 1));
//				}
//				else
//				{
//					JLabel label = new JLabel("<==" + c + "," + r + "==>");
//					label.setFont(new Font("segeo ui", Font.PLAIN, 8 + rnd.nextInt(50)));
//					label.setBackground(new Color(200, 200, 200));
//					label.setBorder(BorderFactory.createLineBorder(new Color(Color.HSBtoRGB(rnd.nextFloat(), 1f, 0.9f)), 10));
//					label.setOpaque(true);
//					label.addMouseListener(new MouseAdapter()
//					{
//						@Override
//						public void mousePressed(MouseEvent aEvent)
//						{
//							JComponent comp = (JComponent)aEvent.getSource();
//							comp.setBackground(Color.RED);
//							comp.setBorder(BorderFactory.createLineBorder(Color.RED, 10));
//						}
//					});
//					panel.add(label);
//				}
//			}
//
//			layout.advanceRow();
//		}
//
//		return panel;
//	}
}
