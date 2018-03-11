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

    public void push()
    {
        undoStack.push(getDocument());
    }

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

    public JTabbedPane getTpane ()
    {
        return tpane;
    }

    public void setTpane (JTabbedPane tpane)
    {
        this.tpane = tpane;
    }

    public int getTabIndex ()
    {
        return tabIndex;
    }

    public void setTabIndex (int tabIndex)
    {
        this.tabIndex = tabIndex;
    }

    public void setTabData (JTabbedPane tp, int idx)
    {
        setTpane(tp);
        setTabIndex(idx);
    }
}
