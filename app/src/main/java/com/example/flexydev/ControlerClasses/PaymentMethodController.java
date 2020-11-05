package com.example.flexydev.ControlerClasses;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.gson.Gson;

public class PaymentMethodController extends AppCompatActivity {

    public PaymentMethod retrievePaymentMethod(Context context){

        final SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode

        Gson gson = new Gson();
        String json = pref.getString("PaymentMethod", null);
        PaymentMethod paymentMethod = gson.fromJson(json, PaymentMethod.class);

        return paymentMethod;
    }

    public void storePaymentMethod(Context context, PaymentMethod paymentMethod){

        SharedPreferences pref = context.getSharedPreferences("userProfile", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();

        Gson gson = new Gson();
        String json = gson.toJson(paymentMethod);
        editor.putString("PaymentMethod", json);
        editor.commit();

    }

}
