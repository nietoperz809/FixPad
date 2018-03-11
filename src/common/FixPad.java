package common;

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
import java.util.ArrayList;
import java.util.List;

public class FixPad
{
    private final ArrayList<MyTextArea> list = new ArrayList<>();
    private final FileManager fman = new FileManager();
    private JPanel panel1;

    public FixPad()
    {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        mainTab = new JTabbedPane();
//        mainTab.addChangeListener(e ->
//        {
//            JTabbedPane tp = (JTabbedPane) e.getSource();
//            int idx = tp.getSelectedIndex();
//            //System.out.println(tp.getSelectedIndex());
//            tp.setBackgroundAt(idx, Color.ORANGE);
//            Component cc = tp.getComponentAt(idx);
//            cc.invalidate();
//            cc.repaint();
//        });
        panel1.add(mainTab, BorderLayout.CENTER);
        setupTabs();
    }

    public static JFrame mainFrame;
    public static JTabbedPane mainTab;

    public static void main (String[] args)
    {
        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        SwingUtilities.invokeLater(() ->
        {
            mainFrame = new JFrame("FixPad");
            FixPad pad = new FixPad();
            mainFrame.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing (WindowEvent e)
                {
                    pad.fman.stop();
                }
            });
            mainFrame.setContentPane(pad.panel1);
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            pad.startFman();
        });
    }

    private void startFman ()
    {
        //setDefaultAttributes();
        fman.put(list);
        fman.start();
    }

    private void enableDrops (MyTextArea jt)
    {
        new DropTarget(jt, new DropTargetAdapter()
        {
            @Override
            public void drop (DropTargetDropEvent event)
            {
                event.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = event.getTransferable();
                DataFlavor[] flavors = transferable.getTransferDataFlavors();
                for (DataFlavor flavor : flavors)
                {
                    try
                    {
                        if (flavor.isFlavorJavaFileListType())
                        {
                            @SuppressWarnings("unchecked")
                            java.util.List<File> files = (List<File>) transferable.getTransferData(flavor);
                            File f = files.get(0);
                            String content = new String(Files.readAllBytes(f.toPath()), "UTF-8");// Charset.defaultCharset().name());
                            jt.setText(content);
                            return; // only one file
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println(e);
                    }
                }
            }
        });
    }

    private void setupTabs ()
    {
        Cursor cur = new Cursor(Cursor.HAND_CURSOR);
        for (int index = 0; index < 21; index++)
        {
            final JPanel panel2 = new JPanel();
            panel2.setLayout(new CardLayout(0, 0));
            mainTab.addTab("E:" + index, panel2);
            final JScrollPane scrollPane1 = new JScrollPane();
            panel2.add(scrollPane1, "Card1");
            MyTextArea jt = new MyTextArea();
            jt.setCursor (cur);
            jt.setTabData(mainTab, index);
            enableDrops(jt);
            scrollPane1.setViewportView(jt);
            list.add(jt);

            jt.setBackground(new Color(12, 14, 16));
            jt.setForeground(Color.WHITE);
            Font f = Tools.getFont("Consolas", -1, 20, jt.getFont());
            if (f != null)
            {
                jt.setFont(f);
            }
            BlockCaret mc = new BlockCaret();
            jt.setCaret(mc);
            //mc.startFlashing();
            jt.setCaretColor(Color.ORANGE);
            jt.addMouseListener(new PopupMenuHandler(jt));
        }
    }
}


