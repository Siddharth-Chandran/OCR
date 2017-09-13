package com.sid.ocr.utilities;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.awt.image.BufferedImage;

@Repository
public class GetGrayScale {

    @Autowired
    ColorToRGB colorToRGB;

    public BufferedImage getGrayScale(BufferedImage image) {
        int alpha, red, green, blue;
        int newPixel;
        int average;

        BufferedImage updatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                alpha = new Color(image.getRGB(i, j)).getAlpha();
                red = new Color(image.getRGB(i, j)).getRed();
                green = new Color(image.getRGB(i, j)).getGreen();
                blue = new Color(image.getRGB(i, j)).getBlue();

                average = (int) (red + green + blue) / 3;

                newPixel = colorToRGB.colorToRGB(alpha, average, average, average);

                updatedImage.setRGB(i, j, newPixel);

            }
        }

        return updatedImage;
    }
}
