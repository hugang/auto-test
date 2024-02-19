package io.hugang.execute.ext;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import io.hugang.execute.Command;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * ocr command executor
 * <p>
 * parameters:
 * <br>target: ocr picture path
 * <br>value: ocr result txt path
 * <br>tessdata: tessdata path (<a href="https://github.com/tesseract-ocr/tessdata">...</a>)
 * <br>language: ocr language
 */
public class OcrCommand extends Command {
    private static final Log log = Log.get();
    private BufferedImage bufferedImage;

    public OcrCommand(String command, String target, String value) {
        super(command, target, value);
    }

    @Override
    public boolean _execute() {
        // use tesseract to read picture from target
        String imagePath = render(getTarget());
        String resultPath = render(getValue());
        String tessdata = render(getDictStr("tessdata"));
        String language = render(getDictStr("language"));

        try {
            // read image
            bufferedImage = ImageIO.read(new File(imagePath));
            // init tesseract
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath(tessdata);
            tesseract.setLanguage(language);
            // ocr
            String result = tesseract.doOCR(bufferedImage);
            // write result to file
            FileUtil.writeUtf8String(result, resultPath);
        } catch (IOException | TesseractException e) {
            log.error("ocr error", e);
            throw new RuntimeException(e);
        }
        return true;
    }
}
