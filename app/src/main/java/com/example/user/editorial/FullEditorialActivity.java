package com.example.user.editorial;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.user.editorial.EditorialContract.SavedEntry;
import java.util.ArrayList;


public class FullEditorialActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar loader;
   static String url ;
   static String title ;
   static String desc ;
    static String time ;
    static String author ;
    public static ArrayList<Editorial> saved = new ArrayList<Editorial>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_editorial);
        System.out.println("enter create full activity");
   /*     CharSequence text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);

        System.out.println("text "+text);*/
        // process the text
        System.out.println("enter full editorial");

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        desc = intent.getStringExtra("desc");
        time = intent.getStringExtra("time");
        author = intent.getStringExtra("author");
        loader = (ProgressBar) findViewById(R.id.loader);
        webView = (WebView) findViewById(R.id.webView);
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        CheckBox check = (CheckBox) findViewById(R.id.checkBox);
        System.out.println("title in full editorial="+title);
        if(title!=null) {
            Cursor c = queryArticle();
            System.out.println("cursor=" + c);
            if (c.moveToFirst() == true) {
                System.out.println("cursor inside=" + c);
                check.setChecked(true);
            }
        }
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                 if (isChecked) {
                                                     insertArticle();
                                                 } else
                                                     deleteArticle();
                                             }
                                         }
        );
        //   webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    loader.setVisibility(View.GONE);
                } else {
                    loader.setVisibility(View.VISIBLE);
                }
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);


    }

    private void insertArticle() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.

            ContentValues values = new ContentValues();
            values.put(SavedEntry.COLUMN_TITLE, title);
            values.put(SavedEntry.COLUMN_DESC, desc);
            values.put(SavedEntry.COLUMN_TIME, time);
            values.put(SavedEntry.COLUMN_AUTHOR, author);
            values.put(SavedEntry.COLUMN_URL, url);
//error
            getContentResolver().insert(SavedEntry.CONTENT_URI, values);

    }

    private Cursor queryArticle() {
        String[] projection = {SavedEntry.COLUMN_TITLE,
                SavedEntry.COLUMN_DESC};
        // String where="word='"+text.toString().toLowerCase()+"'";

        String where = "title like ?";
        String args[] = {title};
        Cursor c = getContentResolver().query(SavedEntry.CONTENT_URI, projection, where, args, null);
        System.out.println("cursor query method =" + c.moveToFirst());
        //  System.out.println("cursor query method ="+c.getCount());
        return c;
    }


    private void deleteArticle() {
        // Only perform the delete if this is an existing pet.

        if (SavedEntry.CONTENT_URI != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            String where = "title like ?";
            String args[] = {title};
            int rowsDeleted = getContentResolver().delete(SavedEntry.CONTENT_URI, where, args);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }


    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    }
