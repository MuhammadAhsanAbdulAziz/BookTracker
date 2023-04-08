package com.example.booktracker.Views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.booktracker.R;
import com.example.booktracker.adapter.BookAdapter;
import com.example.booktracker.databinding.FragmentSearchBookBinding;
import com.example.booktracker.interfaces.BookInterface;
import com.example.booktracker.model.BookModel;
import com.example.booktracker.viewModel.BookViewModel;

import java.util.List;
import java.util.Locale;

public class SearchBookFragment extends Fragment implements BookInterface {

    FragmentSearchBookBinding binding;
    BookAdapter adp;
    BookViewModel bookViewModel;
    NavController navController;

    public SearchBookFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        adp = new BookAdapter(requireActivity(), this);
        binding.Booklist.setAdapter(adp);
        searchBook();

    }

    @Override
    public void BookDetail(BookModel book) {
        bookViewModel.setBookModel(book);
        navController.navigate(R.id.action_searchBookFragment_to_bookDetailsFragment);
    }

    public void searchBook()
    {
        binding.searchbook.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

                bookViewModel.SearchBook(s.toString().trim().
                        toLowerCase(Locale.ROOT)).observe(requireActivity(),
                        bookModels -> adp.submitList(bookModels));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}