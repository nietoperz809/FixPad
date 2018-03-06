import crypto.Crypto;
import transform.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class PopupMenuHandler extends MouseInputAdapter
{
    private final JPopupMenu popup = new JPopupMenu();

    PopupMenuHandler (MyTextArea ta)
    {
        menuOption("Copy", popup, e -> ta.copy());
        menuOption("Paste", popup, e -> ta.paste());
        menuOption("Cut", popup, e -> ta.cut());
        menuOption("Select all", popup, e -> ta.selectAll());
        popup.add(new JSeparator());
        popup.add(settingSubMenu(ta, "Settings"));
        popup.add(imageSubMenu(ta, "Image"));
        popup.add(textSubMenu(ta, "Text Manipulation"));
        popup.add(codingSubMenu(ta, "Coding"));
        popup.add(cryptoSubMenu(ta, "Crypto"));
        popup.add(new JSeparator());
        menuOption("Undo", popup, e -> ta.pop());
    }

    private void menuOption (String name, JComponent men, ActionListener e)
    {
        JMenuItem mi = new JMenuItem(name);
        mi.addActionListener(e);
        men.add(mi);
    }

    private JMenu settingSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);

        menuOption("Change tab name", men, e -> {
            String name = new SingleInputDialog().start("Tab name", "Tab:" + ta.getTabIndex());
            if (!name.isEmpty())
            {
                ta.getTpane().setTitleAt(ta.getTabIndex(), name);
            }
        });
        menuOption("Toggle Word Wrap", men, e -> {
            ta.setLineWrap(!ta.getLineWrap());
            ta.setWrapStyleWord(true);
        });
        menuOption("Font ...", men, e -> {
            final FontChooser2 fc = new FontChooser2(null);
            fc.adjustDisplay(ta.getFont());
            fc.setVisible(true);
            Font f = fc.getSelectedFont();

            if (f != null)
            {
                ta.setFont(f);
            }
        });
        menuOption("Caret Color...", men, e -> {
            Color col = JColorChooser.showDialog(null,
                    "Caret Color", null);
            ta.setCaretColor(col);
        });
        menuOption("Foreground Color...", men, e -> {
            Color col = JColorChooser.showDialog(null,
                    "Foreground Color", null);
            ta.setForeground(col);
        });
        menuOption("Background Color...", men, e -> {
            Color col = JColorChooser.showDialog(null,
                    "Background Color", null);
            ta.setBackground(col);
        });

        return men;
    }

    private JMenu imageSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);

        menuOption("Save as image", men, e -> {
            String fname = new SingleInputDialog().start("Path and name of File", "c:\\TextArea.png");
            if (!fname.isEmpty())
            {
                TextAreaTools.saveAsImage(ta, fname);
            }
        });
        menuOption("Image to clipboard", men, e -> {
            TextAreaTools.saveImageToClipboard(ta);
        });

        return men;
    }

    private JMenu textSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);

        menuOption("Replace ...", men, e -> {
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
        menuOption("Center Text", men, e -> {
            ta.push();
            TextAreaTools.centerText(ta);
        });
        menuOption("Trim Lines", men, e -> {
            ta.push();
            TextAreaTools.trimLines(ta);
        });
        menuOption("Remove left", men, e -> {
            String s = new SingleInputDialog().start("How many chars", "");
            try
            {
                int num = Integer.parseInt(s);
                ta.push();
                TextAreaTools.removeLeft(ta, num);
            }
            catch (NumberFormatException e1)
            {
                System.out.println(e1);
            }
        });
        menuOption("Insert left", men, e -> {
            String s = new SingleInputDialog().start("How many chars", "");
            try
            {
                ta.push();
                TextAreaTools.insertLeft(ta, s);
            }
            catch (NumberFormatException e1)
            {
                System.out.println(e1);
            }
        });
        menuOption("Line Number", men, e -> {
            ta.push();
            TextAreaTools.numberText(ta);
        });
        menuOption("Roman Line Number", men, e -> {
            ta.push();
            TextAreaTools.romanNumberText(ta);
        });
        menuOption("Upcase", men, e -> {
            ta.push();
            ta.setText(ta.getText().toUpperCase());
        });
        menuOption("Downcase", men, e -> {
            ta.push();
            ta.setText(ta.getText().toLowerCase());
        });
        menuOption("Capitalize", men, e -> {
            ta.push();
            TextAreaTools.capitalize(ta);
        });
        menuOption("Reverse", men, e -> {
            ta.push();
            String rev = new StringBuilder(ta.getText()).reverse().toString();
            ta.setText(rev);
        });

        return men;
    }

    private JMenu codingSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);

        menuOption("SHA-256", men, e -> {
            ta.push();
            String st = new SHA256().transform(ta.getText());
            ta.append("\n" + st);
        });
        menuOption("SHA-1", men, e -> {
            ta.push();
            String st = new SHA1().transform(ta.getText());
            ta.append("\n" + st);
        });
        menuOption("MD4", men, e -> {
            ta.push();
            String st = new MD4().transform(ta.getText());
            ta.append("\n" + st);
        });
        menuOption("MD5", men, e -> {
            ta.push();
            String st = new MD5().transform(ta.getText());
            ta.append("\n" + st);
        });
        menuOption("CRC16", men, e -> {
            ta.push();
            String st = new CRC16CCITT().transform(ta.getText());
            ta.append("\n" + st);
        });
        men.add(new JSeparator());
        menuOption("UrlEncode", men, e -> {
            ta.push();
            String st = new UrlEncodeUTF8().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("UrlDecode", men, e -> {
            ta.push();
            String st = new UrlEncodeUTF8().retransform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("Rot13", men, e -> {
            ta.push();
            String st = new Rot13().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("Reverse Rot13", men, e -> {
            ta.push();
            String st = new Rot13().retransform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("Base64", men, e -> {
            ta.push();
            String st = new Base64().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("Reverse Base64", men, e -> {
            ta.push();
            String st = new Base64().retransform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("GrayEncode", men, e -> {
            ta.push();
            String st = new GrayCode().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("GrayDecode", men, e -> {
            ta.push();
            String st = new GrayCode().retransform(ta.getText());
            ta.setText(st);
        });

        return men;
    }

    private JMenu cryptoSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);

        menuOption("HagelinEncrypt", men, e -> {
            ta.push();
            String st = new HagelinCrypt().transform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("Pitti1-Encrypt", men, e -> {
            ta.push();
            String st = new Pitti1Crypt().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("Pitti1-Decrypt", men, e -> {
            ta.push();
            String st = new Pitti1Crypt().retransform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("Peter1Crypt", men, e -> {
            ta.push();
            String pass = new SingleInputDialog().start("Enter Password", "");
            if (!pass.isEmpty())
            {
                try
                {
                    byte[] pwh = Crypto.passwordHash(pass.getBytes("UTF-8"));
                    String s = Crypto.cryptFilePeter1(pwh, ta.getText());
                    ta.setText(s);
                }
                catch (Exception e1)
                {
                    System.out.println(e1);
                }
            }
        });
        men.add(new JSeparator());
        menuOption("AES Encrypt", men, e -> {
            ta.push();
            String pass = new SingleInputDialog().start("Enter Password", "");
            if (!pass.isEmpty())
            {
                try
                {
                    byte[] pwh = Crypto.passwordHash(pass.getBytes("UTF-8"));
                    String s = Crypto.cryptAes256(true, pwh, ta.getText());
                    ta.setText(s);
                }
                catch (Exception e1)
                {
                    System.out.println(e1);
                }
            }
        });
        menuOption("AES Decrypt", men, e -> {
            ta.push();
            String pass = new SingleInputDialog().start("Enter Password", "");
            if (!pass.isEmpty())
            {
                try
                {
                    byte[] pwh = Crypto.passwordHash(pass.getBytes("UTF-8"));
                    String s = Crypto.cryptAes256(false, pwh, ta.getText());
                    ta.setText(s);
                }
                catch (Exception e1)
                {
                    System.out.println(e1);
                }
            }
        });

        return men;
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
