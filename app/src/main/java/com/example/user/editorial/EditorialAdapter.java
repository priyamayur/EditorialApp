package com.example.user.editorial;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditorialAdapter extends ArrayAdapter<Editorial> {

    public EditorialAdapter(Context context, List<Editorial> editorials) {
        super(context, 0, editorials);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.editorial_list_item, parent, false);
        }

        ImageButton img=(ImageButton) listItemView.findViewById(R.id.imageview);
        img.setVisibility(View.GONE);
        Editorial currentDebate = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);

        titleTextView.setText(currentDebate.getTitle());

        TextView descTextView = (TextView) listItemView.findViewById(R.id.desc);

        descTextView.setText(currentDebate.getDesc());

        String separate[] = currentDebate.getTime().split("T");

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_published);
        String formattedDate = formatDate(separate[0]);

        dateTextView.setText(formattedDate);

        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author_name);

        if (currentDebate.getAuthor() != null)
            authorTextView.setText(currentDebate.getAuthor());
        else
            authorTextView.setText("");

        return listItemView;

    }


    private String formatDate(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("LLL dd, yyyy");
        String newFormat = formatter.format(testDate);


        return newFormat;


    }

}
