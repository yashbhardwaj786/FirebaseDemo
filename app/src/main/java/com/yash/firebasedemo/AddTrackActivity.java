package com.yash.firebasedemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.yash.firebasedemo.MainActivity.ARTIST_ID;
import static com.yash.firebasedemo.MainActivity.ARTIST_NAME;

public class AddTrackActivity extends AppCompatActivity {

    TextView artistName;
    EditText editTrackName;
    SeekBar rating;
    ListView listViewTrack;
    Button addTrack;

    DatabaseReference databaseTracks;

    List<Track> tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_track);

        artistName = findViewById(R.id.artistName);
        editTrackName = findViewById(R.id.editTextTrackName);
        rating = findViewById(R.id.seekBarRating);
        listViewTrack = findViewById(R.id.listViewTrack);
        addTrack = findViewById(R.id.buttonAddTrack);

        tracks = new ArrayList<>();

        String id = getIntent().getStringExtra(ARTIST_ID);
        String name = getIntent().getStringExtra(ARTIST_NAME);

        artistName.setText(name);

        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        addTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTrack();
            }
        });
    }

    private void saveTrack() {
        String trackName = editTrackName.getText().toString().trim();
        int trackRating = rating.getProgress();

        if (!TextUtils.isEmpty(trackName)){
            String id = databaseTracks.push().getKey();
            Track track = new Track(id, trackName, trackRating);
            databaseTracks.child(id).setValue(track);
            Toast.makeText(this, "Track Saved Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Track name should not be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tracks.clear();

                for(DataSnapshot trackSnapShot: dataSnapshot.getChildren()){
                    Track track = trackSnapShot.getValue(Track.class);
                    tracks.add(track);

                    TrackList trackListAdapter = new TrackList(AddTrackActivity.this, tracks);
                    listViewTrack.setAdapter(trackListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
