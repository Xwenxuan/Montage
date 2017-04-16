package com.xwx.motage;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 模型
 * @author xiaowenxuan
 *
 */
public class Model {

	public synchronized void getImg (Element e,int i) {
		if(i<100){
		if(e.attr("class").equals("pagination")) {
			System.out.println("get");
			String followers = e.child(1).attr("abs:href");
			BufferedOutputStream out;
			OkHttpClient client = new OkHttpClient();
			try {
				Document doc = Jsoup.connect(followers).get();
				Elements links = doc.select("[class]");
				for(Element e2 : links) {
					if(e2.attr("class").
							equals("d-table-cell col-1 v-align-top")) {
						//粉丝图片链接
						String followerImg = e2.child(0).child(0).attr("abs:src");
						Request request2 = new Request.Builder().url(followerImg).build();
						Response response2 = client.newCall(request2).execute();
						byte[] imgData = response2.body().bytes();
						//粉丝头像图片
						out = new BufferedOutputStream(
								new FileOutputStream(new File("F:\\img\\"+i+++".jpg")));
						out.write(imgData);
						out.flush();
					}
					getImg(e2, i);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		}
	}
	/**
	 * 抓取github上用户头像及粉丝图片
	 * @param url
	 */
	public synchronized void catchImg(String url) {
		BufferedOutputStream out;
		OkHttpClient client = new OkHttpClient();
		int i = 0;
		try {
			Document doc = Jsoup.connect(url).get();
			Elements link = doc.select("[class]");
			for (Element e : link) {
				//找到索引的的第一个人的链接
				if (e.attr("class").equals("d-flex")) {
					String linkOne = e.child(0).attr("abs:href");
					//此人的粉丝页面
					String followers = linkOne+"?tab=followers";
					
					Document doc2 = Jsoup.connect(followers).get();
					Elements link2 = doc2.select("[class]");
					
					//创建文件夹
					File file = new File("F:\\img");
					if(!file.exists()) {
						file.mkdirs();
					}
						//用户图片链接
//						String user = e.child(0).child(0).attr("abs:src");
//						out = new BufferedOutputStream(
//								new FileOutputStream(new File("F:\\img\\src.jpg")));
//						Request request1 = new Request.Builder().url(user).build();
//						Response response1 = client.newCall(request1).execute();
//						byte[] userData = response1.body().bytes();
//						out.write(userData);
//						out.flush();
//					}
					//粉丝节点
					for(Element e2 : link2) {
						//用戶图片链接
						if(e2.attr("class").equals("avatar width-full rounded-2")) {
							String user = e2.attr("abs:src");
							out = new BufferedOutputStream(
									new FileOutputStream(new File("F:\\img\\src.jpg")));
							Request request1 = new Request.Builder().url(user).build();
							Response response1 = client.newCall(request1).execute();
							byte[] userData = response1.body().bytes();
							out.write(userData);
							out.flush();
						}
						if(e2.attr("class").
								equals("d-table-cell col-1 v-align-top")) {
							//粉丝图片链接
							String followerImg = e2.child(0).child(0).attr("abs:src");
							Request request2 = new Request.Builder().url(followerImg).build();
							Response response2 = client.newCall(request2).execute();
							byte[] imgData = response2.body().bytes();
							//粉丝头像图片
							out = new BufferedOutputStream(
									new FileOutputStream(new File("F:\\img\\"+i+++".jpg")));
							out.write(imgData);
							out.flush();
						}
						getImg(e2, i);
					}
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 缩放图片
	 * @param img
	 * @param w
	 * @param h
	 * @return
	 */
	public BufferedImage convertImageTo(BufferedImage img, int w, int h) {
	    BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    result.getGraphics().drawImage(img, 0, 0, w, h, null);
	    return result;
	}
	/**
	 * 一个像素点的灰度值
	 * @param img
	 * @return
	 */
	public int[][] getImageMatrix(BufferedImage img) {
	    int w = img.getWidth();
	    int h = img.getHeight();
	    int result[][] = new int[h][w];
	    for (int i = 0; i < w; i++) {
	        for (int j = 0; j < h; j++) {
	            int pixel = img.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
	            int r = (pixel & 0xff0000) >> 16;
	            int g = (pixel & 0xff00) >> 8;
	            int b = (pixel & 0xff);
	            int rgbResult = (int) (0.11 * r + 0.59 * g + 0.3 * b);
	            result[j][i] = rgbResult;
	        }
	    }
	    return result;
	}
	/**
	 * 所有像素点的平均灰度值
	 * @param img
	 * @return
	 */
	public int  getValue(BufferedImage img){
		int w = img.getWidth();
	    int h = img.getHeight();
	    int a = 0;
	    for (int i = 0; i < w; i++) {
	        for (int j = 0; j < h; j++) {
	            int pixel = img.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
	            int r = (pixel & 0xff0000) >> 16;
	            int g = (pixel & 0xff00) >> 8;
	            int b = (pixel & 0xff);
	            a += (int) (0.11 * r + 0.59 * g + 0.3 * b);
	        }
	    }
	  a = a/(w*h);
		return a ;
	}
	/**
	 * 添加图片
	 * @param ImageOne 小图片
	 * @param ImageNew 空白图片
	 * @param x 横坐标
	 * @param y 纵坐标
	 */
	public void addImg(BufferedImage ImageOne,BufferedImage ImageNew,int x,int y)
	 {
	     try
	     {
	       //读取第一张图片
	       int widthOne = ImageOne.getWidth();//图片宽度
	       int heightOne = ImageOne.getHeight();//图片高度
	       //从图片中读取RGB
	       int[] ImageArrayOne = new int[widthOne*heightOne];
	       ImageArrayOne = ImageOne.getRGB(0,0,widthOne,heightOne,ImageArrayOne,0,widthOne);
	       
	       //涂色
	       ImageNew.setRGB(x*40,y*40,widthOne,heightOne,ImageArrayOne,0,widthOne);//设置左半部分的RGB


	     }
	     catch(Exception e)
	     {
	       e.printStackTrace();
	     }
	 }
	/**
	 * 图片缩放
	 * @param srcPath 原图片
	 * @param targetPath 目标图片
	 * @param scale
	 */
	public void zoom(String srcPath, String targetPath, int newWidth,int newHeight) 
	 {   
	    
	        try 
	        {   
	            BufferedImage src = ImageIO.read(new File(srcPath));  // 读入源图像   
	                             
	            //  获取一个宽、长是原来scale的图像实例   
	            Image image = src.getScaledInstance(newWidth, 
	            		newHeight, Image.SCALE_DEFAULT);  
	            
	            //缩放图像
	            BufferedImage tag = new BufferedImage(newWidth, 
	            		newHeight, BufferedImage.TYPE_INT_RGB);     
	            Graphics2D g = tag.createGraphics();   
	            
	            g.drawImage(image, 0, 0, null); // 绘制缩小后的图   
	            g.dispose();   
	              
	            OutputStream out = new FileOutputStream(targetPath);   
	            ImageIO.write(tag, "JPG", out);// 输出 
	            out.close();   
	        } 
	        catch (IOException e) 
	        {   
	            e.printStackTrace();   
	        } 
	 }
}
