package com.example.user.editorial;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.TimeZone;
import java.time.LocalDate;
import java.util.Calendar;
import	java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class fetchData {

    private fetchData() {
    }

    private static final String LOG_TAG = fetchData.class.getSimpleName();
    static int totalResults;
    static List<Editorial> hindu=new ArrayList<>();
    static List<Editorial> independent=new ArrayList<>();
    static List<Editorial> newYork=new ArrayList<>();
    static List<Editorial> headLines=new ArrayList<>();
    static int c=0;

    public static List<Editorial> fetchEditorialData(String requestUrl,int frag){
        // Create URL object



    //    Calendar cal = Calendar.getInstance();
      //  cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        //cal.add(Calendar.DATE, -2);
    //    String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(cal.getTime());
// Output "Wed Sep 26 14:23:28 EST 2012"

        String date = format1.format(cal.getTime());
        System.out.println(date);
        // Output "2012-09-26"

      //  System.out.println(format1.parse(date));

     System.out.println(date);
 /*if (frag==2)
 {
     return new ArrayList<>();
 }*/URL url=null;
        if(frag!=4) {

             url = createUrl(requestUrl + "&page=1" + "&from=" + date);
        }
        else
        {
            url = createUrl(requestUrl + "&page=1");

        }


        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.d(LOG_TAG, "Problem making the HTTP request.", e);
        }


        List<Editorial>   editorials = extractFeatureFromJson(jsonResponse,frag);
        int loops;
        if (totalResults%100 == 0)
            loops=(totalResults/100) -1;
        else
            loops=totalResults/100;

        System.out.println("editorials length"+ editorials.size()+" for frag"+frag);
        System.out.println("frag="+frag+" loops="+loops);
        for(int j =0;j<loops;j++){
            System.out.println("frag="+frag+" loop no="+j);
           // String url_temp= "https://newsapi.org/v2/everything?sources=the-hindu&apiKey=5ad0265806ca456fb55bca238fd79909&from=2019-03-11&pageSize=100&page="+(j+2);
            URL url_next=null;
            if(frag!=4){
             url_next = createUrl(requestUrl+"&page="+(j+2)+"&from="+date);}
             else
                url_next = createUrl(requestUrl+"&page="+(j+2));

            String jsonResponse_next = null;
            try {
                jsonResponse_next = makeHttpRequest(url_next);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Problem making the HTTP request.", e);
            }
            editorials = extractFeatureFromJson(jsonResponse_next,frag);
           // editorials.add(extractFeatureFromJson(jsonResponse_next,frag));

        }
        if(editorials!=null)
        System.out.println("editorials length"+ editorials.size()+" for frag"+frag);

        return editorials;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.d(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
          //  SystemClock.sleep(7000);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(30000 /* milliseconds */);
            urlConnection.setConnectTimeout(30000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                System.out.println("enter read from stream");
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.d(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                System.out.println("NOT enter read from stream");
                if (urlConnection.getResponseCode()==429)
                    SystemClock.sleep(30000);
                else
                    return jsonResponse;


            }
        } catch (IOException e) {
            Log.d(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
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
        else
            System.out.println("input stream null");
        return output.toString();
    }

    private static List<Editorial> extractFeatureFromJson(String editorialJSON,int mFrag) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(editorialJSON)) {
            System.out.println("TEXT IS NULL ");
            if (mFrag==1)
                return hindu;
            if (mFrag==2)
                return  independent;
            if (mFrag==3)
                return  newYork;
            if (mFrag==4)
                return  headLines;

            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to


        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(editorialJSON);
            JSONArray articlesArray = baseJsonResponse.getJSONArray("articles");
             totalResults = baseJsonResponse.getInt("totalResults");








      //      System.out.println("ARRAY "+articlesArray.get(0));


            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).



            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < articlesArray.length(); i++) {


                JSONObject currentEditorial = articlesArray.getJSONObject(i);

                String url = currentEditorial.getString("url");


              //  System.out.println("count "+c++);
              //  System.out.println("URL "+url);
                if (mFrag==1) {
                    if (url.contains("https://www.thehindu.com/opinion")) {

                        System.out.println("URL editorial " + url);
                        String author = currentEditorial.getString("author");
                        String title = currentEditorial.getString("title");
                        String desc = currentEditorial.getString("description");
                        String time = currentEditorial.getString("publishedAt");
                        Editorial editorial = new Editorial(title, desc, author, time, url);
                        hindu.add(editorial);
                    }
                }

                if (mFrag==2){

                    if (url.contains("https://www.independent.co.uk/voices")) {
                        System.out.println("URL editorial " + url);
                        String author = currentEditorial.getString("author");
                        String title = currentEditorial.getString("title");
                        String desc = currentEditorial.getString("description");
                        String time = currentEditorial.getString("publishedAt");
                        Editorial editorial = new Editorial(title, desc, author, time, url);
                        independent.add(editorial);
                    }

                }
                if (mFrag==3){

                    if (url.contains("opinion")) {
                        System.out.println("URL editorial " + url);
                        String author = currentEditorial.getString("author");
                        String title = currentEditorial.getString("title");
                        String desc = currentEditorial.getString("description");
                        String time = currentEditorial.getString("publishedAt");
                        Editorial editorial = new Editorial(title, desc, author, time, url);
                        newYork.add(editorial);
                    }

                }

                if (mFrag==4){

                        System.out.println("URL editorial " + url);
                        String author = currentEditorial.getString("author");
                        String title = currentEditorial.getString("title");
                        String desc = currentEditorial.getString("description");
                        String time = currentEditorial.getString("publishedAt");
                        Editorial editorial = new Editorial(title, desc, author, time, url);
                        headLines.add(editorial);

                }


            }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.d("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

      //  System.out.println("EDITORIAL 1  "+editorials.get(0).getTitle());
        // Return the list of earthquakes
        if (mFrag==1)
          return hindu;
       if (mFrag==2)
            return  independent;
        if (mFrag==3)
            return  newYork;
        if (mFrag==4)
            return  headLines;

       return new ArrayList<>();


    }
}
