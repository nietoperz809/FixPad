import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.Stack;

public class MyTextArea extends JTextPane
{
    private Stack<String> undoStack = new Stack<>();
    private JTabbedPane tpane;
    private int tabIndex;

    void append (String s)
    {
        Document doc = getDocument();
        try
        {
            doc.insertString(doc.getLength(), s, null);
        }
        catch (BadLocationException e)
        {
            System.out.println(e);
        }
    }

    private boolean wrap;
    public void setLineWrap (boolean b)
    {
        wrap = b;
    }
    public boolean getLineWrap ()
    {
        return wrap;
    }
    public void setWrapStyleWord (boolean b)
    {
        
    }

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
