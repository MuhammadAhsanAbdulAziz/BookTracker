package com.example.booktracker.Views;

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

import com.example.booktracker.R;
import com.example.booktracker.adapter.BookAdapter;
import com.example.booktracker.databinding.FragmentViewBookBinding;
import com.example.booktracker.interfaces.BookInterface;
import com.example.booktracker.model.BookModel;
import com.example.booktracker.viewModel.BookViewModel;


public class ViewBookFragment extends Fragment implements BookInterface {

    FragmentViewBookBinding binding;
    BookAdapter adp;
    BookViewModel bookViewModel;
    NavController navController;


    public ViewBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewBookBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        adp = new BookAdapter(requireActivity(), this);
        binding.Booklist.setAdapter(adp);

        binding.filterbtn.setOnClickListener(v -> {
            if (binding.filterrow.getVisibility() == View.GONE) {
                binding.filterrow.setVisibility(View.VISIBLE);
            } else {
                binding.filterrow.setVisibility(View.GONE);
            }
        });


        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        bookViewModel.getBook().observe(requireActivity(), bookModels -> adp.submitList(bookModels));

        filteredData();

    }

    public void filteredData() {
        binding.authorbtn.setOnClickListener(v -> bookViewModel.getBookbyAuthor().observe(requireActivity()
                , bookModels -> adp.submitList(bookModels)));

        binding.titlebtn.setOnClickListener(v -> bookViewModel.getBookbyTitle().observe(requireActivity()
                , bookModels -> adp.submitList(bookModels)));

        binding.datebtn.setOnClickListener(v -> bookViewModel.getBookbyDate().observe(requireActivity()
                , bookModels -> adp.submitList(bookModels)));
    }

    @Override
    public void BookDetail(BookModel book) {
        bookViewModel.setBookModel(book);
        navController.navigate(R.id.action_viewBookFragment_to_bookDetailsFragment);
    }
}
