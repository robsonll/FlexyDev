package com.example.flexydev;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flexydev.ControlerClasses.PaymentMethodController;
import com.example.flexydev.ModelClasses.PaymentMethod;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class SelectPaymentMethodAdapter extends ArrayAdapter<PaymentMethod> {

    Context context;
    List<PaymentMethod> paymentMethodsData = new ArrayList<PaymentMethod>();
    private FirebaseFirestore db;

    public SelectPaymentMethodAdapter(Context context, List<PaymentMethod> paymentMethods) {
        super(context, R.layout.select_payment_method_adapter);

        this.paymentMethodsData = paymentMethods;
        this.context = context;
    }

    @Override
    public int getCount() {
        return paymentMethodsData.size();
    }

    @Override
    public PaymentMethod getItem(int position) {
        return paymentMethodsData.get(position);
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
                    .from(parent.getContext()).inflate(R.layout.select_payment_method_adapter,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.textViewPMName = convertView.findViewById(R.id.poName);
            viewHolder.imageViewEditPaymentMethod = convertView.findViewById(R.id.imageViewEditPaymentMethod);
            viewHolder.imageViewDeletePaymentMethod = convertView.findViewById(R.id.imageViewDeletePaymentMethod);

            final PaymentMethod paymentMethod = paymentMethodsData.get(position);

            viewHolder.imageViewEditPaymentMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "Edit." + paymentMethod.getName(), Toast.LENGTH_LONG).show();

                    PaymentMethodController pmController = new PaymentMethodController();

                    pmController.storePaymentMethod(context, paymentMethod);

                    Intent intent = new Intent(context, EditPaymentMethod.class);
                    context.startActivity(intent);
                }
            });

            viewHolder.imageViewDeletePaymentMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletePaymentMethod(v, paymentMethod);
                }
            });

        }

        TextView textViewPaymentOptionName = convertView.findViewById(R.id.poName);
        ImageView imageViewEditPaymentMethod = convertView.findViewById(R.id.imageViewEditPaymentMethod);
        textViewPaymentOptionName.setText(paymentMethodsData.get(position).getName());

        return convertView;
    }

    public void deletePaymentMethod(View v, PaymentMethod paymentMethod){

        db = FirebaseFirestore.getInstance();

        final KProgressHUD hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Deleting Payment Method")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
        

        db.collection("paymentmethods").document(paymentMethod.getId())
                .delete().addOnSuccessListener(new OnSuccessListener< Void >() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Payment Method deleted !",
                        Toast.LENGTH_SHORT).show();

                hud.dismiss();

                paymentMethodsData.remove(paymentMethod);
                notifyDataSetChanged();

            }
        });


    }

    private static class ViewHolder{
        TextView textViewPMName;
        ImageView imageViewEditPaymentMethod;
        ImageView imageViewDeletePaymentMethod;
    }

}
