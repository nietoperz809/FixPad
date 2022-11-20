package common;

import database.DBHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static common.Tools.invertColor;

public class FixPad {
    public static final FileManager fman = new FileManager();
    private final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static JFrame mainFrame;
    public static JTabbedPane mainTab;
    private static JLabel statusBar;
    private final ArrayList<MyTextArea> list = new ArrayList<>();
    private final JPanel panel1;

    public FixPad() {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        mainTab = new JTabbedPane() {
            public Color getForegroundAt(int index) {
                if (getSelectedIndex() == index) return Color.BLACK;
                return invertColor(getBackgroundAt(index));
            }

        };
        panel1.add(mainTab, BorderLayout.CENTER);
        statusBar = new JLabel("...");
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.BLACK);
        statusBar.setForeground(Color.GREEN);
        panel1.add(statusBar, BorderLayout.SOUTH);
    }

    public static void setStatusBar(String txt) {
        Calendar cal = Calendar.getInstance();
        String time = dateFormat.format(cal.getTime()) + ": ";

        SwingUtilities.invokeLater(() -> statusBar.setText(time + txt));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
        {
            mainFrame = new JFrame("FixPad");
            FixPad pad = new FixPad();
            pad.setupTabs();
            mainFrame.setContentPane(pad.panel1);
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    fman.stop();
                    DBHandler.getInst().closeConnection();
                }
            });
            mainFrame.setVisible(true);
            pad.startFman();
        });
    }

    private void startFman() {
        fman.put(list);
        fman.start();
    }

    private void enableDrops(MyTextArea jt) {
        new DropTarget(jt, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent event) {
                event.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = event.getTransferable();
                DataFlavor[] flavors = transferable.getTransferDataFlavors();
                for (DataFlavor flavor : flavors) {
                    try {
                        if (flavor.isFlavorJavaFileListType()) {
                            @SuppressWarnings("unchecked")
                            java.util.List<File> files = (List<File>) transferable.getTransferData(flavor);
                            File f = files.get(0);
                            String content = Files.readString(
                                    f.toPath());
                            jt.setText(content);
                            return; // only one file
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void setupTabs() {
        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        for (int index = 0; index < 21; index++) {
            final JPanel panel2 = new JPanel();
            panel2.setLayout(new CardLayout(0, 0));
            mainTab.addTab("E:" + index, panel2);
            final JScrollPane scrollPane1 = new JScrollPane();
            panel2.add(scrollPane1, "Card1");
            MyTextArea jt = new MyTextArea();
            jt.setCursor(cur);
            jt.setAditionalTabData(mainTab, index);
            enableDrops(jt);
            scrollPane1.setViewportView(jt);
            list.add(jt);

            jt.setBackground(new Color(12, 14, 16));
            jt.setForeground(Color.WHITE);
            BlockCaret mc = new BlockCaret();
            jt.setCaret(mc);
            mc.startFlashing();
            jt.setCaretColor(Color.ORANGE);
            jt.addMouseListener(new PopupMenuHandler(jt));
        }
    }
}

/*
    private void setupTabs() {
        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        for (int index = 0; index < 21; index++) {
            final JPanel panel2 = new JPanel();
            panel2.setLayout(new CardLayout(0, 0));
            mainTab.addTab("E:" + index, panel2);
            final JScrollPane scrollPane1 = new JScrollPane();
            panel2.add(scrollPane1, "Card1");
            MyTextArea jt = new MyTextArea();
            jt.setCursor(cur);
            jt.setAditionalTabData(mainTab, index);
            enableDrops(jt);
            scrollPane1.setViewportView(jt);
            list.add(jt);

            jt.setBackground(new Color(12, 14, 16));
            jt.setForeground(Color.WHITE);
            BlockCaret mc = new BlockCaret();
            jt.setCaret(mc);
            mc.startFlashing();
            jt.setCaretColor(Color.ORANGE);
            jt.addMouseListener(new PopupMenuHandler(jt));
        }
    }


 */
