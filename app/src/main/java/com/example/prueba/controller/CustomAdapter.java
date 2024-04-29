package com.example.prueba.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.prueba.view.EditActivity;
import com.example.prueba.view.MainActivity;
import com.example.prueba.R;
import com.example.prueba.db.DatabaseManager;
import com.example.prueba.model.Chip;

import java.util.ArrayList;

// Adaptador personalizado para el ListView
public class CustomAdapter extends ArrayAdapter<Chip> {
    private Context mContext;
    private ArrayList<Chip> chipsList;
    private DatabaseManager dbManager;
    private MainActivity mainActivity;
    private Context activityContext;

    // Constructor
    public CustomAdapter(Context context, ArrayList<Chip> chips, DatabaseManager dbManager, MainActivity mainActivity) {
        super(context, 0, chips);
        mContext = context;
        chipsList = chips;
        this.dbManager = dbManager;
        this.mainActivity = mainActivity;
        this.activityContext = mainActivity;
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
            viewHolder.buttonBaneed = convertView.findViewById(R.id.buttonBaneed);
            viewHolder.buttonInUse = convertView.findViewById(R.id.buttonInUse);
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

        viewHolder.buttonBaneed.setOnClickListener(new View.OnClickListener() {
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
                LottieAnimationView likeAnimationView = dialogView.findViewById(R.id.likeImageView2);
                AnimatorNew likeAnimator = new AnimatorNew();
                likeAnimator.beginAnimation(likeAnimationView,R.raw.alert,R.raw.alert_dark);

                // Establecer el mensaje del diálogo
                textMessage.setText("¿Está seguro de actualizar la fecha de baneo?");

                // Configurar el clic del botón "Sí"
                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obtener el dato seleccionado
                        Chip chipSeleccionado = getItem(position);

                        // Actualizar la fecha de baneo a la fecha actual en la base de datos
                        dbManager.updateBaneoFecha(chipSeleccionado);

                        // Notificar al adaptador que los datos han cambiado
                        notifyDataSetChanged();

                        mainActivity.mostrarDatos();
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

        viewHolder.buttonInUse.setOnClickListener(new View.OnClickListener() {
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
                LottieAnimationView likeAnimationView = dialogView.findViewById(R.id.likeImageView2);
                AnimatorNew likeAnimator = new AnimatorNew();
                likeAnimator.beginAnimation(likeAnimationView,R.raw.prueba3,R.raw.alert_dark);

                // Establecer el mensaje del diálogo
                textMessage.setText("¿Está seguro de usar este chip?");

                // Configurar el clic del botón "Sí"
                buttonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obtener el dato seleccionado
                        Chip chipSeleccionado = getItem(position);

                        // Actualizar la fecha de baneo a la fecha actual en la base de datos
                        dbManager.updateInUse(chipSeleccionado);

                        // Notificar al adaptador que los datos han cambiado
                        notifyDataSetChanged();

                        mainActivity.mostrarDatos();
                        // Mostrar un mensaje de éxito
                        Toast.makeText(mContext, "Chip actualizado", Toast.LENGTH_SHORT).show();

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
                Chip chipSeleccionado = getItem(position);

                // Crear un intent para abrir la actividad de edición
                Intent intent = new Intent(mainActivity, EditActivity.class);

                // Pasar el dato seleccionado a la actividad de edición
                intent.putExtra("chipSeleccionado", chipSeleccionado);

                // Iniciar la actividad de edición
                activityContext.startActivity(intent);
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
        Button buttonBaneed;
        Button buttonInUse;
    }
}