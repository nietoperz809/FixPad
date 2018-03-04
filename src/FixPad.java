import javax.swing.*;
import javax.swing.text.DefaultCaret;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FixPad
{
    private final ArrayList<MyTextArea> list = new ArrayList<>();
    private final FileManager fman = new FileManager();
    private JPanel panel1;

    {
        setupUI();
    }

    public static void main (String[] args)
    {
        JFrame frame = new JFrame("FixPad");
        FixPad pad = new FixPad();
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing (WindowEvent e)
            {
                pad.fman.stop();
            }
        });
        frame.setContentPane(pad.panel1);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        pad.startFman();
        frame.setVisible(true);
    }

    private void startFman ()
    {
        setDefaultAttributes();
        fman.put(list);
        fman.start();
    }

    private void setDefaultAttributes ()
    {
        for (MyTextArea ta : list)
        {
            ta.setBackground(new Color(12, 14, 16));
            ta.setForeground(Color.WHITE);
            Font f = Tools.getFont("Consolas", -1, 20, ta.getFont());
            if (f != null)
            {
                ta.setFont(f);
            }
            BlockCaret mc = new BlockCaret();
            mc.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            ta.setCaret(mc);
            ta.setCaretColor(Color.ORANGE);
            ta.addMouseListener(new PopupMenuHandler(ta));
        }
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
                            String content = new String(Files.readAllBytes(f.toPath()), Charset.defaultCharset().name());
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

    private void setupUI ()
    {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        JTabbedPane tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, BorderLayout.CENTER);

        for (int s = 0; s < 21; s++)
        {
            final JPanel panel2 = new JPanel();
            panel2.setLayout(new CardLayout(0, 0));
            tabbedPane1.addTab("E:" + s, panel2);
            final JScrollPane scrollPane1 = new JScrollPane();
            panel2.add(scrollPane1, "Card1");
            MyTextArea jt = new MyTextArea();
            enableDrops(jt);
            scrollPane1.setViewportView(jt);
            list.add(jt);
        }
    }
}


