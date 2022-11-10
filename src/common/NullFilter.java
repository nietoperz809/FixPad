package common;

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public class NullFilter extends RGBImageFilter {

    public static Image createIdentityImage(Image i) {
        NullFilter filter = new NullFilter();
        ImageProducer prod = new FilteredImageSource(i.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(prod);
    }

    public NullFilter() {
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        return rgb;
    }
}
