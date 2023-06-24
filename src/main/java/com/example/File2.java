package com.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;



public class File2 extends JFrame implements ActionListener {
    JButton jb = new JButton("导入文件2");
    String filename = "";
	
    public File2(){
		jb.setActionCommand("open");
		jb.setBackground(Color.GREEN);//设置按钮颜色
		this.getContentPane().add(jb, BorderLayout.SOUTH);//建立容器使用边界布局
		//
		jb.addActionListener(this);
		this.setTitle("标题");
		this.setSize(333, 288);
		this.setLocation(200,500);
		//显示窗口true
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("open")){
			JFileChooser jf = new JFileChooser();
			jf.showOpenDialog(this);//显示打开的文件对话框
			File f =  jf.getSelectedFile();//使用文件类获取选择器选择的文件
			String s = f.getAbsolutePath();//返回路径名
			//JOptionPane弹出对话框类，显示绝对路径名
			filename = s;
		}

    }
    
}
