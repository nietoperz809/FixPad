package common;

import settings.MainWindowSettings;
import settings.TextAreaSettings;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.io.File;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileManager implements Runnable
{
    private ArrayList<MyTextArea> list;
    public static String homePath;
    public static final String backupPath;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void loadFromNewPath (String path)
    {
        homePath = path;
        FixPad.fman.loadEditors();
    }

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

    synchronized private PlainDocument load (String fname) throws IOException, ClassNotFoundException
    {
        String content = new String(Files.readAllBytes(Paths.get(fname)),
                StandardCharsets.UTF_8);
        PlainDocument pd = new PlainDocument();
        try {
            pd.insertString(0, content, null);
        } catch (BadLocationException e) {
            return null;
        }
        return pd;
    }

    synchronized private boolean save (PlainDocument doc, String fname) {
        try {
            String content = doc.getText(0, doc.getLength());
            Files.write( Paths.get(fname), content.getBytes());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void loadEditors()
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

    private void saveEditors (boolean wait)
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
        String out = homePath + File.separator + "pane" + n;
        //System.out.println(out);
        return out;
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
