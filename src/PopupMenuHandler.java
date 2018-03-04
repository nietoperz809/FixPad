import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PopupMenuHandler extends MouseInputAdapter
{
    private final JPopupMenu popup = new JPopupMenu();

    private JMenu imageSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);
        JMenuItem menuItem;

        menuItem = new JMenuItem("Save as image");
        menuItem.addActionListener(e ->
                TextAreaTools.saveAsImage(ta, "c:\\TextArea"));
        men.add(menuItem);

        menuItem = new JMenuItem("As image to clipboard");
        menuItem.addActionListener(e ->
                TextAreaTools.saveImageToClipboard(ta));
        men.add(menuItem);

        return men;
    }

    private JMenu settingSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);
        JMenuItem menuItem;

        menuItem = new JMenuItem("Font...");
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
        men.add(menuItem);

        menuItem = new JMenuItem("Caret Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Caret Color", null);
            ta.setCaretColor(col);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Foreground Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Foreground Color", null);
            ta.setForeground(col);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Background Color...");
        menuItem.addActionListener(e ->
        {
            Color col = JColorChooser.showDialog(null,
                    "Background Color", null);
            ta.setBackground(col);
        });
        men.add(menuItem);

        return men;
    }

    private JMenu textSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);
        JMenuItem menuItem;

        menuItem = new JMenuItem("Replace ...");
        menuItem.addActionListener(e ->
        {
            ta.push();
            SearchBox.SbResult res = new SearchBox().run();
            if (res != null)
            {
                if (!(res.from.isEmpty() || res.to.isEmpty()))
                {
                    String s = ta.getText().replace(res.from, res.to);
                    ta.setText(s);
                }
            }
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Center Text");
        menuItem.addActionListener(e ->
        {
            ta.push();
            TextAreaTools.centerText(ta);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Number Text");
        menuItem.addActionListener(e ->
        {
            ta.push();
            TextAreaTools.numberText(ta);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Roman Numbering");
        menuItem.addActionListener(e ->
        {
            ta.push();
            TextAreaTools.romanNumberText(ta);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Upcase");
        menuItem.addActionListener(e ->
        {
            ta.push();
            ta.setText(ta.getText().toUpperCase());
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Lowcase");
        menuItem.addActionListener(e ->
        {
            ta.push();
            ta.setText(ta.getText().toLowerCase());
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Capitalize");
        menuItem.addActionListener(e ->
        {
            ta.push();
            TextAreaTools.capitalize(ta);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Reverse");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String rev = new StringBuilder(ta.getText()).reverse().toString();
            ta.setText(rev);
        });
        men.add(menuItem);

        return men;
    }

    PopupMenuHandler (MyTextArea ta)
    {
        JMenuItem menuItem;

        menuItem = new JMenuItem("Copy");
        menuItem.addActionListener(e ->
        {
            ta.copy();
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Paste");
        menuItem.addActionListener(e ->
        {
            ta.paste();
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Cut");
        menuItem.addActionListener(e ->
        {
            ta.cut();
        });
        popup.add(menuItem);

        menuItem = new JMenuItem("Select all");
        menuItem.addActionListener(e ->
        {
            ta.selectAll();
        });
        popup.add(menuItem);

        popup.add (settingSubMenu(ta, "Settings"));
        popup.add (imageSubMenu(ta, "Image"));
        popup.add (textSubMenu(ta, "Text Manipulation"));

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
