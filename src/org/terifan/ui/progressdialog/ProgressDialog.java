package org.terifan.ui.progressdialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.terifan.ui.layout.VerticalFlowLayout;


/**
 * Display a dialog with a text message and single indeterminate progress bar. The dialog can be cancelled
 * <pre>
 * try (ProgressDialog dialog = new ProgressDialog(null, "Loading", "Waiting..."))
 * {
 * 	dialog.setCancellable(false);
 * 	dialog.show();
 *
 * 	Thread.sleep(1000);
 *
 * 	dialog.setCancellable(true);
 * 	dialog.setRange(0, 9);
 *
 * 	for (int i = 0; i &lt; 10 &amp;&amp; !dialog.isCancelled(); i++)
 * 	{
 * 		dialog.setStatus(i, "Step " + i);
 *
 * 		if (i == 5)
 * 		{
 * 			dialog.setStatus("Step " + i);
 * 			Thread.sleep(2000);
 * 		}
 *
 * 		Thread.sleep(500);
 * 	}
 * }
 * </pre>
 */
public class ProgressDialog implements AutoCloseable
{
	private JDialog mDialog;
	private boolean mCancelled;
	private boolean mPaused;
	private JLabel[] mLabel;
	private JProgressBar[] mProgressBar;
	private JButton mCancelButton;
	private JToggleButton mPauseButton;


	public ProgressDialog(JFrame aOwner, String aTitle, String aStatus)
	{
		this(aOwner, aTitle, aStatus, 1);
	}


	public ProgressDialog(JFrame aOwner, String aTitle, String aStatus, int aCount)
	{
		mCancelButton = new JButton(mCancelAction);
		mPauseButton = new JToggleButton(new AbstractAction("Pause")
		{
			@Override
			public void actionPerformed(ActionEvent aEvent)
			{
				mPaused = !mPaused;
			}
		});

		JPanel mainPanel = new JPanel(new VerticalFlowLayout(8));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		mLabel = new JLabel[aCount];
		mProgressBar = new JProgressBar[aCount];

		for (int i = 0; i < aCount; i++)
		{
			mLabel[i] = new JLabel(" ", SwingConstants.LEFT);
			mProgressBar[i] = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

			mainPanel.add(mLabel[i]);
			mainPanel.add(mProgressBar[i]);
		}

		mLabel[0].setText(aStatus + " ");

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(16, 8, 8, 8));
		buttonPanel.add(mPauseButton, BorderLayout.WEST);
		buttonPanel.add(mCancelButton, BorderLayout.EAST);

		JPanel body = new JPanel(new BorderLayout());
		body.add(mainPanel, BorderLayout.CENTER);
		body.add(buttonPanel, BorderLayout.SOUTH);

		mDialog = new JDialog(aOwner, aTitle);
		mDialog.add(body);
		mDialog.setMinimumSize(new Dimension(400, 100));
		mDialog.pack();
		mDialog.setLocationRelativeTo(null);
		mDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mDialog.setResizable(false);
		mDialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent aEvent)
			{
				confirmCancel();
			}
		});
	}

	private AbstractAction mCancelAction = new AbstractAction("Cancel")
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			confirmCancel();
		}
	};


	protected void confirmCancel() throws HeadlessException
	{
		if (mCancelButton.isEnabled() && JOptionPane.showConfirmDialog(null, "Confirm you want to cancel the process?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
		{
			mCancelButton.setEnabled(false);
			mCancelled = true;
		}
	}


	public void show()
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			throw new IllegalStateException("Executed on EventDispatchThread");
		}

		for (int i = 0; i < mProgressBar.length; i++)
		{
			mProgressBar[i].setIndeterminate(true);
		}

		mDialog.setVisible(true);
	}


	@Override
	public void close()
	{
		mDialog.dispose();
	}


	public boolean isCancelled()
	{
		return mCancelled;
	}


	public boolean isPaused()
	{
		return mPaused;
	}


	public int getProgress(int aIndex)
	{
		return mProgressBar[aIndex].getValue();
	}


	/**
	 * Sets the status text and make the progress bar indeterminate.
	 */
	public ProgressDialog setIndeterminate(int aIndex, String aText)
	{
		mProgressBar[aIndex].setIndeterminate(true);
		if (aText != null)
		{
			mLabel[aIndex].setText(aText);
		}
		return this;
	}


	/**
	 * Sets the status text and current value of progress bar.
	 */
	public ProgressDialog setProgress(int aIndex, int aValue, String aText)
	{
		mProgressBar[aIndex].setIndeterminate(false);
		mProgressBar[aIndex].setValue(aValue);
		if (aText != null)
		{
			mLabel[aIndex].setText(aText);
		}
		return this;
	}


	public ProgressDialog setRange(int aIndex, int aMin, int aMax)
	{
		mProgressBar[aIndex].setMinimum(aMin);
		mProgressBar[aIndex].setMaximum(aMax);
		return this;
	}


	public ProgressDialog setCancellable(boolean aState)
	{
		mCancelButton.setEnabled(aState);
		return this;
	}


	public void waitIfPaused()
	{
		while (isPaused())
		{
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
				break;
			}
		}
	}
}
