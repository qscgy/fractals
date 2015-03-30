package fractals;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 *Draws a Julia set with the rule f_c(z)=z^k+c, where c and k are constants.
 */
public class ColoredJulia extends JFrame{
	BufferedImage img;
	
	public final static int SQUARE_MODE=1;
	public final static int CUBE_MODE=2;
	
	final float BRIGHTNESS=0.8f;
	final float HUE_SHIFT=0.5f;
	
	/**
	 * Constructs a new <code>ColoredJulia</code> object, which displays an image of a Julia set.
	 * @param w	the width of the image,in pixels
	 * @param h	the height of the image, in pixels
	 * @param c the complex value c in the polynomial
	 * @param sat the saturation of the image
	 * @param scale the zoom of the image
	 * @param mode what power z is taken to (use one of this class' constants)
	 * @param maxIter the maximum number of times to iterate the polynomial before breaking
	 * @param xshift the amount by which the set is shifted right
	 * @param yshift the amount by which the set is shifted up
	 */
	public ColoredJulia(int w,int h,Complex c, float sat,int scale,int mode,int maxIter,int xshift,int yshift){
		super("Julia Set");
		setBounds(0,0,w,h);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		drawJulia(c,sat,scale,mode,maxIter,xshift,yshift);
	}
	
	/*
	 * Does the actual drawing of the Julia set. Coloring is done with the Escape Time Algorithm.
	 */
	private void drawJulia(Complex c,float sat,int scale,int mode,int maxIter,int xshift,int yshift){
		final Complex origin=new Complex(getWidth()/2,getHeight()/2);	//center of the window
		img=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<getWidth();i++){
			//System.out.println(i);
			for(int j=0;j<getHeight();j++){
				//System.out.println(i+" "+j);
				//Complex z=new Complex((i-origin.a-X_SHIFT)/SCALE,(origin.b-j-Y_SHIFT)/SCALE);	//i and j are the pixel coordinates, which wust be scaled down
				
				double zx=(i-origin.a-xshift)/scale;
				double zy=(origin.b-j-xshift)/scale;
				
				int k=0;
				while(k<maxIter && zx*zx+zy*zy<=4){
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
				float brightness=k<maxIter?BRIGHTNESS:0;
				float saturation=sat;
				float hue=(k%maxIter)/((float)maxIter-1);
				if(hue+HUE_SHIFT>1)
					hue=hue-1;
				hue+=HUE_SHIFT;
				Color color=Color.getHSBColor(hue,saturation,brightness);
				img.setRGB(i,j,color.getRGB());
			}
		}
	}
	
	public void paint(Graphics g){
		g.drawImage(img,0,0,this);
	}
	
	private static Complex getCValue(){	//prompts the user for the real and imaginary parts of c
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
			ColoredJulia cj=new ColoredJulia(800,800,c,1.0f,240,ColoredJulia.SQUARE_MODE,256,0,50);
			cj.setVisible(true);
		} else {
			return;
		}
	}

}
