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
import com.example.flexydev.ControlerClasses.PaymentMethodController;
import com.example.flexydev.ModelClasses.Business;
import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class SelectBusinessAdapter extends ArrayAdapter<Business> {

    Context context;
    List<Business> businessData = new ArrayList<Business>();
    private FirebaseFirestore db;

    public SelectBusinessAdapter(Context context, List<Business> businesses) {
        super(context, R.layout.select_business_adapter);

        this.businessData = businesses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return businessData.size();
    }

    @Override
    public Business getItem(int position) {
        return businessData.get(position);
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
                    .from(parent.getContext()).inflate(R.layout.select_business_adapter,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.textViewBusinessName = convertView.findViewById(R.id.businessName);
            viewHolder.imageViewEditBusiness = convertView.findViewById(R.id.imageViewEditBusiness);

            final Business business = businessData.get(position);

            viewHolder.imageViewEditBusiness.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "Edit." + paymentMethod.getName(), Toast.LENGTH_LONG).show();

                    BusinessController businessController = new BusinessController();

                    businessController.storeBusiness(context, business);

                    Intent intent = new Intent(context, EditBusiness.class);
                    context.startActivity(intent);
                }
            });

        }

        TextView textViewBusinessName = convertView.findViewById(R.id.businessName);
        ImageView imageViewEditBusiness = convertView.findViewById(R.id.imageViewEditBusiness);
        textViewBusinessName.setText(businessData.get(position).getName());

        return convertView;
    }


    private static class ViewHolder{
        TextView textViewBusinessName;
        ImageView imageViewEditBusiness;
    }

}
