package co.edu.eafit.dis.numerical.methods;


import android.content.Context;
import android.util.Log;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class Newton {
  
  private FunctionsEvaluator functionEvaluator = null;
  private FunctionsEvaluator derivedFunctionEvaluator = null;
  private Context c = null;
  private String function = null;
  private String derivedFunction = null;
  
  public Newton (Context c){
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    derivedFunctionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
  }
  
  public Newton(Context c, String function, String dFunction) throws Exception {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    derivedFunctionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    this.function = function;
    derivedFunction = dFunction;
    try {
      this.setFunction(function);
      this.setDerivedFunction(dFunction);
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
  public double[] evaluate(double x0, double tol, double niter)
    throws Exception {
	functionEvaluator.setFunction(function);
    double fx = functionEvaluator.calculate(x0);
    derivedFunctionEvaluator.setFunction(derivedFunction);
    double dfx = derivedFunctionEvaluator.calculate(x0);
    Log.i("Function", String.valueOf(fx));
    Log.i("DerivedFunction", String.valueOf(dfx));
    double[] result = new double[2];
    double x1=0;
    if (fx == 0) {
      result[0] = x0;
      result[1] = -1;
      return result;   //x0 es raíz
    }
    int cont=0;
    double error = tol + 1.0;
    while((fx!=0)&&(dfx!=0)&&(error>tol)&&(cont<niter)){
    	x1 = (x0 - (fx/dfx));
    	functionEvaluator.setFunction(function);
    	fx = functionEvaluator.calculate(x1);
    	derivedFunctionEvaluator.setFunction(derivedFunction);
    	dfx = derivedFunctionEvaluator.calculate(x1);
    	error = Math.abs(x1 - x0);
    	x0 = x1;
    	cont++;
    }
    if(fx == 0){
    	result[0] = x0;//x0 es raiz exacta
    	result[1] = -1;//Indica que es exacta
    	return result;
    }else if(error < tol){
    	result[0] = x1;//x1 es aproximacion a raiz
    	result[1] = tol; //Se indica el valor de la tolerancia
    	return result;
    }
    else if(dfx == 0){
    	result[0] = x1;//x1 es una posible raiz
    	result[1] = tol; //Se indica el valor de la tolerancia
    	return result;
    }else {
        String methodName = c.getResources()
                .getString(R.string.title_activity_newton);
        String errorMessage = c.getString(
                  R.string.error_exceeded_iterations,
                  methodName); 
        throw new Exception(errorMessage);
    }
  }
  
  
  
  public void setFunction(String function) throws Exception {
    try {
      functionEvaluator.setFunction(function);
      this.function = function;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
  
  public void setDerivedFunction(String function) throws Exception {
	    try {
	      derivedFunctionEvaluator.setFunction(function);
	      derivedFunction = function;
	    } catch (Exception e) {
	      throw new Exception(e.getMessage());
	    }
  }

}
