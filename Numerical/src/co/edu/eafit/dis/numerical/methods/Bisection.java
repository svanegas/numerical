package co.edu.eafit.dis.numerical.methods;

import android.content.Context;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class Bisection {
	
	private FunctionsEvaluator functionEvaluator = null;
	private Context c = null;
	
	public Bisection (Context c){
		functionEvaluator = FunctionsEvaluator.getInstance(c);
		this.c = c;
	}
	
	public Bisection(Context c, String function) throws Exception {
		functionEvaluator = FunctionsEvaluator.getInstance(c);
		this.c = c;
		try {
			this.setFunction(function);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public double[] evaluate(double xi, double xs, double tol, double niter)
		throws Exception {
		double fxi = functionEvaluator.calculate(xi);
		double fxs = functionEvaluator.calculate(xs);
		double[] result = new double[2];
		if (fxi == 0) {
			result[0] = xi;
			result[1] = -1;
			return result;   //xi es raíz
		}
		else if (fxs == 0) {
			result[0] = xs;
			result[1] = -1;
			return result;   //xs es raíz
		} 
		else if (fxi * fxs < 0) {
			double xm = (xi + xs) / 2.0;
			double fxm = functionEvaluator.calculate(xm);
			int cont = 1;
			double error = tol + 1.0;
			
			while ((error > tol) && (fxm != 0) && (cont <= niter)) {
				if (fxi * fxm < 0) {
					xs = xm;
					fxs = fxm;					
				} 
				else {
					xi = xm;
					fxi = fxm;
				}
				
				double xaux = xm;
				xm = (xi + xs) / 2.0;
				fxm = functionEvaluator.calculate(xm);
				error = Math.abs(xm - xaux);
				cont++;				
			}
			if (fxm == 0) {
				result[0] = xm;
				result[1] = -1;
				return result;
			} 
			else if (error < tol){
				result[0] = xm;
				result[1] = tol;
				return result;
			} 
			else {
				String methodName = c.getResources()
						.getString(R.string.title_activity_bisection);
				String errorMessage = c.getString(
							R.string.error_exceeded_iterations,
							methodName); 
				throw new Exception(errorMessage);
			}
		} 
		else {
			throw new Exception(c.getResources()
					.getString(R.string.error_ivalid_interval));
		}
	}
	
	
	
	public void setFunction(String function) throws Exception {
		try {
			functionEvaluator.setFunction(function);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

}
