package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.ResultsTable;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ResultsTableActivity extends Activity {
  
  private TextView methodTitle;
  private TableLayout resultsTableLayout;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results_table);
    
    //Activar el botón de ir atrás en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    methodTitle = (TextView) findViewById(R.id.method_title);
    resultsTableLayout = (TableLayout) findViewById(R.id.results_table_layout);
    
    String methodName = getIntent().getExtras().getString(getResources()
          .getString(R.string.text_key_method_name));
    String [][] matrix = ResultsTable.getStringMatrix();
    try {
      setUpFields(methodName, matrix);
    } catch (Exception e) {
      //Log.e("HOLA"S, "Error, no se puede crear la tabla");
    }
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
  
  public void setUpFields(String methodName, String [][] matrix) 
      throws Exception {
    methodTitle.setText(methodName);
    if (matrix.length == 0) {
      throw new Exception(getResources()
          .getString(R.string.error_table_results_empty));
    }
    fillTable(matrix.length, matrix[0].length, matrix);
  }
  
  public void fillTable(final int n, final int m, final String[][] matrix) {
    resultsTableLayout.removeAllViews();
    for (int i = 0; i < n; i++) {
      TableRow row = new TableRow(ResultsTableActivity.this);
      row.setLayoutParams(new TableRow
                          .LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                                        TableRow.LayoutParams.WRAP_CONTENT));
      for (int j = 0; j < m; j++) {
        EditText edit = new EditText(ResultsTableActivity.this);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER |
                          InputType.TYPE_NUMBER_FLAG_DECIMAL | 
                          InputType.TYPE_NUMBER_FLAG_SIGNED);
        edit.setLayoutParams(new TableRow.LayoutParams(
                              TableRow.LayoutParams.WRAP_CONTENT,
                              TableRow.LayoutParams.WRAP_CONTENT));
        edit.setGravity(Gravity.CENTER);
        
        edit.setText(matrix[i][j]);
        edit.setKeyListener(null);
        if (i == 0) {
          edit.setBackgroundColor(getResources()
              .getColor(R.color.header_table_results_row));
          edit.setTypeface(edit.getTypeface(), Typeface.BOLD);
        }
        else {
          if (i % 2 == 0) edit.setBackgroundColor(getResources()
                            .getColor(R.color.light_blue_table_results_row));
          else edit.setBackgroundColor(getResources()
                            .getColor(R.color.light_gray_table_results_row));
        }
        if (j == 0) { //Si es la columna inicial entonces se añade una barra
          EditText separator = new EditText(ResultsTableActivity.this);
          separator.setLayoutParams(new TableRow.LayoutParams(1,
                                                 LayoutParams.WRAP_CONTENT));
          separator.setEnabled(false);
          separator.setBackgroundColor(Color.BLACK);
          row.addView(separator);
        }
        edit.setTextColor(Color.BLACK);
        edit.setEnabled(false);
        row.addView(edit);
        EditText separator = new EditText(ResultsTableActivity.this);
        separator.setLayoutParams(new TableRow.LayoutParams(1,
                                               LayoutParams.WRAP_CONTENT));
        separator.setEnabled(false);
        separator.setBackgroundColor(Color.BLACK);
        row.addView(separator);
      }
      resultsTableLayout.addView(row);
    }
  }
}
