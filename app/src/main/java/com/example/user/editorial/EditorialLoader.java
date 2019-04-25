package com.example.user.editorial;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.text.ParseException;
import java.util.List;

import static com.example.user.editorial.fetchData.fetchEditorialData;

public class EditorialLoader extends AsyncTaskLoader<List<Editorial>> {

    private int mFrag;
    private String mUrl;


    public EditorialLoader(Context context, String url,int frag) {

        super(context);
        mUrl = url;
        mFrag=frag;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<Editorial> loadInBackground() {
        if (mUrl == null) {
            return null;
        }


        System.out.println("fetch data enter "+mFrag);

       // fetchData.editorials.removeAll(fetchData.editorials);
        List<Editorial> editorials = null;

            editorials = fetchEditorialData(mUrl,mFrag);


//        System.out.println("EDITORIAL for "+mFrag+" "+editorials.size());
        return editorials;
    }


}
