package co.edu.eafit.dis.numerical.methods;

import android.content.Context;
import android.util.Log;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.ResultsTable.Row;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class Bisection {

  private static final String TAG = Bisection.class.getSimpleName();
  private FunctionsEvaluator functionEvaluator = null;
  private Context c = null;
  private ResultsTable results;

  public Bisection(Context c) {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    results = ResultsTable.getInstance();
    ResultsTable.setUpNewTable(c, 9);
  }

  public Bisection(Context c, String function) throws Exception {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    results = ResultsTable.getInstance();
    ResultsTable.setUpNewTable(c, 9);
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
  public double[] evaluate(double xi, double xs, double tol, double niter)
      throws Exception {
    // Se inicializa la tabla de resultados
    ResultsTable.clearTable();
    ResultsTable.Row headers = results.new Row();
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_iteration));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_xi_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_fxi_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_xs_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_fxs_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_xm_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_fxm_value));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_absolute_error));
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_relative_error));
    ResultsTable.addRow(headers);
    double fxi = functionEvaluator.calculate(xi);
    double fxs = functionEvaluator.calculate(xs);
    ResultsTable.Row firstRow = results.new Row();
    firstRow.addCell("1");
    firstRow.addCell(String.valueOf(xi));
    firstRow.addCell(String.valueOf(fxi));
    firstRow.addCell(String.valueOf(xs));
    firstRow.addCell(String.valueOf(fxs));
    firstRow.addCell(String.valueOf((xi + xs) / 2.0));
    firstRow.addCell("-");
    firstRow.addCell("-");
    double[] result = new double[2];
    if (fxi == 0) {
      ResultsTable.addRow(firstRow);
      result[0] = xi;
      result[1] = -1;
      return result; // xi es ra?z
    } else if (fxs == 0) {
      ResultsTable.addRow(firstRow);
      result[0] = xs;
      result[1] = -1;
      return result; // xs es ra?z
    } else if (fxi * fxs < 0) {
      double xm = (xi + xs) / 2.0;
      double fxm = functionEvaluator.calculate(xm);
      int cont = 1;
      double error = tol + 1.0;
      double relativeError = Math.abs(error / xm);
      while ((error > tol) && (fxm != 0) && (cont <= niter)) {
        ResultsTable.Row row = results.new Row();
        row.addCell(String.valueOf(cont));
        row.addCell(String.valueOf(xi));
        row.addCell(String.valueOf(fxi));
        row.addCell(String.valueOf(xs));
        row.addCell(String.valueOf(fxs));
        row.addCell(String.valueOf(xm));
        row.addCell(String.valueOf(fxm));
        if (cont == 1) {
          row.addCell("-");
          row.addCell("-");
        } else {
          row.addCell(String.valueOf(error));
          row.addCell(String.valueOf(relativeError));
        }
        ResultsTable.addRow(row);
        if (fxi * fxm < 0) {
          xs = xm;
          fxs = fxm;
        } else {
          xi = xm;
          fxi = fxm;
        }

        double xaux = xm;
        xm = (xi + xs) / 2.0;
        fxm = functionEvaluator.calculate(xm);
        error = Math.abs(xm - xaux);
        relativeError = Math.abs(error / xm);
        cont++;
      }
      if (fxm == 0) {
        result[0] = xm; // xm es ra?z exacta
        result[1] = -1; // Se indica que es exacta
        ResultsTable.Row row = results.new Row();
        row.addCell(String.valueOf(cont));
        row.addCell(String.valueOf(xi));
        row.addCell(String.valueOf(fxi));
        row.addCell(String.valueOf(xs));
        row.addCell(String.valueOf(fxs));
        row.addCell(String.valueOf(xm));
        row.addCell(String.valueOf(fxm));
        if (cont == 1) {
          row.addCell("-");
          row.addCell("-");
        } else {
          row.addCell(String.valueOf(error));
          row.addCell(String.valueOf(relativeError));
        }
        ResultsTable.addRow(row);
        return result;
      } else if (error < tol) {
        result[0] = xm; // xm es aproximaci?n a ra?z
        result[1] = tol; // Se indica el valor de la tolerancia
        ResultsTable.Row row = results.new Row();
        row.addCell(String.valueOf(cont));
        row.addCell(String.valueOf(xi));
        row.addCell(String.valueOf(fxi));
        row.addCell(String.valueOf(xs));
        row.addCell(String.valueOf(fxs));
        row.addCell(String.valueOf(xm));
        row.addCell(String.valueOf(fxm));
        if (cont == 1) {
          row.addCell("-");
          row.addCell("-");
        } else {
          row.addCell(String.valueOf(error));
          row.addCell(String.valueOf(relativeError));
        }
        ResultsTable.addRow(row);
        return result;
      } else {
        String methodName = c.getResources().getString(
            R.string.title_activity_bisection);
        String errorMessage = c.getString(R.string.error_exceeded_iterations,
            methodName);
        throw new Exception(errorMessage);
      }
    } else {
      throw new Exception(c.getResources().getString(
          R.string.error_ivalid_interval));
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
