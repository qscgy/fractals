package fractals;

import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
/**
 * Draws the Mandelbrot set using the Escape Time Algorithm.
 * @author qscgy
 *
 */

public class ColoredMandelbrot extends JFrame {
	BufferedImage img;
	final int SCALE=120;
	final int X_SHIFT=60;	//right shift
	final int Y_SHIFT=0;	//up shift
	final int MAX_ITER=2048;
	final float SAT=1f;
	final float BRIGHTNESS=0.8f;
	final float HUE_SHIFT=0.5f;
	final int COLOR_SCALE=8192;	//change this to change the color scheme
	final int COLOR_SHIFT=18;	//bit shift
	
	public ColoredMandelbrot(int width,int height){
		super("Mandelbrot Set");
		setBounds(0,0,width,height);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		img=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<getWidth();i++){
			for(int j=0;j<getHeight();j++){
				final Complex origin=new Complex(getWidth()/2,getHeight()/2);	//center of the window
				//Complex z=new Complex(0,0);
				double cx=(i-origin.a-X_SHIFT)/SCALE;
				double cy=(origin.b-j-Y_SHIFT)/SCALE;
				double zx=0;
				double zy=0;
				//System.out.println(i+" "+j);
				//Complex c=new Complex((i-origin.a-X_SHIFT)/SCALE,(origin.b-j-Y_SHIFT)/SCALE);	//i and j are the pixel coordinates, which wust be scaled down
				int k=0;
				while(k<MAX_ITER && zx*zx+zy*zy<=4){
					/*
					//System.out.println(z.a+" "+z.b);
					double tempZX=zx*zx-zy*zy+cx;
					zy=2*zx*zy+cy;
					zx=tempZX;
					//z.square();
					//z.add(c);
					 */
					double tempZX=Math.pow(zx, 3)-3*zx*zy*zy+cx;
					zy=3*zx*zx*zy-Math.pow(zy,3)+cy;
					zx=tempZX;
					//System.out.println(k);
					k++;
				}
				//System.out.println(zx*zx+zy*zy);
				//img.setRGB(i,j,(MAX_ITER-k)*COLOR_SCALE);
				//img.setRGB(i,j,k|(k<<COLOR_SHIFT));
				
				float brightness=k<MAX_ITER?BRIGHTNESS:0;
				float saturation=SAT;
				float hue=(k%256)/255.0f;
				if(hue+HUE_SHIFT>1)
					hue=hue-1;
				hue+=HUE_SHIFT;
				Color color=Color.getHSBColor(hue,saturation,brightness);
				img.setRGB(i,j,color.getRGB());
			}
		}
		//System.out.println("done");
	}
	
	public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}
	
	public static void main(String[] args) {
		new ColoredMandelbrot(400,400).setVisible(true);
	}

}
