package com.example.android.bookfinder;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import static com.example.android.bookfinder.MainActivity.LOG_TAG;

public class QueryUtils {

    QueryUtils(){

    }

    public static List<Book> fetchBookData(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> books = extractFeatureFromJson(jsonResponse);

        return books;

    }

    public static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Book> extractFeatureFromJson(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Book> books = new ArrayList<Book>();

        try
        {
            JSONObject object = new JSONObject(jsonResponse);

            JSONArray items = object.getJSONArray("items");

            for (int i=0;i<items.length();i++)
            {
                JSONObject book = items.getJSONObject(i);
                JSONObject info = book.getJSONObject("volumeInfo");

                String title = info.getString("title");
                String publisher;
                if(info.has("publisher")) {
                    publisher = info.getString("publisher");
                }
                else
                {
                    publisher = "No publisher info!!";
                }

                String publishDate;
                if(info.has("publishedDate"))
                {
                     publishDate = info.getString("publishedDate");
                }
                else {
                    publishDate = "Publish date info unavailable.";
                }

                String description;
                if(info.has("description")){
                    description = info.getString("description");
                }
                else {
                    description = "No description available.";
                }

                String infoUrl = info.getString("infoLink");

                int pages;
                if(info.has("pages")) {
                    pages = info.getInt("pageCount");
                }
                else {
                    pages = 0;
                }

                JSONObject accessInfo = book.getJSONObject("accessInfo");
                String webLink;
                if(accessInfo.has("webReaderLink")){
                    webLink = accessInfo.getString("webReaderLink");
                }
                else {
                    webLink = "";
                }


                JSONArray authors;
                String author;
                if(info.has("authors")) {
                    authors = info.getJSONArray("authors");
                    author = authors.getString(0);
                }
                else {
                    author = "Author name unavailable";
                }


                JSONObject image;
                String imageUrl;

                if(info.has("imageLinks")) {
                    image = info.getJSONObject("imageLinks");
                    imageUrl = image.getString("smallThumbnail");
                }
                else {
                    imageUrl = "";
                }

                books.add(new Book(title,author,infoUrl,imageUrl,description,publisher,publishDate,webLink,pages));
            }
        }
        catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return books;
    }



}
