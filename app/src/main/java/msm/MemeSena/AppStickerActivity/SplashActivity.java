package msm.MemeSena.AppStickerActivity;

import android.content.Intent;
import android.os.Bundle;

import msm.MemeSena.Base.BaseActivity;
import msm.MemeSena.R;

import android.os.Handler;
import android.view.WindowManager;
import android.support.annotation.Nullable;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_entry);
        overridePendingTransition(0, 0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        }, 2000);
    }
}