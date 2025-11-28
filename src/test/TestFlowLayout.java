package test;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import org.terifan.ui.Orientation;
import org.terifan.ui.layout.FlowLayout;
import org.terifan.ui.layout.FlowLayout.Wrap;
import org.terifan.ui.layout.VerticalFlowLayout;


public class TestFlowLayout
{
	public static void main(String... args)
	{
		try
		{
			FlowLayout layout = new FlowLayout(Orientation.VERTICAL);
			JPanel panel = new JPanel(layout);
			Random rnd = new Random(2);
			for (int i = 0; i < 20; i++)
			{
				int s = 4 << rnd.nextInt(5);
				ImageIcon icon = new ImageIcon(new BufferedImage(s, s, BufferedImage.TYPE_INT_RGB));
				String text = Character.toString(65 + i) + Character.toString(97 + i).repeat(4 + rnd.nextInt(20));
				JButton btn = new JButton(text, icon);
				btn.setBorder(BorderFactory.createMatteBorder(1, 2, 4, 8, i % 2 == 0 ? Color.RED : Color.CYAN));
				panel.add(btn);
			}

			JPanel controls0 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
			controls0.add(new JLabel("Orientation:"));
			ButtonGroup bg0 = new ButtonGroup();
			for (Orientation a : Orientation.values())
			{
				JRadioButton rb = new JRadioButton(new AbstractAction(a.name())
				{
					@Override
					public void actionPerformed(ActionEvent aEvent)
					{
						layout.setOrientation(a);
						panel.revalidate();
					}
				});
				bg0.add(rb);
				controls0.add(rb);
			}
			JPanel controls1 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
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
			JPanel controls2 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
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
			JPanel controls3 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
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
			JPanel controls4 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
			controls4.add(new JLabel("GapX:"));
			JSlider slider1 = new JSlider(0, 20, 0);
			slider1.addChangeListener(e ->
			{
				layout.getGap().width = slider1.getValue();
				panel.revalidate();
			});
			controls4.add(slider1);
			controls4.add(new JLabel("GapY:"));
			JSlider slider2 = new JSlider(0, 20, 0);
			slider2.addChangeListener(e ->
			{
				layout.getGap().height = slider2.getValue();
				panel.revalidate();
			});
			controls4.add(slider2);
			JPanel controls5 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
			controls5.add(new JLabel("PadX:"));
			JSlider slider3 = new JSlider(-20, 20, 0);
			slider3.addChangeListener(e ->
			{
				layout.getPadding().width = slider3.getValue();
				panel.revalidate();
			});
			controls5.add(slider3);
			controls5.add(new JLabel("PadY:"));
			JSlider slider4 = new JSlider(-20, 20, 0);
			slider4.addChangeListener(e ->
			{
				layout.getPadding().height = slider4.getValue();
				panel.revalidate();
			});
			controls5.add(slider4);
			JPanel controls6 = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
			ButtonGroup bg4 = new ButtonGroup();
			controls6.add(new JLabel("Wrap:"));
			for (Wrap a : Wrap.values())
			{
				JRadioButton rb = new JRadioButton(new AbstractAction(a.name())
				{
					@Override
					public void actionPerformed(ActionEvent aEvent)
					{
						layout.setWrap(a);
						panel.revalidate();
					}
				});
				bg4.add(rb);
				controls6.add(rb);
			}

			JPanel controlsPane = new JPanel(new VerticalFlowLayout(0, Anchor.NORTH_WEST));
			controlsPane.add(controls0);
			controlsPane.add(controls1);
			controlsPane.add(controls2);
			controlsPane.add(controls3);
			controlsPane.add(controls4);
			controlsPane.add(controls5);
			controlsPane.add(controls6);

			JTabbedPane tabbedPane = new JTabbedPane();
//			tabbedPane.addTab("title", new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
			tabbedPane.addTab("title", panel);

			JSplitPane splitPaneVer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(controlsPane), tabbedPane);
			JSplitPane splitPaneHor = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, splitPaneVer, new JLabel("Dummy"));

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
