package com.example.booktracker.viewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.booktracker.Repository.BookRepository;
import com.example.booktracker.model.BookModel;

import java.util.List;

public class BookViewModel extends ViewModel {

    BookRepository repo = new BookRepository();
    BookModel bookModel;

    public BookModel getBookModel() {
        return bookModel;
    }

    public void setBookModel(BookModel bookModel) {
        this.bookModel = bookModel;
    }

    public LiveData<List<BookModel>> getBook(Context context) {
        return repo.getBook(context);
    }
    public LiveData<List<BookModel>> getBookbyAuthor(Context context) {
        return repo.getBookByAuthor(context);
    }
    public LiveData<List<BookModel>> getBookbyTitle(Context context) {
        return repo.getBookByTitle(context);
    }
    public LiveData<List<BookModel>> getBookbyDate(Context context) {
        return repo.getBookbyDate(context);
    }
    public LiveData<List<BookModel>> SearchBook(String name,Context context) {
        return repo.SearchBook(name,context);
    }
}
