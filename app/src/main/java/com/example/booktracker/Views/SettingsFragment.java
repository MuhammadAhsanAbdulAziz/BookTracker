package com.example.booktracker.Views;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booktracker.R;
import com.example.booktracker.databinding.FragmentSettingsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsFragment extends Fragment {
    FragmentSettingsBinding binding;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    String loggedId;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding  = FragmentSettingsBinding.inflate(inflater,container,false);
        if (auth.getCurrentUser() != null) {
            loggedId = auth.getCurrentUser().getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(loggedId);

        setName();

        binding.logout.setOnClickListener(v -> logout());
        binding.fontsize.setOnClickListener(v -> fontsize());
        binding.color.setOnClickListener(v -> changeColor());
        return binding.getRoot();
    }

    private void setName() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.name.setText(snapshot.child("Name").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void changeColor() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
        builder1.setMessage("What do you want to do?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Light red",
                (dialog, id) -> {
                    BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
                    bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.red));
                    setDefaults("color","red",requireContext());
                    dialog.cancel();
                });
        builder1.setNeutralButton(
                "Default",
                (dialog, id) -> {
                    BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
                    bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.myTheme));
                    setDefaults("color","default",requireContext());
                    dialog.cancel();
                });
        builder1.setNegativeButton(
                "Light green",
                (dialog, id) -> {
                    BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav);
                    bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.green));
                    setDefaults("color","green",requireContext());
                    dialog.cancel();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
    private void fontsize() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
        builder1.setMessage("What do you want to do?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Large",
                (dialog, id) -> {
                    changeFontSize(150);
                    dialog.cancel();
                });

        builder1.setNegativeButton(
                "Normal",
                (dialog, id) -> {
                    changeFontSize(100);
                    dialog.cancel();
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
    private void logout() {
        auth.signOut();
        startActivity(new Intent(requireContext(),Login.class));
        requireActivity().finish();
        setDefaults("Email", null, requireContext());
        setDefaults("Password", null, requireContext());
        setDefaults("id",null,requireContext());
    }
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    private void changeFontSize(int size) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setFontSize(size);
        }
    }
}