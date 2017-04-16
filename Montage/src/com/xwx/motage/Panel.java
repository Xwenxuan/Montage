package com.xwx.motage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * 背景图片
 * @author xiaowenxuan
 *
 */
public class Panel extends JPanel{
	BufferedImage img;
    
	public Panel() {
		try {
			img = ImageIO.read(new File("C:\\Users\\xiaowenxuan\\Desktop\\bg.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img,0,0, getWidth(), getHeight(), null);
	}
}
