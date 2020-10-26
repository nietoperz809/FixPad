package common;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.event.MouseListener;
import java.util.Stack;

public class MyTextArea extends JTextArea
{
    private Stack<Document> undoStack = new Stack<>();
    private JTabbedPane tpane;
    private int tabIndex;
    private PopupMenuHandler menuHandler;

    @Override
    public synchronized void addMouseListener(MouseListener l)
    {
        if (l instanceof PopupMenuHandler)
            menuHandler = (PopupMenuHandler)l;
        super.addMouseListener(l);
    }

    int getUndoStackSize()
    {
        return undoStack.size();
    }

    void updateUndoMenu()
    {
        menuHandler.undoItem.setText("Undo ("+getUndoStackSize()+")");
    }

    /**
     * Push current Doc on Undo Stack
     */
    public void push()
    {
        Document doc = getDocument();
        if (doc == null || doc.getLength() == 0)
            return;
        try
        {
            String txt = doc.getText(0, doc.getLength());
            doc = new PlainDocument();
            doc.insertString(0, txt, null);
        }
        catch (BadLocationException e)
        {
            System.out.println(e);
            return;
        }
        if (undoStack == null)
            undoStack = new Stack<>();
        undoStack.push(doc);
        System.out.println("push");
        updateUndoMenu();
    }

    /**
     * Pop current Doc from Undo Stack
     */
    public void pop()
    {
        try
        {
            Document doc = undoStack.pop();
            updateUndoMenu();
            super.setDocument(doc);
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @Override
    public void setDocument(Document doc)
    {
        push();
        super.setDocument(doc);
    }

    @Override
    public void append(String str)
    {
        push();
        super.append(str);
    }

    @Override
    public void setText(String str)
    {
        push();
        super.setText(str);
    }

    /**
     * Set new Context by reparing tne Doc Object
     * @param s new content as string
     */
    public void setFastText(String s)
    {
        //System.out.println("setfast "+s);
        push();
        PlainDocument doc = new PlainDocument();
        try
        {
            doc.insertString(0, s, null);
            super.setDocument(doc);
        }
        catch (BadLocationException e1)
        {
            System.out.println(e1);
        }
    }



    public String getTabTitle()
    {
        return getTpane().getTitleAt(getTabIndex());
    }

    /**
     * Get Tab pane
     * @return the pane
     */
    public JTabbedPane getTpane ()
    {
        return tpane;
    }


    /**
     * get index of tab pane where this textfield resides
     * @return the index
     */
    public int getTabIndex ()
    {
        return tabIndex;
    }

    /**
     * Set tab pane and tab index of this text field
     * @param tp Pane
     * @param idx index
     */
    public void setTabData (JTabbedPane tp, int idx)
    {
        this.tpane = tp;
        this.tabIndex = idx;
    }
}
