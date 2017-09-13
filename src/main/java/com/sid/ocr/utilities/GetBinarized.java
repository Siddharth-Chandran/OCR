package com.sid.ocr.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.awt.image.BufferedImage;

@Repository
public class GetBinarized {

    @Autowired
    ColorToRGB colorToRGB;

    public BufferedImage binarize(BufferedImage image){
        int red;
        int newPixel;

        int threshold = otsuTreshold(image);

        BufferedImage binarized = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());


        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                // Get pixels
                red = new Color(image.getRGB(i, j)).getRed();
                int alpha = new Color(image.getRGB(i, j)).getAlpha();
                if (red > threshold) {
                    newPixel = 255;
                } else {
                    newPixel = 0;
                }
                newPixel = colorToRGB.colorToRGB(alpha, newPixel, newPixel, newPixel);
                binarized.setRGB(i, j, newPixel);

            }
        }

        return binarized;
    }

    // Get binary treshold using Otsu's method
    private static int otsuTreshold(BufferedImage original) {

        int[] histogram = imageHistogram(original);
        int total = original.getHeight() * original.getWidth();

        float sum = 0;
        for (int i = 0; i < 256; i++) sum += i * histogram[i];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) continue;
            wF = total - wB;

            if (wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;

    }

    // Return histogram of grayscale image
    public static int[] imageHistogram(BufferedImage input) {

        int[] histogram = new int[256];

        for (int i = 0; i < histogram.length; i++) histogram[i] = 0;

        for (int i = 0; i < input.getWidth(); i++) {
            for (int j = 0; j < input.getHeight(); j++) {
                int red = new Color(input.getRGB(i, j)).getRed();
                histogram[red]++;
            }
        }

        return histogram;

    }
}

