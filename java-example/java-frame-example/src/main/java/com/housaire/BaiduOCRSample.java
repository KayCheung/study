package com.housaire;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.util.LinkedHashMap;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/11/19 11:07
 * @see
 * @since 1.0.0
 */
public class BaiduOCRSample
{
    //设置APPID/AK/SK
    public static final String APP_ID = "58e31005d48f443ebe6ba012960a03ca";
    public static final String API_KEY = "5dc037eeaa9c420780e82b6c10eb13b7";
    public static final String SECRET_KEY = "90762ca2e0534a2a91d7f6eb88cc50de";

    private static AipOcr API_OCR_CLIENT = null;

    static
    {
        System.setProperty("aip.log4j.conf", "./log4j.properties");
        // 初始化一个AipOcr
        API_OCR_CLIENT = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        API_OCR_CLIENT.setConnectionTimeoutInMillis(2000);
        API_OCR_CLIENT.setSocketTimeoutInMillis(60000);
    }

    public static void ocrImage(String imagePath)
    {
        JSONObject res = API_OCR_CLIENT.basicGeneral(imagePath, new LinkedHashMap<>());
        System.out.println(res.toString(2));
    }

    public static void main(String[] args)
    {
        ocrImage("E:\\TIM截图.png");
    }
}
