package common;

import settings.MainWindowSettings;
import settings.TextAreaSettings;

import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileManager implements Runnable
{
    private ArrayList<MyTextArea> list;
    public static final String homePath;
    public static final String backupPath;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        homePath = System.getProperty("user.home") + File.separator
                + "fixpad" + System.getProperty("java.version");
        backupPath = homePath + File.separator +"backup";

        Tools.mkdir(homePath);
        Tools.mkdir(backupPath);
    }

    public void put (ArrayList<MyTextArea> otherList)
    {
        list = otherList;
    }

    public void start()
    {
        try
        {
            loadEditors();
        }
        catch (Exception e)
        {
            System.out.println("in fileman start:");
            System.out.println(e);
        }
        MainWindowSettings.load();
        TextAreaSettings.load(list);
        scheduler.scheduleAtFixedRate(this, 10, 30, TimeUnit.SECONDS);
    }

    public void stop()
    {
        scheduler.shutdown();
        try
        {
            saveEditors(false);
        }
        catch (Exception e)
        {
            System.out.println("In fileman stop:");
            System.out.println(e.getMessage());
        }
        TextAreaSettings.save(list);
        MainWindowSettings.save();
    }

    private PlainDocument load (String fname) throws IOException, ClassNotFoundException
    {
        ObjectReader re = new ObjectReader(fname);
        PlainDocument pd = (PlainDocument) re.getObject();
        re.close();
        return pd;
    }

    private boolean save (PlainDocument doc, String fname)
    {
//        if (doc.getLength() == 0)
//            return false;
        try
        {
            PlainDocument pd2 = load (fname);
            String t1 = pd2.getText (0, pd2.getLength());
            String t2 = doc.getText (0, doc.getLength());
            if (t1.equals(t2))
            {
                return false; // nothing to save
            }
        }
        catch (Exception e)
        {
            // fall through
        }
        try
        {
            ObjectWriter wr = new ObjectWriter(fname);
            wr.putObject(doc);
            wr.close();
        }
        catch (IOException e)
        {
            System.out.println("in fileman save");
            System.out.println(e);
        }
        return true;
    }

    private synchronized void loadEditors()
    {
        for (int n=0; n<list.size(); n++)
        {
            MyTextArea jp = list.get(n);
            String fname = createFname(n);
            try
            {
                Document doc = load (fname);
                jp.setDocument(doc);
            }
            catch (Exception e)
            {
                System.out.println("failed to load doc: "+fname);
                System.out.println(e.getMessage());
            }
            //System.out.println("load: "+n);
        }                            
    }

    private synchronized void saveEditors (boolean wait)
    {
        for (int n=0; n<list.size(); n++)
        {
            MyTextArea jp = list.get(n);
            String fname = createFname(n);
            String suffix = n+": "+jp.getTabTitle();
            PlainDocument doc = (PlainDocument) jp.getDocument();
            if (!save(doc, fname))
            {
                //FixPad.setStatusBar("No need to save " + suffix);
            }
            else
            {
                FixPad.setStatusBar("Saved Tab " +suffix);
            }
            if (wait)
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    return;
                    //FixPad.setStatusBar("Failed to save Tab "+n+" / "+e);
                }
            }
        }
    }

    private String createFname (int n)
    {
        return homePath + File.separator + "pane" + n;
    }

    public void run()
    {
        FixPad.setStatusBar("Autosave");
        try
        {
            saveEditors(true);
        }
        catch (Exception e)
        {
            FixPad.setStatusBar("Timerproc / "+e);
        }
        TextAreaSettings.save(list);
        MainWindowSettings.save();
    }
}
