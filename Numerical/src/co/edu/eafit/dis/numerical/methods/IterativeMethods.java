package co.edu.eafit.dis.numerical.methods;

public class IterativeMethods {

  public static double[] gaussSeidel(double[][] matrix, double[] b,
      double[] x0, int iterations, double tol, double alpha) {
    int n = matrix.length;
    int counter = 1;
    // Ver las marcas
    double error = tol + 1;
    double[] x = new double[n];
    while (error > tol && counter < iterations) {
      error = 0;
      for (int i = 1; i < n + 1; i++)
        x[i - 1] = x0[i - 1];
      for (int i = 1; i < n + 1; i++) {
        double sum = 0;
        for (int j = 1; j < n + 1; j++)
          if (i != j)
            sum = sum + matrix[i - 1][j - 1] * x[j - 1];
        x[i - 1] = (b[i - 1] - sum) / matrix[i - 1][i - 1];
        x[i - 1] = alpha * (x[i - 1]) + (1 - alpha) * (x0[i - 1]);
      }
      error = VariableSolver.norma(x, x0, n);
      for (int i = 1; i < n + 1; i++) x0[i - 1] = x[i - 1];
      counter++;
    }
    if (error < tol)return x;
    else return null; // Retornar no se pudo completar el metodo
  }

  public static double [] jacobi(double[][] matrix, double[] b, double[] x0,
      int iterations, double tol, double alpha) {
    int n = matrix.length;
    int counter = 1;
    double error = tol + 1;
    double[] x = new double[n];
    while (error > tol && counter < iterations) {
      error = 0;
      for (int i = 1; i < n + 1; i++) {
        double sum = 0;
        for (int j = 1; j < n + 1; j++)
          if (i != j) sum = sum + matrix[i - 1][j - 1] * x0[j - 1];
        x[i - 1] = (b[i - 1] - sum) / matrix[i - 1][i - 1];
        x[i - 1] = alpha * (x[i - 1]) + (1 - alpha) * (x0[i - 1]);
      }
      error = VariableSolver.norma(x, x0, n);
      for (int i = 1; i < n + 1; i++) x0[i - 1] = x[i - 1];
      counter++;
    }
    if (error < tol)  return x;
    else return null; //Retornar no se pudo encontrar
  }
}
