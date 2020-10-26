package common;

import crypto.Crypto;
import dialogs.SearchBox;
import dialogs.SingleInputDialog;
import transform.*;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class PopupMenuHandler extends MouseInputAdapter
{
    private final JPopupMenu popup = new JPopupMenu();
    public JMenuItem undoItem;

    PopupMenuHandler (MyTextArea ta)
    {
        menuOption("Copy", popup, e -> ta.copy());
        menuOption("Paste", popup, e -> ta.paste());
        menuOption("Cut", popup, e -> ta.cut());
        menuOption("Select all", popup, e -> ta.selectAll());
        popup.add(new JSeparator());
        popup.add(settingSubMenu(ta, "TextAreaSettings"));
        popup.add(imageSubMenu(ta, "Output Image"));
        popup.add(textOutputSubMenu(ta, "Output Text"));
        popup.add(textSubMenu(ta, "Text Manipulation"));
        popup.add(codingSubMenu(ta, "Coding"));
        popup.add(cryptoSubMenu(ta, "Crypto"));
        popup.add(new JSeparator());
        undoItem = menuOption("Undo", popup, e -> ta.pop());
    }

    private JMenuItem menuOption (String name, JComponent men, ActionListener e)
    {
        JMenuItem mi = new JMenuItem(name);
        mi.addActionListener(e);
        men.add(mi);
        return mi;
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
        menuOption("Change tab color ...", men, e -> {
            JTabbedPane tp = ta.getTpane();
            int idx = ta.getTabIndex();
            Color col = JColorChooser.showDialog(null,
                    "Tab Color", tp.getBackground());
            tp.setBackgroundAt(idx, col);
        });
        menuOption("Toggle Word Wrap", men, e -> {
            ta.setLineWrap(!ta.getLineWrap());
            ta.setWrapStyleWord(true);
        });
        menuOption("Toggle Write Lock", men, e -> ta.setEditable(!ta.isEditable()));
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

    private void saveTextFunc (MyTextArea ta, String encoding)
    {
        String defFile = ta.getTpane().getTitleAt(ta.getTabIndex());
        String fname = new SingleInputDialog().start(
                "Path and name of File",
                "c:" + File.separator + defFile + "-" + encoding + ".txt");
        if (!fname.isEmpty())
        {
            TextAreaTools.saveAsText(ta, fname, encoding);
        }
    }

    private JMenu textOutputSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);
        menuOption("Save as ASCII", men, e -> saveTextFunc(ta,"US-ASCII"));
        menuOption("Save as UTF8", men, e ->  saveTextFunc(ta,"UTF-8"));
        menuOption("Save as ISO-8859-1", men, e ->  saveTextFunc(ta,"ISO-8859-1"));
        menuOption("Save as UTF-16BE", men, e ->  saveTextFunc(ta,"UTF-16BE"));
        menuOption("Save as UTF-16LE", men, e ->  saveTextFunc(ta,"UTF-16LE"));
        menuOption("Save as UTF-16", men, e ->  saveTextFunc(ta,"UTF-16"));
        return men;
    }

    private JMenu textSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);

        menuOption("Replace ...", men, e -> {
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
            TextAreaTools.centerText(ta);
        });
        menuOption("Trim Lines", men, e -> {
            TextAreaTools.trimLines(ta);
        });
        menuOption("Remove left", men, e -> {
            String s = new SingleInputDialog().start("How many chars", "");
            try
            {
                int num = Integer.parseInt(s);
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
                TextAreaTools.insertLeft(ta, s);
            }
            catch (NumberFormatException e1)
            {
                System.out.println(e1);
            }
        });
        menuOption("Line Number", men, e -> {
            TextAreaTools.numberText(ta);
        });
        menuOption("Roman Line Number", men, e -> {
            TextAreaTools.romanNumberText(ta);
        });
        menuOption("Upcase", men, e -> {
            ta.setText(ta.getText().toUpperCase());
        });
        menuOption("Downcase", men, e -> {
            ta.setText(ta.getText().toLowerCase());
        });
        menuOption("Capitalize", men, e -> {
            TextAreaTools.capitalize(ta);
        });
        menuOption("Reverse", men, e -> {
            String rev = new StringBuilder(ta.getText()).reverse().toString();
            ta.setText(rev);
        });

        return men;
    }

    private JMenu codingSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);

        menuOption("SHA-256", men, e -> {
            String st = new SHA256().transform(ta.getText());
            ta.append("\n" + st);
        });
        menuOption("SHA-1", men, e -> {
            String st = new SHA1().transform(ta.getText());
            ta.append("\n" + st);
        });
        menuOption("MD4", men, e -> {
            String st = new MD4().transform(ta.getText());
            ta.append("\n" + st);
        });
        menuOption("MD5", men, e -> {
            String st = new MD5().transform(ta.getText());
            ta.append("\n" + st);
        });
        menuOption("CRC16", men, e -> {
            String st = new CRC16CCITT().transform(ta.getText());
            ta.append("\n" + st);
        });
        men.add(new JSeparator());
        menuOption("UrlEncode", men, e -> {
            String st = new UrlEncodeUTF8().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("UrlDecode", men, e -> {
            String st = new UrlEncodeUTF8().retransform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("Rot13", men, e -> {
            String st = new Rot13().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("Reverse Rot13", men, e -> {
            String st = new Rot13().retransform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("Base64", men, e -> {
            String st = new Base64().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("Reverse Base64", men, e -> {
            String st = new Base64().retransform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("GrayEncode", men, e -> {
            String st = new GrayCode().transform(ta.getText());
            ta.setText(st);
        });
        menuOption("GrayDecode", men, e -> {
            String st = new GrayCode().retransform(ta.getText());
            ta.setText(st);
        });
        men.add(new JSeparator());
        menuOption("toHexBytes", men, e -> {
            String st = new HexBytes().transform(ta.getText());
            ta.setFastText(st);
        });
        menuOption("fromHexBytes", men, e -> {
            String st = new HexBytes().retransform(ta.getText());
            ta.setFastText(st);
        });

        men.add(new JSeparator());
        menuOption("toAsciiBytes (LOSSY!)", men, e -> {
            String st = new AsciiBytes().transform(ta.getText());
            ta.setFastText(st);
        });
        menuOption("fromAsciiBytes", men, e -> {
            String st = new AsciiBytes().retransform(ta.getText());
            ta.setFastText(st);
        });

        men.add(new JSeparator());
        menuOption("toBinary16", men, e -> {
            String st = new Binary(16).transform(ta.getText());
            ta.setFastText(st);
        });
        menuOption("fromBinary16", men, e -> {
            String st = new Binary(16).retransform(ta.getText());
            ta.setFastText(st);
        });
        menuOption("toBinary8", men, e -> {
            String st = new Binary(8).transform(ta.getText());
            ta.setFastText(st);
        });
        menuOption("fromBinary8", men, e -> {
            String st = new Binary(8).retransform(ta.getText());
            ta.setFastText(st);
        });

        return men;
    }

    private JMenu cryptoSubMenu (MyTextArea ta, String title)
    {
        JMenu men = new JMenu(title);

        menuOption("HagelinCrypt", men, e -> {
            String st = Crypto.cryptHagelin(ta.getText());
            ta.setFastText(st);
        });
        menuOption("PittyCrypt", men, e -> {
            String st = Crypto.cryptPitty(ta.getText());
            ta.setFastText(st);
        });
        menuOption("Peter1Crypt", men, e -> {
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
        menuOption("AES", men, e -> {
            String pass = new SingleInputDialog().start("Enter Password", "");
            if (!pass.isEmpty())
            {
                try
                {
                    byte[] pwh = Crypto.passwordHash(pass.getBytes(StandardCharsets.UTF_8));
                    String s = Crypto.cryptAes256(pwh, ta.getText());
                    ta.setFastText(s);
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
