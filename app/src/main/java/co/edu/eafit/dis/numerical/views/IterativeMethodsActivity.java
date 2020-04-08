package co.edu.eafit.dis.numerical.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.GaussianElimination;
import co.edu.eafit.dis.numerical.methods.IterativeMethods;
import co.edu.eafit.dis.numerical.methods.LUFactorization;
import co.edu.eafit.dis.numerical.methods.VariableSolver;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;
import co.edu.eafit.dis.numerical.utils.InputChecker;
import co.edu.eafit.dis.numerical.utils.PreferencesManager;

public class IterativeMethodsActivity extends Activity {

  public static final int JACOBI_METHOD = 1;
  public static final int GAUSS_SEIDEL_METHOD = 2;

  /* Vistas */
  private TextView methodNameView;
  private TableLayout itialValuesTable;
  private EditText[] inputInitialEdits;
  private EditText iterationsEdit;
  private EditText toleranceEdit;
  private EditText alphaEdit;

  /* Variables */
  private String methodName;
  private int methodType;
  private int matrixSize;
  private double[] initialValues;
  private FunctionsEvaluator evaluator;

  /* M?todos */
  private IterativeMethods iterativeMethods;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_iterative_methods);

    // Activar el bot?n de ir atr?s en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    methodName = getIntent().getExtras().getString(
        getResources().getString(R.string.text_key_method_name));

    methodType = getIntent().getExtras().getInt(
        getResources().getString(R.string.text_key_iterative_method_type));

    matrixSize = getIntent().getExtras().getInt(
        getResources().getString(R.string.text_key_matrix_size));

    methodNameView = (TextView) findViewById(R.id.subsection_title);
    itialValuesTable = (TableLayout) findViewById(R.id.input_initial_values);
    iterationsEdit = (EditText) findViewById(R.id.input_max_iterations);
    toleranceEdit = (EditText) findViewById(R.id.input_tol);
    alphaEdit = (EditText) findViewById(R.id.input_alpha);

    inputInitialEdits = new EditText[matrixSize];
    setUpViewWithMethodName(methodName);
    setUpViewInitialValuesTable(matrixSize);

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

  private void setUpViewWithMethodName(String methodName) {
    methodNameView.setText(methodName);
  }

  private void setUpViewInitialValuesTable(int unknowns) {
    itialValuesTable.removeAllViews();
    TableRow row = new TableRow(IterativeMethodsActivity.this);
    row.setLayoutParams(new TableRow.LayoutParams(
        TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
    for (int j = 0; j < matrixSize; ++j) {
      inputInitialEdits[j] = new EditText(IterativeMethodsActivity.this);
      inputInitialEdits[j].setInputType(InputType.TYPE_CLASS_NUMBER
          | InputType.TYPE_NUMBER_FLAG_DECIMAL
          | InputType.TYPE_NUMBER_FLAG_SIGNED);
      inputInitialEdits[j].setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.WRAP_CONTENT,
          TableRow.LayoutParams.WRAP_CONTENT));
      inputInitialEdits[j].setGravity(Gravity.CENTER);
      inputInitialEdits[j].setHint(getResources().getString(
          R.string.text_hint_initial_value, j + 1));
      if (j == 0) { // Si es la columna inicial entonces se a?ade una barra
        EditText separator = new EditText(IterativeMethodsActivity.this);
        separator.setLayoutParams(new TableRow.LayoutParams(1,
            LayoutParams.WRAP_CONTENT));
        separator.setEnabled(false);
        separator.setBackgroundColor(Color.BLACK);
        separator.setFocusable(false);
        row.addView(separator);
      }
      inputInitialEdits[j].setTextColor(Color.BLACK);
      row.addView(inputInitialEdits[j]);
      EditText separator = new EditText(IterativeMethodsActivity.this);
      separator.setLayoutParams(new TableRow.LayoutParams(1,
          LayoutParams.WRAP_CONTENT));
      separator.setEnabled(false);
      separator.setBackgroundColor(Color.BLACK);
      separator.setFocusable(false);
      row.addView(separator);
    }
    itialValuesTable.addView(row);
  }

  private void fillFieldsWithStoredData() {
    PreferencesManager.setup(this);
    toleranceEdit.setText(PreferencesManager.getTolerance());
    iterationsEdit.setText(PreferencesManager.getMaxIterations());

    String[] initialVector = PreferencesManager.getInitialVector();

    for (int i = 0; i < Math
        .min(inputInitialEdits.length, initialVector.length); ++i) {
      inputInitialEdits[i].setText(initialVector[i]);
    }
  }

  private void storeDataFromFields() {
    PreferencesManager.saveTolerance(toleranceEdit.getText().toString());
    PreferencesManager.saveMaxIterations(iterationsEdit.getText().toString());

    String[] initialVector = new String[matrixSize];
    for (int i = 0; i < matrixSize; i++) {
      initialVector[i] = inputInitialEdits[i].getText().toString().trim();
    }
    PreferencesManager.setInitialVector(initialVector);
  }

  private boolean isInputValid() {
    initialValues = new double[matrixSize];
    boolean allCorrect = true;
    for (int j = 0; j < matrixSize; ++j) {
      String inputText = inputInitialEdits[j].getText().toString().trim();
      if (inputText.isEmpty()) {
        inputInitialEdits[j].setError(getResources().getString(
            R.string.input_required_error));
        inputInitialEdits[j].requestFocus();
        allCorrect = false;
      } else if (!InputChecker.isDouble(inputText)) {
        inputInitialEdits[j].setError(getResources().getString(
            R.string.not_a_number_error));
        inputInitialEdits[j].requestFocus();
        allCorrect = false;
      } else
        initialValues[j] = Double.valueOf(inputText);
    }

    iterationsEdit.setError(null);
    toleranceEdit.setError(null);
    alphaEdit.setError(null);

    // Revisar si los datos ingresados est?n completos, se revisa desde el
    // final hasta el inicial, para obtener el focus en el campo inv?lido
    // de m?s arriba.
    String fieldRequired = getResources().getString(
        R.string.input_required_error);
    boolean fieldsCompleted = true;

    // Revisar si los campos est?n vac?os
    if (iterationsEdit.getText().toString().trim().isEmpty()) {
      iterationsEdit.setError(fieldRequired);
      iterationsEdit.requestFocus();
      fieldsCompleted = false;
    }
    if (toleranceEdit.getText().toString().trim().isEmpty()) {
      toleranceEdit.setError(fieldRequired);
      toleranceEdit.requestFocus();
      fieldsCompleted = false;
    }

    allCorrect &= fieldsCompleted;

    // Revisar que los que deber?an ser n?meros si lo sean
    String notANumberError = getResources().getString(
        R.string.not_a_number_error);
    boolean correctFields = true;
    evaluator = FunctionsEvaluator.getInstance(this);
    try {
      evaluator.setFunction(toleranceEdit.getText().toString().trim());
    } catch (Exception e1) {
      toleranceEdit.setError(notANumberError);
      toleranceEdit.requestFocus();
      correctFields = false;
    }
    if (!alphaEdit.getText().toString().isEmpty()
        && !InputChecker.isDouble(alphaEdit.getText().toString())) {
      alphaEdit.setError(notANumberError);
      alphaEdit.requestFocus();
      correctFields = false;
    }
    allCorrect &= correctFields;
    return allCorrect;
  }

  public void calculate(View view) {
    if (!isInputValid())
      return;

    // Datos correctos, guardarlos en PreferencesManager
    storeDataFromFields();

    String methodNameKey = getResources().getString(
        R.string.text_key_method_name);
    String methodTypeKey = getResources().getString(
        R.string.text_key_method_type);
    String iterativeTypeKey = getResources().getString(
        R.string.text_key_iterative_method_type);
    String matrixSizeKey = getResources().getString(
        R.string.text_key_matrix_size);
    String methodResultKey = getResources()
        .getString(R.string.text_key_results);

    int iterations = Integer.parseInt(iterationsEdit.getText().toString()
        .trim());
    double tolerance = evaluator.calculate(1.0);
    double alpha = (alphaEdit.getText().length() == 0) ? 1.0 : Double
        .parseDouble(alphaEdit.getText().toString().trim());
    double[] solution = null;

    Intent resultsIntent = null;
    String resultsText = null;
    iterativeMethods = new IterativeMethods(this);
    resultsIntent = new Intent(IterativeMethodsActivity.this,
        ResultsActivity.class);
    resultsIntent.putExtra(iterativeTypeKey, methodType);
    resultsIntent.putExtra(matrixSizeKey, matrixSize);
    resultsIntent.putExtra(methodNameKey, methodName);
    resultsIntent.putExtra(methodTypeKey, ResultsActivity.ITERATIVE_METHODS);
    try {
      if (methodType == IterativeMethodsActivity.JACOBI_METHOD) {
        solution = iterativeMethods.jacobi(IterativeMethods.matrix,
            IterativeMethods.vector, matrixSize, initialValues, iterations,
            tolerance, alpha);
      } else if (methodType == IterativeMethodsActivity.GAUSS_SEIDEL_METHOD) {
        solution = iterativeMethods.gaussSeidel(IterativeMethods.matrix,
            IterativeMethods.vector, matrixSize, initialValues, iterations,
            tolerance, alpha);
      }
      // Se encontr? una soluci?n con una tolerancia
      String stringSolution = VariableSolver.getStringSolution(solution);
      resultsText = stringSolution;
    } catch (Exception e) {
      resultsText = e.getMessage();
    }

    resultsIntent.putExtra(methodResultKey, resultsText);
    IterativeMethodsActivity.this.startActivity(resultsIntent);
  }
}
