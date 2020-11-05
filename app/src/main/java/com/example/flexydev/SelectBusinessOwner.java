package com.example.flexydev;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.flexydev.ControlerClasses.BusinessController;
import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.BusinessOwner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectBusinessOwner extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView textViewTitle;
    private List<BusinessOwner> businessOwnersList;
    private SelectBusinessOwnerAdapter selectBusinessOwnerAdapter;
    private ListView listViewBusinessOwners;
    BusinessOwner currentBusinessOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_business_owner);

        db = FirebaseFirestore.getInstance();

        fetchBusinessOwners();

        selectBusinessOwnerAdapter = new SelectBusinessOwnerAdapter(getApplicationContext(), businessOwnersList);

        textViewTitle = findViewById(R.id.textViewTitle);
        listViewBusinessOwners = (ListView) findViewById(R.id.listViewBusinessOwners);
        listViewBusinessOwners.setAdapter(selectBusinessOwnerAdapter);

        listViewBusinessOwners.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                currentBusinessOwner = (BusinessOwner) adapterView.getItemAtPosition(position);
            }
        });


        ImageView imageViewAddBusinessOwner = findViewById(R.id.imageViewAddBusinessOwner);

        imageViewAddBusinessOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectBusinessOwner.this, AddBusinessOwner.class));
            }
        });

    }

    private void fetchBusinessOwners()
    {

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveBusiness(SelectBusinessOwner.this);

        if(business.getOwners() != null)
            businessOwnersList = business.getOwners();
        else
            businessOwnersList = new ArrayList<BusinessOwner>();

    }



}
