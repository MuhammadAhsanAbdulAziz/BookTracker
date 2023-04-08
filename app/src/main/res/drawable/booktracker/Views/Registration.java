package com.example.booktracker.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.booktracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Registration extends AppCompatActivity {

    private EditText regNameField, regEmailField, regPasswordField;
    Button regSubmitBtn;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    SweetAlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        regNameField = findViewById(R.id.regNameField);
        regEmailField = findViewById(R.id.regEmailField);
        regPasswordField = findViewById(R.id.regPasswordField);
        regSubmitBtn = findViewById(R.id.regSubmitBtn);
        regSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        String Name = regNameField.getText().toString().trim();
        String Email = regEmailField.getText().toString().trim();
        String Password = regPasswordField.getText().toString().trim();


        if (Name.isEmpty()) {
            regNameField.setError("Enter Name");
            regNameField.requestFocus();
            return;
        }
        if (Email.isEmpty()) {
            regEmailField.setError("Enter Email");
            regEmailField.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            regEmailField.setError("Enter valid Email");
            regEmailField.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            regPasswordField.setError("Enter Password");
            regPasswordField.requestFocus();
            return;
        }
        if (Password.length() < 6) {
            regPasswordField.setError("Password must be more than 6 characters");
            regPasswordField.requestFocus();
            return;
        }
        progressDialog.show();
        auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Map<String, Object> user = new HashMap<>();
                    user.put("Name", Name);
                    user.put("Email", Email);
                    databaseReference.child(auth.getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        warningMessage();
                                    } else {
                                        errorMessage("Failed to Register! Try Again");
                                    }

                                }

                            });
                } else {
                    progressDialog.dismiss();
                    errorMessage("Email already exist");
                }
            }
        });


    }

    public void warningMessage() {
        alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("IMPORTANT").
                setContentText("Acount Created Successfully").setConfirmText("Okay").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        alertDialog.cancel();
                        alertDialog.dismiss();
                        startActivity(new Intent(Registration.this, Login.class));
                        finish();
                    }
                });
        alertDialog.show();

    }

    public void errorMessage(String msg) {

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(msg).show();
    }

}