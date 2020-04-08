package co.edu.eafit.dis.numerical.methods;

import android.content.Context;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.ResultsTable.Row;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class Secant {

  private FunctionsEvaluator functionEvaluator = null;
  private Context c = null;
  private ResultsTable results;

  public Secant(Context c) {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    results = ResultsTable.getInstance();
    ResultsTable.setUpNewTable(c, 7);
  }

  public Secant(Context c, String function, String dFunction) throws Exception {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    results = ResultsTable.getInstance();
    ResultsTable.setUpNewTable(c, 7);
    try {
      this.setFunction(function);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }

  /**
   * Retorna una arreglo de doubles, donde el primer valor es la ra?z y el
   * segundo valor indica si fue una aproximaci?n con una tolerancia o fue una
   * ra?z exacta, es decir, si el segundo valor es -1 quiere decir que se
   * encontr? la ra?z exactca, de lo contrario dicha posici?n contendr? el valor
   * de la tolerancia.
   */
  public double[] evaluate(double x0, double x1, double tol, double niter)
      throws Exception {
    ResultsTable.clearTable();
    ResultsTable.Row headers = results.new Row();
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_iteration));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_x0_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_fx0_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_x1_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_fx1_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_absolute_error));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_relative_error));
    ResultsTable.addRow(headers);
    double fx0 = functionEvaluator.calculate(x0);
    double fx1 = functionEvaluator.calculate(x1);
    ResultsTable.Row firstRow = results.new Row();
    firstRow.addCell("0");
    firstRow.addCell(String.valueOf(x0));
    firstRow.addCell(String.valueOf(fx0));
    firstRow.addCell(String.valueOf(x1));
    firstRow.addCell(String.valueOf(fx1));
    firstRow.addCell("-");
    firstRow.addCell("-");
    ResultsTable.addRow(firstRow);
    double[] result = new double[2];
    if (fx0 == 0) {
      result[0] = x0;
      result[1] = -1;
      return result; // x0 es ra?z
    } else {
      // double fx1 = functionEvaluator.calculate(x1);Ya se calcul? arriba
      int count = 0;
      double error = tol + 1.0;
      double relativeError = Math.abs(error / x1);
      double den = fx1 - fx0;
      while (error > tol && fx1 != 0 && den != 0 && count < niter) {
        double x2 = x1 - (fx1 * (x1 - x0) / den);
        error = Math.abs(x2 - x1);
        relativeError = Math.abs(error / x2);
        x0 = x1;
        fx0 = fx1;
        x1 = x2;
        fx1 = functionEvaluator.calculate(x1);
        den = fx1 - fx0;
        count += 1;
        ResultsTable.Row row = results.new Row();
        row.addCell(String.valueOf(count));
        row.addCell(String.valueOf(x0));
        row.addCell(String.valueOf(fx0));
        row.addCell(String.valueOf(x1));
        row.addCell(String.valueOf(fx1));
        row.addCell(String.valueOf(error));
        row.addCell(String.valueOf(relativeError));
        ResultsTable.addRow(row);
      }
      if (fx1 == 0) {
        result[0] = x1;
        result[1] = -1; // x1 es ra?z
        return result;
      } else if (error < tol) {
        result[0] = x1;
        result[1] = tol;// x1 es aproximaci?n con tol
        return result;
      } else {
        String methodName = c.getResources().getString(
            R.string.title_activity_secant);
        String errorMessage = c.getString(R.string.error_exceeded_iterations,
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
