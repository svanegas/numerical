package co.edu.eafit.dis.numerical.utils;

import android.content.Context;
import android.content.SharedPreferences;

/** Clase utilizada para manejar las preferencias compartidas, funciones e 
 * información ingresadas por el usuario. (Busca éste que no deba reescribir)
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
  
  public static void setup(Context context) {
    sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    spEditor = sp.edit();
  }
  
  // --- Put methods ---
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
