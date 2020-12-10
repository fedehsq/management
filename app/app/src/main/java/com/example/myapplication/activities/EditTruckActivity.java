package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.R;
import com.example.myapplication.Truck;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.company.Company;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EditTruckActivity extends AppCompatActivity implements View.OnClickListener {

    // store id of editTexts
    private static final int[] editTextIds = {
            R.id.edit_text_marca, R.id.edit_text_targa_mezzo, R.id.edit_text_tara_mezzo,
            R.id.edit_text_lordo, R.id.edit_text_portata_mezzo, R.id.edit_text_litri
    };
    // edit texts to show
    private final ArrayList<EditText> editTexts = new ArrayList<>();
    // save saveButton
    private Button saveButton;
    // owner company of truck
    private Company company;
    // truck to modify
    private Truck truck;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_truck);

        truck = new Gson().fromJson(getIntent().getStringExtra("TRUCK"), Truck.class);
        company =  new Gson().fromJson(getIntent().getStringExtra("COMPANY"), Company.class);

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        // initialize View object; 2 arrays has same size!
        for (int i = 0; i < editTextIds.length; i++) {
            editTexts.add(findViewById(editTextIds[i]));
        }
        editTexts.get(0).setText(truck.getTruckBrand());
        editTexts.get(1).setText(truck.getTruckLicensePlate());
        editTexts.get(2).setText(truck.getTruckTare() + "");
        editTexts.get(3).setText(truck.getTruckGross() + "");
        editTexts.get(4).setText(truck.getTruckReach() + "");
        if (company.companyName.equals("ButanGas")) {
            findViewById(R.id.text_input_layout_litri).setVisibility(View.VISIBLE);
            editTexts.get(5).setText(truck.getCurrentLiters() + "");
        }
    }

    @Override
    public void onClick(View v) {
        if (v == saveButton) {
            // check if all fields are written
            int size = company.companyName.equals("ButanGas") ? 6 : 5;
            for (int i = 0; i < size; i++) {
                if (editTexts.get(i).getText().toString().isEmpty()) {
                    Toast.makeText(this, "Riempi tutti i campi", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            }
            // update truck
            for (int i = 0; i < company.trucks.size(); i++) {
                if (company.trucks.get(i).getTruckBrand().equals(truck.getTruckBrand())) {
                    Truck truck = new Truck(
                            editTexts.get(0).getText().toString(),
                            editTexts.get(1).getText().toString(),
                            Integer.parseInt(editTexts.get(2).getText().toString()),
                            Integer.parseInt(editTexts.get(3).getText().toString()),
                            Integer.parseInt(editTexts.get(4).getText().toString()), 0);
                    if (company.companyName.equals("ButanGas")) {
                        truck.setCurrentLiters(Integer.parseInt(editTexts.get(5)
                                .getText().toString()));
                    }
                    company.trucks.set(i, truck);
                    break;
                }
            }
            new Thread(() -> {
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, MainActivity.DATABASE_NAME).build();
                // remove previous item for delete trucks
                db.companyDao().delete(company);
                // add the new company with updated trucks
                db.companyDao().insertAll(company);
                db.close();
                Intent intent = new Intent();
                intent.putExtra("UPDATED_COMPANY", new Gson().toJson(company, Company.class));
                setResult(RESULT_OK, intent);
                finish();
            }).start();
        }
    }
}