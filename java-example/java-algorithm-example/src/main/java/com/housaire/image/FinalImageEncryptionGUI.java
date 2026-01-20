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
public class FinalImageEncryptionGUI extends JFrame {

    private static final byte[] MAGIC = { 'A', 'E', 'S', 'I' }; // IV嵌入标识

    private BufferedImage originalImage = null;   // 左：用户加载的图（可作原图或加密图）
    private BufferedImage encryptedImage = null;  // 中：加密结果
    private BufferedImage decryptedImage = null;  // 右：解密结果（只读输出）

    private JLabel originalLabel = new JLabel("原始图像", JLabel.CENTER);
    private JLabel encryptedLabel = new JLabel("加密后图像", JLabel.CENTER);
    private JLabel decryptedLabel = new JLabel("解密后图像", JLabel.CENTER);

    // 仅两个输入源选项
    private JRadioButton originalRadio = new JRadioButton("原图");
    private JRadioButton encryptedRadio = new JRadioButton("加密图");
    private ButtonGroup inputGroup = new ButtonGroup();

    private JTextField passwordField = new JTextField(20);
    private JComboBox<String> algorithmCombo = new JComboBox<>(new String[]{
            "像素扰乱",
            "AES像素加密（无IV嵌入）",
            "AES像素加密（IV嵌入LSB）"
    });

    public FinalImageEncryptionGUI() {
        setTitle("图像加密系统 - 手动指定输入角色");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === 顶部：密码与算法 ===
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("密码:"));
        topPanel.add(passwordField);
        topPanel.add(new JLabel("  加密方式:"));
        topPanel.add(algorithmCombo);
        add(topPanel, BorderLayout.NORTH);

        // === 中部：三图显示区 ===
        JPanel imagePanel = new JPanel(new GridLayout(1, 3, 10, 5));
        setupImageDisplay(imagePanel, originalLabel, "原始图像");
        setupImageDisplay(imagePanel, encryptedLabel, "加密后");
        setupImageDisplay(imagePanel, decryptedLabel, "解密后");
        add(imagePanel, BorderLayout.CENTER);

        // === 底部：操作按钮 + Radio 按钮 ===
        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton selectButton = new JButton("选择图片");
        JButton encryptButton = new JButton("加密");
        JButton decryptButton = new JButton("解密");
        JButton saveButton = new JButton("保存加密图");
        JButton clearButton = new JButton("清空");

        bottomPanel.add(selectButton);
        bottomPanel.add(encryptButton);
        bottomPanel.add(decryptButton);
        bottomPanel.add(saveButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
        bottomPanel.add(originalRadio);
        bottomPanel.add(encryptedRadio);

        inputGroup.add(originalRadio);
        inputGroup.add(encryptedRadio);
        originalRadio.setSelected(true); // 默认选原图

        add(bottomPanel, BorderLayout.SOUTH);

        // === 事件绑定 ===
        selectButton.addActionListener(e -> selectImage());
        encryptButton.addActionListener(e -> encryptImage());
        decryptButton.addActionListener(e -> decryptImage());
        saveButton.addActionListener(e -> saveEncryptedImage());
        clearButton.addActionListener(e -> clearAll());

        resetDisplay();
        pack();
        setSize(1280, 600);
        setLocationRelativeTo(null);
    }

    private void setupImageDisplay(JPanel panel, JLabel label, String title) {
        label.setBorder(BorderFactory.createTitledBorder(title));
        label.setPreferredSize(new Dimension(380, 450));
        label.setVerticalAlignment(JLabel.TOP);
        panel.add(label);
    }

    private void selectImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                BufferedImage loaded = ImageIO.read(file);
                if (loaded == null) {
                    showError("无法读取图像文件");
                    return;
                }
                originalImage = loaded; // 总是加载到 originalImage
                // 注意：不清空其他图，也不改变 radio 选择
                resetDisplay();
            } catch (IOException ex) {
                showError("读取图像失败: " + ex.getMessage());
            }
        }
    }

    private void encryptImage() {
        BufferedImage input = getSelectedInputImage();
        if (input == null) {
            showError("请选择一张有效的图像进行加密！");
            return;
        }
        String password = getPassword();
        if (password.isEmpty()) {
            showError("请输入密码！");
            return;
        }

        try {
            String algo = (String) algorithmCombo.getSelectedItem();
            BufferedImage result;
            switch (algo) {
                case "像素扰乱":
                    result = scrambleImage(input, password, true);
                    break;
                case "AES像素加密（无IV嵌入）":
                    result = aesEncryptFixedIV(input, password);
                    break;
                case "AES像素加密（IV嵌入LSB）":
                    result = aesEncryptWithIvEmbedded(input, password);
                    break;
                default:
                    throw new IllegalArgumentException("未知算法");
            }
            encryptedImage = result;
            resetDisplay();
            showInfo("加密完成！");
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("加密失败: " + ex.getMessage());
        }
    }

    private void decryptImage() {
        // 关键：只有选中“加密图”才允许解密
        if (originalRadio.isSelected()) {
            showError("请先选择“加密图”作为输入源！");
            return;
        }

        BufferedImage input = getSelectedInputImage();
        if (input == null) {
            showError("没有可用的加密图像！");
            return;
        }
        String password = getPassword();
        if (password.isEmpty()) {
            showError("请输入密码！");
            return;
        }

        try {
            String algo = (String) algorithmCombo.getSelectedItem();
            BufferedImage result;
            switch (algo) {
                case "像素扰乱":
                    result = scrambleImage(input, password, false);
                    break;
                case "AES像素加密（无IV嵌入）":
                    result = aesDecryptFixedIV(input, password);
                    break;
                case "AES像素加密（IV嵌入LSB）":
                    byte[] iv = extractIVFromImage(input);
                    if (iv == null) {
                        showError("无法提取 IV，请确认图像是用 'IV嵌入LSB' 模式加密的！");
                        return;
                    }
                    result = aesDecryptWithIvEmbedded(input, password, iv);
                    break;
                default:
                    throw new IllegalArgumentException("未知算法");
            }
            decryptedImage = result;
            resetDisplay();
            showInfo("解密成功！");
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("解密失败: " + ex.getMessage());
        }
    }

    private BufferedImage getSelectedInputImage() {
        if (originalRadio.isSelected()) {
            return originalImage;
        } else if (encryptedRadio.isSelected()) {
            // 优先使用加密结果；若为空，则使用用户加载的图（视为外部加密图）
            return encryptedImage != null ? encryptedImage : originalImage;
        }
        return null;
    }

    private String getPassword() {
        return passwordField.getText().trim();
    }

    private void saveEncryptedImage() {
        if (encryptedImage == null) {
            showError("没有加密图像可保存！");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("encrypted.png"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".png")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
                ImageIO.write(encryptedImage, "PNG", file);
                showInfo("已保存至:\n" + file.getAbsolutePath());
            } catch (IOException ex) {
                showError("保存失败: " + ex.getMessage());
            }
        }
    }

    private void clearAll() {
        originalImage = null;
        encryptedImage = null;
        decryptedImage = null;
        passwordField.setText("");
        originalRadio.setSelected(true); // 默认回原图
        resetDisplay();
        showInfo("已清空所有内容。");
    }

    private void resetDisplay() {
        originalLabel.setIcon(originalImage != null ? getScaledIcon(originalImage) : null);
        encryptedLabel.setIcon(encryptedImage != null ? getScaledIcon(encryptedImage) : null);
        decryptedLabel.setIcon(decryptedImage != null ? getScaledIcon(decryptedImage) : null);
        repaint();
    }

    // ==================== 加密/解密核心方法 ====================

    private BufferedImage scrambleImage(BufferedImage img, String key, boolean encrypt) throws Exception {
        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        Random rand = new Random(getKeySeed(key));
        int[] perm = new int[pixels.length];
        for (int i = 0; i < perm.length; i++) perm[i] = i;
        // Fisher-Yates shuffle
        for (int i = perm.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int t = perm[i]; perm[i] = perm[j]; perm[j] = t;
        }

        int[] result = new int[pixels.length];
        if (encrypt) {
            for (int i = 0; i < perm.length; i++) {
                result[i] = pixels[perm[i]];
            }
        } else {
            int[] inv = new int[perm.length];
            for (int i = 0; i < perm.length; i++) {
                inv[perm[i]] = i;
            }
            for (int i = 0; i < perm.length; i++) {
                result[i] = pixels[inv[i]]; // ✅ 正确逆映射
            }
        }

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        out.setRGB(0, 0, w, h, result, 0, w);
        return out;
    }

    private BufferedImage aesEncryptFixedIV(BufferedImage img, String password) throws Exception {
        byte[] key = deriveKey(password, 16);
        byte[] iv = new byte[16]; Arrays.fill(iv, (byte) 0x33);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        byte[] enc = cipher.doFinal(intArrayToByteArray(pixels));
        int[] encPixels = byteArrayToIntArray(enc, pixels.length);

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        out.setRGB(0, 0, w, h, encPixels, 0, w);
        return out;
    }

    private BufferedImage aesDecryptFixedIV(BufferedImage img, String password) throws Exception {
        byte[] key = deriveKey(password, 16);
        byte[] iv = new byte[16]; Arrays.fill(iv, (byte) 0x33);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        byte[] dec = cipher.doFinal(intArrayToByteArray(pixels));
        int[] decPixels = byteArrayToIntArray(dec, pixels.length);

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        out.setRGB(0, 0, w, h, decPixels, 0, w);
        return out;
    }

    private BufferedImage aesEncryptWithIvEmbedded(BufferedImage img, String password) throws Exception {
        int w = img.getWidth(), h = img.getHeight();
        if (w * h < 20) {
            throw new IllegalArgumentException("图像太小（需≥20像素）");
        }
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        byte[] key = deriveKey(password, 16);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        byte[] enc = cipher.doFinal(intArrayToByteArray(pixels));
        int[] encPixels = byteArrayToIntArray(enc, pixels.length);

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        out.setRGB(0, 0, w, h, encPixels, 0, w);
        embedMagicAndIV(out, iv);
        return out;
    }

    private BufferedImage aesDecryptWithIvEmbedded(BufferedImage img, String password, byte[] iv) throws Exception {
        byte[] key = deriveKey(password, 16);
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

        int w = img.getWidth(), h = img.getHeight();
        int[] pixels = new int[w * h];
        img.getRGB(0, 0, w, h, pixels, 0, w);

        byte[] dec = cipher.doFinal(intArrayToByteArray(pixels));
        int[] decPixels = byteArrayToIntArray(dec, pixels.length);

        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        out.setRGB(0, 0, w, h, decPixels, 0, w);
        return out;
    }

    private void embedMagicAndIV(BufferedImage img, byte[] iv) {
        int w = img.getWidth();
        for (int i = 0; i < MAGIC.length; i++) {
            int x = i % w;
            int y = i / w;
            int rgb = img.getRGB(x, y);
            int newR = MAGIC[i] & 0xFF;
            img.setRGB(x, y, (rgb & 0xFF00FFFF) | (newR << 16));
        }
        for (int i = 0; i < iv.length; i++) {
            int idx = MAGIC.length + i;
            int x = idx % w;
            int y = idx / w;
            int rgb = img.getRGB(x, y);
            int newR = iv[i] & 0xFF;
            img.setRGB(x, y, (rgb & 0xFF00FFFF) | (newR << 16));
        }
    }

    private byte[] extractIVFromImage(BufferedImage img) {
        int total = img.getWidth() * img.getHeight();
        if (total < 20) return null;
        int w = img.getWidth();
        for (int i = 0; i < MAGIC.length; i++) {
            int x = i % w;
            int y = i / w;
            int r = (img.getRGB(x, y) >> 16) & 0xFF;
            if (r != MAGIC[i]) return null;
        }
        byte[] iv = new byte[16];
        for (int i = 0; i < 16; i++) {
            int idx = MAGIC.length + i;
            int x = idx % w;
            int y = idx / w;
            iv[i] = (byte) ((img.getRGB(x, y) >> 16) & 0xFF);
        }
        return iv;
    }

    // ==================== 工具方法 ====================

    private byte[] intArrayToByteArray(int[] arr) {
        byte[] b = new byte[arr.length * 4];
        for (int i = 0; i < arr.length; i++) {
            b[i * 4] = (byte) (arr[i] >> 24);
            b[i * 4 + 1] = (byte) (arr[i] >> 16);
            b[i * 4 + 2] = (byte) (arr[i] >> 8);
            b[i * 4 + 3] = (byte) arr[i];
        }
        return b;
    }

    private int[] byteArrayToIntArray(byte[] b, int len) {
        int[] arr = new int[len];
        for (int i = 0; i < len && i * 4 + 3 < b.length; i++) {
            arr[i] = ((b[i * 4] & 0xFF) << 24) |
                    ((b[i * 4 + 1] & 0xFF) << 16) |
                    ((b[i * 4 + 2] & 0xFF) << 8) |
                    (b[i * 4 + 3] & 0xFF);
        }
        return arr;
    }

    private byte[] deriveKey(String pwd, int len) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return Arrays.copyOf(md.digest(pwd.getBytes()), len);
    }

    private long getKeySeed(String key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(key.getBytes());
        long seed = 0;
        for (int i = 0; i < 8; i++) seed = (seed << 8) | (hash[i] & 0xFF);
        return seed;
    }

    private ImageIcon getScaledIcon(BufferedImage img) {
        if (img == null) return null;
        int w = img.getWidth(), h = img.getHeight();
        double scale = Math.min(360.0 / w, 400.0 / h);
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
            new FinalImageEncryptionGUI().setVisible(true);
        });
    }
}