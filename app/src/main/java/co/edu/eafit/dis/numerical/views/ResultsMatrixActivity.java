package co.edu.eafit.dis.numerical.views;

import co.edu.eafit.dis.numerical.R;
import co.edu.eafit.dis.numerical.methods.ResultsMatrix;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ResultsMatrixActivity extends Activity {

  private static final String TAG = ResultsMatrixActivity.class.getSimpleName();

  public static final int ELIMINATION_TYPE = 1;
  public static final int FACTORIZATION_TYPE = 2;
  public static final int ITERATIVE_TYPE = 3;
  private TextView methodTitle;
  private TableLayout resultsMatrix;
  private TableLayout resultsLowerMatrix;
  private TableLayout resultsUpperMatrix;
  private int methodType;

  private LinearLayout resultMatrixGroup;
  private LinearLayout resultLowerMatrixGroup;
  private LinearLayout resultUpperMatrixGroup;

  private Button nextButton;
  private Button prevButton;

  private TextView currentStage;
  private int currentViewingStage;
  private int numberOfStages;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_results_matrixes);

    // Activar el bot?n de ir atr?s en el action bar
    getActionBar().setDisplayHomeAsUpEnabled(true);

    methodTitle = (TextView) findViewById(R.id.method_title);
    resultsMatrix = (TableLayout) findViewById(R.id.results_matrix);
    resultsLowerMatrix = (TableLayout) findViewById(R.id.results_lower_matrix);
    resultsUpperMatrix = (TableLayout) findViewById(R.id.results_upper_matrix);
    resultMatrixGroup = (LinearLayout) findViewById(R.id.result_matrix_group);
    resultLowerMatrixGroup = (LinearLayout) findViewById(R.id.result_lower_matrix_group);
    resultUpperMatrixGroup = (LinearLayout) findViewById(R.id.result_upper_matrix_group);
    currentStage = (TextView) findViewById(R.id.current_stage);
    prevButton = (Button) findViewById(R.id.prev_stage_button);
    nextButton = (Button) findViewById(R.id.next_stage_button);

    String methodName = getIntent().getExtras().getString(
        getResources().getString(R.string.text_key_method_name));
    int methodType = getIntent().getExtras().getInt(
        getResources().getString(R.string.text_key_gaussian_method_type));

    setUpFields(methodName, methodType);

    try {
      if (methodType == ResultsMatrixActivity.ELIMINATION_TYPE) {
        numberOfStages = ResultsMatrix.getStagesMatrixes().size();
        String[][] firstStage = ResultsMatrix.getStringMatrix(0);
        fillMatrix(firstStage.length, firstStage[0].length, firstStage,
            resultsMatrix);
      } else if (methodType == ResultsMatrixActivity.FACTORIZATION_TYPE) {
        numberOfStages = ResultsMatrix.getStagesLower().size();
        String[][] firstStageL = ResultsMatrix.getStringLower(0);
        fillMatrix(firstStageL.length, firstStageL[0].length, firstStageL,
            resultsLowerMatrix);

        String[][] firstStageU = ResultsMatrix.getStringUpper(0);
        fillMatrix(firstStageU.length, firstStageU[0].length, firstStageU,
            resultsUpperMatrix);
      }
    } catch (Exception e) {
      Log.e(TAG, getResources().getString(R.string.error_matrixes_failed_setup)
          + ": " + e.getMessage());
    }
    updateStage(0);
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

  public void setUpFields(String methodName, int methodType) {
    this.methodType = methodType;
    methodTitle.setText(methodName);
    if (methodType == ELIMINATION_TYPE) {
      resultMatrixGroup.setVisibility(View.VISIBLE);
      resultLowerMatrixGroup.setVisibility(View.GONE);
      resultUpperMatrixGroup.setVisibility(View.GONE);
    } else {
      resultMatrixGroup.setVisibility(View.GONE);
      resultLowerMatrixGroup.setVisibility(View.VISIBLE);
      resultUpperMatrixGroup.setVisibility(View.VISIBLE);
    }
  }

  public void fillMatrix(final int n, final int m, final String[][] matrix,
      TableLayout matrixLayout) {
    matrixLayout.removeAllViews();
    for (int i = 0; i < n; i++) {
      TableRow row = new TableRow(ResultsMatrixActivity.this);
      row.setLayoutParams(new TableRow.LayoutParams(
          TableRow.LayoutParams.WRAP_CONTENT,
          TableRow.LayoutParams.WRAP_CONTENT));
      for (int j = 0; j < m; j++) {
        EditText edit = new EditText(ResultsMatrixActivity.this);
        edit.setInputType(InputType.TYPE_CLASS_NUMBER
            | InputType.TYPE_NUMBER_FLAG_DECIMAL
            | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edit.setLayoutParams(new TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT));
        edit.setGravity(Gravity.CENTER);

        edit.setText(matrix[i][j]);
        edit.setKeyListener(null);
        if (i == 0) {
          edit.setBackgroundColor(getResources().getColor(
              R.color.header_table_results_row));
          edit.setTypeface(edit.getTypeface(), Typeface.BOLD);
        } else {
          double val = Double.parseDouble(matrix[i][j]);
          if (val == Double.POSITIVE_INFINITY
              || val == Double.NEGATIVE_INFINITY) {
            if (matrixLayout == resultsLowerMatrix) {
              edit.setText(getResources().getString(
                  R.string.text_lower_unresolved_element, i, j + 1));
            } else if (matrixLayout == resultsUpperMatrix) {
              edit.setText(getResources().getString(
                  R.string.text_upper_unresolved_element, i, j + 1));
            }
          }
          if (i % 2 == 0)
            edit.setBackgroundColor(getResources().getColor(
                R.color.light_blue_table_results_row));
          else
            edit.setBackgroundColor(getResources().getColor(
                R.color.light_gray_table_results_row));
        }
        if (j == 0) { // Si es la columna inicial entonces se a?ade una barra
          EditText separator = new EditText(ResultsMatrixActivity.this);
          separator.setLayoutParams(new TableRow.LayoutParams(1,
              LayoutParams.WRAP_CONTENT));
          separator.setEnabled(false);
          separator.setBackgroundColor(Color.BLACK);
          row.addView(separator);
        }
        edit.setTextColor(Color.BLACK);
        edit.setEnabled(false);
        row.addView(edit);
        EditText separator = new EditText(ResultsMatrixActivity.this);
        separator.setLayoutParams(new TableRow.LayoutParams(1,
            LayoutParams.WRAP_CONTENT));
        separator.setEnabled(false);
        separator.setBackgroundColor(Color.BLACK);
        row.addView(separator);
      }
      matrixLayout.addView(row);
    }
  }

  private void updateStage(int stage) {
    if (stage < 0 || stage > numberOfStages)
      return;
    currentViewingStage = stage;
    currentStage.setText(String.valueOf(currentViewingStage));
    updateButtons();
  }

  public void increaseStage(View v) {
    int oldCurrentViewingStage = currentViewingStage;
    currentViewingStage = Math.min(currentViewingStage + 1, numberOfStages - 1);
    if (oldCurrentViewingStage == currentViewingStage)
      return;

    try {
      if (methodType == ELIMINATION_TYPE) {
        String[][] nextMatrix = ResultsMatrix
            .getStringMatrix(currentViewingStage);
        fillMatrix(nextMatrix.length, nextMatrix[0].length, nextMatrix,
            resultsMatrix);
      } else if (methodType == FACTORIZATION_TYPE) {
        String[][] nextMatrixLower = ResultsMatrix
            .getStringLower(currentViewingStage);
        fillMatrix(nextMatrixLower.length, nextMatrixLower[0].length,
            nextMatrixLower, resultsLowerMatrix);

        String[][] nextMatrixUpper = ResultsMatrix
            .getStringUpper(currentViewingStage);
        fillMatrix(nextMatrixUpper.length, nextMatrixUpper[0].length,
            nextMatrixUpper, resultsUpperMatrix);
      }
      currentStage.setText(String.valueOf(currentViewingStage));
    } catch (Exception e) {
      Log.e(TAG, getResources().getString(R.string.error_matrixes_failed_setup)
          + ": " + e.getMessage());
    }
    updateButtons();
  }

  public void decreaseStage(View v) {
    int oldCurrentViewingStage = currentViewingStage;
    currentViewingStage = Math.max(currentViewingStage - 1, 0);
    if (oldCurrentViewingStage == currentViewingStage)
      return;
    try {
      if (methodType == ELIMINATION_TYPE) {
        String[][] nextMatrix = ResultsMatrix
            .getStringMatrix(currentViewingStage);
        fillMatrix(nextMatrix.length, nextMatrix[0].length, nextMatrix,
            resultsMatrix);
      } else if (methodType == FACTORIZATION_TYPE) {
        String[][] nextMatrixLower = ResultsMatrix
            .getStringLower(currentViewingStage);
        fillMatrix(nextMatrixLower.length, nextMatrixLower[0].length,
            nextMatrixLower, resultsLowerMatrix);

        String[][] nextMatrixUpper = ResultsMatrix
            .getStringUpper(currentViewingStage);
        fillMatrix(nextMatrixUpper.length, nextMatrixUpper[0].length,
            nextMatrixUpper, resultsUpperMatrix);
      }
      currentStage.setText(String.valueOf(currentViewingStage));
    } catch (Exception e) {
      Log.e(TAG, getResources().getString(R.string.error_matrixes_failed_setup)
          + ": " + e.getMessage());
    }
    updateButtons();
  }

  private void updateButtons() {
    nextButton.setEnabled(currentViewingStage != numberOfStages - 1);
    prevButton.setEnabled(currentViewingStage != 0);
  }
}
