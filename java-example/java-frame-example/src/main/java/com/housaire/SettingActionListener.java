package com.housaire;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/11/19 11:44
 * @see
 * @since 1.0.0
 */
public class SettingActionListener implements ActionListener
{

    private Frame parentComponent;

    public SettingActionListener(Frame parentComponent)
    {
        this.parentComponent = parentComponent;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        // 创建一个模态对话框
        final JDialog dialog = new JDialog(this.parentComponent, "设置", true);
        // 设置对话框的宽高
        dialog.setSize(300, 200);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(this.parentComponent);
        dialog.setLayout(null);

        // 创建一个标签显示消息内容
        JLabel imagePathLabel = new JLabel("存储地址：");
        imagePathLabel.setBounds(10, 15, 70, 20);
        JTextField tfImagePath = new JTextField();
        tfImagePath.setBounds(85, 12, 140, 30);

        JButton btFileChooser = new JButton("选择");
        btFileChooser.setBounds(230, 12, 60, 30);
        btFileChooser.addActionListener(e1 ->
        {
            JFileChooser fcImagePath = new JFileChooser();
            FileSystemView fsv = FileSystemView.getFileSystemView();
            fcImagePath.setCurrentDirectory(fsv.getHomeDirectory());
            fcImagePath.setDialogTitle("请选择文件夹...");
            fcImagePath.setApproveButtonText("选择");
            fcImagePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fcImagePath.showOpenDialog(dialog);
            if (JFileChooser.APPROVE_OPTION == result) {
                String path = fcImagePath.getSelectedFile().getPath();
                tfImagePath.setText(path);
            }
        });


        JLabel screenDimensionLabel = new JLabel("屏幕抓取X/Y：");
        screenDimensionLabel.setBounds(10, 60, 85, 20);
        JTextField screenDimensionX = new JTextField();
        screenDimensionX.setBounds(95, 55, 60, 30);
        JTextField screenDimensionY = new JTextField();
        screenDimensionY.setBounds(165, 55, 60, 30);

        JButton btScreen = new JButton("获取");
        btScreen.setBounds(230, 55, 60, 30);

        // 创建一个按钮用于关闭对话框
        JButton okBtn = new JButton("确定");
        okBtn.setBounds(120, 130, 60, 30);

        dialog.add(imagePathLabel);
        dialog.add(tfImagePath);
        dialog.add(btFileChooser);
        dialog.add(screenDimensionLabel);
        dialog.add(screenDimensionX);
        dialog.add(screenDimensionY);
        dialog.add(btScreen);
        dialog.add(okBtn);
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
