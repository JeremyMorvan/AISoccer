package ann;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import math.MathFunction;
import aisoccer.InvalidArgumentException;


public class Network {
	Layer[] layers;
	
	public Network(ArrayList<double[][]> layersW, MathFunction f) throws InvalidArgumentException {
		layers = new Layer[layersW.size()]; 
//		System.out.println("Creation d'un reseau à "+layersW.size()+" layers");
		for(int i=0; i<layersW.size(); i++){
//			System.out.println("Layer n°"+i+" : dim entrée : "+layersW.get(i)[0].length+" / dim sortie : "+layersW.get(i).length);
			if(i>0 && layersW.get(i-1).length+1 != layersW.get(i)[0].length){
				throw new InvalidArgumentException();				
			}
			layers[i] = new Layer(layersW.get(i), f);
		}
	}
	
	public double[] eval(double[] input) {
		double[] out = null;
		double[] in = expand(input);		
		for(int i=0; i<layers.length; i++){
			out = layers[i].eval(in);
//			System.out.println(Arrays.toString(out));
			in = expand(out);
		}
		return out;
	}
	
	public void setFunction(MathFunction f){
		for(Layer l : layers){
			l.setFunction(f);			
		}
	}
	
	private double[] expand(double[] v){
		double[] r = new double[v.length+1];
		for(int i=0; i<v.length; i++){
			r[i] = v[i];
		}
		r[r.length-1] = 1;
		return r;
	}
	
	
	public static Network load(String path, MathFunction f){
		ArrayList<double[][]> weights = parseWeights(path);
		try {
			return new Network(weights,f);
		} catch (InvalidArgumentException e) {e.printStackTrace();}
		return null;		
	}
	
	private static ArrayList<double[][]> parseWeights(String path) {
		ArrayList<double[][]> res = new ArrayList<double[][]>();
		
		double[][] matrix;
		double[] n;
		ArrayList<double[]> buffer = new ArrayList<double[]>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String line="";
			String[] neuron;
			while(line != null){
				line = in.readLine();
				if (line == null  || line.isEmpty()){
//					System.out.println("Oh une ligne vide !");
					if(!buffer.isEmpty()){
						matrix = new double[buffer.size()][buffer.get(0).length];
						for(int i=0; i<buffer.size();i++){
							matrix[i] = buffer.get(i);
						}
//						System.out.println("nb de neurones dans ce layer : "+matrix.length);
						buffer = new ArrayList<double[]>();
						res.add(matrix);
					}
				}else{
//					System.out.println("ligne = "+line);
					neuron = line.split(" ");
					n = new double[neuron.length];
					for(int i=0; i<neuron.length; i++){
						n[i] = Double.valueOf(neuron[i]);
					}
					buffer.add(n);
				}
			}			
			in.close();
		} 
		catch (FileNotFoundException e1) {e1.printStackTrace();}
		catch (Exception e1) {e1.printStackTrace();}
//		System.out.println("nb de layers : "+res.size());
		return res;
	}
}
