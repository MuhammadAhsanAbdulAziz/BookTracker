package com.example.booktracker.Views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.booktracker.R;
import com.example.booktracker.adapter.BookAdapter;
import com.example.booktracker.databinding.FragmentViewBookBinding;
import com.example.booktracker.interfaces.BookInterface;
import com.example.booktracker.model.BookModel;
import com.example.booktracker.viewModel.BookViewModel;

import java.util.List;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

        binding.filterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.filterrow.getVisibility() == View.GONE) {
                    binding.filterrow.setVisibility(View.VISIBLE);
                } else {
                    binding.filterrow.setVisibility(View.GONE);
                }
            }
        });


        bookViewModel = new ViewModelProvider(requireActivity()).get(BookViewModel.class);

        bookViewModel.getBook(requireContext()).observe(requireActivity(), new Observer<List<BookModel>>() {
            @Override
            public void onChanged(List<BookModel> bookModels) {
                adp.submitList(bookModels);
            }
        });

        filteredData();

    }

    public void filteredData() {
        binding.authorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookViewModel.getBookbyAuthor(requireContext()).observe(requireActivity(), new Observer<List<BookModel>>() {
                    @Override
                    public void onChanged(List<BookModel> bookModels) {
                        adp.submitList(bookModels);
                    }
                });
            }
        });

        binding.titlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookViewModel.getBookbyTitle(requireContext()).observe(requireActivity(), new Observer<List<BookModel>>() {
                    @Override
                    public void onChanged(List<BookModel> bookModels) {
                        adp.submitList(bookModels);
                    }
                });
            }
        });

        binding.datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookViewModel.getBookbyDate(requireContext()).observe(requireActivity(), new Observer<List<BookModel>>() {
                    @Override
                    public void onChanged(List<BookModel> bookModels) {
                        adp.submitList(bookModels);
                    }
                });
            }
        });
    }

    @Override
    public void BookDetail(BookModel book) {
        bookViewModel.setBookModel(book);
        navController.navigate(R.id.action_viewBookFragment_to_bookDetailsFragment);
    }
}
