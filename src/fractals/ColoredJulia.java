package fractals;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *Draws a Julia set with the rule f_c(z)=z^2+c, where c is a constant.
 */
public class ColoredJulia extends JFrame{
	BufferedImage img;
	final int SCALE=300;
	final int X_SHIFT=0;	//right shift
	final int Y_SHIFT=0;	//up shift
	final int MAX_ITER=1024;
	final float SAT=1f;
	final float BRIGHTNESS=0.8f;
	final float HUE_SHIFT=0.5f;
	final int COLOR_SCALE=200;	//change this to change the color scheme
	final Complex c=new Complex(0.285,0.01);
	
	public ColoredJulia(int w,int h){
		super("Julia Set");
		setBounds(0,0,w,h);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		img=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<getWidth();i++){
			for(int j=0;j<getHeight();j++){
				final Complex origin=new Complex(getWidth()/2,getHeight()/2);	//center of the window
				//System.out.println(i+" "+j);
				//Complex z=new Complex((i-origin.a-X_SHIFT)/SCALE,(origin.b-j-Y_SHIFT)/SCALE);	//i and j are the pixel coordinates, which wust be scaled down
				
				double zx=(i-origin.a-X_SHIFT)/SCALE;
				double zy=(origin.b-j-Y_SHIFT)/SCALE;
				
				int k=0;
				while(k<MAX_ITER && zx*zx+zy*zy<=4){
					//System.out.println(z.a+" "+z.b);
					double tempZX=zx*zx-zy*zy+c.a;
					zy=2*zx*zy+c.b;
					zx=tempZX;
					//z.square();
					//z.add(c);
					k++;
				}
				float brightness=k<MAX_ITER?BRIGHTNESS:0;
				float saturation=SAT;
				float hue=(k%256)/255.0f;
				if(hue+HUE_SHIFT>1)
					hue=hue-1;
				hue+=HUE_SHIFT;
				Color color=Color.getHSBColor(hue,saturation,brightness);
				//System.out.println(k);
				//img.setRGB(i,j,(MAX_ITER-k)*COLOR_SCALE);
				//img.setRGB(i, j, k<<16);
				img.setRGB(i,j,color.getRGB());
			}
		}
	}
	
	public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}
	
	public static void main(String[] args) {
		new ColoredJulia(1000,1000).setVisible(true);
	}

}
