package com.housaire.image;

import java.io.*;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/3/7 0007
 * @Since 1.0
 */
public class ImageEncodeDecode2 {

    public static void decryptFile(File decryptedFile,File newFile) throws IOException {
        FileInputStream fileInputStream=new FileInputStream(decryptedFile);
        FileOutputStream fileOutputStream=new FileOutputStream(newFile);
        BufferedInputStream inputStream=new BufferedInputStream(fileInputStream);
        BufferedOutputStream outputStream=new BufferedOutputStream(fileOutputStream);
        int b;
        while ((b=inputStream.read())!=-1){
            //与5进行异或
            outputStream.write(b^5);
        }
        outputStream.flush();//刷新流
        //关闭流
        inputStream.close();
        outputStream.close();
    }
    public static void encryptedFile(File oldFile,File newFile) throws IOException {
        FileInputStream fileInputStream=new FileInputStream(oldFile);
        FileOutputStream fileOutputStream=new FileOutputStream(newFile);
        BufferedInputStream inputStream=new BufferedInputStream(fileInputStream);
        BufferedOutputStream outputStream=new BufferedOutputStream(fileOutputStream);
        int b;
        while ((b=inputStream.read())!=-1){
            //与5进行异或
            outputStream.write(b^5);
        }
        outputStream.flush();//刷新流
        //关闭流
        inputStream.close();
        outputStream.close();
    }

    public static void main(String[] args) throws IOException {
//        encryptedFile(new File("H:\\imageed\\raw.jpg"),new File("H:\\imageed\\encode.jpg"));
        decryptFile(new File("H:\\imageed\\encode.jpg"), new File("H:\\imageed\\decode.jpg"));
    }

}
