package com.desarrollojlcp.gps_topography;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@SuppressLint("ClickableViewAccessibility")
public class VistaTrabajo extends View implements View.OnTouchListener {

    protected float displayScale = 4.0f;
    protected float xCen = 125;
    protected float yCen = 900;
    private float previousX = 0;
    private float previousY = 0;
    private ScaleGestureDetector scaleDetector;
    protected Canvas canvasParaExportar;
    private Poligono poligonoTrabajo;

    protected void actualizarVistaTrabajo(Poligono poligono){
        poligonoTrabajo = poligono;
        xCen = poligono.xCen;
        yCen = poligono.yCen;
        displayScale = poligono.displayScale;
        invalidate();
    }

    protected void ingresarPoligono (Poligono poligono){
        poligonoTrabajo = poligono;
    }

    public VistaTrabajo(Context context) {
        super(context);
    }

    public VistaTrabajo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        scaleDetector = new ScaleGestureDetector(this.getContext(),new ScaleListener());
        setOnTouchListener(this);
    }

    public VistaTrabajo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public VistaTrabajo(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void vistaTrabajo(){
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector){
            float newDisplayScale = displayScale * detector.getScaleFactor();
            displayScale = newDisplayScale;
            invalidate();
            return true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        scaleDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                previousX = event.getX();
                previousY = event.getY();
                break;
            } case MotionEvent.ACTION_MOVE: {
                float deltaX = event.getX() - previousX;
                float deltaY = event.getY() - previousY;
                xCen += deltaX;
                yCen += deltaY;
                previousX = event.getX();
                previousY = event.getY();
                invalidate();
                break;
            } case MotionEvent.ACTION_UP: {
                break;
            } default: {
                break;
            }
        }
        return true;
    }

    public void onDraw(Canvas canvas){
        canvas.translate(xCen,yCen);
        canvas.scale(displayScale,displayScale);
        final Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        dibujarCosas(canvas);
        canvasParaExportar = canvas;
    }

    public void dibujarCosas (Canvas canvas){
        dibujarPlano(canvas);
        dibujarEstaciones(canvas);
        dibujarRadiaciones(canvas);
        dibujarElementos(canvas);
        dibujarPoligonoFinal(canvas);
        dibujarLibreta(canvas);
    }

    public void dibujarCosas2 (Canvas canvas){
        dibujarEstaciones(canvas);
        dibujarRadiaciones(canvas);
        dibujarElementos(canvas);
        dibujarPoligonoFinal(canvas);
        dibujarLibreta(canvas);
    }

    private void dibujarPlano(Canvas canvas){
        int x = 10000;
        float cX = (float) (x * 20);
        float cY = (float) (x * -20);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        //paint.setColor(getResources().getColor(R.color.colorNegro));
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        if (displayScale<1.75f){
            paint.setStrokeWidth((float)4);
        }else {
            paint.setStrokeWidth((float)1.5);
        }
        //Dibujar Plano Cartesiano
        paint.setColor(getResources().getColor(android.R.color.black));
        canvas.drawLine(0, cY, 0, (-1*cY), paint);
        canvas.drawLine(cX, 0, (-1*cX),0, paint);

        paint.setTextSize(30f);
        paint.setStrokeWidth((float)3);

        //Dibujar numeracion de plano
        for (int i = 1; i < x; i++){
            if((i%20)==0){
                String cadCoor = String.valueOf(i);
                canvas.drawText(cadCoor, -20, (i*(-20)) , paint);
                canvas.drawText("-"+cadCoor, -25,-1*(i*(-20)) , paint);
                canvas.drawText(cadCoor, (i*(20)), 14 , paint);
                canvas.drawText("-"+cadCoor, (i*(-20)), 14 , paint);
            }
        }
    }

    private void dibujarEstaciones(Canvas canvas){
        poligonoTrabajo.calculoCoorParciales();
        if (poligonoTrabajo.estaciones.size() > 0){
            //poligonoTrabajo.calculoCoorTotales();
            //poligonoTrabajo.calculoCoorTotalesDibujar(poligonoTrabajo.estaciones.elementAt(0).xt, poligonoTrabajo.estaciones.elementAt(0).yt);
            poligonoTrabajo.calculoCoorTotalesDibujar(0, 0);

            for (int i = 0; i < poligonoTrabajo.estaciones.size(); i++){
                Estacion estTemp = poligonoTrabajo.estaciones.elementAt(i);
                String id = estTemp.idEst ;
                float cX = (float) (estTemp.xt * 20);
                float cY = (float) (estTemp.yt * -20);
                if (estTemp.ultimoPunto) {

                } else {
                    dibujarNodo(canvas,id,cX,cY,(float)estTemp.xt,(float)estTemp.yt, estTemp.partePoligonoFinal);

                    if(estTemp.radiaciones.size()>0){
                            for (int j = 0; j < estTemp.radiaciones.size(); j++){
                                Estacion radiacion = estTemp.radiaciones.elementAt(j);
                                float cXr = (float) (radiacion.xt * 20);
                                float cYr = (float) (radiacion.yt * -20);
                                dibujarNodo(canvas,radiacion.idEst,cXr,cYr,(float) radiacion.xt,(float)radiacion.yt, radiacion.partePoligonoFinal);

                        }
                    }
                }
            }
        }
    }

    private void dibujarNodo(Canvas canvas, String id, float cX, float cY, float coorX, float coorY, boolean partePoligonoFinal){
        final Paint paint = new Paint();
        if (partePoligonoFinal){
            paint.setColor(Color.BLACK);
        } else {
            paint.setColor(Color.BLUE);
        }
        paint.setStyle(Paint.Style.STROKE);
        int tamano = 0;
        if(this.displayScale>=1.75f){
            tamano = 1;
        }else if (this.displayScale<1.75f){
            tamano = 1;
        } else if (this.displayScale<1.0f){
            tamano = 2*4*2;
        }else if (this.displayScale<0.50f){
            tamano = 4*16*2;
        }
        else if (this.displayScale<0.25f){
            tamano = 4*64*2;
        }
        paint.setStrokeWidth(1*tamano);
        canvas.drawPoint(cX, cY, paint);
        canvas.drawCircle(cX, cY,10 * tamano, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cX, cY,((10 * tamano)/2), paint);

        paint.setStyle(Paint.Style.FILL);

        DecimalFormat df = new DecimalFormat("###,###.##");
        String cadX = df.format(coorX);
        String cadY = df.format(coorY);

        paint.setTextSize(4f * tamano*12);
        paint.setStrokeWidth(1 * tamano * 8);
        canvas.drawText(id, (float) (cX - (20*tamano*4)), cY+(4*tamano*-6), paint);
        paint.setTextSize(4f * tamano * 4);
        paint.setStrokeWidth(1* tamano);
        String cadCoor = "(" + cadX + " , " + cadY + ")";
        canvas.drawText(cadCoor, cX - 23, (cY*tamano)+30 , paint);
    }


    public void dibujarRadiaciones (Canvas canvas){
        if (poligonoTrabajo.estaciones.size()>0){
            for (int i = 0; i < poligonoTrabajo.estaciones.size(); i++){
                Estacion estacion1 = poligonoTrabajo.estaciones.elementAt(i);
                if (estacion1.radiaciones.size()>0){
                    for (int j = 0; j < estacion1.radiaciones.size(); j++){
                        Estacion radiacion = estacion1.radiaciones.elementAt(j);

                        float cXN = (float) (estacion1.xt * 20);
                        float cYN = (float) (estacion1.yt * -20);
                        float cXF = (float) (radiacion.xt * 20);
                        float cYF = (float) (radiacion.yt * -20);

                        if (cYN == 0) {
                            cYN = 0;
                        }
                        if (cYF == 0) {
                            cYF = 0;
                        }

                        if (estacion1.partePoligonoFinal && radiacion.partePoligonoFinal){
                            dibujarElemento(canvas,cXN,cYN,cXF,cYF, Color.RED);
                        }else {
                            dibujarElemento(canvas,cXN,cYN,cXF,cYF, Color.BLUE);
                        }
                    }
                }
            }
        }
    }

    private Double YMin(){
        //encontrar coordenada minima en metros
         double yMin = 0;
        if (poligonoTrabajo.estaciones.size()>0) {
            for (int i = 0; i < poligonoTrabajo.estaciones.size(); i++) {
                Estacion e = poligonoTrabajo.estaciones.elementAt(i);
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

        //dibujar linea de encabezado
        //Coordenadas actuales
        float x, y, x2,y2, xL1, xL2,xL3, xL4, xL5;
        String texto = "";

        x = 50;
        y = 50 + ((float)(Math.abs(YMin()) * 20));

        paint.setColor(Color.BLACK);

        //lineas horizontales de la fila, ENCABEZADO
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

        paint.setColor(Color.BLACK);
        paint.setTextSize(28f);
        texto = getResources().getText(R.string.libreta).toString();
        canvas.drawText(texto, x +300, y+27 , paint);

        //Segunda linea
        //lineas horizontales de la fila
        y += 40;

        paint.setColor(Color.BLACK);

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

        paint.setColor(Color.BLUE);
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

        paint.setColor(Color.BLACK);

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

        if (poligonoTrabajo.estaciones.size()>0){
            if (poligonoTrabajo.estaciones.elementAt(0).partePoligonoFinal){
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(Color.BLUE);
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

        if (poligonoTrabajo.estaciones.size()>0){
            for (int i = 0; i< poligonoTrabajo.estaciones.size(); i++){
                Estacion e = poligonoTrabajo.estaciones.elementAt(i);
                if (i == 0){
                    //La linea de la E-0 ya esta dibujada, ahora hay que dibujar sus radiaciones
                    if (e.radiaciones.size()>0){
                        for (int j = 0; j < e.radiaciones.size(); j++){
                            Estacion r = e.radiaciones.elementAt(j);

                            //imprimir cada una de las radiaciones
                            y += 40;

                            paint.setColor(Color.BLACK);

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

                            if (r.partePoligonoFinal){
                                paint.setColor(Color.BLACK);
                            }else {
                                paint.setColor(Color.BLUE);
                            }

                            paint.setTextSize(28f);
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
                }else if (poligonoTrabajo.estaciones.size()>1) {
                    Estacion e0 = poligonoTrabajo.estaciones.elementAt(i-1);
                    //imprimir cada una de las estaciones
                    y += 40;

                    paint.setColor(Color.BLACK);

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

                    if (e.partePoligonoFinal){
                        paint.setColor(Color.BLACK);
                    }else {
                        paint.setColor(Color.BLUE);
                    }
                    paint.setTextSize(28f);
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

                            paint.setColor(Color.BLACK);

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

                            if (r.partePoligonoFinal){
                                paint.setColor(Color.BLACK);
                            }else {
                                paint.setColor(Color.BLUE);
                            }
                            paint.setTextSize(28f);
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
        if (poligonoTrabajo.estaciones.size() > 1){
            for (int i = 0; i < poligonoTrabajo.estaciones.size(); i++){
                if (i == 0){
                }else {
                    Estacion estacion1 = poligonoTrabajo.estaciones.elementAt(i-1);
                    Estacion estacion2 = poligonoTrabajo.estaciones.elementAt(i);
                    float cXN = (float) (estacion1.xt * 20);
                    float cYN = (float) (estacion1.yt * -20);
                    float cXF = (float) (estacion2.xt * 20);
                    float cYF = (float) (estacion2.yt * -20);
                    if (cYN == 0) {
                        cYN = 0;
                    }
                    if (cYF == 0) {
                        cYF = 0;
                    }
                    if (estacion1.partePoligonoFinal && estacion2.partePoligonoFinal){
                        dibujarElemento(canvas,cXN,cYN,cXF,cYF, Color.RED);
                    }else {
                        dibujarElemento(canvas,cXN,cYN,cXF,cYF, Color.MAGENTA);
                    }
                }
            }
        }
    }

    private void dibujarElemento(Canvas canvas, float cXN, float cYN, float cXF, float cYF, int color){
        final Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);

        float cXMedio = (cXN + cXF) / 2;
        float cYMedio = (cYN + cYF) / 2;
        float deltaX = cXF - cXN;
        float deltaY = cYF - cYN;
        int tamano = 0;
        if(this.displayScale>=1.75f){
            tamano = 1;
        }else if (this.displayScale<1.75f){
            tamano = 1;
        } else if (this.displayScale<1.0f){
            tamano = 2*2;
        }else if (this.displayScale<0.50f){
            tamano = 4*2;
        }
        //Dibujar Elemento
        paint.setStrokeWidth(2*tamano*2);
        canvas.drawLine(cXN, cYN, cXF, cYF, paint);
    }


    private void dibujarPoligonoFinal(Canvas canvas){
        poligonoTrabajo.calculosParaPoligonoFinal();
        if (poligonoTrabajo.estacionesPolFinal.size()>0){
            for (int i = 0; i < poligonoTrabajo.estacionesPolFinal.size(); i++){
                if (i>0){
                    Estacion e0 = poligonoTrabajo.estacionesPolFinal.elementAt(i-1);
                    Estacion e1 = poligonoTrabajo.estacionesPolFinal.elementAt(i);

                    float cXN = (float) (e0.xt * 20);
                    float cYN = (float) (e0.yt * -20);
                    float cXF = (float) (e1.xt * 20);
                    float cYF = (float) (e1.yt * -20);
                    if (cYN == 0) {
                        cYN = 0;
                    }
                    if (cYF == 0) {
                        cYF = 0;
                    }

                    dibujarElemento(canvas,cXN,cYN,cXF,cYF, Color.RED);

                }
                if (i == (poligonoTrabajo.estacionesPolFinal.size()-1)){
                    Estacion e0 = poligonoTrabajo.estacionesPolFinal.elementAt(i);
                    Estacion e1 = poligonoTrabajo.estacionesPolFinal.elementAt(0);

                    float cXN = (float) (e0.xt * 20);
                    float cYN = (float) (e0.yt * -20);
                    float cXF = (float) (e1.xt * 20);
                    float cYF = (float) (e1.yt * -20);
                    if (cYN == 0) {
                        cYN = 0;
                    }
                    if (cYF == 0) {
                        cYF = 0;
                    }

                    dibujarElemento(canvas,cXN,cYN,cXF,cYF, Color.RED);

                }
            }
            }





    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
