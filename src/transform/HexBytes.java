package transform;

import common.Tools;

public class HexBytes implements Transformation
{
    final private static char[] hexArray = "0123456789abcdef".toCharArray();

    private char[] byteToHex (byte b)
    {
        char[] hexChars = new char[2];
        int v = b & 0xff;
        hexChars[0] = hexArray[v >>>4];
        hexChars[1] = hexArray[v & 0x0f];
        return hexChars;
    }

    @Override
    public String transform (String in)
    {
        byte[] barr = Tools.toRawByteArray(in);
        StringBuilder sb = new StringBuilder(barr.length*5);
        for (byte b : barr)
        {
            sb.append(byteToHex(b)).append(' ');
        }
        return sb.toString();
    }

    @Override
    public String retransform (String in)
    {
        return null;
    }
}
