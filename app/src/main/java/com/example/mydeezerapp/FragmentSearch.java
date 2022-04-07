package com.example.mydeezerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentSearch extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, IApiListener {

    ArrayList<Music> musics; // tableau de musiques
    MusicAdapter adapter;
    ListView listViewMusics;
    EditText editTextSearch;
    ImageButton buttonSearch;
    IMusicSelected listenerMusicSelected;

    public void setListenerMusicSelected(IMusicSelected listenerMusicSelected) {
        this.listenerMusicSelected = listenerMusicSelected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, null);

        listViewMusics= (ListView) v.findViewById(R.id.listViewMusics);
        musics= new ArrayList<Music>();
        //musics.add(new Music(959353, "My Way", "Frank Sinatra", "My Way (Expanded Edition)", "https://e-cdns-images.dzcdn.net/images/cover/ae5e2905c3e2236b19480b96c7b93bc8/250x250-000000-80-0-0.jpg", "https://www.deezer.com/track/959353", "https://cdns-preview-8.dzcdn.net/stream/c-8189d22fad5279baea90dccd48c37213-9.mp3"));
        //musics.add(new Music(884041, "Gimme! Gimme! Gimme! (A Man After Midnight)", "ABBA", "ABBA Gold", "https://e-cdns-images.dzcdn.net/images/cover/b8b70d474b7a8f27799e0d665e9b737e/56x56-000000-80-0-0.jpg", "https://www.deezer.com/track/884041", "https://cdns-preview-c.dzcdn.net/stream/c-c30d80f23a9cd9564985ed347e6ab579-16.mp3"));

        adapter= new MusicAdapter(getContext(), musics);
        listViewMusics.setAdapter(adapter);
        listViewMusics.setOnItemClickListener(this);

        editTextSearch= (EditText) v.findViewById(R.id.editTextSearch);
        buttonSearch= (ImageButton) v.findViewById(R.id.imageButtonSearch);
        buttonSearch.setOnClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Music music = musics.get(i);
        listenerMusicSelected.onSelectMusic(music);
    }

    @Override
    public void onClick(View view) {
        Services.searchMusics(getContext(), editTextSearch.getText().toString(), this);
    }

    @Override
    public void onReceiveMusics(ArrayList<Music> musics) {
        this.musics= musics;
        adapter= new MusicAdapter(getContext(), musics);
        listViewMusics.setAdapter(adapter);
    }
}