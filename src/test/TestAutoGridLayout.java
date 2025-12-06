package test;

import java.awt.Color;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.terifan.ui.layout.AutoGridLayout;


public class TestAutoGridLayout
{
	public static void main(String ... args)
	{
		try
		{
			JPanel panel = new JPanel(new AutoGridLayout());

			JFrame frame = new JFrame();
			frame.add(panel);
			frame.setSize(1024, 768);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);

			for (int i = 0; i < 24; i++)
			{
				Thread.sleep(2000);

				JLabel lbl = new JLabel("" + i);
				lbl.setBackground(new Color(new Random().nextInt(0xffffff)));
				lbl.setOpaque(true);
				panel.add(lbl);
				panel.invalidate();
				panel.revalidate();
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
