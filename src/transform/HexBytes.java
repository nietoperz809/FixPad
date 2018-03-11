package transform;

import common.Tools;

import java.util.ArrayList;

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

    private byte hexToByte (char[] hex)
    {
        int upper = Character.digit(hex[0],16)<<4;
        int lower = Character.digit(hex[1],16);
        return (byte)(upper + lower);
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
        StringBuilder sb = new StringBuilder(in);
        ArrayList<Byte> arb = new ArrayList<>();
        char[] arc = new char[2];
        int idx = 0;
        while(sb.length() != 0)
        {
            char c = sb.charAt(0);
            sb.deleteCharAt(0);
            if (Character.isWhitespace(c))
                continue;
            arc[idx++] = c;
            if (idx == 2)
            {
                idx = 0;
                arb.add(hexToByte(arc));
            }
        }
        byte[] bytes = new byte[arb.size()];
        for (int s=0; s<bytes.length; s++)
            bytes[s] = arb.get(s);
        char[] ret = Tools.fromRawByteArray(bytes);
        return new String (ret);
    }
}
