package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.MainActivity;
import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.IncrementalSearch;
import co.edu.eafit.dis.numerical.utils.InputChecker;
import co.edu.eafit.dis.numerical.utils.PreferencesManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IncrementalSearchActivity extends Activity {

  // Lógica
  private IncrementalSearch incremental;

  // Vista
  private EditText inputFunction;
  private EditText inputX0;
  private EditText inputDelta;
  private EditText inputMaxIterations;
  private Button calculateButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_incremental_search);

    // Activar el botón de ir atrás en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    incremental = new IncrementalSearch(this);

    inputFunction = (EditText) findViewById(R.id.input_function);
    inputX0 = (EditText) findViewById(R.id.input_x0);
    inputDelta = (EditText) findViewById(R.id.input_delta);
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
    inputX0.setText(PreferencesManager.getX0());
    inputDelta.setText(PreferencesManager.getDelta());
    inputMaxIterations.setText(PreferencesManager.getMaxIterations());
  }

  private void storeDataFromFields() {
    PreferencesManager.saveFx(inputFunction.getText().toString());
    PreferencesManager.saveX0(inputX0.getText().toString());
    PreferencesManager.saveDelta(inputDelta.getText().toString());
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
    inputDelta.setError(null);
    inputX0.setError(null);
    inputFunction.setError(null);

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
    if (inputDelta.getText().toString().trim().isEmpty()) {
      inputDelta.setError(fieldRequired);
      inputDelta.requestFocus();
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
    if (!InputChecker.isDouble(inputDelta.getText().toString())) {
      inputDelta.setError(notANumberError);
      inputDelta.requestFocus();
      correctFields = false;
    }
    if (!correctFields)
      return;

    // Obtener valores de los inputs
    String function = inputFunction.getText().toString();
    String x0Text = inputX0.getText().toString();
    String deltaText = inputDelta.getText().toString();
    int maxIterations = Integer.parseInt(inputMaxIterations.getText()
        .toString());

    double x0 = Double.valueOf(x0Text);
    double delta = Double.valueOf(deltaText);

    // Try para revisar si la función está bien escrita
    try {
      incremental.setFunction(function);
    } catch (Exception ex) {
      inputFunction.setError(ex.getMessage());
      inputFunction.requestFocus();
      return;
    }

    // Datos correctos, guardarlos en PreferencesManager
    storeDataFromFields();

    Intent resultsIntent = new Intent(IncrementalSearchActivity.this,
        ResultsActivity.class);
    String methodNameKey = getResources().getString(
        R.string.text_key_method_name);
    String methodName = getResources().getString(
        R.string.title_activity_incremental_search);
    String resultsKey = getResources().getString(R.string.text_key_results);
    String methodTypeKey = getResources().getString(
        R.string.text_key_method_type);
    String resultsText;
    // Intentar evaluar con los datos recogidos
    try {
      double[] result = incremental.evaluate(x0, delta, maxIterations);
      if (result[0] == result[1]) {
        // Se encontró una raíz
        String rootFound = getString(R.string.root_found, result[0]);
        // Toast.makeText(this, rootFound, Toast.LENGTH_SHORT).show();
        resultsText = rootFound;
      } else {
        // Se encontró un intervalo
        String intervalFound = getString(R.string.interval_root_found,
            result[0], result[1]);
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
    IncrementalSearchActivity.this.startActivity(resultsIntent);
  }
}
