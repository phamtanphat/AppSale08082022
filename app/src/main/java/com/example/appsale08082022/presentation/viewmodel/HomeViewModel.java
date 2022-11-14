package com.example.appsale08082022.presentation.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appsale08082022.data.model.AppResource;
import com.example.appsale08082022.data.model.Product;
import com.example.appsale08082022.data.remote.dto.ProductDTO;
import com.example.appsale08082022.data.repository.ProductRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pphat on 11/14/2022.
 */
public class HomeViewModel extends ViewModel {
    private MutableLiveData<AppResource<List<Product>>> listProducts = new MutableLiveData<>();
    private ProductRepository repository;

    public HomeViewModel(Context context) {
        repository = new ProductRepository(context);
    }

    public LiveData<AppResource<List<Product>>> getListProducts() {
        return listProducts;
    }

    public void fetchProducts() {
        listProducts.setValue(new AppResource.Loading(null));
        Call<AppResource<List<ProductDTO>>> callProducts = repository.getProducts();
        callProducts.enqueue(new Callback<AppResource<List<ProductDTO>>>() {
            @Override
            public void onResponse(Call<AppResource<List<ProductDTO>>> call, Response<AppResource<List<ProductDTO>>> response) {
                if (response.isSuccessful()) {
                    List<ProductDTO> listProductDTO = response.body().data;
                    List<Product> listProduct = new ArrayList<>();
                    for (ProductDTO productDTO : listProductDTO) {
                        listProduct.add(new Product(
                                productDTO.getId(),
                                productDTO.getName(),
                                productDTO.getAddress(),
                                productDTO.getPrice(),
                                productDTO.getImg(),
                                productDTO.getQuantity(),
                                productDTO.getGallery())
                        );
                    }
                    listProducts.setValue(new AppResource.Success<>(listProduct));
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        listProducts.setValue(new AppResource.Error<>(message));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AppResource<List<ProductDTO>>> call, Throwable t) {
                listProducts.setValue(new AppResource.Error<>(t.getMessage()));
            }
        });
    }
}
