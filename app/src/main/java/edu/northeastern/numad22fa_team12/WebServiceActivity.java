package edu.northeastern.numad22fa_team12;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;

import edu.northeastern.numad22fa_team12.model.IPlaceholder;
import edu.northeastern.numad22fa_team12.model.PostModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceActivity extends AppCompatActivity {
    Button searchButton;
    EditText inputBrand;
    TextView textView;

    private static final String TAG = "WebServiceActivity";
    private Retrofit retrofit;
    private IPlaceholder api;
    private String brandSelected = "maybelline";  //placeholder, should be replaced by user input
    private String productTypeSelected = "EYESHADOW";  //placeholder, should be replaced by user input
    private HashSet<String> allProductTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

        searchButton = findViewById(R.id.search_button);
        inputBrand = findViewById(R.id.input_brand);

        allProductTypes = new HashSet<>();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postWithQ(brandSelected);
//                postWithQMultipleParams(brandSelected, productTypeSelected);;
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://makeup-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IPlaceholder.class);
    }

    private void postWithQ(String brandSelected){
        // to execute the call
        //api:: https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline
        Call<List<PostModel>> call = api.getPostsWithQuery(brandSelected);
        //call.execute() runs on the current thread, which is main at the momement. This will crash
        // use Retrofit's method enque. This will automaically push the network call to background thread
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                //This gets called when atleast the call reaches a server and there was a response BUT 404 or any legitimate error code from the server, also calls this
                // check response code is between 200-300 and API was found

                if(!response.isSuccessful()){
                    Log.d(TAG, "Call failed!" + response.code());
                    return;
                }

                Log.d(TAG, "Call Successed!");
                List<PostModel> postModels = response.body();
                for(PostModel post : postModels){
                    allProductTypes.add(post.getProductType().toUpperCase());
                    StringBuffer  str = new StringBuffer();
                    str.append("Code:: ")
                            .append(response.code())
                            .append("\n")
                            .append("Brand : ")
                            .append(post.getBrand())
                            .append("\n")
                            .append("Name: ")
                            .append(post.getName())
                            .append("\n")
                            .append("Product Type: ")
                            .append(post.getProductType())
                            .append("\n")
                            .append("Price :")
                            .append(post.getPrice())
                            .append("\n")
                            .append("Image Link: ")
                            .append(post.getImage_link())
                            .append("\n")
                            .append("Description : ")
                            .append(post.getDescription())
                            .append("\n");

                    Log.d(TAG, str.toString());
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                // this gets called when url is wrong and therefore calls can't be made OR processing the request goes wrong.
                Log.d(TAG, "Call failed!" + t.getMessage());
            }
        });
    }

    private void postWithQMultipleParams(String brandSelected, String productTypeSelected){
        // to execute the call
        // api::  https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline&product_type=EYESHADOW
        Call<List<PostModel>> call = api.getPostsWithQueryMultipleParams(brandSelected, productTypeSelected);
        //call.execute() runs on the current thread, which is main at the momement. This will crash
        // use Retrofit's method enque. This will automaically push the network call to background thread
        call.enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                //This gets called when atleast the call reaches a server and there was a response BUT 404 or any legitimate error code from the server, also calls this
                // check response code is between 200-300 and API was found

                if(!response.isSuccessful()){
                    Log.d(TAG, "Call failed!" + response.code());
                    return;
                }

                Log.d(TAG, "Call Successed!");
                List<PostModel> postModels = response.body();
                for(PostModel post : postModels){
                    StringBuffer  str = new StringBuffer();
                    str.append("Code:: ")
                            .append(response.code())
                            .append("\n")
                            .append("Brand : ")
                            .append(post.getBrand())
                            .append("\n")
                            .append("Name: ")
                            .append(post.getName())
                            .append("\n")
                            .append("Product Type: ")
                            .append(post.getProductType())
                            .append("\n")
                            .append("Price :")
                            .append(post.getPrice())
                            .append("\n")
                            .append("Image Link: ")
                            .append(post.getImage_link())
                            .append("\n")
                            .append("Description : ")
                            .append(post.getDescription())
                            .append("\n");
                    Log.d(TAG, str.toString());
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                // this gets called when url is wrong and therefore calls can't be made OR processing the request goes wrong.
                Log.d(TAG, "Call failed!" + t.getMessage());
            }
        });
    }

}