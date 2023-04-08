package com.example.booktracker.Views;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.booktracker.R;
import com.example.booktracker.databinding.FragmentSettingsBinding;
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

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding  = FragmentSettingsBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        String loggedId = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(loggedId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.name.setText(snapshot.child("Name").getValue(String.class));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(requireContext(),Login.class));
                requireActivity().finish();
                setDefaults("Email", null, requireContext());
                setDefaults("Password", null, requireContext());
                setDefaults("id",null,requireContext());

            }
        });
        binding.fonsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                builder1.setMessage("What do you want to do?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Large",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                changeFontSize(150);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Normal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                changeFontSize(100);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        binding.color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                builder1.setMessage("What do you want to do?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Light red",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Window window = getActivity().getWindow();
                                window.setBackgroundDrawableResource(R.drawable.lightred);
                                setDefaults("color","red",requireContext());
                                dialog.cancel();
                            }
                        });
                builder1.setNeutralButton(
                        "Default",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Window window = getActivity().getWindow();
                                window.setBackgroundDrawableResource(R.drawable.lightcolor);
                                setDefaults("color","default",requireContext());
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "Light green",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Window window = getActivity().getWindow();
                                window.setBackgroundDrawableResource(R.drawable.lightgreen);
                                setDefaults("color","green",requireContext());
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
        return binding.getRoot();
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