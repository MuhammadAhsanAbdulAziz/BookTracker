package com.example.booktracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booktracker.R;
import com.example.booktracker.databinding.BookBoxBinding;
import com.example.booktracker.interfaces.BookInterface;
import com.example.booktracker.model.BookModel;

public class BookAdapter extends ListAdapter<BookModel, BookAdapter.ViewHolder> {

    BookInterface bookInterface;
    Context context;
    public BookAdapter(Context context,BookInterface bookInterface) {
        super(BookModel.bookModelItemCallback);
        this.context = context;
        this.bookInterface = bookInterface;

    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layout = LayoutInflater.from(parent.getContext());
        BookBoxBinding binding = BookBoxBinding.inflate(layout,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        BookModel data = getItem(position);
        holder.binding.setBookInterface(bookInterface);
        holder.binding.setBook(data);
        Glide.with(context).load(data.getPicture()).
                error(R.drawable.ic_baseline_image_24).dontAnimate().into(holder.binding.bookimg);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        BookBoxBinding binding;
        public ViewHolder(BookBoxBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

        }
    }
}
