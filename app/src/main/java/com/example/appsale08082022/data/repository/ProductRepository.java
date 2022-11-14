package com.example.appsale08082022.data.repository;

import android.content.Context;

import com.example.appsale08082022.data.model.AppResource;
import com.example.appsale08082022.data.remote.ApiService;
import com.example.appsale08082022.data.remote.RetrofitClient;
import com.example.appsale08082022.data.remote.dto.ProductDTO;
import com.example.appsale08082022.data.remote.dto.UserDTO;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * Created by pphat on 11/14/2022.
 */
public class ProductRepository {
    private ApiService apiService;

    public ProductRepository(Context context) {
        apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public Call<AppResource<List<ProductDTO>>> getProducts() {
        return apiService.getProducts();
    }
}
