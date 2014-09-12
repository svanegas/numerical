package co.edu.eafit.dis.numerical.methods;

import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class IncrementalSearch {

	private FunctionsEvaluator functionEvaluator = null;
	
	public IncrementalSearch() {
		functionEvaluator = FunctionsEvaluator.getInstance();
	}
	
	public IncrementalSearch(String function) throws Exception {
		functionEvaluator = FunctionsEvaluator.getInstance();
		try {
			this.setFunction(function);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public double [] evaluate(double x0, double delta, double nIter) 
			throws Exception {
		double[] interval = new double[2];
		double fx0 = functionEvaluator.calculate(x0);
		if (fx0 == 0.0) {
			interval[0] = interval[1] = x0;
		}
		else {
			double x1 = x0 + delta;
			int currentIteration = 1;
			double fx1 = functionEvaluator.calculate(x1);
			while (fx0 * fx1 > 0.0 && currentIteration <= nIter) {
				x0 = x1;
				fx0 = fx1;
				x1 = x0 + delta;
				fx1 = functionEvaluator.calculate(x1);
				currentIteration++;
			}
			if (fx1 == 0.0) interval[0] = interval[1] = x1;
			else if (fx0 * fx1 < 0) {
				interval[0] = x0;
				interval[1] = x1;
			}
			else {
				throw new Exception("Incremental search exceeded number " +
						"of iterations");
			}
		}
		return interval;
	}
	
	public void setFunction(String function) throws Exception {
		try {
			functionEvaluator.setFunction(function);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
}
