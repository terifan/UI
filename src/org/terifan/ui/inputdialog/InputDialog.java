package org.terifan.ui.inputdialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;


/**
 * The InputDialog is an easy to use dialog that allow custom fields to be added in groups and columns.
 */
public class InputDialog
{
	public final static int OK_OPTION = 1;
	public final static int CANCEL_OPTION = 0;

	private JDialog mDialog;
	private String mTitle;
	private String mSubtitle;
	private JPanel mContentPanel;
	private JPanel mCurrentGroup;
	private JPanel[] mColumns;
	private int mColumnIndex;
	private boolean mInsideColumn;
	private boolean mInsideGroup;
	private boolean mIsFinished;
	private JCheckBox mGroupEnabler;
	private int mOption;


	public InputDialog(Frame aFrame, String aTitle)
	{
		this(aFrame, aTitle, null, null);
	}


	public InputDialog(Frame aFrame, String aTitle, String aSubtitle)
	{
		this(aFrame, aTitle, aTitle, aSubtitle);
	}


	public InputDialog(Component aParent, String aWindowTitle, String aTitle, String aSubtitle)
	{
		Window window = aParent == null ? null : SwingUtilities.windowForComponent(aParent);
		if (window instanceof Frame)
		{
			mDialog = new JDialog((Frame)window, aWindowTitle, true);
		}
		else
		{
			mDialog = new JDialog((Dialog)window, aWindowTitle, true);
		}

		mDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		mDialog.setLayout(new GridBagLayout());

		mContentPanel = new JPanel(new GridBagLayout());
		mCurrentGroup = mContentPanel;

		mTitle = aTitle;
		mSubtitle = aSubtitle;
	}


	/**
	 * Adds a row to the InputDialog with a field label and one or more fields.
	 *
	 * Note: JTextArea components should be added without a JScrollPane for best result.
	 *
	 * @param aLabel the field label
	 * @param aComponent the components that will be associated with the field
	 */
	public void add(String aLabel, JComponent... aComponent)
	{
		add(aLabel, null, aComponent);
	}


	public void add(String aLabel, ButtonGroup aButtonGroup, JComponent... aComponent)
	{
		GridBagConstraints c;

		if (aLabel != null)
		{
			JPanel panel = new JPanel(new GridBagLayout());
			JLabel label = new JLabel(aLabel);

			c = new GridBagConstraints();
			c.anchor = GridBagConstraints.NORTHEAST;
			c.gridx = 0;
			c.insets = new Insets(3, 10, 0, 5);
			panel.add(label, c);

			c.anchor = GridBagConstraints.NORTHEAST;
			c.gridx = 0;
			c.insets = new Insets(0, 0, 3, 0);
			mCurrentGroup.add(panel, c);

			if (mInsideGroup && mGroupEnabler != null && !mGroupEnabler.isSelected())
			{
				label.setEnabled(false);
			}
		}

		JPanel panel = new JPanel(new GridBagLayout());

		ButtonGroup group = aButtonGroup;
		if (aButtonGroup == null && aComponent[0] instanceof JRadioButton)
		{
			group = new ButtonGroup();
		}

		boolean hasWeightedComponent = false;

		for (int i = 0; i < aComponent.length; i++)
		{
			JComponent comp = aComponent[i];

			if (group != null)
			{
				group.add((JRadioButton)comp);
			}

			c = new GridBagConstraints();
			if (comp instanceof JTextArea)
			{
				c.weightx = 1;
				c.fill = GridBagConstraints.BOTH;
				c.ipady = 50;

				comp = new JScrollPane(comp);
				hasWeightedComponent = true;
			}
			else if ((comp instanceof JTextComponent) || (comp instanceof JPanel) || (comp instanceof JScrollPane))
			{
				c.weightx = 1;
				c.fill = GridBagConstraints.HORIZONTAL;
				hasWeightedComponent = true;
			}

			c.insets = new Insets(0, 0, 3, 5);
			c.anchor = GridBagConstraints.WEST;
			c.gridx = i;
			c.gridy = 0;

			panel.add(comp, c);

			if (mInsideGroup && mGroupEnabler != null && !mGroupEnabler.isSelected())
			{
				comp.setEnabled(false);
			}
		}

		// pad panel so that components are pushed west
		if (!hasWeightedComponent)
		{
			c = new GridBagConstraints();
			c.weightx = 1;
			c.gridx = aComponent.length;
			panel.add(new JLabel(), c);
		}

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 1;
		mCurrentGroup.add(panel, c);

		// swap column
		if (mInsideColumn && !mInsideGroup)
		{
			mColumnIndex = 1 - mColumnIndex;
			mCurrentGroup = mColumns[mColumnIndex];
		}
	}


	/**
	 * Begins a group of components.
	 *
	 * Note: Groups can not contain other groups.
	 *
	 * @param aTitle if specified then a titled border will surround the group. If null then the group will be slightly indented.
	 */
	public void beginGroup(String aTitle)
	{
		if (mInsideGroup)
		{
			throw new IllegalStateException("A group can't contain other groups.");
		}

		beginGroup(aTitle, null, true, true);
	}


	/**
	 * Begins a group of components.
	 *
	 * Note: Groups can not contain other groups.
	 *
	 * @param aTitle if specified then a titled border will surround the group. If null then the group will be slightly indented.
	 * @param aEnableCheckBox a JCheckBox component that will be used to enable/disable the group and all it's child components. The
	 * CheckBox is added before the group.
	 */
	public void beginGroup(String aTitle, boolean aBorder)
	{
		beginGroup(aTitle, null, aBorder, true);
	}

	public void beginGroup(String aTitle, JCheckBox aEnableCheckBox)
	{
		beginGroup(aTitle, aEnableCheckBox, true, true);
	}

	public void beginGroup(String aTitle, JCheckBox aEnableCheckBox, boolean aBorder, boolean aIndent)
	{
		JPanel group = new JPanel(new GridBagLayout());
//		group.setBackground(Color.blue);

		if (aBorder && aTitle != null && !aTitle.isEmpty())
		{
			group.setBorder(BorderFactory.createTitledBorder(aTitle));
		}
		else
		{
			group.setBorder(null);
//			group.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 5));
		}

		GridBagConstraints c, d;

		// add enabler checkbox
		if (aEnableCheckBox != null)
		{
			d = new GridBagConstraints();
			d.gridx = 0;

			c = new GridBagConstraints();
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;

			group.setEnabled(aEnableCheckBox.isSelected());
			mGroupEnabler = aEnableCheckBox;
			aEnableCheckBox.addActionListener(new GroupEnableTriggered(group));

			if (mInsideColumn)
			{
				mColumns[mColumnIndex].add(new JLabel(""), d);
				mColumns[mColumnIndex].add(mGroupEnabler, c);
			}
			else
			{
				mContentPanel.add(new JLabel(""), d);
				mContentPanel.add(mGroupEnabler, c);
			}
		}

		if (mInsideColumn)
		{
			// add group panel
			d = new GridBagConstraints();
			d.gridx = 0;

			c = new GridBagConstraints();
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;

			if (!aIndent)
			{
				d.gridwidth = 2;
				d.weightx = 1;
				d.fill = GridBagConstraints.HORIZONTAL;

				mColumns[mColumnIndex].add(group, d);
			}
			else if (!aBorder)
			{
				d.anchor = GridBagConstraints.NORTHWEST;
				d.insets = new Insets(3, 10, 0, 5);

				mColumns[mColumnIndex].add(new JLabel(aTitle), d);
				mColumns[mColumnIndex].add(group, c);
			}
			else
			{
				mColumns[mColumnIndex].add(new JLabel(""), d);
				mColumns[mColumnIndex].add(group, c);
			}

			mColumnIndex = 1 - mColumnIndex;
		}
		else
		{
			// add group panel
			d = new GridBagConstraints();
			d.gridx = 0;

			c = new GridBagConstraints();
			c.anchor = GridBagConstraints.NORTHWEST;
			c.gridx = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1;

			mContentPanel.add(new JLabel(""), d);
			mContentPanel.add(group, c);
		}
		mCurrentGroup = group;
		mInsideGroup = true;
	}


	/**
	 * Ends the current group.
	 */
	public void endGroup()
	{
		mGroupEnabler = null;
		mInsideGroup = false;
		if (mInsideColumn)
		{
			mCurrentGroup = mColumns[mColumnIndex];
		}
		else
		{
			mCurrentGroup = mContentPanel;
		}
	}


	/**
	 * Begins column mode. Every odd indexed component added then in column mode will be added to the left column and evenly indexed to the
	 * right column.
	 *
	 * Note: columns can not contain other columns.
	 */
	public void beginColumns()
	{
		if (mInsideGroup)
		{
			throw new IllegalStateException("Columns can not be used inside a group.");
		}
		if (mInsideColumn)
		{
			throw new IllegalStateException("Already inside a column.");
		}

		mColumns = new JPanel[]
		{
			new JPanel(new GridBagLayout()),
			new JPanel(new GridBagLayout())
		};

//		mColumns[0].setBackground(Color.red);
//		mColumns[1].setBackground(Color.green);
		GridBagConstraints c;

		JPanel panel = new JPanel(new GridBagLayout());

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		panel.add(mColumns[0], c);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		panel.add(mColumns[1], c);

		c = new GridBagConstraints();
		c.gridx = 0;
		mContentPanel.add(new JLabel(""), c);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		mContentPanel.add(panel, c);

		mColumnIndex = 0;
		mCurrentGroup = mColumns[0];
		mInsideColumn = true;
	}


	/**
	 * Ends column mode.
	 */
	public void endColumns()
	{
		mCurrentGroup = mContentPanel;
		mColumns = null;
		mInsideColumn = false;
	}


	/**
	 * Begins a new section.
	 *
	 * @param aLabel the section label
	 */
	public void addSection(String aLabel)
	{
		GridBagConstraints c;

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 2;
		c.insets = new Insets(0, 0, 3, 0);
		mCurrentGroup.add(new SectionHeader(aLabel), c);
	}


	/**
	 * Create the buttons and title.
	 */
	private void finish()
	{
		GridBagConstraints c;

		// setup title panel
		JPanel titlePanel = null;
		if (mTitle != null)
		{
			titlePanel = new JPanel(new GridBagLayout());
			titlePanel.setBackground(Color.WHITE);
			titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, titlePanel.getBackground().darker()));

			c = new GridBagConstraints();
			c.weightx = 1;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(10, 5, 2, 0);
			JLabel label = new JLabel(mTitle);
			label.setFont(label.getFont().deriveFont(Font.BOLD));
			titlePanel.add(label, c);

			c = new GridBagConstraints();
			c.weightx = 1;
			c.anchor = GridBagConstraints.WEST;
			c.gridx = 0;
			c.gridy = 1;
			c.insets = new Insets(0, 20, 10, 0);
			titlePanel.add(new JLabel(mSubtitle == null ? "" : mSubtitle), c);

			titlePanel.setBorder(new BevelBorder(true));
		}

		// setup button panel
		JPanel buttonPanel = new JPanel(new GridBagLayout());

		c = new GridBagConstraints();
		c.weightx = 1;
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.gridx = 0;
		c.gridy = 0;
		c.ipadx = 25;
		c.insets = new Insets(0, 0, 0, 3);
		buttonPanel.add(new JButton(new ButtonAction(this, "OK", OK_OPTION)), c);

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.SOUTH;
		c.gridx = 1;
		c.gridy = 0;
		c.ipadx = 10;
		c.insets = new Insets(0, 0, 0, 3);
		buttonPanel.add(new JButton(new ButtonAction(this, "Cancel", CANCEL_OPTION)), c);

		buttonPanel.setBorder(new BevelBorder(false));

		// setup dialog
		if (titlePanel != null)
		{
			c = new GridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.NORTHWEST;
			c.weightx = 1;
			c.gridx = 0;
			c.gridy = 0;
			c.ipady = 6;
			mDialog.add(titlePanel, c);
		}

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		if (titlePanel != null)
		{
			c.ipady = 30;
		}
		mDialog.add(mContentPanel, c);

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 20;
		mDialog.add(buttonPanel, c);

		mIsFinished = true;
	}


	/**
	 * Displays the dialog.
	 *
	 * @return the OK_OPTION or CANCEL_OPTION option made by the user.
	 */
	public int show()
	{
		if (mInsideColumn)
		{
			throw new IllegalStateException("Column not properly ended.");
		}
		if (mInsideGroup)
		{
			throw new IllegalStateException("Group not properly ended.");
		}

		mOption = CANCEL_OPTION;

		if (!mIsFinished)
		{
			finish();
		}

		mDialog.pack();
		mDialog.setLocationRelativeTo(mDialog.getParent());
		mDialog.setVisible(true);
		mDialog.dispose();

		return mOption;
	}


	protected void setOption(int aOption)
	{
		mOption = aOption;
	}


	public JDialog getDialog()
	{
		return mDialog;
	}


	public JPanel getContentPanel()
	{
		return mContentPanel;
	}
}
