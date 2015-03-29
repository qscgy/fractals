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
	
	/*
	public Complex(String c){
		int commaPos=getCommaPos(c);
		
	}
	
	private static int getCommaPos(String str){
		return str.indexOf(',');
	}
	*/
	
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
	
	public String toString(){
		return "("+a+","+b+")";
	}
	
	public boolean equals(Object o){
		if(((o instanceof Complex))){
			return false;
		}
		if(((Complex)o).a==this.a && ((Complex)o).b==this.b){
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode(){
		return (int)(3*a*100000000+b*100000000);	//100000000=10^8
	}
}
