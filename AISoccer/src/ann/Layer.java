package ann;

import math.MathFunction;
import aisoccer.InvalidArgumentException;

public class Layer {
	Neuron[] neurons;
	
	public Layer(float[][] weights, MathFunction f){
		neurons = new Neuron[weights.length];
		for(int i=0; i<neurons.length; i++){
			neurons[i] = new Neuron(weights[i], f);
		}
	}
	
	public float[] eval(float[] input) {
		float[] out = new float[neurons.length];
		for(int i=0; i<neurons.length; i++){
			try {
				out[i] = neurons[i].eval(input);
			} catch (InvalidArgumentException e) {
				System.err.println("probleme de dimension au neurone "+i);
				e.printStackTrace();
			}
		}
		return out;
	}
	
}
