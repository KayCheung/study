package com.housaire.image;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
public class ImageEncryptionGUIWithAES extends JFrame {

    private BufferedImage originalImage = null;
    private BufferedImage encryptedImage = null;
    private BufferedImage decryptedImage = null;

    private JLabel originalLabel = new JLabel("原始图像", JLabel.CENTER);
    private JLabel encryptedLabel = new JLabel("加密后图像", JLabel.CENTER);
    private JLabel decryptedLabel = new JLabel("解密后图像", JLabel.CENTER);

    private JTextField passwordField = new JTextField(20);
    private JComboBox<String> algorithmCombo = new JComboBox<>(new String[]{"像素扰乱", "AES像素加密"});
    private JButton selectButton = new JButton("选择图片");
    private JButton encryptButton = new JButton("加密");
    private JButton decryptButton = new JButton("解密");

    public ImageEncryptionGUIWithAES() {
        setTitle("图像加密演示 - 支持双模式");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 顶部：密码 + 算法选择
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("密码:"));
        topPanel.add(passwordField);
        topPanel.add(new JLabel("  加密方式:"));
        topPanel.add(algorithmCombo);
        add(topPanel, BorderLayout.NORTH);

        // 底部：按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 中部：三图显示
        JPanel imagePanel = new JPanel(new GridLayout(1, 3, 10, 10));
        originalLabel.setBorder(BorderFactory.createTitledBorder("原始图像"));
        encryptedLabel.setBorder(BorderFactory.createTitledBorder("加密后（雪花图）"));
        decryptedLabel.setBorder(BorderFactory.createTitledBorder("解密后"));
        imagePanel.add(originalLabel);
        imagePanel.add(encryptedLabel);
        imagePanel.add(decryptedLabel);
        add(imagePanel, BorderLayout.CENTER);

        // 事件绑定
        selectButton.addActionListener(e -> selectImage());
        encryptButton.addActionListener(e -> encryptImage());
        decryptButton.addActionListener(e -> decryptImage());

        encryptButton.setEnabled(false);
        decryptButton.setEnabled(false);

        pack();
        setSize(1200, 520);
        setLocationRelativeTo(null);
    }

    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                originalImage = ImageIO.read(chooser.getSelectedFile());
                encryptedImage = null;
                decryptedImage = null;
                updateDisplays();
                encryptButton.setEnabled(true);
                decryptButton.setEnabled(false);
            } catch (IOException ex) {
                showError("无法读取图像: " + ex.getMessage());
            }
        }
    }

    private void encryptImage() {
        if (!validateInput()) {
            return;
        }
        try {
            String algo = (String) algorithmCombo.getSelectedItem();
            encryptedImage = "AES像素加密".equals(algo)
                    ? aesEncryptImage(originalImage, passwordField.getText())
                    : scrambleImage(originalImage, passwordField.getText(), true);
            decryptedImage = null;
            updateDisplays();
            decryptButton.setEnabled(true);
            showInfo("加密完成！图像已变为噪点图。");
        } catch (Exception ex) {
            showError("加密失败: " + ex.getMessage());
        }
    }

    private void decryptImage() {
        if (encryptedImage == null) {
            showError("请先加密一张图片！");
            return;
        }
        String password = passwordField.getText().trim();
        if (password.isEmpty()) {
            showError("请输入密码！");
            return;
        }
        try {
            String algo = (String) algorithmCombo.getSelectedItem();
            decryptedImage = "AES像素加密".equals(algo)
                    ? aesDecryptImage(encryptedImage, password)
                    : scrambleImage(encryptedImage, password, false);
            updateDisplays();
            showInfo("解密完成！图像已还原。");
        } catch (Exception ex) {
            showError("解密失败，请检查密码或加密方式是否匹配！");
        }
    }

    // ========== 模式1：像素扰乱 ==========
    private BufferedImage scrambleImage(BufferedImage img, String key, boolean encrypt) throws Exception {
        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        Random rand = new Random(getKeySeed(key));
        Integer[] idx = new Integer[pixels.length];
        for (int i = 0; i < idx.length; i++) {
            idx[i] = i;
        }
        for (int i = idx.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int t = idx[i]; idx[i] = idx[j]; idx[j] = t;
        }

        int[] result = new int[pixels.length];
        if (encrypt) {
            for (int i = 0; i < pixels.length; i++) {
                result[i] = pixels[idx[i]];
            }
        } else {
            int[] inv = new int[idx.length];
            for (int i = 0; i < idx.length; i++) {
                inv[idx[i]] = i;
            }
            for (int i = 0; i < pixels.length; i++) {
                result[inv[i]] = pixels[i];
            }
        }

        BufferedImage out = new BufferedImage(w, h, img.getType());
        out.setRGB(0, 0, w, h, result, 0, w);
        return out;
    }

    // ========== 模式2：AES 像素加密（CTR 模式）==========
    private BufferedImage aesEncryptImage(BufferedImage img, String password) throws Exception {
        byte[] keyBytes = deriveKey(password, 16); // AES-128
        byte[] iv = new byte[16]; // 固定 IV（实际应用中应随机并存储）
        Arrays.fill(iv, (byte) 0x55); // 简化：固定 IV，仅用于演示

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(iv));

        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        byte[] pixelBytes = intArrayToByteArray(pixels);
        byte[] encryptedBytes = cipher.doFinal(pixelBytes);
        int[] encryptedPixels = byteArrayToIntArray(encryptedBytes, pixels.length);

        BufferedImage out = new BufferedImage(w, h, img.getType());
        out.setRGB(0, 0, w, h, encryptedPixels, 0, w);
        return out;
    }

    private BufferedImage aesDecryptImage(BufferedImage img, String password) throws Exception {
        byte[] keyBytes = deriveKey(password, 16);
        byte[] iv = new byte[16];
        Arrays.fill(iv, (byte) 0x55);

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(iv));

        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        byte[] pixelBytes = intArrayToByteArray(pixels);
        byte[] decryptedBytes = cipher.doFinal(pixelBytes);
        int[] decryptedPixels = byteArrayToIntArray(decryptedBytes, pixels.length);

        BufferedImage out = new BufferedImage(w, h, img.getType());
        out.setRGB(0, 0, w, h, decryptedPixels, 0, w);
        return out;
    }

    // 工具方法
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
        return Arrays.copyOf(hash, length); // 截取前16字节用于AES-128
    }

    private long getKeySeed(String key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(key.getBytes());
        long seed = 0;
        for (int i = 0; i < 8; i++) {
            seed = (seed << 8) | (hash[i] & 0xFF);
        }
        return seed;
    }

    // UI 辅助
    private void updateDisplays() {
        originalLabel.setIcon(originalImage != null ? getScaledIcon(originalImage) : null);
        encryptedLabel.setIcon(encryptedImage != null ? getScaledIcon(encryptedImage) : null);
        decryptedLabel.setIcon(decryptedImage != null ? getScaledIcon(decryptedImage) : null);
        repaint();
    }

    private ImageIcon getScaledIcon(BufferedImage img) {
        if (img == null) {
            return null;
        }
        int w = img.getWidth(), h = img.getHeight();
        double scale = Math.min(350.0 / w, 400.0 / h);
        Image scaled = img.getScaledInstance((int)(w * scale), (int)(h * scale), Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private boolean validateInput() {
        if (originalImage == null) {
            showError("请先选择一张图片！");
            return false;
        }
        if (passwordField.getText().trim().isEmpty()) {
            showError("请输入密码！");
            return false;
        }
        return true;
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
            new ImageEncryptionGUIWithAES().setVisible(true);
        });
    }
}