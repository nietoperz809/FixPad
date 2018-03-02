import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
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
        BufferedImage bimage = new BufferedImage(ta.getWidth(), ta.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        Printable pr = ta.getPrintable(null, null);
        pr.print(bGr, new PageFormat(), 0);
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
