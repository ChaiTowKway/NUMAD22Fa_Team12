package edu.northeastern.numad22fa_team12;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;

public class MakeupItemActivity extends AppCompatActivity {

    private ImageView productImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeup_item);


        productImage = findViewById(R.id.product_image);

        new DownloadImageTask((ImageView) productImage)
                .execute("https://d3t32hsnjxo7q6.cloudfront.net/i/991799d3e70b8856686979f8ff6dcfe0_ra,w158,h184_pa,w158,h184.png");

    }
}

