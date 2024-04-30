package com.example.prueba.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prueba.R;
import com.example.prueba.controller.CustomAdapter;
import com.example.prueba.db.DatabaseHelper;
import com.example.prueba.db.DatabaseManager;
import com.example.prueba.model.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DatabaseManager dbManager;
    private FloatingActionButton fabAgregar;
    private ListView listViewDatos;
    private CustomAdapter adapter;
    private EditText editTextBusqueda;
    private ArrayList<Chip> chipsList = new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        // Mostrar los datos actualizados cada vez que la actividad se reanude
        mostrarDatos();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asignar vistas
        fabAgregar = findViewById(R.id.fabAgregar);
        listViewDatos = findViewById(R.id.listViewDatos);
        editTextBusqueda = findViewById(R.id.searchEditText);

        // Crear instancia del administrador de la base de datos
        dbManager = new DatabaseManager(this);
        dbManager.open();

        // Configurar adaptador para el ListView
        adapter = new CustomAdapter(MainActivity.this, chipsList, dbManager, MainActivity.this);
        listViewDatos.setAdapter(adapter);

        // Asignar acción al botón "Agregar"
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, 0);
            }
        });
        editTextBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                chipsList = new ArrayList<>();
                // Obtener todos los datos de la base de datos
                Cursor cursor = dbManager.searchData(""+s);

                // Procesar los datos y agregarlos a la lista de chips
                if (cursor.moveToFirst()) {
                    do {
                        @SuppressLint("Range") String imei = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMEI));
                        @SuppressLint("Range") String numero = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMERO));
                        @SuppressLint("Range") String baneo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BANEO));
                        @SuppressLint("Range") String compania = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COMPANIA));
                        @SuppressLint("Range") String detalles = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DETALLES));

                        // Construir el objeto Chip y agregarlo a la lista
                        Chip chip = new Chip(imei, numero, baneo, compania, detalles);
                        chipsList.add(chip);
                    } while (cursor.moveToNext());
                }

                // Notificar al adaptador que los datos han cambiado
                adapter.notifyDataSetChanged();

                // Cerrar el cursor
                cursor.close();
                adapter = new CustomAdapter(MainActivity.this, chipsList, dbManager, MainActivity.this);
                listViewDatos.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Obtener todos los datos y mostrarlos en la interfaz de usuario
        mostrarDatos();
    }
    public void switchFragment(Fragment fragment, int enterAnimation, int exitAnimation){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setCustomAnimations(enterAnimation,0,0,exitAnimation);
        //ft.replace(R.id.frame_container,fragment);
        ft.addToBackStack(null).commit();

    }

    public void mostrarDatos() {
        chipsList.clear();

        // Obtener todos los datos de la base de datos
        Cursor cursor = dbManager.getAllData();

        // Procesar los datos y agregarlos a la lista de chips
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String imei = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IMEI));
                @SuppressLint("Range") String numero = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NUMERO));
                @SuppressLint("Range") String baneo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BANEO));
                @SuppressLint("Range") String compania = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COMPANIA));
                @SuppressLint("Range") String detalles = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DETALLES));

                // Construir el objeto Chip y agregarlo a la lista
                Chip chip = new Chip(imei, numero, baneo, compania, detalles);
                chipsList.add(chip);
            } while (cursor.moveToNext());
        }

        // Notificar al adaptador que los datos han cambiado
        adapter.notifyDataSetChanged();

        // Cerrar el cursor
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerrar la base de datos al destruir la actividad
        dbManager.close();
    }
}
