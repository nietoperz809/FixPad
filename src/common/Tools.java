package common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Tools {
    public static Font getFont(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) {
            return null;
        }
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    static void saveImage(String name, BufferedImage img, boolean jpg) throws IOException {
        if (jpg) {
            if (!name.endsWith(".jpg")) {
                name = name + ".jpg";
            }
        } else {
            if (!name.endsWith(".png")) {
                name = name + ".png";
            }
        }
        File f = new File(name);
        if (jpg) {
            ImageIO.write(img, "jpg", f);
        } else {
            ImageIO.write(img, "png", f);
        }
    }

    public static int findAndSelect(java.awt.List li, String s) {
        for (int i = 0; i < li.getItemCount(); i++) {
            String item = li.getItem(i);
            if (item.equals(s)) {
                li.select(i);
                return i;
            }
        }
        return -1;
    }

    public static String ensureMinLength(String in, int min) {
        StringBuilder inBuilder = new StringBuilder(in);
        return ensureMinLength(inBuilder, min);
    }

    public static String ensureMinLength(StringBuilder in, int min) {
        while (in.length() < min)
            in.insert(0, ' ');
        return in.toString();
    }

    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static byte[] toRawByteArray(String in) {
        char[] chars = in.toCharArray();
        byte[] bytes = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; i++) {
            bytes[i * 2] = (byte) (chars[i] >> 8);
            bytes[i * 2 + 1] = (byte) chars[i];
        }
        return bytes;
    }

    public static char[] fromRawByteArray(byte[] bytes) {
        char[] chars2 = new char[bytes.length / 2];
        for (int i = 0; i < chars2.length; i++)
            chars2[i] = (char) ((bytes[i * 2] << 8) + (bytes[i * 2 + 1] & 0xFF));
        return chars2;
    }

    public static Image loadImageFromRessource(String name) {
        InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(name);
        if (is == null) {
            System.out.println("Wrong resource name/path?");
            return null;
        }
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String glueStrings(ArrayList<String> in) {
        StringBuilder buff = new StringBuilder();
        for (String s : in)
            buff.append(s);
        return buff.toString();
    }

    public static ArrayList<String> fixedSplit(String in, int partsize) {
        partsize = partsize <= 0 ? 1 : partsize;
        ArrayList<String> list = new ArrayList<>();
        StringBuilder buff = new StringBuilder(in);

        for (int n = 0; n < in.length(); n += partsize) {
            try {
                int end = n + partsize;
                end = Math.min(end, in.length());
                String str = buff.substring(n, end);
                list.add(str);
            } catch (Exception e) {
                break;
            }
        }
        return list;
    }

    /**
     * Create bool array of case information
     *
     * @param in any String
     * @return array containing true if char is ucase, otherwise false
     */
    public static boolean[] getCases(String in) {
        boolean[] cs = new boolean[in.length()];
        for (int s = 0; s < in.length(); s++) {
            if (Character.isUpperCase(in.charAt(s)))
                cs[s] = true;
            else
                cs[s] = false;
        }
        return cs;
    }

    /**
     * Restore cases
     *
     * @param in    lowercase string
     * @param cases array of case information
     * @return string containing ucase and lcase chars
     */
    public static String adjustCases(String in, boolean[] cases) {
        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < in.length(); s++) {
            char c = in.charAt(s);
            if (cases[s])
                sb.append(Character.toUpperCase(c));
            else
                sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Zip all files in a folder (except other folders and zip files)
     * @param sourceDirPath Dir were source files are
     * @param zipFilePath Path to new Zip file
     * @throws IOException if smth gone wrong
     */
    public static void packToZip(String sourceDirPath, String zipFilePath) throws IOException {
        Path p;
        p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        if (!path.toString().endsWith(".zip")) {
                            ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                            try {
                                zs.putNextEntry(zipEntry);
                                Files.copy(path, zs);
                                zs.closeEntry();
                            } catch (IOException e) {
                                System.err.println(e);
                            }
                        }
                    });
        }
    }

    /**
     * Makes dir if it doesn't exist
     * @param path path of the dir
     * @return tue if dir created or already exists
     */
    public static boolean mkdir (String path)
    {
        File directory = new File (path);
        if (directory.exists())
            return true;
        return directory.mkdir();
    }

    /**
     * Make a static final class member accessible
     * @param o The class
     * @param name name of the variable
     * @return a "Field" to access the varieble
     * @throws Exception if smth. went wrong
     */
    public static Field makeAccessible (Class<?> o, String name) throws Exception {
        // Get field instance
        Field f = o.getDeclaredField( name);
        f.setAccessible(true);
        // Remove "final" modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
        return f;
    }

    /**
     * Read SerialVersionUID of any class
     * @param o the class
     * @return SV-UID value
     * @throws Exception id smth. went wrong
     */
    public static long fetchSvuid (Class<?> o) throws Exception {
        Field f = makeAccessible (o, "serialVersionUID");
        return f.getLong(null);
    }

    /**
     * Change SerialVersionUID of any class
     * @param o The class
     * @param newUID The new value
     * @throws Exception If smth. went wrong
     */
    public static void changeSvuid (Class<?> o, long newUID) throws Exception {
        Field f = makeAccessible (o, "serialVersionUID");
        f.setLong(null, newUID);
    }
}
