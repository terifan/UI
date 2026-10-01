package test;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import org.terifan.ui.Orientation;
import org.terifan.ui.layout.Box;


public class TestBox
{
	public static void main(String... args)
	{
		try
		{
			Box b1 = new Box(Orientation.HORIZONTAL, new JButton("Aaa"), new JButton("Bbbbb"));
			Box b2 = new Box(Orientation.HORIZONTAL, new JButton("Ccccc"), new JButton("Dddd"), new JButton("Eeeee"));
			Box b3 = new Box(Orientation.HORIZONTAL, new JButton("Fffffff"), new JButton("Gggg"));
			Box b4 = new Box(Orientation.HORIZONTAL, new JButton("Hhhhhh"), new JButton("Iiiii"));
			b1.setBorder(BorderFactory.createMatteBorder(4, 0, 0, 0, Color.CYAN));
			b2.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
			b3.setBorder(BorderFactory.createMatteBorder(0, 4, 0, 0, Color.MAGENTA));
			b4.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, Color.GREEN));

			Box panel = new Box(Orientation.VERTICAL, b1, b2, b3, b4);

			JSplitPane splitPaneHor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, panel, new JLabel("Dummy"));

			JFrame frame = new JFrame();
			frame.add(splitPaneHor);
			frame.setSize(1024, 1200);
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
