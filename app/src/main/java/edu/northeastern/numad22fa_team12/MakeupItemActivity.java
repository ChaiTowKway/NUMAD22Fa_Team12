package edu.northeastern.numad22fa_team12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class MakeupItemActivity extends AppCompatActivity {
    private String price;
    private String image_link;
    private String name;
    private String productType;
    private String description;

    private ImageView productImage;
    private TextView nameTV;
    private TextView categoryTV;
    private TextView priceTV;
    private TextView descriptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeup_item);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        // allProductInfo = {name, category, price, description, imageLink}
        // must follow this order to get the info
        Bundle b = this.getIntent().getExtras();
        ArrayList<String> allProductInfo = b.getStringArrayList("allProductInfo");

        name = "Name: " + allProductInfo.get(0);
        productType = "Category: " + allProductInfo.get(1);
        price = "Price: $" + allProductInfo.get(2);
        description = "Description: \n" + allProductInfo.get(3);
        description = description.replace("\n", "");
        Log.i("Description", description);
        image_link = allProductInfo.get(4);

        productImage = findViewById(R.id.product_image);
        nameTV = findViewById(R.id.product_name);
        categoryTV = findViewById(R.id.category_name);
        priceTV = findViewById(R.id.price);
        descriptionTV = findViewById(R.id.description);
        descriptionTV.setMovementMethod(new ScrollingMovementMethod());

        nameTV.setText(name);
        categoryTV.setText(productType);
        priceTV.setText(price);
        descriptionTV.setText(description);

//        DownloadImageTask loadImage = new DownloadImageTask(productImage, "https://d3t32hsnjxo7q6.cloudfront.net/i/991799d3e70b8856686979f8ff6dcfe0_ra,w158,h184_pa,w158,h184.png");
        DownloadImageTask loadImage = new DownloadImageTask(productImage, image_link);
        new Thread(loadImage).start();
    }
}

