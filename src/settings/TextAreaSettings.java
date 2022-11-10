package settings;

import common.*;
import database.DBHandler;

import java.awt.*;
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

    private final static String fname = "FPsettings";


    static public void save (ArrayList<MyTextArea> list)
    {
        String tabTitle = "??";
        try
        {
            for (int n=0; n<list.size(); n++) {
                MyTextArea jt = list.get(n);
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
                DBHandler.getInst().storeObject(fname+n, st);
            }
        }
        catch (Exception e)
        {
            FixPad.setStatusBar("TA settings / " + tabTitle + " / "+e);
        }
    }

    static public void load (ArrayList<MyTextArea> list)
    {
        try
        {
            for (int n=0; n<list.size(); n++) {
                MyTextArea jt = list.get(n);
                TextAreaSettings st = (TextAreaSettings) DBHandler.getInst().fetchObject(fname+n);
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
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

}
