package edu.northeastern.numad22fa_team12;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
//example:DownloadImageTask loadImage = new DownloadImageTask(productImage, "https://d3t32hsnjxo7q6.cloudfront.net/i/991799d3e70b8856686979f8ff6dcfe0_ra,w158,h184_pa,w158,h184.png");
//        new Thread(loadImage).start();
class DownloadImageTask implements Runnable {
    ImageView productImage;
    String address = "";
    private Handler textHandler = new Handler();

    public DownloadImageTask(ImageView productImage, String addr) {
        this.productImage = productImage;
        this.address = addr;
    }


    @Override
    public void run() {
        Bitmap bits = null;
        try {
            InputStream in = new java.net.URL(address).openStream();
            Log.i("THREAD", "i AM HERE");
            bits = BitmapFactory.decodeStream(in);
            Bitmap finalBits = bits;
            textHandler.post(new Runnable() {
                @Override
                public void run() {
                    productImage.setImageBitmap(finalBits);
                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

}
