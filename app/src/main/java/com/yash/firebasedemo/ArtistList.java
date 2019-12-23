package com.yash.firebasedemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ArtistList extends ArrayAdapter<Artists> {

    private Activity context;
    private List<Artists> artistsList;


    public ArtistList(Activity context, List<Artists> artistsList) {
        super(context, R.layout.layout_list,artistsList);
        this.context = context;
        this.artistsList = artistsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();

        View listViewItem = layoutInflater.inflate(R.layout.layout_list, null, true);

        TextView artistName = listViewItem.findViewById(R.id.textViewName);
        TextView artistGenre = listViewItem.findViewById(R.id.textViewGenre);

        Artists artists = artistsList.get(position);
        artistName.setText(artists.getArtistName());
        artistGenre.setText(artists.getArtistGenres());

        return listViewItem;
    }
}
