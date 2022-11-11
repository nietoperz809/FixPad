package common;


import bitmap.ImageNegative;
import bitmap.NullFilter;
import database.DBHandler;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.util.Stack;

public class MyTextArea extends JTextArea {
    private Stack<Document> undoStack = new Stack<>();
    private JTabbedPane tpane;
    private int tabIndex;
    private PopupMenuHandler menuHandler;

    private Image bkimg;

    @Override
    public synchronized void addMouseListener(MouseListener l) {
        if (l instanceof PopupMenuHandler)
            menuHandler = (PopupMenuHandler) l;
        super.addMouseListener(l);
    }

    int getUndoStackSize() {
        return undoStack.size();
    }

    void updateUndoMenu() {
        menuHandler.undoItem.setText("Undo (" + getUndoStackSize() + ")");
    }

    /**
     * Push current Doc onto Undo Stack
     */
    public void push() {
        Document doc = getDocument();
        if (doc == null || doc.getLength() == 0)
            return;
        try {
            String txt = doc.getText(0, doc.getLength());
            doc = new PlainDocument();
            doc.insertString(0, txt, null);
        } catch (BadLocationException e) {
            System.out.println(e);
            return;
        }
        if (undoStack == null)
            undoStack = new Stack<>();
        undoStack.push(doc);
        //System.out.println("push");
        updateUndoMenu();
    }

    /**
     * Pop current Doc from Undo Stack
     */
    public void pop() {
        try {
            Document doc = undoStack.pop();
            updateUndoMenu();
            super.setDocument(doc);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void setDocument(Document doc) {
        push();
        super.setDocument(doc);
    }

    @Override
    public void append(String str) {
        push();
        super.append(str);
    }

    @Override
    public void setText(String str) {
        push();
        super.setText(str);
    }

    /**
     * Set new Context by reparing tne Doc Object
     *
     * @param s new content as string
     */
    public void setFastText(String s) {
        push();
        PlainDocument doc = new PlainDocument();
        try {
            doc.insertString(0, s, null);
            super.setDocument(doc);
        } catch (BadLocationException e1) {
            System.out.println(e1.getClass().getName());
            System.out.println(e1);
        }
    }


    public String getTabTitle() {
        return getTpane().getTitleAt(getTabIndex());
    }

    /**
     * Get Tab pane
     *
     * @return the pane
     */
    public JTabbedPane getTpane() {
        return tpane;
    }


    /**
     * get index of tab pane where this textfield resides
     *
     * @return the index
     */
    public int getTabIndex() {
        return tabIndex;
    }

    /**
     * Set tab pane and tab index of this text field
     *
     * @param tp  Pane
     * @param idx index
     */
    public void setTabData(JTabbedPane tp, int idx) {
        this.tpane = tp;
        this.tabIndex = idx;
        BufferedImage img = DBHandler.getInst().getBKImage(idx);
        if (img != null)
        {
            bkimg = NullFilter.createImage (img);
            setOpaque(false);
            repaint();
        }
    }

    public void setBackImg (BufferedImage i, boolean negative) {
        if (negative) {
            bkimg = ImageNegative.createImage(i);
        }
        else {
            bkimg = NullFilter.createImage(i);
        }
        DBHandler.getInst().addBKImage(Tools.toBufferedImage(bkimg), tabIndex);
        setOpaque(false);
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        if (bkimg != null) {
            //g.drawImage(bkimg, 0, 0, this);
            Tools.drawStretchedImage(bkimg, this, g);
        }
        super.paint(g);
    }
}

