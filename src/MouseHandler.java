import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseInputAdapter
{
    final JPopupMenu popup = new JPopupMenu();

    MouseHandler (JTextArea ta)
    {
        JMenuItem menuItem = new JMenuItem("Font...");
        menuItem.addActionListener(e ->
        {
            Font f = FontChooser2.main();
            if (f != null)
                ta.setFont(f);
        });
        popup.add (menuItem);
    }

    @Override
    public void mousePressed (MouseEvent e)
    {
        if (e.getButton() == 3) // right button
        {
            popup.show(e.getComponent(), e.getX(), e.getY());
        }
        super.mousePressed(e);
    }
}
