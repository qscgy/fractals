package fractals;
/**
 * Represents a complex number in the form of (a+bi).
 *
 */
public class Complex {
	public double a,b;	//a is the real part; b is the imaginary part
	
	public Complex(double a,double b){
		this.a=a;
		this.b=b;
	}
	
	public void square(){	//(a+bi)^2
		double tempA=a*a-b*b;
		b=2*a*b;
		a=tempA;
	}
	
	public void add(Complex c){
		a+=c.a;
		b+=c.b;
	}
	
	public double length(){
		return Math.sqrt(a*a+b*b);
	}
}
