package com.example.myapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplication.MyDialogFragment;
import com.example.myapplication.R;
import com.example.myapplication.Truck;
import com.example.myapplication.adapter.TruckAdapter;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.company.Company;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * TRUCKS NOT USED AS ITEM OF ROOM. DEPENDING ONLY FROM COMPANY
 */
public class EditCompanyActivity extends AppCompatActivity implements View.OnClickListener,
        MyDialogFragment.TruckCallback {

    private TruckAdapter truckAdapter;
    private EditText companyEditText;
    private Company company;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ArrayList<Truck> trucks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company);
        // reference to views
        // shows current company name, can be modified
        companyEditText = findViewById(R.id.edit_text_company_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.main_floating_action_button);
        recyclerView = findViewById(R.id.main_recycler_view);
        // get company from previous activity
        company = new Gson()
                .fromJson(getIntent().getStringExtra("COMPANY"), Company.class);
        // set the text of edit text as company name
        companyEditText.setText(company.companyName);
        // build app bar
        toolbar.setTitle("Modifica");
        setSupportActionBar(toolbar);
        fab.setOnClickListener(this);
        // build the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // the adapter shows the data of Day
        trucks.addAll(company.trucks);
        truckAdapter = new TruckAdapter(trucks);
        truckAdapter.setOnClickListener(myClickListener);
        recyclerView.setAdapter(truckAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // get UPDATED company and UPDATE recycler view!
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {

                trucks.clear();
                //truckAdapter.notifyDataSetChanged();
                // get company from called activity
                company = new Gson()
                        .fromJson(data.getStringExtra("UPDATED_COMPANY"), Company.class);
                // the adapter shows the data of Day
                trucks.addAll(company.trucks);
                truckAdapter.notifyDataSetChanged();

                /*
                truckAdapter = new TruckAdapter(company.trucks);
                recyclerView.setOnClickListener(myClickListener);
                recyclerView.setAdapter(truckAdapter);

                 */
            }
        }
    }

    @Override
    public void onBackPressed() {
        // take the result (updated name) to caller activity!
        String updatedCompanyName = companyEditText.getText().toString();
        if (!company.companyName.equals(updatedCompanyName)) {
            company.companyName = updatedCompanyName;
            saveToRoom();
        }
        super.onBackPressed();
    }

    /**
     * Save the updated company to Room.
     * It can be updated in 3 different ways: delete/edited/added truck.
     */
    public void saveToRoom() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, MainActivity.DATABASE_NAME).build();
            db.companyDao().delete(company);
            // add the new company with updated name
            db.companyDao().insertAll(company);
            db.close();
        }).start();
    }


    @Override
    public void onClick(View v) {
        if (v == fab) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            Bundle bundle = new Bundle();
            if (company.companyName.equals("ButanGas")) {
                bundle.putBoolean("BUTANGAS", true);
            }
            bundle.putInt("WHAT", 1);
            myDialogFragment.setArguments(bundle);
            myDialogFragment.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void truckCreated(Truck truck) {
        Log.d("TAG", "created");
        company.trucks.add(truck);
        trucks.add(truck);
        truckAdapter.notifyDataSetChanged();
        saveToRoom();
    }

    /**
     * Click listener associated to adapter, passed as argument of it.
     * Edit company on tap.
     */
    public View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("TAG", "clicked");
            // reference to Object tapped
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            // index of item clicked
            int i = viewHolder.getAdapterPosition();
            // clicked on all item
            if (v.getId() == -1) {
                Intent intent = new Intent(getApplicationContext(), EditTruckActivity.class);
                intent.putExtra("TRUCK", new Gson().toJson(trucks.get(i)));
                intent.putExtra("COMPANY", new Gson().toJson(company));
                startActivityForResult(intent, 1);
                // remove item from COMPANY trucks
            } else if (v.getId() == R.id.delete_image_button) {
                company.trucks.remove(i);
                trucks.remove(i);
                truckAdapter.notifyDataSetChanged();
                new Thread(() -> {
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, MainActivity.DATABASE_NAME).build();
                    // remove previous item for delete trucks
                    db.companyDao().delete(company);
                    // add the new company with updated trucks
                    db.companyDao().insertAll(company);
                    db.close();
                }).start();
            }
        }
    };

}