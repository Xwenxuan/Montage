package com.xwx.motage;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * 显示窗口
 * @author xiaowenxuan
 *
 */
public class ShowFrame extends JFrame {

	private ShowPanel contentPane;


	/**
	 * Create the frame.
	 */
	public ShowFrame() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 650);
		contentPane = new ShowPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
