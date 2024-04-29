package test;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import org.terifan.ui.layout.WrappingHorFlowLayoutManager;
import org.terifan.ui.layout.VerticalFlowLayout;


public class TestHorFlowLayoutManager
{
	public static void main(String ... args)
	{
		try
		{
			JPanel panel1 = new JPanel(new WrappingHorFlowLayoutManager());
			panel1.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
			panel1.add(new JLabel("Aaaaaaaa1"));
			panel1.add(new JLabel("Aaaaaaaa2"));
			panel1.add(new JLabel("Aaaaaaaa3"));
			panel1.add(new JLabel("Aaaaaaaa4"));
			panel1.add(new JLabel("Aaaaaaaa5"));
			panel1.add(new JLabel("Aaaaaaaa6"));
			panel1.add(new JLabel("Aaaaaaaa7"));
			panel1.add(new JLabel("Aaaaaaaa8"));
			panel1.add(new JLabel("Aaaaaaaa9"));

			JPanel panel2 = new JPanel(new VerticalFlowLayout());
			panel2.add(panel1);

			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JTextArea(), panel2);

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
