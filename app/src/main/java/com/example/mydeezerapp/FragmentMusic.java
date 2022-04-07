package com.example.mydeezerapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class FragmentMusic extends Fragment implements View.OnClickListener {

    TextView textViewArtist, textViewAlbum, textViewTitle;
    ImageView imageViewMusic;
    Button buttonPlay, buttonLink;
    Music currentMusic;
    Switch switchFav;
    MediaPlayer player= new MediaPlayer();

    public void setCurrentMusic(Music currentMusic) {
        this.currentMusic = currentMusic;
        refresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_music, null);

        textViewArtist = (TextView) v.findViewById(R.id.textViewArtist);
        textViewAlbum = (TextView) v.findViewById(R.id.textViewAlbum);
        textViewTitle = (TextView) v.findViewById(R.id.textViewTitle);
        imageViewMusic = (ImageView) v.findViewById(R.id.imageViewMusic);
        buttonPlay= (Button) v.findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
        buttonLink= (Button) v.findViewById(R.id.buttonLink);
        buttonLink.setOnClickListener(this);
        switchFav= (Switch) v.findViewById(R.id.switchFavoris);
        switchFav.setOnClickListener(this);
        return v;
    }

    public void refresh() {
        //currentMusic= (Music) intent.getSerializableExtra("selectedMusic");
        if (currentMusic != null) {
            textViewArtist.setText(currentMusic.getArtist());
            textViewAlbum.setText(currentMusic.getAlbum());
            textViewTitle.setText(currentMusic.getTitle());
            Services.loadImage(getContext(), currentMusic.getImage(), imageViewMusic);
            switchFav.setChecked(FavoriteRepository.getInstance(getContext()).isFavorite(currentMusic));
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(buttonLink)){ // la vue sur laquelle je clique donc ici buttonLink
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(currentMusic.getLink()));
            startActivity(intent);
        }else if(view.equals(buttonPlay)){ // la vue sur laquelle je clique donc ici buttonPlay
            if(!player.isPlaying()){
                buttonPlay.setText("STOP"); // si le lecteur joue une musique on met le texte STOP
                try {
                    player.reset(); // reinitialisation
                    player.setDataSource(getContext(), Uri.parse(currentMusic.getPreview())); // url de preview de la musique
                    player.prepare(); // commence à DL la musique sur le tel
                    player.start(); // lance la musique
                } catch (IOException e) { // erreur de l'url de la musique
                    e.printStackTrace();
                }
            }else{
                buttonPlay.setText("PLAY");
                player.stop();
            }
        }else{ // si je ne suis pas sur buttonLink ou buttonPlay, je suis sur le bouton switch
            if(FavoriteRepository.getInstance(getContext()).isFavorite(currentMusic)){ // on recupere l'instance dans faveoriterepository et on utilise la fonction isfavorite
                FavoriteRepository.getInstance(getContext()).remove(currentMusic);
                switchFav.setChecked(false);
            }else{
                FavoriteRepository.getInstance(getContext()).add(currentMusic); // si le titre n'etais pas dans les favoris, je l'ajoute à la liste des favoris
                switchFav.setChecked(true);
            }
        }
    }

}
