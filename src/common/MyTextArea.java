package common;

import javax.swing.*;
import java.util.Stack;

public class MyTextArea extends JTextArea
{
    private Stack<String> undoStack = new Stack<>();
    private JTabbedPane tpane;
    private int tabIndex;

    public void push()
    {
        undoStack.push(getText());
    }

    public void pop()
    {
        try
        {
            setText(undoStack.pop());
        }
        catch (Exception e)
        {
            System.out.println(e);
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
