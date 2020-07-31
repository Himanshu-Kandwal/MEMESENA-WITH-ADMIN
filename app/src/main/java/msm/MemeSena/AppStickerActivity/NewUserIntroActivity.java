package msm.MemeSena.AppStickerActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import msm.MemeSena.R;

public class NewUserIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setButtonBackVisible(false);
        addSlide(new SimpleSlide.Builder()
                .title("Welcome to Sticker Maker for WhatsApp - WAStickerApps")
                .description("Make your own WhatsApp sticker with Sticker Maker for WhatsApp - the amazing WhatsApp sticker maker app.")
                .image(R.drawable.appicon_small)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorAccent)
                .scrollable(false)
                .build());

      /*  if(!checkIfBatteryOptimizationIgnored() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            addSlide(new SimpleSlide.Builder()
                    .title("We've recognized our app is optimized by Android's Doze system.")
                    .description("In order for the app to work correctly, please disable the optimization for \"StickerMaker\" after clicking the button.")
                    .background(R.color.colorAccent)
                    .scrollable(false)
                    .buttonCtaLabel("Let's Go")
                    .buttonCtaClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                            startActivityForResult(intent, 4113);
                        }
                    })
                    .build());
        }*/

        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            addSlide(new SimpleSlide.Builder()
                    .title("We need to save and fetch your stickers!")
                    .description("In order for the app to work correctly, please allow the write and read storage permissions.")
                    .background(R.color.colorPrimary)
                    .backgroundDark(R.color.colorAccent)
                    .scrollable(false)
                    .buttonCtaLabel("Grant Permissions")
                    .buttonCtaClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            verifyStoragePermissions(NewUserIntroActivity.this);
                        }
                    })
                    .build());
        }

        addSlide(new SimpleSlide.Builder()
                .title("Everything is set!")
                .description("You can start creating your sticker packs!\n\n" +
                        "Please notice! Sticker packs created or loaded with the app require the app being installed on device.\n\n" +
                        "Uninstalling the app will cause of removing the sticker packs from your WhatsApp client " +
                        "and will be lost if they weren't shared.")
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorAccent)
                .scrollable(false)
                .build());

        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i > 0 && i != getSlides().size() - 1) {
                    setNavigation(false, true);
                } else {
                    setNavigation(true, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(requestCode == 4113){
            if(checkIfBatteryOptimizationIgnored()){
                setNavigation(true, false);
                nextSlide();
            }
        }*/
    }

    private boolean checkIfBatteryOptimizationIgnored() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            return pm.isIgnoringBatteryOptimizations(packageName);
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void setNavigation(boolean forward, boolean backward) {
        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int i) {
                return forward;
            }

            @Override
            public boolean canGoBackward(int i) {
                return backward;
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this)
                            .setPositiveButton("Let's Go", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    verifyStoragePermissions(NewUserIntroActivity.this);
                                }
                            })
                            .create();
                    alertDialog.setTitle("Notice!");
                    alertDialog.setMessage("Allowing storage permissions is crucial for the app to work. Please grant the permissions.");
                    alertDialog.show();
                } else {
                    setNavigation(true, false);
                    nextSlide();
                }
                break;
        }
    }
}
