import transform.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PopupMenuHandler extends MouseInputAdapter
{
    private final JPopupMenu popup = new JPopupMenu();

    private JMenu cryptoSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);
        JMenuItem menuItem;

        menuItem = new JMenuItem("HagelinDecrypt");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new HagelinCrypt().retransform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Pitti1Encrypt");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new Pitti1Crypt().transform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Pitti1Decrypt");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new Pitti1Crypt().retransform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Peter1Crypt");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String pass = new SingleInputDialog().start("Enter Password");
            if (!pass.isEmpty())
            {
                try
                {
                    byte[] pwh = Crypto.passwordHash(pass.getBytes("UTF-8"));
                    String s = Crypto.ecryptFilePeter1(pwh, ta.getText());
                    ta.setText(s);
                }
                catch (Exception e1)
                {
                    System.out.println(e1);
                }
            }
        });
        men.add(menuItem);

        return men;
    }

    private JMenu codingSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);
        JMenuItem menuItem;

        menuItem = new JMenuItem("SHA-256");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new SHA256().transform (ta.getText());
            ta.append("\n"+st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("SHA-1");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new SHA1().transform (ta.getText());
            ta.append("\n"+st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("MD4");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new MD4().transform (ta.getText());
            ta.append("\n"+st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("MD5");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new MD5().transform (ta.getText());
            ta.append("\n"+st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("CRC16");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new CRC16CCITT().transform (ta.getText());
            ta.append("\n"+st);
        });
        men.add(menuItem);

        men.add(new JSeparator());

        menuItem = new JMenuItem("HagelinEncrypt");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new HagelinCrypt().transform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("UrlEncode");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new UrlEncodeUTF8().transform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("UrlDecode");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new UrlEncodeUTF8().retransform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        men.add(new JSeparator());

        menuItem = new JMenuItem("Rot13");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new Rot13().transform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Reverse Rot13");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new Rot13().retransform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        men.add(new JSeparator());

        menuItem = new JMenuItem("Base64");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new Base64().transform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("Reverse Base64");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new Base64().retransform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        men.add(new JSeparator());

        menuItem = new JMenuItem("GrayEncode");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new GrayCode().transform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        menuItem = new JMenuItem("GrayDecode");
        menuItem.addActionListener(e ->
        {
            ta.push();
            String st = new GrayCode().retransform (ta.getText());
            ta.setText(st);
        });
        men.add(menuItem);

        return men;
    }

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

        menuItem = new JMenuItem("Toggle Word Wrap");
        menuItem.addActionListener(e ->
        {
            ta.setLineWrap(!ta.getLineWrap());
        });
        men.add(menuItem);

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
        popup.add (codingSubMenu(ta, "Coding"));
        popup.add (cryptoSubMenu(ta, "Crypto"));

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
