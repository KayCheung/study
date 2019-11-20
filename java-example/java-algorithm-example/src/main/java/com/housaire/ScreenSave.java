package com.housaire;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/11/18 15:37
 * @see
 * @since 1.0.0
 */
public class ScreenSave
{

    public static void main(String[] args)
    {
        try {
            //获取屏幕大小
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            //截图
            BufferedImage bim = new Robot()
                    .createScreenCapture(new Rectangle(550, 130, 700,
                            800));
            ImageIO.write(bim, "png", new File("E:\\TIM截图.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
