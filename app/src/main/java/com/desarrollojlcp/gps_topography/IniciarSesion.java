package com.desarrollojlcp.gps_topography;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class IniciarSesion extends AppCompatActivity implements View.OnClickListener{

    boolean permisoUbicacion = false;
    //defining view objects
    private EditText textEmail;
    private EditText textPassword;
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
            setContentView(R.layout.iniciar_sesion);

            //inicializamos el objeto firebaseAuth

            //Referenciamos los views
            textEmail = (EditText) findViewById(R.id.edit_correo);
            textPassword = (EditText) findViewById(R.id.edit_contra);
            textEmail.setText(loadDataEmail(getApplicationContext()));

            btnRegistrar = (Button) findViewById(R.id.boton_registrarse);
            btnLogin = (Button) findViewById(R.id.boton_login);


            progressDialog = new ProgressDialog(this);

            //attaching listener to button
            btnRegistrar.setOnClickListener(this);
            btnLogin.setOnClickListener(this);

            if (loadDataEmail(getApplicationContext()) == ""){
                textEmail.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            } else {
                textPassword.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }



        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void onStart() { super.onStart();
    }

    @Override
    public void onPause() { super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void iniciarSesion(){

        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = textEmail.getText().toString().trim();
        final String password  = textPassword.getText().toString().trim();

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


        progressDialog.setMessage(getResources().getText(R.string.iniciando_sesion));
        progressDialog.show();


    }


    private void lanzarActividadPrincipal(){
        hideKeyboard(this);
        Intent intent = new Intent(getApplicationContext(), Ingreso.class);
        startActivity(intent);
        finish();
    }

    private void abrirActividadRegistrarse(){
        Intent intent = new Intent(getApplicationContext(), Registro.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boton_registrarse:
                abrirActividadRegistrarse();
                break;

            case R.id.boton_login:
                iniciarSesion();
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

    public static String loadDataEmail(Context context) {
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
