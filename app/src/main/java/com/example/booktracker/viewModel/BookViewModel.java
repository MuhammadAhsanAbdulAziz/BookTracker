package com.example.booktracker.viewModel;


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

    public LiveData<List<BookModel>> getBook() {
        return repo.getBook();
    }
    public LiveData<List<BookModel>> getBookbyAuthor() {
        return repo.getBookByAuthor();
    }
    public LiveData<List<BookModel>> getBookbyTitle() {
        return repo.getBookByTitle();
    }
    public LiveData<List<BookModel>> getBookbyDate() {
        return repo.getBookbyDate();
    }
    public LiveData<List<BookModel>> SearchBook(String name) {
        return repo.SearchBook(name);
    }
}
