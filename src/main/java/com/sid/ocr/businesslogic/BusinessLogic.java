package com.sid.ocr.businesslogic;


import com.sid.ocr.Application;
import com.sid.ocr.utilities.GetBinarized;
import com.sid.ocr.utilities.GetGrayScale;
import com.sid.ocr.utilities.GetScaled;
import com.sid.ocr.utilities.ImageToText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BusinessLogic {


    @Autowired
    GetScaled getScaled;
    @Autowired
    GetGrayScale getGrayScale;
    @Autowired
    GetBinarized getBinarized;
    @Autowired
    ImageToText imageToText;

    @Value("${image.source}")
    private String sourcePath;
    @Value("${image.processed}")
    private String processedPath;

    public void processImages() {

        File sourceDirectory = new File(sourcePath);
        File[] sourceFiles = sourceDirectory.listFiles(
                ((dir, name) -> name.toLowerCase().endsWith(".jpg"))
        );

        BufferedImage image = null;
        String content;
        for (File sourceFile : sourceFiles) {
            Application.fileCount++;

            try {
                image = ImageIO.read(sourceFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image = getScaled.getScaledImage(image);
            image = getGrayScale.getGrayScale(image);
            image = getBinarized.binarize(image);
            content = imageToText.convertImageToString(image).get(0).getText();

            for (String lexicon : Application.lexicons) {
                if (content.toLowerCase().contains(lexicon.toLowerCase())) {

                    if (Application.alertDetails.containsKey(lexicon)) {
                        List<String> tempList = Application.alertDetails.get(lexicon);
                        String filePath = Paths.get(processedPath, Application.fileCount + ".jpg").toString();
                        tempList.add(filePath);
                        Application.alertDetails.put(lexicon, tempList);
                    } else {
                        List<String> tempList = new ArrayList<>();
                        tempList.add(Paths.get(processedPath, Application.fileCount + ".jpg").toString());
                        Application.alertDetails.put(lexicon, tempList);
                    }
                }
            }

            try {
                ImageIO.write(ImageIO.read(sourceFile), "jpg", new File(Paths.get(processedPath, Application.fileCount + ".jpg").toString()));
//                sourceFile.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
