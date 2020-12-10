package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

/**
 * Builder class for every Dialog shown on app.
 */
public class MyDialogFragment extends DialogFragment {
    // this interfaces permits to "bring" items in Activities

    // update the new company of analysis (ShowCompanyActivity)
    public interface CompanyNameCallback {
        // set the date of analysis
        void nameChosen(String string);
    }

    // create new truck (EditCompanyActivity)
    public interface TruckCallback {
        // set the date of analysis
        void truckCreated(Truck truck);
    }

    // link with activities
    private CompanyNameCallback companyNameCallback;
    private TruckCallback truckCallback;

    // empty constructor
    public MyDialogFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = getActivity();
        // associate callback to activities
        if (activity != null) {
            if (activity.getClass().getName()
                    .equals("com.example.myapplication.activities.ShowCompanyActivity")) {
                // for updating field
                companyNameCallback = (CompanyNameCallback) context;
            } else if (activity.getClass().getName()
                    .equals("com.example.myapplication.activities.EditCompanyActivity")) {
                // new truck!
                truckCallback = (TruckCallback) context;
                Log.d("TAG", "YESS");
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // an activity open dialog with argument put in a Bundle
        Bundle bundle = getArguments();
        int id = -1;
        if (bundle != null) {
            id = bundle.getInt("WHAT");
        }
        switch (id) {
            // users insert name of new company
            case 0: {
                // build a customized UI for dialog
                View v = LayoutInflater.from(getContext())
                        .inflate(R.layout.single_text_input_layout, null);
                EditText nameEditText = v.findViewById(R.id.edit_text);
                return new AlertDialog.Builder(requireContext())
                        .setTitle("Nuova ditta")
                        .setView(v)
                        .setPositiveButton("salva", (dialog, which) -> {
                            companyNameCallback.nameChosen(nameEditText.getText().toString());
                        })
                        .create();
            }
            // add new truck to company
            case 1: {
                // build a customized UI for dialog
                boolean btg = bundle.getBoolean("BUTANGAS");
                int size = 5;
                View v = LayoutInflater.from(getContext())
                        .inflate(R.layout.activity_edit_truck, null);
                EditText[] editTexts = new EditText[6];
                editTexts[0] = v.findViewById(R.id.edit_text_marca);
                editTexts[1] = v.findViewById(R.id.edit_text_targa_mezzo);
                editTexts[2] = v.findViewById(R.id.edit_text_tara_mezzo);
                editTexts[3] = v.findViewById(R.id.edit_text_lordo);
                editTexts[4] = v.findViewById(R.id.edit_text_portata_mezzo);
                editTexts[5] = v.findViewById(R.id.edit_text_litri);
                if (btg) {
                    size = 6;
                    v.findViewById(R.id.text_input_layout_litri).setVisibility(View.VISIBLE);
                }
                Button saveButton = v.findViewById(R.id.save_button);
                int finalSize = size;
                saveButton.setOnClickListener(v1 -> {
                    for (int i = 0; i < finalSize; i++) {
                        if (editTexts[i].getText().toString().isEmpty()) {
                            Toast.makeText(getContext(), "Riempi tutti i campi",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Objects.requireNonNull(getDialog()).cancel();
                    }
                    Truck truck = new Truck(editTexts[0].getText().toString(),
                            editTexts[1].getText().toString(),
                            Integer.parseInt(editTexts[2].getText().toString()),
                            Integer.parseInt(editTexts[3].getText().toString()),
                            Integer.parseInt(editTexts[4].getText().toString()), 0);
                    if (btg) {
                        truck.setCurrentLiters(Integer.parseInt(editTexts[5].getText().toString()));
                    }
                    truckCallback.truckCreated(truck);
                });
                return new AlertDialog.Builder(requireContext())
                        .setTitle("Nuovo mezzo")
                        .setView(v)
                        .create();
            }
        }
        return null;
    }
}
