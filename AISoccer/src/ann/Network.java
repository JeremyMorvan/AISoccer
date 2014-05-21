package ann;

import aisoccer.InvalidArgumentException;

public class Network {
	Layer[] layers;
	
	public float[] eval(float[] input) {
		float[] out = new float[layers.length+1];
		for(int i=0; i<layers.length; i++){
			//try {
				//out[i] = layers[i].eval(input);
			//} catch (InvalidArgumentException e) {
			//	e.printStackTrace();
			//}
		}
		return out;
	}
}
