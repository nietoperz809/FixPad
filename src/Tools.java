import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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


    static void saveImage (String name, BufferedImage img, boolean jpg) throws IOException
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

    public static int findAndSelect (java.awt.List li, String s)
    {
        for (int i = 0; i < li.getItemCount(); i++)
        {
            String item = li.getItem(i);
            if (item.equals(s))
            {
                li.select(i);
                return i;
            }
        }
        return -1;
    }

    public static String ensureMinLength (String in, int min)
    {
        StringBuilder inBuilder = new StringBuilder(in);
        return ensureMinLength(inBuilder, min);
    }

    public static String ensureMinLength (StringBuilder in, int min)
    {
        while (in.length() < min)
            in.insert (0,' ');
        return in.toString();
    }

}
