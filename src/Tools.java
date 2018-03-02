import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;

public class Tools
{
    static Font getFont (String fontName, int style, int size, Font currentFont)
    {
        if (currentFont == null)
        {
            return null;
        }
        String resultName;
        if (fontName == null)
        {
            resultName = currentFont.getName();
        }
        else
        {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1'))
            {
                resultName = fontName;
            }
            else
            {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    public static BufferedImage toImage (JTextArea ta) throws PrinterException
    {
//        BufferedImage fake1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D fake2 = fake1.createGraphics();
//        FontMetrics fm = fake2.getFontMetrics(ta.getFont());

        Rectangle rc = ta.getVisibleRect();
        BufferedImage bimage = new BufferedImage(rc.width,
                rc.height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();

        bGr.setColor(ta.getBackground());
        bGr.fillRect(0, 0, rc.width, rc.height);
        
        Printable pr = ta.getPrintable(null, null);
        PageFormat form = new PageFormat();
        //form.setOrientation(PageFormat.PORTRAIT);
        Paper pap = new Paper();
        pap.setImageableArea(3,3, 2000, 2000);
        form.setPaper(pap);
        pr.print(bGr, form, 0);
        return bimage;
    }

    public static boolean saveToClipboard (JTextArea ta)
    {
        try
        {
            BufferedImage bimage = toImage (ta);
            new ClipboardImage(bimage);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    public static boolean saveAsImage (JTextArea ta, String filePath)
    {
        try
        {
            BufferedImage bimage = toImage (ta);
            saveImage(filePath, bimage,false);
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return false;
        }
    }

    public static void saveImage (String name, BufferedImage img, boolean jpg) throws IOException
    {
        if (jpg)
        {
            if (!name.endsWith(".jpg"))
            {
                name = name + ".jpg";
            }
        }
        else
        {
            if (!name.endsWith(".png"))
            {
                name = name + ".png";
            }
        }
        File f = new File(name);
        if (jpg)
        {
            ImageIO.write(img, "jpg", f);
        }
        else
        {
            ImageIO.write(img, "png", f);
        }
    }
}
