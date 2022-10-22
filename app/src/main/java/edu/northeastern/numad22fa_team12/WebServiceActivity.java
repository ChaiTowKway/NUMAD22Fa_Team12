package edu.northeastern.numad22fa_team12;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import edu.northeastern.numad22fa_team12.model.IPlaceholder;
import edu.northeastern.numad22fa_team12.model.PostModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebServiceActivity extends AppCompatActivity {
    private Button loadDataBtn, searchButton;
    private Spinner initialSpinner, brandSpinner, typeSpinner;

    private static final String TAG = "WebServiceActivity";
    private Retrofit retrofit;
    private IPlaceholder api;
    private String initialSelected = "M"; //placeholder, should be replaced by user input
    private String brandSelected = "maybelline";  //placeholder, should be replaced by user input
    private String productTypeSelected = "EYESHADOW";  //placeholder, should be replaced by user input
    private List<String> allInitials;
    private Hashtable<String, List<String>> allBrandsAndInitial;
    private List<String> allProductTypes;
    private List<String> allProducts;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_service);

        loadDataBtn = findViewById(R.id.loadingDataBtn);
        searchButton = findViewById(R.id.search_button);
        initialSpinner = (Spinner) findViewById(R.id.brandInitialSpinner);
        brandSpinner = (Spinner) findViewById(R.id.brandSpinner);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        allInitials = new ArrayList<>();
        allBrandsAndInitial = new Hashtable<>();
        allProductTypes = new ArrayList<>();
        allProducts = new ArrayList<>();
        progressBar.setVisibility(View.GONE);
        // load brand initials and brands (display loading animation while loading?)
        loadDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                getPosts();

            }
        });


        // search button get the products list and pass it to the recyclerView
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postWithQMultipleParams(brandSelected, productTypeSelected);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("https://makeup-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(IPlaceholder.class);
    }


    private void setSpinner() {
        //set nested spinner
        ArrayAdapter<String> initialArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, allInitials);
        // not sure if this setDropDownViewResource is needed or no, leave it for now
        initialArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        initialSpinner.setAdapter(initialArrayAdapter);
        initialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initialSelected = initialSpinner.getSelectedItem().toString();
                final List<String> brands = allBrandsAndInitial.get(initialSelected);
                ArrayAdapter<String> brandArrayAdapter = new ArrayAdapter<>(
                        WebServiceActivity.this, android.R.layout.simple_spinner_dropdown_item, brands);
                brandArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(brandArrayAdapter);
                brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int j, long l) {
                        brandSelected = brandSpinner.getSelectedItem().toString();
                        postWithQ(brandSelected);
                        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<>(
                                WebServiceActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, allProductTypes);
                        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        typeSpinner.setAdapter(typeArrayAdapter);
                        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int k, long l) {
                                productTypeSelected = typeSpinner.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void getPosts() {
        // to execute the call
        //api:: https://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline
        Call<List<PostModel>> call = api.getPostModels();

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
                    String curBrand = post.getBrand();
                    if (curBrand != null) {
                        char curBrandInitial = Character.toUpperCase(curBrand.charAt(0));
                        String curBrandInitialStr = Character.toString(curBrandInitial);
                        if (!allBrandsAndInitial.containsKey(curBrandInitialStr)) {
                            allBrandsAndInitial.put(curBrandInitialStr, new ArrayList<>());
                        }
                        if (!allInitials.contains(curBrandInitialStr)) {
                            allInitials.add(curBrandInitialStr);
                        }
                        if (!allBrandsAndInitial.get(curBrandInitialStr).contains(curBrand)) {
                            allBrandsAndInitial.get(curBrandInitialStr).add(curBrand);
                        }
                    }
                    String curType = post.getProductType();
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
                            .append("\n");
                    Log.d(TAG, str.toString());
                }
                Collections.sort(allInitials);
                setSpinner();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
                // this gets called when url is wrong and therefore calls can't be made OR processing the request goes wrong.
                Log.d(TAG, "Call failed!" + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }

        });

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
                    String curProductTypes = post.getProductType().toUpperCase();
                    if (!allProductTypes.contains(curProductTypes)) allProductTypes.add(curProductTypes);
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
                Collections.sort(allProductTypes);
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
                    allProducts.add(post.getName());
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