package co.edu.eafit.dis.numerical.methods;

import java.util.Arrays;

import android.content.Context;

public class LUFactorization {

  private static final String TAG = LUFactorization.class.getName();

  private Context c = null;
  private ResultsMatrix results;

  public LUFactorization(Context c) {
    this.c = c;
    results = ResultsMatrix.getInstance();
  }

  public double[] calculateCrout(int n, double A[][], double[] b) {
    double[][] L = new double[n][n];
    double[][] U = new double[n][n];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (i < j) {
          U[i][j] = Double.POSITIVE_INFINITY;
          L[i][j] = 0;
        } else if (i > j) {
          L[i][j] = Double.POSITIVE_INFINITY;
          U[i][j] = 0;
        } else if (i == j) {
          U[i][j] = 1;
          L[i][j] = Double.POSITIVE_INFINITY;
        }
      }
    }
    ResultsMatrix.setUpNewTable(c, n);
    ResultsMatrix.clearStages();
    ResultsMatrix.addStageMatrix(VariableSolver.getStringMatrix(A));
    ResultsMatrix.addLowerStage(VariableSolver.getStringMatrix(L));
    ResultsMatrix.addUpperStage(VariableSolver.getStringMatrix(U));
    for (int k = 1; k < n + 1; k++) {
      ResultsMatrix.addStageMatrix(VariableSolver.getStringMatrix(A));
      double suma = 0;
      for (int p = 0; p < k - 1; p++) {
        suma += L[k - 1][p] * U[p][k - 1];
      }
      L[k - 1][k - 1] = (A[k - 1][k - 1] - suma) / U[k - 1][k - 1];
      for (int i = k + 1; i < n + 1; i++) {
        suma = 0;
        for (int p = 0; p < k - 1; p++) {
          suma += L[i - 1][p] * U[p][k - 1];
        }
        L[i - 1][k - 1] = (A[i - 1][k - 1] - suma) / U[k - 1][k - 1];
      }

      ResultsMatrix.addLowerStage(VariableSolver.getStringMatrix(L));

      for (int j = k + 1; j < n + 1; j++) {
        suma = 0;
        for (int p = 0; p < k - 1; p++) {
          suma += L[k - 1][p] * U[p][j - 1];
        }
        U[k - 1][j - 1] = (A[k - 1][j - 1] - suma) / L[k - 1][k - 1];
      }
      ResultsMatrix.addUpperStage(VariableSolver.getStringMatrix(U));
    }
    double[] z = VariableSolver.progresiveSustitution(L, b);
    double[] x = VariableSolver.regresiveSustitution(U, z);
    return x;
  }

  public double[] calculateDoolittle(int n, double[][] A, double[] b) {
    double[][] L = new double[n][n];
    double[][] U = new double[n][n];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (i < j) {
          U[i][j] = Double.POSITIVE_INFINITY;
          L[i][j] = 0;
        } else if (i > j) {
          L[i][j] = Double.POSITIVE_INFINITY;
          U[i][j] = 0;
        } else if (i == j) {
          L[i][j] = 1;
          U[i][j] = Double.POSITIVE_INFINITY;
        }
      }
    }

    ResultsMatrix.setUpNewTable(c, n);
    ResultsMatrix.clearStages();
    ResultsMatrix.addStageMatrix(VariableSolver.getStringMatrix(A));
    ResultsMatrix.addLowerStage(VariableSolver.getStringMatrix(L));
    ResultsMatrix.addUpperStage(VariableSolver.getStringMatrix(U));

    for (int k = 1; k < n + 1; k++) {
      ResultsMatrix.addStageMatrix(VariableSolver.getStringMatrix(A));
      double suma = 0;
      for (int p = 0; p < k - 1; p++) {
        suma += L[k - 1][p] * U[p][k - 1];
      }
      U[k - 1][k - 1] = (A[k - 1][k - 1] - suma) / L[k - 1][k - 1];

      for (int j = k + 1; j < n + 1; j++) {
        suma = 0;
        for (int p = 0; p < k - 1; p++) {

          suma += L[k - 1][p] * U[p][j - 1];
        }
        U[k - 1][j - 1] = (A[k - 1][j - 1] - suma) / L[k - 1][k - 1];
      }
      ResultsMatrix.addLowerStage(VariableSolver.getStringMatrix(L));
      for (int i = k + 1; i < n + 1; i++) {
        suma = 0;
        for (int p = 0; p < k - 1; p++) {
          suma += L[i - 1][p] * U[p][k - 1];
        }
        L[i - 1][k - 1] = (A[i - 1][k - 1] - suma) / U[k - 1][k - 1];
      }
      ResultsMatrix.addUpperStage(VariableSolver.getStringMatrix(U));
    }

    double[] z = VariableSolver.progresiveSustitution(L, b);
    double[] x = VariableSolver.regresiveSustitution(U, z);
    return x;
  }

  public double[] calculateCholesky(int n, double[][] A, double[] b) {
    double[][] L = new double[n][n];
    double[][] U = new double[n][n];

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (i < j) {
          U[i][j] = Double.POSITIVE_INFINITY;
          L[i][j] = 0;
        } else if (i > j) {
          L[i][j] = Double.POSITIVE_INFINITY;
          U[i][j] = 0;
        } else if (i == j) {
          L[i][j] = Double.POSITIVE_INFINITY;
          U[i][j] = Double.POSITIVE_INFINITY;
        }
      }
    }

    ResultsMatrix.setUpNewTable(c, n);
    ResultsMatrix.clearStages();
    ResultsMatrix.addStageMatrix(VariableSolver.getStringMatrix(A));
    ResultsMatrix.addLowerStage(VariableSolver.getStringMatrix(L));
    ResultsMatrix.addUpperStage(VariableSolver.getStringMatrix(U));

    for (int k = 1; k < n + 1; k++) {
      ResultsMatrix.addStageMatrix(VariableSolver.getStringMatrix(A));
      double suma = 0;
      for (int p = 0; p < k - 1; p++) {
        suma += L[k - 1][p] * U[p][k - 1];
      }
      U[k - 1][k - 1] = Math.sqrt(A[k - 1][k - 1] - suma);
      L[k - 1][k - 1] = U[k - 1][k - 1];

      for (int j = k + 1; j < n + 1; j++) {
        suma = 0;
        for (int p = 0; p < k - 1; p++) {

          suma += L[j - 1][p] * U[p][k - 1];
        }
        L[j - 1][k - 1] = (A[j - 1][k - 1] - suma) / L[k - 1][k - 1];
      }
      ResultsMatrix.addLowerStage(VariableSolver.getStringMatrix(L));
      for (int i = k + 1; i < n + 1; i++) {
        suma = 0;
        for (int p = 0; p < k - 1; p++) {
          suma += L[k - 1][p] * U[p][i - 1];
        }
        U[k - 1][i - 1] = (A[k - 1][i - 1] - suma) / L[k - 1][k - 1];
      }
      ResultsMatrix.addUpperStage(VariableSolver.getStringMatrix(U));
    }

    double[] z = VariableSolver.progresiveSustitution(L, b);
    double[] x = VariableSolver.regresiveSustitution(U, z);
    return x;
  }
}