package com.desarrollojlcp.gps_topography.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.desarrollojlcp.gps_topography.R;
import com.desarrollojlcp.gps_topography.model.object.Estacion;
import com.desarrollojlcp.gps_topography.model.object.Poligono;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Vector;

public class BuscarCoordenadas extends AppCompatActivity {

    private RadioButton radioGPS, radioUTM;
    private EditText edLat, edLon, edZone, edLatZone, edEast, edNort;
    int REQUEST_CODE = 1;
    int REQUEST_CODE_UTM = 2;

    Poligono polTrab;
    private Button botonCancel;
    private Button botonBuscarGPS, botonBuscarUTM;

    //private InterstitialAd mInterstitialAd = new InterstitialAd(this);
    public static final String PREF_FILE= "preferenciaSusGPSTpro";
    public static final String SUBSCRIBE_KEY= "subscribe";
    public static final String SEARCH_KEY= "buscar_coordenadas";

    private final boolean valorSuscripcion = false;
    private boolean getSubscribeValueFromPref(){
        return getPreferenceObject().getBoolean( SUBSCRIBE_KEY,false);
    }
    private SharedPreferences getPreferenceObject() {
        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        polTrab = (Poligono) Objects.requireNonNull(getIntent().getExtras()).getSerializable("pol_trab");

        setContentView(R.layout.activity_buscar_coordenadas);
        RadioGroup radioOpcion = findViewById(R.id.radio_grupo_opcion);
        radioGPS = findViewById(R.id.radio_gps);
        radioUTM = findViewById(R.id.radio_utm);
        edLat = findViewById(R.id.edit_lat);
        edLon = findViewById(R.id.edit_lon);
        edZone = findViewById(R.id.edit_zone);
        edLatZone = findViewById(R.id.edit_lat_zone);
        edEast = findViewById(R.id.edit_easting);
        edNort = findViewById(R.id.edit_northing);
        botonCancel = findViewById(R.id.boton_cancelar_coor);
        Button botonBuscar = findViewById(R.id.boton_buscar_lugar);
        botonBuscarGPS = findViewById(R.id.boton_importar_gps);
        botonBuscarUTM = findViewById(R.id.boton_importar_utm);
        radioGPS.setChecked(true);
        edLat.setEnabled(true);
        edLon.setEnabled(true);
        edZone.setEnabled(false);
        edLatZone.setEnabled(false);
        edEast.setEnabled(false);
        edNort.setEnabled(false);
        edLat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        edLon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        edZone.setBackgroundColor(getResources().getColor(R.color.colorGris));
        edLatZone.setBackgroundColor(getResources().getColor(R.color.colorGris));
        edEast.setBackgroundColor(getResources().getColor(R.color.colorGris));
        edNort.setBackgroundColor(getResources().getColor(R.color.colorGris));
        botonBuscarGPS.setEnabled(true);
        botonBuscarUTM.setEnabled(false);

        radioOpcion.setOnCheckedChangeListener((group, checkedId) -> {
            if (radioGPS.isChecked()) {
                edLat.setEnabled(true);
                edLon.setEnabled(true);
                edZone.setEnabled(false);
                edLatZone.setEnabled(false);
                edEast.setEnabled(false);
                edNort.setEnabled(false);
                edLat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                edLon.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                edZone.setBackgroundColor(getResources().getColor(R.color.colorGris));
                edLatZone.setBackgroundColor(getResources().getColor(R.color.colorGris));
                edEast.setBackgroundColor(getResources().getColor(R.color.colorGris));
                edNort.setBackgroundColor(getResources().getColor(R.color.colorGris));
                botonBuscarGPS.setEnabled(true);
                botonBuscarUTM.setEnabled(false);
            } else if (radioUTM.isChecked()) {
                edLat.setEnabled(false);
                edLon.setEnabled(false);
                edZone.setEnabled(true);
                edLatZone.setEnabled(true);
                edEast.setEnabled(true);
                edNort.setEnabled(true);
                edLat.setBackgroundColor(getResources().getColor(R.color.colorGris));
                edLon.setBackgroundColor(getResources().getColor(R.color.colorGris));
                edZone.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                edLatZone.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                edEast.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                edNort.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                botonBuscarGPS.setEnabled(false);
                botonBuscarUTM.setEnabled(true);
            }
        });

        botonCancel.setOnClickListener(view -> {
            Poligono poligonoTemp = new Poligono();
            Intent intent = new Intent(getApplicationContext(), Ingreso.class);
            intent.putExtra("pol_trab", poligonoTemp);
            startActivity(intent);
            finish();
        });

        botonBuscarGPS.setOnClickListener(view -> {
            openFC(REQUEST_CODE);
        });

        botonBuscarUTM.setOnClickListener(view -> {
            openFC(REQUEST_CODE_UTM);
        });

        botonBuscar.setOnClickListener(view -> {
            Poligono poligonoTemp = new Poligono();
            Estacion e0 = new Estacion();

            e0.idEst = "E-0";

            try {
                if (radioGPS.isChecked()) {
                    String cadTemp1 = edLat.getText().toString();
                    String cadTemp2 = edLon.getText().toString();
                    double lat = Double.parseDouble(cadTemp1);
                    double lon = Double.parseDouble(cadTemp2);
                    e0.setLat(lat);
                    e0.setLon(lon);
                } else if (radioUTM.isChecked()) {
                    String cadTemp1 = edZone.getText().toString();
                    String cadTemp2 = edLatZone.getText().toString();
                    String cadTemp3 = edEast.getText().toString();
                    String cadTemp4 = edNort.getText().toString();
                    String cadUTM = cadTemp1 + " " + cadTemp2 + " " + cadTemp3 + " " + cadTemp4;
                    double[] coorGPS = Poligono.calcularGPSdeUTM(cadUTM);
                    e0.setLat(coorGPS[0]);
                    e0.setLon(coorGPS[1]);
                }
                poligonoTemp.ingresarEstacion(e0);
                Intent intent = new Intent(getApplicationContext(), Ingreso.class);
                intent.putExtra("pol_trab", poligonoTemp);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Bad coordinates, try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openFC(int RQ) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        startActivityForResult(intent, RQ);
    }

    private SharedPreferences.Editor getPreferenceEditObjectCoor() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
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
    public void onBackPressed() {
        super.onBackPressed();
        botonCancel.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                String string;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    assert uri != null;
                    InputStream is = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while ((string = reader.readLine()) != null) {
                        stringBuilder.append(string).append("\n");
                    }
                    assert is != null;
                    is.close();
                } catch (Exception e) {
                    Log.i("Mensaje", e.toString());
                }
                String resultado = stringBuilder.toString();
                String[] lineas = resultado.split("\n");
                StringBuilder prueba = new StringBuilder();
                Vector<Estacion> estaciones1 = new Vector<>();

                for (int i = 0; i < lineas.length; i++) {
                    String[] cad = lineas[i].split(" ");
                    Estacion e = new Estacion();
                    e.setIdEstacion(i);
                    e.idEst = cad[0];

                    NumberFormat nf = NumberFormat.getInstance(Locale.US);

                    e.setLat(nf.parse(cad[1]).floatValue());
                    e.setLon(nf.parse(cad[2]).floatValue());
                    e.setAlt(nf.parse(cad[3]).floatValue());

                    estaciones1.addElement(e);
                    prueba.append(e.getLat() + " ").append("\n");
                }

                Toast.makeText(getApplicationContext(), prueba, Toast.LENGTH_LONG).show();

                polTrab.setEstaciones(estaciones1);

                //se inicia el intent para cargar la actividad para calcular y presentar resultados
                Intent intent = new Intent(getApplicationContext(), Ingreso.class);
                polTrab.poligonoCargado = true;
                intent.putExtra("pol_trab", polTrab);
                startActivity(intent);
                finish();
            }

            if (requestCode == REQUEST_CODE_UTM && resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                String string;
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    assert uri != null;
                    InputStream is = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while ((string = reader.readLine()) != null) {
                        stringBuilder.append(string).append("\n");
                    }
                    assert is != null;
                    is.close();
                } catch (Exception e) {
                    Log.i("Mensaje", e.toString());
                }
                String resultado = stringBuilder.toString();
                String[] lineas = resultado.split("\n");
                StringBuilder prueba = new StringBuilder();
                Vector<Estacion> estaciones1 = new Vector<>();

                for (int i = 0; i < lineas.length; i++) {
                    String[] cad = lineas[i].split(" ");
                    Estacion e = new Estacion();
                    e.setIdEstacion(i);
                    e.idEst = cad[0];
                    String cadTemp1 = cad[1];
                    String cadTemp2 = cad[2];
                    String cadTemp3 = cad[3];
                    String cadTemp4 = cad[4];
                    String cadUTM = cadTemp1 + " " + cadTemp2 + " " + cadTemp3 + " " + cadTemp4;

                    double[] coorGPS = Poligono.calcularGPSdeUTM(cadUTM);

                    e.setLat(coorGPS[0]);
                    e.setLon(coorGPS[1]);
                    e.setAlt(Double.parseDouble(cad[5]));
                    estaciones1.addElement(e);
                    prueba.append(e.getLat()).append("\n");
                }

                polTrab.setEstaciones(estaciones1);

                //se inicia el intent para cargar la actividad para calcular y presentar resultados
                Intent intent = new Intent(getApplicationContext(), Ingreso.class);
                polTrab.poligonoCargado = true;
                intent.putExtra("pol_trab", polTrab);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            //"Bad file, try again!"
            String error = e.toString();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
