package com.desarrollojlcp.gps_topography;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.desarrollojlcp.gps_topography.utilidades.Utilidades;

import com.google.android.gms.maps.model.LatLng;

import java.util.Vector;

public class GuardarPoligono extends AppCompatActivity {

    //Declaracion de variables locales de la actividad actual


    private EditText editProyecto, editCliente, editUbicacion, editResponsable;
    private TextView textFecha;
    private Button botonCancel, botonGuardar;
    boolean ultimoPunto = false;
    ConexionSQLiteHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Se obtiene el poligono de trabajo de la actividad anterior
        final Poligono poligonoActual = (Poligono) getIntent().getExtras().getSerializable("pol_trab");

        //Se establece el layout para la actividad actual
        setContentView(R.layout.layout_guardar_poligono);



        textFecha = (TextView) findViewById(R.id.texto_guardar_pol_fecha);
        editProyecto = (EditText) findViewById(R.id.edit_proyecto);
        editCliente = (EditText)findViewById(R.id.edit_cliente);
        editUbicacion = (EditText)findViewById(R.id.edit_ubicacion);
        editResponsable = (EditText)findViewById(R.id.edit_responsable);
        botonGuardar = (Button) findViewById(R.id.boton_guardar_poligono);
        botonCancel = (Button) findViewById(R.id.boton_cancelar_17);

        textFecha.setText(poligonoActual.fecha);
        helper = new ConexionSQLiteHelper(this, Utilidades.NOMBRE_BD, null, Utilidades.VERSION_BD);


        botonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                poligonoActual.proyecto = editProyecto.getText().toString();
                poligonoActual.cliente = editCliente.getText().toString();
                poligonoActual.ubicacion = editUbicacion.getText().toString();
                poligonoActual.responsable = editResponsable.getText().toString();


                if (poligonoActual.tPol == 0){

                    /*Vector<LatLng> estaciones = new Vector<LatLng>() ;
                    for (int F = 0; F < poligonoActual.estaciones.size(); F++) {
                        Estacion estTemp = poligonoActual.estaciones.elementAt(F);
                        String cadi1 = "E-" + F;
                        double lon = estTemp.lon;
                        double lat = estTemp.lat;
                        estTemp.idEst = cadi1;
                        LatLng puntoActual = new LatLng(lat, lon);
                        estaciones.addElement(puntoActual);
                    }*/


                    long newPolId = 0;
                    if (poligonoActual.estaciones.size()>0){

                        ContentValues valuesPoligono = new ContentValues();
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_FECHA, poligonoActual.fecha);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_TIPO_POL, 0);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_PROYECTO, poligonoActual.proyecto);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_CLIENTE, poligonoActual.cliente);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_UBICACION, poligonoActual.ubicacion);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_RESPONSABLE, poligonoActual.responsable);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_TIPO_MEDICION, poligonoActual.tipoMedicion);

                        SQLiteDatabase db = helper.getWritableDatabase();
                        newPolId = db.insert(Utilidades.PoligonoEntry.TABLA_POLIGONOS, null, valuesPoligono);

                        for (int F = 0; F < poligonoActual.estaciones.size(); F++) {
                            String cadi1 = "E-" + F;
                            ContentValues valuesEstacion = new ContentValues();
                            valuesEstacion.put(Utilidades.EstacionEntry.CAMPO_NOMBRE_EST, cadi1);
                            valuesEstacion.put(Utilidades.EstacionEntry.CAMPO_LAT, poligonoActual.estaciones.elementAt(F).lat);
                            valuesEstacion.put(Utilidades.EstacionEntry.CAMPO_LON, poligonoActual.estaciones.elementAt(F).lon);
                            valuesEstacion.put(Utilidades.EstacionEntry.CAMPO_OBSERVACIONES, poligonoActual.estaciones.elementAt(F).observaciones);
                            valuesEstacion.put(Utilidades.EstacionEntry.CAMPO_ALT, poligonoActual.estaciones.elementAt(F).alt);

                            valuesEstacion.put(Utilidades.EstacionEntry.CAMPO_ID_POL, newPolId);

                            // Insert the new row, returning the primary key value of the new row
                            long newEstId = db.insert(Utilidades.EstacionEntry.TABLA_ESTACIONES, null, valuesEstacion);

                        }
                    db.close();
                    }
                    Toast.makeText(getApplicationContext(),"Project saved successfully! -- ID: "+ String.valueOf(newPolId),Toast.LENGTH_LONG).show();

                    finish();

                } else if (poligonoActual.tPol == 1){
                    long newPolId = 0;
                    if (poligonoActual.estaciones.size()>0){

                        ContentValues valuesPoligono = new ContentValues();
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_FECHA, poligonoActual.fecha);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_TIPO_POL, 1);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_PROYECTO, poligonoActual.proyecto);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_CLIENTE, poligonoActual.cliente);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_UBICACION, poligonoActual.ubicacion);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_RESPONSABLE, poligonoActual.responsable);
                        valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_TIPO_MEDICION, poligonoActual.tipoMedicion);

                        SQLiteDatabase db = helper.getWritableDatabase();
                        newPolId = db.insert(Utilidades.PoligonoEntry.TABLA_POLIGONOS, null, valuesPoligono);


                        db.close();
                        finish();
                        Toast.makeText(getApplicationContext(),"Project saved successfully! -- ID: "+ String.valueOf(newPolId),Toast.LENGTH_LONG).show();


                    }




                }


            }
        });

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




    @Override
    public void onBackPressed(){

        finish();
    }
}
