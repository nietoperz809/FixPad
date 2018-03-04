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

    public BlockCaret ()
    {
        setBlinkRate(500);
    }

    protected synchronized void damage (Rectangle r)
    {
        if (r != null)
        {
            JTextComponent component = getComponent();
            x = r.x;
            y = r.y;
            Font font = component.getFont();
            width = component.getFontMetrics(font).charWidth('w');
            height = r.height;
            repaint();
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
            try
            {
                JTextComponent component = getComponent();
                Rectangle r = component.getUI().modelToView(component, getDot());
                char cr = component.getText(getDot(),1).charAt(0);
                if (Character.isWhitespace(cr))
                    cr = '.';
                FontMetrics fm = g.getFontMetrics();
                int width = fm.charWidth(cr);
                if (width < 1)
                    width = 1;
                g.setColor(component.getBackground());
                g.setXORMode(component.getCaretColor());
                g.fillRect(r.x, r.y, width, fm.getHeight()-2); //r.width, r.height);
                g.setPaintMode();
            }
            catch (BadLocationException e)
            {
                e.printStackTrace();
            }
        }
    }
}
