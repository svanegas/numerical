package co.edu.eafit.dis.numerical;

import co.edu.eafit.dis.numerical.views.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

  private Intent intent;
  private DrawerLayout drawerLayout;
  private ListView drawer;
  private ActionBarDrawerToggle toggle;
  private static String[] opciones;
  private TextView texto;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Rescatamos el Action Bar y activamos el boton Home
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);

    // Declarar e inicializar componentes para el Navigation Drawer
    drawer = (ListView) findViewById(R.id.drawer);
    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    texto = (TextView) findViewById(R.id.texto);
    //texto.setText(getString(R.string.text_main_activity));
    
    // Definir el arreglo de opciones
    opciones = new String[11];
    opciones[0] = getResources().getString(
        R.string.title_activity_incremental_search);
    opciones[1] = getResources().getString(R.string.title_activity_bisection);
    opciones[2] = getResources().getString(
        R.string.title_activity_false_position);
    opciones[3] = getResources().getString(R.string.title_activity_fixed_point);
    opciones[4] = getResources().getString(R.string.title_activity_newton);
    opciones[5] = getResources().getString(R.string.title_activity_secant);
    opciones[6] = getResources().getString(
        R.string.title_activity_multiple_roots);
    opciones[7] = getResources().getString(
        R.string.title_activity_gaussian_elimination);
    opciones[8] = getResources().getString(
        R.string.title_activity_lu_factorization);
    opciones[9] = getResources().getString(
        R.string.title_activity_iterative_methods);
    opciones[10] = getResources().getString(
        R.string.title_activity_interpolation);

    // Declarar adapter y eventos al hacer click
    drawer.setAdapter(new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, opciones));

    drawer.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
          long arg3) {
        switch (arg2) {
          case 0: // Posición 0 en el vector opciones en Incremental
            intent = new Intent(MainActivity.this,
                IncrementalSearchActivity.class);
            MainActivity.this.startActivity(intent);
            break;
          case 1: // Posición 1 en el vector opciones es Bisection
            intent = new Intent(MainActivity.this, BisectionActivity.class);
            MainActivity.this.startActivity(intent);
            break;
          case 2: // Posición 2 en el vector opciones es Regla Falsa
            intent = new Intent(MainActivity.this, FalsePositionActivity.class);
            MainActivity.this.startActivity(intent);
            break;
          case 3: // Posición 3 en el vector opciones es Punto fijo
            intent = new Intent(MainActivity.this, FixedPointActivity.class);
            MainActivity.this.startActivity(intent);
            break;
          case 4: // Posición 4 en el vector opciones es Newton
            intent = new Intent(MainActivity.this, NewtonActivity.class);
            MainActivity.this.startActivity(intent);
            break;
          case 5: // Posición 5 en el vector opciones es Secante
            intent = new Intent(MainActivity.this, SecantActivity.class);
            MainActivity.this.startActivity(intent);
            break;
          case 6: // Posición 6 en el vector opciones es Raíces Múltiples
            intent = new Intent(MainActivity.this, MultipleRootsActivity.class);
            MainActivity.this.startActivity(intent);
            break;
          case 7: // Posición 7 en el vector opciones es Eliminación Gaussiana
            intent = new Intent(MainActivity.this, MatrixUnknownsActivity.class);
            intent.putExtra(
                getResources().getString(R.string.text_key_subsection_name),
                getResources().getString(
                    R.string.title_activity_gaussian_elimination));
            MainActivity.this.startActivity(intent);
            break;
          case 8: // Posición 8 en el vector opciones es Factorización LU
            intent = new Intent(MainActivity.this, MatrixUnknownsActivity.class);
            intent.putExtra(
                getResources().getString(R.string.text_key_subsection_name),
                getResources().getString(
                    R.string.title_activity_lu_factorization));
            MainActivity.this.startActivity(intent);
            break;
          case 9: // Posición 9 en el vector opciones es Métodos Iterativos
            intent = new Intent(MainActivity.this, MatrixUnknownsActivity.class);
            intent.putExtra(
                getResources().getString(R.string.text_key_subsection_name),
                getResources().getString(
                    R.string.title_activity_iterative_methods));
            MainActivity.this.startActivity(intent);
            break;
          case 10: // Posición 10 en el vector opciones es Interpolación
            intent = new Intent(MainActivity.this, MatrixUnknownsActivity.class);
            intent.putExtra(
                getResources().getString(R.string.text_key_subsection_name),
                getResources().getString(
                    R.string.title_activity_interpolation));
            MainActivity.this.startActivity(intent);
            break;
          default:
            break;
        }
        drawerLayout.closeDrawers();
      }
    });

    // Sombra del panel Navigation Drawer
    drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

    // Integracion boton oficial
    toggle = new ActionBarDrawerToggle(this, // Activity
        drawerLayout, // Panel del Navigation Drawer
        R.drawable.ic_drawer, // Icono que va a utilizar
        R.string.app_name, // Descripcion al abrir el drawer
        R.string.hello_world // Descripcion al cerrar el drawer
    ) {
      public void onDrawerClosed(View view) {
        // Drawer cerrado
        getActionBar().setTitle(getResources().getString(R.string.app_name));
        invalidateOptionsMenu();
      }

      public void onDrawerOpened(View drawerView) {
        // Drawer abierto
        getActionBar().setTitle(
            getResources().getString(R.string.title_action_bar_methods));
        invalidateOptionsMenu();
      }
    };

    drawerLayout.setDrawerListener(toggle);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (toggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // Activamos el toggle con el icono
  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    toggle.syncState();
  }
}