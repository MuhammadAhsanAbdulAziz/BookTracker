package com.example.booktracker.Views;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.example.booktracker.databinding.FragmentCreateBookBinding;
import com.example.booktracker.model.BookModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateBookFragment extends Fragment {

    FragmentCreateBookBinding binding;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Uri u;
    final Calendar myCalendar = Calendar.getInstance();
    SweetAlertDialog alertDialog;
    NavController navController;
    String loggedId;
    FirebaseAuth auth;

    public CreateBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCreateBookBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            loggedId = auth.getCurrentUser().getUid();
        }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().
                getReference("Books").child(loggedId);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };
        binding.datefield.setOnClickListener(v -> new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        binding.imgfield.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 404);
        });
        binding.BookAddBtn.setOnClickListener(v -> addBook());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.datefield.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void addBook() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait..");
        String Title = Objects.requireNonNull(binding.titlefield.getText()).toString().trim().toLowerCase(Locale.ROOT);
        String Author = Objects.requireNonNull(binding.Authorfield.getText()).toString().trim().toLowerCase(Locale.ROOT);
        String Genre = Objects.requireNonNull(binding.genrefield.getText()).toString().trim();
        String Date = Objects.requireNonNull(binding.datefield.getText()).toString().trim();


        if (u == null) {
            errorMessage("Enter Book Cover");
            return;
        }
        if (Title.isEmpty()) {
            binding.titlefield.setError("Enter Title");
            binding.titlefield.requestFocus();
            return;
        }
        if (Author.isEmpty()) {
            binding.Authorfield.setError("Enter Author Name");
            binding.Authorfield.requestFocus();
            return;
        }
        if (Genre.isEmpty()) {
            binding.genrefield.setError("Enter Genre");
            binding.genrefield.requestFocus();
            return;
        }
        if (Date.isEmpty()) {
            binding.datefield.setError("Enter Date");
            binding.datefield.requestFocus();
            return;
        }
        // for registering
        String uniqueID = UUID.randomUUID().toString();
        final StorageReference ref = storageReference.child("Book Images/" + u.getLastPathSegment());
        ref.putFile(u).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> databaseReference.child(uniqueID).
                setValue(new BookModel(uniqueID, Title, Author, Genre, Date, uri.toString()))));
        SuccessMessage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 404) {
            if (data != null) {
                u = data.getData();
                binding.imgfield.setImageURI(u);
            }
        }
    }

    public void SuccessMessage() {
        alertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE).setTitleText("IMPORTANT").
                setContentText("Book Added Successfully").setConfirmText("Okay").setConfirmClickListener(sweetAlertDialog -> {
                    navController.navigate(R.id.action_createBookFragment_to_homeFragment);
                    alertDialog.cancel();
                    alertDialog.dismiss();
                });
        alertDialog.show();

    }

    public void errorMessage(String msg) {

        new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(msg).show();
    }

}