package com.example.mydeezerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class FavoriteRepository implements IFavoriteRepository{

    private DatabaseManager dbm;
    private static FavoriteRepository instance;

    private FavoriteRepository(Context context){
        this.dbm= DatabaseManager.getInstance(context);
    }

    public static FavoriteRepository getInstance(Context context){
        if(instance==null){
            instance= new FavoriteRepository(context);
        }
        return instance;
    }

    @Override
    public boolean add(Music music) { // insertion dans la DB SQLlite
        if(isFavorite(music)) return false;
        ContentValues values = new ContentValues();
        values.put("id", music.getId());
        values.put("title", music.getTitle());
        values.put("artist", music.getArtist());
        values.put("album", music.getAlbum());
        values.put("sampleUrl", music.getPreview());
        values.put("link", music.getLink());
        values.put("coverUrl", music.getImage());
        long line= dbm.getWritableDatabase().insert("favorite", null, values);
        return line != 0;
    }

    @Override
    public boolean remove(Music music) {
        String[] identifier = {String.valueOf(music.getId())};
        long line= dbm.getWritableDatabase().delete("favorite", "id=?", identifier);
        return line != 0;
    }

    @Override
    public boolean isFavorite(Music music) {
        String[] identifier = {String.valueOf(music.getId())};
        Cursor c= dbm.getReadableDatabase().rawQuery("select * from favorite where id=?", identifier);
        return c.getCount()>0;
    }

    @Override
    public ArrayList<Music> getAll() {
        ArrayList<Music> musics = new ArrayList<Music>();
        Cursor c= dbm.getReadableDatabase().rawQuery("select * from favorite ", null);
        c.moveToFirst();
        while (!c.isAfterLast()){
            Music music= new Music();
            music.setId(c.getInt(0));
            music.setTitle(c.getString(1));
            music.setArtist(c.getString(2));
            music.setAlbum(c.getString(3));
            music.setPreview(c.getString(4));
            music.setLink(c.getString(5));
            music.setImage(c.getString(6));
            musics.add(music);
            c.moveToNext();
        }
        c.close();
        return musics;
    }
}
