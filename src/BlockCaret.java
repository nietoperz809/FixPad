import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Code based on Groovy text editor ...
 * groovy-console/src/main/groovy/groovy/ui/text/TextEditor.java
 *
 * @author nietoperz809
 */

public class BlockCaret extends DefaultCaret
{
    private static final long serialVersionUID = 1L;
    private final Object lock = new Object();
    private Rectangle saveRect = null;

    public BlockCaret ()
    {
        setBlinkRate(500);
    }

//    public void startFlashing ()
//    {
//        setVisible(true);
//        setSelectionVisible(true);
//    }

    protected void damage (Rectangle r)
    {
        if (saveRect != null)
        {
            synchronized (lock)
            {
                x = saveRect.x;
                y = saveRect.y;
                width = saveRect.width;
                height = saveRect.height;
                repaint();
            }
        }
    }

    public void mouseClicked (MouseEvent e)
    {
        JComponent c = (JComponent) e.getComponent();
        c.repaint();
    }

    public void paint (Graphics g)
    {
        if (isVisible())
        {
            synchronized (lock)
            {
                try
                {
                    JTextComponent component = getComponent();
                    Rectangle r = component.getUI().modelToView(component, getDot());
                    char cr = component.getText(getDot(), 1).charAt(0);
                    if (Character.isWhitespace(cr))
                    {
                        cr = '.';
                    }
                    FontMetrics fm = g.getFontMetrics();
                    r.width = fm.charWidth(cr);
                    r.height = fm.getHeight();
                    g.setColor(component.getCaretColor());
                    g.setXORMode(component.getBackground());
                    g.fillRect(r.x, r.y, r.width, r.height);
                    saveRect = r;
                }
                catch (BadLocationException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
