/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transform;

public class HagelinCrypt implements Transformation
{
    private final int[] cagetable = new int[]
    {
        0, 1, 1, 2, 2, 3, 4, 4, 5, 6, 8, 8, 9, 10, 12, 16,
        16, 17, 18, 20, 24, 32, 32, 33, 34, 36, 40, 48
    };
    private final int[] cage = new int[27];
    private final int[] warr1 = new int[52];
    private final int[] warr2 = new int[50];
    private final int[] warr3 = new int[46];
    private final int[] warr4 = new int[42];
    private final int[] warr5 = new int[38];
    private final int[] warr6 = new int[34];
    private int wheel1;
    private int wheel2;
    private int wheel3;
    private int wheel4;
    private int wheel5;
    private int wheel6;
    private final int[] key = new int[130];

    private int ix;
    private int jx;

    /**
     *
     */
    public HagelinCrypt ()
    {
    }

    private void reset()
    {
        ix=0;
        jx=0;
        wheel1=0;
        wheel2=0;
        wheel3=0;
        wheel4=0;
        wheel5=0;
        wheel6=0;

    int ip, jp;
        int i;
        jp = 0;
        key[jp++] = 4;
        key[jp++] = 28;
        ip = 0;
        while (jp < 128)
        {
            key[jp] = key[jp - 1] ^ key[ip++];
            jp++;
        }
        setup(warr1, 26);
        setup(warr2, 25);
        setup(warr3, 23);
        setup(warr4, 21);
        setup(warr5, 19);
        setup(warr6, 17);
        jp = 0;
        i = 27;
        while (i-- != 0)
        {
            cage[i] = cagetable[key[jp++] % 28];
        }
    }
    
    private int getbit ()
    {
        int b;
        b = (key[jx] >>> ix) & 1;
        if (ix++ > 5)
        {
            jx++;
            ix = 0;
        }
        return (b);
    }

    private void setup(int list[], int n) 
    {
        int lp;
        lp = 0;
        while (--n != 0)
        {
            list[lp] = lp + 2;
            list[lp + 1] = getbit();
            lp += 2;
        }
        list[lp] = 0;
        list[lp + 1] = getbit();
    }

    /**
     * This routine is an exact implementation of Boris Hagelin's
     *   cryptographic machine.  See U. S. Patent #2,089,603.
     */
    public String crypt (String in)
    {
        reset();
        StringBuilder out = new StringBuilder();
        int precious;
        int crypt;
        int temp;
        int loop = 0;
        
        while (loop < in.length())
        {
            precious = in.charAt(loop++);
            temp = 32 * warr1[wheel1 + 1]; //0
            temp += 16 * warr2[wheel2 + 1]; //0
            temp += 8 * warr3[wheel3 + 1]; //8
            temp += 4 * warr4[wheel4 + 1]; // 12
            temp += 2 * warr5[wheel5 + 1]; // 14
            temp += warr6[wheel6 + 1]; // 15

            wheel1 = warr1[wheel1];
            wheel2 = warr2[wheel2];
            wheel3 = warr3[wheel3];
            wheel4 = warr4[wheel4];
            wheel5 = warr5[wheel5];
            wheel6 = warr6[wheel6];
            int random = 0;
            int i = 27;
            while (i-- != 0)
            {
                if ((temp & cage[i]) != 0)
                {
                    random++;
                }
                //random = random + ((temp & cage[i]) != 0);
            }
            random %= 26;

            if (precious == '\n' || precious == ' ')
            {
                crypt = precious;
            }
            else
            {
                crypt = ('a' + 'z' - precious + random) % 256;
                if (crypt >= 'a' && crypt <= 'z' && precious > 'z')
                {
                    crypt += 26;
                }
                if ((crypt > 'z') && (precious >= 'a') & (precious <= 'z'))
                {
                    crypt -= 26;
                }
                if (crypt == '\n' || crypt == ' ')
                {
                    crypt = precious;
                }
            }
            out.append ((char)crypt);
        }
        return out.toString();
    }

    @Override
    public String transform(String in)
    {
        return crypt(in);
    }

    @Override
    public String retransform(String in)
    {
        return crypt(in);
    }
}
