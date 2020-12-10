package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.database.day.Day;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ShowDayActivity extends AppCompatActivity {

    // store id of editTexts
    private static final int[] editTextIds = {
        R.id.edit_text_data, R.id.edit_text_ditta,
        R.id.edit_text_marca, R.id.edit_text_targa_mezzo,
        R.id.edit_text_tara_mezzo, R.id.edit_text_lordo,
        R.id.edit_text_portata_mezzo, R.id.edit_text_peso_in_arrivo,
        R.id.edit_text_da_caricare, R.id.edit_text_rimanenza,
        R.id.edit_text_litri_in_arrivo, R.id.edit_text_litri_attuali,
        R.id.edit_text_litri_totali
    };

    // edit texts to show
    private final ArrayList<EditText> editTexts = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity);

        // take from MainActivity
        Day day = new Gson().fromJson(getIntent().getStringExtra("DAY"), Day.class);

        // initialize View object; 2 arrays has same size!
        for (int i = 0; i < editTextIds.length; i++) {
            editTexts.add(findViewById(editTextIds[i]));
            editTexts.get(i).setEnabled(false);
        }
        // hide button
        findViewById(R.id.save_button).setVisibility(View.INVISIBLE);
        editTexts.get(0).setText(day.date);
        editTexts.get(1).setText(day.company);
        editTexts.get(2).setText(day.truck.getTruckBrand());
        editTexts.get(3).setText(day.truck.getTruckLicensePlate());
        editTexts.get(4).setText(day.truck.getTruckTare() + "");
        editTexts.get(5).setText(day.truck.getTruckGross() + "");
        editTexts.get(6).setText(day.truck.getTruckReach() + "");
        editTexts.get(7).setText(day.quantityArrived + "");
        editTexts.get(8).setText(day.toRecharge + "");
        editTexts.get(9).setText(day.remainingStock + "");
        if (day.company.equals("ButanGas")) {
            findViewById(R.id.text_input_layout_litri_in_arrivo).setVisibility(View.VISIBLE);
            editTexts.get(10).setVisibility(View.VISIBLE);
            editTexts.get(10).setText(day.litersArrived + "");
            findViewById(R.id.linear_layout4).setVisibility(View.VISIBLE);
            editTexts.get(11).setText(day.truck.getCurrentLiters() + "");
            editTexts.get(12).setText(day.totalLiters + "");
        }

    }
}