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

import com.example.flexydev.ControlerClasses.PaymentMethodController;
import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.kaopiz.kprogresshud.KProgressHUD;

public class EditPaymentMethod extends AppCompatActivity {

    private EditText editTextPMName;
    private EditText editTextPMDescription;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment_method);

        editTextPMName = findViewById(R.id.editTextPMName);
        editTextPMDescription = findViewById(R.id.editTextPMDesc);
        Button buttonEditPaymentMethod= findViewById(R.id.buttonEditPaymentMethod);

        PaymentMethodController pmController = new PaymentMethodController();

        PaymentMethod paymentMethod = pmController.retrievePaymentMethod(this);

        editTextPMName.setText(paymentMethod.getName());
        editTextPMDescription.setText(paymentMethod.getDescription());

        db = FirebaseFirestore.getInstance();


        buttonEditPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validations
                if (editTextPMName.getText().toString().trim().equals("")) {
                    Toast.makeText(EditPaymentMethod.this, "Please enter payment option name.", Toast.LENGTH_SHORT).show();

                } else if (editTextPMDescription.getText().toString().trim().equals("")) {
                    Toast.makeText(EditPaymentMethod.this, "Please enter payment option description.", Toast.LENGTH_SHORT).show();

                } else {

                    updateUser(paymentMethod);

                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SelectPaymentMethod()).commit();

                }
            }
        });
    }

    public void updateUser(PaymentMethod paymentMethod){

        final KProgressHUD hud = KProgressHUD.create(EditPaymentMethod.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Inserting Payment Method")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        String txtPMName = ((EditText)(findViewById(R.id.editTextPMName))).getText().toString();
        String txtPMDescription = ((EditText)(findViewById(R.id.editTextPMDesc))).getText().toString();

        //paymentMethod.setName(txtPMName);
        //paymentMethod.setDescription(txtPMDescription);

        //db.collection("paymentmethods").document(paymentMethod.getId()).set(paymentMethod);

        DocumentReference paymentMethodRef = db.collection("paymentmethods").document(paymentMethod.getId());

        paymentMethodRef
                .update("name",txtPMName,"description",txtPMDescription)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditPaymentMethod.this, "Payment Method Updated !",
                                Toast.LENGTH_SHORT).show();

                        hud.dismiss();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPaymentMethod.this, "Error !",
                                Toast.LENGTH_SHORT).show();
                    }
                });


/*
        WriteBatch batch = db.batch();

        DocumentReference userData = db.collection("paymentmethods").document(paymentMethod.getId());

        batch.update(userData, "name", txtPMName);
        batch.update(userData, "description", txtPMDescription);

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditPaymentMethod.this, "Payment Method Updated !",
                        Toast.LENGTH_SHORT).show();
            }
        });
*/



    }

}
