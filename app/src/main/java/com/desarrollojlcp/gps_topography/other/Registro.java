package com.desarrollojlcp.gps_topography.other;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.desarrollojlcp.gps_topography.activity.Ingreso;
import com.desarrollojlcp.gps_topography.activity.IniciarSesion;
import com.desarrollojlcp.gps_topography.R;


public class Registro extends AppCompatActivity implements View.OnClickListener{

    private EditText TextEmail;
    private EditText TextPassword;
    private Button btnRegistrar;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    private static final String SHARED_PREFS = "ultimoCorreoIngresadoGPST";
    private static String TEXT = "";
    private static final String KEY = "GPSTleedordemail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);

            //inicializamos el objeto firebaseAuth

            //Referenciamos los views
            TextEmail = (EditText) findViewById(R.id.edit_correo);
            TextPassword = (EditText) findViewById(R.id.edit_contra);

            btnRegistrar = (Button) findViewById(R.id.boton_registrarse);
            btnLogin = (Button) findViewById(R.id.boton_login);


            progressDialog = new ProgressDialog(this);

            //attaching listener to button
            btnRegistrar.setOnClickListener(this);
            btnLogin.setOnClickListener(this);

            TextEmail.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        } catch (Exception e) {e.printStackTrace();}


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onPause() {

        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    private void registrarUsuario(){

        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = TextEmail.getText().toString().trim();
        final String password  = TextPassword.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,getResources().getText(R.string.debe_ingresar_correo),Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,getResources().getText(R.string.falta_contra),Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length()<6){
            Toast.makeText(this,getResources().getText(R.string.contra_seis),Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage(getResources().getText(R.string.registro_online));
        progressDialog.show();


    }


    private void lanzarActividadPrincipal(){
        hideKeyboard(this);
        Intent intent = new Intent(getApplicationContext(), Ingreso.class);
        startActivity(intent);
        finish();
    }

    private void lanzarActividadLogIn(){
        Intent intent = new Intent(getApplicationContext(), IniciarSesion.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boton_registrarse:
                registrarUsuario();
                break;

            case R.id.boton_login:
                lanzarActividadLogIn();
                break;

        }

    }





    public static void saveData(Context context, String correo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        TEXT = correo;
        editor.putString(KEY, TEXT);
        editor.apply();

    }

    public static String loadData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String text = sharedPreferences.getString(KEY, "");
        return text;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
