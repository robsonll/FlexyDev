package com.example.flexydev.ControlerClasses;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import com.example.flexydev.ModelClasses.Admin;
import com.google.gson.Gson;

public class AdminController extends AppCompatActivity {


    public Admin retrieveUser(Context context){

        final SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("User", null);
        Admin user = gson.fromJson(json, Admin.class);

        return user;
    }

    public void storeUser(Context context, Admin user){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("User", json);
        editor.commit();

    }
}
