package org.terifan.ui.tree;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
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
			BufferedImage icons = ImageIO.read(TreeNode.class.getResource("icons.png"));
			BufferedImage iconA = icons.getSubimage(16, 16, 16, 16);

			TreeNode n0 = new TreeNode("Aaaaaa", iconA);
			TreeNode n1 = new TreeNode("Bbbbbbbb", iconA);
			TreeNode n2 = new TreeNode("Cccc", iconA);
			TreeNode n3 = new TreeNode("Ddddd", iconA);
			TreeNode n4 = new TreeNode("Eeee", iconA);
			TreeNode n5 = new TreeNode("Fffffff", iconA);
			TreeNode n6 = new TreeNode("Ggggggg", iconA);
			TreeNode n7 = new TreeNode("Hhhhhhh", iconA).setRowHeight(50);
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
			tree.addColumn(new Column("Key").setWidth(400));
			tree.addColumn(new Column("Value").setWidth(200));
			tree.addColumn(new Column("Type"));
			tree.setRoot(n0);
			tree.setPaintIndentLines(true);
			tree.setPaintRootNode(false);

			JPanel panel = new JPanel(new BorderLayout());
			panel.add(tree, BorderLayout.CENTER);

			JFrame frame = new JFrame();
			frame.add(new JScrollPane(panel));
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
