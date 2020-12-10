package com.example.myapplication.database.company;

import androidx.room.TypeConverter;

import com.example.myapplication.Truck;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TrucksTypeConverter {
    static Gson gson = new Gson();

    @TypeConverter
    public static ArrayList<Truck> stringToSomeObjectList(String data) {
        if (data == null) {
            return new ArrayList<>();
        }

        Type listType = new TypeToken<List<Truck>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(ArrayList<Truck> someObjects) {
        return gson.toJson(someObjects);
    }
}
