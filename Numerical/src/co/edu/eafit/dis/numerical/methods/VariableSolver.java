package co.edu.eafit.dis.numerical.methods;

public class VariableSolver {
  
  private static final String TAG = VariableSolver.class.getName();

    public static double[] progresiveSustitution(double[][] matrix, double[] b){
        int n = matrix.length;
        double x[] = new double[n];
        for(int i = 1; i <= n; i++){
            double suma = 0;
            for(int j = i-1; j > 0; j--){
                double a = matrix[i-2][j];
                double c = x[j-1];
                suma += (matrix[i-1][j-1] * x[j-1]);
            }
            x[i-1] = (b[i-1] - suma) / matrix[i-1][i-1];
        }
        return x;
    }

    public static double[] regresiveSustitution(double[][] matrix, double[] b){
        int n = matrix.length;
        double[] x = new double[n];
        for(int i = n-1; i>=0; i--){
            double suma = 0;
            for(int j = i+1; j<n; j++){
                suma += matrix[i][j] * x[j];
            }
            x[i] = (b[i]- suma) / matrix[i][i];
        }
        return x;
    }
}