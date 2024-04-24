package com.example.prueba;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
    private CardView cardView;
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

        // Crear instancia del administrador de la base de datos
        dbManager = new DatabaseManager(this);
        dbManager.open();

        // Configurar adaptador para el ListView
        adapter = new CustomAdapter(this, chipsList);
        listViewDatos.setAdapter(adapter);

        // Asignar acción al botón "Agregar"
        fabAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Crear un intent para abrir la actividad de edición
                Intent intent = new Intent(MainActivity.this, AddActivity.class);

                // Iniciar la actividad de edición
                startActivity(intent);
            }
        });

        // Obtener todos los datos y mostrarlos en la interfaz de usuario
        mostrarDatos();
    }

    private void mostrarDatos() {
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

    // Adaptador personalizado para el ListView
    private class CustomAdapter extends ArrayAdapter<Chip> {
        private Context mContext;
        private ArrayList<Chip> chipsList;

        // Constructor
        public CustomAdapter(Context context, ArrayList<Chip> chips) {
            super(context, 0, chips);
            mContext = context;
            chipsList = chips;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.cardView = convertView.findViewById(R.id.cardView);
                viewHolder.textViewImei = convertView.findViewById(R.id.textViewImei);
                viewHolder.textViewPhoneNumber = convertView.findViewById(R.id.textViewPhoneNumber);
                viewHolder.textViewBanDate = convertView.findViewById(R.id.textViewBanDate);
                viewHolder.textViewCompany = convertView.findViewById(R.id.textViewCompany);
                viewHolder.textViewChipDetails = convertView.findViewById(R.id.textViewChipDetails);
                viewHolder.button = convertView.findViewById(R.id.buttonListItem);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Chip chip = chipsList.get(position);

            viewHolder.textViewImei.setText(chip.getImei());
            viewHolder.textViewPhoneNumber.setText(chip.getNumero());
            viewHolder.textViewBanDate.setText(chip.getBaneo());
            viewHolder.textViewCompany.setText(chip.getCompania());
            viewHolder.textViewChipDetails.setText(chip.getDetalle());

            // Guardar la posición final para acceder dentro del OnClickListener
            final int finalPosition = position;

            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Inflar el layout del diálogo personalizado
                    View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);

                    // Crear el diálogo
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();

                    // Obtener referencias a los elementos del layout del diálogo
                    TextView textMessage = dialogView.findViewById(R.id.textMessage);
                    Button buttonYes = dialogView.findViewById(R.id.buttonYes);
                    Button buttonNo = dialogView.findViewById(R.id.buttonNo);

                    // Establecer el mensaje del diálogo
                    textMessage.setText("¿Está seguro de actualizar la fecha de baneo?");

                    // Configurar el clic del botón "Sí"
                    buttonYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Obtener el dato seleccionado
                            Chip chipSeleccionado = adapter.getItem(position);

                            // Actualizar la fecha de baneo a la fecha actual en la base de datos
                            dbManager.updateBaneoFecha(chipSeleccionado);

                            // Notificar al adaptador que los datos han cambiado
                            notifyDataSetChanged();

                            mostrarDatos();
                            // Mostrar un mensaje de éxito
                            Toast.makeText(mContext, "Fecha de baneo actualizada", Toast.LENGTH_SHORT).show();

                            dialog.dismiss(); // Cerrar el diálogo después de hacer clic en "Sí"
                        }
                    });

                    // Configurar el clic del botón "No"
                    buttonNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); // Cerrar el diálogo después de hacer clic en "No"
                        }
                    });

                    // Mostrar el diálogo
                    dialog.show();
                }
            });

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener el dato seleccionado
                    Chip chipSeleccionado = adapter.getItem(position);

                    // Crear un intent para abrir la actividad de edición
                    Intent intent = new Intent(MainActivity.this, EditActivity.class);

                    // Pasar el dato seleccionado a la actividad de edición
                    intent.putExtra("chipSeleccionado", chipSeleccionado);

                    // Iniciar la actividad de edición
                    startActivity(intent);
                }
            });

            return convertView;
        }

        // Clase ViewHolder para almacenar las vistas de cada elemento de la lista
        private class ViewHolder {
            TextView textViewImei;
            TextView textViewPhoneNumber;
            TextView textViewBanDate;
            TextView textViewCompany;
            TextView textViewChipDetails;
            CardView cardView;
            Button button;
        }
    }
}
