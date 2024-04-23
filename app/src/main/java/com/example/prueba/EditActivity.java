package com.example.prueba;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prueba.db.DatabaseManager;
import com.example.prueba.model.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    private EditText editTextImei, editTextNumero, editTextCompania, editTextDetalles;
    private Button btnGuardar, btnEliminar;

    private TextInputEditText editTextBaneo;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Asignar vistas
        editTextImei = findViewById(R.id.editTextImei);
        editTextNumero = findViewById(R.id.editTextNumero);
        editTextBaneo = findViewById(R.id.editTextBaneo);
        editTextCompania = findViewById(R.id.editTextCompania);
        editTextDetalles = findViewById(R.id.editTextDetalles);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Crear instancia del administrador de la base de datos
        dbManager = new DatabaseManager(this);
        dbManager.open();

        // Obtener el dato seleccionado de la actividad anterior
        Intent intent = getIntent();
        // Recuperar el objeto Chip del Intent
        Chip chipSeleccionado = (Chip) getIntent().getSerializableExtra("chipSeleccionado");

        // Mostrar el dato seleccionado en los campos de edición
        if (chipSeleccionado != null) {
            editTextImei.setText(chipSeleccionado.getImei()); // IMEI: xxxx
            editTextNumero.setText(chipSeleccionado.getNumero()); // Número: xxxx
            editTextBaneo.setText(chipSeleccionado.getBaneo()); // Baneo: xxxx
            editTextCompania.setText(chipSeleccionado.getCompania()); // Compañía: xxxx
            editTextDetalles.setText(chipSeleccionado.getDetalle()); // Detalles: xxxx
        }

        // Configurar el botón de guardar para actualizar el dato en la base de datos
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos valores de los campos de edición
                String nuevoImei = editTextImei.getText().toString();
                String nuevoNumero = editTextNumero.getText().toString();
                String nuevoBaneo = editTextBaneo.getText().toString();
                String nuevaCompania = editTextCompania.getText().toString();
                String nuevosDetalles = editTextDetalles.getText().toString();

                // Actualizar el dato en la base de datos
                Chip chip = new Chip(nuevoImei, nuevoNumero, nuevoBaneo, nuevaCompania, nuevosDetalles);
                dbManager.updateData(chip);

                // Mostrar un mensaje de éxito o cerrar la actividad
                Toast.makeText(EditActivity.this, "Dato actualizado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        // Configurar el botón de eliminar para eliminar el dato de la base de datos
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar el layout del diálogo personalizado
                View dialogView = LayoutInflater.from(EditActivity.this).inflate(R.layout.dialog_confirm, null);

                // Crear el diálogo
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
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
                        // Obtener el imei del item seleccionado
                        String imeiABorrrar = editTextImei.getText().toString();
                        // Eliminar el dato de la base de datos
                        Chip chip = new Chip();
                        chip.setImei(imeiABorrrar);
                        dbManager.deleteData(chip);

                        // Mostrar un mensaje de éxito o cerrar la actividad
                        Toast.makeText(EditActivity.this, "Dato eliminado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
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
        editTextBaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dia="";
                                if(dayOfMonth<10){
                                    dia="0"+dayOfMonth;
                                }else{
                                    dia=""+dayOfMonth;
                                }
                                String mes="";
                                if(month<10){
                                    mes="0"+(month+1);
                                }else{
                                    mes=""+(month+1);
                                }
                                // Actualizar el campo de texto con la fecha seleccionada
                                editTextBaneo.setText(year + "/" + mes + "/" + dia);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cerrar la base de datos al destruir la actividad
        dbManager.close();
    }
}
