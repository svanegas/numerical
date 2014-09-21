package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.R.layout;
import co.edu.eafit.dis.numerical.R.menu;
import co.edu.eafit.dis.numerical.methods.FixedPoint;
import co.edu.eafit.dis.numerical.utils.InputChecker;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FixedPointActivity extends Activity {

  //L�gica
  private FixedPoint setPoint;
  
  // Vista
  private EditText inputFunction;
  private EditText inputGfunction;
  private EditText inputXa;
  private EditText inputTol;
  private EditText inputMaxIterations;
  private Button calculateButton; 
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fixed_point);
    
    //Activar el bot�n de ir atr�s en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);
    
    setPoint = new FixedPoint(this);
    
    inputFunction = (EditText) findViewById(R.id.input_function);
    inputGfunction = (EditText) findViewById(R.id.input_gx_function);
    inputXa = (EditText) findViewById(R.id.input_xa);
    inputTol = (EditText) findViewById(R.id.input_tol);
    inputMaxIterations = (EditText) findViewById(R.id.input_max_iterations);
    calculateButton = (Button) findViewById(R.id.calculate_button);
    
    calculateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        calculate();
      }
    });   
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
    inputXa.setError(null);
    inputFunction.setError(null);
    inputGfunction.setError(null);
    
    //Revisar si los datos ingresados est�n completos, se revisa desde el
    //final hasta el inicial, para obtener el focus en el campo inv�lido
    //de m�s arriba.
    String fieldRequired = getResources()
            .getString(R.string.input_required_error);
    boolean fieldsCompleted = true;
    
    //Revisar si los campos est�n vac�os
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
    if (inputXa.getText().toString().trim().isEmpty()) {
      inputXa.setError(fieldRequired);
      inputXa.requestFocus();
      fieldsCompleted = false;
    } 
    if (inputFunction.getText().toString().trim().isEmpty()) {
      inputFunction.setError(fieldRequired);
      inputFunction.requestFocus();
      fieldsCompleted = false;
    }
    if (inputGfunction.getText().toString().trim().isEmpty()) {
        inputGfunction.setError(fieldRequired);
        inputGfunction.requestFocus();
        fieldsCompleted = false;
    }
    if (!fieldsCompleted) return; 
    //Revisar que los que deber�an ser n�meros si lo sean
    String notANumberError = getResources()
            .getString(R.string.not_a_number_error);
    boolean correctFields = true;
    if (!InputChecker.isDouble(inputXa.getText().toString())) {
      inputXa.setError(notANumberError);
      inputXa.requestFocus();
      correctFields = false;
    } 
    if (!InputChecker.isDouble(inputTol.getText().toString())) {
      inputTol.setError(notANumberError);
      inputTol.requestFocus();
      correctFields = false;
    }
    if (!correctFields) return;
    
    //Obtener valores de los inputs
    String function = inputFunction.getText().toString();
    String gFunction = inputGfunction.getText().toString();
    String xaText = inputXa.getText().toString();
    String tolText = inputTol.getText().toString();
    int maxIterations = Integer.parseInt(inputMaxIterations
                    .getText().toString());
    
    double xa = Double.valueOf(xaText);
    double tol = Double.valueOf(tolText);
    
    //Try para revisar si la funci�n est� bien escrita
    try {
      setPoint.setFunction(function);
    } catch (Exception ex) {
      inputFunction.setError(ex.getMessage());
      inputFunction.requestFocus();
      return;
    }
    
    //Try para revisar si la funci�n g est� bien escrita
    try {
      setPoint.setGfunction(gFunction);
    } catch (Exception ex) {
      inputGfunction.setError(ex.getMessage());
      inputGfunction.requestFocus();
      return;
    }
    
    //Intentar evaluar con los datos recogidos
    try {
      double [] result = setPoint.evaluate(xa, tol,
                            maxIterations);
      if (result[1] == -1) {
        //Se encontr� una ra�z exacta
        String rootFound = getString(R.string.root_found, result[0]); 
        Toast.makeText(this, rootFound, Toast.LENGTH_SHORT).show();
      }
      else {
        //Se encontr� una ra�z con una tolerancia
        String intervalFound = getString(R.string.root_found_tol,
                                         result[0],
                                         tolText); 
        Toast.makeText(this, intervalFound, Toast.LENGTH_SHORT).show();
      }
    } catch (Exception e) {
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }
}