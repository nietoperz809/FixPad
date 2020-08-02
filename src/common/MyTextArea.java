package common;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.util.Stack;

public class MyTextArea extends JTextArea
{
    private Stack<Document> undoStack = new Stack<>();
    private JTabbedPane tpane;
    private int tabIndex;

    /**
     * Push current Doc on Undo Stack
     */
    public void push()
    {
        undoStack.push(getDocument());
    }

    /**
     * Pop current Doc from Undo Stack
     */
    public void pop()
    {
        try
        {
            setDocument(undoStack.pop());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    /**
     * Set new Context by repacing tne Doc Object
     * @param s new content as string
     */
    public void setFastText(String s)
    {
        PlainDocument doc = new PlainDocument();
        try
        {
            doc.insertString(0, s, null);
            this.setDocument(doc);
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

//    /**
//     * Set tab pane where this textfield belongs to
//     * @param tpane The tab pane
//     */
//    public void setTpane (JTabbedPane tpane)
//    {
//        this.tpane = tpane;
//    }

    /**
     * get index of tab pane where this textfield resides
     * @return the index
     */
    public int getTabIndex ()
    {
        return tabIndex;
    }

//    /**
//     * Set index in tab Pane where this textfield belongs to
//     * @param tabIndex
//     */
//    public void setTabIndex (int tabIndex)
//    {
//        this.tabIndex = tabIndex;
//    }

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
