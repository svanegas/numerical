package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends Activity {

  public static final int ONE_VARIABLE_EQUATIONS = 1;
  public static final int SYSTEMS_OF_EQUATIONS = 2;
  public static final int ITERATIVE_METHODS = 3;
  private TextView methodTitle;
  private TextView results;
  private String methodName;
  private int methodType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results);

    // Activar el botón de ir atrás en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    methodTitle = (TextView) findViewById(R.id.method_title);
    results = (TextView) findViewById(R.id.results_text_view);

    // Extras que recibe la actividad, nombre del método, resultados y
    // tipo de método para saber qué Actividad instanciar al mostrar la tabla
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
  }
}
