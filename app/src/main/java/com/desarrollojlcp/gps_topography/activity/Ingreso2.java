package com.desarrollojlcp.gps_topography.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.desarrollojlcp.gps_topography.R;
import com.desarrollojlcp.gps_topography.model.db.ConexionSQLiteHelper;
import com.desarrollojlcp.gps_topography.model.object.Estacion;
import com.desarrollojlcp.gps_topography.model.object.Poligono;
import com.desarrollojlcp.gps_topography.model.object.cabezera;
import com.desarrollojlcp.gps_topography.model.db.Utilidades;

import com.desarrollojlcp.gps_topography.view.VistaTrabajo;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jsevy.adxf.DXFCanvas;
import com.jsevy.adxf.DXFDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


public class Ingreso2 extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_INVITE = 101;
    private static final String TAG = "Ingreso2" ;
    private VistaTrabajo vistaTrabajo;
    private boolean perGPS = false;
    String fechaNombreReporte = "";
    final String fecha1 = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
    double longitudGPS, latitudGPS, elevacionGPS;
    LocationManager locationManager;
    private static Vector<Estacion> estacionesD = new Vector<Estacion>();
    private boolean yaEsta = false;//vector para contener todas las estaciones
    private GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private boolean videoCargado = false;
    private Location locationActual;
    private Paint paint = new Paint();
    public static String encabezado;
    public static String autor;
    public static PdfPTable tabla1;
    Phrase ESPACIO = new Phrase(" ");
    public static File file;
    Font fuente = new Font();
    private TextView textoResultados;
    public boolean primerUso = true;
    double areaP = 0;
    double perimetroP = 0;
    protected static String firma, pagina, de;
    float zoom = 17;
    boolean mostrarInfoMarcadores = true;
    Poligono poligonoActual = new Poligono();
    private Canvas canvasLimpiar = new Canvas();
    ConexionSQLiteHelper helper;
    private FloatingActionButton botonCompartir, botonPoligono, botonLinea, botonPunto, botonCentrar, botonInfo, botonAgregarPunto, botonUnDo, botonRecalcular, botonAnalizar, botonPro, botonGuardar, botonCargar;

    private boolean valorSuscripcion = false;


    private void agregarFindIdS() {
        botonInfo = findViewById(R.id.boton_info);
        botonAgregarPunto = findViewById(R.id.boton_agregar_punto);
        botonUnDo = findViewById(R.id.boton_undo);
        botonRecalcular = findViewById(R.id.boton_recalcular);
        botonAnalizar = findViewById(R.id.boton_analizar);
        botonCentrar = findViewById(R.id.boton_centrar);
        botonPoligono = findViewById(R.id.boton_poligono);
        botonLinea = findViewById(R.id.boton_linea);
        botonPunto = findViewById(R.id.boton_punto2);
        botonPro = findViewById(R.id.boton_pro);
        botonGuardar = findViewById(R.id.boton_guardar);
        botonCargar = findViewById(R.id.boton_cargar);
        botonCompartir = findViewById(R.id.boton_compartir);
        textoResultados = findViewById(R.id.texto_resultados);
    }

    public void setFecha (){
        final String fecha1 = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
        String ano = fecha1.substring(17,19);
        String mes = fecha1.substring(12,14);
        String dia = fecha1.substring(9,11);
        String hor = fecha1.substring(0,2);
        String min = fecha1.substring(3,5);
        fechaNombreReporte = ano+mes+dia+hor+min;
        poligonoActual.setFecha(fecha1);
        fechaNombreReporte.replace(" ","");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.guardar) {
            botonGuardar.performClick();
            return true;
        } else if (itemId == R.id.cargar) {
            botonCargar.performClick();
            return true;
        } else if (itemId == R.id.cambiar_pantalla) {
            botonRecalcular.performClick();
            return true;
        } else if (itemId == R.id.poligono) {
            botonPoligono.performClick();
            return true;
        } else if (itemId == R.id.linea) {
            botonLinea.performClick();
            return true;
        } else if (itemId == R.id.puntos) {
            botonPunto.performClick();
            return true;
        } else if (itemId == R.id.capturar_ubicacion_actual) {
            botonAgregarPunto.performClick();
            return true;
        } else if (itemId == R.id.acerca_de) {
            abirLink("https://jlcp89.github.io/d3sarrollo/#/gpst");
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean getSubscribeValueFromPref(){
        return getPreferenceObject().getBoolean( SUBSCRIBE_KEY,false);
    }

    protected SharedPreferences getPreferenceObject() {
        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }

    public static final String SUBSCRIBE_KEY= "subscribe";
    public static final String PREF_FILE= "preferenciaSusGPSTpro";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valorSuscripcion = getSubscribeValueFromPref();

        setContentView(R.layout.activity_ingreso2);

        vistaTrabajo = (VistaTrabajo) findViewById(R.id.vista_trabajo);
        agregarFindIdS();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        try {
            poligonoActual = (Poligono) getIntent().getExtras().getSerializable("pol_trab");
        } catch (NullPointerException e){
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
            Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
            intent.putExtra("pol_trab", poligonoTemp);
            finish();
            startActivity(intent);
        }



        if (poligonoActual.estaciones.size()==1){
            if (poligonoActual.estaciones.elementAt(0).radiaciones.size() == 0){
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
                    builder.setTitle("Confirm");
                    builder.setMessage("E-0 is part of the final polygon?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            poligonoActual.estaciones.elementAt(0).setPartePoligonoFinal(true);
                            vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            poligonoActual.estaciones.elementAt(0).setPartePoligonoFinal(false);
                            vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                } catch (Exception e) {
                    poligonoActual.estaciones.elementAt(0).setPartePoligonoFinal(true);
                    vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
                }
            }
        }
        vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
        textoResultados = findViewById(R.id.texto_resultados);
        firma  = getResources().getText(R.string.firma_sello).toString();
        pagina = getResources().getText(R.string.pagina).toString() + " ";
        de = getResources().getText(R.string.de).toString();
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        helper = new ConexionSQLiteHelper(this, Utilidades.NOMBRE_BD, null, Utilidades.VERSION_BD);
        setFecha();

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (poligonoActual.estaciones.size()>0){
                    poligonoActual.tPol = 1;
                    Intent intent = new Intent(getApplicationContext(), GuardarPoligono.class);
                    intent.putExtra("pol_trab", poligonoActual);
                    startActivity(intent);
                } else {
                }

            }
        });

        botonCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preguntarSalir();
            }
        });

        botonPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://play.google.com/store/apps/details?id=com.desarrollojlcp.gps_topography_pro";
                abirLink(url);
            }
        });

        botonCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInviteClicked();
            }
        });

        botonAgregarPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poligonoActual.calculosParaPoligonoFinal();
                poligonoActual.xCen = vistaTrabajo.xCen;
                poligonoActual.yCen = vistaTrabajo.yCen;
                poligonoActual.displayScale = vistaTrabajo.displayScale;
                Intent intent = new Intent(getApplicationContext(), IngresoDistanciaAzimutEstacion.class);
                intent.putExtra("pol_trab", poligonoActual);
                finish();
                startActivity(intent);

            }
        });


        botonCentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                poligonoActual.xCen = 125;
                poligonoActual.yCen = 900;
                poligonoActual.displayScale = 1.0f;
                vistaTrabajo.actualizarVistaTrabajo(poligonoActual);

            }
        });

        botonUnDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tamañoVectorEstaciones = poligonoActual.estaciones.size();
                if (tamañoVectorEstaciones > 0){
                    int indexUltimoElemento = tamañoVectorEstaciones -1;
                    Estacion ultimaEstacion = poligonoActual.estaciones.elementAt(indexUltimoElemento);
                    if ((tamañoVectorEstaciones > 1) || (ultimaEstacion.radiaciones.size()>0) ){
                        if (ultimaEstacion.radiaciones.size()>0){
                            poligonoActual.xCen = vistaTrabajo.xCen;
                            poligonoActual.yCen = vistaTrabajo.yCen;
                            poligonoActual.displayScale = vistaTrabajo.displayScale;
                            ultimaEstacion.radiaciones.removeElementAt(ultimaEstacion.radiaciones.size()-1);
                            poligonoActual.estaciones.setElementAt(ultimaEstacion, (indexUltimoElemento));
                            vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
                        } else if (ultimaEstacion.radiaciones.size() == 0) {
                            poligonoActual.xCen = vistaTrabajo.xCen;
                            poligonoActual.yCen = vistaTrabajo.yCen;
                            poligonoActual.displayScale = vistaTrabajo.displayScale;
                            poligonoActual.estaciones.removeElementAt(indexUltimoElemento);
                            poligonoActual.contEs = poligonoActual.contEs - 1;
                            vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
                        }

                    }else if ((tamañoVectorEstaciones == 1) && (ultimaEstacion.radiaciones.size() == 0)){
                        poligonoActual.xCen = vistaTrabajo.xCen;
                        poligonoActual.yCen = vistaTrabajo.yCen;
                        poligonoActual.displayScale = vistaTrabajo.displayScale;
                        poligonoActual.contEs = 0;
                        vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
                        Toast.makeText(getApplicationContext(),getResources().getText(R.string.no_estacion),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        botonAnalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                poligonoActual.calculoCoorParciales();
                poligonoActual.calculoCoorTotalesDibujar(0,0);

                poligonoActual.estacionesPolFinal.removeAllElements();

                poligonoActual.calculosParaPoligonoFinal();
                poligonoActual.calculosParaPoligonoFinal2();

                if (poligonoActual.estacionesPolFinal.size() > 3){

                    calcular();
                    generarArchivos();


                } else {
                    Toast.makeText(getApplicationContext(),getResources().getText(R.string.tres_puntos),Toast.LENGTH_LONG).show();
                }
            }
        });

        botonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Info.class);
                startActivity(intent);
            }
        });

        botonRecalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (poligonoActual.estaciones.size()>0){
                    if ((poligonoActual.estaciones.size()>1)||(poligonoActual.estaciones.elementAt(0).radiaciones.size()>0)){
                        preguntarSalirLevamtamiento();
                    } else {
                        poligonoActual.limpiarPoligono();
                        Intent intent = new Intent(getApplicationContext(), Ingreso.class);
                        intent.putExtra("pol_trab", poligonoActual);
                        finish();
                        startActivity(intent);

                    }
                } else {
                    poligonoActual.limpiarPoligono();
                    Intent intent = new Intent(getApplicationContext(), Ingreso.class);
                    intent.putExtra("pol_trab", poligonoActual);
                    finish();
                    startActivity(intent);

                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("POL2", poligonoActual);
        savedInstanceState.putFloat("XCEN", vistaTrabajo.xCen);
        savedInstanceState.putFloat("YCEN", vistaTrabajo.yCen);
        savedInstanceState.putFloat("DIS_ES", vistaTrabajo.displayScale);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        Poligono polTemp = (Poligono) savedInstanceState.getSerializable("POL2");
        Float xCen2 = (Float) savedInstanceState.getFloat("XCEN");
        Float yCen2 = (Float) savedInstanceState.getFloat("YCEN");
        Float displayScale2 = (Float) savedInstanceState.getFloat("DIS_ES");
        poligonoActual = polTemp;
        poligonoActual.xCen = xCen2;
        poligonoActual.yCen = yCen2;
        poligonoActual.displayScale = displayScale2;
        vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
    }


    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse("android.resource://com.desarrollojlcp.gps_topography_pro/drawable/compartir"))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
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


    public void calcular(){
        poligonoActual.calculoCoorParciales2();
        poligonoActual.calculoNyS2();
        poligonoActual.calculoEyO2();
        poligonoActual.calculoErrorCierre2();
        poligonoActual.calculoCorrecciones2();
        poligonoActual.calculoCoorCompensadas2();
        poligonoActual.calculoCoorTotales2();
        poligonoActual.calculoAreaPoligono2();
        poligonoActual.calcularPerimetro();
        areaP = poligonoActual.getArea();
        perimetroP = poligonoActual.getSumaDist();
    }



    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        vistaTrabajo.actualizarVistaTrabajo(poligonoActual);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBackPressed() {
        try {
            new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getText(R.string.cerrando_app))
                    .setMessage(getResources().getText(R.string.seguro_cerrar_app))
                    .setPositiveButton(getResources().getText(R.string.si), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton(getResources().getText(R.string.no), null)
                    .show();
        } catch (Exception e){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void preguntarSalir (){
        try {
            new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getText(R.string.cerrando_levantamiento))
                    .setMessage(getResources().getText(R.string.seguro_salir_levantamiento))
                    .setPositiveButton(getResources().getText(R.string.si), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            poligonoActual.limpiarPoligono();
                            Intent intent = new Intent(getApplicationContext(), Cargar.class);
                            startActivity(intent);
                            finish();
                        }

                    })
                    .setNegativeButton(getResources().getText(R.string.no), null)
                    .show();
        }catch (Exception e) {
            poligonoActual.limpiarPoligono();
            Intent intent = new Intent(getApplicationContext(), Cargar.class);
            startActivity(intent);
            finish();
        }

    }

    public void preguntarSalirLevamtamiento (){
        try {
            new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getText(R.string.cerrando_levantamiento))
                    .setMessage(getResources().getText(R.string.seguro_salir_levantamiento))
                    .setPositiveButton(getResources().getText(R.string.si), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            poligonoActual.limpiarPoligono();
                            Intent intent = new Intent(getApplicationContext(), Ingreso.class);
                            intent.putExtra("pol_trab", poligonoActual);
                            finish();
                            startActivity(intent);

                        }

                    })
                    .setNegativeButton(getResources().getText(R.string.no), null)
                    .show();
        } catch (Exception e){

            poligonoActual.limpiarPoligono();
            Intent intent = new Intent(getApplicationContext(), Ingreso.class);
            intent.putExtra("pol_trab", poligonoActual);
            finish();
            startActivity(intent);
        }

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void generarArchivos(){
        setFecha();
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        String nomCarpetaGPST = "GPS_Topography_Projects";
        poligonoActual.rutaCarpetaActual = tarjetaSD+ File.separator + nomCarpetaGPST
                + File.separator +fechaNombreReporte;
        poligonoActual.rutaCarpetaActual2 = nomCarpetaGPST
                + File.separator +fechaNombreReporte;
        String nombreArchivoCSV = fechaNombreReporte+".txt";
        String nombreArchivoDXF = fechaNombreReporte+".dxf";
        poligonoActual.nombreArchivoImagen = fechaNombreReporte+".jpg";
        fechaNombreReporte =      fechaNombreReporte+".pdf";
        fechaNombreReporte.replace(" ","");
        poligonoActual.nombreArchivoCSV = nombreArchivoCSV;
        poligonoActual.nombreArchivoGuardado = fechaNombreReporte;
        poligonoActual.nombreArchivoDXF = nombreArchivoDXF;
        poligonoActual.rutaArchivoDXF = poligonoActual.rutaCarpetaActual + File.separator + poligonoActual.nombreArchivoDXF;
        File carpetaProyectosGPST = new File (poligonoActual.rutaCarpetaActual);
        if (!carpetaProyectosGPST.exists()) {
            carpetaProyectosGPST.mkdirs();
        }
        File carpetaProyectoActual = new File (poligonoActual.rutaCarpetaActual2);
        if (!carpetaProyectoActual.exists()){
            carpetaProyectoActual.mkdirs();
        }

        if (valorSuscripcion) {
            generarPDF(poligonoActual.rutaCarpetaActual);
            generarTXT(poligonoActual.rutaCarpetaActual);
            generarDXF();
            Toast.makeText(getApplicationContext(), "PRO User, 3 files generated!", Toast.LENGTH_LONG).show();

        } else {
            generarTXT(poligonoActual.rutaCarpetaActual);
            Toast.makeText(getApplicationContext(), "Free user, only TXT file was generated", Toast.LENGTH_LONG).show();

        }

        //takeScreenshot(poligonoActual.rutaCarpetaActual);
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.archivos).toString() + "!  " + poligonoActual.rutaCarpetaActual, Toast.LENGTH_LONG).show();
    }


    public void generarTXT(String rutaCarpetaActual){
        File file;
        int conti = poligonoActual.estacionesPolFinal.size();
        DecimalFormat df = new DecimalFormat("################.###");
        String cadTemp = "";
        poligonoActual.rutaArchivoCSV = rutaCarpetaActual + File.separator + poligonoActual.nombreArchivoCSV;
        Uri uri = Uri.parse(poligonoActual.rutaArchivoCSV);
        file = new File(uri.getPath());
        file.getParentFile().mkdirs();
        FileOutputStream pw = null;
        try {
            pw = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            cadTemp = "#Point,X,Y,Z  -  (m)  -  https://jlcp89.github.io/d3sarrollo/#/gpst";
            sb.append(cadTemp);

            sb.append("\r\n");
            sb.append("\r\n");
            for (int F = 0; F < (conti - 1); F++) {
                Estacion estacionTemp = poligonoActual.estacionesPolFinal.elementAt(F);
                sb.append(Integer.toString(F));
                sb.append(",");
                cadTemp = df.format(estacionTemp.xt);
                sb.append(cadTemp);
                sb.append(",");
                cadTemp = df.format(estacionTemp.yt);
                sb.append(cadTemp);
                sb.append(",");
                cadTemp = df.format(estacionTemp.zt);
                sb.append(cadTemp);
                sb.append("");
                sb.append("\r\n");
            }
            byte[] contentInBytes = sb.toString().getBytes();
            pw.write(contentInBytes);
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error FileNotFound -----  "+ e +"  -----on loading TXT, working on this.",Toast.LENGTH_LONG).show();

        } catch (Exception e1) {
            e1.printStackTrace();
            Toast.makeText(this,"Error-----  "+ e1 +"  -----on loading TXT, working on this.",Toast.LENGTH_LONG).show();
        }
    }

    public void generarDXF (){
        DXFDocument dxfDocument = new DXFDocument(poligonoActual.nombreArchivoDXF);
        DXFCanvas dxfCanvas = dxfDocument.getCanvas();
        canvasLimpiar = dxfCanvas;
        drawDesign(dxfCanvas);

        try
        {
            // print dxf output to console for convenient inspection
            String stringOutput = dxfDocument.toDXFString();
            System.out.println(stringOutput);

            // now write to files - one with .dxf for display, and one with .txt for inspection
            // get appropriate suffix depending on what's generated

            String tarjetaSD = Environment.getExternalStorageDirectory().toString();
            String nombreCarpetaProyectos = "GPS_Topography_Projects";
            String rutaCarpetaProyectos = tarjetaSD + File.separator + nombreCarpetaProyectos;
            File carpetaProyectosGPST = new File (rutaCarpetaProyectos);
            if (!carpetaProyectosGPST.exists()) {
                carpetaProyectosGPST.mkdir();
            }

            String rutaCarpetaProyectoActual = poligonoActual.rutaCarpetaActual2;
            File carpetaProyectoActual = new File (rutaCarpetaProyectoActual);
            if (!carpetaProyectoActual.exists()){
                carpetaProyectoActual.mkdir();
            }

            String rutaDXF = poligonoActual.rutaArchivoDXF;

            String dxfPath = rutaDXF;
            FileWriter dxfFileWriter = new FileWriter(dxfPath);
            dxfFileWriter.write(stringOutput);
            dxfFileWriter.flush();
            dxfFileWriter.close();


        }
        catch (IOException e)
        {
            System.out.println("Bah!!!!! " + e.toString());
        }
    }

    private void drawDesign(Canvas canvas)
    {
        // draw some stuff
        Paint paint = new Paint();

        // fill the page with a pale yellow
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        vistaTrabajo.dibujarCosas2(canvas);
        canvasLimpiar = canvas;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void generarPDF(String rutaCarpetaProyectoActual) {
        PdfWriter writer = null;
        poligonoActual.rutaArchivoGuardado = rutaCarpetaProyectoActual + File.separator + poligonoActual.nombreArchivoGuardado;
        Uri uri = Uri.parse(poligonoActual.rutaArchivoGuardado);
        file = new File(uri.getPath());
        file.getParentFile().mkdirs();
        Document document = new Document(PageSize.A4);
        autor = getResources().getText(R.string.app_name).toString() +getResources().getText(R.string.resto_autor).toString();

        encabezado = poligonoActual.getProyecto() + " " + poligonoActual.getFecha();
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            cabezera event = new cabezera();
            writer.setPageEvent(event);
        } catch (DocumentException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error PDF ----  "+ e +"  -----, working on this.",Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error PDF ----  "+ e +"  -----, working on this.",Toast.LENGTH_LONG).show();
        }
        document.open();
        float anchoPaginaA4 = PageSize.A4.getRight() - 30;
        paint.setColor(Color.BLACK);
        PdfPTable tablaPro = generarTablaDatosProfesional(anchoPaginaA4);
        PdfPTable tablaDatos = generarTablaDatos(anchoPaginaA4);
        PdfPTable tablaTerreno = tablaPro;
        PdfPTable tablaLevantamiento = generarTablaLevantamiento(anchoPaginaA4);
        PdfPTable tabla1 = generarTabla1(anchoPaginaA4);
        PdfPTable tabla2 = generarTabla2(anchoPaginaA4);
        PdfPTable tablaError = generarTablaError(anchoPaginaA4);
        PdfPTable tabla3 = generarTabla3(anchoPaginaA4);
        PdfPTable tabla4 = generarTabla4(anchoPaginaA4);
        PdfPTable tabla5 = generarTabla5(anchoPaginaA4);
        Paragraph tituloReporte = new Paragraph(getResources().getString(R.string.encabezado));
        tituloReporte.setAlignment(Element.ALIGN_CENTER);
        Paragraph ultimaLinea = new Paragraph(getResources().getString(R.string.ultima_linea));
        ultimaLinea.setAlignment(Element.ALIGN_CENTER);

        try {
            document.add(tituloReporte);
            document.add(ESPACIO);
            document.add(tablaPro);
            document.add(ESPACIO);
            document.add(tablaDatos);
            document.add(ESPACIO);
            /*document.add(tabla6);
            document.add(ESPACIO);*/
            document.add(tablaLevantamiento);
            document.add(ESPACIO);
            document.add(tabla1);
            document.add(ESPACIO);
            document.add(tabla2);
            document.add(ESPACIO);
            document.add(tablaError);
            document.add(ESPACIO);
            document.add(tabla3);
            document.add(ESPACIO);
            document.add(tabla4);
            document.add(ESPACIO);
            document.add(tabla5);
            document.add(ESPACIO);
            document.add(ultimaLinea);
        } catch (DocumentException e) {
            Toast.makeText(this,"Error PDF ----  "+ e +"  -----, working on this.",Toast.LENGTH_LONG).show();
        }
        document.close();
    }
    private PdfPTable generarTablaDatosProfesional(float anchoPag) {
        tabla1 = new PdfPTable(4);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        PdfPCell cell;
        String resposable = getResources().getText(R.string.responsable).toString();
        String resp = poligonoActual.responsable;
        String tituloTabla = getResources().getText(R.string.tabla_pro).toString();

        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(resposable));
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.firma_sello).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(resp));
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(" "));
        cell.setFixedHeight(50);
        cell.setColspan(3);
        tabla1.addCell(cell);
        return tabla1;
    }
    private PdfPTable generarTablaDatos(float anchoPag) {
        tabla1 = new PdfPTable(5);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla_datos).toString();
        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.fecha).toString()));
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(poligonoActual.getFecha()));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase( getResources().getText(R.string.proyecto).toString()));
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(poligonoActual.getProyecto()));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.cliente).toString()));
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(poligonoActual.cliente));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.ubicacion).toString()));
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(poligonoActual.ubicacion));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.responsable).toString()));
        cell.setColspan(1);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(poligonoActual.responsable));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        return tabla1;
    }

    //***********************-------------------------------------**********************************-----------------------------------------
    //***********************-------------------------------------**********************************-----------------------------------------
    //***********************-------------------------------------**********************************-----------------------------------------


    private PdfPTable generarTabla1(float anchoPag) {
        tabla1 = new PdfPTable(12);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(2);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla1).toString();
        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(12);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        for (int F = 0; F < (poligonoActual.estacionesPolFinal.size() + 1); F++) {
            if (F == 0) {
                cell = new PdfPCell(new Phrase(getResources().getText(R.string.id_est_anterior).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.est).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.PO).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.dist).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.gra).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.min).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.seg).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase("Yp"));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase("Xp"));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

            } else {
                String idEstTempAntes = "";
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("###,###.###");
                String cadTemp = "";

                cell = new PdfPCell(new Phrase(estacionTemp.idAnterior));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(idEstTempAntes));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(estacionTemp.idEst));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.dist);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.grado);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.minuto);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.segundo);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.yp);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.xp);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);
            }
        }
        return tabla1;
    }

    private PdfPTable generarTablaLevantamiento(float anchoPag) {
        tabla1 = new PdfPTable(11);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(2);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla_levantamiento).toString();
        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(11);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        for (int F = 0; F < (poligonoActual.estaciones.size() + 1); F++) {
            if (F == 0) {
                cell = new PdfPCell(new Phrase(getResources().getText(R.string.est).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.PO).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(
                        getResources().getText(R.string.dist).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.gra).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.min).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.seg).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase("Yt"));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase("Xt"));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

            } else {
                String idEstTempAntes = "";
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = (Estacion) poligonoActual.estaciones.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = (Estacion) poligonoActual.estaciones.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("###,###.###");
                String cadTemp = "";

                cell = new PdfPCell(new Phrase(idEstTempAntes));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(estacionTemp.idEst));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.dist);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.grado);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.minuto);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.segundo);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.yt);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.xt);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

            if (estacionTemp.radiaciones.size()>0){
                for (int i = 0; i < estacionTemp.radiaciones.size(); i++){
                    idEstTempAntes = estacionTemp.idEst;
                    Estacion radiacion = estacionTemp.radiaciones.elementAt(i);


                    cell = new PdfPCell(new Phrase(idEstTempAntes));
                    cell.setColspan(1);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla1.addCell(cell);

                    cell = new PdfPCell(new Phrase(radiacion.idEst));
                    cell.setColspan(1);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla1.addCell(cell);

                    cadTemp = df.format(radiacion.dist);
                    cell = new PdfPCell(new Phrase(cadTemp));
                    cell.setColspan(2);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla1.addCell(cell);

                    cadTemp = df.format(radiacion.grado);
                    cell = new PdfPCell(new Phrase(cadTemp));
                    cell.setColspan(1);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla1.addCell(cell);

                    cadTemp = df.format(radiacion.minuto);
                    cell = new PdfPCell(new Phrase(cadTemp));
                    cell.setColspan(1);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla1.addCell(cell);

                    cadTemp = df.format(radiacion.segundo);
                    cell = new PdfPCell(new Phrase(cadTemp));
                    cell.setColspan(1);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla1.addCell(cell);

                    cadTemp = df.format(radiacion.yt);
                    cell = new PdfPCell(new Phrase(cadTemp));
                    cell.setColspan(2);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla1.addCell(cell);

                    cadTemp = df.format(radiacion.xt);
                    cell = new PdfPCell(new Phrase(cadTemp));
                    cell.setColspan(2);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabla1.addCell(cell);

                }
            }


            }
        }
        return tabla1;
    }




    private PdfPTable generarTabla2(float anchoPag) {
        tabla1 = new PdfPTable(10);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(2);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla2).toString();
        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        for (int F = 0; F < (poligonoActual.estacionesPolFinal.size() + 1); F++) {
            if (F == 0) {

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.est).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.PO).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.n).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.s).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.e).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.o).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);


            } else {
                String idEstTempAntes = "";
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("#,###,###.###");
                String cadTemp = "";

                cell = new PdfPCell(new Phrase(idEstTempAntes));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(estacionTemp.idEst));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.Norte);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.Sur);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.Este);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.Oeste);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);
            }
        }
        return tabla1;
    }
    private PdfPTable generarTablaError(float anchoPag) {
        tabla1 = new PdfPTable(12);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(1);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla_error).toString();

        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(12);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        DecimalFormat df = new DecimalFormat("#,###,###.###");
        String cadTemp = " ";

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.suma_n).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.suma_s).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.suma_e).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.suma_o).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cadTemp = df.format(poligonoActual.getSumaNortes());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cadTemp = df.format(poligonoActual.getSumaSures());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cadTemp = df.format(poligonoActual.getSumaEstes());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cadTemp = df.format(poligonoActual.getSumaOestes());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.resta_y).toString()));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.resta_x).toString()));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.perimetro).toString()));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cadTemp = df.format(poligonoActual.getDeltaY());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cadTemp = df.format(poligonoActual.getDeltaX());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cadTemp = df.format(poligonoActual.getSumaDist());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.error).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.error_un).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.cy).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.cx).toString()));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cadTemp = df.format(poligonoActual.getErrC());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cadTemp = df.format(poligonoActual.getErrCierreUn());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cadTemp = df.format(poligonoActual.getCy());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cadTemp = df.format(poligonoActual.getCx());
        cell = new PdfPCell(new Phrase(cadTemp));
        cell.setColspan(3);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.error_permisible).toString()));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        cell = new PdfPCell(new Phrase(poligonoActual.getErrorCumple()));
        cell.setColspan(8);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        return tabla1;
    }
    private PdfPTable generarTabla3(float anchoPag) {
        tabla1 = new PdfPTable(10);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(2);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla3).toString();
        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        for (int F = 0; F < (poligonoActual.estacionesPolFinal.size() + 1); F++) {
            if (F == 0) {

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.est).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.PO).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.cn).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.cs).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.ce).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.co).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);


            } else {
                String idEstTempAntes = "";
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("###,###.###");
                String cadTemp = "";

                cell = new PdfPCell(new Phrase(idEstTempAntes));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(estacionTemp.idEst));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);


                cadTemp = df.format(poligonoActual.corrN.elementAt(F - 1));
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(poligonoActual.corrS.elementAt(F - 1));
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(poligonoActual.corrE.elementAt(F - 1));
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(poligonoActual.corrO.elementAt(F - 1));
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);
            }
        }
        return tabla1;
    }
    private PdfPTable generarTabla4(float anchoPag) {
        tabla1 = new PdfPTable(10);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(2);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla4).toString();
        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        for (int F = 0; F < (poligonoActual.estacionesPolFinal.size() + 1); F++) {
            if (F == 0) {

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.est).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.PO).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.nc).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.sc).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.ec).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.oc).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);


            } else {
                String idEstTempAntes = "";
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("###,###.###");
                String cadTemp = "";

                cell = new PdfPCell(new Phrase(idEstTempAntes));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(estacionTemp.idEst));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.nc);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.sc);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.ec);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.oc);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);
            }
        }
        return tabla1;
    }
    private PdfPTable generarTabla5(float anchoPag) {
        tabla1 = new PdfPTable(14);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(2);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla5).toString();
        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(14);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);
        DecimalFormat df = new DecimalFormat("###,###.###");
        String cadTemp = "";
        for (int F = 0; F < (poligonoActual.estacionesPolFinal.size() + 1); F++) {
            if (F == 0) {

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.est).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.PO).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.ypc).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.xpc).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.yt).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.xt).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.yx).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.xy).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);


            } else {
                String idEstTempAntes = "";
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = (Estacion) poligonoActual.estacionesPolFinal.elementAt(F - 1);
                fuente.setSize(9);
                cell = new PdfPCell(new Phrase(idEstTempAntes, fuente));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(estacionTemp.idEst, fuente));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.ypc);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.xpc);
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                fuente.setSize(8);
                cadTemp = df.format(estacionTemp.yt);
                cell = new PdfPCell(new Phrase(cadTemp, fuente));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.xt);
                cell = new PdfPCell(new Phrase(cadTemp, fuente));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                fuente.setSize(5);
                cadTemp = df.format(estacionTemp.YX);
                cell = new PdfPCell(new Phrase(cadTemp,fuente));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.XY);
                cell = new PdfPCell(new Phrase(cadTemp,fuente));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);
            }
        }

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.totales).toString()));
        cell.setColspan(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        fuente.setSize(7);
        cadTemp = df.format(poligonoActual.getSumaYX());
        cell = new PdfPCell(new Phrase(cadTemp,fuente));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cadTemp = df.format(poligonoActual.getSumaXY());
        cell = new PdfPCell(new Phrase(cadTemp,fuente));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        cell = new PdfPCell(new Phrase(getResources().getText(R.string.area).toString()));
        cell.setColspan(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        DecimalFormat df1 = new DecimalFormat("###,###,###,###,###.###");
        fuente.setSize(10);

        cadTemp = df1.format(areaP);
        cell = new PdfPCell(new Phrase(cadTemp,fuente));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        return tabla1;


    }
}