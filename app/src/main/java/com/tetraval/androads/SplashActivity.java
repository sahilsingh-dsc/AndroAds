package com.tetraval.androads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SPLASH";
    Bitmap bitmap;
    ImageView imgSplashLogo;
    QRGEncoder qrgEncoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        imgSplashLogo = findViewById(R.id.imgSplashLogo);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("TV", 0);

        if (!preferences.getString("tv_id", "0").equals("0")){
            DatabaseReference tvRef = FirebaseDatabase.getInstance().getReference("TVS_ID");
            String tv_id = tvRef.push().getKey();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("tv_id", tv_id);
            editor.apply();

            if (tv_id.length() > 0) {
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;

                qrgEncoder = new QRGEncoder(
                        tv_id, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                try {
                    bitmap = qrgEncoder.encodeAsBitmap();
                    imgSplashLogo.setImageBitmap(bitmap);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=new Intent(SplashActivity.this,
                                    AdsActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 10000);

                } catch (WriterException e) {
                    Log.v(TAG, e.toString());
                }
            } else {
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        }else {


            if (preferences.getString("tv_id", "0").length() > 0) {
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;

                qrgEncoder = new QRGEncoder(
                        preferences.getString("tv_id", "0"), null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                try {
                    bitmap = qrgEncoder.encodeAsBitmap();
                    imgSplashLogo.setImageBitmap(bitmap);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=new Intent(SplashActivity.this,
                                    AdsActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 60000);

                } catch (WriterException e) {
                    Log.v(TAG, e.toString());
                }
            } else {
                Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
