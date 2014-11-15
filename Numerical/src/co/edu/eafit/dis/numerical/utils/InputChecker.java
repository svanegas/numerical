package co.edu.eafit.dis.numerical.utils;

import android.util.Log;

public class InputChecker {

  private static final String TAG = FunctionsEvaluator.class.getName();

  public static boolean isDouble(String s) {
    try {
      double d = Double.valueOf(s);
      return true;
    } catch (NumberFormatException e) {
      Log.e(TAG, "Could not convert from string " + s + " to double");
      return false;
    }
  }

  public static boolean isInt(String s) {
    try {
      int i = Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      Log.e(TAG, "Could not convert from string " + s + " to int");
      return false;
    }
  }
}
