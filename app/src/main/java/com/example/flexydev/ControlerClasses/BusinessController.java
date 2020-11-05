package com.example.flexydev.ControlerClasses;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flexydev.ModelClasses.Admin;
import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.BusinessOwner;
import com.google.gson.Gson;

public class BusinessController extends AppCompatActivity {


    public Business retrieveBusiness(Context context){

        final SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("Business", null);
        Business business = gson.fromJson(json, Business.class);

        return business;
    }

    public void storeBusiness(Context context, Business business){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(business);
        editor.putString("Business", json);
        editor.commit();

    }

    public BusinessOwner retrieveBusinessOwner(Context context){

        final SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("businessOwner", null);
        BusinessOwner businessOwner = gson.fromJson(json, BusinessOwner.class);

        return businessOwner;
    }

    public void storeBusinessOwner(Context context, BusinessOwner businessOwner){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(businessOwner);
        editor.putString("businessOwner", json);
        editor.commit();

    }

}
