package org.terifan.ui.progressdialog;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.terifan.ui.Utilities;


/**
 * Display a dialog with a text message and single indeterminate progress bar. The dialog can be canceled
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
	private JLabel mStatus;
	private JProgressBar mProgressBar;
	private JButton mCancelButton;


	public ProgressDialog(JFrame aOwner, String aTitle, String aStatus)
	{
		mStatus = new JLabel(aStatus + " ");
		mStatus.setHorizontalAlignment(SwingConstants.LEFT);

		mCancelButton = new JButton(mCancelAction);

		mProgressBar = new JProgressBar(JProgressBar.HORIZONTAL);

		JPanel statusPanel = new JPanel(new BorderLayout());
		statusPanel.setBorder(BorderFactory.createEmptyBorder(16, 8, 0, 8));
		statusPanel.add(mStatus, BorderLayout.CENTER);
		statusPanel.setBackground(new Color(255, 255, 255));

		JPanel progressPanel = new JPanel(new BorderLayout());
		progressPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 16, 8));
		progressPanel.add(mProgressBar, BorderLayout.CENTER);
		progressPanel.setBackground(new Color(255, 255, 255));

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(223, 223, 223)), BorderFactory.createEmptyBorder(16, 8, 16, 8)));
		buttonPanel.add(mCancelButton, BorderLayout.EAST);
		buttonPanel.setBackground(new Color(240, 240, 240));

		JPanel body = new JPanel(new BorderLayout());
		body.add(statusPanel, BorderLayout.NORTH);
		body.add(progressPanel, BorderLayout.CENTER);
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
		show(0, 100);
	}


	public void show(int aMin, int aMax)
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			throw new IllegalStateException("Executed on EventDispatchThread");
		}

		setRange(aMin, aMax);
		mProgressBar.setIndeterminate(true);

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


	/**
	 * Sets the status text and make the progress bar indeterminate.
	 */
	public void setIndeterminate(String aText)
	{
		mProgressBar.setIndeterminate(true);
		mStatus.setText(aText);
	}


	/**
	 * Sets the status text and current value of progress bar.
	 */
	public void setStatus(int aValue, String aText)
	{
		mProgressBar.setValue(aValue);
		mProgressBar.setIndeterminate(false);
		mStatus.setText(aText);
	}


	public void setRange(int aMin, int aMax)
	{
		mProgressBar.setMinimum(aMin);
		mProgressBar.setMaximum(aMax);
	}


	public void setCancellable(boolean aState)
	{
		mCancelButton.setEnabled(aState);
	}
}
