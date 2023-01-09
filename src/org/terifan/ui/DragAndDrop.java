package org.terifan.ui;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;


/**
 * Implement Drag-n-Drop on a component. Use the static <code>register</code> method to enable default DND or instantiate and override the
 * various methods for custom handling.
 */
public class DragAndDrop
{
	public final static DataFlavor DATA_FLAVOR;
	public final static DataFlavor FILE_FLAVOR;
	public final static DataFlavor IMAGE_FLAVOR;
	public final static DataFlavor STRING_FLAVOR;
	public final static DataFlavor HTML_FLAVOR;
	public final static DataFlavor HTML_FRAGMENT_FLAVOR;


	static
	{
		try
		{
			DATA_FLAVOR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
			FILE_FLAVOR = DataFlavor.javaFileListFlavor;
			IMAGE_FLAVOR = DataFlavor.imageFlavor;
			STRING_FLAVOR = DataFlavor.stringFlavor;
			HTML_FLAVOR = DataFlavor.allHtmlFlavor;
			HTML_FRAGMENT_FLAVOR = DataFlavor.fragmentHtmlFlavor;
		}
		catch (Exception e)
		{
			throw new Error("Error", e);
		}
	}

	protected JComponent mComponent;
	protected boolean mCanDrop;


	public DragAndDrop(JComponent aComponent)
	{
		this(aComponent, true, true);
	}


	public DragAndDrop(JComponent aComponent, boolean aCanDrag, boolean aCanDrop)
	{
		mComponent = aComponent;
		mCanDrop = aCanDrop;

		if (aCanDrag)
		{
			DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(mComponent, DnDConstants.ACTION_COPY_OR_MOVE, new MyDragGestureListener());
		}

		mComponent.setTransferHandler(new MyTransferHandler());

		aComponent.setDropTarget(new DropTarget(aComponent, new DropTargetListener()
		{
			@Override
			public void dragEnter(DropTargetDragEvent aDtde)
			{
				DragAndDrop.this.dragEnter(aDtde);
			}


			@Override
			public void dragOver(DropTargetDragEvent aDtde)
			{
				DragAndDrop.this.dragOver(aDtde);
			}


			@Override
			public void dropActionChanged(DropTargetDragEvent aDtde)
			{
				DragAndDrop.this.onDropActionChanged(aDtde);
			}


			@Override
			public void dragExit(DropTargetEvent aDte)
			{
				DragAndDrop.this.dragExit(aDte);
			}


			@Override
			public void drop(DropTargetDropEvent aDtde)
			{
				try
				{
					if (canDrop(new CanDropEvent(aDtde.getLocation(), aDtde.getDropAction())))
					{
						aDtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						DataFlavor flavor = aDtde.getTransferable().getTransferDataFlavors()[0];
						Object transferData = aDtde.getTransferable().getTransferData(flavor);
						if (transferData instanceof List)
						{
							transferData = new ArrayList((List)transferData);
						}
						DragAndDrop.this.onDrop(new DropEvent(aDtde.getLocation(), aDtde.getDropAction(), transferData));
					}
				}
				catch (Throwable e)
				{
					e.printStackTrace(System.err);
				}
			}
		}));
	}


	/**
	 * Helper function to register a component as capable of dragging.
	 *
	 * @param aComponent the component to be setup
	 * @param aFlavorProvider an implementation of an FunctionalInterface that provides the type of content dragged
	 * @param aDragProvider an implementation of an FunctionalInterface that provides the value dragged
	 * @param aDragEndProvider optional, executed when a drop has completed
	 */
	public static void register(JComponent aComponent, FlavorProvider aFlavorProvider, DragProvider aDragProvider, DragEndProvider aDragEndProvider)
	{
		new DragAndDrop(aComponent)
		{
			@Override
			public DataFlavor dragFlavor(Point aDragOrigin)
			{
				return aFlavorProvider.dragFlavor(aDragOrigin);
			}


			@Override
			public Object onDrag(Point aDragOrigin)
			{
				return aDragProvider.onDrag(aDragOrigin);
			}


			@Override
			public void dragEnd(boolean aSuccess, Object aDropValue, int aDropAction)
			{
				if (aDragEndProvider != null)
				{
					aDragEndProvider.dragEnd(aSuccess, aDropValue, aDropAction);
				}
			}
		};
	}


	public JComponent getComponent()
	{
		return mComponent;
	}


	/**
	 * Called when a drag is recognized. This implementation returns null.
	 *
	 * @param aDragOrigin the position on the component where the drag occurred.
	 */
	public Object onDrag(Point aDragOrigin)
	{
		return null;
	}


	/**
	 * Return true if the drop is accepted. This method always return false and must be replaced in order for drops to be allowed.
	 *
	 * @param aDropEvent an object containing details about the drop.
	 * @return true if the drop is allowed
	 */
	public boolean canDrop(CanDropEvent aDropEvent)
	{
		return mCanDrop;
	}


	/**
	 * Called when a drop occur. This implementation does nothing.
	 *
	 * @param aDropEvent an object containing details about the drop.
	 */
	public void onDrop(DropEvent aDropEvent)
	{
	}


	/**
	 * Notified when a transfer has finished. This implementation does nothing.
	 *
	 * @param aDropEvent an object containing details about the drop.
	 */
	public void dragEnd(boolean aSuccess, Object aDropValue, int aDropAction)
	{
	}


	public void dragEnter(DropTargetDragEvent aDtde)
	{
	}


	public void dragExit(DropTargetEvent aDtde)
	{
	}


	public void dragOver(DropTargetDragEvent aDtde)
	{
	}


	public void onDropActionChanged(DropTargetDragEvent aDtde)
	{
	}


	/**
	 * Return the type of data that can be dragged from the current location
	 *
	 * @param aDragOrigin the position on the component where the drag may occur.
	 */
	public DataFlavor dragFlavor(Point aDragOrigin)
	{
		return DATA_FLAVOR;
	}


	private class MyDragGestureListener implements DragGestureListener
	{
		@Override
		public void dragGestureRecognized(DragGestureEvent aDrag)
		{
			MyTransferable transferable = new MyTransferable(aDrag, dragFlavor(aDrag.getDragOrigin()));

			aDrag.startDrag(null, transferable, new DragSourceAdapter()
			{
				@Override
				public void dragDropEnd(DragSourceDropEvent aDragSourceDropEvent)
				{
					try
					{
						DataFlavor flavor = transferable.getTransferDataFlavors()[0];
						Object transferData = aDragSourceDropEvent.getDragSourceContext().getTransferable().getTransferData(flavor);
						dragEnd(aDragSourceDropEvent.getDropSuccess(), transferData, aDragSourceDropEvent.getDropAction());
					}
					catch (UnsupportedFlavorException | IOException e)
					{
						throw new IllegalStateException(e);
					}
				}
			});
		}
	}


	private class MyTransferHandler extends TransferHandler
	{
		@Override
		public boolean canImport(TransferSupport aSupport)
		{
			try
			{
				return aSupport.isDataFlavorSupported(DATA_FLAVOR) && aSupport.getTransferable() != null && canDrop(new DropEvent(aSupport));
			}
			catch (Throwable e)
			{
				e.printStackTrace(System.err);
			}
			return false;
		}


		@Override
		public boolean importData(TransferSupport aSupport)
		{
			try
			{
				onDrop(new DropEvent(aSupport));
			}
			catch (Throwable e)
			{
				e.printStackTrace(System.err);
			}
			return true;
		}


		@Override
		public int getSourceActions(JComponent aComponent)
		{
			return COPY_OR_MOVE;
		}


		@Override
		protected Transferable createTransferable(JComponent aComponent)
		{
			throw new UnsupportedOperationException("Swing DND is enabled on component!");
		}
	}


	private class MyTransferable implements Transferable
	{
		private Point mDragOrigin;
		private DataFlavor mFlavor;


		public MyTransferable(DragGestureEvent aEvent, DataFlavor aFlavor)
		{
			mDragOrigin = aEvent.getDragOrigin();
			mFlavor = aFlavor;
		}


		@Override
		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[]
			{
				mFlavor
			};
		}


		@Override
		public boolean isDataFlavorSupported(DataFlavor aFlavor)
		{
			return aFlavor.equals(mFlavor);
		}


		@Override
		public Object getTransferData(DataFlavor aFlavor)
		{
			Object drag = onDrag(mDragOrigin);

			if (mFlavor == FILE_FLAVOR)
			{
				if (drag instanceof File)
				{
					drag = Arrays.asList((File)drag);
				}
				else if (drag instanceof File[])
				{
					drag = Arrays.asList((File[])drag);
				}
			}

			return drag;
		}
	}


	public static class CanDropEvent
	{
		public final static int COPY = DnDConstants.ACTION_COPY;
		public final static int MOVE = DnDConstants.ACTION_MOVE;

		protected Point mDropLocation;
		protected int mDropAction;


		CanDropEvent(Point aDropLocation, int aDropAction)
		{
			mDropAction = aDropAction;
			mDropLocation = aDropLocation;
		}


		public Point getDropLocation()
		{
			return mDropLocation;
		}


		public int getDropAction()
		{
			return mDropAction;
		}


		@Override
		public String toString()
		{
			String action = mDropAction == MOVE ? "move" : mDropAction == COPY ? "copy" : "other";
			String at = "[" + mDropLocation.x + "," + mDropLocation.y + "]";
			return "{action=" + action + ", dropLocation=" + at + "}";
		}
	}


	public static class DropEvent extends CanDropEvent
	{
		private Object mTransferData;


		public DropEvent(TransferSupport aSupport)
		{
			super(aSupport.getDropLocation().getDropPoint(), aSupport.getDropAction());
			try
			{
				mTransferData = aSupport.getTransferable().getTransferData(DATA_FLAVOR);
			}
			catch (UnsupportedFlavorException | IOException e)
			{
				throw new IllegalStateException(e);
			}
		}


		public DropEvent(Point aDropLocation, int aDropActon, Object aTransferData)
		{
			super(aDropLocation, aDropActon);
			mTransferData = aTransferData;
		}


		public <T> T getTransferData()
		{
			return (T)mTransferData;
		}


		public <E> E getTransferData(Class<E> aType)
		{
			if (aType.isAssignableFrom(mTransferData.getClass()))
			{
				return (E)mTransferData;
			}
			return null;
		}


		@Override
		public String toString()
		{
			String action = mDropAction == MOVE ? "move" : mDropAction == COPY ? "copy" : "other";
			String at = "[" + mDropLocation.x + "," + mDropLocation.y + "]";
			return "{action=" + action + ", dropLocation=" + at + ", transferable=" + getTransferData() + "}";
		}
	}


	@FunctionalInterface
	public interface FlavorProvider
	{
		DataFlavor dragFlavor(Point aDragOrigin);
	}


	@FunctionalInterface
	public interface DragProvider
	{
		Object onDrag(Point aDragOrigin);
	}


	@FunctionalInterface
	public interface DragEndProvider
	{
		void dragEnd(boolean aSuccess, Object aDropValue, int aDropAction);
	}

//	public static void main(String ... args)
//	{
//		try
//		{
//			JTree tree = new JTree();
//			JPanel panel = new JPanel(null);
//
//			new DragAndDrop(tree)
//			{
//				@Override
//				public boolean canDrop(DropEvent aDropEvent)
//				{
//					return true;
//				}
//
//				@Override
//				public void onDrop(DropEvent aDropEvent)
//				{
//					DefaultMutableTreeNode data = (DefaultMutableTreeNode)aDropEvent.getTransferData();
//
//					if (data.getParent() != null)
//					{
//						((DefaultTreeModel)tree.getModel()).removeNodeFromParent(data);
//					}
//
//					TreePath path = tree.getClosestPathForLocation(aDropEvent.getDropLocation().x, aDropEvent.getDropLocation().y);
//
//					DefaultMutableTreeNode lastPathComponent = (DefaultMutableTreeNode)path.getLastPathComponent();
//					lastPathComponent.add(data);
//					tree.expandPath(path);
//				}
//
//				@Override
//				public Object onDrag(Point aDragOrigin)
//				{
//					return tree.getClosestPathForLocation(aDragOrigin.x, aDragOrigin.y).getLastPathComponent();
//				}
//
//				@Override
//				public void dragEnd(boolean aSuccess, Object aTransferData, int aDropAction)
//				{
//					Log.out.println(aSuccess+" "+aTransferData+" "+aDropAction);
//				}
//			};
//
//			new DragAndDrop(panel)
//			{
//				@Override
//				public boolean canDrop(DropEvent aDropEvent)
//				{
//					return aDropEvent.getTransferData() != null && !"food".equals(aDropEvent.getTransferData().toString());
//				}
//
//				@Override
//				public void onDrop(DropEvent aDropEvent)
//				{
//					JLabel label = new JLabel(aDropEvent.getTransferData().toString());
//					label.setLocation(aDropEvent.getDropLocation());
//					label.setSize(100,20);
//					panel.add(label);
//					panel.repaint();
//
//					new DragAndDrop(label)
//					{
//						@Override
//						public Object onDrag(Point aDragOrigin)
//						{
//							return ((JLabel)mComponent).getText();
//						}
//
//						@Override
//						public void dragEnd(boolean aSuccess, Object aDropValue, int aDropAction)
//						{
//							if (aSuccess && aDropAction == DropEvent.MOVE)
//							{
//								Container parent = mComponent.getParent();
//								parent.remove(mComponent);
//								parent.repaint();
//							}
//						}
//					};
//				}
//			};
//
//			JPanel pane = new JPanel(new GridLayout(1,2));
//			pane.add(tree);
//			pane.add(panel);
//
//			JFrame frame = new JFrame();
//			frame.add(pane);
//			frame.setSize(1024, 768);
//			frame.setLocationRelativeTo(null);
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			frame.setVisible(true);
//		}
//		catch (Throwable e)
//		{
//			e.printStackTrace(System.out);
//		}
//	}
}
