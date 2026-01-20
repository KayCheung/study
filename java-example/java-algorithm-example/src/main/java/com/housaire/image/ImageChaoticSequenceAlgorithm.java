package com.housaire.image;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/3/10 0010
 * @Since 1.0
 */
public class ImageChaoticSequenceAlgorithm {

    public static void main(String[] args) {
        File raw = new File("H:\\imageed\\raw.bmp");
        process(raw, 123456, true, "raw_encoded");
//        File encoded = new File("H:\\imageed\\lean_encoded.bmp");
//        process(encoded, 123456, false, "lean_decoded");
//        jpeg2Bmp("H:\\imageed\\raw.jpg", "H:\\imageed\\raw.bmp");
    }

    public static void jpeg2Bmp(String inputPath, String outputPath) {
        try {
            long start = System.currentTimeMillis();

            // 加载Jpeg图片资源
            FileImageInputStream fiis = new FileImageInputStream(new File(inputPath));
            FileImageOutputStream fios = new FileImageOutputStream(new File(outputPath));
            ImageReader jpegReader = null;
            Iterator<ImageReader> it1 = ImageIO.getImageReadersByFormatName("jpeg");
            if (it1.hasNext()) {
                jpegReader = it1.next();
            }
            jpegReader.setInput(fiis);

            ImageWriter bmpWriter = null;
            Iterator<ImageWriter> it2 = ImageIO.getImageWritersByFormatName("bmp");
            if (it2.hasNext()) {
                bmpWriter = it2.next();
            }
            bmpWriter.setOutput(fios);
            BufferedImage br = jpegReader.read(0);
            bmpWriter.write(br);
            fiis.close();
            fios.close();

            System.out.println("jpeg 转 bmp，共耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String bmp2Jpeg(String filePath, String outPath) {
        try {
            long start = System.currentTimeMillis();
            // 加载bmp图片
            File file = new File(filePath);
            Image img = ImageIO.read(file);
            BufferedImage tag = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(img.getScaledInstance(img.getWidth(null), img.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);

            // 输出为Jpeg
            FileOutputStream out = new FileOutputStream(outPath);
            // JPEGImageEncoder可适用于其他图片类型的转换
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
            System.out.println("bmp 转 JPEG，共耗时：  " + (System.currentTimeMillis() - start) + " 毫秒");
            return outPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outPath;
    }


    public static File process(File input, int password, boolean encode, String name) {
        float x0_1, x0_2;
        float u = 4f;
        password %= 100000000;
        x0_1 = (password % 10000) * 0.0001f;//1~4
        x0_2 = (password / 10000) * 0.0001f;//5~8
        try {
            BufferedImage image = ImageIO.read(input);
            int width = image.getWidth(),
                    height = image.getHeight();
            int size = width * height;
            int[] rgb = new int[size];
            image.getRGB(0, 0, width, height, rgb, 0, width);
            float[] x = new float[size];
            int maxLoop = 20;

            x[0] = x0_1;
            for (int i = 0; i < maxLoop; i++)
                x[0] = u * x[0] * (1 - x[0]);
            for (int i = 0; i < size - 1; i++)
                x[i + 1] = u * x[i] * (1 - x[i]);

            System.out.println("排序生成置换矩阵开始......");
            int[] index = sort(x);
            System.out.println("排序生成置换矩阵完成......");
            //加密位置置换
            int[] rgb_rep = new int[size];
            System.out.println("加密像素置换开始......");
            if (encode) {
                for (int i = 0; i < size; i++)
                    rgb_rep[i] = rgb[index[i]];
            } else {
                System.arraycopy(rgb, 0, rgb_rep, 0, size);
            }
            //像素置换
            x[0] = x0_2;
            for (int i = 0; i < maxLoop; i++)
                x[0] = u * x[0] * (1 - x[0]);

            for (int i = 0; i < size - 1; i++)
                x[i + 1] = u * x[i] * (1 - x[i]);
            for (int i = 0; i < size; i++) {
                x[i] = x[i] * 0xffffff;
                rgb_rep[i] = rgb_rep[i] ^ (int) x[i];
            }
            System.out.println("加密像素置换结束......");
            //解密位置置换
            if (!encode) {
                for (int i = 0; i < size; i++)
                    rgb[index[i]] = rgb_rep[i];
                rgb_rep = rgb;
            }

            image.flush();
            image = new BufferedImage(width, height, image.getType());
            image.setRGB(0, 0, width, height, rgb_rep, 0, width);
            String suffix = input.getName().substring(input.getName().lastIndexOf('.') + 1);
            File out = new File(input.getParent(), name + "." + suffix);
            ImageIO.write(image, suffix, out);
            image.flush();
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File process2(File input, int password, boolean encode, String name) {
        float x0_1, x0_2;
        float u = 4f;
        password %= 100000000;
        x0_1 = (password % 10000) * 0.0001f;//1~4
        x0_2 = (password / 10000) * 0.0001f;//5~8
        try {
            BufferedImage image = ImageIO.read(input);
            int width = image.getWidth(), height = image.getHeight();
            int[] rgb = image.getRaster().getPixels(0, 0, width, height, (int[])null);
            int size = rgb.length;
            float[] x = new float[size];

            int maxLoop = 20;

            x[0] = x0_1;
            for (int i = 0; i < maxLoop; i++)
                x[0] = u * x[0] * (1 - x[0]);
            for (int i = 0; i < size - 1; i++)
                x[i + 1] = u * x[i] * (1 - x[i]);

            System.out.println("排序生成置换矩阵开始......");
            int[] index = sort(x);
            System.out.println("排序生成置换矩阵完成......");
            //加密位置置换
            int[] rgb_rep = new int[size];
            System.out.println("加密像素置换开始......");
            if (encode) {
                for (int i = 0; i < size; i++)
                    rgb_rep[i] = rgb[index[i]];
            } else {
                System.arraycopy(rgb, 0, rgb_rep, 0, size);
            }
            //像素置换
            x[0] = x0_2;
            for (int i = 0; i < maxLoop; i++)
                x[0] = u * x[0] * (1 - x[0]);

            for (int i = 0; i < size - 1; i++)
                x[i + 1] = u * x[i] * (1 - x[i]);
            for (int i = 0; i < size; i++) {
                x[i] = x[i] * 0xffffff;
                rgb_rep[i] = rgb_rep[i] ^ (int) x[i];
            }
            System.out.println("加密像素置换结束......");
            //解密位置置换
            if (!encode) {
                for (int i = 0; i < size; i++)
                    rgb[index[i]] = rgb_rep[i];
                rgb_rep = rgb;
            }

            image.flush();
            image = new BufferedImage(width, height, image.getType());
            image.setRGB(0, 0, width, height, rgb_rep, 0, width);
            String suffix = input.getName().substring(input.getName().lastIndexOf('.') + 1);
            File out = new File(input.getParent(), name + "." + suffix);
            ImageIO.write(image, suffix, out);
            image.flush();
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据混沌矩阵 排序生成置换矩阵
     * @param x
     * @return
     */
    private static int[] sort(float[] x) {
        int size = x.length;
        int[] index = new int[size];//置换表
        for (int i = 0; i < size; i++) {
            index[i] = i;
        }
        for (int i = 0; i < size - 1; i++) {
            int min = i;
            for (int j = i + 1; j < size; j++) {
                if (x[min] > x[j]) {
                    min = j;
                }
            }
            float temp = x[min];
            x[min] = x[i];
            x[i] = temp;

            int temp2 = index[min];
            index[min] = index[i];
            index[i] = temp2;
        }
        return index;
    }

}
