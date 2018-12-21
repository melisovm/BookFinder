package com.example.android.bookfinder;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book>{

    public BookAdapter(Activity context, ArrayList<Book> books) {

        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);
        String myUrl = currentBook.getImageUrl();

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.image);

        Glide.with(getContext())
                .load(myUrl)
                .into(imageView);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title_text_view);
        String currentTitle = currentBook.getTitle();
        titleTextView.setText(currentTitle);

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_text_view);
        String currentAuthor = currentBook.getAuthor();
        authorTextView.setText(currentAuthor);

        return listItemView;
    }

}
