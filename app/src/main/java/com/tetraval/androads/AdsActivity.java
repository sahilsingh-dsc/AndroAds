package com.tetraval.androads;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;

import java.util.Objects;

import static com.tetraval.androads.Utils.Constant.DB_CONST;

public class AdsActivity extends AppCompatActivity {

    ExoVideoView videoView;
    TextView MarqueeText;
    ImageView imgMedia;
    DatabaseReference mediaRef, textRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ads);

        videoView = findViewById(R.id.videoView);
        imgMedia = findViewById(R.id.imgMedia);
        MarqueeText = findViewById(R.id.MarqueeText);
        MarqueeText.setSelected(true);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("TV", 0);
        if (preferences.getString("tv_id", "0").equals("0")){

        }else {
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }

        dbHelper();
    }

    private void dbHelper(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("TV", 0);
        mediaRef = FirebaseDatabase.getInstance().getReference(DB_CONST);
        mediaRef.child(preferences.getString("tv_id", "0")).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String MEDIA_TYPE = Objects.requireNonNull(dataSnapshot.child("ad_media_type").getValue()).toString();
                String MEDIA_URL = Objects.requireNonNull(dataSnapshot.child("ad_media_url").getValue()).toString();
                serveMedia(MEDIA_TYPE, MEDIA_URL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        textRef = FirebaseDatabase.getInstance().getReference(DB_CONST);
        textRef.child(preferences.getString("tv_id", "0")).child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String TEXT_STATUS = Objects.requireNonNull(dataSnapshot.child("ad_media_text_status").getValue()).toString();
                String TEXT_TO_SHOW = Objects.requireNonNull(dataSnapshot.child("ad_media_text").getValue()).toString();
                serveTextMedia(TEXT_STATUS, TEXT_TO_SHOW);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void serveMedia(String MEDIA_TYPE, String MEDIA_URL){
        if (MEDIA_TYPE.equals("1")){
            imgMedia.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            Glide.with(this).load(MEDIA_URL).into(imgMedia);
        } else if (MEDIA_TYPE.equals("2")){
            imgMedia.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            SimpleMediaSource mediaSource = new SimpleMediaSource(MEDIA_URL);
            videoView.play(mediaSource);
        }
    }

    private void serveTextMedia(String TEXT_STATUS, String TEXT_TO_SHOW){
        if (TEXT_STATUS.equals("1")){
            MarqueeText.setVisibility(View.VISIBLE);
            MarqueeText.setText(TEXT_TO_SHOW);
        } else if (TEXT_STATUS.equals("0")){
            MarqueeText.setVisibility(View.GONE);
        }
    }
}