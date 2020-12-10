package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.MyDialogFragment;
import com.example.myapplication.R;
import com.example.myapplication.adapter.CompanyAdapter;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.company.Company;
import com.example.myapplication.database.company.CompanyDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ShowCompanyActivity extends AppCompatActivity implements View.OnClickListener,
        MyDialogFragment.CompanyNameCallback {

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CompanyAdapter companyAdapter;
    private ArrayList<Company> companies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fields);

        // reference to views
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.main_floating_action_button);
        recyclerView = findViewById(R.id.main_recycler_view);
        // build app bar
        toolbar.setTitle("Seleziona una ditta");
        setSupportActionBar(toolbar);

        fab.setOnClickListener(this);
        // build the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // the adapter shows the data of Day
        companyAdapter = new CompanyAdapter(companies);
        companyAdapter.setOnClickListner(myClickListener);
    }

    /**
     *
     * CHANGE STA PORCATA!
     */
    @Override
    protected void onResume() {
        super.onResume();
        companies.clear();
        readCompaniesFromRoom();
    }


    /**
     * Opens db and read all companies and storing them to a list.
     */
    private void readCompaniesFromRoom() {
        new Thread(() -> {
            AppDatabase database = Room.databaseBuilder(
                    getApplicationContext(), AppDatabase.class, MainActivity.DATABASE_NAME).build();
            CompanyDao companyDao = database.companyDao();
            companies.addAll(companyDao.getAll());
            runOnUiThread(() -> recyclerView.setAdapter(companyAdapter));
            database.close();
        }).start();
    }

    @Override
    public void onClick(View v) {
        // add a new company
        if (v == fab) {
            MyDialogFragment myDialogFragment = new MyDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("WHAT", 0);
            myDialogFragment.setArguments(bundle);
            myDialogFragment.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * Callback from DialogFragment; build new company.
     * Add it to list and to db.
     * @param string new name of company to create.
     */
    @Override
    public void nameChosen(String string) {
        Company company = new Company(string);
        companies.add(company);
        companyAdapter.notifyDataSetChanged();
        new Thread(() -> {
            AppDatabase database = Room.databaseBuilder(
                    getApplicationContext(), AppDatabase.class, MainActivity.DATABASE_NAME).build();
            CompanyDao companyDao = database.companyDao();
            companyDao.insertAll(company);
            database.close();
        }).start();
    }

    /**
     * Click listener associated to adapter, passed as argument of it.
     * Edit company on tap.
     */
    public View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // reference to Object tapped
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            // index of item clicked
            int i = viewHolder.getAdapterPosition();
            // clicked on all item
            if (v.getId() == -1) {
                Intent intent = new Intent(getApplicationContext(), EditCompanyActivity.class);
                intent.putExtra("COMPANY", new Gson().toJson(companies.get(i)));
                startActivity(intent);
                // remove item from database
            } else if (v.getId() == R.id.delete_image_button) {
                Company company = companies.remove(i);
                companyAdapter.notifyDataSetChanged();
                new Thread(() -> {
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, MainActivity.DATABASE_NAME).build();
                    db.companyDao().delete(company);
                    db.close();
                }).start();
            }
        }
    };
}