package fractals;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
/**
 * Draws the Mandelbrot set using the Escape Time Algorithm.
 * @author qscgy
 */

public class ColoredMandelbrot extends JFrame {
	BufferedImage img;
	final int SCALE=800;
	final int X_SHIFT=100;	//right shift
	final int Y_SHIFT=0;	//up shift
	final int MAX_ITER=256;
	final float SAT=1f;
	final float BRIGHTNESS=0.8f;
	final float HUE_SHIFT=0.5f;
	final int COLOR_SCALE=8192;	//change this to change the color scheme
	final int COLOR_SHIFT=18;	//bit shift
	public final static int QUADRATIC=0;
	public final static int CUBIC=1;
	public final static int BURNING_SHIP=2;
	public final static int PHOENIX=3;
	public static int MODE;
	
	public ColoredMandelbrot(int width,int height,int scale,int mode,int maxIter,int xshift,int yshift){
		super("Mandelbrot Set");
		setBounds(0,0,width,height);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		img=new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
		for(int i=0;i<getWidth();i++){
			for(int j=0;j<getHeight();j++){
				final Complex origin=new Complex(getWidth()/2,getHeight()/2);	//center of the window
				//Complex z=new Complex(0,0);
				double cx=(i-origin.a-xshift)/scale;
				double cy=(origin.b-j-yshift)/scale;
				double zx=0;
				double zy=0;
				//System.out.println(i+" "+j);
				//Complex c=new Complex((i-origin.a-X_SHIFT)/SCALE,(origin.b-j-Y_SHIFT)/SCALE);	//i and j are the pixel coordinates, which wust be scaled down
				int k=0;
				while(k<maxIter && zx*zx+zy*zy<=4){
					
					//System.out.println(z.a+" "+z.b);
					if(mode==QUADRATIC){
						double tempZX=zx*zx-zy*zy+cx;
						zy=2*zx*zy+cy;
						zx=tempZX;
						//z.square();
						//z.add(c);
					} else if(mode==CUBIC){
						double tempZX=Math.pow(zx, 3)-3*zx*zy*zy+cx;
						zy=3*zx*zx*zy-Math.pow(zy,3)+cy;
						zx=tempZX;
					} else if(mode==BURNING_SHIP){
						double tempZX=zx*zx-zy*zy-cx;
						zy=2*Math.abs(zx*zy)-cy;
						zx=tempZX;
					} else if(mode==PHOENIX){
						
					}
					//System.out.println(k);
					k++;
				}
				//System.out.println(zx*zx+zy*zy);
				//img.setRGB(i,j,(MAX_ITER-k)*COLOR_SCALE);
				//img.setRGB(i,j,k|(k<<COLOR_SHIFT));
				
				float brightness=k<maxIter?BRIGHTNESS:0;
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
	
	private static List<Object> getStartConditions(){	//prompts the user for the real and imaginary parts of c
		List<Object> values=new ArrayList<>();
		//JTextField re=new JTextField("0",15);
		//JTextField im=new JTextField("0",15);
		//JSlider sat=new JSlider(0,255,128);
		
		JComboBox<String> mode=new JComboBox<>(new String[]{"Quadratic","Cubic","Burning Ship"}); //the
		mode.setSelectedIndex(0);
		mode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				JComboBox modeLocal=(JComboBox)e.getSource();
				ColoredMandelbrot.MODE=(int)modeLocal.getSelectedIndex();
			}
		});
		
		JTextField xshift=new JTextField("0",5);
		JTextField yshift=new JTextField("0",5);
		
		/*
		JPanel cValues=new JPanel();
		
		cValues.add(new JLabel("Re(c): "));
		cValues.add(re);
		cValues.add(Box.createHorizontalStrut(15));
		cValues.add(new JLabel("Im(c): "));
		cValues.add(im);
		cValues.add(Box.createHorizontalStrut(20));
		cValues.add(new JLabel("Saturation: "));
		cValues.add(new JTextField(15));
		*/
		
		Object[] inputs={
				"Mode: ", mode,
				"X offset (right=+): ", xshift,
				"Y offset (down=+): ", yshift
		};
		
		int result=JOptionPane.showConfirmDialog(null,inputs,"Please enter conditions",JOptionPane.OK_CANCEL_OPTION);
		
		if(result==JOptionPane.OK_OPTION){
			try{
				//values.add(Double.parseDouble(re.getText()));
				//values.add(Double.parseDouble(im.getText()));
				values.add(Integer.parseInt(xshift.getText()));
				values.add(Integer.parseInt(yshift.getText()));
				//values.add(sat.getValue()/256.0f);
				return values;
			} catch(NumberFormatException e){	//user entered invalid numbers
				JOptionPane.showMessageDialog(null, "You need to enter valid numbers", "Try again", JOptionPane.ERROR_MESSAGE);
				return getStartConditions();
			}
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		List<Object> values=getStartConditions();
		if(values!=null){
			ColoredMandelbrot cm=new ColoredMandelbrot(800,800,240,MODE,256,(int)values.get(0),
					(int)values.get(1));
			cm.setVisible(true);
		}
	}

}
