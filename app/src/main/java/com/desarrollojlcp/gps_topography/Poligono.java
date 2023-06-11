/*
 * Copyright (C) <2017>  <Juan Luis Carrillo Paxtor, Desarrollo JLCP>
 *
 * Copyright (c) GPS Topography y Polygon Area AC son propiedad de Desarrollo JLCP, basada en Quetzaltenango, Guatemala, C.A.  Propiedad de Juan Luis Carrillo Paxtor (JLCP). Se distribuye bajo la licensia AGPL y bajo las siguientes condiciones: 1) tu no distribuiras software en ninguna red sin liberar la totalidad del codigo fuente de tu software bajo la licensia AGPL.  2) debes liberar la totalidad del codigo fuente. 3) Debes liberar cualquier modificacion al codigo de GPS Topography y Polygon Area AC incluyendo modificaciones a las clases Estacion, Poligono y Azimut. 4) Debes mencionar oportunamente a GPS Topography y Desarrollo JLCP e incluir el copyright de GPS Topography  y la licencia AGPL en el metadata.
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * __________________________________________________________________________________________________________________________
 * iText Copyright
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General Public License version 3 as published by the Free Software Foundation with the addition of the following permission added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY ITEXT GROUP NV, ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. You should have received a copy of the GNU Affero General Public License along with this program; if not, see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions of this program must display Appropriate Legal Notices, as required under Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, you must retain the producer line in every PDF that is created or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is mandatory as soon as you develop commercial activities involving the iText software without disclosing the source code of your own applications. These activities include: offering paid services to customers as an ASP, serving PDFs on the fly in a web application, shipping iText with a closed source product.
 * _________________________________________________________________________________________________________________________
 * Copyright Coordinate Conversion by Sami Salkosuo
 * https://www.ibm.com/developerworks/library/j-coordconvert/index.html
 *
 * Downloader Agreement
 * The following are terms of a legal downloader agreement (the "Agreement") regarding Your download of Content (as defined below) from this Website. IBM may change these terms of use and other requirements and guidelines for use of this Website at its sole discretion. This Website may contain other proprietary notices and copyright information the terms of which must be observed and followed. Any use of the Content in violation of this Agreement is strictly prohibited.
 * "Content" includes, but is not limited to, software, text and/or speech files, code, associated materials, media and /or documentation that You download from this Website. The Content may be provided by IBM or third-parties. IBM Content is owned by IBM and is copyrighted and licensed, not sold. Third-party Content is owned by its respective owner and is licensed by the third party directly to You. IBM's decision to permit posting of third-party Content does not constitute an express or implied license from IBM to You or a recommendation or endorsement by IBM of any particular product, service, company or technology.
 * The party providing the Content (the "Provider") grants You a nonexclusive, worldwide, irrevocable, royalty-free, copyright license to edit, copy, reproduce, publish, publicly display and/or perform, format, modify and/or make derivative works of, translate, re-arrange, and distribute the Content or any portions thereof and to sublicense any or all such rights and to permit sublicensees to further sublicense such rights, for both commercial and non-commercial use, provided You abide by the terms of this Agreement. You understand that no assurances are provided that the Content does not infringe the intellectual property rights of any other entity. Neither IBM nor the provider of the Content grants a patent license of any kind, whether expressed or implied or by estoppel. As a condition of exercising the rights and licenses granted under this Agreement, You assume sole responsibility to obtain any other intellectual property rights needed.
 * The Provider of the Content is the party that submitted the Content for Posting and who represents and warrants that they own all of the Content, (or have obtained all written releases, authorizations and licenses from any other owner(s) necessary to grant IBM and downloaders this license with respect to portions of the Content not owned by the Provider). All information provided on or through this Website may be changed or updated without notice. You understand that IBM has no obligation to check information and /or Content on the Website and that the information and/or Content provided on this Web site may contain technical inaccuracies or typographical errors.
 * IBM may, in its sole discretion, discontinue the Website, any service provided on or through the Website, as well as limit or discontinue access to any Content posted on the Website for any reason without notice. IBM may terminate this Agreement and Your rights to access, use and download Content from the Website at any time, with or without cause, immediately and without notice.
 * ALL INFORMATION AND CONTENT IS PROVIDED ON AN "AS IS" BASIS. IBM MAKES NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, CONCERNING USE OF THE WEBSITE, THE CONTENT, OR THE COMPLETENESS OR ACCURACY OF THE CONTENT OR INFORMATION OBTAINED FROM THE WEBSITE. IBM SPECIFICALLY DISCLAIMS ALL WARRANTIES WITH REGARD TO THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. IBM DOES NOT WARRANT UNINTERRUPTED OR ERROR-FREE OPERATION OF ANY CONTENT. IBM IS NOT RESPONSIBLE FOR THE RESULTS OBTAINED FROM THE USE OF THE CONTENT OR INFORMATION OBTAINED FROM THE WEBSITE.
 * LIMITATION OF LIABILITY. IN NO EVENT WILL IBM BE LIABLE TO ANY PARTY FOR ANY DIRECT, INDIRECT, SPECIAL OR OTHER CONSEQUENTIAL DAMAGES FOR ANY USE OF THIS WEBSITE, THE USE OF CONTENT FROM THIS WEBSITE, OR ON ANY OTHER HYPER LINKED WEB SITE, INCLUDING, WITHOUT LIMITATION, ANY LOST PROFITS, BUSINESS INTERRUPTION, LOSS OF PROGRAMS OR OTHER DATA ON YOUR INFORMATION HANDLING SYSTEM OR OTHERWISE, EVEN IF IBM IS EXPRESSLY ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * The laws of the State of New York, USA govern this Agreement, without reference to conflict of law principles. The "United Nations Convention on International Sale of Goods" does not apply. This Agreement may not be assigned by You. The parties agree to waive their right to a trial by jury.
 * This Agreement is the complete and exclusive agreement between the parties and supersedes all prior agreements, oral or written, and all other communications relating to the subject matter hereof. For clarification, it is understood and You agree, that any additional agreement or license terms that may accompany the Content is invalid, void, and non-enforceable to any downloader of this Content including IBM.
 * If any section of this Agreement is found by competent authority to be invalid, illegal or unenforceable in any respect for any reason, the validity, legality and enforceability of any such section in every other respect and the remainder of this Agreement shall continue in effect.
 * __________________________________________________________________________________________________________________________
 * Copyright ANDROID ADXF LIBRARY by Jonathan Sevy
 *
 * MIT License
 *
 * Copyright (c) [year] [fullname]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.desarrollojlcp.gps_topography;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.*;

/**
 * Created by LoCo5 on 27/05/2017.
 */

@SuppressWarnings("serial")
public class Poligono extends Object implements Serializable {

    //Declaración de variables de poligono
    protected float xCen, yCen, displayScale;
    static String fecha = "";                          //Fecha en que se creo el poligono
    public static double area;                         //area del poligono en metros cuadrados
    public static double perimetro = 0;                //perimetro del poligono, en metros
    public static double sumaNortes = 0;
    public static double sumaSures = 0;
    public static double sumaEstes = 0;
    public static double sumaOestes = 0;
    public static double sumaDist = 0;
    public static double sumaYX = 0;
    public static double sumaXY = 0;
    public static double deltaX = 0;                   //Error de cierre en el eje X
    public static double deltaY = 0;                   //Error de cierre en el eje Y
    public static double errC = 0;                     // error de cierre
    public static double errCierreUn = 0;
    public static double errA = 0;                     //Error de cierre admisible
    public static double Cy = 0;
    public static double Cx = 0;
    public static Vector corrN = new Vector();
    public static Vector corrS = new Vector();
    public static Vector corrE = new Vector();
    public static Vector corrO = new Vector();
    public static int contEs = 0;                     //contador de estaciones ó puntos
    public static String proyecto = "";               //Nombre del proyecto a que corresponde el poligono
    public static String cliente = "";                //Nombre del cliente a que corresponde el poligono
    public static String ubicacion = "";              //Ubicacion ó dirección del poligono
    public static String responsable = "";            //Nombre del Ingeniero/Arquitecto/Topografo responsable
    public static Vector<Estacion> estaciones = new Vector<Estacion>();    //vector para contener todas las estaciones
    public static Vector<Estacion> estacionesPolFinal = new Vector<Estacion>();    //vector para contener todas las estaciones

    private static boolean estFinal = false;
    public static boolean errorAceptable = false;
    public static String nombreArchivoGuardado = "";
    public static String rutaArchivoGuardado = "";
    public static String tipoTerreno = "";
    public static String rutaArchivoCSV= "";
    public static String nombreArchivoCSV = "";
    public static String rutaArchivoDXF= "";
    public static String rutaArchivoImagen= "";
    public static String nombreArchivoImagen= "";

    public static String nombreArchivoDXF = "";
    public static String opMenuPrincipal = "0";
    public static double yto = 0.0;
    public static double xto = 0.0;
    public static double zto = 0.0;
    public static double xtmin, xtmax, ytmin, ytmax;
    public static String rutaCarpetaActual;
    public static String rutaCarpetaActual2;
    protected boolean primeraPantalla;

    public LatLng puntos;
    public String errorCumple = "";
    public int id_bd= 0;
    public int tPol= 0;
    public String tipoMedicion = "";






    //Constructor de Poligono
    public Poligono() {
        this.area = 0;
        this.perimetro = 0;
    }

    //copy constructor
    public Poligono Poligono(Poligono pol){
        this.fecha = pol.fecha;                          //Fecha en que se creo el poligono
        this.area = pol.area;                         //area del poligono en metros cuadrados
        this.perimetro = pol.perimetro;                //perimetro del poligono, en metros
        this.sumaNortes = pol.sumaNortes;
        this.sumaSures = pol.sumaSures;
        this.sumaEstes = pol.sumaEstes;
        this.sumaOestes = pol.sumaOestes;
        this.sumaDist = pol.sumaDist;
        this.sumaYX = pol.sumaYX;
        this.sumaXY = pol.sumaXY;
        this.deltaX = pol.deltaX;                   //Error de cierre en el eje X
        this.deltaY = pol.deltaY;                   //Error de cierre en el eje Y
        this.errC = pol.errC;                     // error de cierre
        this.errCierreUn = pol.errCierreUn;
        this.errA = 0.003;                     //Error de cierre admisible
        this.Cy = pol.Cy;
        this.Cx = pol.Cx;
        this.estaciones = pol.estaciones;
        this.corrN = pol.corrN;
        this.corrS = pol.corrS  ;
        this.corrE = pol.corrE;
        this.corrO = pol.corrO;
        this.contEs = pol.contEs;                     //contador de estaciones ó puntos
        this.proyecto = pol.proyecto;               //Nombre del proyecto a que corresponde el poligono
        this.cliente = pol.cliente;                //Nombre del cliente a que corresponde el poligono
        this.ubicacion = pol.ubicacion;              //Ubicacion ó dirección del poligono
        this.responsable = pol.responsable;            //Nombre del Ingeniero/Arquitecto/Topografo responsable
        this.estFinal = pol.estFinal;
        this.errorAceptable = pol.errorAceptable;
        this.nombreArchivoGuardado = pol.nombreArchivoGuardado;
        this.rutaArchivoGuardado = pol.rutaArchivoGuardado;
        this.tipoTerreno = pol.tipoTerreno;
        this.rutaArchivoCSV= pol.rutaArchivoCSV;
        this.nombreArchivoCSV = pol.nombreArchivoCSV;
        this.opMenuPrincipal = pol.opMenuPrincipal;
        this.yto = pol.yto;
        this.xto = pol.xto;
        this.rutaArchivoDXF= pol.rutaArchivoDXF;
        this.nombreArchivoDXF = pol.nombreArchivoDXF;
        this.xto = pol.xto;
        this.xtmin = pol.xtmin;
        this.xtmax = pol.xtmax;
        this.ytmin = pol.ytmin;
        this.ytmax = pol.ytmax;
        this.rutaCarpetaActual = pol.rutaCarpetaActual;


        this.puntos = pol.puntos;
        return this;
    }

    public  boolean poligonoCargado = false;


    public Vector getEstaciones(){
        return this.estaciones;
    }

    public void setEstaciones(Vector estaciones1){
        this.estaciones = estaciones1;
    }

    public void ingresarEstacion(Estacion est){
        this.estaciones.addElement(est);
    }

    public void ingresoDatosProyecto(String fechaI, String proyectoI) {
        fecha = fechaI;
        proyecto = proyectoI;
        /*cliente = clienteI;
        ubicacion = ubicacionI;
        responsable = responsableI;*/
        area = 0;
        perimetro = 0;
    }



    public void calculoUTM(){
        Estacion estIniFin = this.estaciones.elementAt(0);
        Estacion nuevaIni = new Estacion();
        String idEstIni = estIniFin.idEst;
        double lati = estIniFin.lat;
        double longi = estIniFin.lon;
        double elevi = estIniFin.alt;
        nuevaIni.idEst = idEstIni;
        nuevaIni.lat = lati;
        nuevaIni.lon = longi;
        nuevaIni.alt = elevi;
        nuevaIni.xp = 0;
        nuevaIni.yp = 0;
        nuevaIni.xt = 0;
        nuevaIni.yt = 0;
        if (tipoMedicion.equals("poligono")){
            this.ingresarEstacion(estIniFin);
        }
        this.estaciones.setElementAt(nuevaIni,0);
        for (int i = 0; i < this.estaciones.size(); i++) {
            Estacion estacionTemp = this.estaciones.elementAt(i);
            CoordinateConversion objetoCoversor = new CoordinateConversion();
            String utm = objetoCoversor.latLon2UTM(estacionTemp.lat, estacionTemp.lon);
            estacionTemp.UTM = utm;
            String[] utmArray = utm.split(" ");
            estacionTemp.zone = Integer.parseInt(utmArray[0]);
            estacionTemp.latZone = utmArray[1];
            estacionTemp.easting = Double.parseDouble(utmArray[2]);
            estacionTemp.northing = Double.parseDouble(utmArray[3]);
            if (i == 0){
            }else if (i >0){
                Estacion estacionTempAnterior = this.estaciones.elementAt(i-1);
                estacionTemp.xp = estacionTemp.easting - estacionTempAnterior.easting;
                estacionTemp.yp = estacionTemp.northing - estacionTempAnterior.northing;
                estacionTemp.dist = Math.sqrt(Math.pow(estacionTemp.xp,2)+Math.pow(estacionTemp.yp,2));
                if ((estacionTemp.xp >= 0)&&(estacionTemp.yp >= 0)){
                    estacionTemp.valorDecimalGrados = Math.toDegrees(Math.atan(estacionTemp.xp/estacionTemp.yp));
                }else if ((estacionTemp.xp >= 0)&&(estacionTemp.yp < 0)){
                    estacionTemp.valorDecimalGrados = 90 + Math.toDegrees(Math.atan(estacionTemp.yp/estacionTemp.xp));
                }else if ((estacionTemp.xp < 0)&&(estacionTemp.yp < 0)) {
                    estacionTemp.valorDecimalGrados = 180 + Math.toDegrees(Math.atan(estacionTemp.xp/estacionTemp.yp));
                }else if ((estacionTemp.xp < 0)&&(estacionTemp.yp >= 0)){
                    estacionTemp.valorDecimalGrados = 270 + Math.toDegrees(Math.atan(estacionTemp.yp/estacionTemp.xp));
                }
                estacionTemp.grado = (int) estacionTemp.valorDecimalGrados;
                estacionTemp.minuto = (int) ((estacionTemp.valorDecimalGrados - estacionTemp.grado)*60);
                estacionTemp.segundo = (int) ((estacionTemp.valorDecimalGrados - estacionTemp.grado - (estacionTemp.minuto/60))*3600);
            }
            this.estaciones.setElementAt(estacionTemp,i);
        }

    }

    public void calculoUTM2(){
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion estacionTemp =  estaciones.elementAt(i);
            CoordinateConversion objetoCoversor = new CoordinateConversion();
            String utm = objetoCoversor.latLon2UTM(estacionTemp.lat, estacionTemp.lon);
            estacionTemp.UTM = utm;
            String[] utmArray = utm.split(" ");
            estacionTemp.zone = Integer.parseInt(utmArray[0]);
            estacionTemp.latZone = utmArray[1];
            estacionTemp.easting = Double.parseDouble(utmArray[2]);
            estacionTemp.northing = Double.parseDouble(utmArray[3]);
            if (i == 0){
                estacionTemp.dist = 0.0;
                estacionTemp.grado = 0.0;
                estacionTemp.minuto = 0.0;
                estacionTemp.segundo = 0.0;
                estacionTemp.seno = 0.0;
                estacionTemp.coseno = 0.0;
            }else if (i >0){
                Estacion estacionTempAnterior = estaciones.elementAt(i-1);
                estacionTemp.xp = estacionTemp.easting - estacionTempAnterior.easting;
                estacionTemp.yp = estacionTemp.northing - estacionTempAnterior.northing;
                estacionTemp.dist = Math.sqrt(Math.pow(estacionTemp.xp,2)+Math.pow(estacionTemp.yp,2));
            }
            estaciones.setElementAt(estacionTemp, i);
        }
    }


    public void calculoCoorParciales(){
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
            perimetro = perimetro + estacionTemp.dist;
            estacionTemp.xp = estacionTemp.dist * estacionTemp.seno;
            estacionTemp.yp = estacionTemp.dist * estacionTemp.coseno;

            if (estacionTemp.radiaciones.size()>0){
                for (int j = 0; j< estacionTemp.radiaciones.size();j++){
                    Estacion radiacion = (Estacion) estacionTemp.radiaciones.elementAt(j);
                    radiacion.xp = radiacion.dist * radiacion.seno;
                    radiacion.yp = radiacion.dist * radiacion.coseno;
                    estacionTemp.radiaciones.setElementAt(radiacion,j);

                }
            }

            estaciones.setElementAt(estacionTemp, i);
        }
    }


    public void calculoNyS(){
        for (int i = 0; i < estaciones.size(); i++) {
            if (i > 0){
                Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
                if (estacionTemp.yp >= 0) {
                    estacionTemp.Norte = estacionTemp.yp;
                    estacionTemp.Sur = 0;
                }else if (estacionTemp.yp < 0) {
                    estacionTemp.Norte = 0;
                    estacionTemp.Sur = Math.abs(estacionTemp.yp);
                }
                estaciones.setElementAt(estacionTemp,i);

            }
        }
    }

    public void calculoEyO(){
        for (int i = 0; i < estaciones.size(); i++) {
            if (i > 0){
                Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
                if (estacionTemp.xp >= 0) {
                    estacionTemp.Este = estacionTemp.xp;
                    estacionTemp.Oeste = 0;
                }else if (estacionTemp.xp < 0) {
                    estacionTemp.Este = 0;
                    estacionTemp.Oeste = Math.abs(estacionTemp.xp);
                }
                estaciones.setElementAt(estacionTemp,i);
            }

        }
    }

    public void calculoErrorCierre() {
        sumaNortes = 0;
        sumaSures = 0;
        sumaEstes = 0;
        sumaOestes = 0;
        sumaDist = 0;
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
            sumaNortes = sumaNortes + estacionTemp.Norte;
            sumaSures = sumaSures + estacionTemp.Sur;
            sumaEstes = sumaEstes + estacionTemp.Este;
            sumaOestes = sumaOestes + estacionTemp.Oeste;
            sumaDist = sumaDist + estacionTemp.dist;
        }
        deltaY = Math.abs(sumaNortes - sumaSures);
        deltaX = Math.abs(sumaEstes - sumaOestes);
        double numTemp = (deltaY * deltaY)+(deltaX * deltaX);
        errC = Math.sqrt(numTemp);
        errCierreUn = errC / sumaDist;
        if ((errCierreUn <= 0.003)){
            errorAceptable = true;
            Cy = deltaY / (sumaNortes + sumaSures);
            Cx = deltaX / (sumaEstes + sumaOestes);
            errorCumple = "Yes";

        }else{
            errorAceptable = false;
            Cy = deltaY / (sumaNortes + sumaSures);
            Cx = deltaX / (sumaEstes + sumaOestes);
            errorCumple = "No";

        }
    }

    public void calculoCorrecciones(){

        if (sumaNortes > sumaSures){
            for (int i = 0; i < estaciones.size(); i++) {
                Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
                corrN.addElement(estacionTemp.Norte * Cy * (-1));
                corrS.addElement(estacionTemp.Sur * Cy);
            }
        } else if (sumaNortes == sumaSures){
            for (int i = 0; i < estaciones.size(); i++) {
                corrN.addElement(0.0);
                corrS.addElement(0.0);
            }
        } else{
            for (int i = 0; i < estaciones.size(); i++) {
                Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
                corrN.addElement(estacionTemp.Norte * Cy );
                corrS.addElement(estacionTemp.Sur * Cy * (-1));
            }
        }
        if (sumaEstes > sumaOestes){
            for (int i = 0; i < estaciones.size(); i++) {
                Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
                corrE.addElement(estacionTemp.Este * Cx * (-1));
                corrO.addElement(estacionTemp.Oeste * Cx);
            }
        }  else if (sumaEstes == sumaOestes){
            for (int i = 0; i < estaciones.size(); i++) {
                corrE.addElement(0.0 );
                corrO.addElement(0.0);
            }
        } else{
            for (int i = 0; i < estaciones.size(); i++) {
                Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
                corrE.addElement(estacionTemp.Este * Cx );
                corrO.addElement(estacionTemp.Oeste * Cx * (-1));
            }
        }
    }

    public void calculoCoorCompensadas() {
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
            estacionTemp.nc = estacionTemp.Norte + (double)corrN.elementAt(i);
            estacionTemp.sc = estacionTemp.Sur + (double)corrS.elementAt(i);
            estacionTemp.ec = estacionTemp.Este + (double)corrE.elementAt(i);
            estacionTemp.oc = estacionTemp.Oeste + (double)corrO.elementAt(i);
            estaciones.setElementAt(estacionTemp,i);
        }
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
            estacionTemp.ypc = estacionTemp.nc + (estacionTemp.sc * (-1));
            estacionTemp.xpc = estacionTemp.ec + (estacionTemp.oc * (-1));
            estaciones.setElementAt(estacionTemp,i);
        }
    }

    public void calculoCoorTotales(){
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
            if (i == 0){
                yto = estacionTemp.yt;
                xto = estacionTemp.xt;
                zto = 0;
                estacionTemp.zt = 0;
                estaciones.setElementAt(estacionTemp,i);
            }
            if (i >= 1) {
                Estacion estacionTempAnterior = (Estacion) estaciones.elementAt(i-1);
                Estacion estInicial = (Estacion) estaciones.elementAt(0);
                if (tipoMedicion.equals("poligono")){
                    estacionTemp.xt =  estacionTempAnterior.xt + estacionTemp.xpc;
                    estacionTemp.yt =  estacionTempAnterior.yt + estacionTemp.ypc;
                } else {
                    estacionTemp.xt =  estacionTempAnterior.xt + estacionTemp.xp;
                    estacionTemp.yt =  estacionTempAnterior.yt + estacionTemp.yp;
                }

                estacionTemp.zt = estacionTemp.alt - estInicial.alt;
                estacionTemp.yi = 0 - estacionTemp.yt;
                estaciones.setElementAt(estacionTemp,i);
            }
        }
    }



    public void calculoCoorTotalesDibujar(double ycen, double xcen){
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
            if (i == 0){
                yto = ycen;
                estacionTemp.yt = ycen;
                xto = xcen;
                estacionTemp.xt = xcen;
                if (estacionTemp.yt < ytmin){
                    ytmin = estacionTemp.yt;
                }else if (estacionTemp.yt >= ytmax){
                    ytmax = estacionTemp.yt;
                }
                if (estacionTemp.xt < xtmin){
                    xtmin = estacionTemp.xt;
                }else if (estacionTemp.xt >= xtmax){
                    xtmax = estacionTemp.xt;
                }

                if (estacionTemp.radiaciones.size()>0){
                    for (int j = 0; j< estacionTemp.radiaciones.size();j++){
                        Estacion radiacion = (Estacion) estacionTemp.radiaciones.elementAt(j);
                        radiacion.xt = estacionTemp.xt +  radiacion.xp;
                        radiacion.yt = estacionTemp.yt +  radiacion.yp;
                        estacionTemp.radiaciones.setElementAt(radiacion,j);
                    }
                }

                estaciones.setElementAt(estacionTemp,i);

            }
            if (i >= 1) {

                Estacion estacionTempAnterior =estaciones.elementAt(i-1);
                estacionTemp.xt =  estacionTempAnterior.xt + estacionTemp.xp;
                estacionTemp.yt = estacionTempAnterior.yt + estacionTemp.yp;
                estacionTemp.yi = ycen - estacionTemp.yt;
                if (estacionTemp.yt < ytmin){
                    ytmin = estacionTemp.yt;
                }else if (estacionTemp.yt >= ytmax){
                    ytmax = estacionTemp.yt;
                }
                if (estacionTemp.xt < xtmin){
                    xtmin = estacionTemp.xt;
                }else if (estacionTemp.xt >= xtmax){
                    xtmax = estacionTemp.xt;
                }

                if (estacionTemp.radiaciones.size()>0){
                    for (int j = 0; j< estacionTemp.radiaciones.size();j++){
                        Estacion radiacion = (Estacion) estacionTemp.radiaciones.elementAt(j);
                        radiacion.xt = estacionTemp.xt +  radiacion.xp;
                        radiacion.yt = estacionTemp.yt +  radiacion.yp;
                        estacionTemp.radiaciones.setElementAt(radiacion,j);
                    }
                }

                estaciones.setElementAt(estacionTemp,i);
            }
        }
    }


    public void calculoAreaPoligono(){
        sumaYX = 0;
        sumaXY = 0;
        for (int i = 0; i < (estaciones.size()-1); i++) {
            Estacion estacionTemp = (Estacion) estaciones.elementAt(i);
            Estacion estacionTempSig = (Estacion) estaciones.elementAt(i + 1);
            estacionTemp.YX = estacionTemp.yt * estacionTempSig.xt;
            estacionTemp.XY = estacionTemp.xt * estacionTempSig.yt;
            sumaYX = sumaYX + estacionTemp.YX;
            sumaXY = sumaXY + estacionTemp.XY;
            estaciones.setElementAt(estacionTemp,i);
        }
        area = Math.abs(sumaYX - sumaXY)/2;
    }


    public void calcularPerimetro (){
        perimetro = 0;
        for (int i = 1; i < estaciones.size();i++){
            double deltaX1 = estaciones.elementAt(i).xt - estaciones.elementAt(i-1).xt;
            double deltaY1 = estaciones.elementAt(i).yt - estaciones.elementAt(i-1).yt;
            double distancia = Math.sqrt(Math.sqrt((Math.pow(deltaX1,2)) + (Math.pow(deltaY1,2))));
            perimetro += distancia;
        }
        double deltaX1 = estaciones.elementAt(0).xt - estaciones.elementAt(estaciones.size()-1).xt;
        double deltaY1 = estaciones.elementAt(0).yt - estaciones.elementAt(estaciones.size()-1).yt;
        double distancia = Math.sqrt(Math.sqrt((Math.pow(deltaX1,2)) + (Math.pow(deltaY1,2))));
        if (tipoMedicion.equals("poligono")){
            perimetro += distancia;
        }

    }

    public void limpiarPoligono() {
        this.area = 0;                         //area del poligono en metros cuadrados
        this.perimetro = 0;                //perimetro del poligono, en metros
        this.sumaNortes = 0;
        this.sumaSures = 0;
        this.sumaEstes = 0;
        this.sumaOestes = 0;
        this.sumaDist = 0;
        this.sumaYX = 0;
        this.sumaXY = 0;
        this.deltaX = 0;                   //Error de cierre en el eje X
        this.deltaY = 0;                   //Error de cierre en el eje Y
        this.errC = 0;                     // error de cierre
        this.errCierreUn = 0;
        this.errA = 0;                     //Error de cierre admisible
        this.Cy = 0;
        this.Cx = 0;
        this.estaciones.removeAllElements();
        this.corrN.removeAllElements();
        this.corrS.removeAllElements();
        this.corrE.removeAllElements();
        this.corrO.removeAllElements();
        this.contEs = 0;                     //contador de estaciones ó puntos

        this.estFinal = false;
        this.errorAceptable = false;
        this.nombreArchivoGuardado = "";
        this.rutaArchivoGuardado = "";
        this.tipoTerreno = "";
        this.rutaArchivoCSV= "";
        this.nombreArchivoCSV = "";
        this.opMenuPrincipal = "0";
        this.yto = 0.0;
        this.xto = 0.0;
        this.rutaArchivoDXF= "";
        this.nombreArchivoDXF = "";
        this.xto = 0.0;
        this.xtmin = 0;
        this.xtmax = 0;
        this.ytmin = 0;
        this.ytmax = 0;
        this.rutaCarpetaActual = "";
        this.tipoMedicion = "poligono";


    }

    public Poligono pol(){
        return  this;
    }

    public void limpiarEstIni(){
        this.estaciones.elementAt(0).dist = 0;
        this.estaciones.elementAt(0).xp = 0;
        this.estaciones.elementAt(0).yp = 0;
        this.estaciones.elementAt(0).xt = 0;
        this.estaciones.elementAt(0).yt = 0;
    }

    public void calculoCoorParciales2(){
        perimetro = 0;

        for (int i = 0; i < estacionesPolFinal.size(); i++) {
            if (i == 0){
                Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
                estacionTemp.xp = 0;
                estacionTemp.yp = 0;
                estacionesPolFinal.setElementAt(estacionTemp, i);
            } else if (i >0){
                Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
                perimetro = perimetro + estacionTemp.dist;
                estacionTemp.xp = estacionTemp.dist * estacionTemp.seno;
                estacionTemp.yp = estacionTemp.dist * estacionTemp.coseno;
                estacionesPolFinal.setElementAt(estacionTemp, i);
            }

        }
    }


    public void calculoNyS2(){
        for (int i = 0; i < estacionesPolFinal.size(); i++) {
            if (i > 0){
                Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
                if (estacionTemp.yp >= 0) {
                    estacionTemp.Norte = estacionTemp.yp;
                    estacionTemp.Sur = 0;
                }else if (estacionTemp.yp < 0) {
                    estacionTemp.Norte = 0;
                    estacionTemp.Sur = Math.abs(estacionTemp.yp);
                }
                estacionesPolFinal.setElementAt(estacionTemp,i);

            }
        }
    }

    public void calculoEyO2(){
        for (int i = 0; i < estacionesPolFinal.size(); i++) {
            if (i > 0){
                Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
                if (estacionTemp.xp >= 0) {
                    estacionTemp.Este = estacionTemp.xp;
                    estacionTemp.Oeste = 0;
                }else if (estacionTemp.xp < 0) {
                    estacionTemp.Este = 0;
                    estacionTemp.Oeste = Math.abs(estacionTemp.xp);
                }
                estacionesPolFinal.setElementAt(estacionTemp,i);
            }

        }
    }

    public void calculoErrorCierre2() {
        sumaNortes = 0;
        sumaSures = 0;
        sumaEstes = 0;
        sumaOestes = 0;
        sumaDist = 0;
        for (int i = 0; i < estacionesPolFinal.size(); i++) {
            Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
            sumaNortes = sumaNortes + estacionTemp.Norte;
            sumaSures = sumaSures + estacionTemp.Sur;
            sumaEstes = sumaEstes + estacionTemp.Este;
            sumaOestes = sumaOestes + estacionTemp.Oeste;
            sumaDist = sumaDist + estacionTemp.dist;
        }
        deltaY = Math.abs(sumaNortes - sumaSures);
        deltaX = Math.abs(sumaEstes - sumaOestes);
        double numTemp = (deltaY * deltaY)+(deltaX * deltaX);
        errC = Math.sqrt(numTemp);
        errCierreUn = errC / sumaDist;
        if ((errCierreUn <= 0.003)){
            errorAceptable = true;
            Cy = deltaY / (sumaNortes + sumaSures);
            Cx = deltaX / (sumaEstes + sumaOestes);
            errorCumple = "Yes";

        }else{
            errorAceptable = false;
            Cy = deltaY / (sumaNortes + sumaSures);
            Cx = deltaX / (sumaEstes + sumaOestes);
            errorCumple = "No";

        }
    }

    public void calculoCorrecciones2(){

        if (sumaNortes > sumaSures){
            for (int i = 0; i < estacionesPolFinal.size(); i++) {
                Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
                corrN.addElement(estacionTemp.Norte * Cy * (-1));
                corrS.addElement(estacionTemp.Sur * Cy);
            }
        } else if (sumaNortes == sumaSures){
            for (int i = 0; i < estacionesPolFinal.size(); i++) {
                corrN.addElement(0.0);
                corrS.addElement(0.0);
            }
        } else{
            for (int i = 0; i < estacionesPolFinal.size(); i++) {
                Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
                corrN.addElement(estacionTemp.Norte * Cy );
                corrS.addElement(estacionTemp.Sur * Cy * (-1));
            }
        }
        if (sumaEstes > sumaOestes){
            for (int i = 0; i < estacionesPolFinal.size(); i++) {
                Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
                corrE.addElement(estacionTemp.Este * Cx * (-1));
                corrO.addElement(estacionTemp.Oeste * Cx);
            }
        }  else if (sumaEstes == sumaOestes){
            for (int i = 0; i < estacionesPolFinal.size(); i++) {
                corrE.addElement(0.0 );
                corrO.addElement(0.0);
            }
        } else{
            for (int i = 0; i < estacionesPolFinal.size(); i++) {
                Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
                corrE.addElement(estacionTemp.Este * Cx );
                corrO.addElement(estacionTemp.Oeste * Cx * (-1));
            }
        }
    }

    public void calculoCoorCompensadas2() {
        for (int i = 0; i < estacionesPolFinal.size(); i++) {
            Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
            estacionTemp.nc = estacionTemp.Norte + (double)corrN.elementAt(i);
            estacionTemp.sc = estacionTemp.Sur + (double)corrS.elementAt(i);
            estacionTemp.ec = estacionTemp.Este + (double)corrE.elementAt(i);
            estacionTemp.oc = estacionTemp.Oeste + (double)corrO.elementAt(i);
            estacionesPolFinal.setElementAt(estacionTemp,i);
        }
        for (int i = 0; i < estacionesPolFinal.size(); i++) {
            Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
            estacionTemp.ypc = estacionTemp.nc + (estacionTemp.sc * (-1));
            estacionTemp.xpc = estacionTemp.ec + (estacionTemp.oc * (-1));
            estacionesPolFinal.setElementAt(estacionTemp,i);
        }
    }

    public void calculoCoorTotales2(){
        for (int i = 0; i < estacionesPolFinal.size(); i++) {
            Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
            if (i == 0){
                yto = estacionTemp.yt;
                xto = estacionTemp.xt;
                zto = 0;
                estacionTemp.zt = 0;
                estacionesPolFinal.setElementAt(estacionTemp,i);
            }
            if (i >= 1) {
                Estacion estacionTempAnterior = (Estacion) estacionesPolFinal.elementAt(i-1);
                Estacion estInicial = (Estacion) estacionesPolFinal.elementAt(0);
                estacionTemp.xt =  estacionTempAnterior.xt + estacionTemp.xpc;
                estacionTemp.yt =  estacionTempAnterior.yt + estacionTemp.ypc;
                estacionTemp.zt = estacionTemp.alt - estInicial.alt;
                estacionTemp.yi = 0 - estacionTemp.yt;
                estacionesPolFinal.setElementAt(estacionTemp,i);
            }
        }
    }




    public void calculoAreaPoligono2(){
        sumaYX = 0;
        sumaXY = 0;
        for (int i = 0; i < (estacionesPolFinal.size()-1); i++) {
            Estacion estacionTemp = (Estacion) estacionesPolFinal.elementAt(i);
            Estacion estacionTempSig = (Estacion) estacionesPolFinal.elementAt(i + 1);
            estacionTemp.YX = estacionTemp.yt * estacionTempSig.xt;
            estacionTemp.XY = estacionTemp.xt * estacionTempSig.yt;
            sumaYX = sumaYX + estacionTemp.YX;
            sumaXY = sumaXY + estacionTemp.XY;
            estacionesPolFinal.setElementAt(estacionTemp,i);
        }
        this.area = Math.abs(sumaYX - sumaXY)/2;
    }


    public void calcularPerimetro2(){
        perimetro = 0;
        for (int i = 1; i < estacionesPolFinal.size();i++){
            double deltaX1 = estacionesPolFinal.elementAt(i).xt - estacionesPolFinal.elementAt(i-1).xt;
            double deltaY1 = estacionesPolFinal.elementAt(i).yt - estacionesPolFinal.elementAt(i-1).yt;
            double distancia = Math.sqrt(Math.sqrt((Math.pow(deltaX1,2)) + (Math.pow(deltaY1,2))));
            perimetro += distancia;
        }
        /*double deltaX1 = estacionesPolFinal.elementAt(0).xt - estacionesPolFinal.elementAt(estacionesPolFinal.size()-1).xt;
        double deltaY1 = estacionesPolFinal.elementAt(0).yt - estacionesPolFinal.elementAt(estacionesPolFinal.size()-1).yt;
        double distancia = Math.sqrt(Math.sqrt((Math.pow(deltaX1,2)) + (Math.pow(deltaY1,2))));
        perimetro += distancia;*/

    }

    public Estacion primeraEstacionPolFinal(){
        Estacion e0 = new Estacion();
        boolean primeraEstacionEncontrada = false;
        if (estaciones.size()>0){
            for (int i = 0; i < estaciones.size(); i++){
                Estacion eTemp = estaciones.elementAt(i);
                if (eTemp.partePoligonoFinal){
                    if (primeraEstacionEncontrada){

                    }else {
                        e0 = eTemp;
                        primeraEstacionEncontrada = true;
                    }
                } else {
                    if (eTemp.radiaciones.size()>0){
                        for (int j = 0; j < eTemp.radiaciones.size(); j++){
                            Estacion rTemp = eTemp.radiaciones.elementAt(j);
                            if (rTemp.partePoligonoFinal){
                                if (primeraEstacionEncontrada){

                                }else {
                                    e0 = rTemp;
                                    primeraEstacionEncontrada = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return  e0;
    }


    public void calculosParaPoligonoFinal(){
        estacionesPolFinal.removeAllElements();
        try {
            if (estaciones.size()>0){
                for (int i = 0; i < estaciones.size(); i++){
                    Estacion e1 = estaciones.elementAt(i);

                    if (e1.partePoligonoFinal){
                        Estacion eTemp2 = (Estacion) e1.clone();
                        estacionesPolFinal.addElement(eTemp2);
                    }

                    if (e1.radiaciones.size()>0){
                        for (int j = 0; j < e1.radiaciones.size(); j++){
                            Estacion r1 = e1.radiaciones.elementAt(j);
                            if (r1.partePoligonoFinal){
                                Estacion eTemp2 = (Estacion) r1.clone();
                                estacionesPolFinal.addElement(eTemp2);
                            }
                        }
                    }
                }
            }
        } catch (CloneNotSupportedException e){e.printStackTrace();
        }
    }


    public void calculosParaPoligonoFinal2(){
        if (estacionesPolFinal.size()>0){
            double dX;
            double dY;
            double angulo;

            try{
                Estacion eTemp = estacionesPolFinal.elementAt(0);
                Estacion eTemp2 = (Estacion) eTemp.clone();
                estacionesPolFinal.addElement(eTemp2);
            } catch (CloneNotSupportedException e){
                e.printStackTrace();
            }

            for (int i = 0; i <estacionesPolFinal.size(); i++){
                if (i == 0){
                    dX = 0;
                    dY = 0;
                    angulo = 0;
                    estacionesPolFinal.elementAt(i).dist = 0;
                    estacionesPolFinal.elementAt(i).valorDecimalGrados = 0;
                    estacionesPolFinal.elementAt(i).grado = 0;
                    estacionesPolFinal.elementAt(i).seno =  0;
                    estacionesPolFinal.elementAt(i).coseno = 0;
                    estacionesPolFinal.elementAt(i).idAnterior = estacionesPolFinal.elementAt(i).idEst;
                    estacionesPolFinal.elementAt(i).idEst = "E-0";

                } else if (i >= 1) {
                    dX = (estacionesPolFinal.elementAt(i).xt - estacionesPolFinal.elementAt(i-1).xt);
                    dY = (estacionesPolFinal.elementAt(i).yt - estacionesPolFinal.elementAt(i-1).yt);
                    double diferencial = dX/dY;
                    double tangenteInversa = Math.atan(diferencial);
                    double anguloVariable = Math.toDegrees(tangenteInversa);
                    angulo = 0;

                    if (dX > 0){
                        if (dY>0){
                            angulo = anguloVariable;
                        } else if (dY==0){
                            angulo = 90;
                        } else if (dY <0){
                            angulo = 180 + anguloVariable;
                        }
                    }else if (dX<0){
                        if (dY>0){
                            angulo = 360 + anguloVariable;
                        } else if (dY==0){
                            angulo = 270;
                        } else if (dY <0){
                            angulo = 180 + anguloVariable;
                        }
                    }else if (dX == 0){
                        if (dY>0){
                            angulo = 0;
                        } else if (dY==0){
                            angulo = 0;
                        } else if (dY <0){
                            angulo = 180 ;
                        }
                    }

                    if (angulo == 360 ){
                        angulo = 0;
                    }

                    estacionesPolFinal.elementAt(i).dist = Math.sqrt(Math.pow(dX,2) + Math.pow(dY,2));
                    estacionesPolFinal.elementAt(i).valorDecimalGrados = angulo;
                    int grados = (int) angulo;
                    //estacionesPolFinal.elementAt(i).grado = grados;
                    estacionesPolFinal.elementAt(i).grado = grados;
                    double minutos = ((angulo - grados)*60);
                    int min = (int) minutos;
                    estacionesPolFinal.elementAt(i).minuto = min;
                    double seg = (minutos - min) * 60;
                    estacionesPolFinal.elementAt(i).segundo = seg;
                    if (estacionesPolFinal.elementAt(i).segundo == 60){
                        estacionesPolFinal.elementAt(i).segundo = 0;
                        estacionesPolFinal.elementAt(i).minuto += 1;
                    }

                    if (estacionesPolFinal.elementAt(i).minuto == 60){
                        estacionesPolFinal.elementAt(i).minuto = 0;
                        estacionesPolFinal.elementAt(i).grado += 1;
                        if (estacionesPolFinal.elementAt(i).grado>360){
                            estacionesPolFinal.elementAt(i).grado = estacionesPolFinal.elementAt(i).grado - 360;
                        }
                    }

                    Double anguloRadianes = Math.toRadians(angulo);

                    estacionesPolFinal.elementAt(i).seno =  Math.sin(anguloRadianes);
                    estacionesPolFinal.elementAt(i).coseno = Math.cos(anguloRadianes);
                    estacionesPolFinal.elementAt(i).idAnterior = estacionesPolFinal.elementAt(i).idEst;

                    if (i == (estacionesPolFinal.size()-1)){
                        estacionesPolFinal.elementAt(i).idEst = "E-0";
                    }else
                        estacionesPolFinal.elementAt(i).idEst = "E-" + String.valueOf(i);
                }
            }
        }
    }

    public static double fijarNumero(double numero, int digitos) {
        double resultado;
        resultado = numero * Math.pow(10, digitos);
        resultado = Math.round(resultado);
        resultado = resultado/Math.pow(10, digitos);
        return resultado;
    }

    public static double [] calcularGPSdeUTM (String utm1){
        double[] latLon;
        CoordinateConversion objetoCoversor = new CoordinateConversion();
        latLon = objetoCoversor.utm2LatLon(utm1);
        return latLon;
    }



}