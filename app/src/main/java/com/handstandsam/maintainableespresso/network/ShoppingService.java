package com.handstandsam.maintainableespresso.network;

import com.handstandsam.maintainableespresso.models.Category;
import com.handstandsam.maintainableespresso.models.Item;
import com.handstandsam.maintainableespresso.models.User;
import com.handstandsam.maintainableespresso.network.model.LoginRequest;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ShoppingService {

    @POST("login")
    Single<User> login(@Body LoginRequest loginRequest);

    @GET("categories")
    Single<List<Category>> getCategories();

    @GET("category/{categoryName}/items")
    Single<List<Item>> getItemsForCategory(@Path("categoryName") String categoryName);
}
