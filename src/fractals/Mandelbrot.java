package fractals;

import java.text.DecimalFormat;

import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;

public class Mandelbrot extends Application {
	final int SCALE=700;	//zoom
	final int X_SHIFT=-300;	//right shift
	final int Y_SHIFT=0;	//up shift
	final int MAX_ITER=512;
	final double SAT=1.0;
	final double BRIGHTNESS=0.8;
	final double HUE_SHIFT=0.5;
	Color[] palette;
	
	@Override
	public void start(Stage primaryStage) {
		
		
		
		palette=generatePalette();
		WritableImage img=new WritableImage(800,800);
		generateMandelbrot(img);
		ImageView iv=new ImageView();
		iv.setImage(img);
		StackPane root=new StackPane();
		root.getChildren().add(iv);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Mandelbrot Set");
		primaryStage.show();
	}
	
	private void generateMandelbrot(WritableImage img){
		PixelWriter pw=img.getPixelWriter();
		final Complex origin=new Complex(img.getWidth()/2, img.getHeight()/2);
		for(int i=0;i<img.getWidth();i++){
			for(int j=0;j<img.getHeight();j++){
				double cx=(i-origin.a+X_SHIFT)/SCALE;
				double cy=(origin.b-j-Y_SHIFT)/SCALE;
				double zx=0;	//Re(z)
				double zy=0;	//Im(z)
				int k=0;
				while(k<MAX_ITER && zx*zx+zy*zy<=4){
					double tempZX=zx*zx-zy*zy+cx;
					zy=2*zx*zy+cy;
					zx=tempZX;
					k++;
				}
				Color c;
				
				if(k>=MAX_ITER)
						c=Color.BLACK;
				else
					c=palette[k-1];
				
				pw.setColor(i, j, c);
			}
		}
	}
	
	private Color[] generatePalette(){
		Color[] palette=new Color[MAX_ITER];
		for(int i=0;i<MAX_ITER;i++){
			double hue=240-(double)i/MAX_ITER*360.0;
			double saturation=roundN(Math.pow(.5, (double)i/MAX_ITER)*2,5)-1;
			//System.out.println(saturation);
			double brightness=i/(i+3.0);
			palette[i]=Color.hsb(hue,saturation,brightness);
		}
		return palette;
	}
	
	private double roundN(double num, int n){
		double powerOfTen=Math.pow(10,n);
		return Math.round(num*powerOfTen)/powerOfTen;
	}
	
	public static void main(String[] args){
		launch(args);
	}
	
}
