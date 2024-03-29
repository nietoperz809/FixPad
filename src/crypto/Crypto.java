package crypto;

import common.Tools;
import transform.HagelinCrypt;
import transform.Pitti3Crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto
{
    enum CryptMethod
    {
        AESEncoded,
        Peter1Encoded,
        HagelinEncoded,
        PittyEncoded;

        String text()
        {
            return "*"+name()+"*\n";
        }
    }

    /**
     * Generates a (hopefully) secure hash value of 32 bytes from a given password
     *
     * @param pwd The password
     * @return The hash value
     * @throws NoSuchAlgorithmException Forget it
     */
    public static byte[] passwordHash (byte[] pwd) throws NoSuchAlgorithmException
    {
        byte[] res = new byte[32];
        byte[] md5bytes = new byte[16];
        byte[] sha1bytes = new byte[20];
        byte[] salt = {-48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88,
                -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8,

        };
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");

        System.arraycopy(sha1.digest(pwd), 0, sha1bytes, 0, 20);
        sha1bytes = rotateArrayRight(sha1bytes, sha1bytes[7] & 0xff);
        System.arraycopy(md5.digest(pwd), 0, md5bytes, 0, 16);
        md5bytes = rotateArrayRight(md5bytes, md5bytes[11] & 0xff);

        res[0] = (byte) (md5bytes[0] ^ sha1bytes[18]);
        res[1] = (byte) (md5bytes[1] ^ sha1bytes[19]);
        res[2] = (byte) (md5bytes[2] ^ sha1bytes[16]);
        res[3] = (byte) (md5bytes[3] ^ sha1bytes[17]);
        res[4] = (byte) (md5bytes[4] ^ sha1bytes[14]);
        res[5] = (byte) (md5bytes[5] ^ sha1bytes[15]);
        res[6] = (byte) (md5bytes[6] ^ sha1bytes[12]);
        res[7] = (byte) (md5bytes[7] ^ sha1bytes[13]);
        res[8] = (byte) (md5bytes[8] ^ sha1bytes[10]);
        res[9] = (byte) (md5bytes[9] ^ sha1bytes[11]);
        res[10] = (byte) (md5bytes[10] ^ sha1bytes[8]);
        res[11] = (byte) (md5bytes[11] ^ sha1bytes[9]);
        res[12] = (byte) (md5bytes[12] ^ sha1bytes[6]);
        res[13] = (byte) (md5bytes[13] ^ sha1bytes[7]);
        res[14] = (byte) (md5bytes[14] ^ sha1bytes[4]);
        res[15] = (byte) (md5bytes[15] ^ sha1bytes[5]);
        res[16] = (byte) (md5bytes[14] ^ sha1bytes[2]);
        res[17] = (byte) (md5bytes[13] ^ sha1bytes[3]);
        res[18] = (byte) (md5bytes[12] ^ sha1bytes[0]);
        res[19] = (byte) (md5bytes[11] ^ sha1bytes[1]);
        res[20] = (byte) (md5bytes[10] ^ sha1bytes[17]);
        res[21] = (byte) (md5bytes[9] ^ sha1bytes[11]);
        res[22] = (byte) (md5bytes[8] ^ sha1bytes[13]);
        res[23] = (byte) (md5bytes[7] ^ sha1bytes[5]);
        res[24] = (byte) (md5bytes[6] ^ sha1bytes[9]);
        res[25] = (byte) (md5bytes[5] ^ sha1bytes[3]);
        res[26] = (byte) (md5bytes[4] ^ sha1bytes[7]);
        res[27] = (byte) (md5bytes[3] ^ sha1bytes[0]);
        res[28] = (byte) (md5bytes[2] ^ sha1bytes[4]);
        res[29] = (byte) (md5bytes[1] ^ sha1bytes[8]);
        res[30] = (byte) (md5bytes[11] ^ sha1bytes[12]);
        res[31] = (byte) (md5bytes[0] ^ sha1bytes[15]);

        res = rotateArrayRight(res, res[2] & 0xff);
        res = xor(res, salt);
        return res;
    }

    /**
     * Rotates byte array to the right
     *
     * @param arr The source array
     * @param num number of rotations
     * @return The rotated array
     */
    public static byte[] rotateArrayRight (byte[] arr, int num)
    {
        int rot = num % arr.length;     // --> to the right
        int diff = arr.length - rot;    // difference
        byte[] res = new byte[arr.length];
        System.arraycopy(arr, 0, res, rot, diff);
        System.arraycopy(arr, diff, res, 0, rot);
        return res;
    }

    /**
     * Xors two byte arrays.
     *
     * @param in  IN block.
     * @param src XOR operands.
     * @return The new array
     */
    public static byte[] xor (byte[] in, byte[] src)
    {
        byte[] x = new byte[in.length];
        for (int s = 0; s < in.length; s++)
        {
            x[s] = (byte) (in[s] ^ src[s % src.length]);
        }
        return x;
    }

    public static String cryptPitty (String in, String passwd)
    {
        String header = CryptMethod.PittyEncoded.text();
        boolean mode = true; // encrypt
        if (in.startsWith(header))
        {
            in = in.substring(header.length());
            mode = false;  // decrypt
        }
        String out;
        if (mode)
        {
            out = new Pitti3Crypt(passwd).transform(in);
            out = header+out;
        }
        else
            out = new Pitti3Crypt(passwd).retransform(in);
        return out;
    }

    public static String cryptHagelin (String in)
    {
        String header = CryptMethod.HagelinEncoded.text();
        boolean mode = true; // encrypt
        if (in.startsWith(header))
        {
            in = in.substring(header.length());
            mode = false;  // decrypt
        }
        String out = new HagelinCrypt().crypt(in);
        if (mode)
            out = header+out;
        return out;
    }

    public static String cryptFilePeter1 (byte[] key, String in) throws Exception
    {
        String header = CryptMethod.Peter1Encoded.text();
        boolean mode = true; // encrypt
        if (in.startsWith(header))
        {
            in = in.substring(header.length());
            mode = false;  // decrypt
        }
        byte[] buff;
        byte[] all = Tools.toRawByteArray(in);
        int size = all.length;
        int pos = 0;
        int len;
        StringBuilder sb = new StringBuilder();

        for (;;)
        {
            len = Math.min(size - pos, 32);
            if (len == 0)
                break;
            buff = new byte[len];
            System.arraycopy(all, pos, buff, 0, len);
            pos += len;

            key = passwordHash (key);
            buff = xor(buff, key);
            char[] chars = Tools.fromRawByteArray(buff);
            sb.append (chars);
        }
        if (mode)
            sb.insert (0, header);
        return sb.toString();
    }

    /**
     * Enc/De-crypts a String with AES256
     * @param key AES-compatible key, always 32 bytes
     * @param in input string
     * @return transformed string
     * @throws Exception if smth. gone wrong
     */
    public static String cryptAes256 (byte[] key, String in) throws Exception
    {
        String header = CryptMethod.AESEncoded.text();
        boolean mode = true; // encrypt
        if (in.startsWith(header))
        {
            in = in.substring(header.length());
            mode = false;  // decrypt
        }
        StringBuilder outbuffer = new StringBuilder();
        StringBuilder inBuilder = new StringBuilder(in);
        char paddingChars;
        if (mode)
        {
            paddingChars = 0;
            while (inBuilder.length() % 8 != 0)
            {
                paddingChars++;
                inBuilder.append('.');
            }
            outbuffer.append(paddingChars);
        }
        else
        {
            paddingChars = inBuilder.charAt(0);
            inBuilder.deleteCharAt(0);
        }

        byte[] buff = new byte[16];
        byte[] all = Tools.toRawByteArray(inBuilder.toString());
        int pos = 0;

        AESEngine aes = new AESEngine();
        aes.init (mode, key);
        do {
            aes.processBlock(all, pos, buff, 0);
            pos += 16;
            char[] chars = Tools.fromRawByteArray(buff);
            outbuffer.append(chars);
        } while (pos != all.length);
        if (!mode) // decryption: remove padding
            outbuffer.setLength(outbuffer.length()-paddingChars);
        if (mode)
            outbuffer.insert (0, header);
        return outbuffer.toString();
    }
}
