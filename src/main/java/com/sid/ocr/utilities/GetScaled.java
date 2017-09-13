package com.sid.ocr.utilities;


import org.springframework.stereotype.Repository;

import java.awt.*;
import java.awt.image.BufferedImage;

@Repository
public class GetScaled {

    public BufferedImage getScaledImage(BufferedImage image) {

        Image rescaled = image.getScaledInstance(image.getWidth() * 2, image.getHeight() * 2, Image.SCALE_AREA_AVERAGING);
        BufferedImage scaledImage = toBufferedImage(rescaled, BufferedImage.TYPE_INT_RGB);

        return scaledImage;
    }

    private BufferedImage toBufferedImage(Image image, int type) {

        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage result = new BufferedImage(w, h, type);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }
}
