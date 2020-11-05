package com.example.flexydev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flexydev.ControlerClasses.BusinessController;
import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.BusinessOwner;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SelectBusinessOwnerOld extends Fragment {

    private FirebaseFirestore db;
    private TextView textViewTitle;
    private List<BusinessOwner> businessOwnersList;
    private SelectBusinessOwnerAdapter selectBusinessOwnerAdapter;
    private ListView listViewBusinessOwners;
    BusinessOwner currentBusinessOwner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_select_business_owner, container, false);

        db = FirebaseFirestore.getInstance();

        textViewTitle = view.findViewById(R.id.textViewTitle);
        listViewBusinessOwners = view.findViewById(R.id.listViewBusinessOwners);
        listViewBusinessOwners.setAdapter(selectBusinessOwnerAdapter);

        listViewBusinessOwners.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                currentBusinessOwner = (BusinessOwner) adapterView.getItemAtPosition(position);
            }
        });


        ImageView imageViewAddBusinessOwner = view.findViewById(R.id.imageViewAddBusinessOwner);

        imageViewAddBusinessOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddBusinessOwner.class));
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchBusinessOwners();
    }


    private void fetchBusinessOwners()
    {

        BusinessController businessController = new BusinessController();
        Business business = businessController.retrieveBusiness(getContext());

        businessOwnersList = business.getOwners();

    }

}
