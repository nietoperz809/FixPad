package common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ObjectReader
{
    private FileInputStream f_in;
    private ObjectInputStream obj_in;

    /**
     * Constructor, set file name
     * @param fname file name
     */
    public ObjectReader (String fname) throws IOException
    {
        f_in = new FileInputStream(fname);
        obj_in = new ObjectInputStream(f_in);
    }

    /**
     * Get one Object
     * @return the object or null
     */
    public Object getObject() throws IOException, ClassNotFoundException
    {
        return obj_in.readObject();
    }

    /**
     * Close Object reader
     * must finally be called
     */
    public void close()
    {
        try
        {
            obj_in.close();
            f_in.close();
        }
        catch (IOException e)
        {
            System.out.println("in Objectreader close");
            System.out.println(e);
        }
    }
}
