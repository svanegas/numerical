package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.GaussianElimination;
import co.edu.eafit.dis.numerical.methods.IncrementalSearch;
import co.edu.eafit.dis.numerical.methods.Interpolation;
import co.edu.eafit.dis.numerical.methods.IterativeMethods;
import co.edu.eafit.dis.numerical.methods.LUFactorization;
import co.edu.eafit.dis.numerical.methods.VariableSolver;
import co.edu.eafit.dis.numerical.utils.InputChecker;
import co.edu.eafit.dis.numerical.utils.PreferencesManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class InterpolationActivity extends Activity {

  public static final int NEWTON_INTERPOLATION = 0;
  public static final int LAGRANGE_INTERPOLATION = 1;
  public static final int NEVILLE_INTERPOLATION = 2;

  /* Vistas */
  private Spinner methodSpinner;
  private TableLayout inputPoints;
  private EditText[][] inputPointsEdit;
  private TextView valueToEvaluateText;
  private EditText valueToEvaluate;

  /* Variables */
  private int numberOfPoints;
  private int methodSelection;
  private double[] xPoints;
  private double[] yPoints;
  private double nevilleValue;

  /* Métodos */
  private Interpolation interpolation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_interpolation);

    // Activar el botón de ir atrás en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    interpolation = new Interpolation(this);

    methodSpinner = (Spinner) findViewById(R.id.method_spinner);

    valueToEvaluateText = (TextView) findViewById(R.id.value_to_evaluate_text);
    valueToEvaluate = (EditText) findViewById(R.id.value_to_evaluate);

    numberOfPoints = getIntent().getExtras().getInt(
        getResources().getString(R.string.text_key_matrix_size));
    ArrayAdapter<CharSequence> adapter;

    adapter = ArrayAdapter.createFromResource(this,
        R.array.interpolation_methods_array,
        android.R.layout.simple_spinner_item);

    adapter
        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    methodSpinner.setAdapter(adapter);
    methodSpinner
        .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          /**
           * Called when a new item is selected (in the Spinner)
           */
          public void onItemSelected(AdapterView<?> parent, View view, int pos,
              long id) {
            // An spinnerItem was selected.
            // You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            methodSelection = pos;
            if (methodSelection == NEWTON_INTERPOLATION
                || methodSelection == LAGRANGE_INTERPOLATION) {
              valueToEvaluateText.setVisibility(View.GONE);
              valueToEvaluate.setVisibility(View.GONE);
            }
            else if (methodSelection == NEVILLE_INTERPOLATION) {
              valueToEvaluateText.setVisibility(View.VISIBLE);
              valueToEvaluate.setVisibility(View.VISIBLE);
            }
          }

          public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing, just another required interface callback
          }
        }); // (optional)

    inputPoints = (TableLayout) findViewById(R.id.input_points_table);

    inputPointsEdit = new EditText[numberOfPoints][2];

    setUpViewWithInputPoints(numberOfPoints);

    fillFieldsWithStoredData();
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

  private void setUpViewWithInputPoints(int n) {
    inputPoints.removeAllViews();
    TableRow headers = new TableRow(InterpolationActivity.this);
    headers
        .setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT));

    // Separador izquierdo
    EditText initialSeparator = new EditText(InterpolationActivity.this);
    initialSeparator.setLayoutParams(new TableRow.LayoutParams(1,
        LayoutParams.WRAP_CONTENT));
    initialSeparator.setEnabled(false);
    initialSeparator.setBackgroundColor(Color.BLACK);
    initialSeparator.setFocusable(false);
    headers.addView(initialSeparator);

    // Etiqueta central
    EditText xLabel = new EditText(InterpolationActivity.this);
    xLabel.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
    xLabel
        .setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT));
    xLabel.setText(getResources().getString(R.string.text_header_point_x));
    xLabel.setGravity(Gravity.CENTER);
    xLabel.setTextColor(Color.BLACK);
    xLabel.setBackgroundColor(getResources().getColor(
        R.color.header_table_results_row));
    xLabel.setEnabled(false);
    headers.addView(xLabel);

    // Separador central
    EditText initialSeparator2 = new EditText(InterpolationActivity.this);
    initialSeparator2.setLayoutParams(new TableRow.LayoutParams(1,
        LayoutParams.WRAP_CONTENT));
    initialSeparator2.setEnabled(false);
    initialSeparator2.setBackgroundColor(Color.BLACK);
    initialSeparator2.setFocusable(false);
    headers.addView(initialSeparator2);

    // Etiqueta Y
    EditText yLabel = new EditText(InterpolationActivity.this);
    yLabel.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
    yLabel
        .setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT));
    yLabel.setText(getResources().getString(R.string.text_header_point_y));
    yLabel.setGravity(Gravity.CENTER);
    yLabel.setTextColor(Color.BLACK);
    yLabel.setBackgroundColor(getResources().getColor(
        R.color.header_table_results_row));
    yLabel.setEnabled(false);
    headers.addView(yLabel);

    // Separador derecho
    EditText initialSeparator3 = new EditText(InterpolationActivity.this);
    initialSeparator3.setLayoutParams(new TableRow.LayoutParams(1,
        LayoutParams.WRAP_CONTENT));
    initialSeparator3.setEnabled(false);
    initialSeparator3.setBackgroundColor(Color.BLACK);
    initialSeparator3.setFocusable(false);
    headers.addView(initialSeparator3);
    inputPoints.addView(headers);
    for (int i = 0; i < n; ++i) {
      TableRow row = new TableRow(InterpolationActivity.this);
      row.setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.WRAP_CONTENT,
          TableRow.LayoutParams.WRAP_CONTENT));
      for (int j = 0; j < 2; ++j) {
        inputPointsEdit[i][j] = new EditText(InterpolationActivity.this);
        inputPointsEdit[i][j].setInputType(InputType.TYPE_CLASS_NUMBER
            | InputType.TYPE_NUMBER_FLAG_DECIMAL
            | InputType.TYPE_NUMBER_FLAG_SIGNED);
        inputPointsEdit[i][j].setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT));
        inputPointsEdit[i][j].setGravity(Gravity.CENTER);
        if (j == 0) { // Si es la columna inicial entonces se añade una barra
          inputPointsEdit[i][j].setHint(getResources().getString(
              R.string.text_header_x, i + 1));
          EditText separator = new EditText(InterpolationActivity.this);
          separator.setLayoutParams(new TableRow.LayoutParams(1,
              LayoutParams.WRAP_CONTENT));
          separator.setEnabled(false);
          separator.setBackgroundColor(Color.BLACK);
          separator.setFocusable(false);
          row.addView(separator);
        }
        else {
          inputPointsEdit[i][j].setHint(getResources().getString(
              R.string.text_header_y, i + 1));
        }
        inputPointsEdit[i][j].setTextColor(Color.BLACK);
        row.addView(inputPointsEdit[i][j]);
        EditText separator = new EditText(InterpolationActivity.this);
        separator.setLayoutParams(new TableRow.LayoutParams(1,
            LayoutParams.WRAP_CONTENT));
        separator.setEnabled(false);
        separator.setBackgroundColor(Color.BLACK);
        separator.setFocusable(false);
        row.addView(separator);
      }
      inputPoints.addView(row);
    }
  }

  private void fillFieldsWithStoredData() {
    PreferencesManager.setup(this);
    String[][] tempMatrix = PreferencesManager.getInterpolationPoints();

    for (int i = 0; i < Math.min(inputPointsEdit.length, tempMatrix.length); ++i) {
      for (int j = 0; j < inputPointsEdit[0].length; ++j) {
        inputPointsEdit[i][j].setText(tempMatrix[i][j]);
      }
    }
  }

  private void storeDataFromFields() {
    String[][] tempMatrix = new String[inputPointsEdit.length][inputPointsEdit[0].length];
    for (int i = 0; i < tempMatrix.length; i++) {
      for (int j = 0; j < tempMatrix[0].length; j++) {
        tempMatrix[i][j] = inputPointsEdit[i][j].getText().toString().trim();
      }
    }
    PreferencesManager.setPoints(tempMatrix);
  }

  private boolean isInputValid() {
    xPoints = new double[numberOfPoints];
    yPoints = new double[numberOfPoints];
    boolean allCorrect = true;

    for (int i = 0; i < numberOfPoints; ++i) {
      // Valores de X
      String inputText = inputPointsEdit[i][0].getText().toString().trim();
      if (inputText.isEmpty()) {
        inputPointsEdit[i][0].setError(getResources().getString(
            R.string.input_required_error));
        allCorrect = false;
      }
      else if (!InputChecker.isDouble(inputText)) {
        inputPointsEdit[i][0].setError(getResources().getString(
            R.string.not_a_number_error));
        allCorrect = false;
      }
      else {
        xPoints[i] = Double.valueOf(inputText);
      }

      // Valores de Y
      inputText = inputPointsEdit[i][1].getText().toString().trim();
      if (inputText.isEmpty()) {
        inputPointsEdit[i][1].setError(getResources().getString(
            R.string.input_required_error));
        allCorrect = false;
      }
      else if (!InputChecker.isDouble(inputText)) {
        inputPointsEdit[i][1].setError(getResources().getString(
            R.string.not_a_number_error));
        allCorrect = false;
      }
      else {
        yPoints[i] = Double.valueOf(inputText);
      }
    }

    // Valor inicial si es Neville
    if (methodSelection == NEVILLE_INTERPOLATION) {
      String inputText = valueToEvaluate.getText().toString().trim();
      if (inputText.isEmpty()) {
        valueToEvaluate.setError(getResources().getString(
            R.string.input_required_error));
        allCorrect = false;
      }
      else if (!InputChecker.isDouble(inputText)) {
        valueToEvaluate.setError(getResources().getString(
            R.string.not_a_number_error));
        allCorrect = false;
      }
      else nevilleValue = Double.parseDouble(inputText);
    }
    return allCorrect;
  }

  public void calculate(View view) {
    if (!isInputValid()) return;

    String methodName = null, resultText = null;
    int interpolationType = -1;
    String methodNameKey = getResources().getString(
        R.string.text_key_method_name);
    String methodTypeKey = getResources().getString(
        R.string.text_key_method_type);
    String interpolationTypeKey = getResources().getString(
        R.string.text_key_interpolation_method_type);
    String methodResult = getResources().getString(R.string.text_key_results);

    Intent resultsIntent = null;
    switch (methodSelection) {
      case 0:
        methodName = getResources().getString(
            R.string.text_newton_interpolation);
        interpolationType = NEWTON_INTERPOLATION;
        try {
          resultText = interpolation.calculateNewtonDividedDifferences(
              numberOfPoints, xPoints, yPoints);
        }
        catch (Exception e) {
          resultText = e.getMessage();
        }
        break;
      case 1:
        methodName = getResources().getString(
            R.string.text_lagrange_interpolation);
        interpolationType = LAGRANGE_INTERPOLATION;
        try {
          resultText = interpolation.calculateLagrange(numberOfPoints, xPoints,
              yPoints);
        }
        catch (Exception e) {
          resultText = e.getMessage();
        }
        break;
      case 2:
        methodName = getResources().getString(
            R.string.text_neville_interpolation);
        interpolationType = NEVILLE_INTERPOLATION;
        try {
          resultText = interpolation.calculateNeville(numberOfPoints, xPoints,
              yPoints, nevilleValue);
        }
        catch (Exception e) {
          resultText = e.getMessage();
        }
        break;
    }
    resultsIntent = new Intent(InterpolationActivity.this,
        ResultsActivity.class);
    resultsIntent.putExtra(methodNameKey, methodName);
    resultsIntent.putExtra(methodTypeKey, ResultsActivity.INTERPOLATION);
    resultsIntent.putExtra(interpolationTypeKey, interpolationType);
    resultsIntent.putExtra(methodResult, resultText);

    // Datos correctos, guardarlos en PreferencesManager
    storeDataFromFields();

    InterpolationActivity.this.startActivity(resultsIntent);
  }
}
