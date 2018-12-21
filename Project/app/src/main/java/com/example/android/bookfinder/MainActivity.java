package com.example.android.bookfinder;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    String url = "https://www.googleapis.com/books/v1/volumes?q=";

    private BookAdapter mAdapter;
    private static final int BOOK_LOADER_ID = 1;
    private ImageView mEmptyStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.INVISIBLE);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Book currentBook = mAdapter.getItem(position);
                String bookTitle = currentBook.getTitle();
                String bookAuthor = currentBook.getAuthor();
                String bookDescription = currentBook.getDescription();
                String bookImage = currentBook.getImageUrl();
                String bookLink = currentBook.getUrl();
                String bookPublisher = currentBook.getPublisher();
                String bookPublishDate = currentBook.getPublishedDate();
                String bookWebReaderLink = currentBook.getWebReaderLink();
                int bookPages = currentBook.getPages();

                Intent intent = new Intent(MainActivity.this,InfoActivity.class);
                intent.putExtra("Title",bookTitle);
                intent.putExtra("Author",bookAuthor);
                intent.putExtra("Description",bookDescription);
                intent.putExtra("Image",bookImage);
                intent.putExtra("infoLink",bookLink);
                intent.putExtra("publisher",bookPublisher);
                intent.putExtra("publishDate",bookPublishDate);
                intent.putExtra("webReaderLink",bookWebReaderLink);
                intent.putExtra("pages",bookPages);

                startActivity(intent);

            }
        });

        final Button searchButton = (Button) findViewById(R.id.search_button);

        mEmptyStateView = (ImageView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateView);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View loadingIndicator = findViewById(R.id.loading_indicator);
                loadingIndicator.setVisibility(View.VISIBLE);

                url = "https://www.googleapis.com/books/v1/volumes?q=";

                EditText searchField = (EditText) findViewById(R.id.search_bar);
                String fullString = searchField.getText().toString();

                final String LOCATION_SEPARATOR = " ";

                if (fullString.contains(LOCATION_SEPARATOR)) {
                    String[] parts = fullString.split(LOCATION_SEPARATOR);
                    url += parts[0];
                    for (int i=1;i<parts.length;i++)
                    {
                        url += "+" + parts[i];
                    }
                } else {
                    url += fullString;
                }

                url += "&maxResults=20";

                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
                }
                else {
                    mEmptyStateView.setImageResource(R.drawable.images);
                }

                getLoaderManager().restartLoader(BOOK_LOADER_ID,null,MainActivity.this);
            }
        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, url);

    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

}
