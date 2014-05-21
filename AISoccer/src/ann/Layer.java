package ann;

import aisoccer.InvalidArgumentException;

public class Layer {
	Neuron[] neurons;
	
	public float[] eval(float[] input) {
		float[] out = new float[neurons.length];
		for(int i=0; i<neurons.length; i++){
			try {
				out[i] = neurons[i].eval(input);
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
		}
		
		return out;
	}
	
}
