package com.example.appsale08082022.data.repository;

import android.content.Context;

import com.example.appsale08082022.data.model.AppResource;
import com.example.appsale08082022.data.remote.ApiService;
import com.example.appsale08082022.data.remote.RetrofitClient;
import com.example.appsale08082022.data.remote.dto.CartDTO;

import retrofit2.Call;

/**
 * Created by pphat on 11/16/2022.
 */
public class CartRepository {
    private ApiService apiService;

    public CartRepository(Context context) {
        apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public Call<AppResource<CartDTO>> fetchCart() {
        return apiService.getCart();
    }
}
