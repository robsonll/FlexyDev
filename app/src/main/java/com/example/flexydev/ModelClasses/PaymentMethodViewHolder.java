package com.example.flexydev.ModelClasses;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PaymentMethodViewHolder {

    ImageView thumbnail;
    TextView title;
    Button editButton;
    Button deleteButton;

    public ImageView getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageView thumbnail) {
        this.thumbnail = thumbnail;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public Button getEditButton() {
        return editButton;
    }

    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }
}
