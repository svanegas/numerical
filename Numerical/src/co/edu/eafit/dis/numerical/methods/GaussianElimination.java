package co.edu.eafit.dis.numerical.methods;

import android.util.Log;

public class GaussianElimination {
	
	public GaussianElimination(){
		}
	
	public void calculeSimpleGaussianElimiation(int matrixSize, double[][] matrix){
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
	
	public void calculeGaussianEliminationWithoutPivot(int matrixSize, double[][] matrix){
		for(int k = 1; k < matrixSize; k++){
			matrix = calculeWithoutPivot(matrix, k, matrixSize);
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
	
	private double[][] calculeWithoutPivot(double[][] A, int n, int k){
			double highestValue = Math.abs(A[n-1][n-1]);
			int highestRow = n-1;
			for (int s = n-1; s < k; s++) {
				if (Math.abs(A[s][n-1]) > highestValue) {
					highestValue = Math.abs(A[s][n-1]);
					highestRow = s;
				}
			}
			if(highestValue == 0){
				//El sistema no tiene solucion
			}else{
				if(highestRow != n-1){
                    for(int i = 0; i < A[0].length; i++){
                        double aux = A[n-1][i];
                        A[n-1][i] = A[highestRow][i];
                        A[highestRow][i] = aux;
                    }
				}
			}		
			return A;
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
