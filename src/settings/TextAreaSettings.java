package settings;

import common.MyTextArea;
import common.ObjectReader;
import common.ObjectWriter;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class TextAreaSettings implements Serializable
{
    private Font font;
    private Color caretcol;
    private Color fgcol;
    private Color bkcol;
    private int carpos;
    private boolean linewrap;
    private String tabTitle;

    private transient final static String fname
            = System.getProperty("user.home") + File.separator + "FPsettings";

    static public void save (ArrayList<MyTextArea> list)
    {
        ObjectWriter ow = new ObjectWriter(fname);
        for (MyTextArea jt : list)
        {
            TextAreaSettings st = new TextAreaSettings();
            st.font = jt.getFont();
            st.caretcol = jt.getCaretColor();
            st.fgcol = jt.getForeground();
            st.bkcol = jt.getBackground();
            st.carpos = jt.getCaretPosition();
            st.linewrap = jt.getLineWrap();
            st.tabTitle = jt.getTpane().getTitleAt(jt.getTabIndex());
            ow.putObject(st);
        }
        ow.close();
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
                jt.getTpane().setTitleAt(jt.getTabIndex(), st.tabTitle);
            }
            or.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

}