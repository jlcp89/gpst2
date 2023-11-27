package com.desarrollojlcp.gps_topography.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.desarrollojlcp.gps_topography.model.csv.CSVWriter;
import com.desarrollojlcp.gps_topography.R;
import com.desarrollojlcp.gps_topography.model.db.ConexionSQLiteHelper;
import com.desarrollojlcp.gps_topography.model.db.Utilidades;
import com.desarrollojlcp.gps_topography.model.object.Estacion;
import com.desarrollojlcp.gps_topography.model.object.Poligono;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MostrarPoligono extends AppCompatActivity {
    String fecha, proyecto, cliente, ubicacion, responsable;
    int cantidadEstaciones = 0;
    Cursor todoCursor;
    Cursor cursorEstacionesPOL;
    int tPol = 0;
    int idPol = 0;
    SQLiteDatabase db;
    ConexionSQLiteHelper helper;
    Button botonCancelar, botonExportar;
    Poligono poligonoTemp;
    private String fechaNombreReporte = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mostrar_poligono);
        idPol = (int) getIntent().getExtras().getSerializable("id_pol");
        TextView tIdPol = (TextView) findViewById(R.id.texto_id_pol);
        TextView tFecha = (TextView) findViewById(R.id.text_fecha);
        TextView tNPuntos = (TextView) findViewById(R.id.text_numero_estaciones);
        TextView tTipoPol = (TextView) findViewById(R.id.text_tipo_pol);
        TextView tProyecto = (TextView) findViewById(R.id.text_proyecto);
        TextView tCliente = (TextView) findViewById(R.id.text_cliente);
        TextView tUbicacion = (TextView) findViewById(R.id.text_ubicacion);
        TextView tResponsable = (TextView) findViewById(R.id.texto_responsable);


        helper = new ConexionSQLiteHelper(this, Utilidades.NOMBRE_BD, null, Utilidades.VERSION_BD);
        db = helper.getReadableDatabase();

        todoCursor = db.rawQuery(Utilidades.SELECCIONAR_POLIGONO(idPol), null);
        if (todoCursor.getCount() == 1){
            todoCursor.moveToFirst();
            poligonoTemp = new Poligono();
            poligonoTemp.id_bd = idPol;
            poligonoTemp.setFecha(todoCursor.getString(1));
            poligonoTemp.tPol = todoCursor.getInt(2);
            poligonoTemp.setProyecto(todoCursor.getString(3));
            poligonoTemp.cliente = todoCursor.getString(4);
            poligonoTemp.ubicacion = todoCursor.getString(5);
            poligonoTemp.responsable = todoCursor.getString(6);
            poligonoTemp.setTipoMedicion(todoCursor.getString(7));
            cursorEstacionesPOL = db.rawQuery(Utilidades.SELECCIONAR_ESTACIONES_DE_POLIGONO(idPol), null);
            cantidadEstaciones = cursorEstacionesPOL.getCount();
            tIdPol.setText( String.valueOf(idPol));
            tFecha.setText(poligonoTemp.getFecha());
            tNPuntos.setText(String.valueOf(cantidadEstaciones));
            String textoTipoPol = "";
            if (poligonoTemp.tPol == 0){
                textoTipoPol = "gMap";
            } else if(poligonoTemp.tPol == 1){
                textoTipoPol = "Survey";
            }
            tTipoPol.setText(textoTipoPol);
            tProyecto.setText(poligonoTemp.getProyecto());
            tCliente.setText(poligonoTemp.cliente);
            tUbicacion.setText(poligonoTemp.ubicacion);
            tResponsable.setText(poligonoTemp.responsable);
            db.close();
        }




        Button botonCargar = (Button) findViewById(R.id.boton_load);
        botonCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                poligonoTemp.xCen = 125;
                poligonoTemp.yCen = 1000;
                poligonoTemp.displayScale = 1.0f;


                if (poligonoTemp.tPol == 0){
                    Estacion e0 = new Estacion();
                    cursorEstacionesPOL.moveToFirst();
                    e0.id_bd = cursorEstacionesPOL.getInt(1);
                    e0.idEst = cursorEstacionesPOL.getString(2);
                    e0.setLat(cursorEstacionesPOL.getDouble(8));
                    e0.setLon(cursorEstacionesPOL.getDouble(9));
                    e0.setObservaciones(cursorEstacionesPOL.getString(10));
                    e0.setAlt(cursorEstacionesPOL.getDouble(11));

                    poligonoTemp.ingresarEstacion(e0);
                    while (cursorEstacionesPOL.moveToNext()) {
                        Estacion e = new Estacion();
                        e.id_bd = cursorEstacionesPOL.getInt(1);
                        e.idEst = cursorEstacionesPOL.getString(2);
                        e.setLat(cursorEstacionesPOL.getDouble(8));
                        e.setLon(cursorEstacionesPOL.getDouble(9));
                        e.setObservaciones(cursorEstacionesPOL.getString(10));
                        e.setAlt(cursorEstacionesPOL.getDouble(11));
                        poligonoTemp.ingresarEstacion(e);

                    }



                    Intent intent = new Intent(getApplicationContext(), Ingreso.class);
                    intent.putExtra("pol_trab", poligonoTemp);
                    finish();
                    startActivity(intent);

                } else if (poligonoTemp.tPol == 1){
                    Estacion e0 = new Estacion();
                    cursorEstacionesPOL.moveToFirst();
                    e0.id_bd = cursorEstacionesPOL.getInt(1);
                    e0.idEst = cursorEstacionesPOL.getString(2);
                    e0.dist = cursorEstacionesPOL.getDouble(3);
                    e0.valorDecimalGrados = cursorEstacionesPOL.getDouble(4);
                    int valor = cursorEstacionesPOL.getInt(5);
                    if (valor == 1){e0.ultimoPunto = true;}else{e0.ultimoPunto = false;}
                    valor = cursorEstacionesPOL.getInt(6);
                    if (valor == 1){
                        e0.setPartePoligonoFinal(true);}else{
                        e0.setPartePoligonoFinal(false);}
                    e0.tipoMedicion = cursorEstacionesPOL.getString(7);
                    e0.setObservaciones(cursorEstacionesPOL.getString(10));

                    e0.valorRadianes =  Math.toRadians(e0.valorDecimalGrados);

                    int grados = (int) e0.valorDecimalGrados;
                    //estacionesPolFinal.elementAt(i).grado = grados;
                    e0.grado = grados;
                    double minutos = ((e0.valorDecimalGrados - grados)*60);
                    int min = (int) minutos;
                    e0.minuto = min;
                    double seg = (minutos - min) * 60;
                    e0.segundo = seg;


                    e0.seno   = Math.sin(Math.toRadians(e0.valorDecimalGrados));
                    e0.coseno = Math.cos(Math.toRadians(e0.valorDecimalGrados));

                    poligonoTemp.ingresarEstacion(e0);


                    SQLiteDatabase db2 = helper.getReadableDatabase();
                    Cursor cursorRadiacionesEst = db2.rawQuery(Utilidades.SELECCIONAR_RADIACIONES_ESTACION(e0.id_bd), null);

                    if (cursorRadiacionesEst.getCount()>0){
                        cursorRadiacionesEst.moveToFirst();
                        Estacion r0 = new Estacion ();
                        r0.id_bd = cursorRadiacionesEst.getInt(2);
                        r0.idEst = cursorRadiacionesEst.getString(3);
                        r0.dist = cursorRadiacionesEst.getDouble(4);
                        r0.valorDecimalGrados = cursorRadiacionesEst.getDouble(5);
                        valor = cursorRadiacionesEst.getInt(6);
                        if (valor == 1){r0.ultimoPunto = true;}else{r0.ultimoPunto = false;}
                        valor = cursorRadiacionesEst.getInt(7);
                        if (valor == 1){
                            r0.setPartePoligonoFinal(true);}else{
                            r0.setPartePoligonoFinal(false);}
                        r0.valorRadianes =  Math.toRadians(r0.valorDecimalGrados);
                        r0.setObservaciones(cursorRadiacionesEst.getString(10));

                        grados = (int) r0.valorDecimalGrados;
                        //estacionesPolFinal.elementAt(i).grado = grados;
                        r0.grado = grados;
                        minutos = ((r0.valorDecimalGrados - grados)*60);
                        min = (int) minutos;
                        r0.minuto = min;
                        seg = (minutos - min) * 60;
                        r0.segundo = seg;

                        r0.seno   = Math.sin(Math.toRadians(r0.valorDecimalGrados));
                        r0.coseno = Math.cos(Math.toRadians(r0.valorDecimalGrados));
                        poligonoTemp.estaciones.elementAt(0).radiaciones.addElement(r0);

                        while (cursorRadiacionesEst.moveToNext()) {
                            Estacion r = new Estacion ();
                            r.id_bd = cursorRadiacionesEst.getInt(2);
                            r.idEst = cursorRadiacionesEst.getString(3);
                            r.dist = cursorRadiacionesEst.getDouble(4);
                            r.valorDecimalGrados = cursorRadiacionesEst.getDouble(5);
                            valor = cursorRadiacionesEst.getInt(6);
                            if (valor == 1){r.ultimoPunto = true;}else{r.ultimoPunto = false;}
                            valor = cursorRadiacionesEst.getInt(7);
                            if (valor == 1){
                                r.setPartePoligonoFinal(true);}else{
                                r.setPartePoligonoFinal(false);}
                            r.setObservaciones(cursorRadiacionesEst.getString(10));

                            r.valorRadianes =  Math.toRadians(r.valorDecimalGrados);

                            grados = (int) r.valorDecimalGrados;
                            //estacionesPolFinal.elementAt(i).grado = grados;
                            r.grado = grados;
                            minutos = ((r.valorDecimalGrados - grados)*60);
                            min = (int) minutos;
                            r.minuto = min;
                            seg = (minutos - min) * 60;
                            r.segundo = seg;
                            r.seno   = Math.sin(Math.toRadians(r.valorDecimalGrados));
                            r.coseno = Math.cos(Math.toRadians(r.valorDecimalGrados));
                            poligonoTemp.estaciones.elementAt(0).radiaciones.addElement(r);

                        }

                    }

                    int contador = 1;
                    while (cursorEstacionesPOL.moveToNext()) {
                        Estacion e = new Estacion();
                        e.id_bd = cursorEstacionesPOL.getInt(1);
                        e.idEst = cursorEstacionesPOL.getString(2);
                        e.dist = cursorEstacionesPOL.getDouble(3);
                        e.valorDecimalGrados = cursorEstacionesPOL.getDouble(4);
                        valor = cursorEstacionesPOL.getInt(5);
                        if (valor == 1){e.ultimoPunto = true;}else{e.ultimoPunto = false;}
                        valor = cursorEstacionesPOL.getInt(6);
                        if (valor == 1){
                            e.setPartePoligonoFinal(true);}else{
                            e.setPartePoligonoFinal(false);}
                        e.tipoMedicion = cursorEstacionesPOL.getString(7);
                        e.valorRadianes =  Math.toRadians(e.valorDecimalGrados);
                        e.setObservaciones(cursorRadiacionesEst.getString(10));

                        grados = (int) e.valorDecimalGrados;
                        //estacionsPolFinal.elementAt(i).grado = grados;
                        e.grado = grados;
                        minutos = ((e.valorDecimalGrados - grados)*60);
                        min = (int) minutos;
                        e.minuto = min;
                        seg = (minutos - min) * 60;
                        e.segundo = seg;
                        e.seno   = Math.sin(Math.toRadians(e.valorDecimalGrados));
                        e.coseno = Math.cos(Math.toRadians(e.valorDecimalGrados));
                        poligonoTemp.ingresarEstacion(e);

                        cursorRadiacionesEst = db2.rawQuery(Utilidades.SELECCIONAR_RADIACIONES_ESTACION(e.id_bd), null);
                        if (cursorRadiacionesEst.getCount()>0){
                            cursorRadiacionesEst.moveToFirst();
                            Estacion r0 = new Estacion ();
                            r0.id_bd = cursorRadiacionesEst.getInt(2);
                            r0.idEst = cursorRadiacionesEst.getString(3);
                            r0.dist = cursorRadiacionesEst.getDouble(4);
                            r0.valorDecimalGrados = cursorRadiacionesEst.getDouble(5);
                            valor = cursorRadiacionesEst.getInt(6);
                            if (valor == 1){r0.ultimoPunto = true;}else{r0.ultimoPunto = false;}
                            valor = cursorRadiacionesEst.getInt(7);
                            r0.setObservaciones(cursorRadiacionesEst.getString(10));

                            if (valor == 1){
                                r0.setPartePoligonoFinal(true);}else{
                                r0.setPartePoligonoFinal(false);}
                            r0.valorRadianes =  Math.toRadians(r0.valorDecimalGrados);
                            grados = (int) r0.valorDecimalGrados;
                            //estacionesPolFinal.elementAt(i).grado = grados;
                            r0.grado = grados;
                            minutos = ((r0.valorDecimalGrados - grados)*60);
                            min = (int) minutos;
                            r0.minuto = min;
                            seg = (minutos - min) * 60;
                            r0.segundo = seg;
                            r0.seno   = Math.sin(Math.toRadians(r0.valorDecimalGrados));
                            r0.coseno = Math.cos(Math.toRadians(r0.valorDecimalGrados));
                            poligonoTemp.estaciones.elementAt(contador).radiaciones.addElement(r0);

                            while (cursorRadiacionesEst.moveToNext()) {
                                Estacion r = new Estacion ();
                                r.id_bd = cursorRadiacionesEst.getInt(2);
                                r.idEst = cursorRadiacionesEst.getString(3);
                                r.dist = cursorRadiacionesEst.getDouble(4);
                                r.valorDecimalGrados = cursorRadiacionesEst.getDouble(5);
                                valor = cursorRadiacionesEst.getInt(6);
                                if (valor == 1){r.ultimoPunto = true;}else{r.ultimoPunto = false;}
                                valor = cursorRadiacionesEst.getInt(7);
                                r.setObservaciones(cursorRadiacionesEst.getString(10));

                                if (valor == 1){
                                    r.setPartePoligonoFinal(true);}else{
                                    r.setPartePoligonoFinal(false);}
                                r.valorRadianes =  Math.toRadians(r.valorDecimalGrados);
                                grados = (int) r.valorDecimalGrados;
                                //estacionesPolFinal.elementAt(i).grado = grados;
                                r.grado = grados;
                                minutos = ((r.valorDecimalGrados - grados)*60);
                                min = (int) minutos;
                                r.minuto = min;
                                seg = (minutos - min) * 60;
                                r.segundo = seg;
                                r.seno   = Math.sin(Math.toRadians(r.valorDecimalGrados));
                                r.coseno = Math.cos(Math.toRadians(r.valorDecimalGrados));
                                poligonoTemp.estaciones.elementAt(contador).radiaciones.addElement(r);

                            }

                        }
                        contador +=1;
                    }
                    db2.close();
                    Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
                    intent.putExtra("pol_trab", poligonoTemp);
                    finish();
                    startActivity(intent);
                }


            }

        });


        Button botonBorrar = (Button) findViewById(R.id.boton_agregar_borrar);
        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db2 = helper.getWritableDatabase();
                db2.execSQL(Utilidades.BORRAR_POLIGONO(idPol));
                db2.execSQL(Utilidades.BORRAR_ESTACIONES_DE_POLIGONO(idPol));
                db2.close();
                Intent intent = new Intent(getApplicationContext(), Cargar.class);
                startActivity(intent);
                finish();

            }
        });

        botonCancelar = (Button) findViewById(R.id.boton_cancelar_mostrarpol);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Cargar.class);
                startActivity(intent);
                finish();

            }
        });

        botonExportar = (Button) findViewById(R.id.boton_exportar);
        botonExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportPoligono(idPol);
                Toast.makeText(getApplicationContext(),"Project " +String.valueOf(idPol)+" successfully exported!  - " + rutaCarpetaActualExportar,Toast.LENGTH_LONG).show();


            }
        });


    }

    public void setFecha (){
        final String fecha1 = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
        String ano = fecha1.substring(17,19);
        String mes = fecha1.substring(12,14);
        String dia = fecha1.substring(9,11);
        String hor = fecha1.substring(0,2);
        String min = fecha1.substring(3,5);
        fechaNombreReporte = ano+mes+dia+hor+min;
        //poligonoActual.fecha = fecha1;
        fechaNombreReporte.replace(" ","");
    }

    private String rutaCarpetaActualExportar = "";

    private void exportPoligono(int idPolExportar) {

        ConexionSQLiteHelper dbhelper = helper;

        setFecha();
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        String nomCarpetaGPSTExported = "GPS_Topography_Exported";
        fechaNombreReporte.replace(" ","");
        rutaCarpetaActualExportar = tarjetaSD + File.separator + nomCarpetaGPSTExported
                + File.separator +"ID Pol: "+String.valueOf(idPol)+ " - "+ fechaNombreReporte;

        File carpetaProyectoGPSTActual = new File (rutaCarpetaActualExportar);
        if (!carpetaProyectoGPSTActual.exists()) {
            carpetaProyectoGPSTActual.mkdirs();
        }

        String textoNombreArchivo = "Poligono.csv";
        File file = new File(carpetaProyectoGPSTActual, textoNombreArchivo);
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            Cursor curCSV = db.rawQuery(Utilidades.POLIGONO_EXPORTAR(idPolExportar),null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                //String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i = 0; i<curCSV.getColumnCount();i++){
                    arrStr[i] = curCSV.getString(i);
                }

                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("Fallo generar poligono", sqlEx.getMessage(), sqlEx);
        }

        textoNombreArchivo = "Estaciones.csv";
        file = new File(carpetaProyectoGPSTActual, textoNombreArchivo);

        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            Cursor curCSV = db.rawQuery(Utilidades.ESTACIONES_EXPORTAR(idPolExportar),null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                //String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i = 0; i<curCSV.getColumnCount();i++){
                    arrStr[i] = curCSV.getString(i);
                }

                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("Fallo generar estacio", sqlEx.getMessage(), sqlEx);
        }

        textoNombreArchivo = "Radiaciones.csv";
        file = new File(carpetaProyectoGPSTActual, textoNombreArchivo);

        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();

            Cursor curCSV = db.rawQuery(Utilidades.RADIACIONES_EXPORTAR(idPolExportar),null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext())
            {
                //Which column you want to exprort
                //String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2)};
                String arrStr[] = new String[curCSV.getColumnCount()];
                for (int i = 0; i<curCSV.getColumnCount();i++){
                    arrStr[i] = curCSV.getString(i);
                }

                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        }
        catch(Exception sqlEx)
        {
            Log.e("Fallo generar radiacion", sqlEx.getMessage(), sqlEx);
        }
    }

    @Override
    public void onPause() {super.onPause();}

    @Override
    public void onResume() {super.onResume();}

    @Override
    public void onDestroy(){super.onDestroy(); }

    @Override
    public void onBackPressed(){botonCancelar.performClick();}


}
