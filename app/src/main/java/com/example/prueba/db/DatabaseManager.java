package com.example.prueba.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

<<<<<<< HEAD
import com.example.prueba.model.Chip;

=======
>>>>>>> 236ad19f65dbeffef11277f76b2d0eb67a0a26c1
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

<<<<<<< HEAD
    public void insertData(Chip chip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IMEI, chip.getImei());
        values.put(DatabaseHelper.COLUMN_NUMERO, ""+chip.getNumero());
        values.put(DatabaseHelper.COLUMN_BANEO, chip.getBaneo());
        values.put(DatabaseHelper.COLUMN_COMPANIA, chip.getCompania());
        values.put(DatabaseHelper.COLUMN_DETALLES, chip.getDetalle());
=======
    public void insertData(String imei, String numero, String baneo, String compania, String detalles) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IMEI, imei);
        values.put(DatabaseHelper.COLUMN_NUMERO, numero);
        values.put(DatabaseHelper.COLUMN_BANEO, baneo);
        values.put(DatabaseHelper.COLUMN_COMPANIA, compania);
        values.put(DatabaseHelper.COLUMN_DETALLES, detalles);
>>>>>>> 236ad19f65dbeffef11277f76b2d0eb67a0a26c1
        database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public Cursor getAllData() {
        // Especifica la columna por la que deseas ordenar (en este caso, la columna de fecha de banneo)
<<<<<<< HEAD
        String orderBy = DatabaseHelper.COLUMN_BANEO + " ASC"; // Orden ascendente (desde el más antiguo hasta el más reciente)
=======
        String orderBy = DatabaseHelper.COLUMN_BANEO + " DESC"; // Puedes cambiar a "DESC" si deseas ordenar en orden descendente
>>>>>>> 236ad19f65dbeffef11277f76b2d0eb67a0a26c1

        // Realiza la consulta a la base de datos, incluyendo la cláusula ORDER BY
        return database.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, orderBy);
    }

<<<<<<< HEAD

    public void updateData(Chip chip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NUMERO, chip.getNumero());
        values.put(DatabaseHelper.COLUMN_BANEO, chip.getBaneo());
        values.put(DatabaseHelper.COLUMN_COMPANIA, chip.getCompania());
        values.put(DatabaseHelper.COLUMN_DETALLES, chip.getDetalle());
        database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{chip.getImei()});
    }

    public void updateBaneoFecha(Chip chip) {
=======
    public void updateData(String imei, String numero, String baneo, String compania, String detalles) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NUMERO, numero);
        values.put(DatabaseHelper.COLUMN_BANEO, baneo);
        values.put(DatabaseHelper.COLUMN_COMPANIA, compania);
        values.put(DatabaseHelper.COLUMN_DETALLES, detalles);
        database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{imei});
    }

    public void updateBaneoFecha(String datoSeleccionado) {
>>>>>>> 236ad19f65dbeffef11277f76b2d0eb67a0a26c1
        // Obtén la fecha actual en el formato deseado (puedes usar SimpleDateFormat)
        String fechaActual = obtenerFechaActual(); // Implementa este método según tu formato de fecha

        // Crea un objeto ContentValues para almacenar el nuevo valor de la fecha de baneo
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BANEO, fechaActual);

        // Actualiza el valor de la fecha de baneo en la base de datos para el dato seleccionado
<<<<<<< HEAD
        database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{chip.getImei()});
=======
        database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{obtenerImeiFromDato(datoSeleccionado)});
>>>>>>> 236ad19f65dbeffef11277f76b2d0eb67a0a26c1
    }
    // Método para obtener la fecha actual en el formato deseado
    private String obtenerFechaActual() {
        // Obtén la fecha actual en el formato deseado, por ejemplo "dd/MM/yyyy"
<<<<<<< HEAD
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
=======
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
>>>>>>> 236ad19f65dbeffef11277f76b2d0eb67a0a26c1
        Date date = new Date();
        // Mostrar un mensaje de éxito
        // Toast.makeText(mContext, "Fecha de baneo actual"+dateFormat.format(date), Toast.LENGTH_SHORT).show();
        return dateFormat.format(date);
    }

<<<<<<< HEAD
    public void deleteData(Chip chip) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{chip.getImei()});
=======
    // Método para obtener el IMEI a partir del dato seleccionado
    private String obtenerImeiFromDato(String datoSeleccionado) {
        // Dividir la cadena por ": " y tomar la segunda parte como el IMEI
        String[] partes = datoSeleccionado.split("\n");
        if (partes.length > 1) {
            return partes[0].substring(6).trim();
        } else {
            return ""; // O devuelve una cadena vacía si no se puede extraer el IMEI
        }
    }


    public void deleteData(String imei) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{imei});
>>>>>>> 236ad19f65dbeffef11277f76b2d0eb67a0a26c1
    }
}
