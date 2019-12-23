package com.yash.firebasedemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TrackList extends ArrayAdapter<Track> {

    private Activity context;
    private List<Track> tracksList;


    public TrackList(Activity context, List<Track> tracksList) {
        super(context, R.layout.layout_list,tracksList);
        this.context = context;
        this.tracksList = tracksList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        View listViewItem = layoutInflater.inflate(R.layout.layout_list, null, true);

        TextView artistName = listViewItem.findViewById(R.id.textViewName);
        TextView artistGenre = listViewItem.findViewById(R.id.textViewGenre);

        Track track = tracksList.get(position);
        artistName.setText(track.getTrackName());
        artistGenre.setText(String.valueOf(track.getTrackRating()));

        return listViewItem;
    }
}
