package co.edu.eafit.dis.numerical.utils;

import co.edu.eafit.dis.numerical.R;
import android.content.Context;
import android.util.Log;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

/* Clase utilizada para interpretar y evaluar funciones.
 * est? implementada con Singleton, es decir, solo existir? una instancia en
 * la ejecuci?n del programa.
 */
public class FunctionsEvaluator {

  private static final String TAG = FunctionsEvaluator.class.getName();

  private static Context c;
  private static FunctionsEvaluator instance = null;
  private static Calculable cal;

  protected FunctionsEvaluator() {
  }

  public static FunctionsEvaluator getInstance(Context c) {
    if (instance == null)
      instance = new FunctionsEvaluator();
    instance.setContext(c);
    return instance;
  }

  public void setFunction(String function) throws Exception {
    try {
      cal = new ExpressionBuilder(function).withVariableNames("x").build();
    } catch (Exception e) {
      Log.e(TAG, "Cannot instantiate Calculator with the function: " + function);
      String message = c.getString(R.string.invalid_function_exception,
          function);
      throw new Exception(message);
    }
  }
  
  public String getFunction() {
    return cal.getExpression().toString();
  }

  public double calculate(double xValue) {
    cal.setVariable("x", xValue);
    return cal.calculate();
  }

  public void setContext(Context c) {
    this.c = c;
  }
  
  public String removeLineBreaks(String function) {
    String result;
    result = function.replace("\n", "").replace("\r", "");
    return result;
  }
}
