import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

public class FixPad
{
    private JPanel panel1;
    private final ArrayList<JTextArea> list = new ArrayList<>();
//    private JTextArea textArea1;
//    private JTextArea textArea2;
//    private JTextArea textArea3;
    private FileManager fman = new FileManager();

    private void setDefaultAttributes ()
    {
        for (JTextArea ta : list)
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
            ta.addMouseListener(new MouseHandler(ta));
        }
    }

    private void startFman ()
    {
        setDefaultAttributes();
        fman.put(list);
        fman.start();
    }

    public static void main (String[] args)
    {
        JFrame frame = new JFrame("FixPad");
        FixPad pad = new FixPad();
        frame.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened (WindowEvent e)
            {

            }

            @Override
            public void windowClosing (WindowEvent e)
            {
                pad.fman.stop();
            }

            @Override
            public void windowClosed (WindowEvent e)
            {

            }

            @Override
            public void windowIconified (WindowEvent e)
            {

            }

            @Override
            public void windowDeiconified (WindowEvent e)
            {

            }

            @Override
            public void windowActivated (WindowEvent e)
            {

            }

            @Override
            public void windowDeactivated (WindowEvent e)
            {

            }
        });
        frame.setContentPane(pad.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        pad.startFman();
        frame.setVisible(true);
    }

    {
        $$$setupUI$$$();
    }

    private void $$$setupUI$$$ ()
    {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        JTabbedPane tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, BorderLayout.CENTER);

        for (int s=0; s<11; s++)
        {
            final JPanel panel2 = new JPanel();
            panel2.setLayout(new CardLayout(0, 0));
            tabbedPane1.addTab("Editor "+s, panel2);
            final JScrollPane scrollPane1 = new JScrollPane();
            panel2.add(scrollPane1, "Card1");
            JTextArea jt = new JTextArea();
            scrollPane1.setViewportView(jt);
            list.add (jt);
        }
//
//        final JPanel panel2 = new JPanel();
//        panel2.setLayout(new CardLayout(0, 0));
//        tabbedPane1.addTab("Untitled", panel2);
//        final JScrollPane scrollPane1 = new JScrollPane();
//        panel2.add(scrollPane1, "Card1");
//        textArea1 = new JTextArea();
//        scrollPane1.setViewportView(textArea1);
//
//        final JPanel panel3 = new JPanel();
//        panel3.setLayout(new CardLayout(0, 0));
//        tabbedPane1.addTab("Untitled", panel3);
//        final JScrollPane scrollPane2 = new JScrollPane();
//        panel3.add(scrollPane2, "Card1");
//        textArea2 = new JTextArea();
//        scrollPane2.setViewportView(textArea2);
//
//        final JPanel panel4 = new JPanel();
//        panel4.setLayout(new CardLayout(0, 0));
//        tabbedPane1.addTab("Untitled", panel4);
//        final JScrollPane scrollPane3 = new JScrollPane();
//        panel4.add(scrollPane3, "Card1");
//        textArea3 = new JTextArea();
//        scrollPane3.setViewportView(textArea3);
    }
}


