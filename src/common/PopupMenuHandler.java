package common;

import bitmap.ImageNegative;
import crypto.Crypto;
import database.DBHandler;
import dialogs.SearchBox;
import dialogs.SingleInputDialog;
import transform.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.prefs.Preferences;


class CheckBoxAccessory extends JComponent {
    JCheckBox virtualCheckBox;
    boolean checkBoxInit = false;

    int preferredWidth = 150;
    int preferredHeight = 100;//Mostly ignored as it is
    int checkBoxPosX = 5;
    int checkBoxPosY = 20;
    int checkBoxWidth = preferredWidth;
    int checkBoxHeight = 20;

    public CheckBoxAccessory()
    {
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        virtualCheckBox = new JCheckBox("Negative image", checkBoxInit);
        virtualCheckBox.setBounds(checkBoxPosX, checkBoxPosY, checkBoxWidth, checkBoxHeight);
        this.add(virtualCheckBox);
    }

    public boolean isBoxSelected()
    {
        return virtualCheckBox.isSelected();
    }
}

public class PopupMenuHandler extends MouseInputAdapter
{
    private final JPopupMenu popup = new JPopupMenu();
    public final JMenuItem undoItem;

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
        popup.add(new JSeparator());
        menuOption("Database location", popup, e -> this.databaseSelect());
    }

    private void databaseSelect() {
        String key = "Database.lastDirectory";
        Class clazz = FixPad.mainFrame.getClass();
        String lastDirectory = Preferences.userNodeForPackage (clazz)
                .get(key, System.getProperty("user.home"));
        JFileChooser fc = new JFileChooser();
        File lastPath = new File(lastDirectory);
        if (lastPath.exists() && lastPath.isDirectory()) {
            fc.setCurrentDirectory(new File(lastDirectory));
        }
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter("Database", new String[]{"db"});
        fc.setFileFilter(filter);
        fc.setMultiSelectionEnabled(true);
        if (fc.showOpenDialog(FixPad.mainFrame) == JFileChooser.APPROVE_OPTION) {
            lastDirectory = fc.getCurrentDirectory().getPath();
            Preferences.userNodeForPackage(clazz).put(key, lastDirectory);
            FixPad.__inst.removeAllTabs();
            DBHandler.setNewURL(lastDirectory, fc.getSelectedFile().getName());
            FixPad.__inst.mainFrame.setTitle("Fixpad: "+fc.getSelectedFile().getName());
            FixPad.__inst.setupTabs();
        }

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

        menuOption("Background Image", men, e -> {
            String lastDirectory = Preferences.userNodeForPackage(this.getClass()).get("Images.lastDirectory", System.getProperty("user.home"));
            JFileChooser fc = new JFileChooser();
            File lastPath = new File(lastDirectory);
            if (lastPath.exists() && lastPath.isDirectory()) {
                fc.setCurrentDirectory(new File(lastDirectory));
            }
            CheckBoxAccessory cba = new CheckBoxAccessory();
            fc.setAccessory(cba);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files",
                    "jpg", "jpeg", "png", "bmp");
            fc.setFileFilter(filter);
            if (fc.showOpenDialog(ta) == JFileChooser.APPROVE_OPTION) {
                lastDirectory = fc.getCurrentDirectory().getPath();
                Preferences.userNodeForPackage(this.getClass()).put("Images.lastDirectory", lastDirectory);
                try {
                    BufferedImage img = ImageIO.read(fc.getSelectedFile());
                    ta.setBackImg(img, cba.isBoxSelected());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

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
            ta.removeBKImage();
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
        menuOption("Image to clipboard", men, e -> TextAreaTools.saveImageToClipboard(ta));

        return men;
    }

    private void saveTextFunc (MyTextArea ta, String encoding)
    {
//        Object o = ta.getParent();
//        System.out.println(o);

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
            SearchBox.SbResult res = new SearchBox().start();
            if (res != null)
            {
                if (!(res.from.isEmpty() || res.to.isEmpty()))
                {
                    String s = ta.getText().replace(res.from, res.to);
                    ta.setText(s);
                }
            }
        });
        menuOption("Center Text", men, e -> TextAreaTools.centerText(ta));
        menuOption("Trim Lines", men, e -> TextAreaTools.trimLines(ta));
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
        menuOption("Line Number", men, e -> TextAreaTools.numberText(ta));
        menuOption("Roman Line Number", men, e -> TextAreaTools.romanNumberText(ta));
        menuOption("Upcase", men, e -> ta.setText(ta.getText().toUpperCase()));
        menuOption("Downcase", men, e -> ta.setText(ta.getText().toLowerCase()));
        menuOption("Capitalize", men, e -> TextAreaTools.capitalize(ta));
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
        menuOption("RevMiddle", men, e -> {
            String st = new ReverseMiddle().transform(ta.getText());
            ta.setText(st);
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
            String pass = new SingleInputDialog().start("Enter Password", "");
            if (!pass.isEmpty())
            {
                String st = Crypto.cryptPitty(ta.getText(), pass);
                ta.setFastText(st);
            }
        });
        menuOption("Peter1Crypt", men, e -> {
            String pass = new SingleInputDialog().start("Enter Password", "");
            if (!pass.isEmpty())
            {
                try
                {
                    byte[] pwh = Crypto.passwordHash(pass.getBytes(StandardCharsets.UTF_8));
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
