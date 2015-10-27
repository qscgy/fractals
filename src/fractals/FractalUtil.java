package fractals;

import javafx.scene.paint.Color;

public class FractalUtil {
	
	public static Color colorForTime(int k,int mode,int maxIter){
		switch(mode){
		case 1:
			double green=(Math.sin(k/9.0)+1)*0.5;
			//green=1.0/(1+20*Math.pow(Math.E, k));
			double red=(Math.atan(k/3.0)/Math.PI)+0.5;
			double blue=roundN(Math.pow(0.25, (double)k/maxIter)*4.0/3,5)-(1/3.0);
			return Color.color(red,green,blue);
		case 2:
			green=(Math.sin(k/9.0)+1)*0.5;
			red=(Math.cos(k/20.0)+1)*0.5;
			blue=(Math.atan(k/7.0)/Math.PI)+0.5;
			return Color.color(red,green,blue);
		default:
			double hue=240-(double)k/maxIter*360.0;
			double saturation=roundN(Math.pow(.5, (double)k/maxIter)*2,5)-1;
			//System.out.println(saturation);
			double brightness=k/(k+3.0);
			return Color.hsb(hue,saturation,brightness);
		}
	}
	
	
	public static double roundN(double num, int n){
		double powerOfTen=Math.pow(10,n);
		return Math.round(num*powerOfTen)/powerOfTen;
	}
}
