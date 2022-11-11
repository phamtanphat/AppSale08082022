package com.example.appsale08082022.presentation.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.appsale08082022.R;
import com.example.appsale08082022.data.model.AppResource;
import com.example.appsale08082022.data.model.User;
import com.example.appsale08082022.databinding.ActivitySignInBinding;
import com.example.appsale08082022.presentation.viewmodel.SignInViewModel;

public class SignInActivity extends AppCompatActivity {

    SignInViewModel signInViewModel;
    EditText edtEmail, edtPassword;
    LinearLayout linearSignIn, loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new SignInViewModel(SignInActivity.this);
            }
        }).get(SignInViewModel.class);

        initData();
        observer();
        event();
    }

    private void initData() {
        loadingView = findViewById(R.id.layout_loading);
        linearSignIn = findViewById(R.id.sign_in);
        edtEmail = findViewById(R.id.textEditEmail);
        edtPassword = findViewById(R.id.textEditPassword);
    }

    private void observer() {
        signInViewModel.getUserResource().observe(this, new Observer<AppResource<User>>() {
            @Override
            public void onChanged(AppResource<User> userAppResource) {
                switch (userAppResource.status) {
                    case ERROR:
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, userAppResource.message, Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        loadingView.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        loadingView.setVisibility(View.GONE);
                        Toast.makeText(SignInActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void event() {

        linearSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Bạn chưa nhập đủ thông tin!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                signInViewModel.signIn(email, password);
//                Toast.makeText(SignInActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
