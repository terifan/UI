package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.terifan.ui.Anchor;
import org.terifan.ui.TextBox;


public class TestTextBox
{
	public static void main(String... args)
	{
		try
		{
			JPanel panel1 = new JPanel()
			{
				@Override
				protected void paintComponent(Graphics aGraphics)
				{
					aGraphics.setColor(Color.WHITE);
					aGraphics.fillRect(0, 0, getWidth(), getHeight());

					TextBox box = new TextBox()
						.setForeground(Color.BLACK)
						.setBounds(0, 0, getWidth(), getHeight())
						.setBorder(BorderFactory.createMatteBorder(2, 4, 6, 8, Color.YELLOW))
						.setTextBorder(BorderFactory.createMatteBorder(2, 4, 6, 8, Color.PINK))
						.setHighlight(Color.CYAN)
						.setPadding(2, 4, 6, 8)
						.setShadow(Color.WHITE, 1, 1);

					box.setAnchor(Anchor.NORTH_WEST).setText("text anchored NORTH_WEST").render(aGraphics, true);
					box.setAnchor(Anchor.NORTH).setText("text anchored NORTH").render(aGraphics, true);
					box.setAnchor(Anchor.NORTH_EAST).setText("text anchored NORTH_EAST").render(aGraphics, true);
					box.setAnchor(Anchor.WEST).setText("text anchored WEST").render(aGraphics, true);
					box.setAnchor(Anchor.CENTER).setText("text anchored CENTER").render(aGraphics, true);
					box.setAnchor(Anchor.EAST).setText("text anchored EAST").render(aGraphics, true);
					box.setAnchor(Anchor.SOUTH_WEST).setText("text anchored SOUTH_WEST").render(aGraphics, true);
					box.setAnchor(Anchor.SOUTH).setText("text anchored SOUTH").render(aGraphics, true);
					box.setAnchor(Anchor.SOUTH_EAST).setText("text anchored SOUTH_EAST").render(aGraphics, true);
				}
			};

			JPanel panel2 = new JPanel()
			{
				@Override
				protected void paintComponent(Graphics aGraphics)
				{
					aGraphics.setColor(Color.WHITE);
					aGraphics.fillRect(0, 0, getWidth(), getHeight());

					TextBox box = new TextBox()
						.setForeground(Color.BLACK)
						.setBounds(0, 0, getWidth(), getHeight())
						.setBorder(BorderFactory.createMatteBorder(2, 4, 6, 8, Color.YELLOW))
						.setTextBorder(BorderFactory.createMatteBorder(2, 4, 6, 8, Color.PINK))
						.setHighlight(Color.CYAN)
						.setPadding(2, 4, 6, 8)
						.setShadow(Color.WHITE, 1, 1);

					box.setAnchor(Anchor.CENTER).setText("testing\ntesting\ntesting").render(aGraphics, true);
				}
			};

			JPanel panel = new JPanel(new GridLayout(2, 1));
			panel.add(panel1);
			panel.add(panel2);

			JFrame frame = new JFrame();
			frame.add(panel);
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
