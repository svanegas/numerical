package co.edu.eafit.dis.numerical.methods;

import android.content.Context;
import android.util.Log;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.ResultsTable.Row;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class FixedPoint {

  private FunctionsEvaluator functionEvaluator = null;
  private FunctionsEvaluator gFunctionEvaluator = null;
  private Context c = null;
  private String function = null;
  private String gFunction = null;
  private ResultsTable results;

  public FixedPoint(Context c) {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    gFunctionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    results = ResultsTable.getInstance();
    ResultsTable.setUpNewTable(c, 5);
  }

  public FixedPoint(Context c, String function, String gFunction)
      throws Exception {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    gFunctionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    this.function = function;
    this.gFunction = gFunction;
    results = ResultsTable.getInstance();
    ResultsTable.setUpNewTable(c, 5);
    try {
      this.setFunction(function);
      this.setGfunction(gFunction);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Retorna una arreglo de doubles, donde el primer valor es la raíz y el
   * segundo valor indica si fue una aproximación con una tolerancia o fue una
   * raíz exacta, es decir, si el segundo valor es -1 quiere decir que se
   * encontró la raíz exactca, de lo contrario dicha posición contendrá el valor
   * de la tolerancia.
   */
  public double[] evaluate(double xa, double tol, double niter)
      throws Exception {
    ResultsTable.clearTable();
    ResultsTable.Row headers = results.new Row();
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_iteration));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_xn_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_fxn_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_absolute_error));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_relative_error));
    ResultsTable.addRow(headers);
    functionEvaluator.setFunction(function);
    double fx = functionEvaluator.calculate(xa);
    ResultsTable.Row firstRow = results.new Row();
    firstRow.addCell("0");
    firstRow.addCell(String.valueOf(xa));
    firstRow.addCell(String.valueOf(fx));
    firstRow.addCell("-");
    firstRow.addCell("-");
    ResultsTable.addRow(firstRow);
    double[] result = new double[2];
    if (fx == 0) {
      result[0] = xa;
      result[1] = -1;
      return result; // xi es raíz
    }
    int cont = 0;
    double error = tol + 1.0;
    double relativeError = Math.abs(error / xa);
    while ((fx != 0) && (error > tol) && (cont < niter)) {
      gFunctionEvaluator.setFunction(gFunction);
      double xn = gFunctionEvaluator.calculate(xa);
      functionEvaluator.setFunction(function);
      fx = functionEvaluator.calculate(xn);
      error = Math.abs(xn - xa);
      relativeError = Math.abs(error / xn);
      xa = xn;
      cont++;
      ResultsTable.Row row = results.new Row();
      row.addCell(String.valueOf(cont));
      row.addCell(String.valueOf(xa));
      row.addCell(String.valueOf(fx));
      row.addCell(String.valueOf(error));
      row.addCell(String.valueOf(relativeError));
      ResultsTable.addRow(row);
    }
    if (fx == 0) {
      result[0] = xa;// xa es raiz exacta
      result[1] = -1;// Indica que es exacta
      return result;
    } else if (error < tol) {
      result[0] = xa;// xa es aproximacion a raiz
      result[1] = tol; // Se indica el valor de la tolerancia
      return result;
    } else {
      String methodName = c.getResources().getString(
          R.string.title_activity_fixed_point);
      String errorMessage = c.getString(R.string.error_exceeded_iterations,
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

  public void setGfunction(String function) throws Exception {
    try {
      gFunctionEvaluator.setFunction(function);
      this.gFunction = function;
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

}
