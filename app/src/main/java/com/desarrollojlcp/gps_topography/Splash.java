package com.desarrollojlcp.gps_topography;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.Toast;

public class Splash extends AppCompatActivity {

    Poligono poligonoActual = new Poligono();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        poligonoActual.primeraPantalla = true;
        poligonoActual.limpiarPoligono();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Ingreso.class);
                intent.putExtra("pol_trab", poligonoActual);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
