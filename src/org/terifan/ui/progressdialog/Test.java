package org.terifan.ui.progressdialog;

import org.terifan.ui.Utilities;


public class Test
{
	public static void main(String... args)
	{
		Utilities.setSystemLookAndFeel();

		try (ProgressDialog dialog = new ProgressDialog(null, "Loading", "Waiting..."))
		{
			dialog.setCancellable(false);
			dialog.show();

			Thread.sleep(1000);

			dialog.setCancellable(true);
			dialog.setRange(0, 0, 9);

			for (int i = 0; i < 10 && !dialog.isCancelled(); i++)
			{
				dialog.setProgress(0, i, "Step " + i);

				if (i == 5)
				{
					dialog.setIndeterminate(0, "Step " + i);
					Thread.sleep(2000);
					dialog.setIndeterminate(0, "Step " + i + ", almost ready...");
					Thread.sleep(1000);
				}

				Thread.sleep(500);
			}
		}
		catch (Throwable e)
		{
			e.printStackTrace(System.out);
		}
	}
}
