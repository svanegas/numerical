package co.edu.eafit.dis.numerical.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase utilizada para manejar las preferencias compartidas, funciones e
 * informaci?n ingresadas por el usuario. (Busca ?ste que no deba reescribir)
 * 
 */
public class PreferencesManager {

  private static final String PREFS_NAME = "co.edu.eafit.dis.numerical";
  private static SharedPreferences sp;
  private static SharedPreferences.Editor spEditor;

  // --- Llaves ---
  private static final String FUNCTION_FX = "fx";
  private static final String FUNCTION_D1FX = "d1fx";
  private static final String FUNCTION_D2FX = "d2fx";
  private static final String FUNCTION_GX = "gx";
  private static final String FUNCTION_X0 = "x0";
  private static final String FUNCTION_DELTA = "delta";
  private static final String FUNCTION_MAX_ITERATIONS = "iterations";
  private static final String FUNCTION_XI = "xi";
  private static final String FUNCTION_XS = "xs";
  private static final String FUNCTION_XA = "xa";
  private static final String FUNCTION_TOLERANCE = "tolerance";
  private static final String FUNCTION_ROW = "row#";
  private static final String FUNCTION_VECTOR = "vector";
  private static final String FUNCTION_INITIAL_VECTOR = "initial_vector";
  private static final String MATRIX_ROWS_SIZE = "rows";
  private static final String MATRIX_COLUMNS_SIZE = "columns";
  private static final String VECTOR_SIZE = "size";
  private static final String POINTS_SIZE = "points_size";
  private static final String POINTS_ROW = "point#";

  public static void setup(Context context) {
    sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    spEditor = sp.edit();
  }

  // --- Put methods ---
  // Guarda los datos de la matriz que viene por par?metro en strings separados
  // por comas.
  public static void setMatrix(String[][] matrix) {
    int rows = matrix.length;
    int columns = matrix[0].length;
    // Guarda el tama?o de la matriz para saber de que tama?o crear la que se
    // devuelve
    spEditor.putInt(MATRIX_ROWS_SIZE, rows).commit();
    spEditor.putInt(MATRIX_COLUMNS_SIZE, columns).commit();
    for (int i = 0; i < matrix.length; i++) {
      String temp = "";
      String key = FUNCTION_ROW + i;
      for (int j = 0; j < matrix[i].length; j++) {
        temp += matrix[i][j];
        if (j != matrix[i].length - 1) {
          temp += ",";
        }
      }
      spEditor.putString(key, temp).commit();
    }
  }

  public static void setVector(String[] vector) {
    spEditor.putInt(VECTOR_SIZE, vector.length).commit();
    String temp = "";
    for (int i = 0; i < vector.length; i++) {
      temp += vector[i];
      if (i != vector.length - 1)
        temp += ",";
    }
    spEditor.putString(FUNCTION_VECTOR, temp).commit();
  }

  public static void setInitialVector(String[] initialVector) {
    spEditor.putInt(VECTOR_SIZE, initialVector.length).commit();
    String temp = "";
    for (int i = 0; i < initialVector.length; i++) {
      temp += initialVector[i];
      if (i != initialVector.length - 1)
        temp += ",";
    }
    spEditor.putString(FUNCTION_INITIAL_VECTOR, temp).commit();
  }

  public static void setPoints(String[][] points) {
    int rows = points.length;
    spEditor.putInt(POINTS_SIZE, rows).commit();
    for (int i = 0; i < points.length; i++) {
      String temp = "";
      String key = POINTS_ROW + i;
      for (int j = 0; j < points[i].length; j++) {
        temp += points[i][j];
        if (j != points[i].length - 1) {
          temp += ",";
        }
      }
      spEditor.putString(key, temp).commit();
    }
  }
  
  public static void saveFx(String function) {
    spEditor.putString(FUNCTION_FX, function).commit();
  }

  public static void saveD1fx(String d1Function) {
    spEditor.putString(FUNCTION_D1FX, d1Function).commit();
  }

  public static void saveD2fx(String d2Function) {
    spEditor.putString(FUNCTION_D2FX, d2Function).commit();
  }

  public static void saveGx(String function) {
    spEditor.putString(FUNCTION_GX, function).commit();
  }

  public static void saveX0(String x0) {
    spEditor.putString(FUNCTION_X0, x0).commit();
  }

  public static void saveDelta(String delta) {
    spEditor.putString(FUNCTION_DELTA, delta).commit();
  }

  public static void saveMaxIterations(String iterations) {
    spEditor.putString(FUNCTION_MAX_ITERATIONS, iterations).commit();
  }

  public static void saveXi(String xi) {
    spEditor.putString(FUNCTION_XI, xi).commit();
  }

  public static void saveXs(String xs) {
    spEditor.putString(FUNCTION_XS, xs).commit();
  }

  public static void saveXa(String xa) {
    spEditor.putString(FUNCTION_XA, xa).commit();
  }

  public static void saveTolerance(String tolerance) {
    spEditor.putString(FUNCTION_TOLERANCE, tolerance).commit();
  }

  // --- Fetch methods ---

  // Devuelve una matriz de strings con los valores de una matriz previamente
  // guardada separando cada valor por comas
  public static String[][] getMatrix() {
    String[][] matrix = new String[sp.getInt(MATRIX_ROWS_SIZE, 0)][sp.getInt(
        MATRIX_COLUMNS_SIZE, 0)];
    for (int i = 0; i < matrix.length; i++) {
      String key = FUNCTION_ROW + i;
      String[] rows = sp.getString(key, null).split(",");
      for (int j = 0; j < matrix[0].length; j++) {
        matrix[i][j] = rows[j];
      }
    }
    return matrix;
  }

  public static String[] getVector() {
    String stringVector = sp.getString(FUNCTION_VECTOR, null);
    String[] vector = new String[sp.getInt(VECTOR_SIZE, 0)];
    if (stringVector == null)
      return vector;
    vector = sp.getString(FUNCTION_VECTOR, null).split(",");
    return vector;
  }

  public static String[] getInitialVector() {
    String stringVector = sp.getString(FUNCTION_INITIAL_VECTOR, null);
    String[] vector = new String[sp.getInt(VECTOR_SIZE, 0)];
    if (stringVector == null)
      return vector;
    vector = stringVector.split(",");
    return vector;
  }

  public static String[][] getInterpolationPoints() {
    String[][] points = new String[sp.getInt(POINTS_SIZE, 0)][2];
    for (int i = 0; i < points.length; i++) {
      String key = POINTS_ROW + i;
      String[] rows = sp.getString(key, null).split(",");
      for (int j = 0; j < points[0].length; j++) {
        points[i][j] = rows[j];
      }
    }
    return points;
  }
  
  public static String getFx() {
    return sp.getString(FUNCTION_FX, null);
  }

  public static String getD1fx() {
    return sp.getString(FUNCTION_D1FX, null);
  }

  public static String getD2fx() {
    return sp.getString(FUNCTION_D2FX, null);
  }

  public static String getGx() {
    return sp.getString(FUNCTION_GX, null);
  }

  public static String getX0() {
    return sp.getString(FUNCTION_X0, null);
  }

  public static String getDelta() {
    return sp.getString(FUNCTION_DELTA, null);
  }

  public static String getMaxIterations() {
    return sp.getString(FUNCTION_MAX_ITERATIONS, null);
  }

  public static String getXi() {
    return sp.getString(FUNCTION_XI, null);
  }

  public static String getXs() {
    return sp.getString(FUNCTION_XS, null);
  }

  public static String getXa() {
    return sp.getString(FUNCTION_XA, null);
  }

  public static String getTolerance() {
    return sp.getString(FUNCTION_TOLERANCE, null);
  }
}
