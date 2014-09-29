package co.edu.eafit.dis.numerical.methods;


import android.content.Context;
import android.util.Log;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class MultipleRoots {
  
  private static final String TAG = MultipleRoots.class.getSimpleName();
  
  private FunctionsEvaluator functionEvaluator = null;
  private Context c = null;
  private String function = null;
  private String derived1Function = null;
  private String derived2Function = null;
  
  public MultipleRoots (Context c){
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
  }
  
  public MultipleRoots(Context c, String function, String dFunction,
                       String d2Function) throws Exception {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    this.function = function;
    this.derived1Function = dFunction;
    this.derived2Function = d2Function;
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
  public double[] evaluate(double x0, double tol, double niter)
    throws Exception {
    functionEvaluator.setFunction(function);
    double fx = functionEvaluator.calculate(x0);
    functionEvaluator.setFunction(derived1Function);
    double d1fx = functionEvaluator.calculate(x0);
    functionEvaluator.setFunction(derived2Function);
    double d2fx = functionEvaluator.calculate(x0);
    double[] result = new double[2];
    if (fx == 0) {
      result[0] = x0;
      result[1] = -1;
      return result;   //x0 es raíz
    }
    int cont = 0;
    double error = tol + 1.0;
    double x1 = 0;
    while (fx != 0 && (d1fx != 0 || d2fx != 0) && error > tol && cont < niter) {
      x1 = x0 - ((fx * d1fx) / ((d1fx * d1fx) - (fx * d2fx)));
      functionEvaluator.setFunction(function);
      fx = functionEvaluator.calculate(x1);
      functionEvaluator.setFunction(derived1Function);
      d1fx = functionEvaluator.calculate(x1);
      functionEvaluator.setFunction(derived2Function);
      d2fx = functionEvaluator.calculate(x1);
      error = Math.abs(x1 - x0);
      x0 = x1;
      cont++;
    }
    if(fx == 0) {
      result[0] = x0;//x0 es raiz exacta
      result[1] = -1;//Indica que es exacta
      return result;
    }else if(error < tol) {
      result[0] = x1;  //x1 es aproximacion a raiz
      result[1] = tol; //Se indica el valor de la tolerancia
      return result;
    }
    else if(d1fx == 0 && d2fx == 0) {
      String errorMessage = c.getString(R.string
          .error_unsolvable_function, function); 
      throw new Exception(errorMessage);
    }else {
        String methodName = c.getResources()
                .getString(R.string.title_activity_multiple_roots);
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
  
  public void setFirstDerivate(String derived1Function) throws Exception {
    try {
      functionEvaluator.setFunction(function);
      this.derived1Function = derived1Function;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
  
  public void setSecondDerivate(String derived2Function) throws Exception {
    try {
      functionEvaluator.setFunction(derived2Function);
      this.derived2Function = derived2Function;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
}
