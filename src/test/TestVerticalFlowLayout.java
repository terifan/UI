package test;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import org.terifan.ui.layout.VerticalFlowLayout;


public class TestVerticalFlowLayout
{
	public static void main(String ... args)
	{
		try
		{
			JPanel panel = new JPanel(new VerticalFlowLayout());
			panel.add(new JLabel("Aaaaaaaa1"));
			panel.add(new JLabel("Aaaaaaaa2"));
			panel.add(new JLabel("Aaaaaaaa3"));
			panel.add(new JLabel("Aaaaaaaa4"));
			panel.add(new JLabel("Aaaaaaaa5"));
			panel.add(new JLabel("Aaaaaaaa6"));
			panel.add(new JLabel("Aaaaaaaa7"));
			panel.add(new JLabel("Aaaaaaaa8"));
			panel.add(new JLabel("Aaaaaaaa9"));

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
}
