package common;

import database.DBHandler;
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

public class FileManager implements Runnable {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ArrayList<MyTextArea> list;

    public void put(ArrayList<MyTextArea> otherList) {
        list = otherList;
    }

    public void start() {
        try {
            loadEditors();
        } catch (Exception e) {
            System.out.println("in fileman start:");
            System.out.println(e);
        }
        MainWindowSettings.load();
        TextAreaSettings.load(list);
        scheduler.scheduleAtFixedRate(this, 10, 60, TimeUnit.SECONDS);
    }

    public void stop() {
        scheduler.shutdown();
        try {
            saveEditors(false);
        } catch (Exception e) {
            System.out.println("In fileman stop:");
            System.out.println(e.getMessage());
        }
        TextAreaSettings.save(list);
        MainWindowSettings.save();
    }

    synchronized private PlainDocument load(String fname) throws Exception {
        String content = (String)(DBHandler.getInst().fetchObject(fname));
        //new String(Files.readAllBytes(Paths.get(fname)),
                // StandardCharsets.UTF_8);
        PlainDocument pd = new PlainDocument();
        try {
            pd.insertString(0, content, null);
        } catch (BadLocationException e) {
            return null;
        }
        return pd;
    }

    synchronized private boolean save(PlainDocument doc, String fname) {
        try {
            String content = doc.getText(0, doc.getLength());
            DBHandler.getInst().storeObject(fname, content);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void loadEditors() {
        for (int n = 0; n < list.size(); n++) {
            MyTextArea jp = list.get(n);
            String fname = createFname(n);
            try {
                Document doc = load(fname);
                jp.setDocument(doc);
            } catch (Exception e) {
                System.out.println("failed to load doc: " + fname);
                System.out.println(e.getMessage());
            }
        }
    }

    private void saveEditors(boolean wait) {
        for (int n = 0; n < list.size(); n++) {
            MyTextArea jp = list.get(n);
            String fname = createFname(n);
            String suffix = n + ": " + jp.getTabTitle();
            PlainDocument doc = (PlainDocument) jp.getDocument();
            if (save(doc, fname)) {
                FixPad.setStatusBar("Saved Tab " + suffix);
            }
            if (wait) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    private String createFname(int n) {
        return "pane" + n;
    }

    public void run() {
        FixPad.setStatusBar("Autosave");
        try {
            saveEditors(true);
        } catch (Exception e) {
            FixPad.setStatusBar("Timerproc / " + e);
        }
        TextAreaSettings.save(list);
        MainWindowSettings.save();
    }
}
