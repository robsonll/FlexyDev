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
import androidx.fragment.app.Fragment;

import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.BusinessOwner;
import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectBusiness extends Fragment {

    private FirebaseFirestore db;
    private TextView textViewTitle;
    private List<Business> businessList;
    private SelectBusinessAdapter selectBusinessAdapter;
    private ListView listViewBusiness;
    Business currentBusiness;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_select_business, container, false);

        db = FirebaseFirestore.getInstance();

        textViewTitle = view.findViewById(R.id.textViewTitle);
        listViewBusiness = view.findViewById(R.id.listViewBusinesses);
        listViewBusiness.setAdapter(selectBusinessAdapter);

        listViewBusiness.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                currentBusiness = (Business) adapterView.getItemAtPosition(position);
            }
        });


        ImageView imageViewAddBusiness = view.findViewById(R.id.imageViewAddBusiness);

        imageViewAddBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddBusiness.class));
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchBusinesses();
    }


    private void fetchBusinesses()
    {

        businessList = new ArrayList<Business>();

        db.collection("business")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                                businessList.add(document.toObject(Business.class));

                            if (businessList.isEmpty())
                            {
                                Toast.makeText(getContext(), "No businesses stored.", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                selectBusinessAdapter = new SelectBusinessAdapter(getContext(), businessList);
                                listViewBusiness.setAdapter(selectBusinessAdapter);
                            }
                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
