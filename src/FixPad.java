import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FixPad
{
    private JPanel panel1;
    private JTabbedPane tabbedPane1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JTextArea textArea3;
    private FileManager fman = new FileManager();

    private void adjustTextArea (JTextArea ta)
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

    private void startFman ()
    {
        adjustTextArea(textArea1);
        adjustTextArea(textArea2);
        adjustTextArea(textArea3);
        fman.put(textArea1);
        fman.put(textArea2);
        fman.put(textArea3);
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
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$ ()
    {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        tabbedPane1 = new JTabbedPane();
        panel1.add(tabbedPane1, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new CardLayout(0, 0));
        tabbedPane1.addTab("Untitled", panel2);
        textArea1 = new JTextArea();
        panel2.add(textArea1, "Card1");
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new CardLayout(0, 0));
        tabbedPane1.addTab("Untitled", panel3);
        textArea2 = new JTextArea();
        panel3.add(textArea2, "Card1");
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new CardLayout(0, 0));
        tabbedPane1.addTab("Untitled", panel4);
        textArea3 = new JTextArea();
        panel4.add(textArea3, "Card1");
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$ ()
    {
        return panel1;
    }
}


