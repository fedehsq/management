package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplication.CompanyList;
import com.example.myapplication.R;
import com.example.myapplication.Truck;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.company.Company;
import com.example.myapplication.database.company.CompanyDao;
import com.example.myapplication.database.day.Day;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;


/***
 * aggiungere possibilità di inserire un nuovo mezzo;
 * Contalitri in arrivo = nuovo - precedete; lettura contalitri a mano, sotto peso in arrivo
 * BUILD A CLASS FOR EVERY COMPANY WITH THAT SHIT IN ARRAY.XML
 */
public class InsertDataActivity extends AppCompatActivity implements View.OnClickListener {

    // 0: data, 1 ditta, 2 marca, 3 targa, 4 tara, 5 lordo, 6 portata,
    // 7 peso, 8 da caricare, 9 rimanenza
    private final ArrayList<TextInputLayout> textInputLayouts = new ArrayList<>();
    private final ArrayList<EditText> editTexts = new ArrayList<>();
    // how many companies!?
    private int COMPANIES_NUMBERS;
    private Button saveButton;
    // contains all companies for building dropdown menus!
    private final CompanyList companyList = new CompanyList();
    // company chosen in DropDown edit text
    private Company companyChosen;
    // truck selected
    private Truck truckChosen;
    // store id of textInputLayouts
    private static final int[] textInputLayoutIds = {
            R.id.text_input_layout_data, R.id.text_input_layout_ditta,
            R.id.text_input_layout_marca, R.id.text_input_layout_targa_mezzo,
            R.id.text_input_layout_tara_mezzo, R.id.text_input_layout_lordo,
            R.id.text_input_layout_portata_mezzo, R.id.text_input_layout_peso_in_arrivo,
            R.id.text_input_layout_da_caricare, R.id.text_input_layout_rimanenza,
            R.id.text_input_layout_litri_in_arrivo, R.id.text_input_layout_litri_attuali,
            R.id.text_input_layout_litri_totali
    };

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

    // linear layout of contalitri, show only if butangas selected
    private LinearLayout litersLinearLayout;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_activity);

        // initialize View object; 2 arrays has same size!
        for (int i = 0; i < textInputLayoutIds.length; i++) {
            textInputLayouts.add(findViewById(textInputLayoutIds[i]));
            editTexts.add(findViewById(editTextIds[i]));
        }
        // set the two editable edit texts as to compile
        initializeEditableEditText(7, "Inserisci il peso");

        // Display a date in day, month, year format
        Calendar c = Calendar.getInstance();
        editTexts.get(0).setText(c.get(Calendar.DATE) + "/" +
                (c.get(Calendar.MONTH) + 1)+ "/" + c.get(Calendar.YEAR));
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);



        // fill companies
        initializeCompanies();

        // TEXT WATCHER DA AGGIUNGERE AL PARAMETRO EDITABILE!

        // add text watcher to "lordo"[5] && to "peso_in_arrivo"[7]
        // for calculating "da_caricare" [8]: "da_caricare" = "lordo" - "peso_in_arrivo"
        // the flag indicates if [fst] - [snd] or [snd] - [fst]
        setTextWatcher(5, 7, 8, true);
        //setTextWatcher(7, 5, 8, false);

        // add text watcher to "tara_mezzo"[4] && to "peso_in_arrivo"[7]
        // for calculating "rimanenza" [5]: "rimanenza" = "peso_in_arrivo" - "tara_mezzo"
        // the flag indicates if [fst] - [snd] or [snd] - [fst]
        setTextWatcher(4, 7, 9, false);
        //setTextWatcher(7, 4, 9, true);

    }

    @SuppressLint("SetTextI18n")
    private void initializeButanGasView() {
        // layout showing liters
        litersLinearLayout = findViewById(R.id.linear_layout4);


        textInputLayouts.get(10).setVisibility(View.VISIBLE);
        litersLinearLayout.setVisibility(View.VISIBLE);

        // set error eabled on liters text input layout
        initializeEditableEditText(10, "Inserisci i litri");

        // add text watcher to "litri_in_arrivo"[10] && to "litri_attuali"[11]
        // for calculating "litri_totali" [12]: "litri_totali" = "litri_in_arrivo" - "litri_attuali"
        // the flag indicates if [fst] - [snd] or [snd] - [fst]
        setTextWatcher( 11,10, 12, false);



    }

    /**
     * Set edit text editable with error if is empty.
     */
    private void initializeEditableEditText(int pos, String message) {
        textInputLayouts.get(pos).setErrorEnabled(true);
        textInputLayouts.get(pos).setError(message);
        editTexts.get(pos).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    textInputLayouts.get(pos).setErrorEnabled(true);
                    textInputLayouts.get(pos).setError(message);
                } else {
                    textInputLayouts.get(pos).setErrorEnabled(false);
                }
            }
        });
    }

    /**
     * Initialize all companies with default trucks; reading from Room.
     * Convert toString for build standard adapter.
     */
    private void initializeCompanies() {
        // initialize trucks
        new Thread(() -> {
            AppDatabase database = Room
                    .databaseBuilder(getApplicationContext(), AppDatabase.class,
                            MainActivity.DATABASE_NAME)
                    .build();
            CompanyDao companyDao = database.companyDao();
            // put company in a list
            ArrayList<Company> companies = new ArrayList<>(companyDao.getAll());
            COMPANIES_NUMBERS = companies.size();
            companyList.setCompanies(companies);
            database.close();

            // show ALWAYS only company name and then set the other Adapters!
            runOnUiThread(() -> buildAdapter(companyList.getCompaniesNames()));

        }).start();

    }

    /**
     * Calculate the differences between editTexts[fst] and editTexts[snd] as float
     * and put it to editTexts[to].
     * Set a TextWatcher to editTexts[snd]; when it is filled,
     * a thread will set the result in editTexts[to].
     * @param fst index of fst editText to extract value as float from editTexts[]
     * @param snd index of second editText to extract value as float from editTexts[]
     * @param to the resulting EditText
     * @param bool indicates the correct order of operation
     */
    private void setTextWatcher(int fst, int snd, int to, boolean bool) {
        editTexts.get(snd).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                // calculate and set result to editTexts[to] if fields aren't empty
                String stringFirst = editTexts.get(fst).getText().toString();
                String stringSecond = s.toString();
                if (!stringFirst.isEmpty() && !stringSecond.isEmpty()) {
                    int first = Integer.parseInt(stringFirst);
                    int second = Integer.parseInt(stringSecond);
                    // check correct order of operation
                    int result = (bool) ? first - second : second - first;
                    editTexts.get(to).setText(result + "");
                } else {
                    editTexts.get(to).setText("");
                }

            }
        });
    }


    /*

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String item = (String) parent.getItemAtPosition(position);
        // recognize the EditText clicked thanks to the number of items inside its list
        int size = parent.getCount();
        // company
        if (size == COMPANIES_NUMBERS) {
            // clear all other texts
            clearAll();
            // set correct truck brands to show
            setBrand(item);
        } else {
            // brand selected, fill other forms
            formFilling();
        }
    }

     */

    /**
     * Clear EditTexts[] when a new company is selected
     */
    private void clearAll() {
        for (int i = 2; i < editTexts.size(); i++) {
            editTexts.get(i).setText("");
        }
    }


    /**
     * Shows correct items to show in "brand" EditText[2] building an adapter with proper xml arrays
     * @param companyName name decides what to show in "brand" EditText[2].
     */
     private void setBrand(String companyName) {
         // search in array with all companies the correspondence!
         for (Company company : companyList.getCompanies()) {
             if (company.getCompanyName().equals(companyName)) {
                 companyChosen = company;
                 // if butangas shows contalitri!
                 if (companyName.equals("ButanGas")) {
                     // for butangas
                     initializeButanGasView();
                 } else {
                     textInputLayouts.get(10).setVisibility(View.GONE);
                     if (litersLinearLayout != null) {
                         litersLinearLayout.setVisibility(View.GONE);
                     }
                 }
                 buildAdapter(company.getTrucksBrands(), 2);
                 break;
             }
         }
    }

    /**
     * Fill all remaining EditText[] according to brand truck selected.
     * List in which searches the correct truck to show in the EditTexts.
     */
    @SuppressLint("SetTextI18n")
    private void formFilling() {
        String brand = editTexts.get(2).getText().toString();
        ArrayList<Truck> trucks = companyChosen.getTrucks();
        for (Truck truck : trucks) {
            if (truck.getTruckBrand().equals(brand)) {
                truckChosen = truck;
                editTexts.get(3).setText(truck.getTruckLicensePlate());
                editTexts.get(4).setText(truck.getTruckTare() + "");
                editTexts.get(5).setText(truck.getTruckGross() + "");
                editTexts.get(6).setText(truck.getTruckReach() + "");
                if (companyChosen.companyName.equals("ButanGas")) {
                    editTexts.get(11).setText(truck.getCurrentLiters() + "");
                }
                break;
            }
        }
    }


    private void buildAdapter(ArrayList<String> elements) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_dropdown_item_1line,
            elements);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) editTexts.get(1);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            // clear all other texts
            clearAll();
            // set correct truck brands to show
            setBrand(item);
        });
    }

    /**
     * Builds the array of items to show in drop down menu of EditText[index].
     * @param elements strings to show in adapter.
     */
    private void buildAdapter(ArrayList<String> elements, int index) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_dropdown_item_1line,
            elements);
        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) editTexts.get(index);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> formFilling());
    }

    @Override
    public void onClick(View v) {
        if (v == saveButton) {
            // check if butangas chosen
            int size = editTexts.size();
            int arrivedLiters = 0, currentLiters = 0, totalLiters = 0;
            if (companyChosen != null && companyChosen.companyName.equals("ButanGas")) {
                // save liters on truck
                currentLiters = Integer.parseInt(editTexts
                        .get(12).getText().toString());
                truckChosen.setCurrentLiters(currentLiters);

                arrivedLiters = Integer.parseInt(editTexts.get(10).getText().toString());// liters 1
                totalLiters = Integer.parseInt(editTexts.get(11).getText().toString());

            } else {
                size = editTexts.size() - 3;
            }
            // check if all fields are filled
            for (int i = 0; i < size; i++) {
                if (editTexts.get(i).getText().toString().isEmpty()) {
                    Toast.makeText(this, "Riempi tutti i campi",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // build the day and set as result for MainActivity
            Day day = new Day(editTexts.get(0).getText().toString(), // date
                    editTexts.get(1).getText().toString(), // company name
                    truckChosen, // truck
                    Integer.parseInt(editTexts.get(7).getText().toString()), // quantity
                    Integer.parseInt(editTexts.get(8).getText().toString()), // to recharge
                    Integer.parseInt(editTexts.get(9).getText().toString()), // remaining
                    arrivedLiters, // liters 1
                    totalLiters // liters 2
            );
            // intent to bring back
            Intent intent = new Intent();
            intent.putExtra("DAY", new Gson().toJson(day));
            // bring back updated Company with liters updated if butangas!
            if (companyChosen.companyName.equals("ButanGas")) {
                intent.putExtra("COMPANY", new Gson().toJson(companyChosen));
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}