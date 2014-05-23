package ann;

import java.util.Arrays;

public class Test {
	public final static String PATH = "ANN-Intercepted1.txt";

	public static void main(String[] args) {
		
		Sigmoide s = new Sigmoide();
//		System.out.println(s.value(1.5));
		Network net = Network.load(PATH, s);
		
		double[] out = net.eval(new double[]{2,0,15,5,10,-5});
		System.out.println(Arrays.toString(out));
	}

}
