package edu.northeastern.numad22fa_team12.model;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IPlaceholder {
    @GET("api/v1/products.json")
    Call<List<PostModel>>  getPostModels();


    @GET("api/v1/products.json")
    Call<List<PostModel>>  getPostsWithQuery(@Query("brand") String brand);

    @GET("api/v1/products.json")
    Call<List<PostModel>>  getPostsWithQueryMultipleParams(@Query("brand") String brand,
                                                           @Query("product_type") String productType);
}
