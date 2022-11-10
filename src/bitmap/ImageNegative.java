package bitmap;

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

// classe auxiliaire pour le filtre
public class ImageNegative extends RGBImageFilter {

    public static Image createImage(Image i) {
        ImageNegative filter = new ImageNegative();
        ImageProducer prod = new FilteredImageSource(i.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(prod);
    }

    public ImageNegative() {
        canFilterIndexColorModel = true;
    }// ImageNegative

    @Override
    public int filterRGB(int iX, int iY, int iRgb ) {
        // séparation des couleurs
        int iR = (iRgb >> 16) & 0xFF;   // rouge
        int iG = (iRgb >> 8) & 0xFF;    // vert
        int iB = iRgb & 0xFF;           // bleu

        // enregistrement des valeurs HSB
        float fHsb[] = new float[3];

        // conversion des couleurs RVB dans le modèle HSB
        Color.RGBtoHSB( iR, iG, iB, fHsb );

        // inversion de la clarté
        fHsb[2] = 1 - fHsb[2];

        // reconversion en couleurs RVB (valeur alpha maximale)
        return ( 0xFF000000 | Color.HSBtoRGB( fHsb[0], fHsb[1], fHsb[2] ) );
    }// filterRGB

}// ImageNegative