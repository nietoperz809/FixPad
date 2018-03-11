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
        loadEditors();
        MainWindowSettings.load();
        TextAreaSettings.load(list);
        scheduler.scheduleAtFixedRate(this, 0, 10, TimeUnit.SECONDS);
    }

    public void stop()
    {
        scheduler.shutdown();
        saveEditors();
        TextAreaSettings.save(list);
        MainWindowSettings.save();
    }

    private PlainDocument load (String fname)
    {
        try
        {
            ObjectReader re = new ObjectReader(fname);
            return (PlainDocument) re.getObject();
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private void save (PlainDocument doc, String fname)
    {
        ObjectWriter wr = new ObjectWriter(fname);
        wr.putObject(doc);
        wr.close();
    }

    private void loadEditors()
    {
        for (int n=0; n<list.size(); n++)
        {
            MyTextArea jp = list.get(n);
            String fname = createFname(n);
            Document doc = load (fname);
            if (doc != null)
                jp.setDocument(doc);
        }
    }

    private void saveEditors()
    {
        for (int n=0; n<list.size(); n++)
        {
            MyTextArea jp = list.get(n);
            String fname = createFname(n);
            PlainDocument doc = (PlainDocument) jp.getDocument();
            Document old = load (fname);
            if (old == null || !doc.equals(old))
            {
                save(doc, fname);
            }
        }
    }

    private String createFname (int n)
    {
        return homePath + File.separator + "pane" + n;
    }

    public void run()
    {
        saveEditors();
    }
}
