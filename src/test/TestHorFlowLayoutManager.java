package test;

import java.awt.Color;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.terifan.ui.Anchor;
import org.terifan.ui.Fill;
import org.terifan.ui.Orientation;
import org.terifan.ui.layout.WrappingHorFlowLayoutManager;
import org.terifan.ui.layout.FlowLayout;


public class TestHorFlowLayoutManager
{
	public static void main(String ... args)
	{
		try
		{
			Random rnd = new Random(1);

			JPanel[] vertPanel = new JPanel[2];

			for (int k = 0; k < 2; k++)
			{
				vertPanel[k] = new JPanel(new FlowLayout(Orientation.HORIZONTAL));
				for (int i = 0; i < 5; i++)
				{
					JPanel howPanel = new JPanel(new WrappingHorFlowLayoutManager(k == 0 ? Anchor.EAST : Anchor.WEST));
					howPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
					for (int j = 0; j < 10; j++)
					{
						howPanel.add(new JLabel(Character.toString('A'+j) + Character.toString('a'+j).repeat(5 + rnd.nextInt(20))));
					}

					vertPanel[k].add(howPanel);
				}
			}

			JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, vertPanel[0], vertPanel[1]);

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
