package fractals;

import javax.swing.*;
import java.awt.*;

public class Mandelbrot extends JPanel {
	final int SCALE=150;
	final int SHIFT=30;	//right shift
	final int MAX_ITER=1000;
	public Mandelbrot(){
		super();
		setBackground(Color.white);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.black);
		for(int i=0;i<getWidth();i++){
			for(int j=0;j<getHeight();j++){
				final Complex origin=new Complex(getWidth()/2,getHeight()/2);	//center of the window
				Complex z=new Complex(0,0);
				//System.out.println(i+" "+j);
				Complex c=new Complex((i-origin.a-SHIFT)/SCALE,(origin.b-j)/SCALE);	//i and j are the pixel coordinates, which wust be scaled down
				int k=0;
				while(k<MAX_ITER && z.length()<=2){
				//	System.out.println(z.a+" "+z.b);
					z.square();
					z.add(c);
				}
				if(z.length()<=2){
					g.drawLine((int)i+SHIFT,(int)j,(int)i+SHIFT,(int)j);
				}
			}
		}
	}
	
	public static void main(String[] args){
		JFrame app=new JFrame();
		Mandelbrot m=new Mandelbrot();
		app.setSize(500,500);
		app.add(m);
		app.setVisible(true);
	}
}
