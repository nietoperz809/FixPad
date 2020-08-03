package transform;

public class Binary implements Transformation
{
    private final int mBase;

    public Binary (int base)
    {
        mBase = base;
    }

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

    public String strFromBitString (String in) throws Exception
    {
        in = in.replaceAll("\\s+", "");
        if (in.length ()%mBase != 0)
            throw new Exception ("Input string must be multiple of "+mBase);
        StringBuilder res = new StringBuilder();
        while (!in.isEmpty ())
        {
            String part= in.substring (0, mBase);
            in = in.substring (mBase);
            res.append (fromBitString (part));
        }
        return res.toString ();
    }

    private char fromBitString (String in) throws Exception
    {
        if (in.length () != mBase)
            throw new Exception ("Input string must be "+mBase+" chars");
        char bit = 1;
        char result = 0;
        for (int s=0; s<mBase; s++)
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
        char bit = (char)(1<<(mBase-1)); //32768;
        for (int s=0; s<mBase; s++)
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
