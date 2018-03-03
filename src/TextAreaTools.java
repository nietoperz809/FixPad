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
    public static ArrayList<String> TaToList (MyTextArea ta)
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

    public static void numberText (MyTextArea ta)
    {
        ArrayList<String> list = TaToList(ta);
        StringBuffer sb = new StringBuffer();
        int num = 1;
        for (String s : list)
        {
            sb.append(num++).append(". ").append(s);
        }
        ta.setText(sb.toString());
    }

    public static void romanNumberText (MyTextArea ta)
    {
        ArrayList<String> list = TaToList(ta);
        StringBuffer sb = new StringBuffer();
        int num = 1;
        for (String s : list)
        {
            String rnum = RomanNumber.toRoman(num++);
            sb.append(rnum).append(". ").append(s);
        }
        ta.setText(sb.toString());
    }


    public static void centerText (MyTextArea ta)
    {
        BufferedImage fake1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D fake2 = fake1.createGraphics();
        FontMetrics fm = fake2.getFontMetrics(ta.getFont());

        int lines = ta.getLineCount();
        ArrayList<String> list = TaToList(ta);
        alignLines (list, fm, ta);
    }

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
