package com.desarrollojlcp.gps_topography;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.desarrollojlcp.gps_topography.utilidades.Utilidades;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Cargar extends AppCompatActivity {

    private Button botonRegresar, botonExportar, botonImportar;
    private String fechaNombreReporte;
    private Poligono poligonoActual = new Poligono();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_guardar);
        poligonoActual.primeraPantalla = true;
        poligonoActual.limpiarPoligono();
        ConexionSQLiteHelper helper = new ConexionSQLiteHelper(this, Utilidades.NOMBRE_BD, null, Utilidades.VERSION_BD);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor todoCursor = db.rawQuery(Utilidades.SELECCIONAR_TODOS_POLIGONOS, null);
        todoCursor.moveToFirst();
        ArrayList<String> dataPol = new ArrayList<String>();
        if (todoCursor.getCount()>0){
            int idPol = todoCursor.getInt(0);
            String fecha = todoCursor.getString(1);
            Cursor cursorEstacionesPOL = db.rawQuery(Utilidades.SELECCIONAR_ESTACIONES_DE_POLIGONO(idPol), null);
            String proyecto = todoCursor.getString(3);
            String textoLista = "Pol ID: "+idPol +"  -  "+getResources().getText(R.string.proyecto)+"  "+proyecto+ "  -  "+getResources().getText(R.string.fecha)+" "+ fecha + "  -  # Est: "+ cursorEstacionesPOL.getCount() ;
            dataPol.add(textoLista);
            while (todoCursor.moveToNext()) {
                idPol = todoCursor.getInt(0);
                fecha = todoCursor.getString(1);
                proyecto = todoCursor.getString(3);
                cursorEstacionesPOL = db.rawQuery(Utilidades.SELECCIONAR_ESTACIONES_DE_POLIGONO(idPol), null);

                textoLista = "Pol ID: "+idPol +"  -  "+getResources().getText(R.string.proyecto)+"  "+proyecto+ "  -  "+getResources().getText(R.string.fecha)+" "+ fecha + "  -  # Est: "+ cursorEstacionesPOL.getCount() ;
                dataPol.add(textoLista);
            }

            // Find ListView to populate
            final ListView lvItems = (ListView) findViewById(R.id.list_view);
            // Setup cursor adapter using cursor from last step
            lvItems.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, dataPol));
            lvItems.setBackgroundColor(getResources().getColor(R.color.azul));
            lvItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = (String)lvItems.getItemAtPosition(position);
                    //Toast.makeText(getApplicationContext(),"You selected : " + position, Toast.LENGTH_SHORT).show();
                    String []textItem = item.split(" ");
                    int idPol = Integer.valueOf(textItem[2]);
                    Intent intent = new Intent(getApplicationContext(), MostrarPoligono.class);
                    intent.putExtra("id_pol", idPol);
                    startActivity(intent);
                    finish();
                }
            });
        }


        db.close();

        botonRegresar = (Button) findViewById(R.id.boton_pantallacargar_regresar);
        botonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/csv");
                startActivityForResult(Intent.createChooser(intent, "Open CSV"), READ_REQUEST_CODE);
            }
        });


        /*String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        String nomCarpetaGPST = "GPS_Topography_Databases";
        final String rutaCarpetaActual = tarjetaSD + File.separator + nomCarpetaGPST;

        File carpetaProyectosGPST = new File (rutaCarpetaActual);
        if (!carpetaProyectosGPST.exists()) {
            carpetaProyectosGPST.mkdirs();
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        proImportCSV(new File(data.getData().getPath()));
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void proImportCSV(File archivoPoligono){
        try {

            Long newPolId;
            String rutaArchivoPol = archivoPoligono.getPath();
            String [] rutaArchivoPolCortada = rutaArchivoPol.split(File.separator);
            int tamanoRutaArchivoPolCortada = rutaArchivoPolCortada.length;
            String rutaCarpetaProyectoImportar = "";

            for (int i = 0; i < tamanoRutaArchivoPolCortada - 1; i++) {
                rutaCarpetaProyectoImportar += rutaArchivoPolCortada[i] + File.separator;
            }

            String rutaArchivoEstaciones = rutaCarpetaProyectoImportar + File.separator + "Estaciones.csv";
            File archivoEstaciones = new File(rutaArchivoEstaciones);

            String rutaArchivoRadiaciones = rutaCarpetaProyectoImportar + File.separator + "Radiaciones.csv";
            File archivoRadiaciones = new File(rutaArchivoRadiaciones);

            ContentValues valuesPoligono = new ContentValues();
            ConexionSQLiteHelper helper = new ConexionSQLiteHelper(this, Utilidades.NOMBRE_BD, null, Utilidades.VERSION_BD);

            SQLiteDatabase db = helper.getWritableDatabase();

            CSVReader dataRead = new CSVReader(new FileReader(archivoPoligono)); // <--- This line is key, and why it was reading the wrong file

            String[] valoresLeidosPoligono = null;
            while((valoresLeidosPoligono = dataRead.readNext())!=null) {

                valuesPoligono.clear();
                valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_FECHA, valoresLeidosPoligono[1]);

                int tipoPol = Integer.valueOf(valoresLeidosPoligono[2]);
                valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_TIPO_POL, tipoPol);
                valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_PROYECTO, valoresLeidosPoligono[3]);
                valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_CLIENTE, valoresLeidosPoligono[4]);
                valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_UBICACION, valoresLeidosPoligono[5]);
                valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_RESPONSABLE, valoresLeidosPoligono[6]);
                valuesPoligono.put(Utilidades.PoligonoEntry.CAMPO_TIPO_MEDICION, valoresLeidosPoligono[7]);

                newPolId = db.insert(Utilidades.PoligonoEntry.TABLA_POLIGONOS, null, valuesPoligono);

                if (tipoPol == 0){

                    ContentValues valuesEstacion = new ContentValues();
                    CSVReader dataReadEstaciones = new CSVReader(new FileReader(archivoEstaciones)); // <--- This line is key, and why it was reading the wrong file
                    String[] valoresLeidosEstaciones = null;



                } else if (tipoPol == 1) {

                }

            } dataRead.close();

            db.close();

        } catch (Exception e) { Log.e("TAG",e.toString());

        }
    }

    private static final int READ_REQUEST_CODE = 42;


    @Override
    public void onPause() {super.onPause();}

    @Override
    public void onResume() {super.onResume();}

    @Override
    public void onDestroy(){super.onDestroy();}

    @Override
    public void onBackPressed(){
        poligonoActual = new Poligono();
        Intent intent = new Intent(getApplicationContext(), Ingreso.class);
        intent.putExtra("pol_trab", poligonoActual);
        startActivity(intent);
        finish();
    }

}
