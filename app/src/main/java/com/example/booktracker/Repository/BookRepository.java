package com.example.booktracker.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.booktracker.model.BookModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookRepository {
    MutableLiveData<List<BookModel>> booklist;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.
            getInstance().getReference("Books").child(auth.getCurrentUser().getUid());

    public LiveData<List<BookModel>> getBook() {
        booklist = new MutableLiveData<>();
        ArrayList<BookModel> data = new ArrayList<BookModel>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data.add(new BookModel(ds.child("id").getValue(String.class), ds.child("title").getValue(String.class),
                            ds.child("author").getValue(String.class),
                            ds.child("genre").getValue(String.class),
                            ds.child("publication_date").getValue(String.class),
                            ds.child("picture").getValue(String.class)));
                    booklist.setValue(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return booklist;
    }

    public LiveData<List<BookModel>> SearchBook(String name) {
        booklist = new MutableLiveData<>();
        ArrayList<BookModel> data = new ArrayList<BookModel>();
        databaseReference.orderByChild("title").startAt(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    for(char ch : ds.child("title").getValue(String.class).toCharArray())
                    {
                        for(char c : name.toCharArray()) {
                            if(ch == c)
                                data.add(new BookModel(ds.child("id").getValue(String.class), ds.child("title").getValue(String.class),
                                        ds.child("author").getValue(String.class),
                                        ds.child("genre").getValue(String.class),
                                        ds.child("publication_date").getValue(String.class),
                                        ds.child("picture").getValue(String.class)));
                            booklist.setValue(data);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return booklist;
    }

    public LiveData<List<BookModel>> getBookByAuthor() {
        booklist = new MutableLiveData<>();

        ArrayList<BookModel> data = new ArrayList<BookModel>();
        databaseReference.orderByChild("author").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data.add(new BookModel(ds.child("id").getValue(String.class), ds.child("title").getValue(String.class),
                            ds.child("author").getValue(String.class),
                            ds.child("genre").getValue(String.class),
                            ds.child("publication_date").getValue(String.class),
                            ds.child("picture").getValue(String.class)));
                    booklist.setValue(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return booklist;
    }
    public LiveData<List<BookModel>> getBookByTitle() {
        booklist = new MutableLiveData<>();

        ArrayList<BookModel> data = new ArrayList<BookModel>();
        databaseReference.orderByChild("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data.add(new BookModel(ds.child("id").getValue(String.class), ds.child("title").getValue(String.class),
                            ds.child("author").getValue(String.class),
                            ds.child("genre").getValue(String.class),
                            ds.child("publication_date").getValue(String.class),
                            ds.child("picture").getValue(String.class)));
                    booklist.setValue(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return booklist;
    }

    public LiveData<List<BookModel>> getBookbyDate() {
        booklist = new MutableLiveData<>();

        ArrayList<BookModel> data = new ArrayList<BookModel>();
        databaseReference.orderByChild("publication_date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    data.add(new BookModel(ds.child("id").getValue(String.class), ds.child("title").getValue(String.class),
                            ds.child("author").getValue(String.class),
                            ds.child("genre").getValue(String.class),
                            ds.child("publication_date").getValue(String.class),
                            ds.child("picture").getValue(String.class)));
                    booklist.setValue(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return booklist;
    }

}
