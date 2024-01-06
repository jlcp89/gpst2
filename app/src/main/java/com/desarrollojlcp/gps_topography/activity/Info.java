package com.desarrollojlcp.gps_topography.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.desarrollojlcp.gps_topography.R;


public class Info extends AppCompatActivity  {
    private Button botonManual, botonRate, botonEncuesta, botonDes, botonCancelar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);


        botonManual = (Button) findViewById(R.id.boton_manual_usuario);
        botonManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://jlcp89.github.io/d3sarrollo/#/gpst";
                abirLink(url);
            }
        });



        /*Button botonPro = (Button) findViewById(R.id.boton_ir_pro);
        botonPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/details?id=com.desarrollojlcp.gps_topography_pro";
                abirLink(url);
            }
        });*/

        botonEncuesta = (Button) findViewById(R.id.boton_encuesta);
        botonEncuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getResources().getText(R.string.link_encuesta).toString();
                abirLink(url);
            }
        });

        botonDes = (Button) findViewById(R.id.boton_des);
        botonDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://jlcp89.github.io/d3sarrollo/#/gpst";
                abirLink(url);
            }
        });

        botonCancelar = (Button) findViewById(R.id.boton_cancelar);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    finish();
            }
        });

        botonRate = (Button) findViewById(R.id.boton_rate);
        botonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateApp();
            }
        });
    }

    public void rateApp()
    {
        try
        {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        }
        catch (ActivityNotFoundException e)
        {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 23)
        {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        }
        else
        {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    public void abirLink( String url1) {
        try{
            String url = url1;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }catch (ActivityNotFoundException e){
            e.printStackTrace();
        }

    }



    @Override
    public void onBackPressed(){
        botonCancelar.performClick();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }




}
