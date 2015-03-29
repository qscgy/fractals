package fractals;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *Draws a Julia set with the rule f_c(z)=z^2+c, where c is a constant.
 */
public class ColoredJulia extends JFrame{
	BufferedImage img;
	
	public final static int SQUARE_MODE=1;
	public final static int CUBE_MODE=2;
	
	//final int SCALE=300;
	int X_SHIFT=0;	//right shift
	int Y_SHIFT=0;	//up shift
	final int MAX_ITER=1024;
	//float SAT=1f;
	final float BRIGHTNESS=0.8f;
	final float HUE_SHIFT=0.5f;
	final int COLOR_SCALE=200;	//change this to change the color scheme
	//final Complex c=new Complex(-0.707,0.0);
	
	public ColoredJulia(int w,int h,Complex c, float sat,int scale,int mode){
		super("Julia Set");
		setBounds(0,0,w,h);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		final int SCALE=scale;
		final Complex origin=new Complex(getWidth()/2,getHeight()/2);	//center of the window
		img=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<getWidth();i++){
			for(int j=0;j<getHeight();j++){
				//System.out.println(i+" "+j);
				//Complex z=new Complex((i-origin.a-X_SHIFT)/SCALE,(origin.b-j-Y_SHIFT)/SCALE);	//i and j are the pixel coordinates, which wust be scaled down
				
				double zx=(i-origin.a-X_SHIFT)/SCALE;
				double zy=(origin.b-j-Y_SHIFT)/SCALE;
				
				int k=0;
				while(k<MAX_ITER && zx*zx+zy*zy<=4){
					//System.out.println(z.a+" "+z.b);
					if(mode==SQUARE_MODE){
						double tempZX=zx*zx-zy*zy+c.a;
						zy=2*zx*zy+c.b;
						zx=tempZX;
					} else if(mode==CUBE_MODE){
						double tempZX=Math.pow(zx, 3)-3*zx*zy*zy+c.a;
						zy=3*zx*zx*zy-Math.pow(zy,3)+c.b;
						zx=tempZX;
						//System.out.println(k);
					}
					k++;
				}
				
				//coloring algorithm based on number of iterations it took for loop to break
				float brightness=k<MAX_ITER?BRIGHTNESS:0;
				float saturation=sat;
				float hue=(k%MAX_ITER)/((float)MAX_ITER-1);
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
	
	public static Complex getCValue(){	//prompts the user for the real and imaginary parts of c
		JTextField re=new JTextField(15);
		JTextField im=new JTextField(15);
		//JSlider sat=new JSlider(0,255,128);
				
		JPanel cValues=new JPanel();
		
		cValues.add(new JLabel("Re(c): "));
		cValues.add(re);
		cValues.add(Box.createHorizontalStrut(15));
		cValues.add(new JLabel("Im(c): "));
		cValues.add(im);
		//cValues.add(Box.createHorizontalStrut(15));
		//cValues.add(sat);
		
		int result=JOptionPane.showConfirmDialog(null,cValues,"Please enter c",JOptionPane.OK_CANCEL_OPTION);
		
		if(result==JOptionPane.OK_OPTION){
			try{
				return new Complex(Double.parseDouble(re.getText()),Double.parseDouble(im.getText()));
			} catch(NumberFormatException e){	//user entered invalid numbers
				
				JOptionPane.showMessageDialog(null, "You need to enter valid numbers", "You done goofed", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		Complex c=getCValue();
		if(c!=null){
			ColoredJulia cj=new ColoredJulia(1000,1000,c,1.0f,300,ColoredJulia.CUBE_MODE);
			cj.setVisible(true);
		} else {
			return;
		}
	}

}
