package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.MultipleRoots;
import co.edu.eafit.dis.numerical.utils.InputChecker;
import co.edu.eafit.dis.numerical.utils.PreferencesManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MultipleRootsActivity extends Activity {

  private static final String TAG = MultipleRootsActivity.class.getSimpleName();

  // Lógica
  private MultipleRoots multipleRoots;

  // Vista
  private EditText inputFunction;
  private EditText input1DerivedFunction;
  private EditText input2DerivedFunction;
  private EditText inputX0;
  private EditText inputTol;
  private EditText inputMaxIterations;
  private Button calculateButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_multiple_roots);

    // Activar el botón de ir atrás en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    multipleRoots = new MultipleRoots(this);

    inputFunction = (EditText) findViewById(R.id.input_function);
    input1DerivedFunction = (EditText) findViewById(R.id.input_1derived_function);
    input2DerivedFunction = (EditText) findViewById(R.id.input_2derived_function);
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
    input1DerivedFunction.setText(PreferencesManager.getD1fx());
    input2DerivedFunction.setText(PreferencesManager.getD2fx());
    inputX0.setText(PreferencesManager.getX0());
    inputTol.setText(PreferencesManager.getTolerance());
    inputMaxIterations.setText(PreferencesManager.getMaxIterations());
  }

  private void storeDataFromFields() {
    PreferencesManager.saveFx(inputFunction.getText().toString());
    PreferencesManager.saveD1fx(input1DerivedFunction.getText().toString());
    PreferencesManager.saveD2fx(input2DerivedFunction.getText().toString());
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
    input1DerivedFunction.setError(null);
    input2DerivedFunction.setError(null);

    // Revisar si los datos ingresados están completos, se revisa desde el
    // final hasta el inicial, para obtener el focus en el campo inválido
    // de más arriba.
    String fieldRequired = getResources().getString(
        R.string.input_required_error);
    boolean fieldsCompleted = true;

    // Revisar si los campos están vacíos
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
    if (input2DerivedFunction.getText().toString().trim().isEmpty()) {
      input2DerivedFunction.setError(fieldRequired);
      input2DerivedFunction.requestFocus();
      fieldsCompleted = false;
    }
    if (input1DerivedFunction.getText().toString().trim().isEmpty()) {
      input1DerivedFunction.setError(fieldRequired);
      input1DerivedFunction.requestFocus();
      fieldsCompleted = false;
    }
    if (!fieldsCompleted)
      return;
    // Revisar que los que deberían ser números si lo sean
    String notANumberError = getResources().getString(
        R.string.not_a_number_error);
    boolean correctFields = true;
    if (!InputChecker.isDouble(inputX0.getText().toString())) {
      inputX0.setError(notANumberError);
      inputX0.requestFocus();
      correctFields = false;
    }
    if (!InputChecker.isDouble(inputTol.getText().toString())) {
      inputTol.setError(notANumberError);
      inputTol.requestFocus();
      correctFields = false;
    }
    if (!correctFields)
      return;

    // Obtener valores de los inputs
    String function = inputFunction.getText().toString();
    String derived1Function = input1DerivedFunction.getText().toString();
    String derived2Function = input2DerivedFunction.getText().toString();
    String x0Text = inputX0.getText().toString();
    String tolText = inputTol.getText().toString();
    int maxIterations = Integer.parseInt(inputMaxIterations.getText()
        .toString());

    double x0 = Double.valueOf(x0Text);
    double tol = Double.valueOf(tolText);

    // Try para revisar si la función está bien escrita
    try {
      multipleRoots.setFunction(function);
    } catch (Exception ex) {
      inputFunction.setError(ex.getMessage());
      inputFunction.requestFocus();
      return;
    }

    // Try para revisar si la función derivada está bien escrita
    try {
      multipleRoots.setFirstDerivate(derived1Function);
    } catch (Exception ex) {
      input1DerivedFunction.setError(ex.getMessage());
      input1DerivedFunction.requestFocus();
      return;
    }

    // Try para revisar si la función segunda derivada está bien escrita
    try {
      multipleRoots.setSecondDerivate(derived2Function);
    } catch (Exception ex) {
      input2DerivedFunction.setError(ex.getMessage());
      input2DerivedFunction.requestFocus();
      return;
    }

    storeDataFromFields();

    Intent resultsIntent = new Intent(MultipleRootsActivity.this,
        ResultsActivity.class);
    String methodNameKey = getResources().getString(
        R.string.text_key_method_name);
    String methodName = getResources().getString(
        R.string.title_activity_multiple_roots);
    String resultsKey = getResources().getString(R.string.text_key_results);
    String methodTypeKey = getResources().getString(
        R.string.text_key_method_type);
    String resultsText;
    // Intentar evaluar con los datos recogidos
    try {
      double[] result = multipleRoots.evaluate(x0, tol, maxIterations);
      if (result[1] == -1) {
        // Se encontró una raíz exacta
        String rootFound = getString(R.string.root_found, result[0]);
        // Toast.makeText(this, rootFound, Toast.LENGTH_SHORT).show();
        resultsText = rootFound;
      } else {
        // Se encontró una raíz con una tolerancia
        String intervalFound = getString(R.string.root_found_tol, result[0],
            tolText);
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
    MultipleRootsActivity.this.startActivity(resultsIntent);
  }
}
