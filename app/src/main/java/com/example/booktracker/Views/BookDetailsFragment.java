package com.example.booktracker.Views;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.example.booktracker.R;
import com.example.booktracker.databinding.FragmentBookDetailsBinding;
import com.example.booktracker.viewModel.BookViewModel;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BookDetailsFragment extends Fragment {

    FragmentBookDetailsBinding binding;
    FirebaseAuth auth;
    BookViewModel bookViewModel;
    NavController navController;
    DatabaseReference databaseReference;
    String loggedId;

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false);
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            loggedId = auth.getCurrentUser().getUid();
        }
        databaseReference = FirebaseDatabase.getInstance().
                getReference("Books").child(loggedId)
                .child(bookViewModel.getBookModel().getId());

        checkStatus();
        seekProgress();

        binding.delbtn.setOnClickListener(v -> deleteBook());
        binding.updatebtn.setOnClickListener(v -> navController.navigate(R.id.action_bookDetailsFragment_to_bookUpdateFragment));
        binding.readbtn.setOnCheckedChangeListener((buttonView, isChecked) -> setReadBtn(buttonView));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        Glide.with(requireContext()).load(bookViewModel.getBookModel().getPicture()).
                error(R.drawable.ic_launcher_background).dontAnimate().into(binding.bookimg);
        binding.setBook(bookViewModel);

    }

    public void checkStatus() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Status").exists()) {
                    if (Objects.equals(snapshot.child("Status").getValue(String.class), "Read")) {
                        binding.readbtn.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void deleteBook() {
        databaseReference.removeValue((error, ref) -> navController.navigate(R.id.action_bookDetailsFragment_to_homeFragment));

    }

    public void seekProgress() {
        binding.progress.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                Map<String, Object> track = new HashMap<>();
                track.put("Progress", slider.getValue());
                databaseReference.updateChildren(track);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Progress").exists()) {
                    Long val = snapshot.child("Progress").getValue(Long.class);
                    binding.progress.setValue(val);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    void setReadBtn(CompoundButton btn) {
        if (btn.isChecked()) {
            btn.setText("Read");
            Map<String, Object> status = new HashMap<>();
            status.put("Status", "Read");
            databaseReference.updateChildren(status);
        } else {
            btn.setText("Unread");
            Map<String, Object> status = new HashMap<>();
            status.put("Status", "UnRead");
            databaseReference.updateChildren(status);
        }
    }

}