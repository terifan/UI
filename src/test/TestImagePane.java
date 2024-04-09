package test;

import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.terifan.ui.ImagePane;


public class TestImagePane
{
	public static void main(String ... args)
	{
		try
		{
			ImagePane imagePane = new ImagePane();
			imagePane.setImage(ImageIO.read(new File("D:\\Pictures\\4k-daenerys-targaryen.jpg")));

			JFrame frame = new JFrame();
			frame.add(imagePane);
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
