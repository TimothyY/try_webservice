package com.example.try_webservice;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    TextView tvTitle, tvAlbum;
    Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCtx = this;

        tvTitle = findViewById(R.id.tvTitleXX);
        tvAlbum = findViewById(R.id.tvArtistXX);

        DemoTask task1 = new DemoTask();
        task1.execute();

    }

    private String readStream(InputStream is){
        try{
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int i = is.read();
            while(i!=-1){
                buffer.write(i);
                i= is.read();
            }
            return buffer.toString();
        }catch(Exception e){

        }
        return "";
    }

    private class DemoTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String result="";
            try{
                URL url = new URL("https://itunes.apple.com/search?term=beyonce&entity=musicVideo");
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConn.getInputStream());
                result = readStream(in);
                urlConn.disconnect();
            }catch(IOException e){
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.v("server response", result);

            ITunesParser iTunesParser = new ITunesParser();
            ArrayList<Song> songs;
            try {
                songs = iTunesParser.parseJsonToSongs(mCtx, new JSONObject(result));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            String titleXX  = songs.get(10).title;
            String artistXX = songs.get(10).artist;

            tvTitle.setText(titleXX);
            tvAlbum.setText(artistXX);
        }
    }
}