package edu.northeastern.numad22fa_team12.model;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPlaceholder {
    @GET("api/v1/products.json")    // "post/" is the relative Url of your api. We define base Url at a common place later
    Call<List<PostModel>>  getPostModels();


    @GET("api/v1/products.json")
    Call<List<PostModel>>  getPostsWithQuery(@Query("brand") String brand);
}
