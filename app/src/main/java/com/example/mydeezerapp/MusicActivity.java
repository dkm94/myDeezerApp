package com.example.mydeezerapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewArtist, textViewAlbum, textViewTitle;
    ImageView imageViewMusic;
    Button buttonPlay, buttonLink;
    Music currentMusic;
    Switch switchFav;
    MediaPlayer player= new MediaPlayer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        textViewArtist = (TextView) findViewById(R.id.textViewArtist);
        textViewAlbum = (TextView) findViewById(R.id.textViewAlbum);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        imageViewMusic = (ImageView) findViewById(R.id.imageViewMusic);
        buttonPlay= (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);
        buttonLink= (Button) findViewById(R.id.buttonLink);
        buttonLink.setOnClickListener(this);
        switchFav= (Switch) findViewById(R.id.switchFavoris);
        switchFav.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        currentMusic= (Music) intent.getSerializableExtra("selectedMusic");
        textViewArtist.setText(currentMusic.getArtist());
        textViewAlbum.setText(currentMusic.getAlbum());
        textViewTitle.setText(currentMusic.getTitle());
        Services.loadImage(this, currentMusic.getImage(), imageViewMusic);
        switchFav.setChecked(FavoriteRepository.getInstance(getApplicationContext()).isFavorite(currentMusic));
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
                    player.setDataSource(getApplicationContext(), Uri.parse(currentMusic.getPreview())); // url de preview de la musique
                    player.prepare(); // commence à DL la musique sur le tel
                    player.start(); // lance la musique
                } catch (IOException e) { // erreur de l'url de la musique
                    e.printStackTrace();
                }
            }else{
                buttonPlay.setText("PLAY");
                player.stop();
            }
        }else{ // si je ne suis pas sur buttoinLink ou buttonPlay, je suis sur le bouton switch
            if(FavoriteRepository.getInstance(getApplicationContext()).isFavorite(currentMusic)){ // on recupere l'instance dans faveoriterepository et on utilise la fonction isfavorite
                FavoriteRepository.getInstance(getApplicationContext()).remove(currentMusic);
                switchFav.setChecked(false);
            }else{
                FavoriteRepository.getInstance(getApplicationContext()).add(currentMusic); // si le titre n'etais pas dans les favoris, je l'ajoute à la liste des favoris
                switchFav.setChecked(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player.stop();
    }
}
