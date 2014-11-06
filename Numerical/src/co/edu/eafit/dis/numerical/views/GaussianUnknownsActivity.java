package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.utils.InputChecker;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.View;

public class GaussianUnknownsActivity extends Activity {
  
  private EditText numberOfUnknowns;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_gaussian_unknowns);
    
    //Activar el botón de ir atrás en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    numberOfUnknowns = (EditText) findViewById(R.id.number_of_unkwnowns);
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
  
  public void continueToMethods(View view) {
    String unknowns = numberOfUnknowns.getText().toString();
    if (unknowns.trim().isEmpty()) {
      numberOfUnknowns.setError(getResources()
          .getString(R.string.input_required_error));
      return;
    }
    if (!InputChecker.isInt(numberOfUnknowns.getText().toString())) {
      numberOfUnknowns.setError(getResources()
          .getString(R.string.not_a_number_error));
      return;
    }
    Intent intent = new Intent(GaussianUnknownsActivity.this,
        GaussianEliminationActivity.class);
    intent.putExtra(getResources().getString(R.string.text_key_matrix_size),
        Integer.parseInt(numberOfUnknowns.getText().toString()));
    GaussianUnknownsActivity.this.startActivity(intent);
  }
}