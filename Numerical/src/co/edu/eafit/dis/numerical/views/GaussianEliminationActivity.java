package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.R.id;
import co.edu.eafit.dis.numerical.R.layout;
import co.edu.eafit.dis.numerical.methods.GaussianElimination;
import android.app.Activity;
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
