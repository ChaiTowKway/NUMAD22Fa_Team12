package edu.northeastern.numad22fa_team12;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import java.io.InputStream;
import java.util.ArrayList;

public class MakeupItemActivity extends AppCompatActivity {

    private ImageView productImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeup_item);

        // allProductInfo = {name, category, price, description, imageLink}
        // must follow this order to get the info
        Bundle b = this.getIntent().getExtras();
        ArrayList<String> allProductInfo = b.getStringArrayList("allProductInfo");


        productImage = findViewById(R.id.product_image);


        DownloadImageTask loadImage = new DownloadImageTask(productImage, "https://d3t32hsnjxo7q6.cloudfront.net/i/991799d3e70b8856686979f8ff6dcfe0_ra,w158,h184_pa,w158,h184.png");
        new Thread(loadImage).start();
    }
}

