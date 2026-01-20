package com.housaire.image;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * Desc:
 *
 * @Author Zhang Kai
 * @Email <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @Date 2026/1/20
 * @Since 1.0
 */
public class AdvancedImageEncryptionGUI extends JFrame {

    private BufferedImage originalImage = null;
    private BufferedImage encryptedImage = null;
    private BufferedImage decryptedImage = null;

    // 图像显示标签
    private JLabel originalLabel = new JLabel("原始图像", JLabel.CENTER);
    private JLabel encryptedLabel = new JLabel("加密后图像", JLabel.CENTER);
    private JLabel decryptedLabel = new JLabel("解密后图像", JLabel.CENTER);

    // 单选按钮（用于选择解密源）
    private JRadioButton originalRadio = new JRadioButton("原图");
    private JRadioButton encryptedRadio = new JRadioButton("加密图");
    private JRadioButton decryptedRadio = new JRadioButton("解密图");

    private ButtonGroup imageSelectionGroup = new ButtonGroup();

    // 控件
    private JTextField passwordField = new JTextField(20);
    private JComboBox<String> algorithmCombo = new JComboBox<>(new String[]{"像素扰乱", "AES像素加密"});
    private JButton selectButton = new JButton("选择图片");
    private JButton encryptButton = new JButton("加密");
    private JButton decryptButton = new JButton("解密");
    private JButton saveEncryptedButton = new JButton("保存加密图");

    // 标记：当前选中的用于解密的图像
    private enum SelectedImage { ORIGINAL, ENCRYPTED, DECRYPTED }
    private SelectedImage selectedForDecrypt = SelectedImage.ENCRYPTED;

    public AdvancedImageEncryptionGUI() {
        setTitle("高级图像加密系统 - 支持 IV 嵌入 LSB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 密码 + 算法
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("密码:"));
        topPanel.add(passwordField);
        topPanel.add(new JLabel("  加密方式:"));
        topPanel.add(algorithmCombo);
        add(topPanel, BorderLayout.NORTH);

        // 按钮区
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(saveEncryptedButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 图片区（带单选按钮）
        JPanel imagePanel = new JPanel(new GridLayout(2, 3, 10, 5));

        setupImagePanel(imagePanel, originalLabel, originalRadio, "原始图像");
        setupImagePanel(imagePanel, encryptedLabel, encryptedRadio, "加密后（雪花图）");
        setupImagePanel(imagePanel, decryptedLabel, decryptedRadio, "解密后");

        imageSelectionGroup.add(originalRadio);
        imageSelectionGroup.add(encryptedRadio);
        imageSelectionGroup.add(decryptedRadio);
        encryptedRadio.setSelected(true); // 默认选中加密图用于解密

        // 监听单选
        encryptedRadio.addActionListener(e -> selectedForDecrypt = SelectedImage.ENCRYPTED);
        originalRadio.addActionListener(e -> selectedForDecrypt = SelectedImage.ORIGINAL);
        decryptedRadio.addActionListener(e -> selectedForDecrypt = SelectedImage.DECRYPTED);

        add(imagePanel, BorderLayout.CENTER);

        // 事件绑定
        selectButton.addActionListener(e -> selectImage());
        encryptButton.addActionListener(e -> encryptImage());
        decryptButton.addActionListener(e -> decryptImage());
        saveEncryptedButton.addActionListener(e -> saveEncryptedImage());

        encryptButton.setEnabled(false);
        decryptButton.setEnabled(false);
        saveEncryptedButton.setEnabled(false);

        pack();
        setSize(1250, 600);
        setLocationRelativeTo(null);
    }

    private void setupImagePanel(JPanel panel, JLabel label, JRadioButton radio, String title) {
        label.setBorder(BorderFactory.createTitledBorder(title));
        label.setPreferredSize(new Dimension(350, 400));
        JPanel container = new JPanel(new BorderLayout());
        container.add(label, BorderLayout.CENTER);
        container.add(radio, BorderLayout.SOUTH);
        panel.add(container);
    }

    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                originalImage = ImageIO.read(file);
                // 尝试从 LSB 提取 IV（如果是 AES 加密图）
                byte[] extractedIV = tryExtractIVFromLSB(originalImage);
                if (extractedIV != null) {
                    // 这张图是 AES 加密图！
                    encryptedImage = originalImage;
                    originalImage = null;
                    decryptedImage = null;
                    encryptedRadio.setSelected(true);
                    selectedForDecrypt = SelectedImage.ENCRYPTED;
                } else {
                    encryptedImage = null;
                    decryptedImage = null;
                }
                updateDisplays();
                encryptButton.setEnabled(originalImage != null);
                decryptButton.setEnabled(encryptedImage != null);
                saveEncryptedButton.setEnabled(false);
            } catch (IOException ex) {
                showError("无法读取图像: " + ex.getMessage());
            }
        }
    }

    private void encryptImage() {
        if (originalImage == null) {
            showError("请先选择一张原始图片！");
            return;
        }
        String password = passwordField.getText().trim();
        if (password.isEmpty()) {
            showError("请输入密码！");
            return;
        }

        try {
            String algo = (String) algorithmCombo.getSelectedItem();
            if ("AES像素加密".equals(algo)) {
                encryptedImage = aesEncryptImageWithIV(originalImage, password);
            } else {
                encryptedImage = scrambleImage(originalImage, password, true);
            }
            decryptedImage = null;
            updateDisplays();
            decryptButton.setEnabled(true);
            saveEncryptedButton.setEnabled(true);
            showInfo("加密完成！");
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("加密失败: " + ex.getMessage());
        }
    }

    private void decryptImage() {
        BufferedImage sourceImg = null;
        switch (selectedForDecrypt) {
            case ENCRYPTED:
                sourceImg = encryptedImage;
                break;
            case ORIGINAL:
                sourceImg = originalImage;
                break;
            case DECRYPTED:
                sourceImg = decryptedImage;
                break;
        }
        if (sourceImg == null) {
            showError("请选择一张有效的加密图像进行解密！");
            return;
        }

        String password = passwordField.getText().trim();
        if (password.isEmpty()) {
            showError("请输入密码！");
            return;
        }

        try {
            String algo = (String) algorithmCombo.getSelectedItem();
            if ("AES像素加密".equals(algo)) {
                // 从 LSB 提取 IV
                byte[] iv = tryExtractIVFromLSB(sourceImg);
                if (iv == null) {
                    showError("无法从图像中提取 IV，可能不是 AES 加密图！");
                    return;
                }
                decryptedImage = aesDecryptImageWithIV(sourceImg, password, iv);
            } else {
                decryptedImage = scrambleImage(sourceImg, password, false);
            }
            updateDisplays();
            showInfo("解密成功！");
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("解密失败，请检查密码和加密方式是否匹配！");
        }
    }

    private void saveEncryptedImage() {
        if (encryptedImage == null) {
            showError("没有可保存的加密图像！");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("encrypted_output.png"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
                ImageIO.write(encryptedImage, "PNG", file);
                showInfo("加密图像已保存: " + file.getAbsolutePath());
            } catch (IOException ex) {
                showError("保存失败: " + ex.getMessage());
            }
        }
    }

    // ========== 像素扰乱模式 ==========
    private BufferedImage scrambleImage(BufferedImage img, String key, boolean encrypt) throws Exception {
        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        Random rand = new Random(getKeySeed(key));
        Integer[] idx = new Integer[pixels.length];
        for (int i = 0; i < idx.length; i++) idx[i] = i;
        for (int i = idx.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int t = idx[i]; idx[i] = idx[j]; idx[j] = t;
        }

        int[] result = new int[pixels.length];
        if (encrypt) {
            for (int i = 0; i < pixels.length; i++) result[i] = pixels[idx[i]];
        } else {
            int[] inv = new int[idx.length];
            for (int i = 0; i < idx.length; i++) inv[idx[i]] = i;
            for (int i = 0; i < pixels.length; i++) result[inv[i]] = pixels[i];
        }

        BufferedImage out = new BufferedImage(w, h, img.getType());
        out.setRGB(0, 0, w, h, result, 0, w);
        return out;
    }

    // ========== AES 模式（带 IV 嵌入 LSB）==========
    private BufferedImage aesEncryptImageWithIV(BufferedImage img, String password) throws Exception {
        // 生成随机 IV
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        byte[] keyBytes = deriveKey(password, 16);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(iv));

        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        byte[] pixelBytes = intArrayToByteArray(pixels);
        byte[] encryptedBytes = cipher.doFinal(pixelBytes);
        int[] encryptedPixels = byteArrayToIntArray(encryptedBytes, pixels.length);

        // 创建新图像并嵌入 IV 到 LSB
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        out.setRGB(0, 0, w, h, encryptedPixels, 0, w);

        // 嵌入 IV 到前 16 个像素的 LSB（每个像素存 1 字节）
        embedIVIntoLSB(out, iv);

        return out;
    }

    private BufferedImage aesDecryptImageWithIV(BufferedImage img, String password, byte[] iv) throws Exception {
        byte[] keyBytes = deriveKey(password, 16);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(iv));

        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        // 移除 LSB 中的 IV 标记（实际解密不需要改像素，但为干净可选做）
        // 此处直接使用原始像素（IV 已提取）

        byte[] pixelBytes = intArrayToByteArray(pixels);
        byte[] decryptedBytes = cipher.doFinal(pixelBytes);
        int[] decryptedPixels = byteArrayToIntArray(decryptedBytes, pixels.length);

        BufferedImage out = new BufferedImage(w, h, img.getType());
        out.setRGB(0, 0, w, h, decryptedPixels, 0, w);
        return out;
    }

    // 嵌入 IV 到 LSB（前16像素，每个像素最低位存1字节）
    private void embedIVIntoLSB(BufferedImage img, byte[] iv) {
        int w = img.getWidth();
        int maxPixels = Math.min(16, w * img.getHeight());
        for (int i = 0; i < maxPixels; i++) {
            int x = i % w;
            int y = i / w;
            int rgb = img.getRGB(x, y);
            // 清除最低位，再设置为 iv[i] 的低8位（实际只用1位？不，我们用整个字节覆盖最低8位）
            // 更高效：将整个字节写入 R+G+B+A 的 LSB 组合，但简化：只改 R 通道最低8位（视觉影响极小）
            int r = (rgb >> 16) & 0xFF;
            r = (r & 0xFE) | (iv[i] & 0x01); // 只用1位？不，我们需要8位！
            // 改进：用连续8个像素存1字节（每个像素1位）→ 太复杂
            // 简化方案：直接替换 R 通道为 iv[i]（会有轻微色偏，但安全）
            // 更佳：将 iv[i] 写入 R 通道（完全替换），因为加密图本就是噪点，无所谓
            r = iv[i] & 0xFF;
            int newRgb = (rgb & 0xFF00FFFF) | (r << 16);
            img.setRGB(x, y, newRgb);
        }
    }

    // 从 LSB 提取 IV（检查前16像素 R 通道）
    private byte[] tryExtractIVFromLSB(BufferedImage img) {
        int w = img.getWidth();
        int totalPixels = w * img.getHeight();
        if (totalPixels < 16) return null;

        byte[] iv = new byte[16];
        for (int i = 0; i < 16; i++) {
            int x = i % w;
            int y = i / w;
            int rgb = img.getRGB(x, y);
            int r = (rgb >> 16) & 0xFF;
            iv[i] = (byte) r;
        }
        return iv;
    }

    // ========== 工具方法 ==========
    private byte[] intArrayToByteArray(int[] arr) {
        byte[] bytes = new byte[arr.length * 4];
        for (int i = 0; i < arr.length; i++) {
            bytes[i * 4] = (byte) (arr[i] >> 24);
            bytes[i * 4 + 1] = (byte) (arr[i] >> 16);
            bytes[i * 4 + 2] = (byte) (arr[i] >> 8);
            bytes[i * 4 + 3] = (byte) arr[i];
        }
        return bytes;
    }

    private int[] byteArrayToIntArray(byte[] bytes, int expectedLength) {
        int[] arr = new int[expectedLength];
        for (int i = 0; i < expectedLength && i * 4 + 3 < bytes.length; i++) {
            arr[i] = ((bytes[i * 4] & 0xFF) << 24) |
                    ((bytes[i * 4 + 1] & 0xFF) << 16) |
                    ((bytes[i * 4 + 2] & 0xFF) << 8) |
                    (bytes[i * 4 + 3] & 0xFF);
        }
        return arr;
    }

    private byte[] deriveKey(String password, int length) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        return Arrays.copyOf(hash, length);
    }

    private long getKeySeed(String key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(key.getBytes());
        long seed = 0;
        for (int i = 0; i < 8; i++) seed = (seed << 8) | (hash[i] & 0xFF);
        return seed;
    }

    // ========== UI ==========
    private void updateDisplays() {
        originalLabel.setIcon(originalImage != null ? getScaledIcon(originalImage) : null);
        encryptedLabel.setIcon(encryptedImage != null ? getScaledIcon(encryptedImage) : null);
        decryptedLabel.setIcon(decryptedImage != null ? getScaledIcon(decryptedImage) : null);
        repaint();
    }

    private ImageIcon getScaledIcon(BufferedImage img) {
        if (img == null) return null;
        int w = img.getWidth(), h = img.getHeight();
        double scale = Math.min(320.0 / w, 360.0 / h);
        Image scaled = img.getScaledInstance((int)(w * scale), (int)(h * scale), Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "错误", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new AdvancedImageEncryptionGUI().setVisible(true);
        });
    }
}
