package edu.northeastern.numad22fa_team12;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

//       example:  new DownloadImageTask((ImageView) productImage)
//                      .execute("https://d3t32hsnjxo7q6.cloudfront.net/i/991799d3e70b8856686979f8ff6dcfe0_ra,w158,h184_pa,w158,h184.png");
class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView productImage;

    public DownloadImageTask(ImageView productImage) {
        this.productImage = productImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bits = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bits = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bits;
    }


    protected void onPostExecute(Bitmap result) {
        productImage.setImageBitmap(result);
    }
}
