package com.example.flexydev;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flexydev.ControlerClasses.AdminController;
import com.example.flexydev.ModelClasses.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonSignUp;
    private FirebaseAuth mAuth;

    private String TAG = "SignUpActivity";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidations();
            }
        });

    }



    //Button methods
    void checkValidations() {

        //Check for empty fields and then signup

        if (editTextName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter name.", Toast.LENGTH_SHORT).show();

        } else if (editTextEmail.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter email address.", Toast.LENGTH_SHORT).show();

        } else if (!isValidEmail(editTextEmail.getText().toString())) {
            Toast.makeText(this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();

        } else if (editTextPassword.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter password.", Toast.LENGTH_SHORT).show();

        } else if (editTextPassword.getText().toString().trim().length() < 7) {
            Toast.makeText(this, "Password should be atleast 7 digits.", Toast.LENGTH_SHORT).show();

        } else if (editTextConfirmPassword.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Please enter confirm password.", Toast.LENGTH_SHORT).show();

        } else if (!editTextPassword.getText().toString().trim().equals((editTextConfirmPassword.getText().toString().trim()))) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();

        } else {

            //Firebase save data and upload image
            signUp();
        }
    }


    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void signUp()
    {

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASE :", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            verifyUser(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void verifyUser(FirebaseUser fbUser){

        final String userUid = fbUser.getUid();
        final String userName = fbUser.getDisplayName();
        final String userEmail = fbUser.getEmail();

        final AdminController adminController = new AdminController();

        db = FirebaseFirestore.getInstance();

        CollectionReference buyersReference = db.collection("admin");
        Query usersDataQuery = buyersReference.whereEqualTo("email", userEmail);
        usersDataQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    if (!task.getResult().isEmpty()){
                        // User exists in Firebase Authenticaton and in the database
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Admin user = document.toObject(Admin.class);

                            adminController.storeUser(getApplicationContext(), user);

                            startActivity(new Intent(SignUpActivity.this, NavigationMainActivity.class));

                        }
                    }else{
                        // User exists in Firebase Authenticaton but not in the database. Thus, save user in firebase users document
                        Toast.makeText(SignUpActivity.this, "User not registered as a FLEXY Admin. Contact FLEXY Adm.", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(SignUpActivity.this, "User not registered as a FLEXY Admin. Contact FLEXY Adm.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
