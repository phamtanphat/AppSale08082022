package com.example.appsale08082022.presentation.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import com.example.appsale08082022.R;
import com.example.appsale08082022.common.AppConstant;
import com.example.appsale08082022.data.local.AppCache;
import com.example.appsale08082022.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding splashBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());

        splashBinding.splashView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                AppCache appCache = AppCache.getInstance(SplashActivity.this);
                String token = appCache.getDataString(AppConstant.KEY_TOKEN);
                Intent intent;
                if (token == null) {
                    intent = new Intent(SplashActivity.this, SignInActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
