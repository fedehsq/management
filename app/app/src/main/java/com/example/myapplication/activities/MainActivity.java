package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.Truck;
import com.example.myapplication.adapter.DayAdapter;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.company.Company;
import com.example.myapplication.database.company.CompanyDao;
import com.example.myapplication.database.day.Day;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATABASE_NAME = "Paola";


    // listing companies
    private RecyclerView recyclerView;
    // adapter: update the UI!
    private DayAdapter dayAdapter;
    // elements got from Room
    private ArrayList<Day> days = new ArrayList<>();
    // button starts InsertDataActivity
    private FloatingActionButton fab;
    // custom toolbar
    private Toolbar toolbar;
    // left drawer
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // reference to views
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        fab = findViewById(R.id.main_floating_action_button);
        recyclerView = findViewById(R.id.main_recycler_view);
        // build app bar
        setSupportActionBar(toolbar);
        // set drawable icon
        Objects.requireNonNull(getSupportActionBar())
                .setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab.setOnClickListener(this);
        // build the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // the adapter shows the data of Day
        dayAdapter = new DayAdapter(days);
        dayAdapter.setOnItemClickListener(myClickListener);
        recyclerView.setAdapter(dayAdapter);

        // check if it is the first app accesses so store default values to database
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("FIRST_ACCESS", true)) {
            initializeCompanies();
            sharedPref.edit().putBoolean("FIRST_ACCESS", false).apply();
        }
        // read companies database
        readDaysFromRoom();
    }

    /**
     * Function that initialize default values of companies with them trucks.
     * Store that in Room.
     */
    private void initializeCompanies() {
        // default company
        Company one = new Company("One");
        Company two = new Company("Two");
        Company three = new Company("Three");
        Company four = new Company("Four");
        Company five = new Company("Five");
        Company six = new Company("Six");
        initialize(one, one.trucks);
        initialize(two, two.trucks);
        initialize(four, four.trucks);
        initialize(five, five.trucks);
        initialize(three, three.trucks);
        initialize(six, six.trucks);

    }

    /**
     * Initialize companies putting correct trucks. Save to Room.
     *
     * @param company to save
     * @param trucks  of companies
     */
    public void initialize(Company company, ArrayList<Truck> trucks) {
        switch (company.companyName) {
            case "One":
                trucks.add(new Truck("Brand", "XXXX",
                        8580, 13900, 5320, 0));
                break;
            case "Two":
                trucks.add(new Truck("Brand", "XXXX",
                        6980, 10380, 34000, 0));
                trucks.add(new Truck("Brand", "XXXX",
                        8900, 15000, 6100, 0));
                trucks.add(new Truck("Brand", "XXXX",
                        7360, 11980, 4620, 0));
                trucks.add(new Truck("Brand", "XXXX",
                        8840, 14700, 5860, 0));
                trucks.add(new Truck("Brand", "XXXX",
                        5300, 8000, 2700, 0));
                break;
            case "Three":
                trucks.add(new Truck("Brand", "XXXX",
                        7460, 11980, 4520, 0));
                break;
            case "Four":
                trucks.add(new Truck("Brand", "XXXX",
                        7400, 11600, 4200, 0));
                trucks.add(new Truck("Brand", "XXXX",
                        5800, 9980, 4180, 0));
                break;
            case "Five":
                trucks.add(new Truck("Brand", "XXXX",
                        8000, 13900, 5900, 0));
                trucks.add(new Truck("Brand", "XXXX",
                        6600, 10000, 3340, 0));
                break;
            case "Six":
                trucks.add(new Truck("TG Brand", "XXXX",
                        6440, 9980, 3540, 0));
                trucks.add(new Truck("TG Brand", "XXXX",
                        8300, 13900, 5600, 0));
                break;

        }
        saveCompanyToDb(company);
    }

    /**
     * Save to Room default companies at first access.
     *
     * @param company to save.
     */
    void saveCompanyToDb(Company company) {
        new Thread(() -> {
            AppDatabase appDatabase = Room
                    .databaseBuilder(getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .build();
            CompanyDao companyDao = appDatabase.companyDao();
            companyDao.insertAll(company);
            appDatabase.close();
        }).start();

    }

    // open the drawer
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        drawerLayout.open();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Open and read from database; store all day in companies
     */
    private void readDaysFromRoom() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME).build();
            days.addAll(db.dayDao().getAll());
            db.close();
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // get Day, add to recycler view
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                // get the result day as JSON object
                Day day = new Gson().fromJson(data.getStringExtra("DAY"), Day.class);
                // add to UI list
                days.add(day);
                // update list UI
                dayAdapter.notifyDataSetChanged();
                // update truck if butagas chosen
                Company company = new Gson().fromJson(data.getStringExtra("COMPANY"),
                        Company.class);
                if (company != null) {
                    // updated
                    new Thread(() -> {
                        AppDatabase db = Room.databaseBuilder(this,
                                AppDatabase.class, DATABASE_NAME).build();
                        db.companyDao().delete(company);
                        db.companyDao().insertAll(company);
                        db.close();
                    }).start();
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == fab) {
            // starts activity and wait the Day as result
            startActivityForResult(
                    new Intent(this, InsertDataActivity.class), 1);
        }
    }

    /**
     * Click listener passed to DayAdapter for recognize what clicked.
     */
    public View.OnClickListener myClickListener = v -> {
        // reference to Object tapped
        RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
        // index of item clicked
        int i = viewHolder.getAdapterPosition();
        // clicked on all item
        if (v.getId() == -1) {
            Intent intent = new Intent(this, ShowDayActivity.class);
            intent.putExtra("DAY", new Gson().toJson(days.get(i)));
            startActivity(intent);
            // click on remove button => delete elements from db and ui list
        } else if (v.getId() == R.id.delete_image_button) {
            // remove from list and UI
            days.remove(i);
            dayAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        // remove from db
        saveDaysToRoom();
    }

    /**
     * Open database and save all days into it updating the previous ones.
     *
     */
    private void saveDaysToRoom() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(this,
                    AppDatabase.class, DATABASE_NAME).build();
            db.dayDao().deleteAll((ArrayList<Day>) db.dayDao().getAll());
            db.dayDao().insertAll(days);
            db.close();
        }).start();
    }

}