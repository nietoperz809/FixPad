package common;

import settings.MainWindowSettings;
import settings.TextAreaSettings;

import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileManager implements Runnable
{
    private ArrayList<MyTextArea> list; // = new ArrayList<>();
    private final String homePath = System.getProperty("user.home");
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


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
        scheduler.scheduleAtFixedRate(this, 0, 10, TimeUnit.SECONDS);
    }

    public void stop()
    {
        scheduler.shutdown();
        try
        {
            saveEditors();
        }
        catch (Exception e)
        {
            System.out.println("In fileman stop:");
            System.out.println(e);
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

    private void save (PlainDocument doc, String fname)
    {
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
                System.out.println("failed to load doc: "+n);
            }
        }
    }

    private void saveEditors()
    {
        for (int n=0; n<list.size(); n++)
        {
            MyTextArea jp = list.get(n);
            String fname = createFname(n);
            PlainDocument doc = (PlainDocument) jp.getDocument();
            save(doc, fname);
        }
    }

    private String createFname (int n)
    {
        return homePath + File.separator + "pane" + n;
    }

    public void run()
    {
        try
        {
            saveEditors();
        }
        catch (Exception e)
        {
            System.out.println("in timer proc:");
            System.out.println(e);
        }
        TextAreaSettings.save(list);
        MainWindowSettings.save();

        FixPad.setStatusBar("Saved");
    }
}
