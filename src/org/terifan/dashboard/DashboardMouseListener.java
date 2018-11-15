package org.terifan.dashboard;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;


class DashboardMouseListener extends MouseAdapter
{
	private DashboardTile mComponent;
	private Point mClickPosition;
	private boolean mMove;
	private boolean mResize;


	public DashboardMouseListener(DashboardTile aDashboard)
	{
		mComponent = aDashboard;
	}


	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		mClickPosition = aEvent.getPoint();

		Dashboard panel = mComponent.getDashboard();
		panel.moveToTop(mComponent);
		mComponent.invalidate();
		panel.validate();
		panel.repaint();

		mComponent.getDashboard().setSelected(mComponent);

		mMove = mClickPosition.x < 20 && mClickPosition.y < 20;
		mResize = mClickPosition.x > mComponent.getWidth() - 20 && mClickPosition.y > mComponent.getHeight() - 20;
	}


	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
	}


	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		if (!mMove && !mResize)
		{
			return;
		}

		Dashboard dashboard = mComponent.getDashboard();
		int gs = dashboard.getGridSize();

		if (mMove)
		{
			Point pos = SwingUtilities.convertPoint(mComponent, aEvent.getPoint(), dashboard);
			mComponent.getBounds().setLocation((int)Math.floor((pos.x - mClickPosition.x) / (double)gs) * gs, (int)Math.floor((pos.y - mClickPosition.y) / (double)gs) * gs);
		}
		else
		{
			Point pos = aEvent.getPoint();
			mComponent.getBounds().setSize((int)Math.max(Math.ceil(pos.x / (double)gs), 1) * gs, (int)Math.max(Math.ceil(pos.y / (double)gs), 1) * gs);
		}

		mComponent.invalidate();
		dashboard.validate();
	}
}
