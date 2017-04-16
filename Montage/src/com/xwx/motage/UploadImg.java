package com.xwx.motage;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import javafx.stage.FileChooser;

import java.awt.FlowLayout;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

/**
 * 上传图片窗口
 * @author xiaowenxuan
 *
 */
public class UploadImg extends JFrame {
	private JTextField textFile;
	private JFileChooser chooser;
	Model model = new Model();

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					App frame = new App();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public UploadImg() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		setTitle("图片上传");
		
		JButton fileSelect = new JButton("选择文件");
		fileSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooser = new JFileChooser();
				FileFilter filter = new FileFilter() {
					
					@Override
					public String getDescription() {
						return "images";
					}
					
					@Override
					public boolean accept(File f) {
						String name = f.getName();
						name = name.toLowerCase();
						return name.endsWith(".jpg")
								||name.endsWith(".png")
								||name.endsWith(".gif")
								||f.isDirectory();
					}
				};
				chooser.setFileFilter(filter);
				chooser.showOpenDialog(UploadImg.this);
				String path = chooser.getSelectedFile().getAbsolutePath();
				textFile.setText(path);
			}
		});
		fileSelect.setBounds(259, 126, 123, 29);
		getContentPane().add(fileSelect);
		
		textFile = new JTextField();
		textFile.setBounds(57, 127, 187, 27);
		getContentPane().add(textFile);
		textFile.setColumns(10);
		
		JButton begin = new JButton("开始");
		begin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BufferedImage con = model.convertImageTo(
							ImageIO.read(new File("F:\\img2\\target.jpg")), 3200, 3200);
					model.zoom("F:\\img2\\target.jpg", "F:\\img2\\target.jpg",
							8000, 8000);
					BufferedImage modelImg = model.convertImageTo(
							ImageIO.read(chooser.getSelectedFile()), 80,80);
					int[][] modelRGB = model.getImageMatrix(modelImg);
					File file = new File("F:\\img2");
					File[] files = file.listFiles();
					BufferedImage images[] = new  BufferedImage[files.length];
					for (int i = 0;i<images.length;i++) {
						images[i] = model.convertImageTo(ImageIO.read(files[i]), 40, 40);
					}
					for (int i = 0;i<images.length-1;i++) {
						for(int j = 0;j<images.length-1-i;j++) {
							if(model.getValue(images[j])>model.getValue(images[j+1])) {
								BufferedImage img = null;
								img = images[j];
								images[j] = images[j+1];
								images[j+1] = img;
							}
						}
					}
					for (int j = 0; j < modelRGB.length; j++) {
				        for (int i = 0; i < modelRGB[0].length; i++) {
				            int index = modelRGB[j][i] * images.length / 255;
				            index = index >= images.length - 1 ? images.length - 1 : index;
				            model.addImg(images[index],con, i, j);
				        }
				    }
					ImageIO.write(con, "JPG", new File("F:\\img2\\target.jpg"));
					JOptionPane.showMessageDialog(UploadImg.this, "完成！");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		begin.setBounds(121, 183, 123, 29);
		getContentPane().add(begin);
	}
}
