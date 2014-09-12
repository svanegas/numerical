package co.edu.eafit.dis.numerical.utils;

import android.util.Log;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

public class FunctionsEvaluator {
	
	private static final String TAG = FunctionsEvaluator.class.getName();
	private static FunctionsEvaluator instance = null;
	private static Calculable cal;
	
	protected FunctionsEvaluator () {
		
	}
	
	public static FunctionsEvaluator getInstance() {
		if (instance == null) instance = new FunctionsEvaluator();
		return instance;
	}
	
	public void setFunction(String function) throws Exception{
		try {
			cal = new ExpressionBuilder(function)
									.withVariableNames("x").build();
		} catch (Exception e) {
			Log.e(TAG, "Cannot instantiate Calculator with the function: "
					+ function);
			throw new Exception("The function " + function + " is invalid");
		}
	}
	
	public void setFunction(String function, double xValue) throws Exception {
		try {
			cal = new ExpressionBuilder(function)
									.withVariableNames("x")
									.withVariable("x", xValue).build();
		} catch (Exception e) {
			Log.e(TAG, "Cannot instantiate Calculator with the function: "
					+ function);
			throw new Exception("The function " + function + " is invalid");
		}
	}
	
	public void setXValue(double xValue) {
		cal.setVariable("x", xValue);
	}
	
	public double calculate(double xValue) {
		cal.setVariable("x", xValue);
		return cal.calculate();
	}
}
