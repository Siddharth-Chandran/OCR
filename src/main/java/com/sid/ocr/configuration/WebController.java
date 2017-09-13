package com.sid.ocr.configuration;

import com.sid.ocr.Application;
import com.sid.ocr.businesslogic.BusinessLogic;
import com.sid.ocr.utilities.GetBinarized;
import com.sid.ocr.utilities.GetGrayScale;
import com.sid.ocr.utilities.GetScaled;
import com.sid.ocr.utilities.ImageToText;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import net.sourceforge.tess4j.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    @Autowired
    GetBinarized getBinarized;
    @Autowired
    GetGrayScale getGrayScale;
    @Autowired
    GetScaled getScaled;
    @Autowired
    ImageToText imageToText;
    @Autowired
    BusinessLogic businessLogic;
    @Value("${upload.image.path}")
    private String uploadPath;
    @Value("${image.processed}")
    private String processedPath;

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    @ResponseBody
    public void uploadFile(
            @RequestParam("uploadFile") MultipartFile uploadFile) {

        try {

            String filepath = Paths.get(uploadPath, "tutorial.jpg").toString();

            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadFile.getBytes());
            stream.close();

//            BufferedImage image = ImageIO.read(new File(filepath));
//            image = getScaled.getScaledImage(image);
//            image = getGrayScale.getGrayScale(image);
//            image = getBinarized.binarize(image);
//            for (Word word : imageToText.convertImageToText(image)){
//                System.out.println(word.getText());
//            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RequestMapping(value = "/getOriginalImage", method = RequestMethod.GET)
    @ResponseBody
    public String getOriginalImage() {
        return createEncodedImage(uploadPath, "tutorial.jpg");
    }

    @RequestMapping(value = "/getScaledImage", method = RequestMethod.GET)
    @ResponseBody
    public String getScaledImage() throws IOException {
        BufferedImage tempImage = getScaled.getScaledImage(ImageIO.read(new File(Paths.get(uploadPath, "tutorial.jpg").toString())));
        ImageIO.write(tempImage, "jpg", new File(Paths.get(uploadPath, "tutorial_scaled.jpg").toString()));
        return createEncodedImage(uploadPath, "tutorial_scaled.jpg");
    }

    @RequestMapping(value = "/getGrayImage", method = RequestMethod.GET)
    @ResponseBody
    public String getGrayImage() throws IOException {
        BufferedImage tempImage = getGrayScale.getGrayScale(ImageIO.read(new File(Paths.get(uploadPath, "tutorial_scaled.jpg").toString())));
        ImageIO.write(tempImage, "jpg", new File(Paths.get(uploadPath, "tutorial_grayScale.jpg").toString()));
        return createEncodedImage(uploadPath, "tutorial_grayScale.jpg");
    }

    @RequestMapping(value = "/getBinarizedImage", method = RequestMethod.GET)
    @ResponseBody
    public String getBinarizedImage() throws IOException {
        BufferedImage tempImage = getBinarized.binarize(ImageIO.read(new File(Paths.get(uploadPath, "tutorial_grayScale.jpg").toString())));
        ImageIO.write(tempImage, "jpg", new File(Paths.get(uploadPath, "tutorial_binarized.jpg").toString()));
        return createEncodedImage(uploadPath, "tutorial_binarized.jpg");
    }

    @RequestMapping(value = "/getText", method = RequestMethod.GET)
    @ResponseBody
    public String getText() throws IOException {

        List<Word> words = imageToText.convertImageToString(ImageIO.read(new File(Paths.get(uploadPath, "tutorial_binarized.jpg").toString())));
        return words.get(0).getText().trim();
    }

    @RequestMapping(value = "/getAlerts", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<String>> getAlerts() throws IOException {

        Map<String, List<String>> alertedValues = new HashMap<>();
        List<String> alertImages;
        for (Map.Entry<String, List<String>> entry : Application.alertDetails.entrySet()) {
            alertImages = new ArrayList<>();
            for (String image : entry.getValue()) {
                alertImages.add(createEncodedAlert(image));
            }
            alertedValues.put(entry.getKey().toUpperCase(), alertImages);
        }
        return alertedValues;
    }


    private String createEncodedImage(String uploadPath, String fileName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String encodedImage = null;

        try {
            InputStream inputStream = new FileInputStream(Paths.get(uploadPath, fileName).toString());
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            encodedImage = Base64.encode(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    private String createEncodedAlert(String fileName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String encodedImage = null;

        try {
            InputStream inputStream = new FileInputStream(fileName);
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            encodedImage = Base64.encode(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodedImage;
    }
}
