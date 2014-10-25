package co.edu.eafit.dis.numerical.methods;

import java.util.ArrayList;

import co.edu.eafit.dis.numerical.R;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class ResultsTable {
  
  private static Context c;
  private static ArrayList <Row> results;
  private static int columns;
  private static ResultsTable instance;
  
  private ResultsTable() {
    results = new ArrayList<Row>();
  }
  
  public static ResultsTable getInstance() {
    if (instance == null) instance = new ResultsTable();
    return instance;
  }
  
  public static void setUpNewTable(Context c, int columns) {
    ResultsTable.c = c;
    ResultsTable.columns = columns;
  }
  
  public static void addRow(Row row) {
    results.add(row);
  }
  
  public static void clearTable() {
    results.clear();
  }
  
  public static void setContext(Context c) {
    ResultsTable.c = c;
  }
  
  public static void setColumns(int colunms) {
    ResultsTable.columns = colunms;
  }
  
  public static ArrayList <Row> getResultsTable() {
    return results;
  }
  
  public static String [][] getStringMatrix() {
    int n = results.size();
    String [][] matrixResult = new String[n][columns];
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < columns; ++j) {
        matrixResult[i][j] = results.get(i).get(j);
      }
    }
    return matrixResult;
  }
  
  public class Row {
    private ArrayList <String> row;
    private int currentColumn;
    
    public Row() {
      row = new ArrayList <String>();
      currentColumn = 0;
    }
    
    public void addCell(String content) throws Exception {
      if (currentColumn >= columns) {
        throw new Exception(c.getResources()
            .getString(R.string.error_table_results_exceeded_columns));
      }
      row.add(content);
      currentColumn++;
    }
    
    public void clearRow() {
      row.clear();
    }
    
    public String get(int index) {
      return row.get(index);
    }
    
    public ArrayList <String> getRow() {
      return row;
    }

    public void setRow(ArrayList<String> row) {
      this.row = row;
    }
  }
}
