package com.example.flexydev;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

public class AddPaymentMethod extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_method);

        editTextName = findViewById(R.id.editTextPMName);
        editTextDescription = findViewById(R.id.editTextPMDesc);
        Button buttonAddPaymentMethod= findViewById(R.id.buttonAddPaymentMethod);

        db = FirebaseFirestore.getInstance();


        buttonAddPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validations
                if (editTextName.getText().toString().trim().equals("")) {
                    Toast.makeText(AddPaymentMethod.this, "Please enter payment option name.", Toast.LENGTH_SHORT).show();

                } else if (editTextDescription.getText().toString().trim().equals("")) {
                    Toast.makeText(AddPaymentMethod.this, "Please enter payment option description.", Toast.LENGTH_SHORT).show();

                } else {
                    createPaymentMethod();
                }
            }
        });
    }

    private void createPaymentMethod() {

        PaymentMethod newPaymentMethod = new PaymentMethod(null, editTextName.getText().toString().trim(), editTextDescription.getText().toString().trim());

        //Start HUD
        final KProgressHUD hud = KProgressHUD.create(AddPaymentMethod.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Inserting Payment Method")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        db.collection("paymentmethods")
                .add(newPaymentMethod)
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
                        Log.w(getPackageName(), "Error adding document", e);
                    }
                });

        //startActivity(new Intent(AddAddress.this, AddOnsActivity.class));
    }

}
