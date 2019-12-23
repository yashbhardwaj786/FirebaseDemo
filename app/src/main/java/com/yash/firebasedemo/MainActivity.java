package com.yash.firebasedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    Spinner spinnerGenres;
    Button buttonAdd;
    DatabaseReference databaseArtist;
    ListView listViewArtists;

    List<Artists> artistsList;

    public static final String ARTIST_NAME = "artistname";
    public static final String ARTIST_ID = "artistid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseArtist = FirebaseDatabase.getInstance().getReference("artist");
        databaseArtist.keepSynced(true);

        editTextName = findViewById(R.id.editTextName);
        spinnerGenres = findViewById(R.id.spinnerGenres);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewArtists = findViewById(R.id.artist_list);

        artistsList = new ArrayList<>();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addArtist();
            }
        });

        listViewArtists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artists artists = artistsList.get(position);
                Intent intent = new Intent(getApplicationContext(), AddTrackActivity.class);
                intent.putExtra(ARTIST_NAME, artists.getArtistName());
                intent.putExtra(ARTIST_ID, artists.getArtistId());
                startActivity(intent);
            }
        });

        listViewArtists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Artists artists = artistsList.get(position);
                showUpdateDialog(artists.artistId, artists.artistName, artists.artistGenres);
                return true;
            }
        });
    }

    private void addArtist() {
        String name = editTextName.getText().toString().trim();
        String genres = spinnerGenres.getSelectedItem().toString();

        if (!TextUtils.isEmpty(name)) {

            String id = databaseArtist.push().getKey();
            Artists artists = new Artists(id, name, genres);
            databaseArtist.child(id).setValue(artists);
            Toast.makeText(this, "Artist Added Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You should enter the name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseArtist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                artistsList.clear();

                Toast.makeText(getApplicationContext(), "Application Data Change", Toast.LENGTH_SHORT).show();

                for (DataSnapshot artistSnapshot : dataSnapshot.getChildren()) {
                    Artists artists = artistSnapshot.getValue(Artists.class);
                    artistsList.add(artists);
                    ArtistList adapter = new ArtistList(MainActivity.this, artistsList);
                    listViewArtists.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUpdateDialog(final String artistId, String artistName, String artistGenres) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_update_dialog, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinnerGenres = dialogView.findViewById(R.id.spinnerGenres);
        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final Button update = dialogView.findViewById(R.id.buttonUpdate);
        final Button delete = dialogView.findViewById(R.id.buttonDelete);
        dialogBuilder.setTitle("Updating Artist " + artistName);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        int pos = 0;

        String[] arrStr = getResources().getStringArray(R.array.genres);

            for (int i = 0; i < arrStr.length; i++) {
                if (arrStr[i].equalsIgnoreCase(artistGenres)) {
                    pos = i;
                    break;
                }
            }
            spinnerGenres.setSelection(pos);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String genres = spinnerGenres.getSelectedItem().toString();
                if (TextUtils.isEmpty(name)) {
                    editTextName.setError("Name Required");
                    return;
                }
                updateArtist(artistId, name, genres);
                alertDialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArtist(artistId);
                alertDialog.dismiss();
            }
        });
    }

    private boolean updateArtist(String id, String name, String geners) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("artist").child(id);
        Artists artists = new Artists(id, name, geners);
        databaseReference.setValue(artists);
        Toast.makeText(this, "Artist Updated Successfully", Toast.LENGTH_SHORT).show();

        return true;
    }

    private void deleteArtist(String artistId){
        DatabaseReference drArtist = FirebaseDatabase.getInstance().getReference("artist").child(artistId);
        DatabaseReference drTracks = FirebaseDatabase.getInstance().getReference("tracks").child(artistId);
        drArtist.removeValue();
        drTracks.removeValue();
        Toast.makeText(this, "Artist is Deleted Successfully", Toast.LENGTH_SHORT).show();
    }
}
