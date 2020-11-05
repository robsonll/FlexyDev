package com.example.flexydev;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.BusinessOwner;
import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class AddBusiness extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextUrl;
    private EditText editTextAdress;
    private EditText editTextPhoneNumber;
    private EditText editTextEmail;
    private Boolean businessStatus;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);

        editTextName = findViewById(R.id.editTextBusinessName);
        editTextUrl = findViewById(R.id.editTextBusinessURL);
        editTextAdress = findViewById(R.id.editTextBusinessAddress);
        editTextPhoneNumber = findViewById(R.id.editTextBusinessPhone);
        editTextEmail = findViewById(R.id.editTextBusinessEmail);

        Button buttonAddBusiness= findViewById(R.id.buttonAddBusiness);
        Button buttonAddOwners= findViewById(R.id.buttonAddOwners);

        db = FirebaseFirestore.getInstance();


        buttonAddBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validations
                if (editTextName.getText().toString().trim().equals("")) {
                    Toast.makeText(AddBusiness.this, "Please enter Business name.", Toast.LENGTH_SHORT).show();

                } else {

                    createBusiness();
                }
            }
        });

        buttonAddOwners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(AddBusiness.this, AddBusinessOwner.class));
            }
        });

    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch(view.getId()) {
            case R.id.businessStatusTrue:
                if (checked)
                    businessStatus = true;
                    break;
            case R.id.businessStatusFalse:
                if (checked)
                    businessStatus = false;
                    break;
        }
    }

    private void createBusiness() {

        Business newBusiness = new Business(null,
                editTextName.getText().toString().trim(),
                editTextUrl.getText().toString().trim(),
                editTextAdress.getText().toString().trim(),
                editTextPhoneNumber.getText().toString().trim(),
                editTextEmail.getText().toString().trim(),
                null,
                businessStatus
                );

        //Start HUD
        final KProgressHUD hud = KProgressHUD.create(AddBusiness.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Inserting Business")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        db.collection("business")
                .add(newBusiness)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(getPackageName(), "DocumentSnapshot added with ID: " + documentReference.getId());
                        documentReference.update("id",documentReference.getId());
                        hud.dismiss();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(getPackageName(), "Error adding business", e);
                    }
                });

    }

}
