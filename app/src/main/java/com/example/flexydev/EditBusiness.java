package com.example.flexydev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexydev.ControlerClasses.BusinessController;
import com.example.flexydev.ControlerClasses.PaymentMethodController;
import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

public class EditBusiness extends AppCompatActivity {

    private EditText editTextBusinessName;
    private EditText editTextBusinessURL;
    private EditText editTextBusinessAddress;
    private EditText editTextBusinessPhoneNumber;
    private EditText editTextBusinessEmail;
    private RadioButton businessStatusTrue;
    private RadioButton businessStatusFalse;
    private Boolean businessStatus;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_business);

        editTextBusinessName = findViewById(R.id.editTextBusinessName);
        editTextBusinessURL = findViewById(R.id.editTextBusinessURL);
        editTextBusinessAddress = findViewById(R.id.editTextBusinessAddress);
        editTextBusinessPhoneNumber = findViewById(R.id.editTextBusinessPhone);
        editTextBusinessEmail = findViewById(R.id.editTextBusinessEmail);
        businessStatusTrue = findViewById(R.id.businessStatusTrue);
        businessStatusFalse = findViewById(R.id.businessStatusFalse);

        Button buttonEditBusiness= findViewById(R.id.buttonEditBusiness);

        BusinessController businessController = new BusinessController();

        Business business = businessController.retrieveBusiness(this);

        editTextBusinessName.setText(business.getName());
        editTextBusinessURL.setText(business.getUrl());
        editTextBusinessAddress.setText(business.getAddress());
        editTextBusinessPhoneNumber.setText(business.getPhoneNumber());
        editTextBusinessEmail.setText(business.getEmail());

        if((business.getActive() == null) || (business.getActive() == false)) {
            businessStatusFalse.setChecked(true);
            businessStatus = false;
        }else {
            businessStatusTrue.setChecked(true);
            businessStatus = true;
        }

        db = FirebaseFirestore.getInstance();


        buttonEditBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validations
                if (editTextBusinessName.getText().toString().trim().equals("")) {
                    Toast.makeText(EditBusiness.this, "Please enter business name.", Toast.LENGTH_SHORT).show();

                } else if (editTextBusinessEmail.getText().toString().trim().equals("")) {
                    Toast.makeText(EditBusiness.this, "Please enter business email.", Toast.LENGTH_SHORT).show();

                } else {
                    updateBusiness(business);
                }
            }
        });

        Button buttonEditBusinessOwners= findViewById(R.id.buttonEditOwners);

        buttonEditBusinessOwners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_edit_business, new SelectBusinessOwner()).commit();

                startActivity(new Intent(EditBusiness.this, SelectBusinessOwner.class));

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

    public void updateBusiness(Business business){

        final KProgressHUD hud = KProgressHUD.create(EditBusiness.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Inserting Business")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        String txtBusinessName = ((EditText)(findViewById(R.id.editTextBusinessName))).getText().toString();
        String txtBusinessURL = ((EditText)(findViewById(R.id.editTextBusinessURL))).getText().toString();
        String txtBusinessAddress = ((EditText)(findViewById(R.id.editTextBusinessAddress))).getText().toString();
        String txtBusinessPhoneNumber = ((EditText)(findViewById(R.id.editTextBusinessPhone))).getText().toString();
        String txtBusinessEmail = ((EditText)(findViewById(R.id.editTextBusinessEmail))).getText().toString();

        DocumentReference paymentMethodRef = db.collection("business").document(business.getId());

        paymentMethodRef
                .update("name",txtBusinessName,
                        "url",txtBusinessURL,
                        "address",txtBusinessAddress,
                        "phoneNumber", txtBusinessPhoneNumber,
                        "email", txtBusinessEmail,
                        "active",businessStatus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditBusiness.this, "Business Updated !",
                                Toast.LENGTH_SHORT).show();

                        hud.dismiss();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditBusiness.this, "Error !",
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

}
