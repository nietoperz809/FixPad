package settings;

import common.FileManager;
import common.FixPad;
import database.DBHandler;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import static common.FixPad.mainFrame;
import static common.FixPad.mainTab;

public class MainWindowSettings implements Serializable
{
    private int x, y, width, height, activeTab;
    private transient final static String fname
            = FileManager.homePath + File.separator + "FPglobal";

    public static void save()
    {
        MainWindowSettings mw = new MainWindowSettings();
        mw.x = mainFrame.getX();
        mw.y = mainFrame.getY();
        mw.width = mainFrame.getWidth();
        mw.height = mainFrame.getHeight();
        mw.activeTab = mainTab.getSelectedIndex();
        try
        {
            DBHandler.getInst().storeObject(fname, mw);
        }
        catch (Exception e)
        {
            FixPad.setStatusBar("MW save / "+e);
        }
    }

    public static void load()
    {
        try
        {
            MainWindowSettings mws = (MainWindowSettings) DBHandler.getInst().fetchObject(fname);
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
    }

}


