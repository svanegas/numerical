package co.edu.eafit.dis.numerical.methods;

import android.content.Context;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class Interpolation {

  private static final String TAG = Interpolation.class.getSimpleName();

  private Context c = null;
  private FunctionsEvaluator functionEvaluator = null;
  private ResultsTable results;

  public Interpolation(Context c) {
    this.c = c;
    results = ResultsTable.getInstance();
  }

  public String calculateNewtonDividedDifferences(int nPoints, double[] x,
      double[] y) throws Exception {
    double[][] differences = new double[nPoints][nPoints];
    for (int i = 0; i < nPoints; i++) {
      differences[i][0] = y[i];
    }

    for (int i = 0; i < nPoints; i++) {
      for (int j = 1; j < i + 1; j++) {
        differences[i][j] = (differences[i][j - 1] - differences[i - 1][j - 1])
            / (x[i] - x[i - j]);
      }
    }
    buildResultsTable(differences, nPoints, x);
    String poly = String.valueOf(differences[0][0]);
    String temp = "";
    for (int i = 1; i < nPoints; i++) {
      if (i > 1) temp += "*";
      temp += "(x" + (x[i - 1] < 0.0 ? "+" : "-") + Math.abs(x[i - 1]) + ")";
      poly += "\n" + (differences[i][i] > 0 ? "+" : "")
          + (differences[i][i] + "*" + temp);
    }
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    functionEvaluator.setFunction(functionEvaluator.removeLineBreaks(poly));
    poly = "P(x) = " + poly;
    return poly;
  }

  private void buildResultsTable(double[][] matrix, int n, double[] x)
      throws Exception {
    ResultsTable.setUpNewTable(c, n + 2);
    ResultsTable.clearTable();
    ResultsTable.Row headers = results.new Row();
    headers.addCell(c.getResources().getString(
        R.string.text_results_table_iteration));
    headers.addCell(c.getResources().getString(R.string.text_header_xi));
    headers.addCell(c.getResources().getString(R.string.text_header_fxi));
    for (int i = 1; i < n; ++i) {
      headers.addCell(c.getResources().getString(
          R.string.text_header_divided_difference, i));
    }
    ResultsTable.addRow(headers);

    for (int i = 0; i < n; i++) {
      ResultsTable.Row row = results.new Row();
      row.addCell(String.valueOf(i));
      row.addCell(String.valueOf(x[i]));
      for (int j = 0; j < n; j++) {
        row.addCell(String.valueOf(matrix[i][j]));
      }
      ResultsTable.addRow(row);
    }
  }

  public String calculateLagrange(int n, double[] x, double[] y)
      throws Exception {
    String poly = "";
    for (int k = 0; k < n; k++) {
      String term = "";
      boolean first = true;
      for (int i = 0; i < n; i++) {
        if (i != k) {
          if (!first) term += "*";
          if (first) first = false;
          term += ("[(x-" + x[i] + ")/(" + x[k] + "-" + x[i] + ")]");
        }
      }
      poly += (y[k] > 0 ? "+" : "") + y[k] + "*" + term + "\n";
    }
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    functionEvaluator.setFunction(functionEvaluator.removeLineBreaks(poly));
    poly = "P(x) = " + poly;
    return poly;
  }

  public String calculateNeville(int n, double[] x, double[] y, double value)
      throws Exception {
    double[][] table = new double[n][n];
    for (int i = 0; i < n; i++) {
      table[i][0] = y[i];
    }
    for (int i = 0; i < n; i++) {
      for (int j = 1; j < i + 1; j++) {
        table[i][j] = ((value - x[i - j]) * table[i][j - 1]
            - ((value - x[i]) * table[i - 1][j - 1])) / (x[i] - x[i - j]);
      }
    }
    buildResultsTable(table, n, x);
    return "f(" + value + ") = " + table[n - 1][n - 1];
  }
}
