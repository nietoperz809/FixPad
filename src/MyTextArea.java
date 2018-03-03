import javax.swing.*;
import java.util.Stack;

public class MyTextArea extends JTextArea
{
    private Stack<String> undoStack = new Stack<>();

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
}
