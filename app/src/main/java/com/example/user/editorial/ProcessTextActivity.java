package com.example.user.editorial;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.example.user.editorial.EditorialContract.EditorialEntry;

public class ProcessTextActivity extends FullEditorialActivity {

  //  static ContentValues values;
    static int count=0;
    CharSequence text;
    static ArrayList<String> word= new ArrayList<>();
    static ArrayList<String> gram= new ArrayList<>();
    static ArrayList<String> meaning= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_text);

        System.out.println("enter create process activity");
        System.out.println("enter process");

        if(word!=null){
            word.clear();
            gram.clear();
            meaning.clear();}

        word=new ArrayList<>();
        gram= new ArrayList<>();
        meaning= new ArrayList<>();


        text = getIntent()
                .getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        // process the text
        System.out.println("text is " + text);

        TextView sectionTextView = (TextView) findViewById(R.id.section);
        CheckBox check= (CheckBox) findViewById(R.id.checkBox);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String[]> meanings = databaseAccess.getMeaning(text.toString());
        String str="";

        for(int i=0;i<meanings.size();i++ ){

            System.out.println(meanings.get(i)[0]+" "+meanings.get(i)[1]+" "+meanings.get(i)[2]);
            str=str+(meanings.get(i)[0]+" "+meanings.get(i)[1]+" "+meanings.get(i)[2])+"\n\n";
            word.add(meanings.get(i)[0]);
            gram.add(meanings.get(i)[1]);
            meaning.add(meanings.get(i)[2]);

        }
        if (str.equalsIgnoreCase("")){
            sectionTextView.setText("Word not found");
            check.setVisibility(View.GONE);

        }
        else
        sectionTextView.setText(str);


  //     final Button button_fav = (Button) findViewById(R.id.fav_button);
if(text.toString()!=null) {
    Cursor c = queryMeaning();
    System.out.println("cursor=" + c);
    if (c.moveToFirst() == true) {
        System.out.println("cursor inside=" + c);
        check.setChecked(true);
    }
}
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                             @Override
                                             public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                                                 if (isChecked){
                                                     insertMeaning();
                                                 }
                                                 else
                                                     deleteMeaning();
                                             }
                                         }
        );


        databaseAccess.close();

    }

    private void insertMeaning() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        System.out.println("Insert meaning"+text.toString());
        System.out.println("Insert meaning number of meanings"+word.size());

        for (int i = 0; i < word.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(EditorialContract.EditorialEntry.COLUMN_WORD, word.get(i));
            values.put(EditorialContract.EditorialEntry.COLUMN_GRAMMER, gram.get(i));
            values.put(EditorialContract.EditorialEntry.COLUMN_MEANING, meaning.get(i));
//error
            getContentResolver().insert(EditorialEntry.CONTENT_URI, values);
        }
    }
        private Cursor queryMeaning() {
            System.out.println("Query meaning"+text.toString());
            String[] projection = { EditorialContract.EditorialEntry.COLUMN_WORD,
                    EditorialContract.EditorialEntry.COLUMN_MEANING };
           // String where="word='"+text.toString().toLowerCase()+"'";

            String where="word like ?";
            String args[]={text.toString()};
              Cursor c= getContentResolver().query(EditorialEntry.CONTENT_URI,projection,where,args,null);
            System.out.println("cursor query method ="+c.moveToFirst());
          //  System.out.println("cursor query method ="+c.getCount());
              return c;
            }





    private void deleteMeaning() {
        System.out.println("delete meaning"+text.toString());
        // Only perform the delete if this is an existing pet.
        System.out.println("delete meaning number of meanings"+word.size());

            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            String where="word like ?";
            String args[]={text.toString()};
            int rowsDeleted = getContentResolver().delete(EditorialEntry.CONTENT_URI, where, args);


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


        // Close the activity

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
