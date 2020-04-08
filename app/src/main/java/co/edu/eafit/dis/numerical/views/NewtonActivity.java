package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.R.layout;
import co.edu.eafit.dis.numerical.R.menu;
import co.edu.eafit.dis.numerical.methods.Newton;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;
import co.edu.eafit.dis.numerical.utils.InputChecker;
import co.edu.eafit.dis.numerical.utils.PreferencesManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewtonActivity extends Activity {

  // L?gica
  private Newton newton;

  // Vista
  private EditText inputFunction;
  private EditText inputDerivedFunction;
  private EditText inputX0;
  private EditText inputTol;
  private EditText inputMaxIterations;
  private Button calculateButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_newton);

    // Activar el bot?n de ir atr?s en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    newton = new Newton(this);

    inputFunction = (EditText) findViewById(R.id.input_function);
    inputDerivedFunction = (EditText) findViewById(R.id.input_derived_function);
    inputX0 = (EditText) findViewById(R.id.input_x0);
    inputTol = (EditText) findViewById(R.id.input_tol);
    inputMaxIterations = (EditText) findViewById(R.id.input_max_iterations);
    calculateButton = (Button) findViewById(R.id.calculate_button);

    calculateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        calculate();
      }
    });
    fillFieldsWithStoredData();
  }

  private void fillFieldsWithStoredData() {
    PreferencesManager.setup(this);
    inputFunction.setText(PreferencesManager.getFx());
    inputDerivedFunction.setText(PreferencesManager.getD1fx());
    inputX0.setText(PreferencesManager.getX0());
    inputTol.setText(PreferencesManager.getTolerance());
    inputMaxIterations.setText(PreferencesManager.getMaxIterations());
  }

  private void storeDataFromFields() {
    PreferencesManager.saveFx(inputFunction.getText().toString());
    PreferencesManager.saveD1fx(inputDerivedFunction.getText().toString());
    PreferencesManager.saveX0(inputX0.getText().toString());
    PreferencesManager.saveTolerance(inputTol.getText().toString());
    PreferencesManager.saveMaxIterations(inputMaxIterations.getText()
        .toString());
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

  private void calculate() {
    inputMaxIterations.setError(null);
    inputTol.setError(null);
    inputX0.setError(null);
    inputFunction.setError(null);
    inputDerivedFunction.setError(null);

    // Revisar si los datos ingresados est?n completos, se revisa desde el
    // final hasta el inicial, para obtener el focus en el campo inv?lido
    // de m?s arriba.
    String fieldRequired = getResources().getString(
        R.string.input_required_error);
    boolean fieldsCompleted = true;

    // Revisar si los campos est?n vac?os
    if (inputMaxIterations.getText().toString().trim().isEmpty()) {
      inputMaxIterations.setError(fieldRequired);
      inputMaxIterations.requestFocus();
      fieldsCompleted = false;
    }
    if (inputTol.getText().toString().trim().isEmpty()) {
      inputTol.setError(fieldRequired);
      inputTol.requestFocus();
      fieldsCompleted = false;
    }
    if (inputX0.getText().toString().trim().isEmpty()) {
      inputX0.setError(fieldRequired);
      inputX0.requestFocus();
      fieldsCompleted = false;
    }
    if (inputFunction.getText().toString().trim().isEmpty()) {
      inputFunction.setError(fieldRequired);
      inputFunction.requestFocus();
      fieldsCompleted = false;
    }
    if (inputDerivedFunction.getText().toString().trim().isEmpty()) {
      inputDerivedFunction.setError(fieldRequired);
      inputDerivedFunction.requestFocus();
      fieldsCompleted = false;
    }
    if (!fieldsCompleted)
      return;
    // Revisar que los que deber?an ser n?meros si lo sean
    String notANumberError = getResources().getString(
        R.string.not_a_number_error);
    boolean correctFields = true;
    if (!InputChecker.isDouble(inputX0.getText().toString())) {
      inputX0.setError(notANumberError);
      inputX0.requestFocus();
      correctFields = false;
    }
    FunctionsEvaluator evaluator = FunctionsEvaluator.getInstance(this);
    try {
      evaluator.setFunction(inputTol.getText().toString().trim());
    } catch (Exception e1) {
      inputTol.setError(notANumberError);
      inputTol.requestFocus();
      correctFields = false;
    }
    if (!correctFields)
      return;

    // Obtener valores de los inputs
    String function = inputFunction.getText().toString();
    String dervidedFunction = inputDerivedFunction.getText().toString();
    String x0Text = inputX0.getText().toString();
    String tolText = inputTol.getText().toString();
    int maxIterations = Integer.parseInt(inputMaxIterations.getText()
        .toString());

    double x0 = Double.valueOf(x0Text);
    // double tol = Double.valueOf(tolText);
    double tol = evaluator.calculate(1.0);

    // Try para revisar si la funci?n est? bien escrita
    try {
      newton.setFunction(function);
    } catch (Exception ex) {
      inputFunction.setError(ex.getMessage());
      inputFunction.requestFocus();
      return;
    }

    // Try para revisar si la funci?n derivada est? bien escrita
    try {
      newton.setDerivedFunction(dervidedFunction);
    } catch (Exception ex) {
      inputDerivedFunction.setError(ex.getMessage());
      inputDerivedFunction.requestFocus();
      return;
    }

    storeDataFromFields();

    Intent resultsIntent = new Intent(NewtonActivity.this,
        ResultsActivity.class);
    String methodNameKey = getResources().getString(
        R.string.text_key_method_name);
    String methodName = getResources()
        .getString(R.string.title_activity_newton);
    String resultsKey = getResources().getString(R.string.text_key_results);
    String methodTypeKey = getResources().getString(
        R.string.text_key_method_type);
    String resultsText;
    // Intentar evaluar con los datos recogidos
    try {
      double[] result = newton.evaluate(x0, tol, maxIterations);
      if (result[1] == -1) {
        // Se encontr? una ra?z exacta
        String rootFound = getString(R.string.root_found, result[0]);
        // Toast.makeText(this, rootFound, Toast.LENGTH_SHORT).show();
        resultsText = rootFound;
      } else {
        // Se encontr? una ra?z con una tolerancia
        String intervalFound = getString(R.string.root_found_tol, result[0],
            tol);
        // Toast.makeText(this, intervalFound, Toast.LENGTH_SHORT).show();
        resultsText = intervalFound;
      }
    } catch (Exception e) {
      // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
      resultsText = e.getMessage();
    }
    resultsIntent.putExtra(methodNameKey, methodName);
    resultsIntent.putExtra(resultsKey, resultsText);
    resultsIntent.putExtra(methodTypeKey,
        ResultsActivity.ONE_VARIABLE_EQUATIONS);
    NewtonActivity.this.startActivity(resultsIntent);
  }
}
