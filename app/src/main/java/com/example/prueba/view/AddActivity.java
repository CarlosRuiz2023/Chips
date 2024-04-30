package com.example.prueba.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.prueba.R;
import com.example.prueba.controller.AnimatorNew;
import com.example.prueba.db.DatabaseManager;
import com.example.prueba.model.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    private EditText editTextImei, editTextNumero, editTextCompania, editTextDetalles;
    private Button btnRegistrar;

    private TextInputEditText editTextBaneo;
    private DatabaseManager dbManager;
    private LottieAnimationView likeAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Asignar vistas
        editTextImei = findViewById(R.id.editTextImei);
        editTextNumero = findViewById(R.id.editTextNumero);
        editTextBaneo = findViewById(R.id.editTextBaneo);
        editTextCompania = findViewById(R.id.editTextCompania);
        editTextDetalles = findViewById(R.id.editTextDetalles);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        likeAnimationView = findViewById(R.id.likeImageView1);

        AnimatorNew likeAnimator = new AnimatorNew();
        likeAnimator.beginAnimation(likeAnimationView,R.raw.animation,R.raw.animation);

        // Crear instancia del administrador de la base de datos
        dbManager = new DatabaseManager(this);
        dbManager.open();

        // Configurar el botón de guardar para insertar los datos en la base de datos
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los campos de entrada
                String imei = editTextImei.getText().toString();
                String numero = editTextNumero.getText().toString();
                String baneo = editTextBaneo.getText().toString();
                String compania = editTextCompania.getText().toString();
                String detalles = editTextDetalles.getText().toString();
                // Generamos el Chip mediante los datos obtenidos
                Chip chip = new Chip(imei, numero, baneo, compania, detalles);

                // Insertar los datos en la base de datos
                dbManager.insertData(chip);

                // Mostrar un mensaje de éxito o cerrar la actividad
                Toast.makeText(AddActivity.this, "Datos agregados correctamente", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.zoom_out,0); // Aplica la animación
            }
        });

        editTextBaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this,
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
