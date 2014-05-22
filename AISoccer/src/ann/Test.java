package ann;

import java.util.Arrays;

public class Test {
	public final static String PATH = "ANN-Intercepted.txt";

	public static void main(String[] args) {
		
		Sigmoide s = new Sigmoide();
//		System.out.println(s.value(1.5));
		Network net = Network.load(PATH, s);
		
		double[] out = net.eval(new double[]{1,0,0,0,0,0});
		System.out.println(Arrays.toString(out));
	}

}
