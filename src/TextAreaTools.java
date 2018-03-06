import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;

public class TextAreaTools
{
    /**
     * Create a List of Lines of TextArea
     * @param ta TextArea
     * @return List of Lines
     */
    public static ArrayList<String> TaLinesToList (MyTextArea ta)
    {
        int lines = ta.getLineCount();
        ArrayList<String> list = new ArrayList<>();
        try
        {
            for (int i = 0; i < lines; i++)
            {
                int start = ta.getLineStartOffset(i);
                int end = ta.getLineEndOffset(i);

                String line = ta.getText(start, end - start);
                        //.replace("\n","");
                list.add (line);
            }
        }
        catch (BadLocationException e)
        {
            System.out.println(e);
        }
        return list;
    }

    public static void trimLines(MyTextArea ta)
    {
        ArrayList<String> list = TaLinesToList(ta);
        StringBuilder sb = new StringBuilder();
        for (String s : list)
        {
            if (Tools.isNullOrWhiteSpace(s))
                continue;
            sb.append(s.trim()).append('\n');
        }
        sb.deleteCharAt(sb.length()-1);
        ta.setText(sb.toString());
    }

    public static void removeLeft (MyTextArea ta, int num)
    {
        ArrayList<String> list = TaLinesToList(ta);
        StringBuilder sb = new StringBuilder();
        for (String s : list)
        {
            if (s.length() > num)
                s = s.substring(num);
            sb.append(s);
        }
        ta.setText(sb.toString());
    }

    public static void insertLeft (MyTextArea ta, String add)
    {
        ArrayList<String> list = TaLinesToList(ta);
        StringBuilder sb = new StringBuilder();
        for (String s : list)
        {
            sb.append(add).append(s);
        }
        ta.setText(sb.toString());
    }

    /**
     * Put a line number in front of every Line
     * @param ta Textarea that is changed
     */
    public static void numberText (MyTextArea ta)
    {
        ArrayList<String> list = TaLinesToList(ta);
        StringBuffer sb = new StringBuffer();
        int num = 1;
        for (String s : list)
        {
            String s2 = Tools.ensureMinLength(num+". ", 5);
            sb.append(s2).append(s);
            num++;
        }
        ta.setText(sb.toString());
    }

    /**
     * Put a Roman line number in front of every Line
     * @param ta Textarea that is changed
     */
    public static void romanNumberText (MyTextArea ta)
    {
        ArrayList<String> list = TaLinesToList(ta);
        StringBuffer sb = new StringBuffer();
        int num = 1;
        for (String s : list)
        {
            String rnum = RomanNumber.toRoman(num++);
            rnum = Tools.ensureMinLength(rnum, 10);
            sb.append(rnum).append(". ").append(s);
        }
        ta.setText(sb.toString());
    }

    public static void capitalize (MyTextArea ta)
    {
        ArrayList<String> list = TaLinesToList(ta);
        StringBuffer sb = new StringBuffer();
        for (String s : list)
        {
            StringBuffer b2 = new StringBuffer(s);
            for (int n=0; n<b2.length()-1; n++)
            {
                char c1 = b2.charAt(n);
                char c2 = b2.charAt(n+1);
                if (Character.isWhitespace(c1))
                {
                    if (Character.isLetter(c2))
                    {
                        b2.setCharAt(n+1, Character.toUpperCase(c2));
                    }
                }
            }
            sb.append (b2);
        }
        ta.setText(sb.toString());
    }

    /**
     * Centers Text of a TextArea
     * @param ta TextArea that is changed
     */
    public static void centerText (MyTextArea ta)
    {
        BufferedImage fake1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D fake2 = fake1.createGraphics();
        FontMetrics fm = fake2.getFontMetrics(ta.getFont());
        ArrayList<String> list = TaLinesToList(ta);
        alignLines (list, fm, ta);
    }

    /**
     * Helper function for the above one
     * @param list List of lines coming from TextArea
     * @param fm Fontmetrics to calculate line length
     * @param ta TextArea that is changed
     */
    private static void alignLines (ArrayList<String> list, FontMetrics fm, MyTextArea ta)
    {
        String leading = "      ";
        int longest = -1;
        for (int n=0; n<list.size(); n++)
        {
            String s = list.get(n).replace("\n","").trim();
            list.set(n, s);
            if (fm.stringWidth(s) > longest)
                longest = fm.stringWidth(s);
        }
        for (int n=0; n<list.size(); n++)
        {
            String s = list.get(n);
            if (fm.stringWidth(s) >= longest)
                continue;
            while (fm.stringWidth(s) < longest)
                s = ' '+s+' ';
            list.set(n, s);
        }
        StringBuilder sb = new StringBuilder();
        for (String s : list)
        {
            sb.append(leading).append(s).append('\n');
        }
        ta.setText (sb.toString());
    }

    /**
     * Create Image from Textarea and put in to the Clipboard
     * @param ta Source Textarea
     * @return false if an error occurs
     */
    public static boolean saveImageToClipboard (MyTextArea ta)
    {
        try
        {
            BufferedImage bimage = toImage(ta);
            new ClipboardImage(bimage);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Write TextArea as image to disk
     * @param ta Source TextArea
     * @param filePath Path and file name of the new image
     * @return false if smth. gone wrong
     */
    public static boolean saveAsImage (MyTextArea ta, String filePath)
    {
        try
        {
            BufferedImage bimage = toImage(ta);
            Tools.saveImage(filePath, bimage, false);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Make image from TextArea
     * @param ta Source Textarea
     * @return The Image
     * @throws PrinterException if conversion fails
     */
    private static BufferedImage toImage (MyTextArea ta) throws PrinterException
    {
        Rectangle rc = ta.getVisibleRect();
        BufferedImage bimage = new BufferedImage(rc.width,
                rc.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();

        bGr.setColor(ta.getBackground());
        bGr.fillRect(0, 0, rc.width, rc.height);

        Printable pr = ta.getPrintable(null, null);
        PageFormat form = new PageFormat();
        form.setOrientation(PageFormat.PORTRAIT);
        Paper pap = new Paper();
        pap.setImageableArea(3, 3, 2000, 2000);
        pap.setSize(rc.width, rc.height);
        form.setPaper(pap);
        pr.print(bGr, form, 0);
        return bimage;
    }
}
