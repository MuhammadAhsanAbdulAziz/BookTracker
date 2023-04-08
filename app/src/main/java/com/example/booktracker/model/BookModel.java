package com.example.booktracker.model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class BookModel {
    String Id,Title,Author,Genre,Publication_date,Picture;

    public BookModel(String id, String title, String author, String genre, String publication_date, String picture) {
        Id = id;
        Title = title;
        Author = author;
        Genre = genre;
        Publication_date = publication_date;
        Picture = picture;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public String getPublication_date() {
        return Publication_date;
    }

    public void setPublication_date(String publication_date) {
        Publication_date = publication_date;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    @NonNull
    @Override
    public String toString() {
        return "Books{" +
                "Id='" + Id + '\'' +
                ", Title='" + Title + '\'' +
                ", Author='" + Author + '\'' +
                ", Genre='" + Genre + '\'' +
                ", Publication_date='" + Publication_date + '\'' +
                ", Picture='" + Picture + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookModel books = (BookModel) o;
        return getId().equals(books.getId()) && getTitle().equals(books.getTitle());
    }

    static public DiffUtil.ItemCallback<BookModel> bookModelItemCallback = new DiffUtil.ItemCallback<BookModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull BookModel oldItem, @NonNull BookModel newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull BookModel oldItem, @NonNull BookModel newItem) {
            return oldItem.equals(newItem);
        }
    };
}
