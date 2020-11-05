package com.example.flexydev;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flexydev.ControlerClasses.BusinessController;
import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.BusinessOwner;
import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectBusinessOwnerAdapter extends ArrayAdapter<BusinessOwner> {

    Context context;
    List<BusinessOwner> businessOwnersData = new ArrayList<BusinessOwner>();
    private FirebaseFirestore db;

    public SelectBusinessOwnerAdapter(Context context, List<BusinessOwner> businessOwners) {
        super(context, R.layout.select_business_owner_adapter);

        this.businessOwnersData = businessOwners;
        this.context = context;
    }

    @Override
    public int getCount() {
        return businessOwnersData.size();
    }

    @Override
    public BusinessOwner getItem(int position) {
        return businessOwnersData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = LayoutInflater
                    .from(parent.getContext()).inflate(R.layout.select_business_owner_adapter,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.textViewBusinessOwnerName = convertView.findViewById(R.id.boName);
            viewHolder.imageViewEditBusinessOwner = convertView.findViewById(R.id.imageViewEditBusinessOwner);
            viewHolder.imageViewDeleteBusinessOwner = convertView.findViewById(R.id.imageViewDeleteBusinessOwner);

            final BusinessOwner businessOwner = businessOwnersData.get(position);

            viewHolder.imageViewEditBusinessOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Edit." + businessOwner.getName(), Toast.LENGTH_LONG).show();

                    BusinessController businessController = new BusinessController();

                    businessController.storeBusinessOwner(context, businessOwner);

                    Intent intent = new Intent(context, EditBusinessOwner.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            viewHolder.imageViewDeleteBusinessOwner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteBusinessOwner(v, businessOwner);
                }
            });



        }

        TextView textViewBusinessOwnerName = convertView.findViewById(R.id.boName);
        ImageView imageViewEditBusinessOwner = convertView.findViewById(R.id.imageViewEditBusinessOwner);
        ImageView imageViewDeleteBusinessOwner = convertView.findViewById(R.id.imageViewEditBusinessOwner);
        textViewBusinessOwnerName.setText(businessOwnersData.get(position).getName());

        return convertView;
    }


    private static class ViewHolder{
        TextView textViewBusinessOwnerName;
        ImageView imageViewEditBusinessOwner;
        ImageView imageViewDeleteBusinessOwner;
    }

    public void deleteBusinessOwner(View v, BusinessOwner businessOwner){

        db = FirebaseFirestore.getInstance();

        BusinessController businessController = new BusinessController();
        Business currentBusiness = businessController.retrieveBusiness(v.getContext());

        final KProgressHUD hud = KProgressHUD.create(v.getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Deleting Business Owner")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();


        DocumentReference businessRef = db.collection("business").document(currentBusiness.getId());

        Map<String, Object> updateMap = new HashMap();
        updateMap.put("name", businessOwner.getName());
        updateMap.put("email", businessOwner.getEmail());
        updateMap.put("phoneNumber", businessOwner.getPhoneNumber());

        businessRef.update("owners", FieldValue.arrayRemove(updateMap));

        hud.dismiss();
        businessOwnersData.remove(businessOwner);

        currentBusiness.setOwners(businessOwnersData);
        businessController.storeBusiness(v.getContext(), currentBusiness);

        notifyDataSetChanged();

    }


}
