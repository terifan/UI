package test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import org.terifan.ui.Alignment;
import org.terifan.ui.Anchor;
import org.terifan.ui.Fill;
import org.terifan.ui.layout.VerticalFlowLayout;


public class TestVerticalFlowLayout
{
	public static void main(String... args)
	{
		try
		{
			VerticalFlowLayout layout = new VerticalFlowLayout(0, Anchor.WEST, Fill.NONE);
			JPanel panel = new JPanel(layout);
			for (int i = 0; i < 20; i++)
			{
				panel.add(new JButton(Character.toString(65 + i) + Character.toString(97 + i).repeat(15)));
			}

			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("title", new JScrollPane(panel));

			JPanel controls1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			controls1.add(new JLabel("Anchor:"));
			ButtonGroup bg1 = new ButtonGroup();
			for (Anchor a : Anchor.values())
			{
				JRadioButton rb = new JRadioButton(new AbstractAction(a.name())
				{
					@Override
					public void actionPerformed(ActionEvent aEvent)
					{
						layout.setAnchor(a);
						panel.revalidate();
					}
				});
				bg1.add(rb);
				controls1.add(rb);
			}
			JPanel controls2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			controls2.add(new JLabel("Fill:"));
			ButtonGroup bg2 = new ButtonGroup();
			for (Fill a : Fill.values())
			{
				JRadioButton rb = new JRadioButton(new AbstractAction(a.name())
				{
					@Override
					public void actionPerformed(ActionEvent aEvent)
					{
						layout.setFill(a);
						panel.revalidate();
					}
				});
				bg2.add(rb);
				controls2.add(rb);
			}
			JPanel controls3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			controls3.add(new JLabel("Alignment:"));
			ButtonGroup bg3 = new ButtonGroup();
			for (Alignment a : Alignment.values())
			{
				JRadioButton rb = new JRadioButton(new AbstractAction(a.name())
				{
					@Override
					public void actionPerformed(ActionEvent aEvent)
					{
						layout.setAlignment(a);
						panel.revalidate();
					}
				});
				bg3.add(rb);
				controls3.add(rb);
			}
			JPanel controls4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			controls4.add(new JLabel("Gap:"));
			JSlider slider1 = new JSlider(0, 20, 0);
			slider1.addChangeListener(e ->
			{
				layout.setGap(slider1.getValue());
				panel.revalidate();
			});
			controls4.add(slider1);
			controls4.add(new JLabel("PadX:"));
			JSlider slider2 = new JSlider(-20, 20, 0);
			slider2.addChangeListener(e ->
			{
				layout.getPadding().width = slider2.getValue();
				panel.revalidate();
			});
			controls4.add(slider2);
			controls4.add(new JLabel("PadY:"));
			JSlider slider3 = new JSlider(-20, 20, 0);
			slider3.addChangeListener(e ->
			{
				layout.getPadding().height = slider3.getValue();
				panel.revalidate();
			});
			controls4.add(slider3);

			JPanel controlsPane = new JPanel(new VerticalFlowLayout());
			controlsPane.add(controls1);
			controlsPane.add(controls2);
			controlsPane.add(controls3);
			controlsPane.add(controls4);

			JSplitPane splitPaneVer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(controlsPane), tabbedPane);
			JSplitPane splitPaneHor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, splitPaneVer, new JLabel("Dummy"));

			JFrame frame = new JFrame();
			frame.add(splitPaneHor);
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
