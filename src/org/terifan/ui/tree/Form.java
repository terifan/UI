package org.terifan.ui.tree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Form extends JPanel
{
	public static void main(String ... args)
	{
		try
		{
			Tree test1 = test1();
			Tree test2 = test2();
			Tree test3 = test3();

			JPanel panel = new JPanel(new GridLayout(1, 3, 20, 20));
			panel.add(new JScrollPane(test1));
			panel.add(new JScrollPane(test2));
			panel.add(new JScrollPane(test3));

			JFrame frame = new JFrame();
			frame.addWindowFocusListener(new WindowFocusListener()
			{
				@Override
				public void windowGainedFocus(WindowEvent aEvent)
				{
					test1.mWindowFocused = true;
					test2.mWindowFocused = true;
					test3.mWindowFocused = true;
					frame.repaint();
				}


				@Override
				public void windowLostFocus(WindowEvent aEvent)
				{
					test1.mWindowFocused = false;
					test2.mWindowFocused = false;
					test3.mWindowFocused = false;
					frame.repaint();
				}
			});
			frame.add(panel);
			frame.setSize(1600, 768);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}


	public static Tree test1() throws IOException
	{
		BufferedImage icons = ImageIO.read(TreeNode.class.getResource("icons.png"));
//		BufferedImage iconA = icons.getSubimage(16, 16, 16, 16);
		BufferedImage iconA = null;

		TreeNode n0 = new TreeNode("Aaaaaa", iconA);
		TreeNode n1 = new TreeNode("Bbbbbbbb", iconA);
		TreeNode n2 = new TreeNode("Cccc", iconA);
		TreeNode n3 = new TreeNode("Ddddd", iconA);
		TreeNode n4 = new TreeNode("Eeee", iconA);
		TreeNode n5 = new TreeNode("Fffffff", iconA);
		TreeNode n6 = new TreeNode("Ggggggg", iconA);
		TreeNode n7 = new TreeNode("Hhhhhhh", iconA);
		TreeNode n8 = new TreeNode("Iiiiiii", iconA);
		TreeNode n9 = new TreeNode("Jjjjj", iconA);
		TreeNode n10 = new TreeNode("Kkkkkkkk", iconA);
		TreeNode n11 = new TreeNode("Llllllll", iconA);
		TreeNode n12 = new TreeNode("Mmmmmmm", iconA);
		TreeNode n13 = new TreeNode("Nnnnnnnn", iconA);
		TreeNode n14 = new TreeNode("Ooooooo", iconA);
		TreeNode n15 = new TreeNode("Ppppppp", iconA);
		TreeNode n16 = new TreeNode("Qqqqqqq", iconA);
		TreeNode n17 = new TreeNode("Rrrrrrr", iconA);
		TreeNode n18 = new TreeNode("Sss", iconA);
		TreeNode n19 = new TreeNode("Tttttttttt", iconA);
		n0.add(n1);
		n0.add(n2);
		n0.add(n15);
		n11.add(n16);
		n15.add(n17);
		n15.add(n18);
		n15.add(n19);
		n1.add(n3);
		n1.add(n4);
		n1.add(n10);
		n1.add(n11);
		n1.add(n5);
		n2.add(n6);
		n2.add(n12);
		n2.add(n7);
		n6.add(n8);
		n6.add(n13);
		n0.add(n9);
		n0.add(n14);

		Tree tree = new Tree();
		tree.addColumn(new Column("Key").setWidth(200));
		tree.addColumn(new Column("Value").setWidth(100));
		tree.addColumn(new Column("Type"));
		tree.setRoot(n0);
//		tree.setPaintHorizontalLines(true);
//		tree.setPaintVerticalLines(true);
//		tree.setPaintIndentLines(true);
//		tree.setPaintRootNode(false);
//		tree.setPaintHeaderRow(false);
		tree.setIndentWidth(20);
		tree.setIconWidth(20);
		tree.setIconTextSpacing(4);

		return tree;
	}


	public static Tree test2() throws IOException
	{
		BufferedImage icons = ImageIO.read(TreeNode.class.getResource("icons.png"));
		BufferedImage iconA = icons.getSubimage(16, 16, 16, 16);

		TreeNode n0 = new TreeNode("Aaaaaa", iconA);
		TreeNode n1 = new TreeNode("Bbbbbbbb", iconA).setSelectable(false).setFont(new Font("arial", Font.BOLD, 12));
		TreeNode n2 = new TreeNode("Cccc", iconA).setSelectable(false).setFont(new Font("arial", Font.BOLD, 12));
		TreeNode n3 = new TreeNode("Ddddd", iconA);
		TreeNode n4 = new TreeNode("Eeee", iconA);
		TreeNode n5 = new TreeNode("Fffffff", iconA);
		TreeNode n6 = new TreeNode("Ggggggg", iconA);
		TreeNode n7 = new TreeNode("Hhhhhhh", iconA);
		TreeNode n8 = new TreeNode("Iiiiiii", iconA);
		TreeNode n9 = new TreeNode("Jjjjj", iconA);
		TreeNode n10 = new TreeNode("Kkkkkkkk", iconA);
		TreeNode n11 = new TreeNode("Llllllll", iconA);
		TreeNode n12 = new TreeNode("Mmmmmmm", iconA);
		TreeNode n13 = new TreeNode("Nnnnnnnn", iconA);
		TreeNode n14 = new TreeNode("Ooooooo", iconA);
		TreeNode n15 = new TreeNode("Ppppppp", iconA).setSelectable(false).setFont(new Font("arial", Font.BOLD, 12));
		TreeNode n16 = new TreeNode("Qqqqqqq", iconA);
		TreeNode n17 = new TreeNode("Rrrrrrr", iconA);
		TreeNode n18 = new TreeNode("Sss", iconA);
		TreeNode n19 = new TreeNode("Tttttttttt", iconA);
		n0.add(n1);
		n0.add(n2);
		n0.add(n15);
		n11.add(n16);
		n15.add(n17);
		n15.add(n18);
		n15.add(n19);
		n1.add(n3);
		n1.add(n4);
		n1.add(n10);
		n1.add(n11);
		n1.add(n5);
		n2.add(n6);
		n2.add(n12);
		n2.add(n7);
		n6.add(n8);
		n6.add(n13);
		n0.add(n9);
		n0.add(n14);

		Tree tree = new Tree();
		tree.addColumn(new Column("Key").setWidth(200));
		tree.addColumn(new Column("Value").setWidth(100));
		tree.addColumn(new Column("Type"));
		tree.setRoot(n0);
//		tree.setPaintHorizontalLines(true);
//		tree.setPaintVerticalLines(true);
		tree.setPaintIndentLines(true);
//		tree.setPaintRootNode(false);
//		tree.setPaintHeaderRow(false);
		tree.setIndentWidth(20);
		tree.setIconWidth(20);
		tree.setIconTextSpacing(4);

		return tree;
	}


	public static Tree test3()
	{
		TreeNode n0 = new TreeNode("Aaaaaa");
		TreeNode n1 = new TreeNode("Bbbbbbbb").setRowBackground(new Color(0xF4F7FC)).setSelectable(false).setFont(new Font("arial", Font.BOLD, 12));
		TreeNode n2 = new TreeNode("Cccc").setRowBackground(new Color(0xF4F7FC)).setSelectable(false).setFont(new Font("arial", Font.BOLD, 12));
		TreeNode n3 = new TreeNode("Ddddd");
		TreeNode n4 = new TreeNode("Eeee");
		TreeNode n5 = new TreeNode("Fffffff");
		TreeNode n6 = new TreeNode("Ggggggg");
		TreeNode n7 = new TreeNode("Hhhhhhh");
		TreeNode n8 = new TreeNode("Iiiiiii");
		TreeNode n9 = new TreeNode("Jjjjj");
		TreeNode n10 = new TreeNode("Kkkkkkkk");
		TreeNode n11 = new TreeNode("Llllllll");
		TreeNode n12 = new TreeNode("Mmmmmmm");
		TreeNode n13 = new TreeNode("Nnnnnnnn");
		TreeNode n14 = new TreeNode("Ooooooo");
		TreeNode n15 = new TreeNode("Ppppppp").setRowBackground(new Color(0xF4F7FC)).setSelectable(false).setFont(new Font("arial", Font.BOLD, 12));
		TreeNode n16 = new TreeNode("Qqqqqqq");
		TreeNode n17 = new TreeNode("Rrrrrrr");
		TreeNode n18 = new TreeNode("Sss");
		TreeNode n19 = new TreeNode("Tttttttttt");
		n0.add(n1);
		n0.add(n2);
		n0.add(n15);
		n11.add(n16);
		n15.add(n17);
		n15.add(n18);
		n15.add(n19);
		n1.add(n3);
		n1.add(n4);
		n1.add(n10);
		n1.add(n11);
		n1.add(n5);
		n2.add(n6);
		n2.add(n12);
		n2.add(n7);
		n6.add(n8);
		n6.add(n13);
		n0.add(n9);
		n0.add(n14);

		Tree tree = new Tree();
		tree.addColumn(new Column("Key").setWidth(200));
		tree.addColumn(new Column("Value"));
		tree.setRoot(n0);
		tree.setPaintHorizontalLines(true);
		tree.setPaintVerticalLines(true);
//		tree.setPaintIndentLines(true);
		tree.setPaintRootNode(false);
//		tree.setPaintHeaderRow(false);
		tree.setIndentWidth(20);
		tree.setIndentBackgroundColor(1, new Color(0xF4F7FC));
		tree.setIconWidth(20);
		tree.setIconTextSpacing(4);

		return tree;
	}
}
