package co.edu.eafit.dis.numerical.methods;

import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class LUFactorization {

  private static final String TAG = FunctionsEvaluator.class.getName();
  
  public double[] LUFactorizationWithCrout(double[][] A, double[] b){
    int matrixSize = A[0].length;
    double [][] L = new double[matrixSize][matrixSize];
    double [][] U = new double[matrixSize][matrixSize];
    for(int i = 0; i < matrixSize; ++i){
      for(int j = 0; j < matrixSize; ++j){
        if(i < j){
          U[i][j] = Double.POSITIVE_INFINITY;
          L[i][j] = 0;
        } else if(i > j){
          U[i][j] = 0;
          L[i][j] = Double.POSITIVE_INFINITY;
        } else if(i == j){
          U[i][j] = 1;
          L[i][j] = Double.POSITIVE_INFINITY;
        }
      }
    }
    for(int i = 1; i < matrixSize+1; ++i){
      //TODO save State
      double suma = 0;
      for(int j = 0; j < i-1; ++j) suma += L[i-1][j] * U[j][i-1];
      L[i-1][i-1] = (A[i-1][i-1] - suma) / U[i-1][i-1];
      for(int k = i+1; i < matrixSize+1; i++){
          suma = 0;
          for(int p = 0; p < i-1; p++) suma += L[i-1][p] * U[p][i-1];
          L[k-1][i-1] = (A[k-1][i-1] - suma)/U[i-1][i-1];
      }
      //TODO Guardar estado       
      for(int j = i+1; j <= matrixSize; ++j){
          suma = 0;
          for(int p = 0; p < i-1; p++)suma += L[i-1][p] * U[p][j-1];
          U[i-1][j-1] = (A[i-1][j-1] - suma)/L[i-1][i-1];
      }
      //TODO Guardar estado
    }
    double[] z = VariableSolver.regresiveSustitution(L, b);
    double[] x = VariableSolver.progresiveSustitution(U, z);
    return x;
  }
  
  public double[] LUFactorizationWithDoolittle(double[][] A, double[] b){
    int n = A[0].length;
      double [][] L = new double[n][n];
      double [][] U = new double[n][n];
      for(int i=0;i<n;i++){
        for(int j=0;j<n;j++){
          if(i<j){
           U[i][j] = Double.POSITIVE_INFINITY;
           L[i][j] = 0;
          }else if(i>j){
            L[i][j] = Double.POSITIVE_INFINITY;
            U[i][j] = 0;
          }else if(i==j){
            L[i][j] = 1;
            U[i][j] = Double.POSITIVE_INFINITY;
          }
        }
      }
      //TODO Guardar estados           
      for(int k = 1; k < n+1; ++k){
        double suma = 0;
        for(int p = 0; p < k-1; ++p) suma += L[k-1][p] * U[p][k-1];
        U[k-1][k-1] = (A[k-1][k-1] - suma)/L[k-1][k-1];
        for(int j = k+1; j < n+1; ++j){
          suma = 0;
          for(int p = 0; p < k-1; ++p) suma += L[k-1][p] * U[p][j-1];
          U[k-1][j-1] = (A[k-1][j-1] - suma)/L[k-1][k-1];
        }
        //TODO Guardar estado
        for(int i = k+1; i < n+1; ++i){
          suma = 0;
          for(int p = 0; p < k-1; ++p) suma += L[i-1][p] * U[p][k-1];
          L[i-1][k-1] = (A[i-1][k-1] - suma)/U[k-1][k-1];
        } 
        //TODO Guardar estado
      }
      double[] z = VariableSolver.progresiveSustitution(L, b);
      double[] x = VariableSolver.regresiveSustitution(U, z);
      return x;
  }
  
  public double[] LUFactorizationWithCholesky(double[][] A, double[] b) {
    int n = A[0].length;
    double [][] L = new double[n][n];
    double [][] U = new double[n][n];
    for(int i = 0; i < n; ++i){
      for(int j = 0; j < n; ++j){
        if(i<j){
          U[i][j] = Double.POSITIVE_INFINITY;
          L[i][j] = 0;
        }else if(i>j){
          L[i][j] = Double.POSITIVE_INFINITY;
          U[i][j] = 0;
        }else if(i==j){
          L[i][j] = Double.POSITIVE_INFINITY;
          U[i][j] = Double.POSITIVE_INFINITY;
        }
      }
    }
    //TODO Guardar estados
    for(int k = 1; k < n+1; ++k){
      double suma = 0;
      for(int p = 0; p < k-1; ++p) suma += L[k-1][p] * U[p][k-1];
      U[k-1][k-1] = Math.sqrt(A[k-1][k-1] - suma);
      L[k-1][k-1] = U[k-1][k-1];
      for(int j = k+1; j < n+1; ++j){
        suma = 0;
        for(int p = 0; p < k-1; p++) suma += L[j-1][p] * U[p][k-1];
        L[j-1][k-1] = (A[j-1][k-1] - suma) / L[k-1][k-1];
      }
      //TODO Guardar estado
      for(int i = k+1; i < n+1; i++){
        suma = 0;
        for(int p = 0; p < k-1; p++) suma += L[k-1][p] * U[p][i-1];
        U[k-1][i-1] = (A[k-1][i-1] - suma)/L[k-1][k-1];
      } 
      //TODO Guardar estado
    }
    double[] z = VariableSolver.progresiveSustitution(L, b);
    double[] x = VariableSolver.regresiveSustitution(U, z);
    return x;
  }
}