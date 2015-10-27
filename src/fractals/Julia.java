package fractals;

import java.util.ArrayList;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Julia extends Application {

	int scale=240;	//zoom
	int xShift=0;	//right shift
	int yShift=0;	//up shift
	String eqn="z^2+c";
	Complex c=new Complex(0,0);
	int coloringNum=0;
	final int MAX_ITER=512;
	
	@Override
	public void start(Stage primaryStage){
		getInputs();
		WritableImage img=new WritableImage(800,800);
		generateJulia(img);
		ImageView iv=new ImageView();
		iv.setImage(img);
		StackPane root=new StackPane();
		root.getChildren().add(iv);
		
		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Julia Set");
		primaryStage.show();
	}
	
	private void generateJulia(WritableImage img){
		PixelWriter pw=img.getPixelWriter();
		final Complex origin=new Complex(img.getWidth()/2,img.getHeight()/2);	//center of the window
		for(int i=0;i<img.getWidth();i++){
			//System.out.println(i);
			for(int j=0;j<img.getHeight();j++){
				//System.out.println(i+" "+j);
				//Complex z=new Complex((i-origin.a-X_SHIFT)/SCALE,(origin.b-j-Y_SHIFT)/SCALE);	//i and j are the pixel coordinates, which wust be scaled down
				
				double zx=(i-origin.a-xShift)/scale;	//offset zx and zy so that the center of the window corresponds to (0,0)
				double zy=(origin.b-j-yShift)/scale;
				double lastZX=0;
				double lastZY=0;
				
				int k=0;	//iteration count
				while(k<MAX_ITER && zx*zx+zy*zy<=4){
					//System.out.println(z.a+" "+z.b);
					if(eqn.equals("z^2+c")){
						//z=z^2+c
						double tempZX=zx*zx-zy*zy+c.a;
						zy=2*zx*zy+c.b;
						zx=tempZX;
					} else if(eqn.equals("z^3+c")){
						//z=z^3+c
						double tempZX=Math.pow(zx, 3)-3*zx*zy*zy+c.a;
						zy=3*zx*zx*zy-Math.pow(zy,3)+c.b;
						zx=tempZX;
					} else if(eqn.equals("exp(z)+c")){
						//z=exp(z)+c
						double tempZX=Math.exp(zx)*Math.cos(zy)+c.a;
						zy=Math.exp(zx)*Math.sin(zy)+c.b;
						zx=tempZX;
					} else if(eqn.equals("Phoenix")){
						double tempZX=zx*zx-zy*zy+c.a*lastZX;
						zy=2*zx*zy+c.b*lastZY;
						zx=tempZX;
						lastZX=zx;
						lastZY=zy;
					}
					k++;
				}
				
				Color color;
				
				if(k>=MAX_ITER)
						color=Color.BLACK;
				else
					color=FractalUtil.colorForTime(k,coloringNum,MAX_ITER);
				
				pw.setColor(i, j, color);
			}
		}
	}
	
	private void getInputs(){
		Dialog<ArrayList<String>> inputs=new Dialog<>();
		inputs.setTitle("Enter conditions");
		inputs.getDialogPane().getButtonTypes().addAll(ButtonType.OK,ButtonType.CANCEL);
		
		GridPane grid=new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		TextField cxField=new TextField();
		cxField.setText("0");
		TextField cyField=new TextField();
		cyField.setText("0");
		TextField scaleField=new TextField();
		scaleField.setText(String.valueOf(scale));
		TextField xShiftField=new TextField();
		xShiftField.setText("0");
		TextField yShiftField=new TextField();
		yShiftField.setText("0");
		ComboBox<String> modeSelect=new ComboBox<>();
		modeSelect.getItems().addAll("z^2+c","z^3+c","exp(z)+c");
		modeSelect.setValue("z^2+c");
		ComboBox<String> coloring=new ComboBox();
		coloring.getItems().addAll("HSB 1","Trig-Exp","Trig");
		coloring.setValue("HSB 1");
		
		grid.add(new Label("Re(c): "),0,0);
		grid.add(cxField,1,0);
		grid.add(new Label("Im(c): "),0,1);
		grid.add(cyField,1,1);
		grid.add(new Label("Scale: "),0,2);
		grid.add(scaleField,1,2);
		grid.add(new Label("Right Shift: "), 0, 3);
		grid.add(xShiftField,1,2);
		grid.add(new Label("Up Shift: "),0,4);
		grid.add(yShiftField,1,4);
		grid.add(new Label("Mode: "), 0, 5);
		grid.add(modeSelect, 1, 5);
		grid.add(new Label("Coloring: "), 0, 6);
		grid.add(coloring, 1, 6);

		inputs.getDialogPane().setContent(grid);
		
		inputs.setResultConverter(btn->{
			if(btn==ButtonType.OK){
				ArrayList<String> list=new ArrayList<>();
				list.add(cxField.getText());
				list.add(cyField.getText());
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
			ArrayList<String> values=result.get();
			c=new Complex(Double.parseDouble(values.get(0)),Double.parseDouble(values.get(1)));
			scale=Integer.parseInt(values.get(2));
			xShift=Integer.parseInt(values.get(3));
			yShift=Integer.parseInt(values.get(4));
			eqn=values.get(5);
			//System.out.println(values.get(6));
			switch(values.get(6)){
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
		}
	}
	
	public static void main(String[] args){
		launch(args);
	}

}
