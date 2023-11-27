package com.desarrollojlcp.gps_topography.activity;

import static android.location.LocationManager.GPS_PROVIDER;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.desarrollojlcp.gps_topography.BuildConfig;
import com.desarrollojlcp.gps_topography.R;
import com.desarrollojlcp.gps_topography.model.object.cabezera;
import com.desarrollojlcp.gps_topography.model.db.Utilidades;
import com.desarrollojlcp.gps_topography.model.object.Estacion;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.collect.ImmutableList;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.maps.android.SphericalUtil;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Vector;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.desarrollojlcp.gps_topography.model.object.Poligono;
import com.desarrollojlcp.gps_topography.model.db.ConexionSQLiteHelper;


public class Ingreso extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, LocationListener, PurchasesUpdatedListener {

    private InterstitialAd mInterstitialAd;

    private static final int REQUEST_INVITE = 101;

    private static final String TAG = "Ingreso";
    Poligono poligonoActual = new Poligono();

    private String textoObservaciones;
    private String fechaNombreReporte;
    double longitudGPS, latitudGPS, elevacionGPS;
    LocationManager locationManager;
    private GoogleMap mMap;
    private static Vector<LatLng> estaciones = new Vector<>();
    private Paint paint = new Paint();
    public static String encabezado, autor;
    public static PdfPTable tabla1;
    Phrase ESPACIO = new Phrase(" ");
    public static File file;
    Font fuente = new Font();
    private TextView textoResultados;
    public boolean primerUso = true;
    double areaP = 0;
    double perimetroP = 0;
    public static String firma;
    public static String pagina;
    public static String de;
    float zoom = 17;
    boolean permisoUbicacion = false;
    ConexionSQLiteHelper helper;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private boolean poligonoCargado = false;
    long intervalo = 200;
    private FloatingActionButton botonBuscarCoordenadas, botonPoligono, botonLinea, botonPunto, botonCentrar, botonInfo, botonAgregarPunto, botonUnDo, botonRecalcular, botonAnalizar, botonPro, botonGuardar, botonCargar;
    private FloatingActionButton botonCompartir;
    private boolean centrarPantalla = true;
    int tipoMedicionInt = 1;
    private BillingClient billingClient;
    public static final String ITEM_SKU_SUBSCRIBE= "pro_sub1";
    public static final String PREF_FILE= "preferenciaSusGPSTpro";
    public static final String PREF_FILE_USOS= "preferenciaUSOSGPSTpro";

    public static final String SUBSCRIBE_KEY= "subscribe";
    public static final String USOS_KEY= "usando";

    private boolean valorSuscripcion = false;
    private int usos = 0;

    private LinearLayout linearSuscripcion;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean clienteCompraListo = false;


    private boolean getSubscribeValueFromPref(){
        return getPreferenceObject().getBoolean( SUBSCRIBE_KEY,true);
    }

    private int getUsosValueFromPref(){
        return getPreferenceObjectU().getInt( USOS_KEY,0);
    }


    protected SharedPreferences getPreferenceObject() {
        return getApplicationContext().getSharedPreferences(PREF_FILE, 0);
    }
    protected SharedPreferences getPreferenceObjectU() {
        return getApplicationContext().getSharedPreferences(PREF_FILE_USOS, 0);
    }

    //private ImageView guardar, cargar, cambiar;
    private AutocompleteSupportFragment autocompleteFragment;
    private Geocoder gcd;




    private void setUpBillingClient(){
        //Initialize a BillingClient with PurchasesUpdatedListener onCreate method
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        new PurchasesUpdatedListener() {
                            @Override
                            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                                if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK && list !=null) {
                                    for (Purchase purchase: list){
                                        verifySubPurchase(purchase);
                                    }
                                }
                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        establishConnection();
    }

    void establishConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    //showProducts();
                    clienteCompraListo = true;
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                establishConnection();
            }
        });
    }


    void showProducts() {
        ImmutableList productList = ImmutableList.of(
                //Product 1 = index is 0
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(ITEM_SKU_SUBSCRIBE)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()

        );
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();
        billingClient.queryProductDetailsAsync(
                params,
                (billingResult, productDetailsList) -> {
                    // Process the result
                    for (ProductDetails productDetails : productDetailsList) {
                        if (productDetails.getProductId().equals(ITEM_SKU_SUBSCRIBE)) {
                            List subDetails = productDetails.getSubscriptionOfferDetails();
                            assert subDetails != null;
                            launchPurchaseFlow(productDetails);
                        }
                    }
                }
        );
    }

    void launchPurchaseFlow(ProductDetails productDetails) {
        assert productDetails.getSubscriptionOfferDetails() != null;
        ImmutableList productDetailsParamsList =
                ImmutableList.of(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
                                .build()
                );
        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productDetailsParamsList)
                .build();
        BillingResult billingResult = billingClient.launchBillingFlow(this, billingFlowParams);
    }

    void verifySubPurchase(Purchase purchases) {
        AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams
                .newBuilder()
                .setPurchaseToken(purchases.getPurchaseToken())
                .build();

        billingClient.acknowledgePurchase(acknowledgePurchaseParams, billingResult -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                saveSubscribeValueToPref(true);
                this.recreate();
            }
        });

        Log.d(TAG, "Purchase Token: " + purchases.getPurchaseToken());
        Log.d(TAG, "Purchase Time: " + purchases.getPurchaseTime());
        Log.d(TAG, "Purchase OrderID: " + purchases.getOrderId());
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

    }
    private void saveSubscribeValueToPref(boolean value){
        getPreferenceEditObject().putBoolean(SUBSCRIBE_KEY,value).commit();
    }


    private SharedPreferences.Editor getPreferenceEditObject() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE, 0);
        return pref.edit();
    }

    private void saveUsosValueToPref(int value){
        getPreferenceEditObjectUsos().putInt(USOS_KEY,value).commit();
    }

    private SharedPreferences.Editor getPreferenceEditObjectUsos() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREF_FILE_USOS, 0);
        return pref.edit();
    }

    private void cargarAnuncio(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-7114592307899156/1230497942", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad dismissed fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.");
                                mInterstitialAd = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad showed fullscreen content.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


        gcd = new Geocoder(getApplicationContext(), Locale.getDefault());


        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener((billingResult, list) -> {}).build();
        final BillingClient finalBillingClient = billingClient;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {

            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK){
                    finalBillingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(), (billingResult1, list) -> {
                                if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK){
                                    Log.d("testOffer",list.size() +" size");
                                    if(list.size()>0){
                                        saveSubscribeValueToPref(true);

                                    }else {
                                        saveSubscribeValueToPref(false);
                                    }
                                }
                            });
                }

            }
        });

        try {
            valorSuscripcion = getSubscribeValueFromPref();
            if (!valorSuscripcion){
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(Ingreso.this);
                    cargarAnuncio();
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
            usos = getUsosValueFromPref();
            iniciarActividad();
        } catch (Exception e) {
            e.printStackTrace();
            //Crashlytics.logException(e);
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        cargarAnuncio();

        //signIn();

        // Set up a PlaceSelectionListener to handle the response.
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Objects.requireNonNull(place.getLatLng()), (float) 17.50));
            }
            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });


    }

    private void agregarFindIdS() {
        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));



        botonInfo = findViewById(R.id.boton_info);
        botonAgregarPunto = findViewById(R.id.boton_agregar_punto);
        botonUnDo = findViewById(R.id.boton_undo);
        botonRecalcular = findViewById(R.id.boton_recalcular);
        botonAnalizar = findViewById(R.id.boton_analizar);
        botonCentrar = findViewById(R.id.boton_centrar);
        botonPoligono = findViewById(R.id.boton_poligono);
        botonBuscarCoordenadas = findViewById(R.id.boton_buscar_coor);
        botonLinea = findViewById(R.id.boton_linea);
        botonPunto = findViewById(R.id.boton_punto2);
        botonPro = findViewById(R.id.boton_pro);
        botonGuardar = findViewById(R.id.boton_guardar);
        botonCargar = findViewById(R.id.boton_cargar);
        botonCompartir = findViewById(R.id.boton_compartir);
        textoResultados = findViewById(R.id.texto_resultados);
        linearSuscripcion = findViewById(R.id.linear_sus);
        textoResultados2 = findViewById(R.id.texto_resultados2);
        poligonoActual.setTipoMedicion("poligono");
        tipoMedicionInt = 1;

    }

    private void cargarPoligono() {
        try {
            poligonoActual = (Poligono) Objects.requireNonNull(getIntent().getExtras()).getSerializable("pol_trab");
            if (poligonoActual.estaciones.size() > 0) {
                estaciones.removeAllElements();
                for (int i = 0; i < poligonoActual.estaciones.size(); i++) {
                    Estacion e = new Estacion();
                    e.idEst = poligonoActual.estaciones.elementAt(i).idEst;
                    e.setLat(poligonoActual.estaciones.elementAt(i).getLat());
                    e.setLon(poligonoActual.estaciones.elementAt(i).getLon());
                    LatLng point = new LatLng(e.getLat(), e.getLon());
                    estaciones.addElement(point);
                }
                poligonoCargado = true;
            }
        } catch (NullPointerException ignored) {
        }
    }

    private void limpiarPoligonoParaIngreso2() {
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
        estTemp.valorDecimalGrados = 0;
        estTemp.valorRadianes = Math.toRadians(estTemp.valorDecimalGrados);
        estTemp.seno = Math.sin(Math.toRadians(estTemp.valorDecimalGrados));
        estTemp.coseno = Math.cos(Math.toRadians(estTemp.valorDecimalGrados));
        estTemp.idEst = "E-0";
        poligonoTemp.estaciones.addElement(estTemp);
        poligonoTemp.calculoCoorParciales();

        try {
            Intent intent = new Intent(getApplicationContext(), Ingreso2.class);
            intent.putExtra("pol_trab", poligonoTemp);
            finish();
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setBotonesTipoMedicion() {
        botonPoligono.setOnClickListener(view -> {
            poligonoActual.setTipoMedicion("poligono");
            tipoMedicionInt = 1;
        });
        botonLinea.setOnClickListener(view -> {
            poligonoActual.setTipoMedicion("linea");
            tipoMedicionInt = 2;
        });
        botonPunto.setOnClickListener(view -> {
            poligonoActual.setTipoMedicion("puntos");
            tipoMedicionInt = 3;
        });
    }

    public void setBotones() {

        setBotonesTipoMedicion();

        botonPro.setOnClickListener(view -> {
            String url = "https://play.google.com/store/apps/details?id=com.desarrollojlcp.gps_topography_pro";
            abirLink(url);
        });

        botonCompartir.setOnClickListener(view -> onInviteClicked());

        botonGuardar.setOnClickListener(view -> {
            if (estaciones.size() > 2) {
                Intent intent = new Intent(getApplicationContext(), GuardarPoligono.class);
                intent.putExtra("pol_trab", poligonoActual);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.tres_puntos), Toast.LENGTH_LONG).show();
            }
        });

        botonCargar.setOnClickListener(view -> {
            if (estaciones.size() > 0) {
                preguntarSalirLevamtamiento();
            } else {
                Intent intent = new Intent(getApplicationContext(), Cargar.class);
                startActivity(intent);
                finish();
            }
        });

        botonAgregarPunto.setOnClickListener(view -> {
            LatLng point = new LatLng(latitudGPS, longitudGPS);
            dibujarPantalla(point);
        });

        botonCentrar.setOnClickListener(view -> {
            if (centrarPantalla) {
                centrarPantalla = false;
                Toast.makeText(getApplicationContext(), "Center mode - OFF", Toast.LENGTH_SHORT).show();
                botonCentrar.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorGris)));
            } else {
                centrarPantalla = true;
                Toast.makeText(getApplicationContext(), "Center mode - ON", Toast.LENGTH_SHORT).show();
                botonCentrar.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            }
            LatLng point = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());

            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, mMap.getCameraPosition().zoom));
            }
        });

        botonUnDo.setOnClickListener(view -> {
            if (estaciones.size() < 1) {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.no_estacion), Toast.LENGTH_LONG).show();
            } else {
                estaciones.removeElementAt(estaciones.size() - 1);
                poligonoActual.estaciones.removeElementAt(poligonoActual.getEstaciones().size() - 1);
                dibujarPantalla();
            }
        });

        botonAnalizar.setOnClickListener(view -> {
            if (estaciones.size() > 2) {
                valorSuscripcion = getSubscribeValueFromPref();
                if (!valorSuscripcion){
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(Ingreso.this);
                        cargarAnuncio();
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
                }

                calcular();
                generarArchivos();

            } else {
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.tres_puntos), Toast.LENGTH_LONG).show();
            }
        });

        botonInfo.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Info.class);
            startActivity(intent);
        });

        botonBuscarCoordenadas.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), BuscarCoordenadas.class);
            intent.putExtra("pol_trab", poligonoActual);
            startActivity(intent);
            finish();
        });

        botonRecalcular.setOnClickListener(view -> {
            if (estaciones.size() > 0) {
                preguntarSalir();
            } else {
                limpiarPoligonoParaIngreso2();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.guardar:
                botonGuardar.performClick();
                return true;
            case R.id.cargar:
                botonCargar.performClick();
                return true;
            case R.id.cambiar_pantalla:
                botonRecalcular.performClick();
                return true;
            case R.id.poligono:
                botonPoligono.performClick();
                return true;
            case R.id.linea:
                botonLinea.performClick();
                return true;
            case R.id.puntos:
                botonPunto.performClick();
                return true;
            case R.id.capturar_ubicacion_actual:
                botonAgregarPunto.performClick();
                return true;

            case R.id.acerca_de:
                abirLink("https://d3sarrollo.net/gps-topography/");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }



    public void iniciarActividad() {
        if (valorSuscripcion){
            setContentView(R.layout.activity_ingreso_pro);
        } else {
            setContentView(R.layout.activity_ingreso);

        }
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        agregarFindIdS();

        helper = new ConexionSQLiteHelper(this, Utilidades.NOMBRE_BD, null, Utilidades.VERSION_BD);
        firma = getResources().getText(R.string.firma_sello).toString();
        pagina = getResources().getText(R.string.pagina).toString() + " ";
        de = getResources().getText(R.string.de).toString();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            verificarPermisos();
        }

        cargarPoligono();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        longitudGPS = 0;
        latitudGPS = 0;
        elevacionGPS = 0;
        setFecha();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        try {
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        } catch (RuntimeException r) {
            r.printStackTrace();
        }
        if (poligonoActual.getTipoMedicion() == null) {
            poligonoActual.setTipoMedicion("poligono");
        }
        setBotones();
        if (permisoUbicacion) {
            setFusedParaTrabajo();
        }

        gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setUpBillingClient();
        valorSuscripcion = getSubscribeValueFromPref();


        linearSuscripcion.setOnClickListener(view -> {
            if (clienteCompraListo){
                showProducts();
            }
        });


    }

    private double distKMGPS(double lat1, double lon1, double lat2, double lon2) {
        double earthRadiusKm = 6371;

        double dLat = degreesToRadians(lat2-lat1);
        double dLon = degreesToRadians(lon2-lon1);

        lat1 = degreesToRadians(lat1);
        lat2 = degreesToRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }
    private double degreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }
    public boolean segundoUso = true;

    public void centrarPantallaBien (){
        if (primerUso) {
            if (mMap != null){
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 19.0f));
                primerUso = false;
            } else {
                primerUso = true;
            }

        } else {
            if(centrarPantalla){
                if (segundoUso){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 19.0f));
                    segundoUso = false;
                }
            } else {
                //zoom = mMap.getCameraPosition().zoom;
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudGPS, longitudGPS), zoom));

            }
        }
    }

    private int contadorTiempo = 0;
    double latAnterior = 0;
    double lonAnterior = 0;
    double velocidad = 0;
    private TextView textoResultados2;


    private void setFusedParaTrabajo() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(intervalo);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                lastLocation = locationResult.getLastLocation();
                if (contadorTiempo == 0){
                    latAnterior = latitudGPS;
                    lonAnterior = longitudGPS;
                }
                latAnterior = latitudGPS;
                lonAnterior = longitudGPS;
                latitudGPS = lastLocation.getLatitude();
                longitudGPS = lastLocation.getLongitude();
                elevacionGPS = lastLocation.getAltitude();
                velocidad = lastLocation.getSpeed();
                double precision = lastLocation.getAccuracy();
                double distanciaKM = distKMGPS(latAnterior, lonAnterior, latitudGPS, longitudGPS);
                contadorTiempo = contadorTiempo + 1;
                if (contadorTiempo == 10){
                    double difHoras = 0.0000277 * 10;
                    velocidad = distanciaKM / difHoras;
                    contadorTiempo = 0;
                }

                dibujarPantalla();
                DecimalFormat df = new DecimalFormat("###,###,###,###,###.###");
                String elevacion = df.format(elevacionGPS);
                String textoParaResultado =  actualizarAreaPantalla();
                textoResultados.setText(textoParaResultado);
                zoom = 19f;
                centrarPantallaBien();
                String currentSpeed = df.format(velocidad);
                String textoPresicion = df.format(precision);

                df = new DecimalFormat("##.########");
                String textoLat = df.format(latitudGPS);
                String textoLon = df.format(longitudGPS);

                List<Address> addresses = null;
                try {
                    if (gcd != null ){
                        addresses = gcd.getFromLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), 1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Can't load Name", Toast.LENGTH_SHORT).show();
                    }
                    if (addresses != null){
                        if (addresses.size() > 0)
                            direccion = (addresses.get(0).getAddressLine(0));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                coordi = "Lat(GPS)= " + textoLat + " * Lon(GPS)= " + textoLon+ " * Alt(m)= " + elevacion ;
                String texto = "Lat(GPS)= " + textoLat + " * Lon(GPS)= " + textoLon+ " * Alt(m)= " + elevacion + " * Speed(Km/H)= " +currentSpeed+ " * Name= "+ direccion + " * Accuracy(m) = "+ textoPresicion;
                textoResultados2.setText(texto);
            }
        };

    }

    private String coordi = "-";
    private String direccion = "-";


    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse("android.resource://com.desarrollojlcp.gps_topography_pro/drawable/compartir"))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }





    public void abirLink(String url1) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url1));
            startActivity(i);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void preguntarSalirLevamtamiento() {
        try {
            new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getText(R.string.cerrando_levantamiento))
                    .setMessage(getResources().getText(R.string.seguro_salir_levantamiento))
                    .setPositiveButton(getResources().getText(R.string.si), (dialog, which) -> {
                        estaciones.removeAllElements();
                        poligonoActual.limpiarPoligono();
                        Intent intent = new Intent(getApplicationContext(), Cargar.class);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton(getResources().getText(R.string.no), null)
                    .show();
        } catch (Exception e) {
            estaciones.removeAllElements();
            poligonoActual.limpiarPoligono();
            Intent intent = new Intent(getApplicationContext(), Cargar.class);
            startActivity(intent);
            finish();
        }

    }

    public void preguntarSalir() {
        try {
            final AlertDialog pol_trab;
            pol_trab = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getText(R.string.cerrando_levantamiento))
                    .setMessage(getResources().getText(R.string.seguro_salir_levantamiento))
                    .setPositiveButton(getResources().getText(R.string.si), (dialog, which) -> {
                        estaciones.removeAllElements();
                        poligonoActual.limpiarPoligono();
                        limpiarPoligonoParaIngreso2();
                    })
                    .setNegativeButton(getResources().getText(R.string.no), null)
                    .show();
        } catch (Exception e) {
            estaciones.removeAllElements();
            poligonoActual.limpiarPoligono();
            limpiarPoligonoParaIngreso2();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.clear();
            mMap.setMinZoomPreference(12f);
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            dibujarPantalla();
            mMap.setOnMapClickListener(pointT -> {
                primerUso = false;
                if (pointT != null){
                    dibujarPantalla(pointT);
                }
            });
        }
    }

    public void ponerMarcadorPosicionActual() {
        final LatLng PosicionActual = new LatLng(latitudGPS, longitudGPS);
        String textoMarcadorPosicionUsuario = "You are here! - " + latitudGPS + ",  " + longitudGPS;
        BitmapDescriptor bit = bitmapDescriptorFromVector(getApplicationContext(), R.drawable.posicion_actual);
        mMap.addMarker(new MarkerOptions()
                .position(PosicionActual).icon(bit)
                .title(textoMarcadorPosicionUsuario).flat(true));
    }

    public void imprimirEstacionesGuardadas() {
        double latCentrarPantalla = 0.0;
        double lonCentrarPantalla = 0.0;
        int contador = 0;
        if (estaciones.size() > 0) {
            for (int i = 0; i < estaciones.size(); i++) {
                String textoMarcadorPosicionUsuario;
                LatLng point1 = new LatLng(estaciones.elementAt(i).latitude, estaciones.elementAt(i).longitude);
                textoMarcadorPosicionUsuario = "E-" + i + ",  " + point1.latitude + ",  " + point1.longitude;
                mMap.addMarker(new MarkerOptions()
                        .position(point1)
                        .title(textoMarcadorPosicionUsuario).flat(true));
                //Coordenadas para centrar la camara en la ultima estacion, si es un poligono guardado
                latCentrarPantalla = point1.latitude;
                lonCentrarPantalla = point1.longitude;
                if (poligonoActual.getTipoMedicion().equals("poligono")) {
                    Polygon polygon = mMap.addPolygon(new PolygonOptions()
                            .add(point1)
                            .strokeColor(Color.BLUE)
                            .fillColor(R.color.colorHint).geodesic(true));
                    polygon.setPoints(estaciones);
                } else if (poligonoActual.getTipoMedicion().equals("linea")) {
                    Polyline polyline1 = mMap.addPolyline(new PolylineOptions().add(point1)
                            .color(Color.BLUE)
                            .geodesic(true));
                    polyline1.setPoints(estaciones);
                }

                if (contador == 0){
                    try {
                        if (primerUso) {
                            zoom = 17;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latCentrarPantalla, lonCentrarPantalla), zoom));
                            primerUso = false;
                        } else {
                            if (centrarPantalla) {
                                zoom = mMap.getCameraPosition().zoom;
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latCentrarPantalla, lonCentrarPantalla), zoom));
                            }
                        }
                    } catch (Exception e){
                        Log.d(TAG, e.toString());
                    }
                }
                contador = contador +1;
            }
        }
    }

    public void dibujarPantalla() {
        if (mMap != null) {
            mMap.clear();
            ponerMarcadorPosicionActual();
            imprimirEstacionesGuardadas();
        }
    }

    private void agregarEstacionAlMapa(LatLng point) {
        if (point != null){
            Estacion estTemp = new Estacion();
            int F = estaciones.size();
            String cadi1 = "E-" + F;
            poligonoActual.contEs += 1;
            estTemp.setIdEstacion(F);
            estTemp.setLon(point.longitude);
            estTemp.setLat(point.latitude);
            estTemp.setAlt(elevacionGPS);
            estTemp.setIdEst(cadi1);
            estTemp.setPartePoligonoFinal(true);
            estTemp.setObservaciones(textoObservaciones);
            poligonoActual.ingresarEstacion(estTemp);
            if (mMap != null) {
                mMap.clear();
                //Se imprime la posicion actual del usuario
                ponerMarcadorPosicionActual();
                if (estaciones.size()>0){
                    imprimirEstacionesGuardadas();
                }

                if (estaciones.size() % 2 == 0) {

                }
                if (!valorSuscripcion){
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(Ingreso.this);
                        cargarAnuncio();
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
                }

                //se imprime la estacion nueva
                String textoMarcadorPosicionUsuario = "E-" + poligonoActual.estaciones.size() + " -  " + point.latitude + ",  " + point.longitude;
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(textoMarcadorPosicionUsuario).flat(true));
                //Se agrega el punto a el vector estaciones
                estaciones.add(point);
            }
        }
    }

    public void dibujarPantalla(LatLng pointEntrada) {
        if (pointEntrada !=null){
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            textoObservaciones = input.getText().toString();
            input.setTextColor(ColorStateList.valueOf(getColor(R.color.azul)));
            input.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.azul)));
            new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setTitle(getResources().getText(R.string.ingreso_de_punto)).setView(input)
                    .setMessage(getResources().getText(R.string.ingreso_de_punto_texto))
                    .setPositiveButton(getResources().getText(R.string.guardar), (dialog, which) -> {
                        textoObservaciones = input.getText().toString();
                        //Ingresando la nueva estacion al poligono actual
                        agregarEstacionAlMapa(pointEntrada);
                    }).setNegativeButton(getResources().getText(R.string.cancelar1), null).show();
        }
    }

    public void calcular() {
        poligonoActual.calculoUTM();
        poligonoActual.calculoNyS();
        poligonoActual.calculoEyO();
        if (poligonoActual.getTipoMedicion().equals("poligono")) {
            poligonoActual.calculoErrorCierre();
            poligonoActual.calculoCorrecciones();
            poligonoActual.calculoCoorCompensadas();
            poligonoActual.calculoCoorTotales();
            poligonoActual.calculoAreaPoligono();
            poligonoActual.calcularPerimetro();
        } else if (poligonoActual.getTipoMedicion().equals("linea")) {
            poligonoActual.calculoCoorTotales();
            poligonoActual.calcularPerimetro();
        } else {
            poligonoActual.calculoCoorTotales();
        }


        areaP = poligonoActual.getArea();
        perimetroP = poligonoActual.getPerimetro();

    }

    private String actualizarAreaPantalla() {
        double area = SphericalUtil.computeArea(estaciones);
        DecimalFormat df = new DecimalFormat("###,###,###,###,###.###");
        String areSal = df.format(area);
        String textoParaResultado;
        if (estaciones.size() >= 2) {
            Vector<LatLng> estaciones2 = new Vector<>();
            estaciones2.add(estaciones.elementAt(estaciones.size() - 1));
            estaciones2.add(estaciones.elementAt(0));


            double ultimaDistancia = SphericalUtil.computeLength(estaciones2);
            double perimetro1;
            if (tipoMedicionInt == 1) {
                perimetro1 = SphericalUtil.computeLength(estaciones) + ultimaDistancia;
            } else if (tipoMedicionInt == 2) {
                perimetro1 = SphericalUtil.computeLength(estaciones);
                areSal = String.valueOf(0.0);
            } else {
                perimetro1 = 0.0;
                areSal = String.valueOf(0.0);
            }
            String peri = df.format(perimetro1);
            textoParaResultado = "Points(#)= " + (estaciones.size()) + "  -  A(m2)= " + areSal + "  -  P(m)= " + peri;
        } else {
            textoParaResultado = "Points(#)= " + (estaciones.size()) + "  -  A(m2)= " + areSal + "  -  P(m)= 0";
        }
        return textoParaResultado;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("ESTACIONES", estaciones);
        savedInstanceState.putSerializable("POL_SAVED", poligonoActual);
        if (mMap != null) {
            savedInstanceState.putFloat("ZOOM", mMap.getCameraPosition().zoom);
        } else {
            savedInstanceState.putFloat("ZOOM", 16f);
        }
        savedInstanceState.putInt("EST_POL", tipoMedicionInt);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        List listaEstaciones = (List) (savedInstanceState.getSerializable("ESTACIONES"));
        poligonoActual = (Poligono) savedInstanceState.getSerializable("POL_SAVED");
        zoom = savedInstanceState.getFloat("ZOOM");

        if (listaEstaciones.size()>0){
            estaciones = new Vector<>(listaEstaciones);
            tipoMedicionInt = savedInstanceState.getInt("EST_POL");
            setBotonesTipoMedicion();
            dibujarPantalla();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                verifySubPurchase(purchase);
                            }
                        }
                    }
                }
        );
        checkLocation();
        try {
            startLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "An error occurred, please send this info to www.d3sarrollo.com, thanks! " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void startLocationUpdates() {

        if ((locationRequest == null) || (locationCallback == null) || (fusedLocationClient == null)) {
            setFusedParaTrabajo();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
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
                    .setPositiveButton(getResources().getText(R.string.si), (dialog, which) -> finish())
                    .setNegativeButton(getResources().getText(R.string.no), null)
                    .show();
        } catch (Exception e){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    public void verificarPermisos() {
        int permsRequestCode = 100;
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int accessFinePermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        int accessCoarsePermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        int writePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (writePermission == PackageManager.PERMISSION_GRANTED && accessFinePermission == PackageManager.PERMISSION_GRANTED && accessCoarsePermission == PackageManager.PERMISSION_GRANTED) {
            //se realiza metodo si es necesario...
            permisoUbicacion = true;
        } else {
            requestPermissions(perms, permsRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            permisoUbicacion = true;
        }
    }

    private void checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        isLocationEnabled();
    }

    private void showAlert() {
        try {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
            dialog.setTitle("Enable GPS")
                    .setMessage("GPS off/GPS apagado\nplease activate and restart app/porfavor activelo y reinicie la app")
                    .setPositiveButton("GSP", (paramDialogInterface, paramInt) -> {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    })
                    .setNegativeButton("No", (paramDialogInterface, paramInt) -> {
                    });
            dialog.show();
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "GPS off/GPS apagado\nplease activate and restart app/porfavor activelo y reinicie la app", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(GPS_PROVIDER);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void setFecha (){
        final String fecha1 = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.US).format(new Date());
        String ano = fecha1.substring(17,19);
        String mes = fecha1.substring(12,14);
        String dia = fecha1.substring(9,11);
        String hor = fecha1.substring(0,2);
        String min = fecha1.substring(3,5);
        fechaNombreReporte = ano+mes+dia+hor+min;
        poligonoActual.setFecha(fecha1);
        fechaNombreReporte.replace(" ","");
    }

    public void generarArchivos(){

        setFecha();
        String tarjetaSD = Environment.getExternalStorageDirectory().toString();
        String nomCarpetaGPST = "GPS_Topography_Projects";
        poligonoActual.rutaCarpetaActual = tarjetaSD+ File.separator + nomCarpetaGPST;
        poligonoActual.rutaCarpetaActual2 = poligonoActual.rutaCarpetaActual
                + File.separator+fechaNombreReporte;
        String nombreArchivoCSV = fechaNombreReporte+".txt";
        String nombreArchivoDXF = fechaNombreReporte+".dxf";
        poligonoActual.nombreArchivoImagen = fechaNombreReporte+".jpg";
        String nombreArchivoPDF = fechaNombreReporte+".pdf";
        fechaNombreReporte.replace(" ","");
        poligonoActual.nombreArchivoCSV = nombreArchivoCSV;
        poligonoActual.nombreArchivoGuardado = fechaNombreReporte;
        poligonoActual.nombreArchivoDXF = nombreArchivoDXF;
        poligonoActual.rutaArchivoCSV = poligonoActual.rutaCarpetaActual2 + File.separator + poligonoActual.nombreArchivoCSV;
        poligonoActual.rutaArchivoDXF = poligonoActual.rutaCarpetaActual2 + File.separator + poligonoActual.nombreArchivoDXF;

        /*File carpetaProyectosGPST = new File (Poligono.rutaCarpetaActual);
        if (!carpetaProyectosGPST.exists()) {
            carpetaProyectosGPST.mkdirs();
        }
        File carpetaProyectoActual = new File (Poligono.rutaCarpetaActual2);*/
        ////////////////////////////////////////

        File carpetaProyectoActual = new File(poligonoActual.rutaCarpetaActual);
        if (!carpetaProyectoActual.exists()){
            carpetaProyectoActual.mkdirs();
        }

        //Toast.makeText(this, "Directory exists", Toast.LENGTH_SHORT).show();

        ///////////////////////////////////////

        /*File carpetaProyectoActual = new File (Poligono.rutaCarpetaActual);
        boolean exitoCreandoCarpetaActual = true;

        if (!carpetaProyectoActual.exists()){
            Toast.makeText(this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
            exitoCreandoCarpetaActual = carpetaProyectoActual.mkdirs();
        }
        if (exitoCreandoCarpetaActual) {
            Toast.makeText(this, "Directory Created", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed - Error", Toast.LENGTH_SHORT).show();
        }*/


        usos = getUsosValueFromPref();

        if (valorSuscripcion) {
            generarPDF(poligonoActual.rutaCarpetaActual2, nombreArchivoPDF);
            generarTXT(poligonoActual.rutaCarpetaActual2);
            generarDXF();
            Toast.makeText(getApplicationContext(), "PRO User, 3 files generated!", Toast.LENGTH_LONG).show();

        } else {
            if (usos < 6){
                generarPDF(poligonoActual.rutaCarpetaActual2, nombreArchivoPDF);
                generarTXT(poligonoActual.rutaCarpetaActual2);
                generarDXF();
                usos = usos + 1;
                saveUsosValueToPref(usos);
                int usosRestantes = 5-getUsosValueFromPref();
                Toast.makeText(getApplicationContext(), "PDF and DXF files generated, your remaining uses of free trial: " + usosRestantes, Toast.LENGTH_LONG).show();

            }else {
                generarTXT(poligonoActual.rutaCarpetaActual2);
                Toast.makeText(getApplicationContext(), "No free trial, only TXT file was generated", Toast.LENGTH_LONG).show();

            }

        }

        //takeScreenshot(poligonoActual.rutaCarpetaActual);
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.archivos).toString() + "!  " + poligonoActual.rutaCarpetaActual, Toast.LENGTH_LONG).show();
    }

    public void generarTXT(String rutaCarpetaActual){
        //File file;
        int conti = poligonoActual.estaciones.size();
        DecimalFormat df = new DecimalFormat("################.###");
        String cadTemp = "";
        Uri uri = Uri.parse(poligonoActual.rutaCarpetaActual2 + File.separator + poligonoActual.nombreArchivoCSV);
        file = new File(Objects.requireNonNull(uri.getPath()));
        Objects.requireNonNull(file.getParentFile()).mkdirs();

        FileOutputStream pw = null;
        try {
            pw = new FileOutputStream(file);
            StringBuilder sb = new StringBuilder();
            cadTemp = "#Point,Description,X,Y,Z  -  (m)  -  www.d3sarrollo.com";
            sb.append(cadTemp);

            sb.append("\r\n");
            sb.append("\r\n");
            for (int F = 0; F < (conti - 1); F++) {
                Estacion estacionTemp = poligonoActual.estaciones.elementAt(F);
                sb.append(Integer.toString(F));
                sb.append(",");

                cadTemp = (estacionTemp.getObservaciones());
                sb.append(cadTemp);
                sb.append(",");

                cadTemp = df.format(estacionTemp.getLat());
                sb.append(cadTemp);
                sb.append(",");

                cadTemp = df.format(estacionTemp.getLon());
                sb.append(cadTemp);
                sb.append(",");
                cadTemp = df.format(estacionTemp.getAlt());
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
        drawDesign(dxfCanvas);
        try
        {
            String stringOutput = dxfDocument.toDXFString();
            System.out.println(stringOutput);
            String tarjetaSD = Environment.getExternalStorageDirectory().toString();
            String nombreCarpetaProyectos = "GPS_Topography_Projects";
            String rutaCarpetaProyectos = tarjetaSD + File.separator + nombreCarpetaProyectos;
            File carpetaProyectosGPST = new File (rutaCarpetaProyectos);
            if (!carpetaProyectosGPST.exists()) {
                carpetaProyectosGPST.mkdir();
            }
            File carpetaProyectoActual = new File (poligonoActual.rutaCarpetaActual2);
            if (!carpetaProyectoActual.exists()){
                carpetaProyectoActual.mkdir();
            }

            FileWriter dxfFileWriter = new FileWriter(poligonoActual.rutaCarpetaActual2+File.separator+poligonoActual.nombreArchivoDXF);
            dxfFileWriter.write(stringOutput);
            dxfFileWriter.flush();
            dxfFileWriter.close();
        }
        catch (IOException e)
        {
            System.out.println("Bah!!!!! " + e.toString());
        }
    }

    private void drawDesign(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        switch (poligonoActual.getTipoMedicion()) {
            case "poligono":
                dibujarEstaciones(canvas);
                dibujarLibreta(canvas);
                dibujarElementos(canvas);
                break;
            case "linea":
                dibujarEstaciones(canvas);
                dibujarLibreta(canvas);
                dibujarElementos(canvas);
                break;
            case "puntos":
                dibujarEstaciones(canvas);
                dibujarLibreta(canvas);
                break;
        }

    }

    public void dibujarEstaciones(Canvas canvas){
        for (int i = 0; i < poligonoActual.estaciones.size(); i++){
            Estacion estTemp = poligonoActual.estaciones.elementAt(i);
            String id = estTemp.idEst ;
            float cX = (float) (estTemp.xt * 1);
            float cY = (float) (estTemp.yt * -1);
            if (!estTemp.ultimoPunto) {
                dibujarNodo(canvas,id,cX,cY,(float)estTemp.xt,(float)estTemp.yt, estTemp.isPartePoligonoFinal());
            }
        }
    }

    private void dibujarNodo(Canvas canvas, String id, float cX, float cY, float coorX, float coorY, boolean partePoligonoFinal){
        final Paint paint = new Paint();
        paint.setColor(Color.BLUE);

        paint.setStyle(Paint.Style.STROKE);
        int tamano = 1;

        paint.setStrokeWidth(tamano);
        canvas.drawPoint(cX, cY, paint);
        canvas.drawCircle(cX, cY,10 * tamano, paint);
        paint.setStyle(Paint.Style.FILL);
        float division = (10 * tamano) / 2;
        canvas.drawCircle(cX, cY,division, paint);
        paint.setStyle(Paint.Style.FILL);
        DecimalFormat df = new DecimalFormat("###,###.##");
        String cadX = df.format(coorX);
        String cadY = df.format(coorY);

        paint.setTextSize(4f * tamano*12);
        paint.setStrokeWidth(tamano * 8);
        canvas.drawText(id, (float) (cX - (20*tamano*4)), cY+(4*tamano*-6), paint);
        paint.setTextSize(4f * tamano * 4);
        paint.setStrokeWidth(tamano);
        String cadCoor = "(" + cadX + " , " + cadY + ")";
        canvas.drawText(cadCoor, cX - 23, (cY*tamano)+30 , paint);
    }

    private Double YMin(){
        //encontrar coordenada minima en metros
        double yMin = 0;
        if (poligonoActual.estaciones.size()>0) {
            for (int i = 0; i < poligonoActual.estaciones.size(); i++) {
                Estacion e = poligonoActual.estaciones.elementAt(i);
                if (e.yt < yMin){
                    yMin = e.yt;
                }
                if (e.radiaciones.size() > 0) {
                    for (int j = 0; j < e.radiaciones.size(); j++) {
                        Estacion r = e.radiaciones.elementAt(j);
                        if (r.yt < yMin){
                            yMin = r.yt;
                        }
                    }
                }

            }
        }
        return yMin;
    }

    public void dibujarLibreta (Canvas canvas){
        final Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(40f);
        float x, y, x2,y2, xL1, xL2,xL3, xL4, xL5;
        String texto;
        x = 50;
        y = 50 + ((float)(Math.abs(YMin()) * 1));
        paint.setColor(Color.WHITE);
        x2 = 900;
        y2 = 40;
        xL1 = 125;
        xL2 = 250;
        xL3 = 400;
        xL4 = 600;
        xL5 = 750;

        canvas.drawLine(x, y, x + x2, y, paint);
        canvas.drawLine(x, y+y2, x + x2, y+y2, paint);

        //lineas verticales de la fila
        canvas.drawLine(x, y, x, y+y2, paint);
        canvas.drawLine(x+x2, y, x + x2, y+y2, paint);

        paint.setColor(Color.RED);
        paint.setTextSize(28f);
        texto = getResources().getText(R.string.libreta).toString();
        canvas.drawText(texto, x +300, y+27 , paint);

        //Segunda linea
        //lineas horizontales de la fila
        y += 40;

        paint.setColor(Color.WHITE);

        canvas.drawLine(x, y, x + x2, y, paint);
        canvas.drawLine(x, y+y2, x + x2, y+y2, paint);

        //lineas verticales de la fila
        canvas.drawLine(x, y, x, y+y2, paint);
        canvas.drawLine(x+x2, y, x + x2, y+y2, paint);
        //lineas division
        canvas.drawLine(x+xL1, y, x + xL1, y+y2, paint);
        canvas.drawLine(x+xL2, y, x + xL2, y+y2, paint);
        canvas.drawLine(x+xL3, y, x + xL3, y+y2, paint);
        canvas.drawLine(x+xL4, y, x + xL4, y+y2, paint);
        canvas.drawLine(x+xL5, y, x + xL5, y+y2, paint);

        paint.setColor(Color.RED);
        paint.setTextSize(28f);
        texto = getResources().getText(R.string.est).toString();
        canvas.drawText(texto, x +10, y+27 , paint);
        texto = getResources().getText(R.string.PO).toString();
        canvas.drawText(texto, x +10+xL1, y+27 , paint);

        texto = getResources().getText(R.string.dist).toString();
        canvas.drawText(texto, x +10+xL2, y+27 , paint);
        texto = getResources().getText(R.string.azimut).toString();
        canvas.drawText(texto, x +10+xL3, y+27 , paint);

        texto = getResources().getText(R.string.xt).toString();
        canvas.drawText(texto, x +10+xL4+40, y+27 , paint);
        texto = getResources().getText(R.string.yt).toString();
        canvas.drawText(texto, x +10+xL5+40, y+27 , paint);



        //imprimir la linea de la E-0
        y += 40;

        paint.setColor(Color.WHITE);

        canvas.drawLine(x, y, x + x2, y, paint);
        canvas.drawLine(x, y+y2, x + x2, y+y2, paint);

        //lineas verticales de la fila
        canvas.drawLine(x, y, x, y+y2, paint);
        canvas.drawLine(x+x2, y, x + x2, y+y2, paint);
        //lineas division
        canvas.drawLine(x+xL1, y, x + xL1, y+y2, paint);
        canvas.drawLine(x+xL2, y, x + xL2, y+y2, paint);
        canvas.drawLine(x+xL3, y, x + xL3, y+y2, paint);
        canvas.drawLine(x+xL4, y, x + xL4, y+y2, paint);
        canvas.drawLine(x+xL5, y, x + xL5, y+y2, paint);

        if (poligonoActual.estaciones.size()>0){
            if (poligonoActual.estaciones.elementAt(0).isPartePoligonoFinal()){
                paint.setColor(Color.YELLOW);
            } else {
                paint.setColor(Color.CYAN);
            }
        }
        paint.setTextSize(28f);
        texto = "-";
        canvas.drawText(texto, x +10+30, y+27 , paint);
        texto = "E-0";
        canvas.drawText(texto, x +10+xL1, y+27 , paint);
        texto = "-";
        canvas.drawText(texto, x +10+xL2+80, y+27 , paint);
        canvas.drawText(texto, x +10+xL3+80, y+27 , paint);
        texto = "0";
        canvas.drawText(texto, x +10+xL4, y+27 , paint);
        texto = "0";
        canvas.drawText(texto, x +10+xL5, y+27 , paint);

        if (poligonoActual.estaciones.size()>0){
            for (int i = 0; i< poligonoActual.estaciones.size(); i++){
                Estacion e = poligonoActual.estaciones.elementAt(i);
                if (i == 0){
                    //La linea de la E-0 ya esta dibujada, ahora hay que dibujar sus radiaciones
                    if (e.radiaciones.size()>0){
                        for (int j = 0; j < e.radiaciones.size(); j++){
                            Estacion r = e.radiaciones.elementAt(j);

                            //imprimir cada una de las radiaciones
                            y += 40;

                            paint.setColor(Color.WHITE);

                            canvas.drawLine(x, y, x + x2, y, paint);
                            canvas.drawLine(x, y+y2, x + x2, y+y2, paint);

                            //lineas verticales de la fila
                            canvas.drawLine(x, y, x, y+y2, paint);
                            canvas.drawLine(x+x2, y, x + x2, y+y2, paint);
                            //lineas division
                            canvas.drawLine(x+xL1, y, x + xL1, y+y2, paint);
                            canvas.drawLine(x+xL2, y, x + xL2, y+y2, paint);
                            canvas.drawLine(x+xL3, y, x + xL3, y+y2, paint);
                            canvas.drawLine(x+xL4, y, x + xL4, y+y2, paint);
                            canvas.drawLine(x+xL5, y, x + xL5, y+y2, paint);

                            if (r.isPartePoligonoFinal()){
                                paint.setColor(Color.YELLOW);
                            }else {
                                paint.setColor(Color.CYAN);
                            }

                            paint.setTextSize(22f);
                            texto = e.idEst;
                            canvas.drawText(texto, x +10, y+27 , paint);
                            texto = r.idEst;
                            canvas.drawText(texto, x +10+xL1, y+27 , paint);

                            DecimalFormat df = new DecimalFormat("###,###.##");
                            texto = df.format(r.dist);
                            canvas.drawText(texto, x +10+xL2, y+27 , paint);
                            texto = ((int) r.grado)+"°" + ((int) r.minuto)+"\'"+ df.format(r.segundo)+"\"";
                            canvas.drawText(texto, x +10+xL3, y+27 , paint);
                            texto = df.format(r.xt);
                            canvas.drawText(texto, x +10+xL4, y+27 , paint);
                            texto = df.format(r.yt);
                            canvas.drawText(texto, x +10+xL5, y+27 , paint);
                        }
                    }
                }else if (poligonoActual.estaciones.size()>1) {
                    Estacion e0 = poligonoActual.estaciones.elementAt(i-1);
                    //imprimir cada una de las estaciones
                    y += 40;

                    paint.setColor(Color.WHITE);

                    canvas.drawLine(x, y, x + x2, y, paint);
                    canvas.drawLine(x, y+y2, x + x2, y+y2, paint);

                    //lineas verticales de la fila
                    canvas.drawLine(x, y, x, y+y2, paint);
                    canvas.drawLine(x+x2, y, x + x2, y+y2, paint);
                    //lineas division
                    canvas.drawLine(x+xL1, y, x + xL1, y+y2, paint);
                    canvas.drawLine(x+xL2, y, x + xL2, y+y2, paint);
                    canvas.drawLine(x+xL3, y, x + xL3, y+y2, paint);
                    canvas.drawLine(x+xL4, y, x + xL4, y+y2, paint);
                    canvas.drawLine(x+xL5, y, x + xL5, y+y2, paint);

                    if (e.isPartePoligonoFinal()){
                        paint.setColor(Color.YELLOW);
                    }else {
                        paint.setColor(Color.CYAN);
                    }
                    paint.setTextSize(22f);
                    texto = e0.idEst;
                    canvas.drawText(texto, x +10, y+27 , paint);
                    texto = e.idEst;
                    canvas.drawText(texto, x +10+xL1, y+27 , paint);

                    DecimalFormat df = new DecimalFormat("###,###.##");
                    texto = df.format(e.dist);
                    canvas.drawText(texto, x +10+xL2, y+27 , paint);
                    texto = ((int) e.grado)+"°" + ((int) e.minuto)+"\'"+ df.format(e.segundo)+"\"";
                    canvas.drawText(texto, x +10+xL3, y+27 , paint);
                    texto = df.format(e.xt);
                    canvas.drawText(texto, x +10+xL4, y+27 , paint);
                    texto = df.format(e.yt);
                    canvas.drawText(texto, x +10+xL5, y+27 , paint);

                    //La linea de la E-0 ya esta dibujada, ahora hay que dibujar sus radiaciones
                    if (e.radiaciones.size()>0){
                        for (int j = 0; j < e.radiaciones.size(); j++){
                            Estacion r = e.radiaciones.elementAt(j);

                            //imprimir cada una de las radiaciones
                            y += 40;

                            paint.setColor(Color.WHITE);

                            canvas.drawLine(x, y, x + x2, y, paint);
                            canvas.drawLine(x, y+y2, x + x2, y+y2, paint);

                            //lineas verticales de la fila
                            canvas.drawLine(x, y, x, y+y2, paint);
                            canvas.drawLine(x+x2, y, x + x2, y+y2, paint);
                            //lineas division
                            canvas.drawLine(x+xL1, y, x + xL1, y+y2, paint);
                            canvas.drawLine(x+xL2, y, x + xL2, y+y2, paint);
                            canvas.drawLine(x+xL3, y, x + xL3, y+y2, paint);
                            canvas.drawLine(x+xL4, y, x + xL4, y+y2, paint);
                            canvas.drawLine(x+xL5, y, x + xL5, y+y2, paint);

                            if (r.isPartePoligonoFinal()){
                                paint.setColor(Color.YELLOW);
                            }else {
                                paint.setColor(Color.CYAN);
                            }
                            paint.setTextSize(22f);
                            texto = e.idEst;
                            canvas.drawText(texto, x +10, y+27 , paint);
                            texto = r.idEst;
                            canvas.drawText(texto, x +10+xL1, y+27 , paint);

                            df = new DecimalFormat("###,###.##");
                            texto = df.format(r.dist);
                            canvas.drawText(texto, x +10+xL2, y+27 , paint);
                            texto = ((int) r.grado)+"°" + ((int) r.minuto)+"\'"+ df.format(r.segundo)+"\"";
                            canvas.drawText(texto, x +10+xL3, y+27 , paint);
                            texto = df.format(r.xt);
                            canvas.drawText(texto, x +10+xL4, y+27 , paint);
                            texto = df.format(r.yt);
                            canvas.drawText(texto, x +10+xL5, y+27 , paint);
                        }
                    }
                }
            }
        }
    }

    private void dibujarElementos(Canvas canvas){
        if (poligonoActual.estaciones.size() > 1){
            for (int i = 0; i < poligonoActual.estaciones.size(); i++){
                if (i != 0){
                    Estacion estacion1 = poligonoActual.estaciones.elementAt(i-1);
                    Estacion estacion2 = poligonoActual.estaciones.elementAt(i);
                    float cXN = (float) (estacion1.xt * 1);
                    float cYN = (float) (estacion1.yt * -1);
                    float cXF = (float) (estacion2.xt * 1);
                    float cYF = (float) (estacion2.yt * -1);
                    if (cYN == 0) {
                        cYN = 0;
                    }
                    if (cYF == 0) {
                        cYF = 0;
                    }
                    dibujarElemento(canvas,cXN,cYN,cXF,cYF);
                }
            }
        }
    }

    private void dibujarElemento(Canvas canvas, float cXN, float cYN, float cXF, float cYF){
        final Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        int tamano = 1;
        //Dibujar Elemento
        paint.setStrokeWidth(2*tamano*2);
        canvas.drawLine(cXN, cYN, cXF, cYF, paint);
    }

    public void generarPDF(String rutaCarpetaProyectoActual, String nombreArchivoPDF) {
        PdfWriter writer = null;
        Uri uri = Uri.parse(poligonoActual.rutaCarpetaActual2 + File.separator + nombreArchivoPDF);
        file = new File(Objects.requireNonNull(uri.getPath()));
        file.getParentFile().mkdirs();

        Document document = new Document(PageSize.A4);
        autor = getResources().getText(R.string.app_name).toString() +getResources().getText(R.string.resto_autor).toString();

        encabezado = poligonoActual.getProyecto() + " " + poligonoActual.getFecha();
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            cabezera event = new cabezera();
            writer.setPageEvent(event);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this,"Error PDF ----  "+ e +"  -----, working on this.",Toast.LENGTH_LONG).show();

        }
        document.open();
        float anchoPaginaA4 = PageSize.A4.getRight() - 30;
        paint.setColor(Color.BLACK);
        PdfPTable tablaPro = generarTablaDatosProfesional(anchoPaginaA4);
        PdfPTable tablaDatos = generarTablaDatos(anchoPaginaA4);
        PdfPTable tabla6 = generarTabla6(anchoPaginaA4);
        PdfPTable tabla1 = generarTabla1(anchoPaginaA4);
        PdfPTable tabla2 = generarTabla2(anchoPaginaA4);
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
            //tabla GPS
            document.add(tabla6);
            document.add(ESPACIO);
            document.add(tabla1);
            document.add(ESPACIO);
            document.add(tabla2);
            document.add(ESPACIO);
            if (poligonoActual.getTipoMedicion().equals("poligono")){
                PdfPTable tablaError = generarTablaError(anchoPaginaA4);
                PdfPTable tabla3 = generarTabla3(anchoPaginaA4);
                PdfPTable tabla4 = generarTabla4(anchoPaginaA4);
                document.add(tablaError);
                document.add(ESPACIO);
                document.add(tabla3);
                document.add(ESPACIO);
                document.add(tabla4);
                document.add(ESPACIO);
            }

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

    private PdfPTable generarTabla1(float anchoPag) {
        tabla1 = new PdfPTable(11);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(2);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla1).toString();
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

                cell = new PdfPCell(new Phrase("Yp"));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase("Xp"));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

            } else {
                String idEstTempAntes;
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = poligonoActual.estaciones.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = poligonoActual.estaciones.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("###,###.##");

                cell = new PdfPCell(new Phrase(idEstTempAntes));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(estacionTemp.idEst));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                String cadTemp = df.format(estacionTemp.dist);
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
                String idEstTempAntes;
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = poligonoActual.estaciones.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = poligonoActual.estaciones.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("###,###.##");
                String cadTemp;

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
        DecimalFormat df = new DecimalFormat("#,###,###.####");

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

        String cadTemp = df.format(poligonoActual.getSumaNortes());
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
                String idEstTempAntes;
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = poligonoActual.estaciones.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = poligonoActual.estaciones.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("###,###.####");
                String cadTemp;

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
                String idEstTempAntes;
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = poligonoActual.estaciones.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = poligonoActual.estaciones.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("###,###.###");
                String cadTemp;

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
        String cadTemp;
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
                String idEstTempAntes;
                if (F == 1) {
                    idEstTempAntes = "-";
                } else {
                    Estacion estacionTempAntes = poligonoActual.estaciones.elementAt(F - 2);
                    idEstTempAntes = estacionTempAntes.idEst;
                }
                Estacion estacionTemp = poligonoActual.estaciones.elementAt(F - 1);
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

        /////////////
        cell = new PdfPCell(new Phrase(getResources().getText(R.string.perimetro).toString()));
        cell.setColspan(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        df1 = new DecimalFormat("###,###,###,###,###.###");
        fuente.setSize(10);

        cadTemp = df1.format(perimetroP);
        cell = new PdfPCell(new Phrase(cadTemp,fuente));
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        return tabla1;


    }

    private PdfPTable generarTabla6(float anchoPag) {
        tabla1 = new PdfPTable(12);
        float anchoTabla = anchoPag - 40;
        tabla1.setTotalWidth(anchoTabla);
        tabla1.setLockedWidth(true);
        tabla1.setHeaderRows(2);
        PdfPCell cell;
        String tituloTabla = getResources().getText(R.string.tabla6).toString();
        cell = new PdfPCell(new Phrase(tituloTabla));
        cell.setColspan(12);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        tabla1.addCell(cell);

        for (int F = 0; F < (poligonoActual.estaciones.size()+1); F++) {
            if (F == 0) {

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.est).toString()));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase("#"));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.latitud).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.longitud).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.elevacion).toString()));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(getResources().getText(R.string.utm).toString()));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

            } else {
                Estacion estacionTemp = poligonoActual.estaciones.elementAt(F - 1);
                DecimalFormat df = new DecimalFormat("#,###.#######");
                String cadTemp;

                cell = new PdfPCell(new Phrase(estacionTemp.idEst));
                cell.setColspan(1);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = estacionTemp.getObservaciones();
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.getLat());
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.getLon());
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cadTemp = df.format(estacionTemp.getAlt());
                cell = new PdfPCell(new Phrase(cadTemp));
                cell.setColspan(2);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);

                cell = new PdfPCell(new Phrase(estacionTemp.UTM));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla1.addCell(cell);
            }
        }

        return tabla1;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onStart() {
        super.onStart();

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            } else {
                Log.d(TAG, "Cancelled ");
            }
        }


    }

}