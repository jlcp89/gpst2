package com.desarrollojlcp.gps_topography.other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.desarrollojlcp.gps_topography.activity.Ingreso;
import com.desarrollojlcp.gps_topography.R;
import com.desarrollojlcp.gps_topography.model.object.Poligono;

public class Splash extends AppCompatActivity {

    Poligono poligonoActual = new Poligono();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        poligonoActual.setPrimeraPantalla(true);
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
