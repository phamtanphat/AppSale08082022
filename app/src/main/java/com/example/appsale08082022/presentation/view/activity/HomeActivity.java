package com.example.appsale08082022.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appsale08082022.R;
import com.example.appsale08082022.data.model.AppResource;
import com.example.appsale08082022.data.model.Cart;
import com.example.appsale08082022.data.model.Product;
import com.example.appsale08082022.databinding.ActivityHomeBinding;
import com.example.appsale08082022.presentation.view.adapter.ProductAdapter;
import com.example.appsale08082022.presentation.viewmodel.HomeViewModel;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding homeBinding;
    ProductAdapter productAdapter;
    HomeViewModel homeViewModel;
    TextView tvCountCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

        homeViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new HomeViewModel(HomeActivity.this);
            }
        }).get(HomeViewModel.class);

        initData();
        observerData();
        event();
    }

    private void initData() {
        productAdapter = new ProductAdapter();
        homeBinding.recyclerViewProduct.setAdapter(productAdapter);
        homeBinding.recyclerViewProduct.hasFixedSize();

        // toolbar
        setSupportActionBar(homeBinding.toolbarHome);

        // Call Api
        homeViewModel.fetchCart();
        homeViewModel.fetchProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        final MenuItem menuItem = menu.findItem(R.id.item_menu_cart);
        View actionView = menuItem.getActionView();
        tvCountCart = actionView.findViewById(R.id.text_cart_badge);
        setupBadge(0);
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        return true;
    }

    private void setupBadge(int quantities) {
        if (quantities == 0) {
            tvCountCart.setVisibility(View.GONE);
        } else {
            tvCountCart.setVisibility(View.VISIBLE);
            tvCountCart.setText(String.valueOf(Math.min(quantities, 99)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_cart:
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void event() {
        homeViewModel.getListProducts().observe(this, new Observer<AppResource<List<Product>>>() {
            @Override
            public void onChanged(AppResource<List<Product>> listAppResource) {
                switch (listAppResource.status) {
                    case ERROR:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        Toast.makeText(HomeActivity.this, listAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        productAdapter.updateListProduct(listAppResource.data);
                        break;
                }
            }
        });

        homeViewModel.getCart().observe(this, new Observer<AppResource<Cart>>() {
            @Override
            public void onChanged(AppResource<Cart> cartAppResource) {
                switch (cartAppResource.status) {
                    case ERROR:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        Toast.makeText(HomeActivity.this, cartAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        homeBinding.layoutLoading.layoutLoading.setVisibility(View.GONE);
                        int countBadge = 0;
                        for (int i = 0; i < cartAppResource.data.getProducts().size(); i++) {
                            countBadge += cartAppResource.data.getProducts().get(i).getQuantity();
                        }
                        setupBadge(countBadge);
                        break;
                }
            }
        });

        productAdapter.setOnItemClickFood(new ProductAdapter.OnItemClickProduct() {
            @Override
            public void onClick(int position) {
                homeViewModel.addCart(productAdapter.getListProducts().get(position).getId());
            }
        });
    }

    private void observerData() {

    }
}
