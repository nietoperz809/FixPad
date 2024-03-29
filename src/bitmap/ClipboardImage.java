package bitmap;

import java.awt.*;
import java.awt.datatransfer.*;

public class ClipboardImage implements ClipboardOwner
{
    public ClipboardImage (Image i)
    {
        try
        {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            TransferableImage trans = new TransferableImage(i);
            Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
            c.setContents(trans, this);
        }
        catch (Exception x)
        {
            x.printStackTrace();
            //System.exit(1);
        }
    }

//    public static void main(String[] arg)
//    {
//        ClipboardImage ci = new ClipboardImage();
//    }

    @Override
    public void lostOwnership(Clipboard clip, Transferable trans)
    {
        System.out.println("Lost Clipboard Ownership");
    }

    private static class TransferableImage implements Transferable
    {

        private final Image i;

        TransferableImage (Image i)
        {
            this.i = i;
        }

        @Override
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException
        {
            if (flavor.equals(DataFlavor.imageFlavor) && i != null)
            {
                return i;
            }
            else
            {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors()
        {
            DataFlavor[] flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.imageFlavor;
            return flavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            DataFlavor[] flavors = getTransferDataFlavors();
            for (DataFlavor flavor1 : flavors)
            {
                if (flavor.equals(flavor1))
                {
                    return true;
                }
            }
            return false;
        }
    }
}
