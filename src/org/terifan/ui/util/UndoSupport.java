package org.terifan.ui.util;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;


public class UndoSupport
{
	public static void configure(JTextComponent aComponent)
	{
		configure(aComponent.getDocument(), aComponent);
	}


	public static void configure(Document aDocument, JComponent aComponent)
	{
		UndoManager undoHandler = new UndoManager();
		aDocument.addUndoableEditListener(undoHandler);

		AbstractAction undoAction = new AbstractAction("Undo")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (undoHandler.canUndo())
				{
					undoHandler.undo();
				}
			}
		};
		AbstractAction redoAction = new AbstractAction("Redo")
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (undoHandler.canRedo())
				{
					undoHandler.redo();
				}
			}
		};

		KeyStroke undoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
		aComponent.getInputMap().put(undoKeystroke, "undoKeystroke");
		aComponent.getInputMap().put(redoKeystroke, "redoKeystroke");
		aComponent.getActionMap().put("undoKeystroke", undoAction);
		aComponent.getActionMap().put("redoKeystroke", redoAction);
	}
}
