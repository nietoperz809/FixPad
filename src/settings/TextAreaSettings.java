package settings;

import common.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class TextAreaSettings implements Serializable
{
    private Font font;
    private Color caretcol;
    private Color fgcol;
    private Color bkcol;
    private Color tabColor;
    private int carpos;
    private boolean linewrap;
    private boolean editable;
    private String tabTitle;

    private transient final static String fname
            = FileManager.homePath + File.separator + "FPsettings";


    static public void save (ArrayList<MyTextArea> list)
    {
        String tabTitle = "??";
        try
        {
            ObjectWriter ow = new ObjectWriter(fname);
            for (MyTextArea jt : list)
            {
                tabTitle = jt.getTabTitle();
                TextAreaSettings st = new TextAreaSettings();
                st.font = jt.getFont();
                st.caretcol = jt.getCaretColor();
                st.tabColor = jt.getTpane().getBackgroundAt(jt.getTabIndex());
                st.fgcol = jt.getForeground();
                st.bkcol = jt.getBackground();
                st.carpos = jt.getCaretPosition();
                st.linewrap = jt.getLineWrap();
                st.editable = jt.isEditable();
                st.tabTitle = tabTitle;
                ow.putObject(st);
            }
            ow.close();
        }
        catch (IOException e)
        {
            FixPad.setStatusBar("TA settings / " + tabTitle + " / "+e);
        }
    }

    static public void load (ArrayList<MyTextArea> list)
    {
        try
        {
            ObjectReader or = new ObjectReader(fname);
            for (MyTextArea jt : list)
            {
                TextAreaSettings st = (TextAreaSettings) or.getObject();
                jt.setFont(st.font);
                jt.setCaretColor(st.caretcol);
                jt.setForeground(st.fgcol);
                jt.setBackground(st.bkcol);
                jt.setCaretPosition(st.carpos);
                jt.setLineWrap(st.linewrap);
                jt.setWrapStyleWord(true);
                jt.setEditable(st.editable);
                jt.getTpane().setTitleAt(jt.getTabIndex(), st.tabTitle);
                jt.getTpane().setBackgroundAt(jt.getTabIndex(), st.tabColor);

            }
            or.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

}
