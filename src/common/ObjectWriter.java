package common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ObjectWriter
{
    private FileOutputStream f_out;
    private ObjectOutputStream obj_out;

    /**
     * Open Obj writer
     * @param fname file where objects dwell
     */
    public ObjectWriter (String fname) throws IOException
    {
        f_out = new FileOutputStream(fname);
        obj_out = new ObjectOutputStream(f_out);
    }

    /**
     * Write one object
     * @param o The object
     */
    public void putObject (Object o) throws IOException
    {
        obj_out.writeObject(o);
    }

    /**
     * Close writer
     * Must finally be called
     */
    public void close()
    {
        try
        {
            obj_out.close();
            f_out.close();
        }
        catch (IOException e)
        {
            System.out.println("in objectwriter close:");
            System.out.println(e);
        }
    }
}
