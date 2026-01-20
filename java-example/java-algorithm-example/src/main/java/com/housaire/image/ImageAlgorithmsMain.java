package com.housaire.image;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2023/3/9 0009
 * @Since 1.0
 */
public class ImageAlgorithmsMain {

    volatile File in, encoded;
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    int size;
    JLabel rawLabel;
    JLabel encodedLabel;
    JLabel decodedLabel;

    private void init() {
        try {
            String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);


            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();

            final JFrame jf = new JFrame("Logistic混沌序列图像加密");
            jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int jfWidth = width / 2;
            int jfHeight = height / 2;
            jf.setResizable(false);
            jf.setLocation((width - jfWidth) / 2, (height - jfHeight) / 2);
            jf.setPreferredSize(new Dimension(jfWidth, jfHeight));
            GridBagLayout gridBagLayout = new GridBagLayout();
            jf.setLayout(gridBagLayout);
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.NONE;

            int gap = 10;
            size = (jfWidth - gap * 4) / 3;
            gridBagConstraints.gridwidth = 6;
            gridBagConstraints.weightx = 1.0;

            rawLabel = new JLabel();
            encodedLabel = new JLabel();
            decodedLabel = new JLabel();

            rawLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            rawLabel.setPreferredSize(new Dimension(size, size));
            gridBagLayout.setConstraints(rawLabel, gridBagConstraints);
            jf.add(rawLabel);


            encodedLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            encodedLabel.setPreferredSize(new Dimension(size, size));
            gridBagLayout.setConstraints(encodedLabel, gridBagConstraints);
            jf.add(encodedLabel);

            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

            decodedLabel.setPreferredSize(new Dimension(size, size));
            decodedLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            gridBagLayout.setConstraints(decodedLabel, gridBagConstraints);
            jf.add(decodedLabel);

            JLabel label1 = new JLabel("原图");
            JLabel label2 = new JLabel("加密图");
            JLabel label3 = new JLabel("解密图");
            gridBagConstraints.gridwidth = 6;
            gridBagConstraints.weightx = 0;
            gridBagConstraints.weighty = 0;
            gridBagLayout.setConstraints(label1, gridBagConstraints);

            jf.add(label1);
            gridBagLayout.setConstraints(label2, gridBagConstraints);
            jf.add(label2);
            gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
            gridBagLayout.setConstraints(label3, gridBagConstraints);
            jf.add(label3);

            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.gridwidth = 0;
            JPanel jp2 = new JPanel();
            final JTextField inputP = new JTextField(20);
            inputP.setBorder(BorderFactory.createTitledBorder("密码（数字8位）"));
            inputP.setText("12345678");
            jp2.add(inputP);
            gridBagLayout.setConstraints(jp2, gridBagConstraints);
            jf.add(jp2);

            gridBagConstraints.fill = GridBagConstraints.NONE;
            JPanel jp = new JPanel();
            JButton choose = new JButton("选择图片");
            jp.add(choose);
            JButton enc = new JButton("加密");
            gridBagLayout.setConstraints(enc, gridBagConstraints);
            jp.add(enc);
            JButton dec = new JButton("解密");
            gridBagLayout.setConstraints(dec, gridBagConstraints);
            jp.add(dec);
            gridBagLayout.setConstraints(jp, gridBagConstraints);
            jf.add(jp);
            choose.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int p;
                    try {
                        p = Integer.parseInt(inputP.getText());
                    } catch (Exception er) {
                        JOptionPane.showMessageDialog(null, "请输入正确数字", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }


                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    jFileChooser.setFileFilter(new FileNameExtensionFilter(
                            "BMP图像(*.bmp)", "bmp"));
                    int result = jFileChooser.showOpenDialog(jf);
                    if (result == JFileChooser.CANCEL_OPTION) {
                        return;
                    }

                    in = jFileChooser.getSelectedFile();
                    final ImageIcon rawIcon;
                    try {
                        rawIcon = new ImageIcon(ImageIO.read(in).getScaledInstance(size, size, Image.SCALE_DEFAULT));
                        rawLabel.setIcon(rawIcon);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    /*doEnc(p);
                    doDec(p);*/
                }
            });

            enc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int p;
                    try {
                        p = Integer.parseInt(inputP.getText());
                    } catch (Exception er) {
                        JOptionPane.showMessageDialog(null, "请输入正确数字", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (in != null) {
                        doEnc(p);
                    } else {
                        JOptionPane.showMessageDialog(null, "请先选择一副图像", "错误", JOptionPane.ERROR_MESSAGE);

                    }
                }
            });
            dec.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int p;
                    try {
                        p = Integer.parseInt(inputP.getText());
                    } catch (Exception er) {
                        JOptionPane.showMessageDialog(null, "请输入正确数字", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (in != null) {
                        doDec(p);
                    } else {
                        JOptionPane.showMessageDialog(null, "请先选择一副图像", "错误", JOptionPane.ERROR_MESSAGE);

                    }
                }
            });
            jf.pack();
            jf.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "错误" + e, "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doEnc(final int pass) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                encoded = process(in, pass, true, "encoded");
                if (encoded != null)
                    try {
                        final ImageIcon encodedIcon = new ImageIcon(ImageIO.read(encoded).getScaledInstance(size, size, Image.SCALE_DEFAULT));
                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                encodedLabel.setIcon(encodedIcon);
                            }
                        });

                    } catch (Exception ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(null, "解析设置图像错误", "错误", JOptionPane.ERROR_MESSAGE);
                    }
            }
        });
    }

    private void doDec(final int pass) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                File decoded = process(encoded, pass, false, "decoded");
                if (decoded != null)
                    try {
                        final ImageIcon decodedIcon = new ImageIcon(ImageIO.read(decoded).getScaledInstance(size, size, Image.SCALE_DEFAULT));

                        SwingUtilities.invokeAndWait(new Runnable() {
                            @Override
                            public void run() {
                                decodedLabel.setIcon(decodedIcon);
                            }
                        });

                    } catch (Exception ioException) {
                        ioException.printStackTrace();
                        JOptionPane.showMessageDialog(null, "解析设置图像错误", "错误", JOptionPane.ERROR_MESSAGE);
                    }
            }
        });
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        final ImageAlgorithmsMain main = new ImageAlgorithmsMain();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                main.init();
            }
        });
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

            x[0] = x0_1;
            for (int i = 0; i < 500; i++)
                x[0] = u * x[0] * (1 - x[0]);
            for (int i = 0; i < size - 1; i++)
                x[i + 1] = u * x[i] * (1 - x[i]);

            int[] index = sort(x);
            //加密位置置换
            int[] rgb_rep = new int[size];
            if (encode) {
                for (int i = 0; i < size; i++)
                    rgb_rep[i] = rgb[index[i]];
            } else {
                System.arraycopy(rgb, 0, rgb_rep, 0, size);
            }
            //像素置换
            x[0] = x0_2;
            for (int i = 0; i < 500; i++)
                x[0] = u * x[0] * (1 - x[0]);

            for (int i = 0; i < size - 1; i++)
                x[i + 1] = u * x[i] * (1 - x[i]);
            for (int i = 0; i < size; i++) {
                x[i] = x[i] * 0xffffff;
                rgb_rep[i] = rgb_rep[i] ^ (int) x[i];
            }
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
            JOptionPane.showMessageDialog(null, "加解密错误" + e, "错误", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    //根据混沌矩阵 排序生成置换矩阵
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
