package com.housaire;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Screenshot
{

    public void screenshot() throws Exception
    {
        //截图
        BufferedImage bim = new Robot()
                .createScreenCapture(new Rectangle(550, 130, 700,
                        800));
        ImageIO.write(bim, "png", new File("E:\\TIM截图.png"));
    }

}
