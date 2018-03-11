package settings;

import common.ObjectReader;
import common.ObjectWriter;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static common.FixPad.mainFrame;
import static common.FixPad.mainTab;

public class MainWindowSettings implements Serializable
{
    private int x, y, width, height, activeTab;
    private transient final static String fname
            = System.getProperty("user.home") + File.separator + "FPglobal";

    public static void save()
    {
        MainWindowSettings mw = new MainWindowSettings();
        mw.x = mainFrame.getX();
        mw.y = mainFrame.getY();
        mw.width = mainFrame.getWidth();
        mw.height = mainFrame.getHeight();
        mw.activeTab = mainTab.getSelectedIndex();
        ObjectWriter ow = null;
        try
        {
            ow = new ObjectWriter(fname);
            ow.putObject(mw);
        }
        catch (IOException e)
        {
            System.out.println("in mainwindow settings save:");
            System.out.println(e);
        }
        ow.close();
    }

    public static void load()
    {
        try
        {
            ObjectReader or = new ObjectReader(fname);
            MainWindowSettings mws = (MainWindowSettings) or.getObject();
            mainFrame.setLocation(mws.x, mws.y);
            mainFrame.setSize(mws.width, mws.height);
            mainTab.setSelectedIndex(mws.activeTab);
        }
        catch (Exception e)
        {
            mainFrame.setSize(1000, 600);
            mainFrame.setLocationRelativeTo(null);
            System.out.println("Cannot load MWS");
        }
        mainFrame.setVisible(true);
    }

}


