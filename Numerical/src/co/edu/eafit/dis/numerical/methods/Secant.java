package co.edu.eafit.dis.numerical.methods;


import android.content.Context;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class Secant {
  
  private FunctionsEvaluator functionEvaluator = null;
  private Context c = null;
  
  public Secant (Context c){
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
  }
  
  public Secant(Context c, String function, String dFunction) throws Exception {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    try {
      this.setFunction(function);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
  
  /**
   * Retorna una arreglo de doubles, donde el primer valor es la raíz
   * y el segundo valor indica si fue una aproximación con una tolerancia
   * o fue una raíz exacta, es decir, si el segundo valor es -1 quiere
   * decir que se encontró la raíz exactca, de lo contrario dicha posición
   * contendrá el valor de la tolerancia.
   */
  public double[] evaluate(double x0, double x1, double tol, double niter)
    throws Exception {
    double fx0 = functionEvaluator.calculate(x0);
    double[] result = new double[2];
    if (fx0 == 0) {
      result[0] = x0;
      result[1] = -1;
      return result;   //x0 es raíz
    }
    else {
    	double fx1 = functionEvaluator.calculate(x1);
		int count = 0;
	    double error = tol + 1.0;
	    double den = fx1 - fx0;
	    while (error > tol && fx1 != 0 && den != 0 && count < niter) {
	    	double x2 = x1 - (fx1 * (x1 - x0) / den);
	    	error = Math.abs(x2 - x1);
	    	x0 = x1;
	    	fx0 = fx1;
	    	x1 = x2;
	    	fx1 = functionEvaluator.calculate(x1);
	    	den = fx1 - fx0;
	    	count += 1;
	    }
	    if (fx1 == 0) {
	    	result[0] = x1;
	    	result[1] = -1; //x1 es raíz
	    	return result;
	    }
	    else if (error < tol) {
	    	result[0] = x1;
	    	result[1] = tol;//x1 es aproximación con tol
	    	return result;
	    }
	    else {
	    	String methodName = c.getResources()
	                .getString(R.string.title_activity_secant);
	        String errorMessage = c.getString(
	                  R.string.error_exceeded_iterations,
	                  methodName); 
	        throw new Exception(errorMessage);
	    }
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
