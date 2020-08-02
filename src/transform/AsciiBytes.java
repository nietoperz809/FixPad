package transform;

import common.Tools;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AsciiBytes extends HexBytes
{
    @Override
    public String transform (String in)
    {
        byte[] barr = in.getBytes (StandardCharsets.US_ASCII);
        StringBuilder sb = new StringBuilder(barr.length*3);
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
        char[] hexnum = new char[2];
        int idx = 0;
        while(sb.length() != 0)
        {
            char c = sb.charAt(0);
            sb.deleteCharAt(0);
            if (Character.isWhitespace(c))
                continue;
            if (idx == 0)
                arb.add((byte)0);  // fake 0 to make 16 bit char
            hexnum[idx++] = c;
            if (idx == 2)
            {
                idx = 0;
                arb.add(hexToByte(hexnum));
            }
        }
        byte[] bytes = new byte[arb.size()];
        for (int s=0; s<bytes.length; s++)
            bytes[s] = arb.get(s);
        char[] ret = Tools.fromRawByteArray(bytes);
        return new String (ret);
    }
}
