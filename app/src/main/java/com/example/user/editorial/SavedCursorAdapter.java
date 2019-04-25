package com.example.user.editorial;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.editorial.EditorialContract.SavedEntry;

public class SavedCursorAdapter extends CursorAdapter
{
    String url;

    ImageButton img;

    public SavedCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.editorial_list_item, parent, false);
    }



    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout

        TextView titleView=(TextView) view.findViewById(R.id.title);
        TextView descView=(TextView) view.findViewById(R.id.desc);
        TextView timeView=(TextView) view.findViewById(R.id.date_published);
        TextView authorView = (TextView) view.findViewById(R.id.author_name);
       img=(ImageButton) view.findViewById(R.id.imageview);



        // Find the columns of pet attributes that we're interested in
        int titleColumnIndex = cursor.getColumnIndex(SavedEntry.COLUMN_TITLE);
        int descColumnIndex = cursor.getColumnIndex(SavedEntry.COLUMN_DESC);
        int timeColumnIndex = cursor.getColumnIndex(SavedEntry.COLUMN_TIME);
        int authorColumnIndex = cursor.getColumnIndex(SavedEntry.COLUMN_AUTHOR);
        int urlColumnIndex = cursor.getColumnIndex(SavedEntry.COLUMN_URL);


        // Read the pet attributes from the Cursor for the current pet
       final String title = cursor.getString(titleColumnIndex);
        String desc = cursor.getString(descColumnIndex);
        String time = cursor.getString(timeColumnIndex);
        String author = cursor.getString(authorColumnIndex);
         url = cursor.getString(urlColumnIndex);



        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.


        // Update the TextViews with the attributes for the current pet
        titleView.setText(title);
        descView.setText(desc);
        timeView.setText(time);
        authorView.setText(author);


        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

              {
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

                                        if (SavedEntry.CONTENT_URI != null) {
                                            // Call the ContentResolver to delete the pet at the given content URI.
                                            // Pass in null for the selection and selection args because the mCurrentPetUri
                                            // content URI already identifies the pet that we want.
                                            System.out.println("title=="+title);
                                            String where="title like ?";
                                            String args[]={title};
                                            int rowsDeleted = context.getContentResolver().delete(SavedEntry.CONTENT_URI, where, args);

                                            // Show a toast message depending on whether or not the delete was successful.
                                            if (rowsDeleted == 0) {
                                                // If no rows were deleted, then there was an error with the delete.
                                          //      Toast.makeText(context, context.getString(R.string.editor_delete_pet_failed),
                                                      //  Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Otherwise, the delete was successful and we can display a toast.
                                        //        Toast.makeText(context, context.getString(R.string.editor_delete_pet_successful),
                                          //              Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        Toast.makeText(context, "Article deleted" , Toast.LENGTH_SHORT).show();

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


            }}
        });




    }

    public String getUrl(){
        return url;
    }




}
