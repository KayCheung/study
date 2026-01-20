package com.housaire.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/3/7 0007
 * @Since 1.0
 */
public class ImageEncodeDecode {

    public static void main(String[] args) {

        File I = new File("H:\\imageed\\raw.jpg");
        File IW = new File("H:\\imageed\\out.jpg");
        File NI = new File("H:\\imageed\\new.jpg");
        int pwd = 96585678;
        encode(pwd, I, IW);
        decode(pwd, IW, NI);

        int[] f1 = getFeature(I);
        Arrays.sort(f1);
        System.out.println("I " + Arrays.toString(f1));
        int[] f2 = getFeature(IW);
        Arrays.sort(f2);
        System.out.println("IW " + Arrays.toString(f2));
        int[] f3 = getFeature(NI);
        Arrays.sort(f3);
        System.out.println("NI " + Arrays.toString(f3));

        System.out.println(Arrays.equals(f1, f3) && Arrays.equals(f2, f3));//true
    }

    public static int toGray(int color) {
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;
        return (r * 30 + g * 60 + b * 10) / 100;
    }

    //获取特征
    public static int[] getFeature(File I) {
        try {
            BufferedImage imageI = ImageIO.read(I);
            int widthI = imageI.getWidth(),
                    heightI = imageI.getHeight();
            int sizeI = widthI * heightI;

            //gray
            int[] gray = new int[sizeI];
            for (int i = 0; i < widthI; i++) {
                for (int j = 0; j < heightI; j++) {
                    int color = imageI.getRGB(i, j);
                    gray[j * widthI + i] = toGray(color);
                }
            }

            int b = 20;
            int col, row;

            int blockCount = (widthI / b) * (heightI / b);
            int[] feature = new int[blockCount];

            int perRowCount = heightI / b;
            int perColCount = widthI / b;
            int avg;
            //块循环
            for (int i = 0; i < blockCount; i++) {
                col = (i % perColCount) * b;
                row = (i / perRowCount) * b;
                avg = 0;
                //块内循环
                for (int j = 0; j < b; j++) {
                    for (int k = 0; k < b; k++) {
                        avg += gray[(row + j) * widthI + col + k];
                    }
                }
                avg /= b * b;
                feature[i] = avg;
            }
            //即求每个块灰度的平均值
            return feature;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //加密
    public static void encode(int pwd, File I, File IW) {
        float x0, x0_2;
        float u = 4f;
        pwd %= 100000000;
        x0 = (pwd % 10000) * 0.0001f;//1~4
        x0_2 = (pwd / 10000) * 0.0001f;//5~8

        try {
            BufferedImage imageI = ImageIO.read(I);
            int widthI = imageI.getWidth(), heightI = imageI.getHeight();
            int sizeI = widthI * heightI;
            System.err.println("宽：" + widthI + "   高：" + heightI);

            int b = 20;

            //gray
            int[] gray = new int[sizeI];
            for (int i = 0; i < widthI; i++) {
                for (int j = 0; j < heightI; j++) {
                    int color = imageI.getRGB(i, j);
                    gray[j * widthI + i] = toGray(color);
                }
            }
            int blockCount = (widthI / b) * (heightI / b);
            //块置换序列
            float[] x = new float[blockCount];
            x[0] = x0;
            for (int i = 0; i < 500; i++)
                x[0] = u * x[0] * (1 - x[0]);
            for (int i = 0; i < blockCount - 1; i++)
                x[i + 1] = u * x[i] * (1 - x[i]);
            int[] index = sort(x);

            //块内置换序列
            float[] x2 = new float[b * b];
            x2[0] = x0_2;
            for (int i = 0; i < 500; i++)
                x2[0] = u * x2[0] * (1 - x2[0]);
            for (int i = 0; i < b * b - 1; i++)
                x2[i + 1] = u * x2[i] * (1 - x2[i]);
            int[] index2 = sort(x2);

            int[] rgb = new int[sizeI];
            int[] rgb_rep = new int[sizeI];
            int col, row;

            int perRowCount = heightI / b;
            int perColCount = widthI / b;
            for (int i = 0; i < blockCount; i++) {
                col = (i % perColCount) * b;
                row = (i / perRowCount) * b;
                int repCol = (index[i] % perRowCount) * b;
                int repRow = (index[i] / perRowCount) * b;
                //块置换
                for (int j = 0; j < b; j++) {
                    for (int k = 0; k < b; k++) {
                        //复制打乱的块
                        rgb[(row + j) * widthI + col + k] = gray[(repRow + j) * widthI + repCol + k];
                    }
                }
                //这2个循环应该可以合并。。
                //懒得管了
                //块内置换
                for (int j = 0; j < b; j++) {
                    for (int k = 0; k < b; k++) {
                        //打乱块内像素
                        int pos = index2[j * b + k];
                        int nCol = (pos % b);
                        int nRow = pos / b;
                        rgb_rep[(row + j) * widthI + col + k] = rgb[(row + nRow) * widthI + col + nCol];
                    }
                }
            }
            //灰度值转颜色
            for (int i = 0; i < sizeI; i++) {
                int g = rgb_rep[i];
                rgb_rep[i] = g << 16 | g << 8 | g;
            }

            BufferedImage image = new BufferedImage(widthI, heightI, imageI.getType());
            image.setRGB(0, 0, widthI, heightI, rgb_rep, 0, widthI);
            String suffix = I.getName().substring(I.getName().lastIndexOf('.') + 1);
            ImageIO.write(image, suffix, IW);
            image.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //加密的逆过程
    public static void decode(int pwd, File IW, File I) {
        float x0, x0_2;
        float u = 4f;
        pwd %= 100000000;
        x0 = (pwd % 10000) * 0.0001f;//1~4
        x0_2 = (pwd / 10000) * 0.0001f;//5~8

        try {
            BufferedImage imageIW = ImageIO.read(IW);
            int widthIW = imageIW.getWidth(),
                    heightIW = imageIW.getHeight();
            int sizeIW = widthIW * heightIW;

            //gray
            int[] gray = new int[sizeIW];
            for (int i = 0; i < widthIW; i++) {
                for (int j = 0; j < heightIW; j++) {
                    int color = imageIW.getRGB(i, j);
                    gray[j * widthIW + i] = toGray(color);
                }
            }

            int b = 20;
            int blockCount = (widthIW / b) * (heightIW / b);
            int perRowCount = heightIW / b;
            int perColCount = widthIW / b;
            int col, row;

            //块置换序列
            float[] x = new float[blockCount];
            x[0] = x0;
            for (int i = 0; i < 500; i++)
                x[0] = u * x[0] * (1 - x[0]);
            for (int i = 0; i < blockCount - 1; i++)
                x[i + 1] = u * x[i] * (1 - x[i]);
            int[] index = sort(x);

            //块内置换序列
            float[] x2 = new float[b * b];
            x2[0] = x0_2;
            for (int i = 0; i < 500; i++)
                x2[0] = u * x2[0] * (1 - x2[0]);
            for (int i = 0; i < b * b - 1; i++)
                x2[i + 1] = u * x2[i] * (1 - x2[i]);
            int[] index2 = sort(x2);

            int[] rgb = new int[sizeIW];
            int[] rgb_rep = new int[sizeIW];

            for (int i = 0; i < blockCount; i++) {
                col = (i % perRowCount) * b;
                row = (i / perRowCount) * b;
                for (int j = 0; j < b; j++) {
                    for (int k = 0; k < b; k++) {
                        int pos = index2[j * b + k];
                        int ncol = (pos % b);
                        int nrow = pos / b;
                        rgb_rep[(row + nrow) * widthIW + col + ncol] = gray[(row + j) * widthIW + col + k];
                    }
                }

                int repCol = (index[i] % perRowCount) * b;
                int repRow = (index[i] / perRowCount) * b;

                for (int j = 0; j < b; j++) {
                    for (int k = 0; k < b; k++) {
                        rgb[(repRow + j) * widthIW + repCol + k] = rgb_rep[(row + j) * widthIW + col + k];
                    }
                }
            }


            for (int i = 0; i < sizeIW; i++) {
                int g = rgb[i];
                rgb[i] = g << 16 | g << 8 | g;
            }


            BufferedImage image = new BufferedImage(widthIW, heightIW, imageIW.getType());
            image.setRGB(0, 0, widthIW, heightIW, rgb, 0, widthIW);
            String suffix = IW.getName().substring(IW.getName().lastIndexOf('.') + 1);
            ImageIO.write(image, suffix, I);
            image.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //根据混沌序列生成置换顺序
    private static int[] sort(float[] x) {
        int size = x.length;
        int[] index = new int[size];
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
