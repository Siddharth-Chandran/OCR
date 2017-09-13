package com.sid.ocr.utilities;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;
import org.springframework.stereotype.Repository;

import java.awt.image.BufferedImage;
import java.util.List;

@Repository
public class ImageToText {


    private Tesseract tesseract;

    public ImageToText() {
        tesseract = new Tesseract();
    }

    public List<Word> convertImageToWords(BufferedImage image) {

        return tesseract.getWords(image, TessAPI.TessPageIteratorLevel.RIL_WORD);
    }

    public List<Word> convertImageToSentence(BufferedImage image) {

        return tesseract.getWords(image, TessAPI.TessPageIteratorLevel.RIL_TEXTLINE);
    }

    public List<Word> convertImageToString(BufferedImage image) {

        return tesseract.getWords(image, TessAPI.TessPageIteratorLevel.RIL_BLOCK);
    }
}
