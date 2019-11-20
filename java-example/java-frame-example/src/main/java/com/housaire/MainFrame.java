package com.housaire;

import javax.swing.*;
import java.awt.*;

/**
 * @author <a href="mailto:zhangkai@chinayie.com">张凯</a>
 * @description:
 * @date 2019/11/19 11:31
 * @see
 * @since 1.0.0
 */
public class MainFrame extends JFrame
{
    public MainFrame()
    {
        //最上面的面板, 包含2个按钮
        JPanel panelNorth = new JPanel();
        JButton btSetting = new JButton("设置");
        btSetting.addActionListener(new SettingActionListener(this));//点击按钮后会执行actionPerformed方法
        JButton btStart = new JButton("开始");
        btStart.addActionListener(new StartActionListener());//点击按钮后会执行actionPerformed方法
        panelNorth.add(btSetting);
        panelNorth.add(btStart);

        //下面的面板 包含1个文本框
        JPanel panelSouth = new JPanel();
        JTextField jtfLog = new JTextField(15);
        panelSouth.add(jtfLog);

        add(panelNorth, BorderLayout.NORTH);//panelNorth放置到北面
        add(panelSouth, BorderLayout.SOUTH);//panelSouth放置到南面
        setTitle("抓取屏幕内容");// 窗口标题
        setSize(220, 120);// 窗口大小 宽300 高200
        setLocationRelativeTo(null);// 窗口居中
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 当窗口关闭时,程序结束
        setResizable(false);
    }

    public static void main(String[] args)
    {
        new MainFrame().setVisible(true);
    }

}
