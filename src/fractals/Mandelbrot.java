package fractals;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.layout.*;

public class Mandelbrot extends Application {
	int scale=200;	//zoom
	int xShift=0;	//right shift
	int yShift=0;	//up shift
	String mode="Quadratic";
	int coloringNum=0;
	final int MAX_ITER=512;
	final double SAT=1.0;
	final double BRIGHTNESS=0.8;
	final double HUE_SHIFT=0.5;
	
	@Override
	public void start(Stage primaryStage) {
		ArrayList<String> values=getInputs();
		if(values!=null){
			scale=Integer.parseInt(values.get(0));
			xShift=Integer.parseInt(values.get(1));
			yShift=Integer.parseInt(values.get(2));
			mode=values.get(3);
			//System.out.println(values.get(4));
			switch(values.get(4)){
			case "HSB 1":
				coloringNum=0;
				break;
			case "Trig-Exp":
				coloringNum=1;
				break;
			case "Trig":
				coloringNum=2;
				break;
			}
		} else {
			return;
		}
		System.out.println(coloringNum);
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
				double cx=(i-origin.a+xShift)/scale;
				double cy=(origin.b-j-yShift)/scale;
				double zx=0;	//Re(z)
				double zy=0;	//Im(z)
				int k=0;
				while(k<MAX_ITER && zx*zx+zy*zy<=4){
					if(mode.equals("Quadratic")){
						double tempZX=zx*zx-zy*zy+cx;
						zy=2*zx*zy+cy;
						zx=tempZX;
					} else if(mode.equals("Cubic")){
						double tempZX=Math.pow(zx, 3)-3*zx*zy*zy+cx;
						zy=3*zx*zx*zy-Math.pow(zy,3)+cy;
						zx=tempZX;
					} else if(mode.equals("Burning Ship")){
						double tempZX=zx*zx-zy*zy-cx;
						zy=2*Math.abs(zx*zy)-cy;
						zx=tempZX;
					}
					k++;
				}
				Color c;
				
				if(k>=MAX_ITER)
						c=Color.BLACK;
				else
					c=FractalUtil.colorForTime(k,coloringNum,MAX_ITER);
				
				pw.setColor(i, j, c);
			}
		}
	}
	
	private ArrayList<String> getInputs(){
		Dialog<ArrayList<String>> inputs=new Dialog<>();
		inputs.setTitle("Enter conditions");
		inputs.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
		
		GridPane grid=new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		TextField scaleField=new TextField();
		scaleField.setText(String.valueOf(scale));
		TextField xShiftField=new TextField();
		xShiftField.setText("0");
		TextField yShiftField=new TextField();
		yShiftField.setText("0");
		ComboBox<String> modeSelect=new ComboBox<>();
		modeSelect.getItems().addAll("Quadratic","Cubic","Burning Ship");
		modeSelect.setValue("Quadratic");
		ComboBox<String> coloring=new ComboBox();
		coloring.getItems().addAll("HSB 1","Trig-Exp","Trig");
		coloring.setValue("HSB 1");
		
		grid.add(new Label("Scale: "),0,0);
		grid.add(scaleField,1,0);
		grid.add(new Label("Right Shift: "), 0, 1);
		grid.add(xShiftField,1,1);
		grid.add(new Label("Up Shift: "),0,2);
		grid.add(yShiftField,1,2);
		grid.add(new Label("Mode: "), 0, 3);
		grid.add(modeSelect, 1, 3);
		grid.add(new Label("Coloring: "), 0, 4);
		grid.add(coloring, 1, 4);

		inputs.getDialogPane().setContent(grid);
		
		inputs.setResultConverter(btn->{
			if(btn==ButtonType.OK){
				ArrayList<String> list=new ArrayList<>();
				list.add(scaleField.getText());
				list.add(xShiftField.getText());
				list.add(yShiftField.getText());
				list.add(modeSelect.getValue());
				list.add(coloring.getValue());
				return list;
			}
			return null;
		});
		
		Optional<ArrayList<String>> result=inputs.showAndWait();
		if(result.isPresent()){
			return result.get();
		}
		return null;
	}
	
	private static double roundN(double num, int n){
		double powerOfTen=Math.pow(10,n);
		return Math.round(num*powerOfTen)/powerOfTen;
	}
	
	public static void main(String[] args){
		launch(args);
	}
	
}
