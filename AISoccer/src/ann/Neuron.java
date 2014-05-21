package ann;

import math.MathFunction;
import math.MathTools;
import aisoccer.InvalidArgumentException;

public class Neuron {
	float[] w;
	MathFunction activFunction;
	
	public float eval(float[] input) throws InvalidArgumentException{
		return (Float) activFunction.value(MathTools.sp(w, input));		
	}
}
