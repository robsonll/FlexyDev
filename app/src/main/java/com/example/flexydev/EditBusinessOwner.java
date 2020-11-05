package com.example.flexydev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexydev.ControlerClasses.BusinessController;
import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.BusinessOwner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditBusinessOwner extends AppCompatActivity {

    private EditText editTextBusinessOwnerName;
    private EditText editTextBusinessOwnerEmail;
    private EditText editTextBusinessOwnerPhoneNumber;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_business_owner);

        editTextBusinessOwnerName = findViewById(R.id.editTextBusinessOwnerName);
        editTextBusinessOwnerEmail = findViewById(R.id.editTextBusinessOwnerEmail);
        editTextBusinessOwnerPhoneNumber = findViewById(R.id.editTextBusinessOwnerPhone);

        Button buttonEditBusinessOwner= findViewById(R.id.buttonEditBusinessOwner);

        BusinessController businessController = new BusinessController();

        BusinessOwner businessOwner = businessController.retrieveBusinessOwner(this);

        editTextBusinessOwnerName.setText(businessOwner.getName());
        editTextBusinessOwnerEmail.setText(businessOwner.getEmail());
        editTextBusinessOwnerPhoneNumber.setText(businessOwner.getPhoneNumber());

        //db = FirebaseFirestore.getInstance();


        buttonEditBusinessOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Validations
                if (editTextBusinessOwnerName.getText().toString().trim().equals("")) {
                    Toast.makeText(EditBusinessOwner.this, "Please enter business owners name.", Toast.LENGTH_SHORT).show();

                } else if (editTextBusinessOwnerEmail.getText().toString().trim().equals("")) {
                    Toast.makeText(EditBusinessOwner.this, "Please enter business owners email.", Toast.LENGTH_SHORT).show();

                } else {
                    updateBusinessOwner(v, businessOwner);
                }
            }
        });
    }

    public void updateBusinessOwner(View view, BusinessOwner businessOwner){

        db = FirebaseFirestore.getInstance();

        BusinessController businessController = new BusinessController();
        Business currentBusiness = businessController.retrieveBusiness(view.getContext());
        List<BusinessOwner> listBusinessOwners = currentBusiness.getOwners();

        final KProgressHUD hud = KProgressHUD.create(view.getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Updating Business Owner")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        // Removing old element

        Map<String, Object> oldData = new HashMap();
        oldData.put("name", businessOwner.getName());
        oldData.put("email", businessOwner.getEmail());
        oldData.put("phoneNumber", businessOwner.getPhoneNumber());

        DocumentReference businessRef = db.collection("business").document(currentBusiness.getId());

        businessRef.update("owners", FieldValue.arrayRemove(oldData));

        int i = 0;
        for(BusinessOwner owner : listBusinessOwners){
            if((owner.getName().equals(businessOwner.getName())) && (owner.getEmail().equals(businessOwner.getEmail())) && (owner.getPhoneNumber().equals(businessOwner.getPhoneNumber()))){
                break;
            }
            i++;
        }

        //listBusinessOwners.remove(businessOwner);
        listBusinessOwners.remove(i);

        // Adding new element with updated data

        String txtBusinessOwnerName = ((EditText)(findViewById(R.id.editTextBusinessOwnerName))).getText().toString();
        String txtBusinessOwnerEmail = ((EditText)(findViewById(R.id.editTextBusinessOwnerEmail))).getText().toString();
        String txtBusinessOwnerPhoneNumber = ((EditText)(findViewById(R.id.editTextBusinessOwnerPhone))).getText().toString();

        BusinessOwner updatedData = new BusinessOwner(txtBusinessOwnerName, txtBusinessOwnerEmail, txtBusinessOwnerPhoneNumber);

        Map<String, Object> newData = new HashMap();
        newData.put("name", updatedData.getName());
        newData.put("email", updatedData.getEmail());
        newData.put("phoneNumber", updatedData.getPhoneNumber());

        businessRef.update("owners", FieldValue.arrayUnion(newData));
        listBusinessOwners.add(updatedData);

        currentBusiness.setOwners(listBusinessOwners);
        businessController.storeBusiness(view.getContext(), currentBusiness);

        hud.dismiss();
        // finish();

        Intent intent = new Intent(EditBusinessOwner.this, SelectBusinessOwner.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        EditBusinessOwner.this.startActivity(intent);

    }

}
