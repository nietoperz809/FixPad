package common;

import database.DBHandler;
import settings.MainWindowSettings;
import settings.TextAreaSettings;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FileManager /*implements Runnable */ {

    private final Timer timer;
    private int t_idx = 0;
    private ArrayList<MyTextArea> list;

    public FileManager(ArrayList<MyTextArea> list) {
        this.list = list;
        timer = new Timer(5000, e1 -> {
            SwingUtilities.invokeLater(() -> {
                MyTextArea jp = list.get(t_idx);
                String fname = createFname(t_idx);
                String suffix = t_idx + ": " + jp.getTabTitle();
                PlainDocument doc = (PlainDocument) jp.getDocument();
                if (save(doc, fname)) {
                    FixPad.setStatusBar("Saved Tab " + suffix);
                }
            });
            t_idx++;
            if (t_idx == list.size())
                t_idx = 0;
        });
        timer.setRepeats(true);
        timer.setInitialDelay(5000);
        timer.start();
    }

    public void loadAll() {
        System.out.println("fileman load");
        try {
            loadEditors();
        } catch (Exception e) {
            System.out.println(e);
        }
        MainWindowSettings.load();
        TextAreaSettings.load(list);
        //scheduler.scheduleAtFixedRate(this, 10, 60, TimeUnit.SECONDS);
    }

    public void saveAll() {
        System.out.println("fileman save");
        try {
            saveEditors();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        TextAreaSettings.save(list);
        MainWindowSettings.save();
    }

    synchronized private PlainDocument load(String fname) throws Exception {
        String content = (String)(DBHandler.getInst().fetchObject(fname));
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

    public void saveEditors() {
        for (int n = 0; n < list.size(); n++) {
            MyTextArea jp = list.get(n);
            String fname = createFname(n);
            String suffix = n + ": " + jp.getTabTitle();
            PlainDocument doc = (PlainDocument) jp.getDocument();
            if (save(doc, fname)) {
                FixPad.setStatusBar("Saved Tab " + suffix);
            }
        }
    }

    private String createFname(int n) {
        return "pane" + n;
    }
}
