package com.xwx.motage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ShowPanel extends JPanel{

	private BufferedImage img;
	public ShowPanel() {
		try {
			img = ImageIO.read(new File("F:\\img\\target.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(img, 0, 0,getWidth(),getHeight(),null);
	}
}
