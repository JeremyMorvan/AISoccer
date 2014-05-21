package ann;

import java.util.ArrayList;

import math.MathFunction;
import aisoccer.InvalidArgumentException;


public class Network {
	Layer[] layers;
	
	public Network(ArrayList<float[][]> weights, MathFunction f) throws InvalidArgumentException{
		for(int i=0; i<weights.size(); i++){
			if(i>0){
				if (weights.get(i-1).length+1 != weights.get(i)[1].length){
					throw new InvalidArgumentException();
				}
				layers[i] = new Layer(weights.get(i), f);
			}
		}
	}
	
	public float[] eval(float[] input) {
		float[] out = null;
		float[] in = expand(input);		
		for(int i=0; i<layers.length; i++){
			out = layers[i].eval(in);
			in = expand(out);
		}
		return out;
	}
	
	public float[] expand(float[] v){
		float[] r = new float[v.length+1];
		for(int i=0; i<v.length; i++){
			r[i] = v[i];
		}
		r[r.length] = 1;
		return r;
	}
}
