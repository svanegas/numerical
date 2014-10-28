package co.edu.eafit.dis.numerical.methods;

import java.util.ArrayList;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.views.ResultsMatrixActivity;
import android.content.Context;
import android.util.Log;

public class ResultsMatrix {
  
  private static final String TAG = ResultsMatrix.class.getSimpleName();
  
  private static Context c;
  private static ArrayList <String [][]> stages;
  private static int size;
  private static ResultsMatrix instance;
  
  private ResultsMatrix() {
    stages = new ArrayList<String [][]>();
  }
  
  public static ResultsMatrix getInstance() {
    if (instance == null) instance = new ResultsMatrix();
    return instance;
  }
  
  public static void setUpNewTable(Context c, int size) {
    ResultsMatrix.c = c;
    ResultsMatrix.size = size;
  }
  
  public static void addStage(String [][] stage) {
    stages.add(stage);
    Log.i(TAG, "Agrego un nuevo stage, ya tiene " + stages.size());
  }
  
  public static void clearStages() {
    stages.clear();
  }
  
  public static void setContext(Context c) {
    ResultsMatrix.c = c;
  }
  
  public static void setSize(int size) {
    ResultsMatrix.size = size;
  }
  
  public static ArrayList <String [][]> getStagesMatrixes() {
    return stages;
  }
  
  public static String [][] getStringMatrix(int stageNumber) throws Exception {
    if (stages.isEmpty()) throw new Exception(c.getResources()
        .getString(R.string.error_matrixes_results_empty));
    return stages.get(stageNumber);
  }
}
