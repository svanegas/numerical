package co.edu.eafit.dis.numerical.utils;

import android.util.Log;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

public class FunctionsEvaluator {
	
	private static final String TAG = FunctionsEvaluator.class.getName();
	private static FunctionsEvaluator evaluator = null;
	private static Calculable cal;
	
	private FunctionsEvaluator () {
		evaluator = new FunctionsEvaluator();
	}
	
	public static FunctionsEvaluator getInstance() {
		if (evaluator == null) evaluator = new FunctionsEvaluator();
		return evaluator;
	}
	
	public static void setFunction(String function) {
		try {
			cal = new ExpressionBuilder(function)
									.withVariableNames("x").build();
		} catch (Exception e) {
			Log.e(TAG, "Cannot instantiate Calculator with the function: "
					+ function);
		}
	}
	
	public static void setFunction(String function, double xValue) {
		try {
			cal = new ExpressionBuilder(function)
									.withVariableNames("x")
									.withVariable("x", xValue).build();
		} catch (Exception e) {
			Log.e(TAG, "Cannot instantiate Calculator with the function: "
					+ function);
		}
	}
	
	public static void setXValue(double xValue) {
		cal.setVariable("x", xValue);
	}
	
	public static double calculate(double xValue) {
		cal.setVariable("x", xValue);
		return cal.calculate();
	}
}
