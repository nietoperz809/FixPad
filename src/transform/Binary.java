package transform;

public class Binary implements Transformation
{
    private final int mBitCount;

    public Binary (int bc)
    {
        mBitCount = bc;
    }

    /**
     * Makes bit string (00101...) from string
     * @param in human readable input string
     * @return bit pattern representing this
     */
    public String strToBitString (String in)
    {
        StringBuilder sb = new StringBuilder();
        while (!in.isEmpty ())
        {
            char c = in.charAt (0);
            sb.append (strToBitString (c));
            sb.append (" ");
            in = in.substring (1, in.length ());
        }
        return sb.toString ();
    }

    /**
     * Converts bit pattern into human readable string
     * @param in string containing 1's and 0's as well as whitespaces
     * @return clear text string
     * @throws Exception if bit pattern isn't multiple of bit count
     */
    public String strFromBitString (String in) throws Exception
    {
        in = in.replaceAll("\\s+", "");  // remove whitspaces
        if (in.length ()% mBitCount != 0)
            throw new Exception ("Input string must be multiple of "+ mBitCount);
        StringBuilder res = new StringBuilder();
        while (!in.isEmpty ())
        {
            String part= in.substring (0, mBitCount);
            in = in.substring (mBitCount);
            res.append (fromBitString (part));
        }
        return res.toString ();
    }

    private char fromBitString (String in) throws Exception
    {
        if (in.length () != mBitCount)
            throw new Exception ("Input string must be "+ mBitCount +" chars");
        char bit = 1;
        char result = 0;
        for (int s = 0; s< mBitCount; s++)
        {
            if (in.charAt (in.length ()-1)=='1')
                result |= bit;
            bit = (char)(bit<<1);
            in = in.substring (0, in.length ()-1);
        }
        return result;
    }

    private String strToBitString (char c)
    {
        StringBuilder sb = new StringBuilder ();
        char bit = (char)(1<<(mBitCount -1)); //32768;
        for (int s = 0; s< mBitCount; s++)
        {
            sb.append ((c & bit) == bit ? '1' : '0');
            bit >>= 1;
        }
        return sb.toString ();
    }

    @Override
    public String transform (String in)
    {
        return strToBitString (in);
    }

    @Override
    public String retransform (String in)
    {
        try
        {
            return strFromBitString (in);
        }
        catch (Exception e)
        {
            return (e.getMessage ());
        }
    }
}
