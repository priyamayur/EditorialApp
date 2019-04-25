package com.example.user.editorial;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class HeadlineFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Editorial>>{

    private TextView mEmptyStateTextView;
    private EditorialAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String USGS_REQUEST_URL =
            "https://newsapi.org/v2/top-headlines?country=in&apiKey=5ad0265806ca456fb55bca238fd79909&pageSize=100";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.editorial_list, container, false);
        getActivity().setTitle("Headlines");

        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });


        ListView editorialListView = (ListView) rootView.findViewById(R.id.list);
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        editorialListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new EditorialAdapter(getActivity(), new ArrayList<Editorial>());

        editorialListView.setAdapter(mAdapter);


        editorialListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Editorial currentEditorial = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)

                //  Uri debateUri = Uri.parse(currentDebate.getUrl());

                Intent i = new Intent(getActivity(), FullEditorialActivity.class);
                i.putExtra("url", currentEditorial.getUrl());
                i.putExtra("title", currentEditorial.getTitle());
                i.putExtra("desc", currentEditorial.getDesc());
                i.putExtra("time", currentEditorial.getTime());
                i.putExtra("author", currentEditorial.getAuthor());
            //    i.putExtra("",);
                startActivity(i);

                // Intent websiteIntent = new Intent(Intent.ACTION_VIEW, debateUri);

                // Send the intent to launch a new activity
                //startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }



        return rootView;
    }


    @NonNull
    @Override
    public Loader<List<Editorial>> onCreateLoader(int i, @Nullable Bundle bundle) {

        System.out.println("on create loader");

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();


        return new EditorialLoader(getContext(),uriBuilder.toString(),4);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Editorial>> loader, List<Editorial> editorials) {
        System.out.println("on loader finished");

        View loadingIndicator =(View) getView().findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_editorial);


        if (editorials != null && !editorials.isEmpty()) {

            mAdapter.addAll(editorials);


        }
        //fetchData.editorials.clear();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Editorial>> loader) {

        System.out.println("on loader reset");
        mAdapter.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Tag", "FragmentA.onDestroyView() has been called.");
        //   mAdapter.clear();
        fetchData.headLines.clear();
    }


    public void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
