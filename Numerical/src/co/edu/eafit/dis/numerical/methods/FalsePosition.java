package co.edu.eafit.dis.numerical.methods;

import android.content.Context;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class FalsePosition {
  
  private FunctionsEvaluator functionEvaluator = null;
  private Context c = null;
  
  public FalsePosition (Context c){
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
  }
  
  public FalsePosition(Context c, String function) throws Exception {
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
      double xm = xi - ((fxi * (xs - xi)) / (fxs - fxi));
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
        xm = xi - ((fxi * (xs - xi)) / (fxs - fxi));
        fxm = functionEvaluator.calculate(xm);
        error = Math.abs(xm - xaux);
        cont++;       
      }
      if (fxm == 0) {
        result[0] = xm; //xm es raíz exacta
        result[1] = -1; //Se indica que es exacta
        return result;
      } 
      else if (error < tol){
        result[0] = xm;  //xm es aproximación a raíx
        result[1] = tol; //Se indica el valor de la tolerancia
        return result;
      } 
      else {
        String methodName = c.getResources()
            .getString(R.string.title_activity_false_position);
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
