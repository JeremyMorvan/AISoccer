package ann;

import math.MathFunction;
import aisoccer.InvalidArgumentException;

public class Layer {
	Neuron[] neurons;
	
	public Layer(double[][] weights, MathFunction f){
		neurons = new Neuron[weights.length];
		for(int i=0; i<neurons.length; i++){
			neurons[i] = new Neuron(weights[i], f);
		}
	}
	
	public double[] eval(double[] input) {
		double[] out = new double[neurons.length];
		for(int i=0; i<neurons.length; i++){
			try {
				out[i] = neurons[i].eval(input);
			} catch (InvalidArgumentException e) {
				System.err.println("probleme de dimension au neurone "+(i+1));
				e.printStackTrace();
			}
		}
		return out;
	}
	
}
