package com.example.prueba.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prueba.model.Chip;

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

    public void insertData(Chip chip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IMEI, chip.getImei());
        values.put(DatabaseHelper.COLUMN_NUMERO, ""+chip.getNumero());
        values.put(DatabaseHelper.COLUMN_BANEO, chip.getBaneo());
        values.put(DatabaseHelper.COLUMN_COMPANIA, chip.getCompania());
        values.put(DatabaseHelper.COLUMN_DETALLES, chip.getDetalle());
        database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    public Cursor getAllData() {
        // Especifica la columna por la que deseas ordenar (en este caso, la columna de fecha de banneo)
        String orderBy = DatabaseHelper.COLUMN_BANEO + " ASC"; // Orden ascendente (desde el más antiguo hasta el más reciente)

        // Realiza la consulta a la base de datos, incluyendo la cláusula ORDER BY
        return database.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, orderBy);
    }

    public void updateData(Chip chip) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NUMERO, chip.getNumero());
        values.put(DatabaseHelper.COLUMN_BANEO, chip.getBaneo());
        values.put(DatabaseHelper.COLUMN_COMPANIA, chip.getCompania());
        values.put(DatabaseHelper.COLUMN_DETALLES, chip.getDetalle());
        database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{chip.getImei()});
    }

    public void updateBaneoFecha(Chip chip) {
        // Obtén la fecha actual en el formato deseado (puedes usar SimpleDateFormat)
        String fechaActual = obtenerFechaActual(); // Implementa este método según tu formato de fecha

        // Crea un objeto ContentValues para almacenar el nuevo valor de la fecha de baneo
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BANEO, fechaActual);

        // Actualiza el valor de la fecha de baneo en la base de datos para el dato seleccionado
        database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{chip.getImei()});
    }
    // Método para obtener la fecha actual en el formato deseado
    private String obtenerFechaActual() {
        // Obtén la fecha actual en el formato deseado, por ejemplo "dd/MM/yyyy"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        // Mostrar un mensaje de éxito
        // Toast.makeText(mContext, "Fecha de baneo actual"+dateFormat.format(date), Toast.LENGTH_SHORT).show();
        return dateFormat.format(date);
    }

    public void deleteData(Chip chip) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_IMEI + "=?", new String[]{chip.getImei()});
    }
}
