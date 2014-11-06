package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.R.id;
import co.edu.eafit.dis.numerical.R.layout;
import co.edu.eafit.dis.numerical.methods.GaussianElimination;
import co.edu.eafit.dis.numerical.utils.InputChecker;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class GaussianEliminationActivity extends Activity {

  private static final String TAG = GaussianEliminationActivity
      .class.getSimpleName();
  
  private GaussianElimination gaussianElimination;
  private Spinner gaussianSpinner;
  private int methodSelection;
  private TableLayout inputMatrix;
  private TableLayout inputVector;
  
  /* Contenidos */
  private int matrixSize;
  private EditText [][] inputMatrixEdits;
  private EditText [] inputVectorEdits;
  private double [][] matrix;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gaussian_elimination);
    
    //Activar el botón de ir atrás en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    gaussianSpinner = (Spinner) findViewById(R.id.gaussian_spinner);
    gaussianElimination = new GaussianElimination(this);
    
    //Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.gaussian_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapter
      .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    gaussianSpinner.setAdapter(adapter);
    gaussianSpinner.setOnItemSelectedListener(new AdapterView
        .OnItemSelectedListener() {
      /**
       * Called when a new item is selected (in the Spinner)
       */
      public void onItemSelected(AdapterView<?> parent, View view, 
                 int pos, long id) {
        //An spinnerItem was selected.
        //You can retrieve the selected item using
        //parent.getItemAtPosition(pos)                   
        methodSelection = pos;
      }
      public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing, just another required interface callback
      }
    }); // (optional)

    inputMatrix = (TableLayout) findViewById(R.id.input_matrix_table);
    inputVector = (TableLayout) findViewById(R.id.input_vector_table);

    matrixSize = getIntent().getExtras()
        .getInt(getResources().getString(R.string.text_key_matrix_size));
    inputMatrixEdits = new EditText[matrixSize][matrixSize];
    inputVectorEdits = new EditText[matrixSize];
    matrix = new double[matrixSize][matrixSize + 1];
    setUpViewWithInputMatrix(matrixSize);
    setUpViewWithInputVector(matrixSize);
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
  
  private void setUpViewWithInputMatrix(int matrixSize) {
    inputMatrix.removeAllViews();
    for (int i = 0; i < matrixSize; ++i) {
      TableRow row = new TableRow(GaussianEliminationActivity.this);
      row.setLayoutParams(new TableRow
          .LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
      for (int j = 0; j < matrixSize; ++j) {
        inputMatrixEdits[i][j] = new EditText(GaussianEliminationActivity.this);
        inputMatrixEdits[i][j].setInputType(InputType.TYPE_CLASS_NUMBER |
                                InputType.TYPE_NUMBER_FLAG_DECIMAL | 
                                InputType.TYPE_NUMBER_FLAG_SIGNED);
        inputMatrixEdits[i][j].setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
        inputMatrixEdits[i][j].setGravity(Gravity.CENTER);
        inputMatrixEdits[i][j].setHint(getResources()
            .getString(R.string.text_hint_input_matrix_index, i, j));
        if (j == 0) { //Si es la columna inicial entonces se añade una barra
          EditText separator = new EditText(GaussianEliminationActivity.this);
          separator.setLayoutParams(new TableRow.LayoutParams(1,
                                                 LayoutParams.WRAP_CONTENT));
          separator.setEnabled(false);
          separator.setBackgroundColor(Color.BLACK);
          row.addView(separator);
        }
        inputMatrixEdits[i][j].setTextColor(Color.BLACK);
        row.addView(inputMatrixEdits[i][j]);
        EditText separator = new EditText(GaussianEliminationActivity.this);
        separator.setLayoutParams(new TableRow.LayoutParams(1,
                                               LayoutParams.WRAP_CONTENT));
        separator.setEnabled(false);
        separator.setBackgroundColor(Color.BLACK);
        row.addView(separator);
      }
      inputMatrix.addView(row);
    }
  }
  
  private void setUpViewWithInputVector(int matrixSize) {
    inputVector.removeAllViews();
    TableRow row = new TableRow(GaussianEliminationActivity.this);
    row.setLayoutParams(new TableRow
        .LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                      TableRow.LayoutParams.WRAP_CONTENT));
    for (int j = 0; j < matrixSize; ++j) {
      inputVectorEdits[j] = new EditText(GaussianEliminationActivity.this);
      inputVectorEdits[j].setInputType(InputType.TYPE_CLASS_NUMBER |
                              InputType.TYPE_NUMBER_FLAG_DECIMAL | 
                              InputType.TYPE_NUMBER_FLAG_SIGNED);
      inputVectorEdits[j].setLayoutParams(new TableRow.LayoutParams(
                              TableRow.LayoutParams.WRAP_CONTENT,
                              TableRow.LayoutParams.WRAP_CONTENT));
      inputVectorEdits[j].setGravity(Gravity.CENTER);
      inputVectorEdits[j].setHint(getResources()
          .getString(R.string.text_hint_input_vector_index, j));
      if (j == 0) { //Si es la columna inicial entonces se añade una barra
        EditText separator = new EditText(GaussianEliminationActivity.this);
        separator.setLayoutParams(new TableRow.LayoutParams(1,
                                               LayoutParams.WRAP_CONTENT));
        separator.setEnabled(false);
        separator.setBackgroundColor(Color.BLACK);
        row.addView(separator);
      }
      inputVectorEdits[j].setTextColor(Color.BLACK);
      row.addView(inputVectorEdits[j]);
      EditText separator = new EditText(GaussianEliminationActivity.this);
      separator.setLayoutParams(new TableRow.LayoutParams(1,
                                             LayoutParams.WRAP_CONTENT));
      separator.setEnabled(false);
      separator.setBackgroundColor(Color.BLACK);
      row.addView(separator);
    }
    inputVector.addView(row);
  }
  
  public void calculate(View view) {
    boolean allCorrect = true;
    for (int i = 0; i < matrixSize; ++i) {
      for (int j = 0; j < matrixSize; ++j) {
        String inputText = inputMatrixEdits[i][j].getText().toString().trim();
        if (inputText.isEmpty()) {
          inputMatrixEdits[i][j].setError(getResources()
              .getString(R.string.input_required_error));
          allCorrect = false;
        }
        else if (!InputChecker.isDouble(inputText)){
          inputMatrixEdits[i][j].setError(getResources()
              .getString(R.string.not_a_number_error));
          allCorrect = false;
        }
        else {
          matrix[i][j] = Double.valueOf(inputText);
        }
      }
    }
    for (int j = 0; j < matrixSize; ++j) {
      String inputText = inputVectorEdits[j].getText().toString().trim();
      if (inputText.isEmpty()) {
        inputVectorEdits[j].setError(getResources()
            .getString(R.string.input_required_error));
        allCorrect = false;
      }
      else if (!InputChecker.isDouble(inputText)){
        inputVectorEdits[j].setError(getResources()
            .getString(R.string.not_a_number_error));
        allCorrect = false;
      }
      else {
        matrix[j][matrixSize] = Double.valueOf(inputText);
      }
    }
    
    if (!allCorrect) return;
    Log.i(TAG, matrix.toString());
    /*double[][] matrix = {{  1,  6,  -2,  3,  12},
                         { 14, 15,   2, -5,  32},
                         {  3,  4, -23,  2, -24},
                         {  1, -3,  -2, 16,  14}};*/
    Intent resultsIntent;
    String methodName;
    String methodNameKey = getResources()
        .getString(R.string.text_key_method_name);
    String methodTypeKey = getResources()
        .getString(R.string.text_key_method_type);
    double [] solution;
    switch (methodSelection) {
      case 0:
        solution = gaussianElimination.calculateSimpleGaussianElimiation(4, 
            matrix);
        resultsIntent = new Intent(GaussianEliminationActivity.this,
            ResultsMatrixActivity.class);
        methodName = getResources()
            .getString(R.string.title_activity_gaussian_elim_without);
        //String resultsKey = getResources().getString(R.string.text_key_results);
        //String resultsText;
        resultsIntent.putExtra(methodNameKey, methodName);
        //resultsIntent.putExtra(resultsKey, resultsText);
        resultsIntent.putExtra(methodTypeKey,
                               ResultsMatrixActivity.ELIMINATION_TYPE);
        GaussianEliminationActivity.this.startActivity(resultsIntent);
        break;
      case 1:
        solution = gaussianElimination
          .calculateGaussianEliminationPartialPivoting(4, matrix);
        resultsIntent = new Intent(GaussianEliminationActivity.this,
            ResultsMatrixActivity.class);
        methodNameKey = getResources()
            .getString(R.string.text_key_method_name);
        methodName = getResources()
            .getString(R.string.title_activity_gaussian_elim_partial);
        //String resultsKey = getResources().getString(R.string.text_key_results);
        //String resultsText;
        resultsIntent.putExtra(methodNameKey, methodName);
        //resultsIntent.putExtra(resultsKey, resultsText);
        resultsIntent.putExtra(methodTypeKey,
            ResultsMatrixActivity.ELIMINATION_TYPE);
        GaussianEliminationActivity.this.startActivity(resultsIntent);
        break;
      case 2:
        solution = gaussianElimination
          .calculateGaussianEliminationTotalPivoting(4, matrix);
        resultsIntent = new Intent(GaussianEliminationActivity.this,
            ResultsMatrixActivity.class);
        methodNameKey = getResources()
            .getString(R.string.text_key_method_name);
        methodName = getResources()
            .getString(R.string.title_activity_gaussian_elim_total);
        //String resultsKey = getResources().getString(R.string.text_key_results);
        //String resultsText;
        resultsIntent.putExtra(methodNameKey, methodName);
        //resultsIntent.putExtra(resultsKey, resultsText);
        resultsIntent.putExtra(methodTypeKey,
            ResultsMatrixActivity.ELIMINATION_TYPE);
        GaussianEliminationActivity.this.startActivity(resultsIntent);
        break;
      default:
        solution = new double[0];
        break;
    }                             
    for (int i = 0; i < solution.length; ++i) {
      Log.i(TAG, "X" + (i+1) + " = " + solution[i]);
    }
  }
}