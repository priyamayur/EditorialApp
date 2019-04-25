package com.example.user.editorial;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MyEditorialFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private TextView mEmptyStateTextView;
    private SavedCursorAdapter mCursorAdapter;
    private static final int BOOK_LOADER = 0;
 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

     View rootView = inflater.inflate(R.layout.editorial_list, container, false);

     getActivity().setTitle("My Editorial");

     ListView editorialListView = (ListView) rootView.findViewById(R.id.list);
     mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);

     editorialListView.setEmptyView(mEmptyStateTextView);
     mCursorAdapter = new SavedCursorAdapter(getActivity(), null);
//     mAdapter = new EditorialAdapter(getActivity(), FullEditorialActivity.saved);

     editorialListView.setAdapter(mCursorAdapter);

     ProgressBar bar=(ProgressBar) rootView.findViewById(R.id.loading_indicator);
     bar.setVisibility(View.GONE);
     setHasOptionsMenu(true);

     SwipeRefreshLayout swipe= (SwipeRefreshLayout)  rootView.findViewById(R.id.pullToRefresh);
     //swipe.setVisibility(View.GONE);
     swipe.setEnabled(Boolean.FALSE);

     getLoaderManager().initLoader(BOOK_LOADER, null, this);

   editorialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
             // Release the media player if it currently exists because we are about to
             // Create a new intent to open the {@link to QuizActivity
             String url = mCursorAdapter.getUrl();
             System.out.println("url="+url);

             Intent startIntent = new Intent(getActivity(), SavedEditorial.class);

             // startIntent.putExtra("current_song", Parcels.wrap(playlistB.get(position)) );

             startIntent.putExtra("url",url);

             // Start the new activity
             startActivity(startIntent);

         }
     });
     return rootView;

 }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                EditorialContract.SavedEntry._ID,
                EditorialContract.SavedEntry.COLUMN_TITLE,
                EditorialContract.SavedEntry.COLUMN_DESC,
                EditorialContract.SavedEntry.COLUMN_TIME,
                EditorialContract.SavedEntry.COLUMN_AUTHOR,
                EditorialContract.SavedEntry.COLUMN_URL};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),   // Parent activity context
                EditorialContract.SavedEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sample, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAll();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        int rowsDeleted = getActivity().getContentResolver().delete(EditorialContract.SavedEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from book database");
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
