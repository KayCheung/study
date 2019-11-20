package com.housaire;

import com.recognition.software.jdeskew.ImageDeskew;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/11/18 16:29
 * @see
 * @since 1.0.0
 */
public class OCRImage
{
    static final double MINIMUM_DESKEW_THRESHOLD = 0.05d;

    public static void main(String[] args) throws TesseractException, IOException
    {
        BufferedImage textImage = ImageIO.read(new File("E:\\TIM截图.png"));
        // 这里对图片黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分
//        BufferedImage textImage = ImageHelper.convertImageToGrayscale(ImageHelper.getSubImage(image, 0, 0, 520, 800));
        // 图片锐化
        textImage = ImageHelper.convertImageToBinary(textImage);
        // 图片放大倍数,增强识别率(很多图片本身无法识别,放大5倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大5倍)
        textImage = ImageHelper.getScaledInstance(textImage, textImage.getWidth() * 5, textImage.getHeight() * 5);

        textImage = ImageHelper.convertImageToBinary(textImage);
        //ImageIO.write(textImage, "png", new File("C:\\Users\\JiangJie\\Pictures\\timg_temp.jpg"));

        ITesseract instance = new Tesseract();
        //如果未将tessdata放在根目录下需要指定绝对路径
        instance.setDatapath("E:\\SourceCode\\GitHub\\study\\java-example\\java-frame-example\\src\\main\\resources\\tessdata");

        //如果需要识别英文之外的语种，需要指定识别语种，并且需要将对应的语言包放进项目中
        instance.setLanguage("chi_sim");

        // 指定识别图片
        File imgDir = new File("E:\\TIM截图.png");
        long startTime = System.currentTimeMillis();
//        String ocrResult = instance.doOCR(imgDir);

        //按照每个字取词
        int pageIteratorLevel = ITessAPI.TessPageIteratorLevel.RIL_SYMBOL;
        BufferedImage bi = ImageIO.read(imgDir);
        /*List<Word> result = instance.getWords(bi, pageIteratorLevel);

        //print the complete result
        for (Word word : result) {
            System.err.println(word.toString());
        }*/

        ImageDeskew id = new ImageDeskew(bi);
        double imageSkewAngle = id.getSkewAngle(); // determine skew angle
        if ((imageSkewAngle > MINIMUM_DESKEW_THRESHOLD || imageSkewAngle < -(MINIMUM_DESKEW_THRESHOLD))) {
            bi = ImageHelper.rotateImage(bi, -imageSkewAngle); // deskew image
        }

        String ocrResult2 = instance.doOCR(bi);
        System.out.println(ocrResult2);

        // 输出识别结果
//        System.out.println("OCR Result: \n" + ocrResult + "\n 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }
}
