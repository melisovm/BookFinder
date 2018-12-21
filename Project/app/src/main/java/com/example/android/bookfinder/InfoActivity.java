package com.example.android.bookfinder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class InfoActivity extends AppCompatActivity{

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        Intent data = getIntent();

        String title = data.getStringExtra("Title");
        String author = data.getStringExtra("Author");
        String description = data.getStringExtra("Description");
        String image = data.getStringExtra("Image");
        final String infolink = data.getStringExtra("infoLink");
        String publisher = data.getStringExtra("publisher");
        String publishedDate = data.getStringExtra("publishDate");
        final String webReaderLink = data.getStringExtra("webReaderLink");
        int pages = data.getIntExtra("pages",0);

        setTitle(title);

        ImageView imageView = findViewById(R.id.imageview);

        if(image!="") {
            Glide.with(this)
                    .load(image)
                    .into(imageView);
        }
        else {
            imageView.setImageResource(R.drawable.bookicon);
        }

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        TextView authorTextView = (TextView) findViewById(R.id.author);
        authorTextView.setText(author);

        TextView publisherTextView = (TextView) findViewById(R.id.publisher);
        publisherTextView.setText(publisher);

        TextView publishedDateTextView = (TextView) findViewById(R.id.date);
        publishedDateTextView.setText(publishedDate);

        TextView pagesTextView = (TextView) findViewById(R.id.pages);
        if(pages == 0) {
            pagesTextView.setText("Info not available.");
        }
        else {
            pagesTextView.setText(String.valueOf(pages));
        }

        TextView descriptionTextView = (TextView) findViewById(R.id.description);
        descriptionTextView.setText(description);

        TextView infoLinkTextView = (TextView) findViewById(R.id.infolink);
        infoLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri bookUri = Uri.parse(infolink);

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(websiteIntent);

            }
        });

        TextView webReaderLinkTextView = (TextView) findViewById(R.id.readerlink);
        webReaderLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View view) {

                    if(webReaderLink!="")
                    {
                        Uri readerUri = Uri.parse(webReaderLink);

                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, readerUri);

                        startActivity(websiteIntent);
                    }
                    else {
                        Toast.makeText(getBaseContext(),"Web Link Unavailable",Toast.LENGTH_SHORT);
                    }

                }
            });


    }
}
