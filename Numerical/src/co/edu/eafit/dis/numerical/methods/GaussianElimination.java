package co.edu.eafit.dis.numerical.methods;

<<<<<<< HEAD
import java.util.Collection;
import java.util.Collections;

import android.content.Context;
import android.util.Log;

public class GaussianElimination {
  
  private Context c = null;
  private ResultsMatrix results;
  
  public GaussianElimination(Context c) {
    this.c = c;
    results = ResultsMatrix.getInstance();
  }
  
  public double [] calculateSimpleGaussianElimiation(int matrixSize,
      double[][] matrix) {
    ResultsMatrix.setUpNewTable(c, matrixSize);
    ResultsMatrix.clearStages();
    ResultsMatrix.addStage(getStringMatrix(matrix));
    for(int k = 1; k <= matrixSize-1; k++){
      for(int i = k+1; i <= matrixSize; i++){
        double multiplier = matrix[i-1][k-1]/matrix[k-1][k-1];
        for(int j = k; j <= matrixSize+1; j++){
          matrix[i-1][j-1] = matrix[i-1][j-1] - multiplier*matrix[k-1][j-1];
        }
      }
      //Acá se manda cada iteración de la matriz para mostrar
      ResultsMatrix.addStage(getStringMatrix(matrix));
    }
    return getSolution(matrix, matrixSize);
  }
  
  public double [] calculateGaussianEliminationPartialPivoting(int matrixSize,
      double[][] matrix) {
    ResultsMatrix.setUpNewTable(c, matrixSize);
    ResultsMatrix.clearStages();
    ResultsMatrix.addStage(getStringMatrix(matrix));
    for(int k = 1; k < matrixSize; k++){
      matrix = calculatePartialPivoting(matrix, matrixSize, k);
      for(int i = k+1; i < matrixSize+1; i++){
        double multiplier = matrix[i-1][k-1]/matrix[k-1][k-1];
        for(int j = k; j < matrixSize+2; j++){
          matrix[i-1][j-1] = matrix[i-1][j-1] - multiplier*matrix[k-1][j-1];
        }
      }
      //Acá se manda cada iteración de la matriz para mostrar
      ResultsMatrix.addStage(getStringMatrix(matrix));
    }
    return getSolution(matrix, matrixSize);
  }
  
  private double[][] calculatePartialPivoting(double[][] A, int n, int k){
    double highestValue = Math.abs(A[k-1][k-1]);
    int highestRow = k-1;
    for (int s = k - 1; s < n; s++) {
      if (Math.abs(A[s][k-1]) > highestValue) {
        highestValue = Math.abs(A[s][k-1]);
        highestRow = s;
      }
    }
    if(highestValue == 0){
      //El sistema no tiene solucion
    }else{
      if(highestRow != k-1){
        for(int i = 0; i < A[0].length; i++){
            double aux = A[k-1][i];
            A[k-1][i] = A[highestRow][i];
            A[highestRow][i] = aux;
        }
      }
    }   
    return A;
  }
  
  public double [] calculateGaussianEliminationTotalPivoting(int matrixSize,
      double[][] matrix){
    int[] mark = makeMarks(matrixSize);
    //Se muestra matriz
    ResultsMatrix.setUpNewTable(c, matrixSize);
    ResultsMatrix.clearStages();
    ResultsMatrix.addStage(getStringMatrix(matrix, mark));
    for(int k = 1; k < matrixSize; k++) {
      matrix = calculateTotalPivoting(matrix, mark, matrixSize, k);
      for(int i = k+1; i < matrixSize+1; i++){
        double multiplier = matrix[i-1][k-1]/matrix[k-1][k-1];
        for(int j = k; j < matrixSize+2; j++){
          matrix[i-1][j-1] = matrix[i-1][j-1] - multiplier*matrix[k-1][j-1];
        }
      }            
      //Acá se manda cada iteración de la matriz para mostrar
      ResultsMatrix.addStage(getStringMatrix(matrix, mark));
    }
    return getSolution(matrix, mark, matrixSize);
  }
  
  public double[][] calculateTotalPivoting(double[][] A,int marks[] ,int n,
      int k) {
    double highestValue = 0;
    int highestRow = k-1;
    int highestColumn = k-1;
    for (int r = k-1; r < n; r++) {     
      for (int s = k-1; s < n; s++) {
        if(Math.abs(A[r][s]) > highestValue){
          highestValue = Math.abs(A[r][s]);
          highestRow = r;
          highestColumn = s;
        }
      }
    }
    if(highestValue == 0){
      //El sistema no tiene solución única
    }
    else{
      if(highestRow != k-1){
        //Acá se intercambian las filas
        for(int i = 0; i < A[0].length; i++){
          double aux = A[k-1][i];
          A[k-1][i] = A[highestRow][i];
          A[highestRow][i] = aux;
        }
        //Acá se debe imprimir la matriz
      }
      if(highestColumn != k-1){
        for(int i = 0; i < n; i++){
          double aux = A[i][k-1];
          A[i][k-1] = A[i][highestColumn];
          A[i][highestColumn] = aux;
        }
        int aux2 = marks[highestColumn];
        marks[highestColumn] = marks[k-1];
        marks[k-1] = aux2;
        //acá se imprime la matriz de nuevo
      }
    }       
    return A;
  }
  
  public int[] makeMarks(int n){
    int[] mark = new int[n];
    for(int i = 0; i < n; i++){
      mark[i] = i+1;
    }
    return mark;
  }

  private double [] getSolution(double [][] matrix, int [] marks,
      int matrixSize) {
    double [][] AMatrix = new double[matrixSize][matrixSize];
    double [] bVector = new double[matrixSize];
    for (int i = 0; i < matrixSize; ++i) {
      for (int j = 0; j < matrixSize; ++j) {
        AMatrix[i][j] = matrix[i][j];
      }
    }
    for (int i = 0; i < matrixSize; ++i) {
      bVector[i] = matrix[i][matrixSize];
    }
    double [] solution = VariableSolver.regresiveSustitution(AMatrix, bVector);
    for (int i = 0; i < matrixSize; ++i) {
      int currMark = marks[i];
      if (marks[i] != i + 1) {
        int auxMark = marks[currMark - 1];
        double auxSol = solution[currMark - 1];
        marks[currMark - 1] = currMark;
        marks[i] = auxMark;
        solution[currMark - 1] = solution[i];
        solution[i] = auxSol;
      }
    }
    return solution;
  }
  
  private double [] getSolution(double [][] matrix, int matrixSize) {
    double [][] AMatrix = new double[matrixSize][matrixSize];
    double [] bVector = new double[matrixSize];
    for (int i = 0; i < matrixSize; ++i) {
      for (int j = 0; j < matrixSize; ++j) {
        AMatrix[i][j] = matrix[i][j];
      }
    }
    for (int i = 0; i < matrixSize; ++i) {
      bVector[i] = matrix[i][matrixSize];
    }
    double [] solution = VariableSolver.regresiveSustitution(AMatrix, bVector);
    return solution;
  }
  
  private String [][] getStringMatrix(double [][] matrix, int [] marks) {
    int n = matrix.length;
    int m = matrix[0].length;
    String [][] resultMatrix = new String[n + 1][m];
    for (int j = 0; j < m; ++j) {
      if (j < m - 1) resultMatrix[0][j] = "X" + marks[j]; 
      else resultMatrix[0][j] = "b";
    }
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < m; ++j) {
        resultMatrix[i+1][j] = String.valueOf(matrix[i][j]);
      }
    }
    return resultMatrix;
  }
  
  private String [][] getStringMatrix(double [][] matrix) {
    int n = matrix.length;
    int m = matrix[0].length;
    String [][] resultMatrix = new String[n+1][m];
    for (int j = 0; j < m; ++j) {
      if (j < m - 1) resultMatrix[0][j] = "X" + (j + 1);
      else resultMatrix[0][j] = "b";
    }
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < m; ++j) {
        resultMatrix[i+1][j] = String.valueOf(matrix[i][j]);
      }
    }
    return resultMatrix;
  }
}
=======
import android.util.Log;

public class GaussianElimination {
	
	public GaussianElimination(){
		}
	
	public void calculateSimpleGaussianElimiation(int matrixSize, double[][] matrix){
		//se muestra matriz
		for(int k = 1; k <= matrixSize-1; k++){
			for(int i = k+1; i <= matrixSize; i++){
				double multiplier = matrix[i-1][k-1]/matrix[k-1][k-1];
				for(int j = k; j <= matrixSize+1; j++){
					matrix[i-1][j-1] = matrix[i-1][j-1] - multiplier*matrix[k-1][j-1];
				}
			}            
            //Acá se manda cada iteración de la matriz para mostrar
		}
		sustitucionRegresiva(matrixSize, matrix);
	}
	
	public void calculateGaussianEliminationParcialPivoting(int matrixSize, double[][] matrix){
		//se muestra matriz
		for(int k = 1; k < matrixSize; k++){
			matrix = calculateParcialPivoting(matrix, k, matrixSize);
			for(int i = k+1; i < matrixSize+1; i++){
				double multiplier = matrix[i-1][k-1]/matrix[k-1][k-1];
				for(int j = k; j < matrixSize+2; j++){
					matrix[i-1][j-1] = matrix[i-1][j-1] - multiplier*matrix[k-1][j-1];
				}
			}            
            //Acá se manda cada iteración de la matriz para mostrar
		}
		sustitucionRegresiva(matrixSize, matrix);
	}
	
	private double[][] calculateParcialPivoting(double[][] A, int n, int k){
			double highestValue = Math.abs(A[n-1][n-1]);
			int highestRow = n-1;
			for (int s = n-1; s < k; s++) {
				if (Math.abs(A[s][n-1]) > highestValue){
					highestValue = Math.abs(A[s][n-1]);
					highestRow = s;
				}
			}
			if(highestValue == 0){
				//El sistema no tiene solucion
			}else{
				if(highestRow != n-1){
					//Se intercambian las filas
                    for(int i = 0; i < A[0].length; i++){
                        double aux = A[n-1][i];
                        A[n-1][i] = A[highestRow][i];
                        A[highestRow][i] = aux;
                    }
				}
			}		
			return A;
	}
	
	public void calculateGaussianEliminationTotalPivoting(int matrixSize, double[][] matrix){
		int[] mark = makeMarks(matrixSize);
		//Se muestra matriz
		for(int k = 1; k < matrixSize; k++){
			matrix = calculateTotalPivoting(matrix, mark, k, matrixSize);
			for(int i = k+1; i < matrixSize+1; i++){
				double multiplier = matrix[i-1][k-1]/matrix[k-1][k-1];
				for(int j = k; j < matrixSize+2; j++){
					matrix[i-1][j-1] = matrix[i-1][j-1] - multiplier*matrix[k-1][j-1];
				}
			}            
            //Acá se manda cada iteración de la matriz para mostrar
		}
		sustitucionRegresiva(matrixSize, matrix);
	}
	
	public double[][] calculateTotalPivoting(double[][] A,int marks[] ,int n, int k){
		
		double highestValue = 0;
		int highestRow = k-1;
		int highestColumn = k-1;
		for (int r = k-1; r < n; r++) {			
			for (int s = k-1; s < n; s++) {
				if(Math.abs(A[r][2]) > highestValue){
					highestValue = Math.abs(A[r][s]);
					highestRow = r;
					highestColumn = s;
				}
			}
		}
		
		if(highestValue == 0){
			//El sistema no tiene solución única
		}else{
			if(highestRow != k-1){
				//Acá se intercambian las filas
                for(int i = 0; i < A[0].length; i++){
                    double aux = A[n-1][i];
                    A[n-1][i] = A[highestRow][i];
                    A[highestRow][i] = aux;
                }
                //Acá se debe imprimir la matriz
			}
			if(highestColumn != k-1){
                for(int i = 0; i < k; i++){
                    double aux = A[i][n-1];
                    A[i][n-i] = A[i][highestColumn];
                    A[i][highestColumn] = aux;
                }
                int aux2 = marks[highestColumn];
                marks[highestColumn] = marks[n-1];
                marks[n-1] = aux2;
                //acá se imprime la matriz de nuevo
			}
		}				
		return A;
	}
	
    public int[] makeMarks(int n){
        int[] mark = new int[n];
        for(int i = 0; i < n; i++){
            mark[i] = i+1;
        }
        return mark;
    }
	
	private void sustitucionRegresiva(int matrixSize, double[][]matrix){
        double x[] = new double[matrixSize];
        for(int i = matrixSize; i > 0; i--){
                double summation = 0;
                for(int p = i+1; p <= matrixSize; p++){
                	summation = summation + matrix[i-1][p-1]*x[p-1];
                }
                x[i-1] = (matrix[i-1][matrixSize]-summation)/matrix[i-1][i-1];
                Log.d("result",  "X"+i+" = "+x[i-1]);
        }
	}
       

}
>>>>>>> 82cefd72a17143854e2f2919429c8b7a19397e32
