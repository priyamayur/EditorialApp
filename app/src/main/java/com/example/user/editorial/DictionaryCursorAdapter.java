package com.example.user.editorial;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.user.editorial.EditorialContract.EditorialEntry;


public class DictionaryCursorAdapter extends CursorAdapter {


    public DictionaryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }



    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout

        TextView wordView=(TextView) view.findViewById(R.id.word);
        TextView grammerView = (TextView) view.findViewById(R.id.grammer);
        TextView meaningView = (TextView) view.findViewById(R.id.meaning);
        ImageView img=(ImageView) view.findViewById(R.id.imageview);




        // Find the columns of pet attributes that we're interested in
        int wordColumnIndex = cursor.getColumnIndex(EditorialEntry.COLUMN_WORD);
        int grammerColumnIndex = cursor.getColumnIndex(EditorialEntry.COLUMN_GRAMMER);
        int meaningColumnIndex = cursor.getColumnIndex(EditorialEntry.COLUMN_MEANING);


        // Read the pet attributes from the Cursor for the current pet
  final    String  word = cursor.getString(wordColumnIndex);
        String grammer = cursor.getString(grammerColumnIndex);
  final   String   meaning = cursor.getString(meaningColumnIndex);

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.


        // Update the TextViews with the attributes for the current pet
        wordView.setText(word);
        grammerView.setText(grammer);
        meaningView.setText(meaning);

        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                switch (v.getId()) {
                    case R.id.imageview:

                        PopupMenu popup = new PopupMenu(context, v);
                        popup.getMenuInflater().inflate(R.menu.clipboard_popup,
                                popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                switch (item.getItemId()) {

                                    case R.id.delete:

                                        if (EditorialEntry.CONTENT_URI != null) {
                                            // Call the ContentResolver to delete the pet at the given content URI.
                                            // Pass in null for the selection and selection args because the mCurrentPetUri
                                            // content URI already identifies the pet that we want.
                                            System.out.println("word=="+word);
                                            System.out.println("meaning=="+meaning);
                                            String where="meaning like ?";
                                            String args[]={meaning};
                                            int rowsDeleted = context.getContentResolver().delete(EditorialEntry.CONTENT_URI, where, args);

                                            // Show a toast message depending on whether or not the delete was successful.
                                            if (rowsDeleted == 0) {
                                                // If no rows were deleted, then there was an error with the delete.
                                              /*  Toast.makeText(context, context.getString(R.string.editor_delete_pet_failed),
                                                        Toast.LENGTH_SHORT).show();*/
                                            } else {
                                                // Otherwise, the delete was successful and we can display a toast.
                                            /*    Toast.makeText(context, context.getString(R.string.editor_delete_pet_successful),
                                                        Toast.LENGTH_SHORT).show();*/
                                            }
                                        }
                                        Toast.makeText(context, "Word meaning deleted from dictionary" , Toast.LENGTH_SHORT).show();

                                        break;

                                    default:
                                        break;
                                }

                                return true;
                            }
                        });

                        break;

                    default:
                        break;
                }


            }
        });



    }




}
