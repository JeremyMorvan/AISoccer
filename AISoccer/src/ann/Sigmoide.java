package ann;

import math.MathFunction;

public class Sigmoide extends MathFunction {

	public double value(double input) {
		return 2.0/(1+Math.exp(-input)) -1;
	}

}
