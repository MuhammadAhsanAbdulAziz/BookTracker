package com.example.booktracker.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;


import com.example.booktracker.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getDefaults("Email", Login.this) != null && getDefaults("Password", Login.this) != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        binding.registerbtn.setOnClickListener(v -> startActivity(new Intent(Login.this, Registration.class)));

        binding.loginbtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        String Email = Objects.requireNonNull(binding.loginEmailField.getText()).toString().trim();
        String Password = Objects.requireNonNull(binding.loginPasswordField.getText()).toString().trim();

        if (Email.isEmpty()) {
            binding.loginEmailField.setError("Enter Email");
            binding.loginEmailField.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            binding.loginEmailField.setError("Enter valid Email");
            binding.loginEmailField.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            binding.loginPasswordField.setError("Enter Password");
            binding.loginPasswordField.requestFocus();
            return;
        }
        if (Password.length() < 6) {
            binding.loginPasswordField.setError("Password must be more than 6 characters");
            binding.loginPasswordField.requestFocus();
            return;
        }
        progressDialog.show();
        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressDialog.dismiss();
                setDefaults("Email", Email, Login.this);
                setDefaults("Password", Password, Login.this);
                startActivity(new Intent(Login.this, MainActivity.class));
                finish();
            }
            else
            {
                progressDialog.dismiss();
                errorMessage("Invalid Details");
            }
        });
    }

    public void errorMessage(String msg) {

        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(msg).show();
    }


    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}