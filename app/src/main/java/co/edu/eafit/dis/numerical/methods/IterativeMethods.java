package co.edu.eafit.dis.numerical.methods;

import java.util.Arrays;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.ResultsTable.Row;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;
import android.content.Context;

public class IterativeMethods {

  private static final String TAG = IterativeMethods.class.getSimpleName();

  private Context c = null;
  private FunctionsEvaluator functionEvaluator = null;
  private ResultsTable results;
  public static double[][] matrix;
  public static double[] vector;

  public IterativeMethods(Context c) {
    this.c = c;
    results = ResultsTable.getInstance();
  }

  public double[] gaussSeidel(double[][] matrix, double[] b, int n,
      double[] x0, int iteraciones, double tolerancia, double alpha)
      throws Exception {
    ResultsTable.setUpNewTable(c, n + 2);
    int contador = 1;
    addHeadersToResultTable(n);

    ResultsTable.Row firstRow = results.new Row();
    firstRow.addCell("0");
    addVectorToResultTable(x0, n, firstRow);
    firstRow.addCell("-");
    ResultsTable.addRow(firstRow);

    double error = tolerancia + 1;
    double[] x = new double[n];
    while (error > tolerancia && contador < iteraciones) {
      error = 0;
      for (int i = 1; i < n + 1; i++) {
        x[i - 1] = x0[i - 1];
      }
      for (int i = 1; i < n + 1; i++) {
        double suma = 0;
        for (int j = 1; j < n + 1; j++) {
          if (i != j) {
            suma = suma + matrix[i - 1][j - 1] * x[j - 1];
          }
        }
        x[i - 1] = (b[i - 1] - suma) / matrix[i - 1][i - 1];
        x[i - 1] = alpha * (x[i - 1]) + (1 - alpha) * (x0[i - 1]);
      }
      error = VariableSolver.norma(x, x0, n);
      for (int i = 1; i < n + 1; i++) {
        x0[i - 1] = x[i - 1];
      }
      contador++;
      ResultsTable.Row row = results.new Row();
      row.addCell(String.valueOf(contador - 1));
      addVectorToResultTable(x, n, row);
      row.addCell(String.valueOf(error));
      ResultsTable.addRow(row);
    }

    if (error < tolerancia) {
      return x;
    } else {
      String methodName = c.getResources().getString(R.string.text_jacobi);
      String errorMessage = c.getString(R.string.error_exceeded_iterations,
          methodName);
      throw new Exception(errorMessage);
    }
  }

  public double[] jacobi(double[][] matrix, double[] b, int n, double[] x0,
      int iter, double tol, double alpha) throws Exception {
    ResultsTable.setUpNewTable(c, n + 2);
    int contador = 1;
    addHeadersToResultTable(n);

    ResultsTable.Row firstRow = results.new Row();
    firstRow.addCell("0");
    addVectorToResultTable(x0, n, firstRow);
    firstRow.addCell("-");
    ResultsTable.addRow(firstRow);

    double error = tol + 1;
    double[] x = new double[n];
    while (error > tol && contador < iter) {
      error = 0;
      for (int i = 1; i < n + 1; i++) {
        double suma = 0;
        for (int j = 1; j < n + 1; j++) {
          if (i != j) {
            suma = suma + matrix[i - 1][j - 1] * x0[j - 1];
          }
        }
        x[i - 1] = (b[i - 1] - suma) / matrix[i - 1][i - 1];
        x[i - 1] = alpha * (x[i - 1]) + (1 - alpha) * (x0[i - 1]);
      }
      error = VariableSolver.norma(x, x0, n);
      for (int i = 1; i < n + 1; i++) {
        x0[i - 1] = x[i - 1];
      }
      contador++;

      ResultsTable.Row row = results.new Row();
      row.addCell(String.valueOf(contador - 1));
      addVectorToResultTable(x, n, row);
      row.addCell(String.valueOf(error));
      ResultsTable.addRow(row);
    }

    if (error < tol) {
      return x;
    } else {
      String methodName = c.getResources().getString(R.string.text_jacobi);
      String errorMessage = c.getString(R.string.error_exceeded_iterations,
          methodName);
      throw new Exception(errorMessage);
    }
  }

  public void addHeadersToResultTable(int n) throws Exception {
    ResultsTable.clearTable();
    ResultsTable.Row headers = results.new Row();
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_iteration));
    for (int i = 0; i < n; ++i) {
      headers
          .addCell(c.getResources().getString(R.string.text_header_x, i + 1));
    }
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_absolute_dispersion));
    ResultsTable.addRow(headers);
  }

  public static void addVectorToResultTable(double[] vector, int n,
      ResultsTable.Row row) throws Exception {
    for (int i = 0; i < n; i++) {
      row.addCell(String.valueOf(vector[i]));
    }
  }
}
