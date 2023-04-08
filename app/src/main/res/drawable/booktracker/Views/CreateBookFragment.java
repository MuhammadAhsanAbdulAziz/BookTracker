package com.example.booktracker.Views;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.booktracker.R;
import com.example.booktracker.databinding.FragmentCreateBookBinding;
import com.example.booktracker.model.BookModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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

    public CreateBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCreateBookBinding.inflate(inflater, container, false);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        binding.datefield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Books").child(getDefaults("id",requireContext()));


        binding.imgfield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 404);
            }
        });
        binding.BookAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
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
        String Title = binding.titlefield.getText().toString().trim().toLowerCase(Locale.ROOT);
        String Author = binding.Authorfield.getText().toString().trim().toLowerCase(Locale.ROOT);
        String Genre = binding.genrefield.getText().toString().trim();
        String Date = binding.datefield.getText().toString().trim();


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
        ref.putFile(u).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        databaseReference.child(uniqueID).
                                setValue(new BookModel(uniqueID, Title, Author, Genre, Date, uri.toString()));
                    }
                });
            }
        });
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
                setContentText("Book Added Successfully").setConfirmText("Okay").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        navController.navigate(R.id.action_createBookFragment_to_homeFragment);
                        alertDialog.cancel();
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    public void errorMessage(String msg) {

        new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(msg).show();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

}