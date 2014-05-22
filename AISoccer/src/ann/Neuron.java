package ann;

import math.MathFunction;
import math.MathTools;
import aisoccer.InvalidArgumentException;

public class Neuron {
	double[] w;
	MathFunction activFunction;
	
	public Neuron(double[] weights, MathFunction f){
		w = weights;
		activFunction = f;
	}
	
	public double eval(double[] input) throws InvalidArgumentException{
		return activFunction.value(MathTools.sp(w, input));		
	}
}
