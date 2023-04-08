package com.example.booktracker.Views;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.example.booktracker.R;
import com.example.booktracker.databinding.FragmentBookUpdateBinding;
import com.example.booktracker.viewModel.BookViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class BookUpdateFragment extends Fragment {

    FragmentBookUpdateBinding binding;
    BookViewModel bookViewModel;
    FirebaseAuth auth;
    NavController navController;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    SweetAlertDialog alertDialog;
    Uri u;
    final Calendar myCalendar = Calendar.getInstance();
    String loggedId;

    public BookUpdateFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookUpdateBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            loggedId = auth.getCurrentUser().getUid();
        }

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        binding.datefield.setOnClickListener(v -> new DatePickerDialog(getContext(),
                date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().
                getReference("Books").child(loggedId).child(bookViewModel.getBookModel().getId());


        binding.imgfield.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 404);
        });
        binding.BookUpdateBtn.setOnClickListener(v -> UpdateBook());

        return binding.getRoot();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.datefield.setText(dateFormat.format(myCalendar.getTime()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        binding.titlefield.setText(bookViewModel.getBookModel().getTitle());
        binding.Authorfield.setText(bookViewModel.getBookModel().getAuthor());
        binding.genrefield.setText(bookViewModel.getBookModel().getGenre());
        binding.datefield.setText(bookViewModel.getBookModel().getPublication_date());
        Glide.with(requireContext()).load(bookViewModel.getBookModel().getPicture()).
                error(R.drawable.ic_baseline_image_24).dontAnimate().into(binding.imgfield);
    }

    private void UpdateBook() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Please wait..");
        String Title = Objects.requireNonNull(binding.titlefield.getText()).toString().trim().toLowerCase(Locale.ROOT);
        String Author = Objects.requireNonNull(binding.Authorfield.getText()).toString().trim().toLowerCase(Locale.ROOT);
        String Genre = Objects.requireNonNull(binding.genrefield.getText()).toString().trim();
        String Date = Objects.requireNonNull(binding.datefield.getText()).toString().trim();

        if (u == null) {
            errorMessage("Enter Book Cover Again");
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
        final StorageReference ref = storageReference.child("Book Images/" + u.getLastPathSegment());
        ref.putFile(u).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
            Map<String, Object> Book = new HashMap<>();
            Book.put("title", Title);
            Book.put("author", Author);
            Book.put("genre", Genre);
            Book.put("publication_date", Date);
            Book.put("picture", uri.toString());
            databaseReference.updateChildren(Book);
        }));
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
                setContentText("Book Updated Successfully").setConfirmText("Okay").setConfirmClickListener(sweetAlertDialog -> {
                    navController.navigate(R.id.action_bookUpdateFragment_to_homeFragment);
                    alertDialog.cancel();
                    alertDialog.dismiss();
                });
        alertDialog.show();

    }

    public void errorMessage(String msg) {

        new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText(msg).show();
    }
}