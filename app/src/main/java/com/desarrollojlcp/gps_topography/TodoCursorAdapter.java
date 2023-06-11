package com.desarrollojlcp.gps_topography;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.desarrollojlcp.gps_topography.utilidades.Utilidades;

public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }


    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        //TextView tvFechaPol = (TextView) view.findViewById(R.id.tvBody);
        //TextView tvIdPol = (TextView) view.findViewById(R.id.tvPriority);

        // Extract properties from cursor
        int idPol = cursor.getInt(cursor.getColumnIndexOrThrow(Utilidades.PoligonoEntry.CAMPO_ID_POLIGONO));
        String fechaPol = cursor.getString(cursor.getColumnIndexOrThrow(Utilidades.PoligonoEntry.CAMPO_FECHA));

        // Populate fields with extracted properties
        //tvIdPol.setText(String.valueOf(idPol));
        //tvFechaPol.setText(fechaPol);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.actividad_guardar, parent, false);
    }
}
