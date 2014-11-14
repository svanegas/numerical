package co.edu.eafit.dis.numerical.methods;

public class VariableSolver {

  private static final String TAG = VariableSolver.class.getName();

  public static double[] progresiveSustitution(double[][] matrix, double[] b) {
    int n = matrix.length;
    double x[] = new double[n];
    for (int i = 1; i <= n; i++) {
      double suma = 0;
      for (int j = i - 1; j > 0; j--) {
        double a = matrix[i - 2][j];
        double c = x[j - 1];
        suma += (matrix[i - 1][j - 1] * x[j - 1]);
      }
      x[i - 1] = (b[i - 1] - suma) / matrix[i - 1][i - 1];
    }
    return x;
  }

  public static double[] regresiveSustitution(double[][] matrix, double[] b) {
    int n = matrix.length;
    double[] x = new double[n];
    for (int i = n - 1; i >= 0; i--) {
      double suma = 0;
      for (int j = i + 1; j < n; j++) {
        suma += matrix[i][j] * x[j];
      }
      x[i] = (b[i] - suma) / matrix[i][i];
    }
    return x;
  }

  public static double norma(double[] x, double[] x0, int n) {
    double maximun = Double.NEGATIVE_INFINITY;
    double norma = 0;
    for (int i = 1; i < n; i++)
      maximun = Math.max((x[i - 1] - x0[i - 1]), maximun);
    norma = maximun;
    return norma;
  }

  public static String[][] getStringMatrix(double[][] matrix) {
    int n = matrix.length;
    int m = matrix[0].length;
    String[][] resultMatrix = new String[n + 1][m];
    for (int j = 0; j < m; ++j) {
      if (j < m - 1)
        resultMatrix[0][j] = "X" + (j + 1);
      else
        resultMatrix[0][j] = "b";
    }
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < m; ++j) {
        resultMatrix[i + 1][j] = String.valueOf(matrix[i][j]);
      }
    }
    return resultMatrix;
  }
  
  public static String[][] getStringMatrix(double[][] matrix, int[] marks) {
    int n = matrix.length;
    int m = matrix[0].length;
    String[][] resultMatrix = new String[n + 1][m];
    for (int j = 0; j < m; ++j) {
      if (j < m - 1)
        resultMatrix[0][j] = "X" + marks[j];
      else
        resultMatrix[0][j] = "b";
    }
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < m; ++j) {
        resultMatrix[i + 1][j] = String.valueOf(matrix[i][j]);
      }
    }
    return resultMatrix;
  }
  
  public static String getStringSolution(double[] solution) {
    String result = "";
    for (int i = 0; i < solution.length; i++) {
      result += "X" + (i + 1) + " = " + solution[i] + "\n";
    }
    return result;
  }
}