package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.IncrementalSearch;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IncrementalSearchActivity extends Activity {
	
	//Lógica
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
		
		incremental = new IncrementalSearch();
		
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
	}
	
	private void calculate() {
		//Obtener valores de los inputs
		String function = inputFunction.getText().toString();
		String x0Text = inputX0.getText().toString();
		String deltaText = inputDelta.getText().toString();
		int maxIterations = Integer.parseInt(inputMaxIterations
										.getText().toString());
		
		//TODO Hacer validaciones de los datos ingresados.
		
		double x0 = Double.valueOf(x0Text);
		double delta = Double.valueOf(deltaText);
		
		try {
			incremental.setFunction(function);
		} catch (Exception ex) {
			Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}
		try {
			double [] result = incremental.evaluate(x0, delta, maxIterations);
			if (result[0] == result[1]) {
				String rootFound = getString(R.string.root_found, result[0]); 
				Toast.makeText(this, rootFound, Toast.LENGTH_SHORT).show();
			}
			else {
				String intervalFound = getString(R.string.interval_root_found,
												 result[0],
												 result[1]); 
				Toast.makeText(this, intervalFound, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
