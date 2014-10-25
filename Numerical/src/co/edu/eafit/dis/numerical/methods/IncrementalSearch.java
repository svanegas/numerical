package co.edu.eafit.dis.numerical.methods;

import android.content.Context;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;

public class IncrementalSearch {

  private FunctionsEvaluator functionEvaluator = null;
  private Context c;
  private ResultsTable results;
  
  public IncrementalSearch(Context c) {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    results = ResultsTable.getInstance();
    ResultsTable.setUpNewTable(c, 3);
  }
  
  public IncrementalSearch(Context c, String function) throws Exception {
    functionEvaluator = FunctionsEvaluator.getInstance(c);
    this.c = c;
    results = ResultsTable.getInstance();
    ResultsTable.setUpNewTable(c, 3);
    try {
      this.setFunction(function);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
  
  public double [] evaluate(double x0, double delta, double nIter) 
      throws Exception {
    //Se inicializa la tabla de resultados
    ResultsTable.clearTable();
    ResultsTable.Row headers = results.new Row();
    headers.addCell(c.getResources()
        .getString(R.string.text_results_table_iteration));
    headers.addCell(c.getResources()
        .getString(R.string.text_results_table_x_value));
    headers.addCell(c.getResources()
        .getString(R.string.text_results_table_fxi_value));
    ResultsTable.addRow(headers);
    //interval contiene el menor, mayor del intervalo donde hay una raíz.
    //Si son iguales los valores de interval quiere decir que en ese valor
    //exacto hay una raíz.
    double[] interval = new double[2];
    double fx0 = functionEvaluator.calculate(x0);
    ResultsTable.Row firstRow = results.new Row();
    firstRow.addCell("0"); //Se agrega la primera iteración (0)
    firstRow.addCell(String.valueOf(x0)); //Se agrega el valor inicial
    firstRow.addCell(String.valueOf(fx0)); //Se agrega el valor inicial evaluado
    ResultsTable.addRow(firstRow);
    if (fx0 == 0.0) {
      interval[0] = interval[1] = x0;
      //Hay una raíz en x0
    }
    else {
      double x1 = x0 + delta;
      int currentIteration = 1;
      double fx1 = functionEvaluator.calculate(x1);
      while (fx0 * fx1 > 0.0 && currentIteration <= nIter) {
        ResultsTable.Row row = results.new Row();
        row.addCell(String.valueOf(currentIteration));
        row.addCell(String.valueOf(x1));
        row.addCell(String.valueOf(fx1));
        ResultsTable.addRow(row);
        x0 = x1;
        fx0 = fx1;
        x1 = x0 + delta;
        fx1 = functionEvaluator.calculate(x1);
        currentIteration++;
      }
      if (fx1 == 0.0) {
        interval[0] = interval[1] = x1; //x1 una raíz
        ResultsTable.Row finalRow = results.new Row();
        finalRow.addCell(String.valueOf(currentIteration));
        finalRow.addCell(String.valueOf(x1));
        finalRow.addCell(String.valueOf(fx1));
        ResultsTable.addRow(finalRow);
      }
      else if (fx0 * fx1 < 0) {
        interval[0] = x0;
        interval[1] = x1;
        //Hay una raíz desde x0 hasta x1
        ResultsTable.Row finalRow = results.new Row();
        finalRow.addCell(String.valueOf(currentIteration));
        finalRow.addCell(String.valueOf(x1));
        finalRow.addCell(String.valueOf(fx1));
        ResultsTable.addRow(finalRow);
      }
      else {
        String methodName = c.getResources()
            .getString(R.string.title_activity_incremental_search);
        String errorMessage = c.getString(
              R.string.error_exceeded_iterations,
              methodName); 
        throw new Exception(errorMessage);
      }
    }
    return interval;
  }
  
  public void setFunction(String function) throws Exception {
    try {
      functionEvaluator.setFunction(function);
    } catch (Exception e) {
      throw new Exception(e.getMessage());
    }
  }
  
  public ResultsTable getResults() {
    return results;
  }
}
