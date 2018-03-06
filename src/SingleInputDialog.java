import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SingleInputDialog extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;

    public SingleInputDialog ()
    {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing (WindowEvent e)
            {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setModal(true);
        textField1.requestFocusInWindow();
    }

    private void onOK ()
    {
        // add your code here
        dispose();
    }

    private void onCancel ()
    {
        textField1.setText("");
        dispose();
    }

    public String start (String title, String defaultval)
    {
        textField1.setCaret(new BlockCaret());
        textField1.setCaretColor(Color.YELLOW);
        textField1.setText(defaultval);
        setTitle(title);
        pack();
        setVisible(true);
        return textField1.getText();
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
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setBackground(new Color(-16777216));
        contentPane.setPreferredSize(new Dimension(300, 100));
        contentPane.setRequestFocusEnabled(false);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel1.setBackground(new Color(-16777216));
        panel1.setPreferredSize(new Dimension(200, 50));
        contentPane.add(panel1, BorderLayout.SOUTH);
        buttonOK = new JButton();
        buttonOK.setPreferredSize(new Dimension(100, 31));
        buttonOK.setText("OK");
        panel1.add(buttonOK);
        final JLabel label1 = new JLabel();
        label1.setPreferredSize(new Dimension(30, 10));
        label1.setText("");
        panel1.add(label1);
        buttonCancel = new JButton();
        buttonCancel.setPreferredSize(new Dimension(100, 31));
        buttonCancel.setText("Cancel");
        panel1.add(buttonCancel);
        textField1 = new JTextField();
        textField1.setBackground(new Color(-15987184));
        Font textField1Font = Tools.getFont("Arial", -1, 20, textField1.getFont());
        if (textField1Font != null)
        {
            textField1.setFont(textField1Font);
        }
        textField1.setForeground(new Color(-1));
        textField1.setHorizontalAlignment(0);
        textField1.setOpaque(false);
        textField1.setPreferredSize(new Dimension(300, 50));
        contentPane.add(textField1, BorderLayout.CENTER);
        final JLabel label2 = new JLabel();
        label2.setPreferredSize(new Dimension(11, 11));
        label2.setText(" ");
        contentPane.add(label2, BorderLayout.WEST);
        final JLabel label3 = new JLabel();
        label3.setPreferredSize(new Dimension(11, 11));
        label3.setText(" ");
        contentPane.add(label3, BorderLayout.EAST);
        final JLabel label4 = new JLabel();
        label4.setPreferredSize(new Dimension(11, 11));
        label4.setText("");
        contentPane.add(label4, BorderLayout.NORTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$ ()
    {
        return contentPane;
    }

    //    public static void main (String[] args)
//    {
//        String s = new SingleInputDialog().start("Enter Password");
//        System.out.println(s);
//    }

}
