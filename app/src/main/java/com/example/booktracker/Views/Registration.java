package com.example.booktracker.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;

import com.example.booktracker.databinding.ActivityRegistrationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Registration extends AppCompatActivity {

    ActivityRegistrationBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    SweetAlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        binding.regSubmitBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        String Name = Objects.requireNonNull(binding.regNameField.getText()).toString().trim();
        String Email = Objects.requireNonNull(binding.regEmailField.getText()).toString().trim();
        String Password = Objects.requireNonNull(binding.regPasswordField.getText()).toString().trim();


        if (Name.isEmpty()) {
            binding.regNameField.setError("Enter Name");
            binding.regNameField.requestFocus();
            return;
        }
        if (Email.isEmpty()) {
            binding.regEmailField.setError("Enter Email");
            binding.regEmailField.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            binding.regEmailField.setError("Enter valid Email");
            binding.regEmailField.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            binding.regPasswordField.setError("Enter Password");
            binding.regPasswordField.requestFocus();
            return;
        }
        if (Password.length() < 6) {
            binding.regPasswordField.setError("Password must be more than 6 characters");
            binding.regPasswordField.requestFocus();
            return;
        }
        progressDialog.show();
        auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                Map<String, Object> user = new HashMap<>();
                user.put("Name", Name);
                user.put("Email", Email);
                databaseReference.child(auth.getCurrentUser().getUid())
                        .setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                warningMessage();
                            } else {
                                errorMessage("Failed to Register! Try Again");
                            }

                        });
            } else {
                progressDialog.dismiss();
                errorMessage("Email already exist");
            }
        });


    }

    public void warningMessage() {
        alertDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("IMPORTANT").
                setContentText("Acount Created Successfully").setConfirmText("Okay").setConfirmClickListener(sweetAlertDialog -> {
                    alertDialog.cancel();
                    alertDialog.dismiss();
                    startActivity(new Intent(Registration.this, Login.class));
                    finish();
                });
        alertDialog.show();

    }

    public void errorMessage(String msg) {

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(msg).show();
    }

}