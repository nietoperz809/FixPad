import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileManager implements Runnable
{
    private final ArrayList<JTextArea> list = new ArrayList<>();
    private final String homePath = System.getProperty("user.home");
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void put(JTextArea jp)
    {
        list.add(jp);
    }

    public void start()
    {
        loadEditors();
        Settings.load(list);
        scheduler.scheduleAtFixedRate(this, 0, 10, TimeUnit.SECONDS);
    }

    public void stop()
    {
        scheduler.shutdown();
        saveEditors();
        Settings.save(list);
    }

    private String load (String fname)
    {
        try
        {
            return new String(Files.readAllBytes(Paths.get(fname)));
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private boolean save (String txt, String fname)
    {
        try
        {
            byte[] bytes = txt.getBytes();
            OpenOption[] op = {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            Files.write (Paths.get(fname), bytes, op);
            return true;
        }
        catch (IOException e)
        {
            System.out.println("save fail");
            return false;
        }
    }

    private void loadEditors()
    {
        for (int n=0; n<list.size(); n++)
        {
            JTextArea jp = list.get(n);
            String fname = createFname(n);
            String txt = load (fname);
            jp.setText(txt);
        }
    }

    private void saveEditors()
    {
        for (int n=0; n<list.size(); n++)
        {
            JTextArea jp = list.get(n);
            String fname = createFname(n);
            String txt = jp.getText();
            String old = load (fname);
            if (old == null || !txt.equals(old))
            {
                if (!save(txt, fname))
                {
                    System.out.println("save failed: "+fname);
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
        saveEditors();
    }
}
