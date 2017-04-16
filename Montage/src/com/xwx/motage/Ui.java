package com.xwx.motage;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;

/**
 * 程序开始
 * @author xiaowenxuan
 *
 */
public class Ui extends JFrame {

	private Panel contentPane;
	private JTextField input;
	private Model m = new Model();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ui frame = new Ui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Ui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 565, 469);
		contentPane = new Panel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setTitle("");
		
		JButton begin = new JButton("开始");
		begin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = input.getText();
				s = s.replaceAll(" ", "+");
				if(s.equals("")) {
					JOptionPane.showMessageDialog(Ui.this, "您的输入为空！");
				}else {
//					ExecutorService ex = Executors.newFixedThreadPool(5);
////					ex.submit(new Task("https://github.com/search?utf8=%E2%9C%93&q="+s+"&type=Users"));
//					ex.execute(new Task("https://github.com/search?utf8=%E2%9C%93&q="+s+"&type=Users"));
//					ex.shutdown();
				//抓取图片
				m.catchImg("https://github.com/search?utf8=%E2%9C%93&q="+s+"&type=Users");
				try {
					//target图片
					BufferedInputStream in = new BufferedInputStream(
							new FileInputStream(new File("F:\\img\\0.jpg")));
					byte[] bytes = new byte[1024];
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(new File("F:\\img\\target.jpg")));
					int length;
					while((length = (in.read(bytes)))!=-1) {
						out.write(bytes);
					}
					in.close();
					out.close();
					//装东西的图片
					BufferedImage con = m.convertImageTo(ImageIO.read(new File("F:\\img\\target.jpg")), 8000, 8000);
					//装东西的图片文件
					m.zoom("F:\\img\\target.jpg", "F:\\img\\target.jpg", 8000, 8000);
					//模板图片
					BufferedImage model = m.convertImageTo(ImageIO.read(new File("F:\\img\\src.jpg")), 200, 200);
					//模板图片每个像素点的灰度
					int[][] modelRGB = m.getImageMatrix(model);
					//小图片
					int size = new File("F:\\img").list().length;
					System.out.println(size);
					BufferedImage[] images = new BufferedImage[size-2];
					for(int i = 0;i<images.length;i++) {
						images[i] = m.convertImageTo(
								ImageIO.read(new File("F:\\img\\"+i+".jpg")), 40, 40);
					}
					//小图片灰度从小到大排序
					for (int i = 0;i<images.length-1;i++) {
						for(int j = 0;j<images.length-1-i;j++) {
							if(m.getValue(images[j])>m.getValue(images[j+1])) {
								BufferedImage img = null;
								img = images[j];
								images[j] = images[j+1];
								images[j+1] = img;
							}
						}
					}
					//填充颜色
					for (int j = 0; j < modelRGB.length; j++) {
				        for (int i = 0; i < modelRGB[0].length; i++) {
				            int index = modelRGB[j][i] * images.length / 255;
				            index = index >= images.length - 1 ? images.length - 1 : index;
				            m.addImg(images[index],con, i, j);
				        }
				    }
					//写入图片
					ImageIO.write(con, "jpg", new File("F:\\img\\target.jpg"));
				} catch (IOException exc) {
					exc.printStackTrace();
				}
				ShowFrame frame = new ShowFrame();
				}
			}
		});
		begin.setBackground(Color.GRAY);
		begin.setBounds(405, 298, 123, 29);
//		ImageIcon icon = new ImageIcon(
//				"C:\\Users\\xiaowenxuan\\Desktop\\bg.jpg");
//		Image temp = icon.getImage().getScaledInstance(
//				begin.getWidth(), begin.getHeight(), icon.getImage().SCALE_DEFAULT);
//		begin.setIcon(icon);
//		
		contentPane.add(begin);
		
		input = new JTextField();
		
		JButton addImg = new JButton("上传图片");
		addImg.setBackground(Color.GRAY);
		addImg.setBounds(24, 298, 123, 29);
		contentPane.add(addImg);
		addImg.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				UploadImg upLoad = new UploadImg();
			}
		});
		input.setBounds(230, 175, 80, 27);
		contentPane.add(input);
		input.setColumns(10);
	}
}
