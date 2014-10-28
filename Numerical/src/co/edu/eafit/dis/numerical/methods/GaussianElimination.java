package co.edu.eafit.dis.numerical.methods;

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
