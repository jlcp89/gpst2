package com.desarrollojlcp.gps_topography.activity;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollojlcp.gps_topography.R;
import com.desarrollojlcp.gps_topography.model.object.Estacion;
import com.desarrollojlcp.gps_topography.model.object.Poligono;


public class IngresoDistanciaAzimutEstacion extends AppCompatActivity {

    //Declaracion de variables locales de la actividad actual
    private RadioGroup radioGrupoUltimo, radioGrupoTipoMedida, radioGrupoPartePoligonoFinal;
    private RadioButton radioUltimoSiBoton, radioLindero, radioRadiacion, radioPartePolFinalSi, radioPartePolFinalNo;
    private RadioButton radioUltimoNoBoton;
    private double gra;
    private double min;
    private double seg;
    private double dista;
    private EditText edTextDist;
    private EditText editTextGra;
    private EditText editTextMin;
    private EditText editTextSeg;
    String cadi1, cadi2;
    private Button btnSubmit, desaWeb, desaLike, botonCancel, botonRegresar, botonAgregarOtraEstacion;
    boolean ultimoPunto = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Se obtiene el poligono de trabajo de la actividad anterior
        final Poligono poligonoActual = (Poligono) getIntent().getExtras().getSerializable("pol_trab");

        //Se establece el layout para la actividad actual
        setContentView(R.layout.layout_ingreso_distaz_est);



        //Inicializacion de variables locales, conexion con el layout
        radioGrupoTipoMedida = findViewById(R.id.radio_grupo_medicion);
        radioLindero = findViewById(R.id.radio_lindero);
        radioRadiacion = findViewById(R.id.radio_radiacion);

        radioGrupoPartePoligonoFinal = findViewById(R.id.radio_grupo_partePolFinal);
        radioPartePolFinalSi = findViewById(R.id.radio_partePolFinal_si);
        radioPartePolFinalNo = findViewById(R.id.radio_partePolFinal_no);

        radioGrupoUltimo = findViewById(R.id.radio_grupo_ultimo);
        radioUltimoSiBoton = findViewById(R.id.radio_si);
        radioUltimoNoBoton = findViewById(R.id.radio_no);

        radioGrupoTipoMedida.check(R.id.radio_lindero);
        radioGrupoPartePoligonoFinal.check(R.id.radio_partePolFinal_si);
        radioGrupoUltimo.check(R.id.radio_no);

        radioUltimoSiBoton.setEnabled(false);

        final TextView textEstAp = findViewById(R.id.estacion_aparato);
        final TextView textPuOb = findViewById(R.id.punto_observado);
        edTextDist = findViewById(R.id.distancia_po);
        editTextGra = findViewById(R.id.azimut_grados);
        editTextMin = findViewById(R.id.azimut_minutos);
        editTextSeg = findViewById(R.id.azimut_segundos);
        botonRegresar = findViewById(R.id.boton_regresar);
        botonAgregarOtraEstacion = findViewById(R.id.boton_agregar_otro_punto);
        botonCancel = findViewById(R.id.boton_cancelar_datodistaz);

        final int tamañoVectorEstaciones = poligonoActual.estaciones.size();
        final int indexUltimaEstacion;

        if (tamañoVectorEstaciones>0){
            indexUltimaEstacion = tamañoVectorEstaciones - 1;
        } else {
            indexUltimaEstacion = 0;
            Poligono poligonoTemp = new Poligono();
            poligonoTemp.xCen = 125;
            poligonoTemp.yCen = 1000;
            poligonoTemp.displayScale = 1.0f;
            Estacion estTemp = new Estacion();
            estTemp.setIdEstacion(0);
            estTemp.dist = 0;
            estTemp.grado = 0;
            estTemp.minuto = 0;
            estTemp.segundo = 0;
            double valDecAz = 0 + (0/60) + (0/3600);
            estTemp.valorDecimalGrados = valDecAz;
            estTemp.valorRadianes =  Math.toRadians(estTemp.valorDecimalGrados);
            estTemp.seno =  Math.sin(Math.toRadians(estTemp.valorDecimalGrados));
            estTemp.coseno = Math.cos(Math.toRadians(estTemp.valorDecimalGrados));
            estTemp.idEst = "E-0";
            poligonoTemp.estaciones.addElement(estTemp);
            poligonoTemp.calculoCoorParciales();

            try {
                Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                intent.putExtra("pol_trab", poligonoTemp);
                //Toast.makeText(getApplicationContext(),getResources().getText(R.string.posicion_inicial),Toast.LENGTH_LONG).show();
                finish();
                startActivity(intent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        final int cantidadRadiacionesIniciales = poligonoActual.estaciones.elementAt(indexUltimaEstacion).radiaciones.size();
        cadi1 = "E-" + String.valueOf(indexUltimaEstacion) ;
        cadi2 = "E-" + String.valueOf(tamañoVectorEstaciones);

        textEstAp.setText(cadi1);
        textPuOb.setText(cadi2);

        radioGrupoTipoMedida.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (radioLindero.isChecked()) {
                    //Establecer texto de las estaciones de aparato y punto observado
                    cadi1 = "E-" + String.valueOf(indexUltimaEstacion);
                    cadi2 = "E-" + String.valueOf(tamañoVectorEstaciones);

                    textEstAp.setText(cadi1);
                    textPuOb.setText(cadi2);

                } else if (radioRadiacion.isChecked()) {
                    Estacion e1 = poligonoActual.estaciones.elementAt(indexUltimaEstacion);
                    poligonoActual.contEs = poligonoActual.contEs - 1;
                    cadi1 = "E-" + String.valueOf(indexUltimaEstacion);
                    cadi2 = "E-"+  String.valueOf(indexUltimaEstacion)+"." + String.valueOf(cantidadRadiacionesIniciales + 1);

                    textEstAp.setText(cadi1);
                    textPuOb.setText(cadi2);
                }
            }
        });


        botonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //poligonoActual.contEs = poligonoActual.contEs - 1;
                Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                intent.putExtra("pol_trab", poligonoActual);
                finish();
                startActivity(intent);
            }
        });

        //Dentro del Listener del boton se establecer el proceso para guardar datos en poligono de trabajo
        botonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //estacion temporal para almacenamiento de datos y envio al poligono de trabajo poligono_trabajo_temp

                //se obtienen los datos del layout y se guardan en variables temporales
                //primero se comprueba que el usuario introdujo datos, se usa variable temporal para asignar valores
                String cadTemp = edTextDist.getText().toString();

                if (cadTemp.isEmpty()){
                    dista = 0;
                }else {
                    dista = Float.valueOf(cadTemp);
                }

                cadTemp = editTextGra.getText().toString();

                if (cadTemp.isEmpty()){
                    gra = 0;
                }else {
                    gra = Integer.valueOf(cadTemp);
                }

                cadTemp = editTextMin.getText().toString();

                if (cadTemp.isEmpty()){
                    min = 0;
                }else {
                    min = Integer.valueOf(cadTemp);
                }

                cadTemp = editTextSeg.getText().toString();

                if (cadTemp.isEmpty()){
                    seg = 0;
                }else {
                    seg = Float.valueOf(cadTemp);
                }


                //comprobacion que los datos ingresados sean validos, entre intervalos validos
                if ((dista>0)&&((gra>=0)&&(gra<=360))&&((min>=0)&&(min<60))&&((seg>=0)&&(seg<60))){

                    //ingreso de id, distancia y azimut de estacion temporal
                    Estacion estTemp = new Estacion();
                    estTemp.setIdEstacion(poligonoActual.contEs);
                    estTemp.dist = dista;
                    estTemp.grado = gra;
                    estTemp.minuto = min;
                    estTemp.segundo = seg;
                    double valDecAz = gra + (min/60) + (seg/3600);
                    estTemp.valorDecimalGrados = valDecAz;
                    estTemp.valorRadianes =  Math.toRadians(estTemp.valorDecimalGrados);
                    estTemp.seno =  Math.sin(Math.toRadians(estTemp.valorDecimalGrados));
                    estTemp.coseno = Math.cos(Math.toRadians(estTemp.valorDecimalGrados));
                    estTemp.idEst = cadi2;
                    estTemp.ultimoPunto = ultimoPunto;

                    if (radioLindero.isChecked()){
                        if (radioPartePolFinalSi.isChecked()){
                            estTemp.setPartePoligonoFinal(true);
                        }else if (radioPartePolFinalSi.isChecked()){
                            estTemp.setPartePoligonoFinal(false);
                        }
                        estTemp.tipoMedicion = "lindero";
                        poligonoActual.estaciones.addElement(estTemp);
                    } else if (radioRadiacion.isChecked()){
                        if (radioPartePolFinalSi.isChecked()){
                            estTemp.setPartePoligonoFinal(true);
                        }else if (radioPartePolFinalSi.isChecked()){
                            estTemp.setPartePoligonoFinal(false);
                        }
                        estTemp.tipoMedicion = "radiacion";
                        poligonoActual.estaciones.elementAt(poligonoActual.estaciones.size()-1).radiaciones.addElement(estTemp);
                    }

                    Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                    intent.putExtra("pol_trab", poligonoActual);
                    finish();
                    startActivity(intent);


                    poligonoActual.calculosParaPoligonoFinal();
                    if (poligonoActual.estacionesPolFinal.size()%2 == 0){

                    }

                    //si los valores ingresados estan fuera de los intervalos validos
                }else{

                    //se Set el Texto de los edittext a vacio, para permitir ingreso nuevamente
                    edTextDist.setText("");
                    editTextGra.setText("");
                    editTextMin.setText("");
                    editTextSeg.setText("");
                    //se le informa al usuario que los datos ingresados son invalidos y que debe ingresarlos de nuevo
                    Toast.makeText(getApplicationContext(),R.string.dat_inco_distaz, Toast.LENGTH_LONG).show();
                }
            }
        });





        //Dentro del Listener del boton se establecer el proceso para guardar datos en poligono de trabajo
        botonAgregarOtraEstacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //estacion temporal para almacenamiento de datos y envio al poligono de trabajo poligono_trabajo_temp

                //se obtienen los datos del layout y se guardan en variables temporales
                //primero se comprueba que el usuario introdujo datos, se usa variable temporal para asignar valores
                String cadTemp = edTextDist.getText().toString();

                if (cadTemp.isEmpty()){
                    dista = 0;
                }else {
                    dista = Float.valueOf(cadTemp);
                }

                cadTemp = editTextGra.getText().toString();

                if (cadTemp.isEmpty()){
                    gra = 0;
                }else {
                    gra = Integer.valueOf(cadTemp);
                }

                cadTemp = editTextMin.getText().toString();

                if (cadTemp.isEmpty()){
                    min = 0;
                }else {
                    min = Integer.valueOf(cadTemp);
                }

                cadTemp = editTextSeg.getText().toString();

                if (cadTemp.isEmpty()){
                    seg = 0;
                }else {
                    seg = Float.valueOf(cadTemp);
                }


                //comprobacion que los datos ingresados sean validos, entre intervalos validos
                if ((dista>0)&&((gra>=0)&&(gra<=360))&&((min>=0)&&(min<60))&&((seg>=0)&&(seg<60))){
                    Estacion estTemp = new Estacion();
                    //ingreso de id, distancia y azimut de estacion temporal
                    estTemp.setIdEstacion(poligonoActual.contEs);
                    estTemp.dist = dista;
                    estTemp.grado = gra;
                    estTemp.minuto = min;
                    estTemp.segundo = seg;
                    double valDecAz = gra + (min/60) + (seg/3600);
                    estTemp.valorDecimalGrados = valDecAz;
                    estTemp.valorRadianes =  Math.toRadians(estTemp.valorDecimalGrados);
                    estTemp.seno =  Math.sin(Math.toRadians(estTemp.valorDecimalGrados));
                    estTemp.coseno = Math.cos(Math.toRadians(estTemp.valorDecimalGrados));
                    estTemp.idEst = cadi2;
                    estTemp.ultimoPunto = ultimoPunto;



                    if (radioLindero.isChecked()){
                        estTemp.tipoMedicion = "lindero";
                        if (radioPartePolFinalSi.isChecked()){
                            estTemp.setPartePoligonoFinal(true);
                        }else if (radioPartePolFinalSi.isChecked()){
                            estTemp.setPartePoligonoFinal(false);
                        }
                        poligonoActual.estaciones.addElement(estTemp);
                    } else if (radioRadiacion.isChecked()){
                        estTemp.tipoMedicion = "radiacion";
                        if (radioPartePolFinalSi.isChecked()){
                            estTemp.setPartePoligonoFinal(true);
                        }else if (radioPartePolFinalSi.isChecked()){
                            estTemp.setPartePoligonoFinal(false);
                        }
                        poligonoActual.estaciones.elementAt(poligonoActual.estaciones.size()-1).radiaciones.addElement(estTemp);
                    }

                    //se inicia el intent para cargar la actividad para calcular y presentar resultados
                    Intent intent = new Intent(getApplicationContext(), IngresoDistanciaAzimutEstacion.class);
                    intent.putExtra("pol_trab", poligonoActual);
                    finish();
                    startActivity(intent);

                    poligonoActual.calculosParaPoligonoFinal();
                    if (poligonoActual.estacionesPolFinal.size()%2 == 0){

                    }

                    //si los valores ingresados estan fuera de los intervalos validos
                }else{
                    //se Set el Texto de los edittext a vacio, para permitir ingreso nuevamente
                    edTextDist.setText("");
                    editTextGra.setText("");
                    editTextMin.setText("");
                    editTextSeg.setText("");
                    //se le informa al usuario que los datos ingresados son invalidos y que debe ingresarlos de nuevo
                    Toast.makeText(getApplicationContext(),R.string.dat_inco_distaz, Toast.LENGTH_LONG).show();
                }
            }
        });



        //Condicion para poder usar el boton de ultimo punto, que existan al menos 3 estaciones
        //esto ocurre en el segundo ingreso (se ingresa la tercera estacion, por lo que ya puede ser ultimo punto)
        if (poligonoActual.contEs <= 2) {
            //Como no hay sufiecientes estaciones para cerrar el poligono, se desactiva el RadioGroup y se set en No.

        //cuando ya se ingresaron tres estaciones o mas, es decir, el poligono ya se puede cerrar
        } else if (poligonoActual.contEs > 2){
            radioUltimoSiBoton.setEnabled(true);



            radioGrupoUltimo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if (radioUltimoNoBoton.isChecked()) {
                        cadi2 = "E-" + poligonoActual.contEs;
                        textPuOb.setText(cadi2);
                        ultimoPunto = false;
                        //Dentro del Listener del boton se establecer el proceso para guardar datos en poligono de trabajo

                    } else if (radioUltimoSiBoton.isChecked()) {
                        cadi2 = "E-0";
                        textPuOb.setText(cadi2);
                        ultimoPunto = true;

                    }
                }
            });

        }


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

    public void goToUrlDesa () {

        Uri uriUrl = Uri.parse("https://www.desarrollojlcp.com/manual-de-usuario-topografia-gps/");
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
    public void goToUrlFace () {
        Uri uriUrl = Uri.parse(getResources().getText(R.string.link_face).toString());
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
        finish();
        startActivity(intent);
    }
}
