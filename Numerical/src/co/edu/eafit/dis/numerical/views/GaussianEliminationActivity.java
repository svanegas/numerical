package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.R.id;
import co.edu.eafit.dis.numerical.R.layout;
import co.edu.eafit.dis.numerical.methods.GaussianElimination;
import android.app.Activity;
<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> 82cefd72a17143854e2f2919429c8b7a19397e32
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;

public class GaussianEliminationActivity extends Activity {

<<<<<<< HEAD
  private static final String TAG = GaussianEliminationActivity
      .class.getSimpleName();
  
  private GaussianElimination gaussianElimination;
  private Spinner gaussianSpinner;
  private int methodSelection;
  
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
  

  public void onClick(View view){
    double[][] matrix = {{  1,  6,  -2,  3,  12},
                         { 14, 15,   2, -5,  32},
                         {  3,  4, -23,  2, -24},
                         {  1, -3,  -2, 16,  14}};
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
=======
	private GaussianElimination gaussianElimination;
	private Spinner gaussianSpinner;
	private int methodSelection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gaussian_elimination);
		gaussianSpinner = (Spinner) findViewById(R.id.gaussian_spinner);
		gaussianElimination = new GaussianElimination();
		
	    //Activar el botón de ir atrás en el action bar
	    getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.gaussian_array, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		gaussianSpinner.setAdapter(adapter);
		
        gaussianSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           /**
            * Called when a new item is selected (in the Spinner)
            */
            public void onItemSelected(AdapterView<?> parent, View view, 
                       int pos, long id) {
                   // An spinnerItem was selected. You can retrieve the selected item using
                   // parent.getItemAtPosition(pos)                   
                   methodSelection = pos;
               }

               public void onNothingSelected(AdapterView<?> parent) {
                   // Do nothing, just another required interface callback
               }

        }); // (optional)
	}
	
	public void onClick(View view){
		
	       double[][] matrix = {{ 14,  6,  -2,  3,  12},
	    		   				{  3, 15,   2, -5,  32},                            
    		   					{ -7,  4, -23,  2, -24},
    		   					{  1, -3,  -2, 16,  14}};
	       
	       switch (methodSelection) {
			case 0:
				gaussianElimination.calculateSimpleGaussianElimiation(4, matrix);	
				break;
			case 1:
				gaussianElimination.calculateGaussianEliminationParcialPivoting(4, matrix);				
				break;
			case 2:
				gaussianElimination.calculateGaussianEliminationTotalPivoting(4, matrix);				
				break;
			default:
				break;
		}	       	       	       	    
	       Log.d("result", "holi");
	       

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
	
}
>>>>>>> 82cefd72a17143854e2f2919429c8b7a19397e32
