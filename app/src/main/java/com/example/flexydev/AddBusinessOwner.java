package com.example.flexydev;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexydev.ControlerClasses.BusinessController;
import com.example.flexydev.ControlerClasses.PaymentMethodController;
import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.BusinessOwner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBusinessOwner extends AppCompatActivity {

    private EditText editTextOwnerName;
    private EditText editTextOwnerEmail;
    private EditText editTextOwnerPhone;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_owner);

        editTextOwnerName = findViewById(R.id.editTextOwnerName);
        editTextOwnerEmail = findViewById(R.id.editTextOwnerEmail);
        editTextOwnerPhone = findViewById(R.id.editTextOwnerPhone);

        Button buttonAddOwner= findViewById(R.id.buttonAddOwner);

        buttonAddOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validations
                if (editTextOwnerName.getText().toString().trim().equals("")) {
                    Toast.makeText(AddBusinessOwner.this, "Please enter Owner name.", Toast.LENGTH_SHORT).show();

                } else if (editTextOwnerEmail.getText().toString().trim().equals("")) {
                    Toast.makeText(AddBusinessOwner.this, "Please enter Owner email.", Toast.LENGTH_SHORT).show();
                } else {
                    createOwner(v);
                }
            }
        });

    }

    private void createOwner(View view) {

        final KProgressHUD hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Inserting Payment Method")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        BusinessController businessController = new BusinessController();
        db = FirebaseFirestore.getInstance();

        Business currentBusiness = businessController.retrieveBusiness(this);

        BusinessOwner newOwner = new BusinessOwner(
                editTextOwnerName.getText().toString().trim(),
                editTextOwnerEmail.getText().toString().trim(),
                editTextOwnerPhone.getText().toString().trim()
                );

        List<BusinessOwner> listOwners = currentBusiness.getOwners();

        if(listOwners == null){
            listOwners = new ArrayList<BusinessOwner>();
        }

        listOwners.add(newOwner);

        currentBusiness.setOwners(listOwners);

        businessController.storeBusiness(this, currentBusiness);

        DocumentReference businessRef = db.collection("business").document(currentBusiness.getId());

        Map<String, Object> updateMap = new HashMap();
        updateMap.put("name", newOwner.getName());
        updateMap.put("email", newOwner.getEmail());
        updateMap.put("phoneNumber", newOwner.getPhoneNumber());

        businessRef.update("owners", FieldValue.arrayUnion(updateMap)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hud.dismiss();
                //finish();

                Intent intent = new Intent(AddBusinessOwner.this, SelectBusinessOwner.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AddBusinessOwner.this.startActivity(intent);

            }
        });

    }

}
