package common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Tools
{
    public static Font getFont (String fontName, int style, int size, Font currentFont)
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

    public static boolean isNullOrWhiteSpace(String value)
    {
        return value == null || value.trim().isEmpty();
    }

    public static byte[] toRawByteArray (String in)
    {
        char[] chars = in.toCharArray();
        byte[] bytes = new byte[chars.length*2];
        for(int i=0;i<chars.length;i++)
        {
            bytes[i*2] = (byte) (chars[i] >> 8);
            bytes[i*2+1] = (byte) chars[i];
        }
        return bytes;
    }

    public static char[] fromRawByteArray (byte[] bytes)
    {
        char[] chars2 = new char[bytes.length/2];
        for(int i=0;i<chars2.length;i++)
            chars2[i] = (char) ((bytes[i*2] << 8) + (bytes[i*2+1] & 0xFF));
        return chars2;
    }

    public static Image loadImageFromRessource (String name)
    {
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(name);
        if (is == null)
        {
            System.out.println("Wrong resource name/path?");
            return null;
        }
        try
        {
            return ImageIO.read(is);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
