package com.example.booktracker.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.booktracker.R;
import com.example.booktracker.databinding.FragmentBookDetailsBinding;
import com.example.booktracker.viewModel.BookViewModel;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class BookDetailsFragment extends Fragment {

    FragmentBookDetailsBinding binding;
    BookViewModel bookViewModel;
    NavController navController;
    DatabaseReference databaseReference;

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false);
        checkStatus();
        binding.delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
            }
        });
        binding.updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_bookDetailsFragment_to_bookUpdateFragment);
            }
        });
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
        seekProgress();
        binding.readbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    buttonView.setText("Read");
                    databaseReference = FirebaseDatabase.getInstance().
                            getReference("Books").child(getDefaults("id", requireContext())).child(bookViewModel.getBookModel().getId());
                    Map<String, Object> status = new HashMap<>();
                    status.put("Status", "Read");
                    databaseReference.updateChildren(status);
                } else {
                    buttonView.setText("Unread");
                    databaseReference = FirebaseDatabase.getInstance().
                            getReference("Books").child(getDefaults("id", requireContext())).child(bookViewModel.getBookModel().getId());
                    Map<String, Object> status = new HashMap<>();
                    status.put("Status", "UnRead");
                    databaseReference.updateChildren(status);

                }
            }
        });
    }

    public void checkStatus() {
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().
                getReference("Books").child(getDefaults("id", requireContext())).child(bookViewModel.getBookModel().getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Status").exists()) {
                    if (snapshot.child("Status").getValue(String.class).equals("Read")) {
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
        databaseReference = FirebaseDatabase.getInstance().
                getReference("Books").child(getDefaults("id", requireContext())).child(bookViewModel.getBookModel().getId());
        databaseReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                navController.navigate(R.id.action_bookDetailsFragment_to_homeFragment);
            }
        });

    }

    public void seekProgress() {
        binding.progress.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                databaseReference = FirebaseDatabase.getInstance().
                        getReference("Books").
                        child(getDefaults("id", requireContext())).child(bookViewModel.getBookModel().getId());
                Map<String, Object> track = new HashMap<>();
                track.put("Progress", slider.getValue());
                databaseReference.updateChildren(track);
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Progress").exists()) {

                    binding.progress.setValue(snapshot.child("Progress").getValue(Long.class));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}