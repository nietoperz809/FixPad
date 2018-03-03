import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PopupMenuHandler extends MouseInputAdapter
{
    private final JPopupMenu popup = new JPopupMenu();

    PopupMenuHandler (MyTextArea ta)
    {
        JMenuItem menuItem = new JMenuItem("Font...");
        menuItem.addActionListener(e ->
        {
            final FontChooser2 fc = new FontChooser2(null);
            fc.adjustDisplay(ta.getFont());
            fc.setVisible(true);
            Font f = fc.getSelectedFont();

            if (f != null)
            {
                ta.setFont(f);
            }
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Caret Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Caret Color", null);
            ta.setCaretColor(col);
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Foreground Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Foreground Color", null);
            ta.setForeground(col);
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Background Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Background Color", null);
            ta.setBackground(col);
        });
        popup.add(menuItem);

        popup.add(new JSeparator());

        menuItem = new JMenuItem("Save as image");
        menuItem.addActionListener(e ->
                TextAreaTools.saveAsImage(ta, "c:\\TextArea"));
        popup.add(menuItem);

        menuItem = new JMenuItem("As image to clipboard");
        menuItem.addActionListener(e ->
                TextAreaTools.saveImageToClipboard(ta));
        popup.add(menuItem);

        popup.add(new JSeparator());

        menuItem = new JMenuItem("Center Text");
        menuItem.addActionListener(e ->
        {
            ta.push();
            TextAreaTools.centerText(ta);
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Number Text");
        menuItem.addActionListener(e ->
        {
            ta.push();
            TextAreaTools.numberText(ta);
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Roman Numbering");
        menuItem.addActionListener(e ->
        {
            ta.push();
            TextAreaTools.romanNumberText(ta);
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Upcase");
        menuItem.addActionListener(e ->
        {
            ta.push();
            ta.setText(ta.getText().toUpperCase());
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Lowcase");
        menuItem.addActionListener(e ->
        {
            ta.push();
            ta.setText(ta.getText().toLowerCase());
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Capitalize");
        menuItem.addActionListener(e ->
        {
            ta.push();
            TextAreaTools.capitalize(ta);
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Reverse");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String rev = new StringBuilder(ta.getText()).reverse().toString();
            ta.setText(rev);
        });
        popup.add(menuItem);

        popup.add(new JSeparator());

        menuItem = new JMenuItem("Undo");
        menuItem.addActionListener(e -> ta.pop());
        popup.add(menuItem);
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
