package com.yash.firebasedemo;

public class Artists {

    String artistId;
    String artistName;
    String artistGenres;
    public Artists() {

    }
    public Artists(String artistId, String artistName, String artistGenres) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGenres = artistGenres;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistGenres() {
        return artistGenres;
    }
}
