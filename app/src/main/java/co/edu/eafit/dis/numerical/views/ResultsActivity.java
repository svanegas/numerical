package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.FunctionsEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultsActivity extends Activity {

  private static final String TAG = ResultsActivity.class.getName();
      
  public static final int ONE_VARIABLE_EQUATIONS = 1;
  public static final int SYSTEMS_OF_EQUATIONS = 2;
  public static final int ITERATIVE_METHODS = 3;
  public static final int INTERPOLATION = 4;
  
  /* Vistas */
  private TextView methodTitle;
  private TextView results;
  private Button showTableButton;
  private LinearLayout evaluatorLayout;
  private EditText evaluatorValue;
  private TextView evaluatorResult;
  
  /* Variables */
  private FunctionsEvaluator functionEvaluator = null;
  private String methodName;
  private int methodType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results);

    // Activar el bot?n de ir atr?s en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    methodTitle = (TextView) findViewById(R.id.method_title);
    results = (TextView) findViewById(R.id.results_text_view);
    showTableButton = (Button) findViewById(R.id.show_table_button);
    evaluatorLayout = (LinearLayout) findViewById(R.id.evaluator_layout);
    evaluatorValue = (EditText) findViewById(R.id.value_to_evaluate);
    evaluatorResult = (TextView) findViewById(R.id.evaluation_result);
    
    evaluatorValue.addTextChangedListener(new TextWatcher() {
      
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        double value;
        try {
          value = Double.parseDouble(s.toString());
        }
        catch (Exception e) {
          evaluatorResult.setText("");
          return;
        }
        functionEvaluator = FunctionsEvaluator.getInstance(ResultsActivity.this);
        double result = functionEvaluator.calculate(value);
        evaluatorResult.setText(String.format("%1.5E", result));
        Log.i(TAG, "Evaluo en " + functionEvaluator.getFunction() + " y queda " + result);
      }
      
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }
      
      @Override
      public void afterTextChanged(Editable s) {
      }
    });
    
    // Extras que recibe la actividad, nombre del m?todo, resultados y
    // tipo de m?todo para saber qu? Actividad instanciar al mostrar la tabla
    methodName = getIntent().getExtras().getString(
        getResources().getString(R.string.text_key_method_name));
    String resultsText = getIntent().getExtras().getString(
        getResources().getString(R.string.text_key_results));
    methodType = getIntent().getExtras().getInt(
        getResources().getString(R.string.text_key_method_type));
    setUpResults(methodName, resultsText);
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

  public void showResultsTable(View v) {
    Intent resultsTableIntent;
    String gaussianMethodTypeKey = getResources().getString(
        R.string.text_key_gaussian_method_type);
    switch (methodType) {
      case ONE_VARIABLE_EQUATIONS:
        resultsTableIntent = new Intent(ResultsActivity.this,
            ResultsTableActivity.class);
        break;
      case SYSTEMS_OF_EQUATIONS:
        int gaussianType = getIntent().getExtras().getInt(
            getResources().getString(R.string.text_key_gaussian_method_type));
        resultsTableIntent = new Intent(ResultsActivity.this,
            ResultsMatrixActivity.class);
        resultsTableIntent.putExtra(gaussianMethodTypeKey, gaussianType);
        break;
      case ITERATIVE_METHODS:
        resultsTableIntent = new Intent(ResultsActivity.this,
            ResultsTableActivity.class);
        break;
      case INTERPOLATION:
        resultsTableIntent = new Intent(ResultsActivity.this,
            ResultsTableActivity.class);
        break;
      default:
        resultsTableIntent = new Intent();
    }
    String methodNameKey = getResources().getString(
        R.string.text_key_method_name);
    resultsTableIntent.putExtra(methodNameKey, methodName);
    ResultsActivity.this.startActivity(resultsTableIntent);
  }

  public void setUpResults(String methodName, String resultsText) {
    methodTitle.setText(methodName);
    results.setText(resultsText);
    if (methodType == ResultsActivity.INTERPOLATION) {
      String interpolationTypeKey = getResources().getString(
          R.string.text_key_interpolation_method_type);
      if (getIntent().getExtras().getInt(interpolationTypeKey)
          == InterpolationActivity.LAGRANGE_INTERPOLATION) {
        showTableButton.setVisibility(View.GONE);
      }
      if (getIntent().getExtras().getInt(interpolationTypeKey)
          != InterpolationActivity.NEVILLE_INTERPOLATION)
        evaluatorLayout.setVisibility(View.VISIBLE);
      else evaluatorLayout.setVisibility(View.GONE);
    }
    else evaluatorLayout.setVisibility(View.GONE);
  }
}
