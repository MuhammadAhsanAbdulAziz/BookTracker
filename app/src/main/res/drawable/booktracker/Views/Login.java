package com.example.booktracker.Views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {
    EditText loginEmailField, loginPasswordField;
    Button loginbtn, registerbtn;
    FirebaseAuth auth;
    SweetAlertDialog alertDialog;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);

        if (getDefaults("Email", Login.this) != null && getDefaults("Password", Login.this) != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }

        auth = FirebaseAuth.getInstance();
        loginEmailField = findViewById(R.id.loginEmailField);
        loginPasswordField = findViewById(R.id.loginPasswordField);
        loginbtn = findViewById(R.id.loginbtn);
        registerbtn = findViewById(R.id.registerbtn);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        String Email = loginEmailField.getText().toString().trim();
        String Password = loginPasswordField.getText().toString().trim();

        if (Email.isEmpty()) {
            loginEmailField.setError("Enter Email");
            loginEmailField.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            loginEmailField.setError("Enter valid Email");
            loginEmailField.requestFocus();
            return;
        }
        if (Password.isEmpty()) {
            loginPasswordField.setError("Enter Password");
            loginPasswordField.requestFocus();
            return;
        }
        if (Password.length() < 6) {
            loginPasswordField.setError("Password must be more than 6 characters");
            loginPasswordField.requestFocus();
            return;
        }
        progressDialog.show();
        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    setDefaults("Email", Email, Login.this);
                    setDefaults("Password", Password, Login.this);
                    setDefaults("id", user.getUid(), Login.this);
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                }
                else
                {
                    progressDialog.dismiss();
                    errorMessage("Invalid Details");
                }
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