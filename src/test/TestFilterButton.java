package test;

import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.terifan.ui.buttonlist.FilterButton;
import org.terifan.ui.buttonlist.FilterButtonModel;
import org.terifan.ui.buttonlist.FilterButtonModel.State;
import org.terifan.ui.layout.VerticalFlowLayout;


public class TestFilterButton
{
	public static void main(String ... args)
	{
		try
		{
			FilterButtonModel model = new FilterButtonModel();

			Consumer<FilterButton> aAction = button -> {
				FilterButton a = model.getButton("Aaaaaaaaaa");
				FilterButton c = model.getButton("Cccccccccc");
				FilterButton e = model.getButton("Eeeeeeeeee");
				if (model.getState(a) == State.INCLUDE)
				{
					model.replaceState(c, State.NORMAL, State.SHADED);
					model.replaceState(e, State.NORMAL, State.SHADED);
					c.setInfo(null);
					e.setInfo(null);
				}
				else
				{
					model.replaceState(c, State.SHADED, State.NORMAL);
					model.replaceState(e, State.SHADED, State.NORMAL);
					c.setInfo(4);
					e.setInfo(18);
				}
			};

			JPanel panel = new JPanel(new VerticalFlowLayout());
			panel.add(model.add(new FilterButton("Aaaaaaaaaa").setInfo(7).setOnChange(aAction)));
			panel.add(model.add(new FilterButton("Bbbbbbbbbb").setInfo(2).setOnChange(aAction)));
			panel.add(model.add(new FilterButton("Cccccccccc").setInfo(4).setOnChange(aAction)));
			panel.add(model.add(new FilterButton("Dddddddddd").setInfo(11).setOnChange(aAction)));
			panel.add(model.add(new FilterButton("Eeeeeeeeee").setInfo(18).setOnChange(aAction)));

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
