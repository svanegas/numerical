package co.edu.eafit.dis.numerical.methods;

import java.util.ArrayList;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.views.ResultsMatrixActivity;
import android.content.Context;
import android.util.Log;

public class ResultsMatrix {
  
  private static final String TAG = ResultsMatrix.class.getSimpleName();
  
  private static Context c;
  private static ArrayList <String [][]> stagesA;
  private static ArrayList <String [][]> stagesL;
  private static ArrayList <String [][]> stagesU;
  private static int size;
  private static ResultsMatrix instance;
  
  private ResultsMatrix() {
    stagesA = new ArrayList<String [][]>();
    stagesL = new ArrayList<String [][]>();
    stagesU = new ArrayList<String [][]>();
  }
  
  public static ResultsMatrix getInstance() {
    if (instance == null) instance = new ResultsMatrix();
    return instance;
  }
  
  public static void setUpNewTable(Context c, int size) {
    ResultsMatrix.c = c;
    ResultsMatrix.size = size;
  }
  
  public static void addStageMatrix(String [][] stageA) {
    stagesA.add(stageA);
  }
  
  public static void addLowerStage(String [][] stageL) {
    stagesL.add(stageL);
  }
  
  public static void addUpperStage(String [][] stageU) {
    stagesU.add(stageU);
  }
  
  public static void clearStages() {
    stagesA.clear();
    stagesL.clear();
    stagesU.clear();
  }
  
  public static void setContext(Context c) {
    ResultsMatrix.c = c;
  }
  
  public static void setSize(int size) {
    ResultsMatrix.size = size;
  }
  
  public static ArrayList <String [][]> getStagesMatrixes() {
    return stagesA;
  }
  
  public static ArrayList <String [][]> getStagesLower() {
    return stagesL;
  }
  
  public static ArrayList <String [][]> getStagesUpper() {
    return stagesU;
  }
  
  public static String [][] getStringMatrix(int stageNumber) throws Exception {
    if (stagesA.isEmpty()) throw new Exception(c.getResources()
        .getString(R.string.error_matrixes_results_empty));
    return stagesA.get(stageNumber);
  }
  
  public static String [][] getStringLower(int stageNumber) throws Exception {
    if (stagesL.isEmpty()) throw new Exception(c.getResources()
        .getString(R.string.error_matrixes_results_empty));
    return stagesL.get(stageNumber);
  }
  
  public static String [][] getStringUpper(int stageNumber) throws Exception {
    if (stagesU.isEmpty()) throw new Exception(c.getResources()
        .getString(R.string.error_matrixes_results_empty));
    return stagesU.get(stageNumber);
  }
}
