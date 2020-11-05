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

import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectPaymentMethod extends Fragment {

    private FirebaseFirestore db;
    private TextView textViewTitle;
    private List<PaymentMethod> paymentMethodList;
    private SelectPaymentMethodAdapter selectPaymentMethodAdapter;
    private ListView listViewPaymentMethod;
    PaymentMethod currentPaymentMethod = new PaymentMethod();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_select_payment_method, container, false);

        db = FirebaseFirestore.getInstance();

        textViewTitle = view.findViewById(R.id.textViewTitle);
        listViewPaymentMethod = view.findViewById(R.id.listViewPaymentOptions);
        listViewPaymentMethod.setAdapter(selectPaymentMethodAdapter);

        listViewPaymentMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                currentPaymentMethod = (PaymentMethod) adapterView.getItemAtPosition(position);
            }
        });


        ImageView imageViewAddPaymentMethod = view.findViewById(R.id.imageViewAddPaymentMethod);

        imageViewAddPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddPaymentMethod.class));
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchPaymentMethods();
    }


    private void fetchPaymentMethods()
    {

        paymentMethodList = new ArrayList<PaymentMethod>();

        db.collection("paymentmethods")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                                paymentMethodList.add(document.toObject(PaymentMethod.class));

                            if (paymentMethodList.isEmpty())
                            {
                                Toast.makeText(getContext(), "No payment methods stored.", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                selectPaymentMethodAdapter = new SelectPaymentMethodAdapter(getContext(), paymentMethodList);
                                listViewPaymentMethod.setAdapter(selectPaymentMethodAdapter);
                            }
                        } else {
                            Log.d("FIREBASE", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
