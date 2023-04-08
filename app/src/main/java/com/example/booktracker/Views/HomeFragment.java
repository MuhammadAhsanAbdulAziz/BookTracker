package com.example.booktracker.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booktracker.R;
import com.example.booktracker.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    NavController navController;
    DatabaseReference databaseReference;
    FragmentHomeBinding binding;
    int Count = 0;
    String loggedId;
    FirebaseAuth auth;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            loggedId = auth.getCurrentUser().getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().
                getReference("Books").child(loggedId);
        count();


        binding.addbook.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_createBookFragment));

        binding.viewBook.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_viewBookFragment));
        binding.searchbook.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_searchBookFragment));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    public void count()
    {
        Count = 0;
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Count++;
                }
                binding.bookcount.setText(Count+" books");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}