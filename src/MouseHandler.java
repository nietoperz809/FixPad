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
            final FontChooser2 fc = new FontChooser2(null);
            fc.setFont(ta.getFont());
            fc.setVisible(true);
            Font f = fc.getSelectedFont();

            if (f != null)
                ta.setFont(f);
        });
        popup.add (menuItem);

        menuItem = new JMenuItem("Caret Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Caret Color", null);
            ta.setCaretColor(col);
        });
        popup.add (menuItem);

        menuItem = new JMenuItem("Foreground Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Foreground Color", null);
            ta.setForeground(col);
        });
        popup.add (menuItem);

        menuItem = new JMenuItem("Background Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Background Color", null);
            ta.setBackground(col);
        });
        popup.add (menuItem);

        menuItem = new JMenuItem("Save as image");
        menuItem.addActionListener(e ->
        {
            Tools.saveAsImage(ta, "c:\\TextArea");
        });
        popup.add (menuItem);

        menuItem = new JMenuItem("As image to clipboard");
        menuItem.addActionListener(e ->
        {
            Tools.saveToClipboard(ta);
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
