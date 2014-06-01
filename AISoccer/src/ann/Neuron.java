package ann;

import math.MathFunction;
import math.MathTools;
import aisoccer.InvalidArgumentException;

public class Neuron {
	private double[] w;
	private MathFunction activFunction;
	
	public Neuron(double[] weights, MathFunction f){
		w = weights;
		activFunction = f;
	}
	
	public double eval(double[] input) throws InvalidArgumentException{
		return activFunction.value(MathTools.sp(w, input));		
	}
	
	public void setFunction(MathFunction f){
		this.activFunction = f;
	}
	
	public double[] getWeights(){
		return w;
	}
}
